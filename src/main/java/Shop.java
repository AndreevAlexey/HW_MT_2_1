import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.*;


public class Shop {
    private final int RECEIVE_TIME = 2000;
    private final int SELL_TIME = 2000;
    List<Auto> cars = new ArrayList<>();
    Seller seller = new Seller(this);
    private int dealsCnt = 0;
    final ReentrantLock lock = new ReentrantLock(true);
    final Condition condition = lock.newCondition();

    public Shop() {
    }

    public List<Auto> getCars() {
        return cars;
    }

    public int getDealsCnt() {
        return dealsCnt;
    }
    // продажа авто магазином
    public Auto sellAuto() {
        Auto car = null;
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " зашел в автосалон");
            Thread.sleep(SELL_TIME);
            while(cars.size() == 0) {
                System.out.println("Авто нет в наличии");
                condition.await();
            }
            Thread.sleep(SELL_TIME);
            car = cars.remove(0);
            System.out.println(Thread.currentThread().getName() + " купил " + car.toString());
            dealsCnt++;
            System.out.println("dealsCnt = "+dealsCnt);
        } catch (InterruptedException exp) {
            exp.printStackTrace();
        } finally {
            lock.unlock();
        }
        return car;
    }
    // выпуск авто производителем
    public void receiveAuto(Auto newAuto) {
        lock.lock();
        try {
            System.out.println("Производитель выпустил 1 авто " + newAuto.toString());
            cars.add(newAuto);
            Thread.sleep(RECEIVE_TIME);
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
