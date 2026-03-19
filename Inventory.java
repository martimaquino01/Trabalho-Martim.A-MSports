public class Inventory {

    // Limite maximo pedido no enunciado.
    private static final int MAX_PRODUCTS = 100;

    // Arrays paralelos para nome, quantidade e preco.
    private final String[] names = new String[MAX_PRODUCTS];
    private final int[] quantities = new int[MAX_PRODUCTS];
    private final double[] prices = new double[MAX_PRODUCTS];

    // Quantidade de produtos realmente usados.
    private int size = 0;

    // Numero atual de produtos guardados.
    public int getSize() {
        return size;
    }

    // Indice valido e de 1 ate size.
    public boolean isValidIndex(int index) {
        return index >= 1 && index <= size;
    }

    // Getters por indice (indice visivel comeca em 1).
    public String getNameAt(int index) {
        return names[index - 1];
    }

    public int getQuantityAt(int index) {
        return quantities[index - 1];
    }

    public double getPriceAt(int index) {
        return prices[index - 1];
    }

    // Setters para editar nome e preco de produto.
    public void setNameAt(int index, String newName) {
        names[index - 1] = newName;
    }

    public void setPriceAt(int index, double newPrice) {
        prices[index - 1] = newPrice;
    }

    // Aumenta ou diminui quantidade em stock.
    public void increaseQuantityAt(int index, int amount) {
        quantities[index - 1] = quantities[index - 1] + amount;
    }

    public void decreaseQuantityAt(int index, int amount) {
        quantities[index - 1] = quantities[index - 1] - amount;
    }

    // Procura produto por nome (ignorando maiusculas/minusculas).
    public int findByName(String name) {
        for (int i = 0; i < size; i++) {
            if (names[i].equalsIgnoreCase(name)) {
                return i + 1;
            }
        }
        return -1;
    }

    // Procura por nome e preco (util para inventario do cliente).
    public int findByNameAndPrice(String name, double price) {
        for (int i = 0; i < size; i++) {
            if (names[i].equalsIgnoreCase(name) && prices[i] == price) {
                return i + 1;
            }
        }
        return -1;
    }

    // Adiciona produto novo; se ja existir com mesmo preco, soma quantidade.
    public boolean addProduct(String name, int quantity, double price) {
        if (name == null || name.isEmpty() || quantity <= 0 || price < 0) {
            return false;
        }

        // Se ja existir com mesmo nome e preco, so aumenta quantidade.
        int existing = findByNameAndPrice(name, price);
        if (existing != -1) {
            increaseQuantityAt(existing, quantity);
            return true;
        }

        // Sem espaco livre no inventario.
        if (size >= MAX_PRODUCTS) {
            return false;
        }

        names[size] = name;
        quantities[size] = quantity;
        prices[size] = price;
        size = size + 1;
        return true;
    }

    // Remove produto do indice e puxa todos os seguintes para a esquerda.
    public boolean removeProductAt(int index) {
        if (!isValidIndex(index)) {
            return false;
        }

        int removeAt = index - 1;
        for (int i = removeAt; i < size - 1; i++) {
            names[i] = names[i + 1];
            quantities[i] = quantities[i + 1];
            prices[i] = prices[i + 1];
        }

        names[size - 1] = null;
        quantities[size - 1] = 0;
        prices[size - 1] = 0;
        size = size - 1;
        return true;
    }
}
