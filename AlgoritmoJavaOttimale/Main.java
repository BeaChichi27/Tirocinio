import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   BENCHMARK COMPARATIVO: Factorizer vs FactorizerOpt");
        System.out.println("===============================================================\n");
        
         runSingleTest("Random 10.000 char (Alpha=2)", () -> Generatori.generateRandomWord(2, 10000, 10000));
        runSingleTest("Random 20.000 char (Alpha=2)", () -> Generatori.generateRandomWord(2, 20000, 20000));
        runSingleTest("Random 50.000 char (Alpha=2)", () -> Generatori.generateRandomWord(2, 50000, 50000));

        // Test aggiuntivi con alfabeto più grande
        runSingleTest("Random 10.000 char (Alpha=4)", () -> Generatori.generateRandomWord(4, 10000, 10000));
        runSingleTest("Random 20.000 char (Alpha=4)", () -> Generatori.generateRandomWord(4, 20000, 20000));
        runSingleTest("Random 50.000 char (Alpha=4)", () -> Generatori.generateRandomWord(4, 50000, 50000));

        // 1. TEST FIBONACCI
        // Struttura altamente ripetitiva. Entrambi dovrebbero essere veloci.
        runComparison("Fibonacci (n=18)", () -> Generatori.fibonacciWord(18));
        
        // Puoi provare n=25 o n=28 ora! FactorizerOpt dovrebbe reggerlo.
        // runComparison("Fibonacci (n=26)", () -> Generatori.fibonacciWord(26));

        // 2. TEST THUE-MORSE
        runComparison("Thue-Morse (n=10)", () -> Generatori.thueMorseWord(10));

        // 3. TEST RANDOM (Lunghezza 10.000)
        // Qui Factorizer (vecchio) soffriva molto. FactorizerOpt dovrebbe volare.
        runComparison("Random (Len=10.000, Alpha=2)", () -> Generatori.generateRandomWord(2, 10000, 10000));
        
    }

    /**
     * Esegue entrambi gli algoritmi sulla stessa stringa e confronta i tempi.
     */
    public static void runComparison(String label, Supplier<String> generator) {
        System.out.println(">>> TEST: " + label);
        
        // 1. Generazione (una sola volta per garantire lo stesso input)
        System.out.print("Generazione input... ");
        String word = generator.get();
        System.out.println("Fatto. (Lunghezza: " + word.length() + " char)");
        System.out.println("-".repeat(60));

        // -------------------------------------------------
        // ALGORITMO 1: Factorizer (Il vecchio: Buckets + Pruning)
        // -------------------------------------------------
        System.out.print("[1] Factorizer (Buckets)... ");
        long start1 = System.nanoTime();
        
        FactorizationResult res1 = Factorizer.minimalFactorizationDP(word);
        
        long time1 = (System.nanoTime() - start1) / 1_000_000;
        System.out.printf("Done in %d ms | Fattori: %d%n", time1, res1.totalFactors);

        // Pulizia memoria forzata tra i test per equità
        System.gc(); 

        // -------------------------------------------------
        // ALGORITMO 2: FactorizerOpt (Il nuovo: KMP Incrementale O(N^2))
        // -------------------------------------------------
        System.out.print("[2] FactorizerOpt (O(N^2))... ");
        long start2 = System.nanoTime();
        
        FactorizationResult res2 = FactorizerOpt.minimalFactorizationDP(word);
        
        long time2 = (System.nanoTime() - start2) / 1_000_000;
        System.out.printf("Done in %d ms | Fattori: %d%n", time2, res2.totalFactors);

        // -------------------------------------------------
        // VERDETTO
        // -------------------------------------------------
        System.out.println("-".repeat(60));
        
        // Calcolo Speedup (X volte più veloce)
        double speedup = (double) time1 / Math.max(time2, 1);
        
        if (time2 < time1) {
            System.out.printf("VINCITORE: FactorizerOpt (%.2fx più veloce)%n", speedup);
        } else {
            System.out.println("VINCITORE: Factorizer (Il vecchio regge ancora!)");
        }

        // Controllo Correttezza (Devono trovare lo stesso numero di fattori)
        if (res1.totalFactors == res2.totalFactors) {
            System.out.println("CHECK: OK (Risultati coerenti)");
        } else {
            System.err.println("CHECK: FALLITO! I numeri di fattori sono diversi!");
            System.err.println("   Old: " + res1.totalFactors + " vs New: " + res2.totalFactors);
        }
        
        // Anteprima soluzione (del nuovo algoritmo)
        String preview = res2.factorsString.length() > 80 
                ? res2.factorsString.substring(0, 80) + "..." 
                : res2.factorsString;
        System.out.println("Soluzione: " + preview);
        System.out.println("\n");
    }

    public static void runSingleTest(String label, Supplier<String> generator) {
        System.out.println(">>> TEST: " + label);
        
        // Generazione
        System.out.print("Generazione input... ");
        String word = generator.get();
        System.out.println("Fatto. (Lunghezza: " + word.length() + " char)");
        
        // Pulizia memoria prima del test
        System.gc();
        
        // Esecuzione
        System.out.print("Esecuzione FactorizerOpt... ");
        long start = System.nanoTime();
        
        FactorizationResult res = FactorizerOpt.minimalFactorizationDP(word);
        
        long timeMs = (System.nanoTime() - start) / 1_000_000;
        
        // Risultati
        System.out.printf("Done!%n");
        System.out.printf("  Tempo: %,d ms (%.2f sec)%n", timeMs, timeMs / 1000.0);
        System.out.printf("  Fattori: %d%n", res.totalFactors);
        
        // Anteprima soluzione
        String preview = res.factorsString.length() > 100 
                ? res.factorsString.substring(0, 100) + "..." 
                : res.factorsString;
        System.out.println("  Soluzione: " + preview);
        
        System.out.println("-".repeat(60) + "\n");
    }

}