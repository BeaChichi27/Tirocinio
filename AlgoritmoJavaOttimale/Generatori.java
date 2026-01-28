import java.util.Random;

public class Generatori {
    public static String fibonacciWord(int n) {
        if (n == 0) return "b";
        if (n == 1) return "a";
        
        String fPrev = "a";       
        String fPrevPrev = "b";   
        String current = "";

        for (int i = 2; i <= n; i++) {
            current = fPrev + fPrevPrev;
            
            fPrevPrev = fPrev;
            fPrev = current;
        }
        return current;
    }

    public static String thueMorseWord(int n) {
        String current = "a";
        for (int i = 0; i < n; i++) {
            StringBuilder next = new StringBuilder();
            for (char c : current.toCharArray()) {
                if (c == 'a') next.append("ab");
                else next.append("ba");
            }
            current = next.toString();
        }
        return current;
    }

    public static String generateRandomWord(int alphaSize, int minLen, int maxLen) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int length=0;
        if(maxLen-minLen+1<=0){
            length=minLen;
        }else{
        length = random.nextInt(maxLen - minLen + 1) + minLen;
        }
        char[] fullAlphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        
        for (int i = 0; i < length; i++) {
            int charIndex = random.nextInt(alphaSize);
            sb.append(fullAlphabet[charIndex]);
        }
        return sb.toString();
    }
}