import java.util.Scanner;

public class StoreInterface {

    // Scanner unico para ler dados da consola.
    private final Scanner scanner;

    // Referencia para a loja (onde esta a logica principal).
    private final Store store;

    // Construtor simples da interface.
    public StoreInterface(Store store) {
        this.store = store;
        this.scanner = new Scanner(System.in);
    }

    // Le texto livre.
    public String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Le inteiro; se input invalido devolve -1.
    public int readInt(String prompt) {
        String raw = readText(prompt);
        if (!isInteger(raw)) {
            return -1;
        }
        return Integer.parseInt(raw);
    }

    // Le decimal; aceita virgula ou ponto.
    public double readDouble(String prompt) {
        String raw = readText(prompt);
        raw = raw.replace(',', '.');
        if (!isDecimal(raw)) {
            return -1;
        }
        return Double.parseDouble(raw);
    }

    // Validacao manual de inteiro para evitar excecoes.
    private boolean isInteger(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        int start = 0;
        if (value.charAt(0) == '-') {
            if (value.length() == 1) {
                return false;
            }
            start = 1;
        }

        for (int i = start; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    // Validacao manual de decimal simples.
    private boolean isDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        int start = 0;
        int dots = 0;

        if (value.charAt(0) == '-') {
            if (value.length() == 1) {
                return false;
            }
            start = 1;
        }

        for (int i = start; i < value.length(); i++) {
            char c = value.charAt(i);

            if (c == '.') {
                dots = dots + 1;
                if (dots > 1) {
                    return false;
                }
            } else if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }

    // Ciclo principal da aplicacao em consola.
    public void run() {
        boolean showStore = true;

        while (true) {
            if (showStore) {
                store.showStoreView();
            }

            String action = readText("Enter action: ").toLowerCase();
            showStore = store.executeAction(action, this);
            System.out.println();
        }
    }
}
