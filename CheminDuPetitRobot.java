import java.util.Arrays;
import java.util.Random;

/**
 * Classe qui permet la résolution de l'exemple 1 du projet en rapport avec le petit robot
 */
public class CheminDuPetitRobot {

    static final int plusInfini = Integer.MAX_VALUE, moinsInfini = Integer.MIN_VALUE;

	/**
     * Méthode principale qui permet de lancer le programme
     * @param Args les arguments passés en ligne de commande
     */
    public static void main(String[] Args){
        System.out.println("Exercice 6 : le petit robot");
		int L = 5, C = 7; // grille 5 x 7
		System.out.printf("Grille à %d lignes et %d colonnes\n",L,C);
		int[][] M = calculerM(L,C);
		System.out.println("Tableau M des coûts minimum :");
		afficher(M);
		System.out.printf("Coût minimum d'un chemin de (0,0) à (%d,%d) = %d\n",
			L-1,C-1,M[L-1][C-1]);
		accm(M,L,C,L-1,C-1); // affichage d'un chemin de coût min de 0,0 à L-1,C-1.
		System.out.println();

		int MGlouton = calculerMGlouton(L, C);
		System.out.printf("Coût minimum d'un chemin glouton de (0,0) à (%d,%d) = %d\n", L-1,C-1, MGlouton);

		float[] D = calculerD();
        es(D);
    }

    /* Exercice 6 : le petit robot. */
	public static int[][] calculerM(int L, int C){ // une grille L x C
		int[][] M = new int[L][C]; // de terme général M[l][c] = m(l,c)
		// base 
		M[0][0] = 0;
		for(int c = 1; c < C; c++) M[0][c] = M[0][c-1] + e(0, c-1, L, C);
		for(int l = 1; l < L; l++) M[l][0] = M[l-1][0] + n(l-1, 0, L, C);
		// cas général
		for(int l = 1; l < L; l++)
			for(int c = 1; c < C; c++)
				M[l][c] = min(M[l-1][c] + n(l-1, c, L, C),
							M[l][c-1] + e(l, c-1, L, C), 
							M[l-1][c-1] + ne(l-1, c-1, L, C));
		return M;
	}

	/**
	 * Fonction qui calculer le coût minimum glouton d'un chemin de (0,0) à (l,c)
	 * @param L le nombre de ligne de la grille
	 * @param C le nombre de colonne de la grille
	 * @return le coût minimum glouton d'un chemin de (0,0) à (l,c)
	 */
	public static int calculerMGlouton(int L, int C){
		/* Initialisation : Mglouton = 0, l = 0, c = 0
		   Invariant : 0 < l < L ou 0 < c < C MGlouton += ne(l,c,L,C) si min(n(l-1, c, L, C), e(l, c-1, L, C), ne(l-1, c-1, L, C)) == ne(l-1, c-1, L, C)
								  	    	  MGlouton += n(l,c,L,C) si min(n(l-1, c, L, C), e(l, c-1, L, C), ne(l-1, c-1, L, C)) == n(l-1, c, L, C)
								  	    	  MGlouton += e(l,c,L,C) sinon
		   Base : l = 0 et c = 0, MGlouton = ne(l,c,L,C) si min(ne(l,c,L,C), n(l,c,L,C), e(l,c,L,C)) == ne(l,c,L,C)
								  MGlouton = n(l,c,L,C) si min(ne(l,c,L,C), n(l,c,L,C), e(l,c,L,C)) == n(l,c,L,C)
								  MGlouton = e(l,c,L,C) sinon
		   Condition d'arrêt : l = L-1, c = C-1*/

		int l = 0, c = 0;
		int MGlouton = 0;

		// base
		if( min(ne(l,c,L,C), n(l,c,L,C), e(l,c,L,C)) == ne(l,c,L,C) ){
			MGlouton = ne(l,c,L,C);
			l++;
			c++;
		} else if( min(ne(l,c,L,C), n(l,c,L,C), e(l,c,L,C)) == n(l,c,L,C) ){
			MGlouton = n(l,c,L,C);
			l++;
		} else {
			MGlouton = e(l,c,L,C);
			c++;
		}

		// cas général
		while( l != L || c != C){
			if(min(n(l-1, c, L, C), e(l, c-1, L, C), ne(l-1, c-1, L, C)) == ne(l-1, c-1, L, C)){
				MGlouton += ne(l-1, c-1, L, C);
				l++;
				c++;
			} else if(min(n(l-1, c, L, C), e(l, c-1, L, C), ne(l-1, c-1, L, C)) == n(l-1, c, L, C)){
				MGlouton += n(l-1, c, L, C);
				l++;
			} else {
				MGlouton += e(l, c-1, L, C);
				c++;
			}

		}

		return MGlouton;
	}

	/**
     * Fonction qui permet de calculer le tableau de la distance relative entre chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
    public static float[] calculerD(){
        int Lmax = 1000; // nombre de niveaux maximum
        int Nruns = 5000; // nombre de simulations/runs de l'évaluation statistique
        int Vmax = 100; // la plus grande valeur pouvant être présente dans le triangle

        float[] D = new float[Nruns]; // tableau de la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton pour chaque run
        Random random = new Random(); // générateur de nombres aléatoires

        for(int r = 0; r < Nruns; r++){
			// nombre de ligne et de colonne de la grille
            int L = random.nextInt(Lmax + 2) + 1; 
            int C = random.nextInt(Lmax + 2) + 1; 

            int[][] M = calculerM(L, C);
            float v_etoile = M[L-1][C-1]; 

            float g = calculerMGlouton(L, C); // la valeur du chemin glouton
            //System.out.println("v_etoile = " + v_etoile + " g = " + g + "\n");
            D[r] = (v_etoile - g) / (1 + v_etoile); // la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton
        }
        
        return D; 
    }

    /**
     * Fonction de l'évaluation statistique
     * es = Évaluation Statistique
     */
    public static void es(float[] D){
        float moyenne = moyenne(D);
        System.out.println("\nLa moyenne des valeurs de D est " + moyenne);

        float mediane = mediane(D);
        System.out.println("La médiane des valeurs de D est " + mediane);

        float ecartType = ecartType(D, moyenne);
        System.out.println("L'écart-type des valeurs de D est " + ecartType);
    }

    /**
     * Fonction qui calcule la moyenne des valeurs du tableau D
     * @param D le tableau des valeurs de la distance relative entre chaque run
     * @return la moyenne des valeurs du tableau D
     */
    public static float moyenne(float[] D){
        int n = D.length;

        // calcul de la moyenne
        float moyenne = 0;

        for(int i = 0; i < n; i++){
            moyenne += D[i];
        }

        moyenne /= n;
        return moyenne;
    }

    /**
     * Fonction qui calcule la mediane des valeurs du tableau D
     * @param D le tableau des valeurs de la distance relative entre chaque run
     * @return la mediane des valeurs du tableau D
     */
    public static float mediane(float[] D){
        int n = D.length;
        Arrays.sort(D); // on classe les valeurs par ordre croissant

        // calcul de la mediane
        float mediane = 0;

        if(n % 2 == 0){ // si le nombre de valeurs est pair
            mediane = (D[n/2] + D[n/2 - 1]) / 2;
        } else { // si le nombre de valeurs est impair
            mediane = D[n/2];
        }

        return mediane;
    }

    /**
     * Fonction qui calcule l'écart-type des valeurs du tableau D
     * @param D le tableau des valeurs de la distance relative entre chaque run
     * @param moyenne la moyenne des valeurs du tableau D
     * @return l'écart-type des valeurs du tableau D
     */
    public static float ecartType(float[] D, float moyenne){
        int n = D.length;

        // calcul de l'écart-type
        float ecartType = 0;

        for(int i = 0; i < n; i++){
            ecartType += (D[i] - moyenne) * (D[i] - moyenne);
        }

        ecartType /= n;
        ecartType = (float) Math.sqrt(ecartType);

        return ecartType;
    }

	public static void accm(int[][] M, int L, int C, int l, int c){
	// affiche un chemin de coût minimimum (ccm) de 0,0 à l,c
		// condition d'arrêt
		if (l == 0 && c == 0) {
			System.out.printf("(0,0)"); // le ccm de 0,0 à 0,0 est affiché
			return;
		} else if (l==0){
			accm(M, L, C, l-1, c);
			System.out.printf(" -%d-> (%d,%d)", e(l, c-1, L, C), l, c);
		} else if (c == 0) {
			accm(M, L, C, l-1, c);
			System.out.printf(" -%d-> (%d,%d)", n(l-1, c, L, C), l, c);
		} else {
			int n = n(l-1, c, L, C), ne = ne(l-1, c-1, L, C), e = e(l, c-1, L, C);
			int Mn = M[l-1][c] + n, Mne = M[l-1][c-1] + ne, Me = M[l][c-1] + e;
			if (Mn == min(Mn, Mne, Me)){
				accm(M, L, C, l-1, c);
				System.out.printf(" -%d-> (%d,%d)", n, l, c);
			} else if (Mne == min(Mn, Mne, Me)){
				accm(M, L, C, l-1, c-1);
				System.out.printf(" -%d-> (%d,%d)", ne, l, c);
			} else {
				accm(M, L, C, l, c-1);
				System.out.printf(" -%d-> (%d,%d)", e, l, c);
			}
		}
	
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
	public static int n(int l, int c, int L, int C){
		if (l==L-1) return plusInfini;
		if (l==0 && c==0) return 1;
		if (c==0) return 0;
		return 1;
	}
	
	public static int ne(int l, int c, int L, int C){
		if (l == L-1 || c == C-1) return plusInfini;
		if (l==0 && c==0) return 0;
		return 1;
	}

	public static int e(int l, int c, int L, int C){
		if (c == C-1) return plusInfini;
		if (l == L-1) return 0;
		return 1;
	}	

    /* fonctions annexe */
	public static int max(int x, int y){ if (x >= y) return x; return y;}
	public static int max(int x, int y, int z){ if (x >= max(y,z)) return x; 
		if (y >= z) return y; 
		return z;
	}	

	public static int min(int x, int y){ if (x <= y) return x; return y;}
	public static int min(int x, int y, int z){ if (x <= min(y,z)) return x; 
		if (y <= z) return y; 
		return z;
	}

	public static void afficher(int[][] T){int n = T.length;
		for (int i = n-1; i >= 0; i--)
			System.out.println(Arrays.toString(T[i]));
	}

}