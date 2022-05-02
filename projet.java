import java.util.*;

/**
 * Classe du projet qui permet la recherche du chemin de somme maximum
 */
class Projet{
    /**
     * Méthode principale qui permet de lancer le programme
     * @param Args
     */
    public static void main(String[] Args){
        int[] T = {3, 7, 4, 2, 4, 6, 8, 5, 9, 3};
        // int[] T = {7,5,1,4,2,3};
        
        List<Integer> lines = treeLine(T);
        // System.out.println("Liste des intervalles d'indices : " + lines);
        //gauche(4);
        int [] M = calculerM(T);
        System.out.print("M : ");
        for(int i= 0; i<M.length; i++){
            System.out.print(M[i] + " ");
        }
        System.out.println("\n");
        List<Integer> line = treeLine(M);
        acsm(M, T, 0, T.length);
        // System.out.printf("\nNiveau du nombre à l'indice %d : %d\n", 5, ncd(5, T.length));
        int Mglouton = calculerMGlouton(T);
        System.out.println("\nM_glouton : " + Mglouton);
    }

    /**
     * Fonction de recherche séquentielle du niveau l de l'indice i
     * ncs : niveau, calcul séquentiel 
     * @param i indice de l'élement à traiter
     * @return le niveau de l'indice i
     */
    static int ncs(int i){
        // Initialisation : l = 0
        // Invariant : I(l) --> I(l+1)
        // Condition d'arrêt : l*(l+1) / 2 <= i < (l+1)*(l+2) / 2
        int l = 0;
        while( !(l*(l+1) / 2 <= i && i < (l+1)*(l+2) / 2) ){
            l++;
        }
        return l;
    }

    /**
     * Fonction de recherche dichotomique du niveau l de l'indice i
     * ncd : niveau, calcul dichotomique 
     * @param i indice de l'élement à traiter
     * @return le niveau de l'indice i
     */
    static int ncd(int i, int n){
        // Initialisation : linf = 0, lsup = n-1
        // Invariant : I(linf, lsup) --> I(linf+1, lsup-1)
        // Condition d'arrêt : linf*(linf+1) / 2 > i ou i >= lsup*(lsup+1) / 2
        int linf = 0, lsup = n-1;
        while(linf*(linf+1) / 2 <= i && i < lsup*(lsup+1) / 2){
            linf++;
            lsup--;
        }
        
        return linf-1;
    }

    /**
     * Fonction qui permet de trouver l'indice g(i) du descendant situé à gauche de i
     * @param i indice de l'élément à traiter
     * @return l'indice g(i)
     */
    static int gauche(int i){
        // le niveau l est le niveau de l'indice i dans l'arbre
        int l = ncs(i);
        //System.out.printf("Niveau du nombre à l'indice %d : %d\n", i, l);
        
        // la position dans la ligne de l'arbre est la différence entre l'indice i et le nombre de valeurs du triangle de l niveaux
        int p = i - l*(l+1)/2;
        //System.out.printf("Position du nombre à l'indice %d dans ce niveau : %d\n", i, p);

        // l'indice du descendant à gauche de i :
        // l'indice de début de la ligne correspondant au niveau de g(i) + la position de g(i) dans ce niveau qui est aussi la position de i dans son niveau
        int g = (l+1)*(l+2)/2 + p;
        //System.out.printf("Indice du descendant à gauche de %d : %d\n", i, g);
        
        return g;
    }

    /**
     * Fonction qui permet de trouver l'indice d(i) du descendant situé à droite de i
     * @param i indice de l'élément à traiter
     * @return l'indice d(i)
     */
    static int droite(int i){
        return gauche(i) + 1;
    }
    
    /**
     * Fonction qui permet de calculer la valeur des chemins de somme maximum
     * @param T tableau des nombres correspondant à l'arbre
     * @return le tableau des chemins de somme maximum
     */
    static int[] calculerM(int[] T){
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
            if(gauche(i) > n || droite(i) > n) M[i] = T[i]; // si c'est une feuille, ajoute seulement la valeur de T[i] dans M[i]
            else M[i] = T[i] + Math.max(M[gauche(i)], M[droite(i)]);
        }

        return M;
    }

    /**
     * Procédure qui affiche un chemin de somme maximum commençant à l'indice i
     * @param M le tableau des chemins de somme maximum
     * @param T le tableau correspondant à l'arbre
     * @param i l'indice à partir duquel on affiche le chemin
     * @param n la taille du triangle T
     */
    static void acsm(int[] M, int[] T, int i, int n){
        // cas de base : i > n, i n'est pas un indice des tableaux T et M (condition d'arrêt)
        if(i > n) return;
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

            // System.out.printf(" = %d\n", M[i]);
        }
        
    }

    /**
     * Fonction qui calcul la valeur d'un chemin glouton
     * @param T tableau correspondant à l'arbre
     * @return la valeur d'un chemin glouton
     */
    static int calculerMGlouton(int[] T){
        // Initialisation : M = 0, i = 1
        // Invariant : I(M, i) --> I(M+T[i], gauche(i)) si max(T[gauche(i)], T[droite(i)]) = T[gauche(i)]
        //                   sinon I(M+T[i], droite(i)) (max(T[gauche(i)], T[droite(i)]) = T[droite(i)])
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
    }

    /**
     * Fonction qui permet de trouver les lignes de l'arbre et de les avoir dans une liste
     * elle trace aussi le tableau fourni en paramètre sous forme de triangle
     * (c'est ma fonction test que j'ai utilisé pour trouver les lignes de l'arbre)
     * @param T tableau d'entiers
     */
    static List treeLine(int [] T){
        int i = 0, j = 1, cpt = 1;
        List<Integer> lines = new ArrayList<Integer>();
        /*lines.add(i);
        lines.add(j);*/
        while(j <= T.length){
            //if (j > T.length) j=T.length;
            //affichage arbre
            for(int y = i; y < j; y++){
                System.out.printf("%d ", T[y]);
            }
            System.out.printf("\n");
            lines.add(i);
            lines.add(j);
            cpt++;
            i = j; 
            j += cpt;
        }
        System.out.println();
        // System.out.println("Liste des intervalles d'indices : " + lines);
        return lines;
    }

}