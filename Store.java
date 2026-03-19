
public class Store {
    // Nome da loja (pode ser alterado com a acao rename).
    private String storeName;

    // Dono unico da loja.
    private final User owner;

    // Inventario principal (stock da loja).
    private final Inventory storeInventory;

    // Lista de utilizadores do tipo cliente.
    private final User[] users = new User[100];
    private int userCount = 0;

    // Guarda qual cliente esta "ativo" no momento.
    private int currentUserIndex = 0;

    // Quando true, mostra e usa as acoes de dono.
    private boolean ownerView = false;

    public Store(String storeName, String ownerName, double ownerBalance) {
        this.storeName = storeName;
        this.owner = new User(ownerName, ownerBalance, true);
        this.storeInventory = new Inventory();
    }

    // Adiciona um novo cliente com saldo inicial de 100 euros.
    public void addUser(String name) {
        if (userCount >= users.length || name == null || name.trim().isEmpty()) {
            return;
        }
        users[userCount] = new User(name.trim(), 100.0, false);
        userCount = userCount + 1;
    }

    // Mantido por compatibilidade com codigo antigo.
    public void addClient(String name) {
        addUser(name);
    }

    // Metodo de apoio para inicializar produtos da loja.
    public void addInitialProduct(String name, double price) {
        storeInventory.addProduct(name, 20, price);
    }

    // Mostra a "vista" atual (cliente ou dono).
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
        for (int i = 1; i <= storeInventory.getSize(); i++) {
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
        } else {
            System.out.println("  store - Show store view");
            System.out.println("  buy - Buy a product");
            System.out.println("  inv - List client inventory items");
            System.out.println("  return - Return a product that was bought");
            System.out.println("  client - Switch to another client");
            System.out.println("  owner - Switch to owner of store");
            System.out.println("  register - Add new client");
        }
    }

    // Recebe a acao e envia para o bloco correto (cliente vs dono).
    public boolean executeAction(String action, StoreInterface storeInterface) {
        if (ownerView) {
            return executeOwnerAction(action, storeInterface);
        }
        return executeClientAction(action, storeInterface);
    }

    private boolean executeClientAction(String action, StoreInterface storeInterface) {
        if ("store".equals(action)) {
            return true;
        }
        if ("buy".equals(action)) {
            return buy(storeInterface);
        }
        if ("inv".equals(action)) {
            showCurrentClientInventory();
            return false;
        }
        if ("return".equals(action)) {
            return returnProduct(storeInterface);
        }
        if ("client".equals(action)) {
            return switchUser(storeInterface);
        }
        if ("owner".equals(action)) {
            ownerView = true;
            System.out.println("Switched to store owner.");
            return true;
        }
        if ("register".equals(action)) {
            registerUser(storeInterface);
            return false;
        }

        System.out.println("Invalid action \"" + action + "\".");
        return false;
    }

    private boolean executeOwnerAction(String action, StoreInterface storeInterface) {
        if ("store".equals(action)) {
            return true;
        }
        if ("add".equals(action)) {
            return addProduct(storeInterface);
        }
        if ("remove".equals(action)) {
            return removeProduct(storeInterface);
        }
        if ("edit".equals(action)) {
            return editProduct(storeInterface);
        }
        if ("stock".equals(action)) {
            return increaseStock(storeInterface);
        }
        if ("rename".equals(action)) {
            return renameStore(storeInterface);
        }
        if ("client".equals(action)) {
            return switchUser(storeInterface);
        }
        if ("register".equals(action)) {
            registerUser(storeInterface);
            return false;
        }

        System.out.println("Invalid action \"" + action + "\".");
        return false;
    }

    // Compra de produto por um cliente.
    private boolean buy(StoreInterface storeInterface) {
        if (storeInventory.getSize() == 0) {
            System.out.println("There are no products in store.");
            return false;
        }

        int productIndex = storeInterface.readInt("Enter product index: ");
        if (!storeInventory.isValidIndex(productIndex)) {
            System.out.println("Invalid product index.");
            return false;
        }

        int quantity = storeInterface.readInt("Enter quantity: ");
        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
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
        if (!current.canAfford(total)) {
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

    // Lista os produtos comprados pelo cliente atual.
    private void showCurrentClientInventory() {
        User current = users[currentUserIndex];
        Inventory inv = current.getInventory();

        System.out.println(current.getName() + "'s inventory:");
        if (inv.getSize() == 0) {
            System.out.println("(empty)");
            return;
        }

        for (int i = 1; i <= inv.getSize(); i++) {
            System.out.println(i + " - " + inv.getNameAt(i) + " x" + inv.getQuantityAt(i) + " (" + inv.getPriceAt(i) + " EUR)");
        }
    }

    // Devolve todos os itens da linha escolhida no inventario do cliente.
    private boolean returnProduct(StoreInterface storeInterface) {
        User current = users[currentUserIndex];
        Inventory inv = current.getInventory();

        if (inv.getSize() == 0) {
            System.out.println("Your inventory is empty.");
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
        double refund = quantity * price;

        int storeProductIndex = storeInventory.findByName(name);
        if (storeProductIndex == -1) {
            System.out.println("Cannot return this product because it no longer exists in store.");
            return false;
        }

        if (owner.getBalance() < refund) {
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

    // Troca o cliente atual.
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

    // Regista novo cliente.
    private void registerUser(StoreInterface storeInterface) {
        if (userCount >= users.length) {
            System.out.println("Cannot register more clients.");
            return;
        }

        String newClientName = storeInterface.readText("Enter new client name: ");
        if (newClientName.trim().isEmpty()) {
            System.out.println("Invalid client name.");
            return;
        }

        addUser(newClientName);
        System.out.println("Client \"" + newClientName + "\" added successfully.");
    }

    // Dono adiciona produto com stock inicial 20.
    private boolean addProduct(StoreInterface storeInterface) {
        if (storeInventory.getSize() >= 100) {
            System.out.println("Store inventory is full.");
            return false;
        }

        String newProductName = storeInterface.readText("Enter new product name: ");
        if (newProductName.trim().isEmpty()) {
            System.out.println("Invalid product name.");
            return false;
        }

        double newPrice = storeInterface.readDouble("Enter new product price: ");
        if (newPrice < 0) {
            System.out.println("Invalid product price.");
            return false;
        }

        storeInventory.addProduct(newProductName, 20, newPrice);
        System.out.println("Product \"" + newProductName + "\" added successfully.");
        return true;
    }

    // Dono remove produto pelo indice.
    private boolean removeProduct(StoreInterface storeInterface) {
        int productIndex = storeInterface.readInt("Enter product index: ");
        if (!storeInventory.removeProductAt(productIndex)) {
            System.out.println("Invalid product index.");
            return false;
        }

        System.out.println("Product removed successfully.");
        return true;
    }

    // Dono altera nome e preco do produto.
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
            System.out.println("Invalid product price.");
            return false;
        }

        storeInventory.setNameAt(productIndex, newProductName);
        storeInventory.setPriceAt(productIndex, newProductPrice);

        System.out.println("Product edited successfully.");
        return true;
    }

    // Dono aumenta stock de um produto.
    private boolean increaseStock(StoreInterface storeInterface) {
        int productIndex = storeInterface.readInt("Enter product index: ");
        if (!storeInventory.isValidIndex(productIndex)) {
            System.out.println("Invalid product index.");
            return false;
        }

        int stockToAdd = storeInterface.readInt("Enter how much stock to add: ");
        if (stockToAdd <= 0) {
            System.out.println("Invalid stock quantity.");
            return false;
        }

        storeInventory.increaseQuantityAt(productIndex, stockToAdd);
        System.out.println("Increased stock of product by " + stockToAdd + ".");
        return true;
    }

    // Dono altera o nome da loja.
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
