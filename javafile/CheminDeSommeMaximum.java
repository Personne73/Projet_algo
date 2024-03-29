package javafile;

import java.util.Random;

/**
 * Classe du projet qui permet la recherche du chemin de somme maximum dans un arbre parfait.
 * Il s'agit de l'exemple 5 du projet
 */
class CheminDeSommeMaximum {

    /**
     * Fonction de recherche séquentielle du niveau l de l'indice i
     * ncs : niveau, calcul séquentiel 
     * @param i indice de l'élement à traiter
     * @return le niveau de l'indice i
     */
    public int ncs(int i){
        // Initialisation : l = 0
        // Invariant : I(l) --> I(l+1)
        // Condition d'arrêt : l*(l+1) / 2 <= i < (l+1)*(l+2) / 2
        int l = 0;
        while( !(l*(l+1) / 2 <= i && i < (l+1)*(l+2) / 2) ){
            l++;
        }
        return l;
    } // fonction de complexité Θ(n)

    /**
     * Fonction qui permet de trouver l'indice g(i) du descendant situé à gauche de i
     * @param i indice de l'élément à traiter
     * @return l'indice de l'élement à gauche de i
     */
    public int gauche(int i){
        // le niveau l est le niveau de l'indice i dans l'arbre
        int l = ncs(i);
        
        // la position dans la ligne de l'arbre est la différence entre l'indice i et le nombre de valeurs du triangle de l niveaux
        int p = i - l*(l+1)/2;

        // l'indice du descendant à gauche de i :
        // l'indice de début de la ligne correspondant au niveau de g(i) + la position de g(i) dans ce niveau qui est aussi la position de i dans son niveau
        return (l+1)*(l+2)/2 + p;
    }

    /**
     * Fonction qui permet de trouver l'indice d(i) du descendant situé à droite de i
     * @param i indice de l'élément à traiter
     * @return l'indice de l'élément à droite de i
     */
    public int droite(int i){
        return gauche(i) + 1;
    }
    
    /**
     * Fonction qui permet de calculer la valeur des chemins de somme maximum
     * @param T tableau des nombres correspondant à l'arbre
     * @return le tableau des chemins de somme maximum
     */
    public int[] calculerM(int[] T){
        /*
        Supposons le problème résolu : M[0] = m(0) = T[0] si c'est une feuille, sinon m(0) = T[0] + max(m(gauche(0)), m(droite(0)))
        Généralisons le problème : M[i] = m(i) = T[i] si c'est une feuille, sinon m(i) = T[i] + max(m(gauche(i)), m(droite(i))) pour 0 <= i < n
        Base : i = 0, m(0)
        Cas général : on calcul M de terme général M[i] = m(i) est la valeur d'un chemin de somme maximum qui commence à l'indice i
            i >= 0, m(i) = T[i] + max(m(gauche(i)), m(droite(i)))
        */
        
        int n = T.length;
        int[] M = new int[n];
        
        // Base
        if(n == 1){ // si le tableau contient un seul élément, c'est une feuille
            M[0] = T[0];
        } else {
            M[0] = T[0] + Math.max(M[gauche(0)], M[droite(0)]);
        }

        // Cas général
        for(int i = n-1; i >= 0; i--){
            if(gauche(i) > n || droite(i) > n) M[i] = T[i]; // si c'est une feuille, on ajoute seulement la valeur de T[i] dans M[i]
            else M[i] = T[i] + Math.max(M[gauche(i)], M[droite(i)]);
        }

        return M;
    } // fonction de complexité Θ(n)

    /**
     * Procédure qui affiche un chemin de somme maximum commençant à l'indice i
     * @param M le tableau des chemins de somme maximum
     * @param T le tableau correspondant à l'arbre
     * @param i l'indice à partir duquel on affiche le chemin
     * @param n la taille du triangle T
     */
    public void acsm(int[] M, int[] T, int i, int n){
        // cas de base : i > n, i n'est pas un indice des tableaux T et M (condition d'arrêt)
        if(i >= n) return;
        else {
            // cas général : on fait un appel récursif
            System.out.printf("%d", T[i]);

            if(gauche(i) > n || droite(i) > n) return; // les indices de gauches ou de droites n'appartiennent pas au triangle, on est donc sur une feuille
            
            // si la somme max à l'indice i est égale à la somme max du chemin de gauche + la valeur de T à l'indice i, on peut prendre l'indice de gauche pour l'appel récursif
            // et afficher cette valeur, sinon on prend l'indice de droite
            if(M[i] == M[gauche(i)] + T[i]){
                System.out.printf(" + ", T[gauche(i)]);
                acsm(M, T, gauche(i), n);
            } else {
                System.out.printf(" + ", T[droite(i)]);
                acsm(M, T, droite(i), n);
            }
        }
    } // fonction de complexité Θ(ln(n))

    /**
     * Fonction qui calcule la valeur d'un chemin glouton
     * @param T tableau correspondant à l'arbre
     * @return la valeur d'un chemin glouton
     */
    public int calculerMGlouton(int[] T){
        // Initialisation : M = 0, i = 1
        // Invariant : I(M, i) --> I(M+T[i], gauche(i)) si max(T[gauche(i)], T[droite(i)]) = T[gauche(i)]
        //                   sinon I(M+T[i], droite(i)) si max(T[gauche(i)], T[droite(i)]) = T[droite(i)]
        // Condition d'arrêt : i > T.length
        int n = T.length;
        int M = 0;
        int i = 0;

        while( i < n){
            M += T[i];

            if(gauche(i) > n || droite(i) > n) return M; // on est sur une feuille

            if( Math.max(T[gauche(i)], T[droite(i)]) == T[gauche(i)] ) i = gauche(i); 
            else i = droite(i);
        }

        return M;
    } // fonction de complexité Θ(n)

    /**
     * Fonction qui permet de calculer le tableau de la distance relative entre
     * la solution optimale et la solution gloutonne lors de chaque runs
     * @return le tableau de la distance relative entre chaque runs
     */
    public float[] calculerD() {
        int Lmax = 1000; // nombre de niveaux maximum
        int Nruns = 5000; // nombre de runs de l'évaluation statistique
        int Vmax = 100; // la plus grande valeur pouvant être présente dans le triangle

        float[] D = new float[Nruns]; // tableau de la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton pour chaque run
        Random random = new Random(); // générateur de nombres aléatoires

        for (int r = 0; r < Nruns; r++) {
            int m = random.nextInt(Lmax + 1) + 1; // choix du nombre de niveaux m de l'arbre au hasard
            int n = m * (m + 1) / 2; // nombre de valeurs dans le triangle

            int[] T = new int[n]; // un tableau d'entiers
            for (int i = 0; i < n; i++) {
                T[i] = random.nextInt(Vmax + 1); // génération des valeurs aléatoires de T entre 0 et Vmax + 1
            }

            int[] M = calculerM(T); // calcul de la valeur des chemins de somme maximum
            float v_etoile = M[0]; // la valeur d'un chemin de somme maximum

            float g = calculerMGlouton(T); // la valeur du chemin glouton
            D[r] = (v_etoile - g) / (1 + v_etoile); // la distance relative entre la valeur du chemin de somme maximum et la valeur du chemin glouton
        }

        return D;
    }
} // fin de classe