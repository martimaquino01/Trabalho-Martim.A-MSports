import java.util.Scanner;

public class StoreInterface {

    private Store store;
    private Scanner scanner;

    public StoreInterface(Store store) {
        this.store = store;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        store.printStoreView();

        while (true) {
            System.out.print("Enter action: ");
            String action = scanner.nextLine().trim().toLowerCase();

            if (action.isEmpty()) {
                continue;
            }

            if (!store.isValidAction(action)) {
                System.out.println("Invalid action \"" + action + "\".");
                System.out.println();
                continue;
            }

            switch (action) {
                
                case "store":
                    actionStore();
                    break;
                case "buy":
                    actionBuy();
                    break;
                case "inv":
                    actionInventory();
                    break;
                case "return":
                    actionReturn();
                    break;
                case "client":
                    actionClient();
                    break;
                case "owner":
                    actionOwner();
                    break;
                case "register":
                    actionRegister();
                    break;
                case "add":
                    actionAdd();
                    break;
                case "remove":
                    actionRemove();
                    break;
                case "edit":
                    actionEdit();
                    break;
                case "stock":
                    actionStock();
                    break;
                case "rename":
                    actionRename();
                    break;
                default:
                    System.out.println("Invalid action \"" + action + "\".");
                    System.out.println();
                    break;
            }
        }
    }

    private void actionStore() {
        store.printStoreView();
    }

    private void actionBuy() {
        System.out.print("Enter product index: ");
        int index = readInt();
        if (index == -1) {
            System.out.println("Invalid product index.");
            System.out.println();
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = readInt();
        if (quantity == -1 || quantity <= 0) {
            System.out.println("Invalid quantity.");
            System.out.println();
            return;
        }

        String result = store.buyProduct((Client) store.getCurrentUser(), index - 1, quantity);
        System.out.println(result);

        if (result.startsWith("Successfully")) {
            store.printStoreView();
        } else {
            System.out.println();
        }
    }

    private void actionInventory() {
        Client client = (Client) store.getCurrentUser();
        store.printClientInventory(client);
        System.out.println();
    }

    private void actionReturn() {
        Client client = (Client) store.getCurrentUser();
        store.printClientInventory(client);

        if (client.getInventory().getCount() == 0) {
            System.out.println();
            return;
        }

        System.out.println();
        System.out.print("Enter product index: ");
        int index = readInt();
        if (index == -1) {
            System.out.println("Invalid product index.");
            System.out.println();
            return;
        }

        String result = store.returnProduct(client, index - 1);
        System.out.println(result);

        if (result.startsWith("Successfully")) {
            store.printStoreView();
        } else {
            System.out.println();
        }
    }

    private void actionClient() {
        store.printClients();
        System.out.println();

        System.out.print("Enter client index: ");
        int index = readInt();
        if (index == -1) {
            System.out.println("Invalid client index.");
            System.out.println();
            return;
        }

        Client client = store.getClient(index - 1);
        if (client == null) {
            System.out.println("Invalid client index.");
            System.out.println();
            return;
        }

        store.setCurrentUser(client);
        System.out.println("Switched to client \"" + client.getName() + "\".");
        store.printStoreView();
    }

    private void actionOwner() {
        store.setCurrentUser(store.getOwner());
        System.out.println("Switched to store owner.");
        store.printStoreView();
    }

    private void actionRegister() {
        System.out.print("Enter new client name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Invalid name.");
            System.out.println();
            return;
        }

        Client newClient = new Client(name);
        store.addClient(newClient);
        System.out.println("Client \"" + name + "\" added successfully.");
        System.out.println();
    }

    private void actionAdd() {
        System.out.print("Enter new product name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Invalid name.");
            System.out.println();
            return;
        }

        System.out.print("Enter new product price: ");
        double price = readDouble();
        if (price <= 0) {
            System.out.println("Invalid price.");
            System.out.println();
            return;
        }

        Product product = new Product(name, price, 20);
        store.addProduct(product);
        System.out.println("Product \"" + name + "\" added successfully.");
        store.printStoreView();
    }

    private void actionRemove() {
        System.out.print("Enter product index: ");
        int index = readInt();
        if (index == -1) {
            System.out.println("Invalid product index.");
            System.out.println();
            return;
        }

        if (store.removeProduct(index - 1)) {
            System.out.println("Product removed successfully.");
            store.printStoreView();
        } else {
            System.out.println("Invalid product index.");
            System.out.println();
        }
    }

    private void actionEdit() {
        System.out.print("Enter product index: ");
        int index = readInt();
        if (index == -1) {
            System.out.println("Invalid product index.");
            System.out.println();
            return;
        }

        Product product = store.getInventory().getProduct(index - 1);
        if (product == null) {
            System.out.println("Invalid product index.");
            System.out.println();
            return;
        }

        System.out.print("Enter new product name (leave empty to keep \"" + product.getName() + "\"): ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter new product price (0 to keep " + product.getPrice() + "\u20AC): ");
        double price = readDouble();

        if (!name.isEmpty()) {
            product.setName(name);
        }
        if (price > 0) {
            product.setPrice(price);
        }

        System.out.println("Product edited successfully.");
        store.printStoreView();
    }

    private void actionStock() {
        System.out.print("Enter product index: ");
        int index = readInt();
        if (index == -1) {
            System.out.println("Invalid product index.");
            System.out.println();
            return;
        }

        Product product = store.getInventory().getProduct(index - 1);
        if (product == null) {
            System.out.println("Invalid product index.");
            System.out.println();
            return;
        }

        System.out.print("Enter how much stock to add: ");
        int amount = readInt();
        if (amount == -1 || amount <= 0) {
            System.out.println("Invalid quantity.");
            System.out.println();
            return;
        }

        product.addStock(amount);
        System.out.println("Increased stock of product by " + amount + ".");
        store.printStoreView();
    }

    private void actionRename() {
        System.out.print("Enter new store name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Invalid name.");
            System.out.println();
            return;
        }

        store.setName(name);
        System.out.println("Store renamed to \"" + name + "\".");
        store.printStoreView();
    }

    private int readInt() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            String input = scanner.nextLine().trim();
            input = input.replace(",", ".");
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
