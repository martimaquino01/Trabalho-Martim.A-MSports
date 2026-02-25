public class Store {

    private String name;
    private Owner owner;
    private Inventory inventory;
    private Client[] clients;
    private int clientCount;
    private static final int MAX_CLIENTS = 100;
    private User currentUser;

    public Store(String name, Owner owner) {
        this.name = name;
        this.owner = owner;
        this.inventory = new Inventory();
        this.clients = new Client[MAX_CLIENTS];
        this.clientCount = 0;
        this.currentUser = null;
    }

    public String getName() {
        return name;
    }

    public Owner getOwner() {
        return owner;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public int getClientCount() {
        return clientCount;
    }

    public Client getClient(int index) {
        if (index < 0 || index >= clientCount) {
            return null;
        }
        return clients[index];
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean addClient(Client client) {
        if (clientCount >= MAX_CLIENTS) { 
            return false;
        }
        clients[clientCount] = client;
        clientCount++;
        return true;
    }

    public boolean addProduct(Product product) {
        return inventory.addProduct(product);
    }

    public boolean removeProduct(int index) {
        return inventory.removeProduct(index);
    }

    public String buyProduct(Client client, int productIndex, int quantity) {
        Product product = inventory.getProduct(productIndex);

        if (product == null) {
            return "Invalid product index.";
        }

        if (product.getStock() < quantity) {
            return "Not enough stock to buy " + quantity + " of " + product.getName() + ".";
        }

        double totalCost = product.getPrice() * quantity;

        if (client.getBalance() < totalCost) {
            double needed = totalCost - client.getBalance();
            return "You don't have enough balance to buy " + quantity + " of \"" + product.getName() + "\".\nYou need at least more " + needed + "\u20AC.";
        }

        product.removeStock(quantity);
        client.removeBalance(totalCost);
        owner.addBalance(totalCost);

        Inventory clientInv = client.getInventory();
        int existingIndex = clientInv.findProductByName(product.getName());

        if (existingIndex >= 0) {
            Product existingProduct = clientInv.getProduct(existingIndex);
            existingProduct.addStock(quantity);
        } else {
            Product boughtProduct = new Product(product.getName(), product.getPrice(), quantity);
            clientInv.addProduct(boughtProduct);
        }

        return "Successfully bought " + quantity + " of \"" + product.getName() + "\".";
    }

    public String returnProduct(Client client, int invIndex) {
        Inventory clientInv = client.getInventory();
        Product clientProduct = clientInv.getProduct(invIndex);

        if (clientProduct == null) {
            return "Invalid product index.";
        }

        int storeIndex = inventory.findProductByName(clientProduct.getName());
        if (storeIndex < 0) {
            return "Product \"" + clientProduct.getName() + "\" no longer exists in the store.";
        }

        double refund = clientProduct.getPrice() * clientProduct.getStock();

        if (owner.getBalance() < refund) {
            return "Store owner doesn't have enough balance to refund.";
        }

        Product storeProduct = inventory.getProduct(storeIndex);
        int quantity = clientProduct.getStock();

        storeProduct.addStock(quantity);
        client.addBalance(refund);
        owner.removeBalance(refund);

        clientInv.removeProduct(invIndex);

        return "Successfully returned " + quantity + " of product \"" + clientProduct.getName() + "\".";
    }

    public void printStoreView() {

        System.out.println();
        System.out.println(" ====== " + name + " ====== ");
        System.out.println("Current user: " + currentUser.getName() + " (" + currentUser.getType() + ") (" + currentUser.getBalance() + "\u20AC)");
        System.out.println("Products:");

        if (inventory.getCount() == 0) {
            System.out.println("  No products available.");
        } else {
            for (int i = 0; i < inventory.getCount(); i++) {
                Product p = inventory.getProduct(i);
                System.out.println((i + 1) + " | " + p.getName() + " x" + p.getStock() + " | " + p.getPrice() + " EUR ");  //products of store
            }
        }

        System.out.println("------");
        System.out.println("Actions:");

        if (currentUser instanceof Client) {
            printClientActions();
        } else if (currentUser instanceof Owner) {
            printOwnerActions();
        }
    }

    private void printClientActions() {
        System.out.println("  store - Show store view");
        System.out.println("  buy - Buy a product");
        System.out.println("  inv - List client inventory items");
        System.out.println("  return - Return a product that was bought");
        System.out.println("  client - Switch to another client");
        System.out.println("  owner - Switch to owner of store");
        System.out.println("  register - Add new client");
    }

    private void printOwnerActions() {
        System.out.println("  store - Show store view");
        System.out.println("  add - Add a product");
        System.out.println("  remove - Remove a product");
        System.out.println("  edit - Edit a product");
        System.out.println("  stock - Increase stock of a product");
        System.out.println("  rename - Change store name");
        System.out.println("  client - Switch to a client");
        System.out.println("  register - Add new client");
    }

    public void printClientInventory(Client client) {
        Inventory clientInv = client.getInventory();
        System.out.println(client.getName() + "'s inventory:");

        if (clientInv.getCount() == 0) {
            System.out.println("  Inventory is empty.");
        } else {
            for (int i = 0; i < clientInv.getCount(); i++) {
                Product p = clientInv.getProduct(i);
                System.out.println((i + 1) + " - " + p.getName() + " x" + p.getStock() + " (" + p.getPrice() + "\u20AC)");
            }
        }
    }

    public void printClients() {
        System.out.println("Clients:");
        for (int i = 0; i < clientCount; i++) {
            System.out.println((i + 1) + " - " + clients[i].getName() + " (" + clients[i].getBalance() + "\u20AC)");
        }
    }

    public boolean isValidAction(String action) {
        if (currentUser instanceof Client) {
            return action.equals("store") || action.equals("buy") || action.equals("inv")
                || action.equals("return") || action.equals("client") || action.equals("owner")
                || action.equals("register");
        } else if (currentUser instanceof Owner) {
            return action.equals("store") || action.equals("add") || action.equals("remove")
                || action.equals("edit") || action.equals("stock") || action.equals("rename")
                || action.equals("client") || action.equals("register");
        }
        return false;
    }
}
