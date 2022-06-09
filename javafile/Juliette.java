package javafile;
import java.util.Arrays;
import java.util.Random;

/*
Remarque typographique : dans ce qui suit, la notation h_n est "h indice n".
La notation h_{n-1} est "h indice n-1".
De façon générale, x_y est "x indice y" et x_{expression} est "x indice expression".
Cette notation vient du logiciel LaTeX, logiciel de formattage de textes auquel
je vous encourage à vous auto-former.

Répartition optimale d'un temps de travail -- rene.natowicz@esiee.fr -- 16/03/2022
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
            for (int h = 0; h < H+1; h++){ // calcul des valeurs m(k,h), 0 ≤ h < H+1
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

    public int calculerMAGlouton(int[][] E, int H) {
        // Initialisation : MGlouton = 0, maximum = 0, gain = 0, j = 0
        // Invariant : I(MGlouton, maximum, j) --> I(MGlouton + maximum,
        //                                                  max(G[i][Sauvegarde[i]+1] - G[i][Sauvegarde[i]], gain), i)
        // Condition d'arrêt : S = 0
        int n = E.length; // nombre d'unités
        //int H = E[0].length - 1; // stock max d'un entrepot (puisque le premier élément de l'entrepôt est égale à 0 parce que quand il n'y pas de stock il n'y aucun gain)

        int MGlouton = 0;
        int[] Sauvegarde = new int[n]; // dans chaque entrepot la quantité de stock qu'il contient

        int gain;
        int maximum = 0;
        int j;

        // cas si on a que une seule unité
        if(n == 1) return E[0][H];

        for (int[] ints : E) {
            MGlouton += ints[0];
        }

        // cas général
        while(H != 0) {
            gain = E[0][Sauvegarde[0]+1] - E[0][Sauvegarde[0]];
            j = 0;

            for(int i = 1; i < n; i++){
                maximum = Math.max(E[i][Sauvegarde[i]+1] - E[i][Sauvegarde[i]], gain);
                if(maximum == E[i][Sauvegarde[i]+1] - E[i][Sauvegarde[i]]){
                    gain = E[i][Sauvegarde[i]+1] - E[i][Sauvegarde[i]];
                    j = i;
                }
            }
            Sauvegarde[j] += 1;
            MGlouton += maximum;
            H--;
        }
        return MGlouton;
    }

    public float[] calculerD() {
        int Hmax = 1000; // le nombre d'heure max maximum
        int Nmax = 1000; // nombre maximum d'unité
        int Nruns = 5000; // nombre de simulations/runs de l'évaluation statistique

        float[] D = new float[Nruns]; // tableau de la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton pour chaque run
        Random random = new Random(); // générateur de nombres aléatoires

        for (int r = 0; r < Nruns; r++) {
            int H = random.nextInt(Hmax + 1);
            int n = 1 + random.nextInt(Nmax + 1);

            int[][] E = estimations(n, H);
            int[][] E_H = estimationsRestreintes(E,H);
            int[][][] MA = calculerMA(E_H);
            int[][] M = MA[0];

            float v_etoile = M[n][H];
            float g = calculerMAGlouton(E_H, H);
            D[r] = (v_etoile - g) / (1 + v_etoile);
        }

        return D;
    }

    static void aro(int[][] A, int[][] E, int k, int h){
        // affiche ro(k,h) : répartition optimale de h heures sur les k premières unités.
        if (k == 0) return; // sans rien faire, ro(0,h) a été affichée.
        // ici : k > 0
        // ro(k,h) = ro(k-1,h-a(k,h)) union {"k-1 <-- a(k,h)"}
        int akh = A[k][h]; // nombre d'heures allouées à la k-ème unité dans ro(k,h)
        aro(A,E,k-1,h-akh); // ro(k-1,h-akh) a été affichée
        System.out.printf("unité %d, <-- %d heures, note estimée %d\n",
                k-1, akh, E[k-1][akh]);
        // le nombre d'heures allouées à la kème unité a été affiché
        // Ainsi :
        // 1) La répartition optimale ro(k-1,h-akh) a été affichée,
        // 2) "k-1 <-- akh" a été affichée,
        // 3) donc ro(k,h) = ro(k-1,h-akh) union {"k-1 <-- akh"}
        // a été affichée.
    } // Complexité Theta(n).

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

    public int[][] estimationsRestreintes(int[][] E, int H){ int n = E.length;
        // E[0:n][0:Hmax+1]. Cette fonction retourne E[0:n][0:H+1]
        int[][] E_H = new int[n][H+1];
        for (int i = 0; i < n; i++)
            for (int h = 0; h < H+1; h++)
                E_H[i][h] = E[i][h];
        return E_H;
    }

    static void afficher(int[][] E){ int n = E.length, H = E[0].length - 1;
        // E[0:n][0:H+1] est de terme général E[i][h] = e(i,h), note estimée pour h heures
        // de révision de l'unité i. Les lignes se terminent par une suite de 20.
        // Le premier 20 est affiché. Puis ", ...]"
        // Exemple : [12, 15, 20, 20, 20] --> [12, 15, 20, ...]
        System.out.println("[");
        for (int i = 0; i < n; i++) {
            // recherche du 1er "20"
            int h = 0;
            while (h < H+1 && E[i][h] < 20) h++;
            // E[h:n] = [20, 20, ...]
            if (h == H+1)
                System.out.printf("unité %d %s\n",i, Arrays.toString(E[i]));
            else {
                int[] Ei = Arrays.copyOfRange(E[i],0,h+1);
                String Si = Arrays.toString(Ei);
                int li = Si.length();
                Si = Si.substring(0,li-1) + ", ...]";
                System.out.printf("i = %d %s\n",i,Si);
            }
        }
        System.out.println("]");
    }

    static int min(int x, int y){
        if (x<=y) return x;
        return y;
    }

} // fin de classe