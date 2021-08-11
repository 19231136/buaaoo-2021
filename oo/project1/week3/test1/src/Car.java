public class Car extends Vehicle {
    private int maxSpeed;

    Car(int id, int price, int maxSpeed) {
        super(id,price);
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void run() {
        System.out.println("Wow, I can Run (maxSpeed:" + maxSpeed + ")!");
    }

    @Override
    public int getPrice() {
        System.out.println("price is: " + super.getPrice());
        return super.getPrice();
    }
}
