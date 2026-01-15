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
}