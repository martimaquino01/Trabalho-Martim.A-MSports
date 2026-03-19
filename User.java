public class User {

    // Nome do utilizador (nao muda durante o programa).
    private final String name;

    // Saldo atual do utilizador.
    private Double balance;

    // True se este utilizador for o dono da loja.
    private final boolean owner;

    // Inventario pessoal (so existe para clientes).
    private Inventory inventory;

    // Construtor simples para criar dono ou cliente.
    public User(String name, Double balance, boolean owner) {
        this.name = name;
        this.balance = balance;
        this.owner = owner;

        // Cliente recebe inventario proprio para guardar compras.
        if (!owner) {
            this.inventory = new Inventory();
        }
    }

    // Getter do nome.
    public String getName() {
        return this.name;
    }

    // Getter do saldo.
    public Double getBalance() {
        return this.balance;
    }

    // Diz se o utilizador e dono.
    public boolean isOwner() {
        return owner;
    }

    // Devolve inventario pessoal do cliente.
    public Inventory getInventory() {
        return inventory;
    }

    // Verifica se consegue pagar um valor.
    public boolean canAfford(double amount) {
        return balance >= amount;
    }

    // Soma saldo (ex: reembolso ou venda do dono).
    public void addBalance(double amount) {
        balance = balance + amount;
    }

    // Retira saldo (ex: compra ou reembolso do dono).
    public void removeBalance(double amount) {
        balance = balance - amount;
    }
}
