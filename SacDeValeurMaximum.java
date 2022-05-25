import java.util.Arrays;
import java.util.Random;


/**
 * Classe qui permet la résolution de l'exemple 2 du projet sur le sac de valeur maximum
 */
public class SacDeValeurMaximum {
    
    /**
     * Méthode principale qui permet de lancer le programme
     * @param Argv les arguments passés en ligne de commande
     */
    public static void main(String[] Argv){
        int[] V = {2,1,3,8,4};
		int[] T = {1,1,2,4,3}; 
		int n = V.length;
		System.out.println("V = " + Arrays.toString(V));
		System.out.println("T = " + Arrays.toString(T));
		int C = 5; System.out.printf("C = %d\n",C);
		int[][] M = calculerM(V,T,C); 
		System.out.println("M = "); 
        afficher(M); 
		System.out.printf("Valeur des sacs de valeur maximum = M[%d][%d] = %d\n",
			n, C, M[n][C]);
		//System.out.print("Contenu d'un tel sac :\n");
		// afficher un sac de contenance C, de valeur max, contenant un sous-ensemble
		// des n objets
		//asm(M,V,T,n,C);
		//System.out.println();
        int v = calculerMGloutonValeur(V, T, C);
        System.out.print("Valeur glouton max = " + v);
    }

    public static int[][] calculerM(int[] V, int[] T, int C){
        int n = V.length;
        // Retourne M[0:n+1][0:C+1], de terme général M[k][c] = m(k,c)
        int[][] M = new int[n+1][C+1];

        // Base : m(0,c) = 0 pour toute contenance c, 0 ≤ c < C+1
        for (int c = 0; c < C+1; c++) M[0][c] = 0;

        // Cas général, pour tous k et c, 1 ≤ k < n+1, 0 ≤ c < C+1,
        // m(k,c) = max(M[k-1][c], V[k-1] + M[k-1][c-T[k-1]])
        for (int k = 1; k < n+1; k++)
            for (int c = 0; c < C+1; c++) // calcul et mémorisation de m(k,c)
                if (c-T[k-1] < 0) // le k-ème objet est trop gros pour entrer dans le sac 
                    M[k][c] = M[k-1][c];
                else  
                    M[k][c] = max(M[k-1][c], V[k-1]+M[k-1][c-T[k-1]]);
        return M;
    }

    public static int calculerMGloutonValeur(int[] V, int[] T, int C){
        int n = V.length;
        int v = 0;
        int j = 0;
        int c = 0;
        Triplet[] TabObjets = new Triplet[n];

        // création des objets du sac et on les ajoute dans le tableau de triplets
        for(int i = 0; i < n; i++) {
            Triplet vObjet = new Triplet(i, V[i], T[i]);
            TabObjets[i] = vObjet;
        }

        QuickSortDisplay qs = new QuickSortDisplay();
        qs.quickSortDisplay(TabObjets);

        while(c <= C ){
            if(j > TabObjets.length) return v;
            if(C-c == 0) return v;
            if( TabObjets[j].getTaille() <= C-c){
                c += TabObjets[j].getTaille();
                v += TabObjets[j].getValeur();
            }
            j++;
        }
        return v;
    }



    public static void asm(int[][] M, int[] V, int[] T, int k, int c){
    // affichage d'un sac svm(k,c), sac de valeur maximum, de contenance c, contenant un 
    // un sous-ensemble de [0:k]. Appel principal : asm(M,V,T,n,C).
        if (k == 0) // svm(0,c) est vide. Sans rien faire, il a été affiché.
            return; // svm(0,c) a été affiché.
        // ici : k > 0
        if (M[k][c] == M[k-1][c]) // le k-ème objet n'est pas dans svm(k,c), 
        // donc svm(k,c) = svm(k-1,c). 
            asm(M, V, T, k-1, c) ; // svm(k-1,c) a été affiché, donc svm(k,c) a été affiché
        else {// le k-ème objet est dans le sac. Donc svm(k,c) = svm(k-1,c-t(k-1)) union {k-1}
            asm(M,V,T,k-1,c-T[k-1]); // svm(k-1,c-t(k-1)) a été affiché
            System.out.printf("objet, valeur, taille = %d, %d, %d\n",
               k-1, V[k-1], T[k-1]); // Le k-ème objet été affiché
            // svm(k-1,c-t(k-1)) union {k-1} a été affiché, donc svm(k,c) a été affiché.
        }
    }

    public static void afficher(int[][] M){ int n = M.length; // affichage du tableau M.
        System.out.println("\t[");
        for (int i = n-1; i>=0; i--) 
            System.out.println("\t\t" + Arrays.toString(M[i]));
        System.out.println("\t]");
    }

    public static int somme(int[] T){
        int s = 0; 
        for (int i = 0; i<T.length; i++) 
            s = s+T[i]; 
        return s;
    }

    public static int max(int x, int y){
        if (x >= y) return x; 
        return y; 
    }

    /*public float[] calculerD() {
        int Lmax = 1000; // nombre de niveaux maximum
        int Nruns = 5000; // nombre de simulations/runs de l'évaluation statistique
        int Vmax = 100; // la plus grande valeur pouvant être présente dans le triangle

        float[] D = new float[Nruns]; // tableau de la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton pour chaque run
        Random random = new Random(); // générateur de nombres aléatoires

        for (int r = 0; r < Nruns; r++) {
            int m = random.nextInt(Lmax + 2) + 1; // choix du nombre de niveaux m de l'arbre au hasard
            int n = m * (m + 1) / 2; // nombre de valeurs dans le triangle

            int[] T = new int[n]; // un tableau d'entiers
            for (int i = 0; i < n; i++) {
                T[i] = random.nextInt(Vmax + 1); // génération des valeurs aléatoires de T entre 0 et Vmax + 1
            }

            int[] M = calculerM(T); // calcul de la valeur des chemins de somme maximum
            float v_etoile = M[0]; // la valeur d'une chemin de somme maximum

            float g = calculerMGlouton(T); // la valeur du chemin glouton
            //System.out.println("v_etoile = " + v_etoile + " g = " + g + "\n");
            D[r] = (v_etoile - g) / (1 + v_etoile); // la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton
        }

        return D;
    }*/

}