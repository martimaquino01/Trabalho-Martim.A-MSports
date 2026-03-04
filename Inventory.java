// substituir eventualmente para map o inventario
public class Inventory {

    private static String[] inventory = {"Bola de Futebol", "Chuteiras de Futebol", "Fato de banho Mulher", "Calções de banho Homem", "Sapatilhas de corrida", "Casaco de Ski", "Luvas de Ski"};
    private static int[] stock = {20, 20, 20, 20, 20, 20, 20};

    public static void printInventory() {

        System.out.println("--------------------");
        System.out.println(" Products: ");


        for (int i = 0; i < inventory.length; i++) {

            System.out.println( i+1 + " | " + inventory[i] + " | " + stock[i] + " em stock ");

        }

        System.out.println("--------------------");
        
    }


}
