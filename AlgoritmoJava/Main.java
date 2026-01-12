import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    // Classe di supporto per restituire sia il numero che la rappresentazione testuale
    static class Result {
        int count;
        String factors;

        public Result(int count, String factors) {
            this.count = count;
            this.factors = factors;
        }
    }

    public static String generateRandomWord(int alphaSize, int minLen, int maxLen) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(maxLen - minLen + 1) + minLen;
        char[] fullAlphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        
        for (int i = 0; i < length; i++) {
            int charIndex = random.nextInt(alphaSize);
            sb.append(fullAlphabet[charIndex]);
        }
        return sb.toString();
    }
    
    public static boolean isClosed(String w) {
        int n = w.length();
        if (n <= 1) return true;

        String border = "";
        int lenB = 0;

        for (int i = n - 1; i > 0; i--) {
            if (w.startsWith(w.substring(n - i))) {
                border = w.substring(0, i);
                lenB = i;
                break;
            }
        }

        if (lenB == 0) return false;

        int suffixStartIndex = n - lenB;
        int foundIndex = w.indexOf(border, 1);

        if (foundIndex < suffixStartIndex) {
            return false; // Ha occorrenze interne
        }
        return true;
    }

    public static Result minimalFactorizationDP(String text) {
        int n = text.length();
        int[] dp = new int[n + 1];
        int[] from = new int[n + 1]; // Array per ricostruire il percorso (backtracking)
        
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0; // Stringa vuota ha 0 fattori

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                String sub = text.substring(j, i);
                if (isClosed(sub)) {
                    if (dp[j] != Integer.MAX_VALUE && dp[j] + 1 < dp[i]) {
                        dp[i] = dp[j] + 1;
                        from[i] = j; // Ricorda da dove siamo arrivati (da j a i)
                    }
                }
            }
        }

        // --- Ricostruzione dei fattori ---
        List<String> factorList = new ArrayList<>();
        int curr = n;
        while (curr > 0) {
            int prev = from[curr];
            factorList.add("[" + text.substring(prev, curr) + "]");
            curr = prev;
        }
        Collections.reverse(factorList); // L'abbiamo costruita al contrario, giriamola
        
        return new Result(dp[n], String.join(" ", factorList));
    }

    public static Result greedyFactorization(String text) {
        int n = text.length();
        int count = 0;
        int idx = 0;
        StringBuilder sb = new StringBuilder();

        while (idx < n) {
            int bestEnd = idx + 1;
            
            for (int end = n; end > idx; end--) {
                String sub = text.substring(idx, end);
                if (isClosed(sub)) {
                    bestEnd = end;
                    break; 
                }
            }
            
            sb.append("[").append(text.substring(idx, bestEnd)).append("] ");
            count++;
            idx = bestEnd;
        }
        
        return new Result(count, sb.toString().trim());
    }
    
    public static void runTestMode() {
        System.out.println("\n=== TEST MODE: Greedy vs DP ===");
        System.out.println("Generazione di 5.000 parole...");
        
        String directoryPath = "/mnt/c/Users/beatr/OneDrive/Desktop/Tirocinio/RisultatiAlgo";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "risultati_" + timestamp + ".txt";
        File file = new File(directoryPath, fileName);
        new File(directoryPath).mkdirs();

        int totalWordsToGenerate = 5000;
        int counterExamplesFound = 0;
        int currentAlphaSize = 2;
        int thresholdChange = 500;

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("=== TEST REPORT ===");
            writer.println("Data: " + LocalDateTime.now());
            System.out.println("Salvataggio risultati in: " + file.getAbsolutePath());

            for (int i = 1; i <= totalWordsToGenerate; i++) {
                
                if (i > thresholdChange && currentAlphaSize < 5) {
                    currentAlphaSize++; 
                    thresholdChange += 500; 
                    writer.println(">>> LEVEL UP! Alfabeto: " + currentAlphaSize);
                }

                String word = generateRandomWord(currentAlphaSize, 20, 50);

                Result dpRes = minimalFactorizationDP(word);
                Result greedyRes = greedyFactorization(word);

                if (greedyRes.count > dpRes.count) {
                    counterExamplesFound++;
                    
                    StringBuilder report = new StringBuilder();
                    report.append("\n[!!!] CONTROESEMPIO #").append(counterExamplesFound)
                          .append(" (Parola ").append(i).append(") [!!!]\n");
                    report.append("Parola: ").append(word).append("\n");
                    report.append("Alfabeto: ").append(currentAlphaSize).append("\n");
                    
                    report.append("DP (Ottimo, ").append(dpRes.count).append("): ").append(dpRes.factors).append("\n");
                    report.append("Greedy (Errato, ").append(greedyRes.count).append("): ").append(greedyRes.factors).append("\n");
                    
                    report.append("Differenza: ").append(greedyRes.count - dpRes.count).append("\n");
                    report.append("--------------------------------------------------");

                    System.out.println(report.toString());
                    writer.println(report.toString());
                    writer.flush();
                }

                if (i % 500 == 0) System.out.print(".");
            }

            String footer = "\n\n=== FINE TEST ===\nControesempi trovati: " + counterExamplesFound;
            System.out.println(footer);
            writer.println(footer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runManualMode(Scanner scanner) {
        System.out.println("\n--- Modalita' Manuale ---");
        
        while (true) {
            System.out.print("\nInserisci parola (o 'esci'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("esci")) break;
            if (input.isEmpty()) continue;

            boolean closed = isClosed(input);
            Result dpRes = minimalFactorizationDP(input);
            Result greedyRes = greedyFactorization(input);
            
            System.out.println("Analisi: " + input);
            System.out.println(" - Chiusa: " + closed);
            System.out.println(" - DP (" + dpRes.count + "): " + dpRes.factors);
            System.out.println(" - Greedy (" + greedyRes.count + "): " + greedyRes.factors);
            
            if (greedyRes.count > dpRes.count) {
                System.out.println(" -> NOTA: Il Greedy ha fallito! Differenza: " + (greedyRes.count - dpRes.count));
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1 - Manuale | 2 - Test Auto | 0 - Esci");
            String scelta = scanner.nextLine();
            if (scelta.equals("1")) runManualMode(scanner);
            else if (scelta.equals("2")) runTestMode();
            else if (scelta.equals("0")) break;
        }
        scanner.close();
    }
}