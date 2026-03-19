public class Store {

    private String storeName;
    private final User owner;
    private final Inventory storeInventory;
    private final User[] users = new User[100];
    private int userCount = 0;
    private int currentUserIndex = 0;
    private boolean ownerView = false; //true when the owner is operating the store

    public Store(String storeName, String ownerName, double ownerBalance) {
        this.storeName = storeName;
        this.owner = new User(ownerName, ownerBalance, true);
        this.storeInventory = new Inventory();
    }




    public void addUser(String name) {
        if (userCount >= users.length || name == null || name.trim().isEmpty()) {
            return;
        }
        users[userCount] = new User(name.trim(), 100.0, false);
        userCount = userCount + 1;
    }

    public void addClient(String name) {
        addUser(name);
    }

    public void addInitialProduct(String name, double price) {
        storeInventory.addProduct(name, 20, price);
    }

    public void showStoreView() {
        System.out.println();
        System.out.println("=== " + storeName + " ===");

        if (ownerView) {
            System.out.println("Current user: " + owner.getName() + " (Owner) (" + owner.getBalance() + " EUR)");
        } else {
            User current = users[currentUserIndex];
            System.out.println("Current user: " + current.getName() + " (Client) (" + current.getBalance() + " EUR)");
        }

        System.out.println("Products:");
        for (int i = 1; i <= storeInventory.getSize(); i++) { // iterate through the inventory
            System.out.println(i + " - " + storeInventory.getNameAt(i) + " x" + storeInventory.getQuantityAt(i) + " (" + storeInventory.getPriceAt(i) + " EUR)");
        }

        System.out.println("------");
        System.out.println("Actions:");

        if (ownerView) {
            System.out.println("  store - Show store view");
            System.out.println("  add - Add a product");
            System.out.println("  remove - Remove a product");
            System.out.println("  edit - Edit a product");
            System.out.println("  stock - Increase stock of a product");
            System.out.println("  rename - Change store name");
            System.out.println("  client - Switch to a client");
            System.out.println("  register - Add new client");
            System.out.println("  exit - Exit program");
        } else {
            System.out.println("  store - Show store view");
            System.out.println("  buy - Buy a product");
            System.out.println("  inv - List client inventory items");
            System.out.println("  return - Return a product that was bought");
            System.out.println("  client - Switch to another client");
            System.out.println("  owner - Switch to owner of store");
            System.out.println("  register - Add new client");
            System.out.println("  exit - Exit program");
        }
    }

    public boolean executeAction(String action, StoreInterface storeInterface) { 
        if (ownerView) {
            return executeOwnerAction(action, storeInterface);
        }
        return executeClientAction(action, storeInterface);
    }

    private boolean executeClientAction(String action, StoreInterface storeInterface) {
        switch (action) {
            case "store":
                return true;
            case "buy":
                return buy(storeInterface);
            case "inv":
                showCurrentClientInventory();
                return false;
            case "return":
                return returnProduct(storeInterface);
            case "client":
                return switchUser(storeInterface);
            case "owner":
                ownerView = true;
                System.out.println("Switched to store owner.");
                return true;
            case "register":
                registerUser(storeInterface);
                return false;
            default:
                System.out.println("Invalid action \"" + action + "\".");
                return false;
        }
    }

    private boolean executeOwnerAction(String action, StoreInterface storeInterface) {

        switch (action) {
            case "store":
                return true;
            case "add":
                return addProduct(storeInterface);
            case "remove":
                return removeProduct(storeInterface);
            case "edit":
                return editProduct(storeInterface);
            case "stock":
                return increaseStock(storeInterface);
            case "rename":
                return renameStore(storeInterface);
            case "client":
                return switchUser(storeInterface);
            case "register":
                registerUser(storeInterface);
                return false;
            default:
                System.out.println("Invalid action \"" + action + "\".");
                return false;
        }

        
    }

    private boolean buy(StoreInterface storeInterface) {

        //validate
        if (storeInventory.getSize() == 0) {
            System.out.println("There are no products in store ");
            return false;
        }

        int productIndex = storeInterface.readInt("Enter product index : ");
        if (!storeInventory.isValidIndex(productIndex)) {
            System.out.println("Invalid product index ");
            return false;
        }

        int quantity = storeInterface.readInt("Enter quantity: ");
        if (quantity <= 0) {
            System.out.println("Invalid quantity ");
            return false;
        }

        int stock = storeInventory.getQuantityAt(productIndex);
        String productName = storeInventory.getNameAt(productIndex);
        double price = storeInventory.getPriceAt(productIndex);
        double total = quantity * price; 

        if (stock < quantity) { 
            System.out.println("Not enough stock to buy " + quantity + " of " + productName + ".");
            return false;
        }

        User current = users[currentUserIndex];
        if (!current.canAfford(total)) { // avoids negative balance
            System.out.println("You don't have enough balance to buy " + quantity + " of \"" + productName + "\".");
            System.out.println("You need at least more " + (total - current.getBalance()) + " EUR.");
            return false;
        }

        storeInventory.decreaseQuantityAt(productIndex, quantity);
        current.removeBalance(total);
        owner.addBalance(total);
        current.getInventory().addProduct(productName, quantity, price);

        System.out.println("Successfully bought " + quantity + " of \"" + productName + "\".");
        return true;
    }

    private void showCurrentClientInventory() {
        User current = users[currentUserIndex];
        Inventory inv = current.getInventory();

        System.out.println(current.getName() + "'s inventory:");
        if (inv.getSize() == 0) {
            System.out.println("( empty )");
            return;
        }

        for (int i = 1; i <= inv.getSize(); i++) {
            System.out.println(i + " - " + inv.getNameAt(i) + " x" + inv.getQuantityAt(i) + " (" + inv.getPriceAt(i) + " EUR)");
        }
    }

    private boolean returnProduct(StoreInterface storeInterface) {
        User current = users[currentUserIndex];
        Inventory inv = current.getInventory();

        if (inv.getSize() == 0) {
            System.out.println("Your inventory is empty ");
            return false;
        }

        showCurrentClientInventory();

        int productIndex = storeInterface.readInt("Enter product index: ");
        if (!inv.isValidIndex(productIndex)) {
            System.out.println("Invalid product index.");
            return false;
        }

        String name = inv.getNameAt(productIndex);
        int quantity = inv.getQuantityAt(productIndex);
        double price = inv.getPriceAt(productIndex);
        double refund = quantity * price; //full refund due to the customer

        int storeProductIndex = storeInventory.findByName(name);
        if (storeProductIndex == -1) {
            System.out.println("Cannot return this product because it no longer exists in store ");
            return false;
        }

        if (owner.getBalance() < refund) { //the owner does not have sufficient funds to return the item
            System.out.println("Store owner does not have enough balance to refund this return.");
            return false;
        }

        storeInventory.increaseQuantityAt(storeProductIndex, quantity);
        owner.removeBalance(refund);
        current.addBalance(refund);
        inv.removeProductAt(productIndex);

        System.out.println("Successfully returned " + quantity + " of product \"" + name + "\".");
        return true;
    }

    private boolean switchUser(StoreInterface storeInterface) {
        if (userCount == 0) {
            System.out.println("There are no clients available.");
            return false;
        }

        System.out.println("Clients:");
        for (int i = 0; i < userCount; i++) {
            System.out.println((i + 1) + " - " + users[i].getName() + " (" + users[i].getBalance() + " EUR)");
        }

        int clientIndex = storeInterface.readInt("Enter client index: ");
        if (clientIndex < 1 || clientIndex > userCount) {
            System.out.println("Invalid client index.");
            return false;
        }

        currentUserIndex = clientIndex - 1;
        ownerView = false;
        System.out.println("Switched to client \"" + users[currentUserIndex].getName() + "\".");
        return true;
    }

    private void registerUser(StoreInterface storeInterface) {
        if (userCount >= users.length) {
            System.out.println("Cannot register more clients ");
            return;
        }

        String newClientName = storeInterface.readText("Enter new client name: ");
        if (newClientName.trim().isEmpty()) {
            System.out.println("Invalid client name");
            return;
        }

        addUser(newClientName);
        System.out.println("Client \"" + newClientName + "\" added successfully");
    }

    private boolean addProduct(StoreInterface storeInterface) {
        if (storeInventory.getSize() >= 100) {
            System.out.println("Store inventory is full");
            return false;
        }

        String newProductName = storeInterface.readText("Enter new product name: ");
        if (newProductName.trim().isEmpty()) {
            System.out.println("Invalid product name.");
            return false;
        }

        double newPrice = storeInterface.readDouble("Enter new product price: ");
        if (newPrice < 0) {
            System.out.println("Invalid product price");
            return false;
        }

        storeInventory.addProduct(newProductName, 20, newPrice);
        System.out.println("Product \"" + newProductName + "\" added successfully.");
        return true;
    }

    private boolean removeProduct(StoreInterface storeInterface) {
        int productIndex = storeInterface.readInt("Enter product index: ");
        if (!storeInventory.removeProductAt(productIndex)) {
            System.out.println("Invalid product index");
            return false;
        }

        System.out.println("Product removed successfully.");
        return true;
    }

    private boolean editProduct(StoreInterface storeInterface) {
        int productIndex = storeInterface.readInt("Enter product index: ");
        if (!storeInventory.isValidIndex(productIndex)) {
            System.out.println("Invalid product index.");
            return false;
        }

        String newProductName = storeInterface.readText("Enter new product name: ");
        if (newProductName.trim().isEmpty()) {
            System.out.println("Invalid product name.");
            return false;
        }

        double newProductPrice = storeInterface.readDouble("Enter new product price: ");
        if (newProductPrice < 0) {
            System.out.println("Invalid product price");
            return false;
        }

        storeInventory.setNameAt(productIndex, newProductName);
        storeInventory.setPriceAt(productIndex, newProductPrice);

        System.out.println("Product edited successfully.");
        return true;
    }

    private boolean increaseStock(StoreInterface storeInterface) {
        int productIndex = storeInterface.readInt("Enter product index: ");
        if (!storeInventory.isValidIndex(productIndex)) {
            System.out.println("Invalid product index");
            return false;
        }

        int stockToAdd = storeInterface.readInt("Enter how much stock to add: ");
        if (stockToAdd <= 0) {
            System.out.println("Invalid stock quantity");
            return false;
        }

        storeInventory.increaseQuantityAt(productIndex, stockToAdd);
        System.out.println("Increased stock of product by " + stockToAdd + ".");
        return true;
    }

    private boolean renameStore(StoreInterface storeInterface) {
        String newStoreName = storeInterface.readText("Enter new store name: ");
        if (newStoreName.trim().isEmpty()) {
            System.out.println("Invalid store name.");
            return false;
        }

        storeName = newStoreName;
        System.out.println("Store renamed to \"" + newStoreName + "\".");
        return true;
    }
}
