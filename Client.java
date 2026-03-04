
public class Client {
    
    private static String name;
    private Double balance;

    public Client(String name, Double balance) {
        this.name = name;
        this.balance = balance;

    }


// getters e setter name 

    public void setName(String name) {
        this.name = name;

    }
    public String getName() {
        return this.name;
    }

// getters e setters balance
    public void setBalance(Double balance){

        if (balance > 0.00) {

            this.balance = balance;
            
        }
    }

    public Double getBalance(){
        return this.balance;
    }



    public static void printClientActions() {

        System.out.println(" store - Show store view ");
        System.out.println(" buy - Buy a product ");
        System.out.println(" inv - List client inventory items ");
        System.out.println(" return - Return a product that was bought ");
        System.out.println(" client - Switch to another client ");
        System.out.println(" owner - Switch to owner of store ");
        System.out.println(" register - Add new client ");

    }

}
