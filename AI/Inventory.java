public class Inventory {

    private Product[] products;
    private int count;
    private static final int MAX_PRODUCTS = 100;

    public Inventory() {
        this.products = new Product[MAX_PRODUCTS];
        this.count = 0;
    }

    public boolean addProduct(Product product) {
        if (count >= MAX_PRODUCTS) {
            return false;
        }
        products[count] = product;
        count++;
        return true;
    }

    public boolean removeProduct(int index) {
        if (index < 0 || index >= count) {
            return false;
        }
        for (int i = index; i < count - 1; i++) {
            products[i] = products[i + 1];
        }
        products[count - 1] = null;
        count--;
        return true;
    }

    public Product getProduct(int index) {
        if (index < 0 || index >= count) {
            return null;
        }
        return products[index];
    }

    public int getCount() {
        return count;
    }

    public int findProductByName(String name) {
        for (int i = 0; i < count; i++) {
            if (products[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
