public class Factory {
    public Elevator create(String pattern,Dispatcher dispatcher,String id) {
        Elevator elevator;
        if (pattern.equals("Random")) {
            elevator = new RandomElevator(dispatcher,1,0,id);
        } else if (pattern.equals("Morning")) {
            elevator = new MorningElevator(dispatcher,1,0,id);
        } else {
            elevator = new NightElevator(dispatcher,1,0,id);
        }
        return elevator;
    }

    public void start(Elevator elevator,String pattern) {
        if (pattern.equals("Random")) {
            RandomElevator randomElevator = (RandomElevator) elevator;
            randomElevator.start();
        } else if (pattern.equals("Morning")) {
            MorningElevator morningElevator = (MorningElevator) elevator;
            morningElevator.start();
        } else {
            NightElevator nightElevator = (NightElevator) elevator;
            nightElevator.start();
        }
    }

}
