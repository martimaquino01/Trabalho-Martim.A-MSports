import java.util.Scanner;

public class StoreInterface {
    
    public void showStore(){

        Scanner option = new Scanner(System.in);

        System.out.println("-------------|  " + storeName + "  |-------------");
        System.out.println("Current User: " + currentName + " ( " + type + " ) " + " ( " + currentBalance + " ) ");
        Inventory.printInventory();
        System.out.println(" Actions : ");
        if ("Client".equals(type)) {
            Client.printClientActions();

        }else if ("Onwer".equals(type)) {

            System.out.println(" store - Show store view ");
            System.out.println(" add - Add a product ");
            System.out.println(" remove - Remove a product ");
            System.out.println(" edit - Edit a product ");
            System.out.println(" stock - Increase stock of a product ");
            System.out.println(" rename - Change store name ");
            System.out.println(" client - Switch to a client ");
            System.out.println(" register - Add new client ");

        }else{
            type = "Client";
        }
        
        System.out.println("Enter action: ");
        String newOption = option.nextLine().toLowerCase();


    



    }
}
