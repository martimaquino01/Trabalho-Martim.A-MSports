public class User {

    private final String name;
    private Double balance;
    private final boolean owner;
    private Inventory inventory;

    public User(String name, Double balance, boolean owner) {
        this.name = name;
        this.balance = balance;
        this.owner = owner;

        if (!owner) {
            this.inventory = new Inventory(); //customers save purchases here.
        }
    }

    public String getName() {
        return this.name;
    }

    public Double getBalance() {
        return this.balance;
    }

    public boolean isOwner() {
        return owner;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean canAfford(double amount) {
        return balance >= amount;
    }

    public void addBalance(double amount) {
        balance = balance + amount;
    }

    public void removeBalance(double amount) {
        balance = balance - amount;
    }
}
