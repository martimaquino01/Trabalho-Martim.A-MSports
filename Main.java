
public class Main {

    public static void main(String[] args) {
        // 1) Criar loja e dono.
        Store store = new Store("MSports - Loja virtual de desporto", "Martim", 100.0);

        // 2) Criar alguns utilizadores do tipo cliente.
        store.addUser("Joao");
        store.addUser("Alice");
        store.addUser("Bob");

        // 3) Adicionar alguns produtos iniciais.
        store.addInitialProduct("Bola de Futebol", 20.0);
        store.addInitialProduct("Chuteiras de Futebol", 50.0);
        store.addInitialProduct("Sapatilhas de corrida", 35.0);

        // 4) Arrancar interface de consola.
        StoreInterface storeInterface = new StoreInterface(store);
        storeInterface.run();
    }

}