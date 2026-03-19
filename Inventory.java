public class Inventory {

    private static final int MAX_PRODUCTS = 100;

    private final String[] names = new String[MAX_PRODUCTS];
    private final int[] quantities = new int[MAX_PRODUCTS];
    private final double[] prices = new double[MAX_PRODUCTS];

    private int size = 0; //number of products in the store's inventory

    public int getSize() {
        return size;
    }

    
    public boolean isValidIndex(int index) {
        return index >= 1 && index <= size;
    }

    //getters - product

    public String getNameAt(int index) {
        return names[index - 1];
    }

    public int getQuantityAt(int index) {
        return quantities[index - 1];
    }

    public double getPriceAt(int index) {
        return prices[index - 1];
    }

    //setters - product
    public void setNameAt(int index, String newName) {
        names[index - 1] = newName;
    }

    public void setPriceAt(int index, double newPrice) {
        prices[index - 1] = newPrice;
    }

    public void increaseQuantityAt(int index, int amount) {
        quantities[index - 1] = quantities[index - 1] + amount;
    }

    public void decreaseQuantityAt(int index, int amount) {
        quantities[index - 1] = quantities[index - 1] - amount;
    }

    // find products
    public int findByName(String name) {
        for (int i = 0; i < size; i++) {
            if (names[i].equalsIgnoreCase(name)) {
                return i + 1;
            }
        }
        return -1;
    }

    public int findByNameAndPrice(String name, double price) {
        for (int i = 0; i < size; i++) {
            if (names[i].equalsIgnoreCase(name) && prices[i] == price) {
                return i + 1;
            }
        }
        return -1;
    }

    public boolean addProduct(String name, int quantity, double price) {

        if (name == null || name.isEmpty() || quantity <= 0 || price < 0) { //protection : empty data, negative amounts, invalid prices
            return false;
        }

        int existing = findByNameAndPrice(name, price);

        if (existing != -1) {
            increaseQuantityAt(existing, quantity);
            return true;
        }

        if (size >= MAX_PRODUCTS) {
            return false;
        }

        names[size] = name;
        quantities[size] = quantity;
        prices[size] = price;
        size = size + 1;
        return true;
    }

    public boolean removeProductAt(int index) {

        if (!isValidIndex(index)) {
            return false;
        }

        int removeAt = index - 1;
        for (int i = removeAt; i < size - 1; i++) { //shift elements to the left
            names[i] = names[i + 1];
            quantities[i] = quantities[i + 1];
            prices[i] = prices[i + 1];
        }

        //clear the last element
        names[size - 1] = null;
        quantities[size - 1] = 0;
        prices[size - 1] = 0;
        size = size - 1;
        return true;

    }
}
