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

    public static void performanceMode(){
        System.out.println("\n=== PERFORMANCE MODE ===");
        
        String directoryPath = "/mnt/c/Users/beatr/OneDrive/Desktop/Tirocinio/RisultatiAlgo";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File file = new File(directoryPath, "performance_" + timestamp + ".txt");
        new File(directoryPath).mkdirs();

        int[] lengths = {200, 400, 800, 1600, 1000, 2000};

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("=== REPORT PERFORMANCE ===");
            System.out.println("Log: " + file.getAbsolutePath());

            for (int size : lengths) {
                
                String word = ClosedWordUtils.generateRandomWord(2, size, size); 

                
                long startTimeDP = System.currentTimeMillis();
                Factorizer.minimalFactorizationDP(word);
                long endTimeDP = System.currentTimeMillis();
                long durationDP = endTimeDP - startTimeDP;

                
                long startTimeGreedy = System.currentTimeMillis();
                Factorizer.greedyFactorization(word);
                long endTimeGreedy = System.currentTimeMillis();
                long durationGreedy = endTimeGreedy - startTimeGreedy;

                
                String report = "\nParola di lunghezza " + size + ":\n" +
                                "Tempo DP: " + durationDP + " ms (" + (durationDP/1000.0) + " s)\n" +
                                "Tempo Greedy: " + durationGreedy + " ms (" + (durationGreedy/1000.0) + " s)\n" +
                                "--------------------------------------------------";
                
                System.out.println(report);
                writer.println(report);
                writer.flush();
            }

            System.out.println("\n=== FINE PERFORMANCE TEST ===");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generatorMode() {
        System.out.println("\n=== GENERATOR MODE & REPORT ===");
        
        String directoryPath = "/mnt/c/Users/beatr/OneDrive/Desktop/Tirocinio/RisultatiAlgo";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File file = new File(directoryPath, "report_generatori_" + timestamp + ".txt");
        new File(directoryPath).mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("=== REPORT ANALISI GENERATORI ===");
            writer.println("Data: " + LocalDateTime.now());
            System.out.println("Log salvato in: " + file.getAbsolutePath());

            String headerFib = "\n=== ANALISI PAROLE DI FIBONACCI ===";
            System.out.println(headerFib);
            writer.println(headerFib);

            String tableHeader = String.format("%-5s | %-15s | %-10s | %-10s | %-10s | %-10s | %-15s", 
                 "n", "Lunghezza", "DP Size", "Greedy", "Time(ms)", "Diff", "Check");
            System.out.println(tableHeader);
            writer.println(tableHeader);

            String separator = "-----------------------------------------------------------------------------------------";
            System.out.println(separator);
            writer.println(separator);

            for (int i = 3; i <= 17; i++) {
                String fibWord = Generatori.fibonacciWord(i);
                
                long startT = System.currentTimeMillis();
                FactorizationResult dp = Factorizer.minimalFactorizationDP(fibWord);
                FactorizationResult greedy = Factorizer.greedyFactorization(fibWord);
                long endT = System.currentTimeMillis();

                int diff = greedy.totalFactors - dp.totalFactors;
                String check = (diff > 0) ? "!!! CONTROESEMPIO" : "OK";

                String row = String.format("%-5d | %-15d | %-10d | %-10d | %-10d | %-10d | %-15s", 
                    i, fibWord.length(), dp.totalFactors, greedy.totalFactors, (endT - startT), diff, check);
                
                System.out.println(row);
                writer.println(row);
                writer.flush();
            }
            System.out.println(separator);
            writer.println(separator);

            String headerTM = "\n\n=== ANALISI PAROLE DI THUE-MORSE ===";
            System.out.println(headerTM);
            writer.println(headerTM);
            
            System.out.println(tableHeader);
            writer.println(tableHeader);
            
            System.out.println(separator);
            writer.println(separator);

            for (int i = 1; i <= 11; i++) { 
                String thueWord = Generatori.thueMorseWord(i);
                
                long startT = System.currentTimeMillis();
                FactorizationResult dp = Factorizer.minimalFactorizationDP(thueWord);
                FactorizationResult greedy = Factorizer.greedyFactorization(thueWord);
                long endT = System.currentTimeMillis();

                int diff = greedy.totalFactors - dp.totalFactors;
                String check = (diff > 0) ? "!!! CONTROESEMPIO" : "OK";

                String row = String.format("%-5d | %-15d | %-10d | %-10d | %-10d | %-10d | %-15s", 
                    i, thueWord.length(), dp.totalFactors, greedy.totalFactors, (endT - startT), diff, check);

                System.out.println(row);
                writer.println(row);
                writer.flush();
            }
            System.out.println(separator);
            writer.println(separator);
            
            System.out.println("\nAnalisi completata.");

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
        System.out.println("1 - Manuale | 2 - Test Auto | 3 - Performance | 4 - Generatori");
        String choice = scanner.nextLine();
        if (choice.equals("2")) runTestMode();
        else if (choice.equals("3")) performanceMode();
        else if (choice.equals("4")) generatorMode();
        else runManualMode(scanner);
    }
}