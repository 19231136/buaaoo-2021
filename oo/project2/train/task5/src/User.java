public class User implements Observer {
    private String username;

    User(String username) {
        this.username = username;
    }
    @Override
    public void update(String msg) {
        System.out.println(msg);
    }
}
