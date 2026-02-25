public class Owner extends User {

    public Owner(String name, double balance) {
        super(name, balance);
    }

    public String getType() {
        return "Owner";
    }
}
