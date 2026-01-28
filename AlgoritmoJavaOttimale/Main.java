import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   BENCHMARK OTTIMIZZAZIONE");
        System.out.println("==================================================\n");

        // 1. TEST FIBONACCI
        // n=18 -> 4181 caratteri
        runTest("Fibonacci (n=18)", () -> Generatori.fibonacciWord(18));
        
        // n=25 -> 121.393 caratteri (scommenta se vuoi testare carichi pesanti)
        // runTest("Fibonacci (n=25)", () -> Generatori.fibonacciWord(25));

        // 2. TEST THUE-MORSE
        runTest("Thue-Morse (n=10)", () -> Generatori.thueMorseWord(10));

        // 3. TEST RANDOM (Lunghezza 10.000)
        // CORREZIONE QUI: Passo 10000 due volte (min e max)
        runTest("Random (Len=10.000, Alpha=2)", () -> Generatori.generateRandomWord(2, 5000, 5000));
        
        // 4. TEST RANDOM ESTREMO (Lunghezza 50.000)
        // CORREZIONE QUI: Passo 50000 due volte (min e max)
        runTest("Random (Len=50.000, Alpha=3)", () -> Generatori.generateRandomWord(3, 10000, 10000));
    }

    /**
     * Metodo helper per eseguire il test, misurare il tempo e stampare i risultati.
     */
    public static void runTest(String label, Supplier<String> generator) {
        System.out.println("--- Test: " + label + " ---");
        
        // 1. Generazione Parola
        System.out.print("Generazione parola... ");
        String word = generator.get();
        System.out.println("Fatto. (Lunghezza: " + word.length() + ")");

        // 2. Avvio Timer
        long startTime = System.nanoTime();

        // 3. Esecuzione Fattorizzazione
        FactorizationResult result = Factorizer.minimalFactorizationDP(word);

        // 4. Stop Timer
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // 5. Stampa Report
        System.out.println("Tempo esecuzione: " + durationMs + " ms");
        System.out.println("Numero Fattori:   " + result.totalFactors);
        
        // Stampa anteprima pulita
        String preview = result.factorsString.length() > 100 
                ? result.factorsString.substring(0, 100) + "..." 
                : result.factorsString;
        
        System.out.println("Anteprima:        " + preview);
        System.out.println("--------------------------------------------------\n");
    }
}