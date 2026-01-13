import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Factorizer {

    public static FactorizationResult minimalFactorizationDP(String text) {
        int n = text.length();
        int[] dp = new int[n + 1];
        long[] ways = new long[n + 1];
        
        
        List<Integer>[] predecessors = new ArrayList[n + 1];
        for (int k = 0; k <= n; k++) {
            predecessors[k] = new ArrayList<>();
        }
        
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0; 
        ways[0] = 1; 

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                String sub = text.substring(j, i);
                if (ClosedWordUtils.isClosed(sub)) {
                    if (dp[j] != Integer.MAX_VALUE) {
                        int candidateLen = dp[j] + 1;
                        
                        
                        if (candidateLen < dp[i]) {
                            dp[i] = candidateLen;
                            ways[i] = ways[j];
                            
                            
                            predecessors[i].clear();
                            predecessors[i].add(j);
                        } 
                        
                        else if (candidateLen == dp[i]) {
                            ways[i] += ways[j];
                            
                            predecessors[i].add(j);
                        }
                    }
                }
            }
        }

        
        List<String> allSolutions = new ArrayList<>();
        
        
        
        if (dp[n] != Integer.MAX_VALUE) {
            collectPaths(n, predecessors, text, new LinkedList<>(), allSolutions, 10);
        }

        String example = allSolutions.isEmpty() ? "" : allSolutions.get(0);
        
        return new FactorizationResult(dp[n], example, ways[n], allSolutions);
    }

    
    private static void collectPaths(int currentIdx, List<Integer>[] preds, String text, 
                                     LinkedList<String> currentPath, List<String> results, int limit) {
        
        if (results.size() >= limit) return; 

        
        if (currentIdx == 0) {
            results.add(String.join(" ", currentPath));
            return;
        }

        
        for (int prevIdx : preds[currentIdx]) {
            String factor = "[" + text.substring(prevIdx, currentIdx) + "]";
            currentPath.addFirst(factor); 
            
            collectPaths(prevIdx, preds, text, currentPath, results, limit);
            
            currentPath.removeFirst(); 
        }
    }

    public static FactorizationResult greedyFactorization(String text) {
        int n = text.length();
        int count = 0;
        int idx = 0;
        StringBuilder sb = new StringBuilder();

        while (idx < n) {
            int bestEnd = idx + 1;
            for (int end = n; end > idx; end--) {
                String sub = text.substring(idx, end);
                if (ClosedWordUtils.isClosed(sub)) {
                    bestEnd = end;
                    break; 
                }
            }
            sb.append("[").append(text.substring(idx, bestEnd)).append("] ");
            count++;
            idx = bestEnd;
        }
        
        List<String> singleSol = new ArrayList<>();
        singleSol.add(sb.toString().trim());
        
        return new FactorizationResult(count, sb.toString().trim(), 1, singleSol);
    }
}