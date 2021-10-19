public class ReceiveCar implements Runnable {
    private Auto car;
    private Shop shop;

    public ReceiveCar(Auto car, Shop shop) {
        this.car = car;
        this.shop = shop;
    }

    @Override
    public void run() {
        shop.receiveAuto(car);
    }
}
