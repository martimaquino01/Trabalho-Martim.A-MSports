public class Client extends User {

    private Inventory inventory;

    public Client(String name) {
        super(name, 100.0);
        this.inventory = new Inventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getType() {
        return "Client";
    }
}
