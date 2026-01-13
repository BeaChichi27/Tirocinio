import java.util.Random;

public class ClosedWordUtils {

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
            return false; 
        }
        return true;
    }
}