import java.util.Scanner;

// it handles the user output and interacts with the store
public class StoreInterface {

    private final Scanner scanner;
    private final Store store;

    public StoreInterface(Store store) {
        this.store = store;
        this.scanner = new Scanner(System.in);
    }

    public String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        String raw = readText(prompt);
        // Se o texto não for inteiro, devolvemos -1 para o código tratar como inválido.
        if (!isInteger(raw)) { 
            return -1;
        }
        return Integer.parseInt(raw);
    }

    public double readDouble(String prompt) {
        String raw = readText(prompt);
        raw = raw.replace(',', '.');
        // Validação simples para evitar NumberFormatException e dar feedback cedo.
        if (!isDecimal(raw)) {
            return -1;
        }
        return Double.parseDouble(raw);
    }

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

    public void run() {

        boolean showStore = true; //

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
