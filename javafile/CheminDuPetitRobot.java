package javafile;

import java.util.Random;

/**
 * Classe qui permet la résolution de l'exemple 1 du projet en rapport avec le chemin du petit robot
 * Certains morceaux de code ont été pris du professeur M. René Natowicz
 * (le code optimal et les fonctions nécessaires au fonctionnement de l'exercice)
 * mais par la suite modifié par nos soins pour la résolution de l'exercice
 */
public class CheminDuPetitRobot {

	final int plusInfini = Integer.MAX_VALUE;

	/**
	 * Fonction optimale permettant le calcul du chemin de coût minimum du petit robot
	 * @author René Natowicz (code adapté à nos besoins)
	 * @param L le nombre de lignes de la grille
	 * @param C le nombre de colonnes de la grille
	 * @param matrix grille de coûts
	 * @return un tableau contenant les coûts des différents chemins
	 */
	public int[][] calculerM(int L, int C, int[][] matrix){
		// de terme général M[l][c] = m(l,c)
		int[][] M = new int[L][C];

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
	 * @param L le nombre de lignes de la grille
	 * @param C le nombre de colonnes de la grille
	 * @param matrix la grille de coûts
	 * @return le coût minimum glouton d'un chemin de (0,0) à (l,c)
	 */
	public int calculerMGlouton(int L, int C, int[][] matrix){
		// Initialisation : Mglouton = 0, l = 0, c = 0
		// Base : l = 0 et c = 0, MGlouton = min(ne(l,c,L,C,matrix), n(l,c,L,C,matrix), e(l,c,L,C,matrix))
		// Invariant : 0 < l < L-1 ou 0 < c < C-1 MGlouton += min(n(l, c, L, C, matrix), e(l, c, L, C, matrix), ne(l, c, L, C, matrix))
		// Condition d'arrêt : l >= L-1 et c >= C-1
		int l = 0, c = 0, MGlouton = 0;

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
	} // fonction de complexité Θ(n)

	/**
	 * Fonction qui permet de générer une matrice de coûts aléatoires
	 * @param L le nombre de lignes de la grille
	 * @param C le nombre de colonnes de la grille
	 * @param coutMax le coût max pouvant être dans une case
	 * @return une matrice de coûts aléatoire
	 */
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
	 * Fonction qui permet de calculer le tableau de la distance relative entre
	 * la solution optimal et la solution gloutonne lors de chaque runs
	 * @return le tableau de la distance relative entre chaque runs
	 */
    public float[] calculerD(){
        int Lmax = 1000; // nombre de ligne maximum
		int Cmax = 1000; // nombre de colonne maximum
		int coutMax = 100; // coût maximum
        int Nruns = 5000;

		// tableau de la distance relative entre la valeur du chemin de coût minimum et la valeur du chemin glouton pour chaque run
        float[] D = new float[Nruns];
        Random random = new Random();

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

	/**
	 * Fonction de coût de déplacements vers le Nord
	 * @author René Natowicz (code adapté à nos besoins)
	 * @param l la coordonnée l du robot
	 * @param c la coordonnée c du robot
	 * @param L le nombre de lignes de la grille
	 * @param C le nombre de colonnes de la grille
	 * @param matrix la matrice des coûts
	 * @return le coût permettant un déplacement au nord
	 */
	public int n(int l, int c, int L, int C, int[][] matrix){
		if (l==L-1) return plusInfini;
		return matrix[l+1][c];
	}

	/**
	 * Fonction de coût de déplacements vers le NordEst
	 * @author René Natowicz (code adapté à nos besoins)
	 * @param l la coordonnée l du robot
	 * @param c la coordonnée c du robot
	 * @param L le nombre de lignes de la grille
	 * @param C le nombre de colonnes de la grille
	 * @param matrix la matrice des coûts
	 * @return le coût permettant un déplacement au nord-est
	 */
	public int ne(int l, int c, int L, int C, int[][] matrix){
		if (l == L-1 || c == C-1) return plusInfini;
		return matrix[l+1][c+1];
	}

	/**
	 * Fonction de coût de déplacements vers l'Est
	 * @author René Natowicz (code adapté à nos besoins)
	 * @param l la coordonnée l du robot
	 * @param c la coordonnée c du robot
	 * @param L le nombre de lignes de la grille
	 * @param C le nombre de colonnes de la grille
	 * @param matrix la matrice des coûts
	 * @return le coût permettant un déplacement à l'Est
	 */
	public int e(int l, int c, int L, int C, int[][] matrix) {
		if (c == C - 1) return plusInfini;
		return matrix[l][c+1];
	}

	/**
	 * Fonction qui calcul le minimum entre deux valeurs
	 * @author René Natowicz
	 * @param x valeur 1
	 * @param y valeur 2
	 * @return le minimum
	 */
	public int min(int x, int y){ if (x <= y) return x; return y;}

	/**
	 * Fonction qui calcul le minimum entre trois valeurs
	 * @author René Natowicz
	 * @param x valeur 1
	 * @param y valeur 2
	 * @param z valeur 3
	 * @return le minimum
	 */
	public int min(int x, int y, int z){
		if (x <= min(y,z)) return x;
		if (y <= z) return y;
		return z;
	}

} // fin de classe