public class Main {

    public static void main(String[] args) {
        Owner owner = new Owner("Martim", 500.0);
        Store store = new Store("MSports", owner);

        store.addProduct(new Product("Bola de Futebol", 8.90, 20));
        store.addProduct(new Product("Chuteiras de Futebol", 74.00, 20));
        store.addProduct(new Product("Fato de Banho Mulher", 15.90, 20));
        store.addProduct(new Product("Calcoes de Banho Homem", 8.90, 20));
        store.addProduct(new Product("Sapatilhas de Corrida", 39.90, 20));
        store.addProduct(new Product("Casaco de Ski", 44.90, 20));
        store.addProduct(new Product("Luvas de Ski", 9.90, 20));

        Client client1 = new Client("Joao");
        Client client2 = new Client("Maria");
        Client client3 = new Client("Pedro");

        store.addClient(client1);
        store.addClient(client2);
        store.addClient(client3);

        store.setCurrentUser(client1);

        StoreInterface storeInterface = new StoreInterface(store);
        storeInterface.start();
    }
}
