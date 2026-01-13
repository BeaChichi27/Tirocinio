import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static void runTestMode() {
        System.out.println("\n=== TEST MODE: Unicità & Greedy ===");
        
        String directoryPath = "/mnt/c/Users/beatr/OneDrive/Desktop/Tirocinio/RisultatiAlgo";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File file = new File(directoryPath, "risultati_" + timestamp + ".txt");
        new File(directoryPath).mkdirs();

        int totalWords = 5000;
        int nonUniqueFound = 0; 
        int counterExamplesFound = 0; 
        long startTime = System.currentTimeMillis();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("=== REPORT RICERCA ===");
            System.out.println("Log: " + file.getAbsolutePath());

            for (int i = 1; i <= totalWords; i++) {
                
                
                String word = ClosedWordUtils.generateRandomWord(2, 20, 50); 

                
                FactorizationResult dp = Factorizer.minimalFactorizationDP(word);
                FactorizationResult greedy = Factorizer.greedyFactorization(word);

                
                if (greedy.totalFactors > dp.totalFactors) {
                    counterExamplesFound++; 
                    
                    int diff = greedy.totalFactors - dp.totalFactors;

                    StringBuilder report = new StringBuilder();
                    report.append("\n[!!!] CONTROESEMPIO #").append(counterExamplesFound)
                          .append(" (Parola ").append(i).append(") [!!!]\n");
                    report.append("Parola: ").append(word).append("\n");
                    
                    report.append("DP (Ottimo, ").append(dp.totalFactors).append("): ")
                          .append(dp.factorsString).append("\n");
                    
                    
                    report.append("Totale Soluzioni Ottime: ").append(dp.solutionCount).append("\n");
                    
                    report.append("Greedy (Errato, ").append(greedy.totalFactors).append("): ")
                          .append(greedy.factorsString).append("\n");
                    
                    report.append("Differenza: ").append(diff).append("\n");
                    report.append("--------------------------------------------------");
                    
                    System.out.println(report.toString());
                    writer.println(report.toString());
                    writer.flush();
                }

                
                if (dp.solutionCount > 1) {
                    nonUniqueFound++;
                    
                    StringBuilder msg = new StringBuilder();
                    msg.append("\n[!!!] NON-UNICITÀ TROVATA (Parola ").append(i).append(") [!!!]\n");
                    msg.append("Parola: ").append(word).append("\n");
                    msg.append("Minimo Fattori: ").append(dp.totalFactors).append("\n");
                    
                    
                    msg.append("Differenza (Varianti): ").append(dp.solutionCount).append("\n");
                    msg.append("Esempio percorso: ").append(dp.factorsString).append("\n");
                    
                    msg.append("--- Elenco Soluzioni Ottimali ---\n");
                    if (dp.allSolutions != null) {
                        for (String sol : dp.allSolutions) {
                            msg.append("   -> ").append(sol).append("\n");
                        }
                    }
                    msg.append("--------------------------------------------------");

                    System.out.println(msg.toString());
                    writer.println(msg.toString());
                    writer.flush();
                }

                if (i % 500 == 0) System.out.print(".");
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            String footer = "\n\n=== FINE TEST ===\n" +
                            "Parole analizzate: " + totalWords + "\n" +
                            "Controesempi Greedy trovati: " + counterExamplesFound + "\n" +
                            "Casi di Non-Unicità trovati: " + nonUniqueFound + "\n" +
                            "Tempo totale esecuzione: " + duration + " ms (" + (duration/1000.0) + " s)";
            
            System.out.println(footer);
            writer.println(footer);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runManualMode(Scanner scanner) {
        while (true) {
            System.out.print("\nInserisci parola (scrivi 'esci' per uscire): ");
            String input = scanner.nextLine().trim();
            if (input.equals("esci")) break;

            FactorizationResult dp = Factorizer.minimalFactorizationDP(input);
            FactorizationResult greedy = Factorizer.greedyFactorization(input);

            System.out.println("DP: " + dp.totalFactors + " fattori (Modi possibili: " + dp.solutionCount + ")");
            System.out.println(" -> " + dp.factorsString);
            
            if (dp.solutionCount > 1 && dp.allSolutions != null) {
                System.out.println("   Altre soluzioni ottime:");
                for (String s : dp.allSolutions) {
                    if (!s.equals(dp.factorsString)) {
                        System.out.println("   -> " + s);
                    }
                }
            }

            System.out.println("Greedy: " + greedy.totalFactors + " fattori");
            System.out.println(" -> " + greedy.factorsString);
            
            if (greedy.totalFactors > dp.totalFactors) {
                 System.out.println(" -> NOTA: Il Greedy ha fallito! Differenza: " + (greedy.totalFactors - dp.totalFactors));
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1 - Manuale | 2 - Test Auto");
        String choice = scanner.nextLine();
        if (choice.equals("2")) runTestMode();
        else runManualMode(scanner);
    }
}