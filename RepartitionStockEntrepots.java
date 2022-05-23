import java.util.Arrays;
import java.util.Random;

public class RepartitionStockEntrepots {

    public static void main(String[] Argv){
        System.out.println("Exercice 3 : répartition optimale d'un stock");
        int[][] G = new int[][] // g(k,s) = gain obtenu d'une livraison 
        // d'une quantité de stock s à l'entrepôt k
            {	{ 0, 5, 5, 7, 7,10,10,12,12,13,13}, // 
                { 0, 8,10,10,10,12,12,14,14,14,14},
                {0,10,10,12,12,13,13,14,15,16,16},
                {0,14,14,14,16,16,16,16,16,16,16},
                {0,10,14,14,14,14,14,14,14,16,16},
                {0,10,12,12,16,16,16,16,16,16,16},
                {0,12,12,14,14,15,15,15,17,17,17}
            } ;		
            int K = G.length, S = G[0].length - 1;
            System.out.println("tableau des gain : g(k,s) = gain obtenu en livrant s à k");
            afficher(G);
            int[][][] MA = calculerMA(G);
            int[][] M = MA[0], A = MA[1];
            System.out.printf("gain total maximum : %d\n", M[K][S]);
            System.out.println("tableau M des gains maximum :");
            afficher(M); 
            System.out.println("une affectation optimale :");
            //aro(M,A,G,K,S);
            System.out.println();
    }

    /* Exercice 3 : répartition optimale d'un stock S sur n entrepôts/*
	m(k,s) : gain d'une répartition optimale d'un stock s sur le sous-ensemble
	des k premiers entrepôts. */
	static int[][][] calculerMA(int[][] G) { // G[0:n][0:S+1] de terme général
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

    public static int calculerMAGlouton(int[][] G){
        return 0;
    }

    static void aro(int[][] M, int[][] A, int[][] G){ /* affichage d'une répartition
	optimale du stock S sur les n entrepôts. G
	G : tableau des gains (g(i,s) = gain d'une livraison d'un stock s à l'entrepôt i)
	G est à n lignes et S+1 colonnes où n est le nombre d'entrepôts et S le stock total.
	M est le tableau de terme général m(k,s) = gain d'une répartition optimale d'un
	stock s sur le sous-ensemble des k premiers entrepôts. M est à n+1 lignes et S+1 col.
	A = arg M : a(k,s) = quantité de stock livré au k-ème entrepôt (de numéro k-1)
	dans une répartition optimale du stock s sur les k premiers entrepôts. */
        int n = G.length, S = G.length - 1;
        aro(M,A,G,n,S); // afficher une répartition optimale du stock S sur le
            // sous-ensemble des n premiers entrepôts. Autrement dit : afficher une
            // répartition optimale du stock S sur tous les entrepôts (sans contrainte.)
        }

    static void aro(int[][] M, int[][] A, int[][] G, int k, int s){ /* affichage d'une
		répartition optimale du stock s sur le sous-ensemble des k premiers entrepôts.
		Notation : ro(k,s) = répartition optimale du stock s sur le sous-ensemble [0:k] */
            // base = condition d'arrêt
        if (k == 0) return;
        // cas général
        if(M[k][s] == M[k-1][s]) {
            aro(M, A, G, k - 1, s);
        }// k-ième objet non pris
        else if(s-G[k-1][s]>=0 && M[k][s] == M[k-1][s-G[k-1][s]] + G[k-1][s]){
            aro(M,A,G,k-1,s-G[k-1][s]);
            System.out.printf("Entrepôt %d : stock : %d, gain : %d\n", k-1, G[k-1][s], G[k-1][s]);
            }
        else{
            aro(M,A,G,k-1,s-G[k-1][s]);
            System.out.printf("Entrepôt %d : stock : %d, gain : %d\n", k-1, G[k-1][s], G[k-1][s]);
        }
        // cas général : appel récursif
    }

    static int max(int x, int y){ if (x >= y) return x; return y;}
	static int max(int x, int y, int z){ if (x >= max(y,z)) return x; 
		if (y >= z) return y; 
		return z;
	}	

    static void afficher(int[][] T){int n = T.length;
		for (int i = n-1; i >= 0; i--)
			System.out.println(Arrays.toString(T[i]));
	}
}
