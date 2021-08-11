import java.util.List;

public class Factory {
    public static Vehicle getNew(List<String> ops) {
        String type = ops.get(1);
        if ("Car".equals(type)) {
            int id = Integer.parseInt(ops.get(2));
            int price = Integer.parseInt(ops.get(3));
            int max_speed = Integer.parseInt(ops.get(4));
            return new Car(id,price,max_speed);
        } else if ("Sprinkler".equals(type)) {
            int id = Integer.parseInt(ops.get(2));
            int price = Integer.parseInt(ops.get(3));
            int volume = Integer.parseInt(ops.get(4));
            return new Sprinkler(id,price,volume);
        } else {
            int id = Integer.parseInt(ops.get(2));
            int price = Integer.parseInt(ops.get(3));
            int volume = Integer.parseInt(ops.get(4));
            return new Bus(id,price,volume);
        }
    }
}
