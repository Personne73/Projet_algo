import java.util.Arrays;
import java.util.Random;

/**
 * Programme quickSort fourni par Monsieur Natowicz en cours, mais légèrement modifié par nos soins
 */
public class QuickSortDisplay {
    public void quickSortDisplay(Triplet[] T){ int n = T.length;
        int nblancs = 0; // affichage indenté de nblancs
        qsd(T,0,n); // qsd = quicksort display
    }

    public void qsd(Triplet[] T, int i, int j){
        if (j-i < 2) {
            return; // T[i:j] est croissant
        }
        int k = segmenter(T, i, j); // T[i:k] ≤ T[k:k+1] < T[k+1:j]
        qsd(T, i, k); // T[i:k] est croissant
        qsd(T, k+1, j); // T[k+1:j] est croissant
        // T[i:k] et T[k+1:j] croissants et T[i:k] ≤ T[k:k+1] < T[k+1:j]
        // donc T[i:j] croissant.
    }

    public int segmenter(Triplet[] T, int i, int j){
        // calcule une permutation des valeurs de T[i:j] qui vérifie
        // T[i:k] ≤ T[k:k+1] < T[k+1:j], et retourne l'indice k.
        // I(k,j') : T[i:k] ≤ T[k:k+1] < T[k+1:j']

        int h = hasard(i,j); // h est choisi au hasard dans l'intervalle [i:j]
        permuter(T,i,h);
        int k = i, jprime = k+1; // I(k,j') est vraie
        while (jprime < j)
            if (T[jprime].getValeur() < T[k].getValeur()) // I(k,j'+1) est vraie
                jprime = jprime+1;
            else { permuter(T,jprime,k+1);
                permuter(T,k+1,k);
                // I(k+1,j'+1) est vraie
                k = k+1; // I(k,j'+1) est vraie
                jprime = jprime+1; // I(k,j') est vraie
            }
        // I(k,j) vraie, i.e. T[i:k] ≤ T[k:k+1] < T[k+1:j]

        return k;
    }
    public void quickSortDisplayRatio(Triplet[] T){ int n = T.length;
        int nblancs = 0; // affichage indenté de nblancs
        qsdRatio(T,0,n); // qsd = quicksort display
    }

    public void qsdRatio(Triplet[] T, int i, int j){
        if (j-i < 2) {
            return; // T[i:j] est croissant
        }
        int k = segmenterRatio(T, i, j); // T[i:k] ≤ T[k:k+1] < T[k+1:j]
        qsdRatio(T, i, k); // T[i:k] est croissant
        qsdRatio(T, k+1, j); // T[k+1:j] est croissant
        // T[i:k] et T[k+1:j] croissants et T[i:k] ≤ T[k:k+1] < T[k+1:j]
        // donc T[i:j] croissant.
    }

    public int segmenterRatio(Triplet[] T, int i, int j){
        // calcule une permutation des valeurs de T[i:j] qui vérifie
        // T[i:k] ≤ T[k:k+1] < T[k+1:j], et retourne l'indice k.
        // I(k,j') : T[i:k] ≤ T[k:k+1] < T[k+1:j']

        int h = hasard(i,j); // h est choisi au hasard dans l'intervalle [i:j]
        permuter(T,i,h);
        int k = i, jprime = k+1; // I(k,j') est vraie
        while (jprime < j)
            if (T[jprime].getRatio() < T[k].getRatio()) // I(k,j'+1) est vraie
                jprime = jprime+1;
            else { permuter(T,jprime,k+1);
                permuter(T,k+1,k);
                // I(k+1,j'+1) est vraie
                k = k+1; // I(k,j'+1) est vraie
                jprime = jprime+1; // I(k,j') est vraie
            }
        // I(k,j) vraie, i.e. T[i:k] ≤ T[k:k+1] < T[k+1:j]

        return k;
    }

    static Random r = new Random(); // utilisé par segmenter(...) et hasard(...)
    public int hasard(int i, int j){
        // retourne un indice au hasard dans l'intervalle [i:j]
        return i + r.nextInt(j-i); // nextInt(j-i) est dans [0:j-i]
    }
    public void permuter(Triplet[] T, int i, int j){
        Triplet ti = T[i];
        T[i] = T[j];
        T[j] = ti;
    }

    /*public static void indenter(int nblancs){ for (int i = 0; i<nblancs; i++) System.out.print(" ");}
    static void afficher(int T, int i, int j){
        int[] Tij = Arrays.copyOfRange(T,i,j);  //  Tij = T[i:j]
        System.out.print(Arrays.toString(Tij));
    }
    static void nl(){ System.out.println(); }// nl : new line

    static int[] tableauAleatoire(int n){
        // retourne A[0:n] à valeurs aléatoires dans [0:n]
        int[] A = new int[n];
        for (int k = 0; k < n; k++)
            A[k] = r.nextInt(n);
        return A;
    }*/
    /*public static void main(String args[]) {
        {   System.out.println();
            int n = 10;
            Triplet [] T = {new Triplet(0, 5, 2), new Triplet(1, 4, 3), new Triplet(4, 8, 2), new Triplet(2,1234,5)};
            System.out.print("T avant le tri : "); //afficher(T,0,n); System.out.println();
            for (Triplet triplet : T) {
                System.out.print(triplet.getValeur() + " ");
            }
            //quickSortDisplay(T);
            //nl();
            System.out.print("T après le tri : "); //afficher(T,0,n); System.out.println();
            for (Triplet triplet : T) {
                System.out.print(triplet.getValeur() + " ");
            }
        }
    }*/
}
