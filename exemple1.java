public class exemple1{

    static final int plusInfini = Integer.MAX_VALUE, moinsInfini = Integer.MIN_VALUE;

    public static void main(String[] args){
        System.out.println("Hello World");
    }

    /* Exercice 6 : le petit robot. */
	static int[][] calculerM(int L, int C){ // une grille L x C
		int[][] M = new int[L][C]; // de terme général M[l][c] = m(l,c)
		// base 
		M[0][0] = 0;
		for(int c = 1; c < C; c++) M[0][c] = M[0][c-1] + e(0, c-1, L, C);
		for(int l = 1; l < L; l++) M[l][0] = M[l-1][0] + n(l-1, 0, L, C);
		// cas général
		for(int l = 1; l < L; l++)
			for(int c = 1; c < C; c++)
				M[l][c] = min(M[l][c-1] + e(l, c-1, L, C), 
							M[l-1][c-1] + ne(l-1, c-1, L, C), 
							M[l-1][c] + n(l-1, c, L, C));
		return M;
	}

	static void accm(int[][] M, int L, int C, int l, int c){
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
	static int n(int l, int c, int L, int C){
		if (l==L-1) return plusInfini;
		if (l==0 && c==0) return 1;
		if (c==0) return 0;
		return 1;
	}
	static int ne(int l, int c, int L, int C){
		if (l == L-1 || c == C-1) return plusInfini;
		if (l==0 && c==0) return 0;
		return 1;
	}
	static int e(int l, int c, int L, int C){
		if (c == C-1) return plusInfini;
		if (l == L-1) return 0;
		return 1;
	}	

    /* fonctions annexe */
	static int max(int x, int y){ if (x >= y) return x; return y;}
	static int max(int x, int y, int z){ if (x >= max(y,z)) return x; 
		if (y >= z) return y; 
		return z;
	}	
	static int min(int x, int y){ if (x <= y) return x; return y;}
	static int min(int x, int y, int z){ if (x <= min(y,z)) return x; 
		if (y <= z) return y; 
		return z;
	}

}
