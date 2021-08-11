public class Factory {
    public Elevator create(String pattern, Dispatcher dispatcher,
                           String id, String type,Controller controller) {
        int max;
        int speed;
        if (type.equals("A")) {
            max = 8;
            speed = 600;
        } else if (type.equals("B")) {
            max = 6;
            speed = 400;
        } else {
            max = 4;
            speed = 200;
        }
        Elevator elevator;
        elevator = new RandomElevator(dispatcher,id,max,
                speed,type,(RandomController) controller);
        return elevator;
    }

    public void start(Elevator elevator,String pattern) {
        if (pattern.equals("Random")) {
            RandomElevator randomElevator = (RandomElevator) elevator;
            randomElevator.start();
        }
    }
}
