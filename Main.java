
public class Main {

    public static void main(String[] args) {

        Store store = new Store("MSports - Loja virtual de desporto", "Martim", 100.0);


//                           Default Clients


        store.addUser("Pedro");
        store.addUser("Sofia");
        store.addUser("Tomas");

//                           Default Products

store.addInitialProduct("Chuteiras de Futebol Criança F50 League", 52.00);
store.addInitialProduct("Chuteiras de Futebol Adulto Predator Club Turf", 44.90);
store.addInitialProduct("Sapatilhas de Futsal Adulto Super Sala III", 44.90);
store.addInitialProduct("Trotinete Btwin B100 3 Rodas Criança", 24.90);
store.addInitialProduct("Baliza de futebol Classic M (180 cm x 120 cm)", 59.00);
store.addInitialProduct("Bola de Futebol Adidas Treino (World Cup 2026)", 29.90);
store.addInitialProduct("Patins de Criança PLAY5", 24.90);
store.addInitialProduct("Papagaio estático Criança MFK 100", 8.90);
store.addInitialProduct("Tabela de Basquetebol de Parede Set K900", 32.90);
store.addInitialProduct("Rede de Badminton Fun set (3 Metros)", 39.90);
store.addInitialProduct("Óculos de Sol Categoria 3 Active 500 SQR", 19.90);
store.addInitialProduct("Leggings Slim de Fitness Mulher", 6.90);
store.addInitialProduct("Meias de Corrida RUN100 (Pack x3)", 3.90);
store.addInitialProduct("Fogareiro de campismo a gás Campingaz", 29.90);
store.addInitialProduct("Halteres Musculação 20kg (Conjunto)", 50.00);
store.addInitialProduct("Tapete de Ginástica e Pilates Conforto 6,5 mm", 3.90);


//                          Store Interface

        StoreInterface storeInterface = new StoreInterface(store);
        storeInterface.run();
    }

}