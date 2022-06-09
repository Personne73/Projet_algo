package javafile;
import java.util.Arrays;
import java.util.Random;

/**
 * Classe qui permet la résolution de l'exemple 4 du projet sur la répartition optimale d'un temps de travail sur un ensemble d'unités
 * Certains morceaux de code ont été pris du professeur M. René Natowicz
 * (le code optimal et les fonctions nécessaires au fonctionnement de l'exercice)
 */
public class Juliette {

    /**
     * Répartition optimale d'un temps de travail sur un ensemble d'unités
     * @author René Natowicz
     * @param E tableau de l'estimation des notes des unités en fonction du temps de travail
     * @return un tableau 3D : un 2D de la solution optimal et son tableau d'argument de la solution optimal
     */
    public int[][][] calculerMA(int[][] E){	// E : tableau des notes estimées.
        // E[0:n][0:H+1] est de terme général E[i][h] = e(i,h).
        // Retourne M et A : M[0:n+1][0:H+1] de terme général M[k][h] = m(k,h), somme maximum
        // des notes d'une répartition de h heures sur le sous-ensemble des k premières unités.
        int n = E.length, H = E[0].length - 1;
        int[][] M = new int[n+1][H+1], A = new int[n+1][H+1];
        // base, k = 0.
        int s0 = 0; // somme des notes pour 0 heure travaillée
        for (int i = 0; i < n; i++)
            s0 = s0 + E[i][0];
        // Base : m(0,h) = s0 pour tout h, 0 ≤ h < H+1
        for (int h = 0; h < H+1; h++)
            M[0][h] = s0;
        // Cas général, 1 ≤ k < n+1 pour tout h, h, 0 ≤ h < H+1 :
        // m(k,h) = ( Max m(k-1, h - h_k) + e(k-1,h_k) sur h_k, 0 ≤ h_k < h+1 ) - e(k-1,0)
        // Calcul des valeur m(k,h) par k croissants et mémorisation dans le tableau M.
        // Calcul à la volée des a(k,h) = arg m(k,h) et mémorisation dans le tableau A.
        for (int k = 1; k < n+1; k++) // par tailles k croissantes
            for (int h = 0; h < H+1; h++){ // calcul des valeurs m(k,h), 0 ≤h < H+1
                // Calcul de M[k][h] =
                // ( Max M[k-1][h-h_k] + e(k-1,h_k), h_k, 0 ≤ h_k < h+1 ) - e(k-1,0)
                M[k][h] = -1;
                for (int h_k = 0; h_k < h+1; h_k++){
                    int mkhh_k = M[k-1][h - h_k] + E[k-1][h_k];
                    if (mkhh_k > M[k][h]){
                        M[k][h] = mkhh_k;
                        A[k][h] = h_k;
                    }
                }
                // M[k][h] = (max M[k-1][h-h_k] + e(k-1,h_k), h_k, 0 ≤ h_k < h+1)
                M[k][h] = M[k][h] - E[k-1][0];  // M[k][h] = m(k,h)
            }
        return new int[][][] {M, A};
    } // complexité Theta(n x H^2).

    /**
     * Fonction qui calcul la somme maximale de note gloutonne que Juliette peut avoir en fonction de son temps de travail
     * @param E tableau de l'estimation des notes des unités en fonction du temps de travail
     * @param H le nombre d'heures travaillé
     * @return la somme maximum gloutonne
     */
    public int calculerMAGlouton(int[][] E, int H) {
        // Initialisation : MGlouton = 0, maximum = 0, gain = 0, j = 0
        // Invariant : I(maximum, MGlouton, j) --> I(max(G[i][Save[i]+1] - G[i][Save[i]]), MGlouton + maximum, gain), i)
        // Condition d'arrêt : H = 0
        int n = E.length; // nombre d'unités

        int MGlouton = 0;
        int[] Save = new int[n];

        int gain;
        int maximum = 0;
        int j;

        // une seule unité
        if(n == 1) return E[0][H];

        // calcul de la note de Juliette lorsqu'elle travail 0 heures
        for (int[] ints : E) {
            MGlouton += ints[0];
        }

        // cas général
        while(H != 0) {
            gain = E[0][Save[0]+1] - E[0][Save[0]];
            j = 0;

            for(int i = 1; i < n; i++){
                maximum = Math.max(E[i][Save[i]+1] - E[i][Save[i]], gain);
                if(maximum == E[i][Save[i]+1] - E[i][Save[i]]){
                    gain = E[i][Save[i]+1] - E[i][Save[i]];
                    j = i;
                }
            }
            Save[j] += 1;
            MGlouton += maximum;
            H--;
        }
        return MGlouton;
    } // fonction de complexité Θ(n²)

    /**
     * Fonction qui permet de calculer le tableau de la distance relative entre
     * la solution optimal et la solution gloutonne lors de chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
    public float[] calculerD() {
        int Hmax = 1000; // nombre d'heures max maximum
        int Nmax = 1000; // nombre maximum d'unité
        int Nruns = 5000;

        float[] D = new float[Nruns];
        Random random = new Random();

        for (int r = 0; r < Nruns; r++) {
            int H = random.nextInt(Hmax + 1);
            int n = 1 + random.nextInt(Nmax + 1);

            int[][] E = estimations(n, H);
            int[][][] MA = calculerMA(E);
            int[][] M = MA[0];

            float v_etoile = M[n][H];
            float g = calculerMAGlouton(E, H);
            D[r] = (v_etoile - g) / (1 + v_etoile);
        }

        return D;
    }

    /**
     * Estimation aléatoire des notes en fonctions du nombre d'heures travailler pour chaque unité
     * @param n le nombre d'unité
     * @param H le nombre d'heures
     * @return une estimation des notes
     */
    public int[][] estimations(int n, int H){ // retourne E[0:n][0:H+1] de terme général
        // E[i][h] = e(i,h). Les estimations sont aléatoires, croissantes selon h.
        int[][] E = new int[n][H+1];
        Random rand = new Random(); // pour génération aléatoire des notes estimées.
        for (int i = 0; i < n; i++) E[i][0] = 6 + rand.nextInt(5);
        for (int i = 0; i < n; i++)
            for (int h = 1; h < H+1; h++)
                E[i][h] = min( E[i][h-1] + (1+rand.nextInt(5)), 20) ;
        return E;
    }

    /**
     * Fonction qui calcul le minimum entre deux valeurs
     * @author René Natowicz
     * @param x valeur 1
     * @param y valeur 2
     * @return le minimum
     */
    public int min(int x, int y){
        if (x<=y) return x;
        return y;
    }

} // fin de classe