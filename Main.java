
public class Main {

    public static void main(String[] args) {
        Store store = new Store("MSports - Loja virtual de desporto", "Martim", 100.0);

        store.addUser("Joao");
        store.addUser("Alice");
        store.addUser("Bob");

        store.addInitialProduct("Bola de Futebol", 20.0);
        store.addInitialProduct("Chuteiras de Futebol", 50.0);
        store.addInitialProduct("Sapatilhas de corrida", 35.0);

        StoreInterface storeInterface = new StoreInterface(store);
        storeInterface.run();
    }

}