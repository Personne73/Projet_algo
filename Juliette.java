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
    public static void main(String[] Args){
        int n = 10, Hmax = 10; // Juliette peut travailler jusqu'à Hmax heures.
        int[][] E = estimations(n,Hmax); // notes aléatoires, croissantes selon h.
        System.out.printf("Nombre d'unités : %d \n", n);
        System.out.println("Notes estimées : ");
        afficher(E);
        // Juliette travaille H heures, 0 ≤ H < Hmax + 1. Affichage des sommes maximum.
        for (int H = 0; H < Hmax+1; H = H + 1){ // H = nombre d'heures de révision
            System.out.printf("\nNOMBRE D'HEURES TRAVAILLEES : %d\n", H);
            int[][] E_H = estimationsRestreintes(E,H); // notes estimées pour 0 ≤ h < H+1
            int[][][] MA = calculerMA(E_H);
            int[][] M = MA[0], A = MA[1];
            System.out.printf("Somme maximum des notes : %d\n", M[n][H]);
            float moyenneMaximum = (float) M[n][H]/n;
            String strDouble = String.format("%.2f", moyenneMaximum);
            System.out.printf("Moyenne maximum : %s/20\n", strDouble);
            System.out.println("Une répartition optimale" +
                    "........... :");
            aro(A,E_H,n,H);
            ////////////////////////////////////////////////////////////////////////////////
            int MGlouton = calculerMAGlouton(E_H, H);
            System.out.printf("Somme maximum des notes gloutonnes : %d\n", MGlouton);
            System.out.printf("Moyenne maximum gloutonne : %f/20\n", (float)MGlouton/n);
            if (M[n][H] == 20 * n){
                System.out.println("\n *** inutile de travailler plus... ***");
                return;
            }
        }
    }

    static int[][][] calculerMA(int[][] E){	// E : tableau des notes estimées.
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

    public static int calculerMAGlouton(int[][] E, int H) {
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

        // cas général
        while(H != 0) {
            gain = E[0][Sauvegarde[0]+1];// - E[0][Sauvegarde[0]];
            j = 0;

            for(int i = 1; i < n; i++){
                maximum = Math.max(E[i][Sauvegarde[i]+1] /*- E[i][Sauvegarde[i]]*/, gain);
                if(maximum == E[i][Sauvegarde[i]+1]){ //- E[i][Sauvegarde[i]]){
                    gain = E[i][Sauvegarde[i]+1]; //- E[i][Sauvegarde[i]];
                    j = i;
                }
            }
            Sauvegarde[j] += 1;
            MGlouton += maximum;
            H--;
        }
        return MGlouton;
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

    static int[][] estimations(int n, int H){ // retourne E[0:n][0:H+1] de terme général
        // E[i][h] = e(i,h). Les estimations sont aléatoires, croissantes selon h.
        int[][] E = new int[n][H+1];
        Random rand = new Random(); // pour génération aléatoire des notes estimées.
        for (int i = 0; i < n; i++) E[i][0] = 6 + rand.nextInt(5);
        for (int i = 0; i < n; i++)
            for (int h = 1; h < H+1; h++)
                E[i][h] = min( E[i][h-1] + (1+rand.nextInt(5)), 20) ;
        return E;
    }

    static int[][] estimationsRestreintes(int[][] E, int H){ int n = E.length;
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


}
/* Compilation et exemple d'exécution du programme :
% javac Juliette.java
% java Juliette 10 100
Nombre d'unités : 10
Notes estimées :
[
i = 0 [7, 9, 10, 12, 16, 20, ...]
i = 1 [6, 9, 12, 17, 18, 20, ...]
i = 2 [10, 12, 13, 15, 16, 19, 20, ...]
i = 3 [9, 13, 18, 19, 20, ...]
i = 4 [9, 11, 16, 17, 20, ...]
i = 5 [7, 12, 14, 17, 20, ...]
i = 6 [7, 11, 15, 16, 17, 20, ...]
i = 7 [6, 11, 12, 14, 17, 20, ...]
i = 8 [8, 12, 17, 20, ...]
i = 9 [9, 11, 12, 17, 19, 20, ...]
]

NOMBRE D'HEURES TRAVAILLEES : 0
Somme maximum des notes : 78
Moyenne maximum : 7,80/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 0 heures, note estimée 9
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 0 heures, note estimée 7
unité 6, <-- 0 heures, note estimée 7
unité 7, <-- 0 heures, note estimée 6
unité 8, <-- 0 heures, note estimée 8
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 1
Somme maximum des notes : 83
Moyenne maximum : 8,30/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 0 heures, note estimée 9
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 0 heures, note estimée 7
unité 7, <-- 0 heures, note estimée 6
unité 8, <-- 0 heures, note estimée 8
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 2
Somme maximum des notes : 88
Moyenne maximum : 8,80/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 0 heures, note estimée 9
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 0 heures, note estimée 7
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 0 heures, note estimée 8
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 3
Somme maximum des notes : 92
Moyenne maximum : 9,20/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 0 heures, note estimée 7
unité 7, <-- 0 heures, note estimée 6
unité 8, <-- 0 heures, note estimée 8
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 4
Somme maximum des notes : 97
Moyenne maximum : 9,70/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 0 heures, note estimée 7
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 0 heures, note estimée 8
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 5
Somme maximum des notes : 101
Moyenne maximum : 10,10/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 1 heures, note estimée 11
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 0 heures, note estimée 8
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 6
Somme maximum des notes : 106
Moyenne maximum : 10,60/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 0 heures, note estimée 7
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 7
Somme maximum des notes : 110
Moyenne maximum : 11,00/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 1 heures, note estimée 11
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 8
Somme maximum des notes : 114
Moyenne maximum : 11,40/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 0 heures, note estimée 6
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 9
Somme maximum des notes : 117
Moyenne maximum : 11,70/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 0 heures, note estimée 7
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 10
Somme maximum des notes : 121
Moyenne maximum : 12,10/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 1 heures, note estimée 11
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 11
Somme maximum des notes : 125
Moyenne maximum : 12,50/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 0 heures, note estimée 9
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 12
Somme maximum des notes : 128
Moyenne maximum : 12,80/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 1 heures, note estimée 11
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 13
Somme maximum des notes : 132
Moyenne maximum : 13,20/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 14
Somme maximum des notes : 135
Moyenne maximum : 13,50/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 15
Somme maximum des notes : 137
Moyenne maximum : 13,70/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 3 heures, note estimée 17
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 16
Somme maximum des notes : 140
Moyenne maximum : 14,00/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 17
Somme maximum des notes : 143
Moyenne maximum : 14,30/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 18
Somme maximum des notes : 145
Moyenne maximum : 14,50/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 19
Somme maximum des notes : 148
Moyenne maximum : 14,80/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 1 heures, note estimée 12
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 20
Somme maximum des notes : 151
Moyenne maximum : 15,10/20
Une répartition optimale :
unité 0, <-- 0 heures, note estimée 7
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 21
Somme maximum des notes : 153
Moyenne maximum : 15,30/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 22
Somme maximum des notes : 156
Moyenne maximum : 15,60/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 23
Somme maximum des notes : 158
Moyenne maximum : 15,80/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 1 heures, note estimée 12
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 0 heures, note estimée 9

NOMBRE D'HEURES TRAVAILLEES : 24
Somme maximum des notes : 161
Moyenne maximum : 16,10/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 25
Somme maximum des notes : 164
Moyenne maximum : 16,40/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 26
Somme maximum des notes : 166
Moyenne maximum : 16,60/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 1 heures, note estimée 12
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 27
Somme maximum des notes : 168
Moyenne maximum : 16,80/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 1 heures, note estimée 11
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 28
Somme maximum des notes : 170
Moyenne maximum : 17,00/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 2 heures, note estimée 17
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 29
Somme maximum des notes : 173
Moyenne maximum : 17,30/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 30
Somme maximum des notes : 175
Moyenne maximum : 17,50/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 1 heures, note estimée 12
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 31
Somme maximum des notes : 177
Moyenne maximum : 17,70/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 0 heures, note estimée 10
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 32
Somme maximum des notes : 179
Moyenne maximum : 17,90/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 1 heures, note estimée 12
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 33
Somme maximum des notes : 181
Moyenne maximum : 18,10/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 1 heures, note estimée 12
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 4 heures, note estimée 19

NOMBRE D'HEURES TRAVAILLEES : 34
Somme maximum des notes : 182
Moyenne maximum : 18,20/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 2 heures, note estimée 16
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 35
Somme maximum des notes : 184
Moyenne maximum : 18,40/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 1 heures, note estimée 12
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 36
Somme maximum des notes : 186
Moyenne maximum : 18,60/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 37
Somme maximum des notes : 188
Moyenne maximum : 18,80/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 4 heures, note estimée 19

NOMBRE D'HEURES TRAVAILLEES : 38
Somme maximum des notes : 189
Moyenne maximum : 18,90/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 5 heures, note estimée 20
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 2 heures, note estimée 15
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 39
Somme maximum des notes : 191
Moyenne maximum : 19,10/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 40
Somme maximum des notes : 193
Moyenne maximum : 19,30/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 3 heures, note estimée 17
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 4 heures, note estimée 19

NOMBRE D'HEURES TRAVAILLEES : 41
Somme maximum des notes : 194
Moyenne maximum : 19,40/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 5 heures, note estimée 20
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 3 heures, note estimée 17

NOMBRE D'HEURES TRAVAILLEES : 42
Somme maximum des notes : 196
Moyenne maximum : 19,60/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 5 heures, note estimée 20
unité 2, <-- 5 heures, note estimée 19
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 4 heures, note estimée 19

NOMBRE D'HEURES TRAVAILLEES : 43
Somme maximum des notes : 197
Moyenne maximum : 19,70/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 5 heures, note estimée 20
unité 2, <-- 6 heures, note estimée 20
unité 3, <-- 2 heures, note estimée 18
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 4 heures, note estimée 19

NOMBRE D'HEURES TRAVAILLEES : 44
Somme maximum des notes : 198
Moyenne maximum : 19,80/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 5 heures, note estimée 20
unité 2, <-- 6 heures, note estimée 20
unité 3, <-- 3 heures, note estimée 19
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 4 heures, note estimée 19

NOMBRE D'HEURES TRAVAILLEES : 45
Somme maximum des notes : 199
Moyenne maximum : 19,90/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 5 heures, note estimée 20
unité 2, <-- 6 heures, note estimée 20
unité 3, <-- 4 heures, note estimée 20
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 4 heures, note estimée 19

NOMBRE D'HEURES TRAVAILLEES : 46
Somme maximum des notes : 200
Moyenne maximum : 20,00/20
Une répartition optimale :
unité 0, <-- 5 heures, note estimée 20
unité 1, <-- 5 heures, note estimée 20
unité 2, <-- 6 heures, note estimée 20
unité 3, <-- 4 heures, note estimée 20
unité 4, <-- 4 heures, note estimée 20
unité 5, <-- 4 heures, note estimée 20
unité 6, <-- 5 heures, note estimée 20
unité 7, <-- 5 heures, note estimée 20
unité 8, <-- 3 heures, note estimée 20
unité 9, <-- 5 heures, note estimée 20

 *** inutile de travailler plus... ***
%

*/