import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe qui permet la résolution de l'exemple 1 du projet en rapport avec le petit robot
 */
public class CheminDuPetitRobot {

	final int plusInfini = Integer.MAX_VALUE;

    /*public static void main(String[] Args){
        System.out.println("Exercice 6 : le petit robot");
		int L = 5, C = 7; // grille 5 x 7
		System.out.printf("Grille à %d lignes et %d colonnes\n",L,C);
		int[][] matrix = matrice(L, C, 10);
		int[][] M = calculerM(L,C, matrix);
		System.out.println("Tableau M des coûts minimum :");
		afficher(M);
		System.out.println("Matrice des coûts :");
		afficher(matrix);
		System.out.printf("Coût minimum d'un chemin de (0,0) à (%d,%d) = %d\n",
			L-1,C-1,M[L-1][C-1]);
		System.out.println();

		int MGlouton = calculerMGlouton(L, C, matrix);
		System.out.printf("Coût minimum d'un chemin glouton de (0,0) à (%d,%d) = %d\n", L-1,C-1, MGlouton);
    }*/

    /* Exercice 6 : le petit robot. */
	public int[][] calculerM(int L, int C, int[][] matrix){ // une grille L x C
		int[][] M = new int[L][C]; // de terme général M[l][c] = m(l,c)
		// base 
		M[0][0] = 0;
		for(int c = 1; c < C; c++) M[0][c] = M[0][c-1] + e(0, c-1, L, C, matrix);
		for(int l = 1; l < L; l++) M[l][0] = M[l-1][0] + n(l-1, 0, L, C, matrix);
		// cas général
		for(int l = 1; l < L; l++)
			for(int c = 1; c < C; c++)
				M[l][c] = min(M[l-1][c] + n(l-1, c, L, C, matrix),
						M[l][c-1] + e(l, c-1, L, C, matrix),
						M[l-1][c-1] + ne(l-1, c-1, L, C, matrix));
		return M;
	}

	/**
	 * Fonction qui permet de calculer le coût minimum glouton d'un chemin de (0,0) à (l,c)
	 * @param L le nombre de ligne de la grille
	 * @param C le nombre de colonne de la grille
	 * @return le coût minimum glouton d'un chemin de (0,0) à (l,c)
	 */
	public int calculerMGlouton(int L, int C, int[][] matrix){
		// Initialisation : Mglouton = 0, l = 0, c = 0
		// Base : l = 0 et c = 0, MGlouton = min(ne(l,c,L,C), n(l,c,L,C), e(l,c,L,C))
		// Invariant : 0 < l < L ou 0 < c < C MGlouton += min(n(l-1, c, L, C), e(l, c-1, L, C), ne(l-1, c-1, L, C))
		// Condition d'arrêt : l >= L-1 et c >= C-1
		int l = 0, c = 0, MGlouton = 0;

		// base
		/*MGlouton = min(ne(l, c, L, C,matrix), n(l, c, L, C, matrix), e(l, c, L, C, matrix));
		if( min(ne(l, c, L, C,matrix), n(l, c, L, C, matrix), e(l, c, L, C, matrix)) == ne(l, c, L, C, matrix) ){
			l++;
			c++;
		} else if( min(ne(l, c, L, C,matrix), n(l, c, L, C, matrix), e(l, c, L, C, matrix)) == n(l, c, L, C, matrix) ){
			l++;
		} else {
			c++;
		}*/

		// cas général
		while( l < L-1 || c < C-1){
			MGlouton += min(n(l, c, L, C, matrix), e(l, c, L, C, matrix), ne(l, c, L, C, matrix));
			if(min(n(l, c, L, C, matrix), e(l, c, L, C, matrix), ne(l, c, L, C, matrix)) == ne(l, c, L, C, matrix)){
				l++;
				c++;
			} else if(min(n(l, c, L, C, matrix), e(l, c, L, C, matrix), ne(l, c, L, C, matrix)) == n(l, c, L, C, matrix)){
				l++;
			} else {
				c++;
			}
		}

		return MGlouton;
	}

	public int[][] matrice(int L, int C, int coutMax){
		int[][] matrix = new int[L+1][C+1];
		Random rand = new Random();
		for(int i = 0; i < L+1; i++){
			for(int j = 0; j < C+1; j++){
				matrix[i][j] = rand.nextInt(coutMax + 1);
			}
		}
		return matrix;
	}

	/**
     * Fonction qui permet de calculer le tableau de la distance relative entre chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
    public float[] calculerD(){
        int Lmax = 1000; // nombre de ligne maximum
		int Cmax = 1000; // nombre de colonne maximum
        int Nruns = 5000; // nombre de simulations/runs de l'évaluation statistique
        int coutMax = 100;

        float[] D = new float[Nruns]; // tableau de la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton pour chaque run
        Random random = new Random(); // générateur de nombres aléatoires

        for(int r = 0; r < Nruns; r++){
			// nombre de lignes et de colonnes de la grille
            int L = random.nextInt(Lmax + 1) + 1;
            int C = random.nextInt(Cmax + 1) + 1;

			// choix de la matrice des coûts des directions
			int[][] matrix = matrice(L, C, coutMax);

            int[][] M = calculerM(L, C, matrix);
            float v_etoile = M[L-1][C-1];

            float g = calculerMGlouton(L, C, matrix); // la valeur du chemin glouton

            D[r] = (g - v_etoile) / (1 + v_etoile);
        }
        
        return D; 
    }

    /* Fonctions de coût des déplacements.
	1) depuis la case 00, les déplacements N et E coûtent 1, le déplacement NE coûte 0.
	2) sur la colonne 0, les autres déplacements coûtent 0
	3) sur la ligne L-1 les déplacements E coûtent 0
	4) tous les autres déplacements coûtent 1.
	Chemin de coût minimum : 
		00 -1-> 10 -0-> ... -0-> (L-1)0 -0-> (L-1)1 -0-> ... -0-> L(L-1)(C-1).
	Il est de coût 1.
	*/ 
	public int n(int l, int c, int L, int C, int[][] matrix){
		if (l==L-1) return plusInfini;
		return matrix[l+1][c];
	}
	
	public int ne(int l, int c, int L, int C, int[][] matrix){
		if (l == L-1 || c == C-1) return plusInfini;
		return NE;
	}

	public int e(int l, int c, int L, int C, int[][] matrix) {
		if (c == C - 1) return plusInfini;
		return matrix[l][c+1];
	}

	public int min(int x, int y){ if (x <= y) return x; return y;}
	public int min(int x, int y, int z){ if (x <= min(y,z)) return x;
		if (y <= z) return y; 
		return z;
	}

	public static void afficher(int[][] T){int n = T.length;
		for (int i = n-1; i >= 0; i--)
			System.out.println(Arrays.toString(T[i]));
	}

}