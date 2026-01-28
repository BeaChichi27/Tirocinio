import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Factorizer {

    /*
    Il seguente metodo è progettato per trovare un numero minimo di fattorizzazioni di una stringa data 
    per trovare sottostringhe chiuse utilizzando un approccio di programmazione dinamica.
    Trasforma la stringa di input in un array di caratteri e inizializza diverse strutture dati per tenere traccia
    in modo tale da avere una complessità temporale di O(n^3) (o di O(n^2) nel migliore dei casi) e 
    spaziale di O(n).
    Restituisce un oggetto FactorizationResult che contiene il numero minimo di fattorizzazioni.
    */

    public static FactorizationResult minimalFactorizationDP(String textString) {
        
        char[] text = textString.toCharArray();
        int n = text.length;
        
        int[] dp = new int[n + 1]; 
        // dp[i] rappresenta il costo minimo per fattorizzare il prefisso di lunghezza i
        long[] ways = new long[n + 1];
        // ways[i] rappresenta il numero di modi per ottenere il costo minimo 
        // per fattorizzare il prefisso di lunghezza i
        int[] parent = new int[n + 1];
        // parent[i] rappresenta l'indice di partenza dell'ultima sottostringa chiusa
        
        
        
        List<Integer>[] buckets = new ArrayList[n + 1];
        // buckets[cost] contiene gli indici i tali che dp[i] == cost
        for (int k = 0; k <= n; k++) buckets[k] = new ArrayList<>();

        Arrays.fill(dp, Integer.MAX_VALUE);
        
        dp[0] = 0; 
        ways[0] = 1; 
        buckets[0].add(0);

        int minCostFound = 0; 
        int j;
        int newCost;
        boolean foundOptimal;

        for (int i = 1; i <= n; i++) { 
        // calcoliamo dp[i], ways[i] e parent[i], è il primo ciclo for che scorre tutti i prefissi della stringa
        // e ha complessità O(n)
            foundOptimal = false;
            
            for (int cost = minCostFound; cost < i; cost++) {
            // calcoliamo dp[i] scorrendo i bucket dei costi crescenti
            // e ha complessità O(n) nel caso peggiore, altrimenti è costante
            
                if (buckets[cost].isEmpty()) continue;

                for (int indexInBucket = buckets[cost].size() - 1; indexInBucket >= 0; indexInBucket--) {
                // scorre tutti gli indici j tali che dp[j] == cost
                // e ha complessità O(n) nel caso peggiore
                    j = buckets[cost].get(indexInBucket);
                    
                    // controlliamo se la sottostringa text[j..i-1] è chiusa e nonostante abbia complessità O(n),
                    // l'uso di bucket e la rottura anticipata del ciclo sui costi
                    // riducono notevolmente il numero di chiamate a isClosedOpt, 
                    // migliorando le prestazioni complessive.
                    if (KMPUtils.isClosedOpt(text, j, i - 1)) {
                        newCost = cost + 1;
                        if (newCost < dp[i]) {
                            dp[i] = newCost;
                            ways[i] = ways[j];
                            parent[i] = j;
                            foundOptimal = true;
                        } 
                        else if (newCost == dp[i]) {
                            ways[i] += ways[j];
                        }
                    }
                }

                
                if (foundOptimal) {
                    break; 
                    //interrompiamo il ciclo sui costi, in modo tale da ridurre la complessità temporale
                }
            }
            
            if (dp[i] <= n) {
                buckets[dp[i]].add(i);
            }
        }
        
        return new FactorizationResult(dp[n], "", ways[n], new ArrayList<>());
    }
}