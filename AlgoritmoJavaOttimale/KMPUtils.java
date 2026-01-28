public class KMPUtils {

    // Calcolo l'array dei bordi e lo faccio in un tempo di O(n), in quanto solo il for incide sul tempo;
    // infatti il while può essere eseguito al massimo n volte in totale, poiché ogni volta che si entra nel while
    // si riduce j, che può essere incrementato al massimo n volte dal ciclo for principale. 
    // Rendendo il costo del while ammortizzato.
    // Di conseguenza ho n operazioni totali effettuate dal for, n operazioni totali effettuate dal while e 
    // ho n+n=2n operazioni totali, di conseguenza la complessità temporale è di O(n) e una complessità
    // spaziale di O(n) per l'array dei bordi.

    public static int[] borderArray(char[] text, int start, int end) {
        int m = end - start + 1;
        int[] border = new int[m];
        border[0] = 0; // il bordo del primo carattere è sempre 0
        int j = 0; // lunghezza del bordo corrente

        for (int i = 1; i < m; i++) {
            while (j > 0 && text[start + i] != text[start + j]) {
                j = border[j - 1]; // fallback al bordo precedente
            }
            // Se il carattere corrente (i) è uguale al carattere che mi aspetto dal prefisso (j),
            // significa che il bordo continua.
            // Esempio: in "aaaaa", la quinta 'a' estende il bordo. 
            //In "aaaab", la 'b' rompe la sequenza e non entro qui.
            if (text[start + i] == text[start + j]) {
                j++;
            }
            border[i] = j;
        }
        return border;
    }


    /*
    Calcolo la chiusura della parola, ogni operazione viene svolta in tempo costante, eccezion fatta per il for,
    che viene richiamato al massimo n-1 volte (n volte), di conseguenza questo metodo ha complessità di O(n),
    richiamando anche borderArray solo una volta significa che ho comunque n+n=2n operazioni svolte, pertanto 
    il risultato di O(n) come complessità
    */
    public static boolean isClosedOpt(char[] text, int start, int end) {
        int n = end - start + 1;

        //la parola ha lunghezza di al massimo 1, quindi è chiusa, anche la parola vuota è chiusa
        if(n<=1) return true; 

        int[] b = borderArray(text, start, end);

        int lenB = b[n-1];

        if(lenB==0) return false;

        for (int i = 0; i < n - 1; i++) {
            if (b[i] == lenB) {
                return false; // Occorrenza interna trovata
            }
        }

        return true; //nessuna occorrenza trovata => parola chiusa
    }

}
