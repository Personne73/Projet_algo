package javafile;
import java.util.Arrays;
import java.util.Random;

/**
 * Classe qui permet la résolution de l'exemple 3 du projet sur la répartition d'un stock sur un ensemble d'entrepôts
 * Certains morceaux de code ont été pris du professeur M. René Natowicz
 * (le code optimal et les fonctions nécessaires au fonctionnement de l'exercice)
 */
public class RepartitionStockEntrepots {

    /**
     * Répartition optimale d'un stock S sur n entrepôts
     * @author René Natowicz
     * @param G tableau des gains de chaque entrepôt en fonction du stock
     * @return un tableau 3D : un 2D de la solution optimal et son tableau d'argument de la solution optimal
     */
	public int[][][] calculerMA(int[][] G) {
        // m(k,s) : gain d'une répartition optimale d'un stock s sur le sous-ensemble des k premiers entrepôts
        // G[0:n][0:S+1] de terme général
        // G[i][s] = gain d'une livraison d'un stock s à l'entrepôt i.
        // Calcule : M[0:n+1][0:S+1] de tg M[k][s] = m(k,s) et A = arg M.
        // Retourne : int[][][] MA = {M,A}.
        int n = G.length;
        int S = G[0].length - 1;
        int[][] M = new int[n + 1][S + 1], A = new int[n + 1][S + 1];
        // Base :
        for (int s = 0; s < S + 1; s++)
            M[0][s] = 0;
        for (int k = 0; k < n + 1; k++)
            A[k][0] = 0;
        // cas général
        for (int k = 1; k < n + 1; k++) {
            for (int s = 1; s < S + 1; s++) {
                M[k][s] = max(M[k - 1][s], G[k - 1][s] + M[k - 1][s - 1]);
                if (M[k][s] == M[k - 1][s])
                    A[k][s] = A[k - 1][s];
                else
                    A[k][s] = k;
            }
        }
        return new int[][][]{M, A};
    }

    /**
     * Fonction qui calcul le gain maximum selon la répartition gloutonne
     * @param G tableau des gains de chaque entrepôt en fonction du stock
     * @return le gain glouton
     */
    public int calculerMAGlouton(int[][] G) {
        // Initialisation : MGlouton = 0, maximum = 0, gain = 0, j = 0
        // Invariant : I(maximum, MGlouton, j) --> I(max(G[i][Save[i]+1] - G[i][Save[i]]), MGlouton + maximum, gain), i)
        // Condition d'arrêt : S = 0
        int n = G.length; // nombre entrepot
        int S = G[0].length - 1; // stock max d'un entrepot

        int MGlouton = 0;
        int[] save = new int[n]; // dans chaque entrepot la quantité de stock qu'il contient

        int gain;
        int maximum = 0;
        int j;

        //si on a un seul entrepôt
        if(n == 1) return G[0][S];

        // cas général
        while(S != 0) {
            gain = G[0][save[0]+1] - G[0][save[0]];
            j = 0;

            for(int i = 1; i < n; i++){
                maximum = max(G[i][save[i]+1] - G[i][save[i]], gain);

                if(maximum == G[i][save[i]+1] - G[i][save[i]]){
                    gain = G[i][save[i]+1] - G[i][save[i]];
                    j = i;
                }

            }

            save[j] += 1;
            MGlouton += maximum;
            S--;
        }

        return MGlouton;
    } // fonction de complexité Θ(n²)

    /**
     * Fonction qui permet de calculer le tableau de la distance relative entre
     * la solution optimal et la solution gloutonne lors de chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
    public float[] calculerD() {
        int Smax = 1000; // le stock maximum
        int Nmax = 1000; // nombre maximum d'entrepôts
        int Gmax = 100; // le gain maximum
        int Nruns = 5000;

        float[] D = new float[Nruns];
        Random random = new Random();

        for (int r = 0; r < Nruns; r++) {
            int S = 1 + random.nextInt(Smax + 1); // choix du stock aléatoire des entrepôts
            int n = 1 + random.nextInt(Nmax + 1); // choix du nombre d'entrepôts

            int[][] G = new int[n][S];
            for(int i = 0; i < n; i++){
                G[i][0] = 0;
            }
            for (int i = 0; i < n; i++) {
                for (int s = 1; s < S; s++){
                    G[i][s] = 1 + random.nextInt(Gmax + 1);
                }
                Arrays.sort(G[i]); // trie des valeurs de chaque ligne dans l'ordre croissant
            }

            // calcul de la valeur optimal du gain
            int[][][] MA = calculerMA(G);
            int[][] M = MA[0];
            float v_etoile = M[n][S-1];

            // la valeur maximum du chemin glouton
            float g = calculerMAGlouton(G);

            D[r] = (v_etoile - g) / (1 + v_etoile);
        }

        return D;
    }

    /**
     * Fonction qui calcul le maximum entre deux valeurs
     * @author René Natowicz
     * @param x valeur 1
     * @param y valeur 2
     * @return le maximum
     */
    public int max(int x, int y){ if (x >= y) return x; return y;}


} // fin de classe

