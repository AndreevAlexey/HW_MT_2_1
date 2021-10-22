public class Main {
    // создание потоков покупателей
    public static Thread[] createCustomerThreads(int cnt, Shop shop) {
        Thread[] threads = new Thread[cnt];

        for(int i = 0; i < cnt; i++) {
            threads[i] = new Thread(null, shop::sellAuto, "Покупатель"+(i+1));
            threads[i].start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return threads;
    }
    // проверка завершения всех потоков покупателей
    public static boolean allThreadsStoped(Thread[] threads) {
        for(Thread thread : threads) {
            if (thread.getState() != Thread.State.TERMINATED) return false;
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        final int PROD_TIME = 5000;
        final int STOP_CNT = 10;
        final int cntCust = 4;
        Thread[] customers;
        final Shop shop = new Shop();
        Auto newCar = new Auto("BMW");
        ReceiveCar receiveCar = new ReceiveCar(newCar, shop);
        // создание потоков покупателей
        customers = createCustomerThreads(cntCust, shop);

        // начало работы
        while(true) {
            // все поток покупателей завершены
            if(allThreadsStoped(customers)) {
                // запускаем новые
                customers = createCustomerThreads(cntCust, shop);
            } else {
                // новый поток производетеля авто
                new Thread(null, receiveCar, "Производитель").start();
                Thread.sleep(PROD_TIME);
            }
            // проверка на выход из цикла
            if(shop.getDealsCnt() >= STOP_CNT) {
                for(Thread thread : customers) {
                    thread.interrupt();
                }
                break;
            }
        }
    }
}
