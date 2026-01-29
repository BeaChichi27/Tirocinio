import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class FactorizerOpt {

    /*
    Questo metodo implementa un algoritmo di fattorizzazione ottimale
    utilizzando la programmazione dinamica e il concetto di prefissi
    ripetuti (border array) per ridurre il numero di sottostringhe da considerare.
    Sfrutta degli indici per iniziare e aggiornare la tabella DP in modo efficiente, in modo tale
    da ridurre la complessità temporale passando da una complessità cubica a una complessità quadratica.
    */

    public static FactorizationResult minimalFactorizationDP(String textString) {
        if (textString == null || textString.isEmpty()) {
            return new FactorizationResult(0, "", 1, new ArrayList<>());
        }
        
        char[] text = textString.toCharArray();
        int n = text.length;
        
        int[] dp = new int[n + 1];
        long[] ways = new long[n + 1];
        int[] parent = new int[n + 1];
        
        Arrays.fill(dp, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dp[0] = 0;
        ways[0] = 1;
        
        /*
        Variabili usate per la costruzione del border array e il conteggio dei prefissi
        */
        
        int[] border = new int[n];
        int[] count = new int[n + 1];
        
        
        for (int j = 0; j < n; j++) {
            
            
            if (dp[j] == Integer.MAX_VALUE) continue;
            
            // Reset border e count per la nuova posizione j
            Arrays.fill(count, 0, n - j + 1, 0); 
            
            border[0] = 0;
            count[0] = 1; 
            
            // Considera il singolo carattere come fattore e ha complessità costante
            updateDP(dp, ways, parent, j, j + 1, dp[j] + 1);

            int currentMaxLen = n - j;

            for (int len = 2; len <= currentMaxLen; len++) {
                int i = j + len - 1; 
                
                int k = border[len - 2]; 
                
                while (k > 0 && text[j + k] != text[i]) {
                    k = border[k - 1];
                }
                
                if (text[j + k] == text[i]) {
                    k++;
                }
                
                border[len - 1] = k;
                count[k]++;
                
                if (k > 0 && count[k] == 1) {
                    updateDP(dp, ways, parent, j, i + 1, dp[j] + 1);
                }
            }
        }
        
        String solution = reconstructPath(textString, parent, n);
        return new FactorizationResult(dp[n], solution, ways[n], new ArrayList<>());
    }

    
    private static void updateDP(int[] dp, long[] ways, int[] parent, int start, int target, int newCost) {
        if (newCost < dp[target]) {
            dp[target] = newCost;
            ways[target] = ways[start];
            parent[target] = start;
        } else if (newCost == dp[target]) {
            ways[target] += ways[start];
        }
    }

    private static String reconstructPath(String text, int[] parent, int n) {
        if (n == 0 || dpIsInvalid(parent, n)) return ""; 
        LinkedList<String> factors = new LinkedList<>();
        int current = n;
        while (current > 0) {
            int start = parent[current];
            if (start == -1) break; 
            factors.addFirst("[" + text.substring(start, current) + "]");
            current = start;
        }
        return String.join(" ", factors);
    }
    
    private static boolean dpIsInvalid(int[] parent, int n) {
        return parent[n] == -1;
    }
}