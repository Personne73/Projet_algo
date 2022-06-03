import java.util.Arrays;
import java.util.Random;


/**
 * Classe qui permet la résolution de l'exemple 2 du projet sur le sac de valeur maximum
 */
public class SacDeValeurMaximum {
    /*public static void main(String[] Argv){
        //int[] V = {2,1,3,8,4};
		//int[] T = {1,1,2,4,3};
        int[] V =  {7, 16, 4, 18, 1, 15, 8, 7, 2, 18, 18, 18, 19, 17, 5, 14, 4, 2, 4, 20, 12, 7, 18, 4, 7, 16, 4, 20, 6, 7, 8, 4, 19, 9, 1, 17, 8, 4, 20, 18, 17, 14, 5, 7, 12, 11, 2, 7, 4, 12, 4, 20, 9, 10, 8, 12, 1, 16, 5, 8, 12, 13, 17, 6, 17, 6, 3, 17, 10, 5, 18, 17, 1, 19, 15, 20, 16, 8, 5, 14, 2, 20, 19, 6, 3, 4, 6, 3, 7, 13, 17, 19, 5, 2, 3, 20, 2, 11, 16, 6, 18, 11, 8, 13, 16, 12, 10, 15, 10, 6, 13, 13, 3, 3, 16, 20, 18, 2, 17, 9, 2, 15, 6, 2, 6, 2, 15, 12, 8, 2, 8, 9, 14, 18, 10, 12, 14, 16, 8, 6, 6, 5, 8, 2, 1, 15, 15, 9, 6, 6, 2, 1, 9, 13, 13, 9, 12, 4, 3, 11, 9, 11, 16, 1, 8, 6, 2, 11, 17, 6, 18, 2, 17, 16, 15, 8, 9, 8, 13, 10, 11, 3, 12, 18, 19, 20, 17, 18, 13, 9, 1, 2, 5, 14, 8, 8, 5, 10, 7, 1, 9, 14, 3, 9, 18, 2, 17, 9, 17, 1, 20, 5, 20, 15, 13, 8, 5, 1, 11, 5, 19, 4, 17, 11, 11, 17, 15, 12, 19, 16, 12, 4, 8, 4, 11, 8, 19, 20, 4, 17, 16, 17, 12, 11, 7, 13, 6, 19, 3, 2, 19, 19, 11, 10, 15, 14, 3, 12, 18, 9, 20, 2, 16, 3, 6, 15, 18, 14, 6, 5, 7, 12, 19, 14, 16, 13, 6, 17, 18, 6, 16, 8, 14, 9, 6, 8, 16, 8, 3, 9, 3, 6, 18, 13, 6, 18, 3, 20, 15, 16, 4, 18, 6, 20, 7, 17, 4, 10, 2, 7, 6, 16, 19, 15, 10, 10, 10, 17, 9, 18, 9, 8, 10, 19, 9, 14, 5, 1, 17, 9, 20, 12, 7, 4, 12, 7, 10, 19, 10, 14, 4, 17, 13, 16, 12, 18, 20, 13, 8, 4, 17, 16, 19, 14, 5, 2, 4, 15, 18, 16, 1, 1, 13, 15, 11, 6, 10, 20, 10, 13, 19, 16, 4, 11, 7, 3, 20, 15, 10, 7, 19, 3, 20, 12, 17, 17, 3, 11, 3, 6, 13, 16, 13, 11, 9, 4, 8, 1, 13, 4, 11, 9, 11, 12, 18, 17, 3, 4, 4, 5, 12, 4, 1, 7, 6, 13, 6, 8, 13, 17, 2, 10, 5, 11, 4, 14, 16, 16, 9, 17, 18, 8, 14, 11, 17, 20, 2, 1, 11, 9, 13, 12, 4, 14, 10, 7, 19, 8, 19, 2, 9, 14, 12, 15, 7, 9, 20, 3, 17, 3, 6, 14, 16, 6, 13, 1, 12, 6, 8, 15, 12, 3, 2, 4, 20, 20, 6, 1, 7, 17, 1, 2, 11, 18, 15, 17, 3, 14, 18, 9, 5, 16, 15, 6, 5, 6, 1, 14, 4, 16};
        int[] T =  {10, 14, 1, 15, 19, 5, 19, 2, 3, 7, 10, 4, 17, 20, 9, 17, 17, 7, 3, 8, 5, 6, 8, 1, 15, 5, 3, 5, 16, 15, 12, 17, 11, 11, 11, 18, 15, 10, 17, 10, 18, 7, 9, 6, 20, 16, 9, 10, 7, 5, 1, 8, 6, 15, 10, 8, 1, 12, 1, 19, 14, 17, 20, 11, 16, 17, 8, 15, 6, 7, 15, 1, 18, 4, 3, 9, 10, 2, 2, 20, 19, 5, 10, 8, 20, 17, 3, 1, 4, 18, 17, 13, 15, 10, 4, 12, 20, 7, 1, 12, 4, 20, 3, 9, 4, 15, 12, 17, 10, 3, 13, 3, 7, 16, 20, 7, 9, 12, 9, 1, 13, 7, 20, 2, 7, 5, 3, 14, 2, 19, 3, 2, 7, 9, 13, 5, 16, 13, 11, 20, 19, 8, 13, 6, 6, 8, 1, 20, 4, 7, 15, 3, 10, 1, 4, 15, 18, 16, 6, 2, 3, 13, 15, 8, 4, 7, 14, 17, 1, 3, 18, 14, 6, 9, 3, 6, 8, 11, 9, 4, 6, 5, 4, 12, 16, 12, 19, 11, 10, 17, 12, 8, 6, 3, 1, 18, 2, 10, 19, 2, 19, 3, 13, 4, 11, 11, 12, 20, 5, 3, 11, 14, 20, 16, 6, 10, 20, 1, 11, 11, 1, 20, 15, 4, 4, 19, 6, 3, 10, 20, 15, 6, 13, 9, 19, 5, 17, 13, 20, 11, 13, 13, 7, 20, 10, 12, 17, 18, 14, 14, 18, 4, 4, 3, 7, 1, 15, 15, 18, 5, 16, 4, 16, 1, 17, 16, 10, 17, 9, 3, 7, 2, 1, 18, 11, 2, 18, 18, 5, 4, 5, 17, 1, 5, 11, 11, 1, 8, 8, 20, 6, 16, 10, 7, 13, 17, 7, 8, 11, 1, 15, 20, 12, 4, 16, 10, 13, 16, 11, 4, 10, 13, 5, 19, 15, 20, 13, 7, 8, 6, 1, 19, 16, 4, 7, 16, 17, 16, 4, 17, 13, 4, 9, 15, 16, 13, 20, 8, 7, 1, 9, 8, 5, 18, 15, 10, 19, 3, 17, 1, 1, 7, 16, 2, 4, 6, 8, 13, 19, 2, 20, 13, 9, 17, 3, 13, 13, 11, 9, 5, 16, 5, 17, 15, 19, 6, 2, 1, 18, 12, 4, 14, 1, 14, 15, 7, 13, 9, 9, 16, 1, 16, 8, 19, 14, 3, 6, 1, 18, 15, 20, 16, 18, 16, 17, 18, 17, 11, 6, 9, 3, 7, 5, 11, 12, 3, 2, 2, 15, 8, 13, 2, 5, 16, 5, 15, 15, 5, 4, 4, 2, 3, 16, 1, 11, 10, 5, 5, 14, 5, 1, 10, 8, 6, 9, 6, 11, 17, 20, 3, 1, 7, 13, 2, 1, 15, 16, 2, 12, 10, 14, 11, 20, 17, 8, 6, 11, 14, 8, 17, 5, 20, 13, 1, 10, 5, 17, 6, 11, 2, 10, 2, 1, 1, 15, 3, 3, 18, 15, 17, 20, 4, 3, 9, 17, 8, 8, 10, 11, 11};


        int n = V.length;
		System.out.println("V = " + Arrays.toString(V));
		System.out.println("T = " + Arrays.toString(T));
		int C = 30; System.out.printf("C = %d\n",C);
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
        System.out.println("Valeur glouton max = " + v);
        int ratio = calculerMGloutonRatio(V, T, C);
        System.out.println("Ratio glouton max = " + ratio);
    }*/

    public int[][] calculerM(int[] V, int[] T, int C){
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

    static QuickSortDisplay qs = new QuickSortDisplay();

    /**
     * Fonction qui crée un tableau de Triplet et ajoute les objets dans le tableau
     * @param V le tableau de valeurs
     * @param T le tableau de taille
     * @return le tableau de triplet
     */
    public Triplet[] tableauTriplet(int[] V, int[] T){
        int n = V.length;
        Triplet[] triplet = new Triplet[n];
        for(int i = 0; i < n; i++) {
            Triplet vObjet = new Triplet(i, V[i], T[i]);
            triplet[i] = vObjet;
        }

        return triplet;
    }

    public int calculerMGloutonValeur(int[] V, int[] T, int C){
        int val = 0;
        int j = 0;
        int c = 0;

        // création des objets du sac qu'on ajoute dans le tableau de triplets
        Triplet[] TabObjets = tableauTriplet(V, T);

        qs.quickSortDisplay(TabObjets);

        while(c <= C ){
            if(j >= TabObjets.length) return val;
            if(C-c == 0) return val;
            if( TabObjets[j].getTaille() <= C-c){
                c += TabObjets[j].getTaille();
                val += TabObjets[j].getValeur();
            }
            j++;
        }
        return val;
    }

    // il faut récuperer les valeurs de V et de T pour en faire un ratio mais il faut garder la taille
    public int calculerMGloutonRatio(int[] V, int[] T, int C){
        int valeur = 0;
        int j = 0;
        int c = 0;

        // création des objets du sac qu'on ajoute dans le tableau de triplets
        Triplet[] TabObjets = tableauTriplet(V, T);

        qs.quickSortDisplayRatio(TabObjets);

        while(c <= C ){
            if(j >= TabObjets.length) return valeur;
            if(C-c == 0) return valeur;
            if( TabObjets[j].getTaille() <= C-c){
                c += TabObjets[j].getTaille();
                valeur += TabObjets[j].getValeur();
            }
            j++;
        }
        return valeur;
    }


    public static void afficher(int[][] M){ int n = M.length; // affichage du tableau M.
        System.out.println("\t[");
        for (int i = n-1; i>=0; i--) 
            System.out.println("\t\t" + Arrays.toString(M[i]));
        System.out.println("\t]");
    }

    public static int max(int x, int y){
        if (x >= y) return x; 
        return y; 
    }

    /**
     * Fonction qui permet de définir au hasard la taille maximale d'un objet
     * @param C la contenance du sac
     * @param n le nombre d'objets présents dans le sac
     * @return la taille maximale d'un objet
     */
    public int Tmax(int C, int n){
        int Tmax = 0;
        int[] T = new int[n];
        for(int i = 0; i < n; i++){
            T[i] = (int)(Math.random()*(C+1));
            if(T[i] > Tmax) Tmax = T[i];
        }
        return Tmax;
    }

    public float[] calculerDValeur() {
        int Cmax = 1000; // contenance maximum du sac
        int Nmax = 1000; // nombre maximum d'objets à l'interieur du sac
        int Nruns = 5000; // nombre de simulations/runs de l'évaluation statistique
        int Vmax = 100; // la plus grande valeur pouvant être présente dans le triangle
        int Tmax = 100;
        float[] D = new float[Nruns]; // tableau de la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton pour chaque run
        Random random = new Random(); // générateur de nombres aléatoires

        for (int r = 0; r < Nruns; r++) {
            int C = random.nextInt(Cmax + 1);
            int n = random.nextInt(Nmax + 1);

            int[] V = new int[n];
            int[] T = new int[n];
            for (int i = 0; i < n; i++) {
                V[i] = random.nextInt(Vmax + 1); // génération des valeurs aléatoires de V entre 0 et Vmax + 1
                T[i] = random.nextInt(Tmax + 1);
            }

            int[][] M = calculerM(V, T, C); // calcul de la valeur des chemins de somme maximum
            float v_etoile = M[n][C]; // la valeur d'une chemin de somme maximum

            float g = calculerMGloutonValeur(V, T, C); // la valeur du chemin glouton
            D[r] = (v_etoile - g) / (1 + v_etoile); // la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton
        }

        return D;
    }

    public float[] calculerDRatio() {
        int Cmax = 1000; // contenance maximum du sac
        int Nmax = 1000; // nombre maximum d'objets à l'interieur du sac
        int Nruns = 5000; // nombre de simulations/runs de l'évaluation statistique
        int Vmax = 100; // la plus grande valeur pouvant être présente dans le triangle
        int Tmax = 100;
        float[] D = new float[Nruns]; // tableau de la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton pour chaque run
        Random random = new Random(); // générateur de nombres aléatoires

        for (int r = 0; r < Nruns; r++) {
            int C = random.nextInt(Cmax + 1);
            int n = random.nextInt(Nmax + 1);

            int[] V = new int[n];
            int[] T = new int[n];
            for (int i = 0; i < n; i++) {
                V[i] = random.nextInt(Vmax + 1); // génération des valeurs aléatoires de V entre 0 et Vmax + 1
                T[i] = random.nextInt(Tmax + 1);
            }

            int[][] M = calculerM(V, T, C); // calcul de la valeur des chemins de somme maximum
            float v_etoile = M[n][C]; // la valeur d'une chemin de somme maximum

            float g = calculerMGloutonRatio(V, T, C); // la valeur du chemin glouton
            D[r] = (v_etoile - g) / (1 + v_etoile); // la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton
        }

        return D;
    }
}