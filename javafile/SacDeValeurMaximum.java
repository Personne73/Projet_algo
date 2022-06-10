package javafile;

import java.util.Random;

/**
 * Classe qui permet la résolution de l'exemple 2 du projet sur le sac de valeur maximum
 * Certains morceaux de code ont été pris du professeur M. René Natowicz
 * (le code optimal et les fonctions nécessaires au fonctionnement de l'exercice)
 */
public class SacDeValeurMaximum {

    /**
     * Fonction optimale permettant le calcul du sac de valeur maximum
     * @author René Natowicz
     * @param V un tableau de valeur
     * @param T les tailles associées aux valeurs
     * @param C la contenance du sac
     * @return le tableau contenant les valeurs maximums sur les différents cas
     */
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

    // création d'un objet javafile.QuickSortDisplay afin de pouvoir utiliser les méthodes présente dans ce fichier
    static QuickSortDisplay qs = new QuickSortDisplay();

    /**
     * Fonction qui crée un tableau de Doublet et ajoute les objets dans le tableau
     * @param V le tableau de valeurs
     * @param T le tableau de tailles
     * @return le tableau de doublet
     */
    public Doublet[] tableauDoublet(int[] V, int[] T){
        int n = V.length;
        Doublet[] doublet = new Doublet[n];
        for(int i = 0; i < n; i++) {
            Doublet vObjet = new Doublet(V[i], T[i]);
            doublet[i] = vObjet;
        }

        return doublet;
    }

    /**
     * Fonction qui calcule la valeur du sac pour les fonctions calculerMGloutonValeur et calculerMGloutonRatio
     * @param C la contenance du sac
     * @param TabObjets le tableau d'objet trié de la façon nécessaire
     * @return la valeur maximale gloutonne du sac
     */
    public int valeurSac(int C, Doublet[] TabObjets){
        /*
        Initialisation : val = 0, j = 0, c = 0
        Invariant : 0 < j < V.length, val += TabObjets[j].getValeur() si la taille de l'objet permet de le mettre dans le sac
                                                                    et s'il reste assez d'espace de disponible dans celui-ci
        Condition d'arrêt : c > C
        */
        int val = 0;
        int j = 0;
        int c = 0;

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
    } // fonction de complexité Θ(n)

    /**
     * Fonction qui permet le calcul glouton de la valeur maximale du sac par la stratégie gloutonne par valeur
     * @param V le tableau de valeurs
     * @param T le tableau de tailles
     * @param C la contenance du sac
     * @return la valeur maximum gloutonne du sac
     */
    public int calculerMGloutonValeur(int[] V, int[] T, int C){
        // création des objets du sac qu'on ajoute dans le tableau de javafile.Doublet
        Doublet[] TabObjets = tableauDoublet(V, T);

        // tri des objets par valeurs décroissantes
        qs.quickSortDisplay(TabObjets);

        // calcul glouton de la valeur maximale du sac
        return valeurSac(C, TabObjets);
    }

    /**
     * Fonction qui permet le calcul glouton de la valeur maximale du sac par la stratégie gloutonne par densité de valeur
     * @param V le tableau de valeurs
     * @param T le tableau de tailles
     * @param C la contenance du sac
     * @return la valeur maximum gloutonne du sac
     */
    public int calculerMGloutonRatio(int[] V, int[] T, int C){
        // création des objets du sac qu'on ajoute dans le tableau de triplets
        Doublet[] TabObjets = tableauDoublet(V, T);

        // tri des objets par ratio décroissants
        qs.quickSortDisplayRatio(TabObjets);

        // calcul glouton de la valeur maximale du sac
        return valeurSac(C, TabObjets);
    }

    /**
     * Fonction qui calcule le maximum entre deux valeurs
     * @author René Natowicz
     * @param x valeur 1
     * @param y valeur 2
     * @return le maximum
     */
    public static int max(int x, int y){
        if (x >= y) return x; 
        return y; 
    }

    /**
     * Fonction qui permet de calculer le tableau de la distance relative entre
     * la solution optimale et la solution gloutonne par valeurs lors de chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
    public float[] calculerDValeur() {
        int Cmax = 1000; // contenance maximum du sac
        int Nmax = 1000; // nombre maximum d'objets à l'intérieur du sac
        int Vmax = 100; // la valeur max
        int Tmax = 100; // la taille max
        int Nruns = 5000;

        float[] D = new float[Nruns];
        Random random = new Random();

        for (int r = 0; r < Nruns; r++) {
            int C = random.nextInt(Cmax + 1);
            int n = random.nextInt(Nmax + 1);

            int[] V = new int[n];
            int[] T = new int[n];
            for (int i = 0; i < n; i++) {
                V[i] = random.nextInt(Vmax + 1); // génération des valeurs aléatoires de V entre 0 et Vmax + 1
                T[i] = random.nextInt(Tmax + 1); // génération des valeurs aléatoires de T entre 0 et Tmax + 1
            }

            int[][] M = calculerM(V, T, C);
            float v_etoile = M[n][C];

            float g = calculerMGloutonValeur(V, T, C);
            D[r] = (v_etoile - g) / (1 + v_etoile);
        }

        return D;
    }

    /**
     * Fonction qui permet de calculer le tableau de la distance relative entre
     * la solution optimale et la solution gloutonne par densité de valeurs lors de chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
    public float[] calculerDRatio() {
        int Cmax = 1000; // contenance maximum du sac
        int Nmax = 1000; // nombre maximum d'objets à l'intérieur du sac
        int Vmax = 100; // la valeur max
        int Tmax = 100; // la taille max
        int Nruns = 5000;

        float[] D = new float[Nruns];
        Random random = new Random();

        for (int r = 0; r < Nruns; r++) {
            int C = random.nextInt(Cmax + 1);
            int n = random.nextInt(Nmax + 1);

            int[] V = new int[n];
            int[] T = new int[n];
            for (int i = 0; i < n; i++) {
                V[i] = random.nextInt(Vmax + 1);
                T[i] = random.nextInt(Tmax + 1);
            }

            int[][] M = calculerM(V, T, C);
            float v_etoile = M[n][C];

            float g = calculerMGloutonRatio(V, T, C);
            D[r] = (v_etoile - g) / (1 + v_etoile);
        }

        return D;
    }

} // fin de classe