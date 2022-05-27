import java.util.Arrays;
import java.util.Random;


/**
 * Classe qui permet la résolution de l'exemple 2 du projet sur le sac de valeur maximum
 */
public class SacDeValeurMaximum {

    /*public static void main(String[] Argv){
        int[] V = {2,1,3,8,4};
		int[] T = {1,1,2,4,3}; 
		int n = V.length;
		System.out.println("V = " + Arrays.toString(V));
		System.out.println("T = " + Arrays.toString(T));
		int C = 8; System.out.printf("C = %d\n",C);
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
        float ratio = calculerMGloutonRatio(V, T, C);
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

    /**
     * Fonction qui permet de calculer la valeur du sac lorsqu'il la contenance est maximale. On effectue le tri des
     * valeurs en prenant l'ordre décroissant des valeurs.
     * @param V Le tableau contenant toutes les valeurs des objets.
     * @param T Le tableau contenant toutes les tailles des objets.
     * @param C L'entier qui défini la contenance du sac.
     * @return val La valeur du sac lorsque sa contenance est maximale.
     */
    public int calculerMGloutonValeur(int[] V, int[] T, int C){
        // Initialisation : val = 0, j = 0, c = 0
        // Invariant : I(val, j, c) --> I(val + TabObjets[j].getValeur(), j+1, c + TabObjets[j].getTaille())
        // Condition d'arrêt : c > C
        int val = 0;
        int j = 0;
        int c = 0;

        // création des objets du sac qu'on ajoute dans le tableau de triplets
        Triplet[] TabObjets = tableauTriplet(V, T);

        qs.quickSortDisplay(TabObjets);

        while(c <= C){
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

    /**
     * Fonction qui permet de trier les valeurs dans l'ordre décroissant en faisant le ratio Valeur/Taille
     * @param V Le tableau contenant toutes les valeurs des objets.
     * @param T Le tableau contenant toutes les tailles des objets.
     * @param C L'entier qui défini la contenance du sac.
     * @return ratio La valeur maximale du ratio des objets contenus dans le sac.
     */
    // il faut récuperer les valeurs de V et de T pour en faire un ratio mais il faut garder la taille
    public float calculerMGloutonRatio(int[] V, int[] T, int C){
        // Initialisation : ratio = 0, j = 0, c = 0
        // Invariant : I(val, j, c) --> I(ratio + TabObjets[j].getRatio(), j+1, c + TabObjets[j].getTaille())
        // Condition d'arrêt : c > C
        float ratio = 0;
        int j = 0;
        int c = 0;

        // création des objets du sac qu'on ajoute dans le tableau de triplets
        Triplet[] TabObjets = tableauTriplet(V, T);

        qs.quickSortDisplayRatio(TabObjets);

        while(c <= C){
            if(j >= TabObjets.length) return ratio;
            if(C-c == 0) return ratio;
            if( TabObjets[j].getTaille() <= C-c){
                c += TabObjets[j].getTaille();
                ratio += TabObjets[j].getRatio();
            }
            j++;
        }
        return ratio;
    }


    public void afficher(int[][] M){ int n = M.length; // affichage du tableau M.
        System.out.println("\t[");
        for (int i = n-1; i>=0; i--) 
            System.out.println("\t\t" + Arrays.toString(M[i]));
        System.out.println("\t]");
    }

    public int max(int x, int y){
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
    /**
     * Fonction qui permet de calculer le tableau de la distance relative entre chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
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
                T[i] = random.nextInt(Tmax + 2) + 1;
            }

            int[][] M = calculerM(V, T, C); // calcul de la valeur des chemins de somme maximum
            float v_etoile = M[n][C]; // la valeur d'une chemin de somme maximum

            float g = calculerMGloutonValeur(V, T, C); // la valeur du chemin glouton
            D[r] = (v_etoile - g) / (1 + v_etoile); // la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton
        }

        return D;
    }
    /**
     * Fonction qui permet de calculer le tableau de la distance relative entre chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
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
                T[i] = random.nextInt(Tmax + 2) + 1;
            }

            int[][] M = calculerM(V, T, C); // calcul de la valeur des chemins de somme maximum
            float v_etoile = M[n][C]; // la valeur d'une chemin de somme maximum

            float g = calculerMGloutonRatio(V, T, C); // la valeur du chemin glouton
            D[r] = (v_etoile - g) / (1 + v_etoile); // la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton
        }

        return D;
    }
}