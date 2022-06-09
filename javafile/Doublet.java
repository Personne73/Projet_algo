package javafile;

/**
 * Classe qui permet la création d'un objet javafile.Doublet permettant l'association d'une valeur à sa taille
 * Elle est lié à l'exemple sur le Sac de valeur maximum
 */
public class Doublet {

    private final int aValeur;
    private final int aTaille;

    /**
     * Constructeur naturel de la classe
     * @param pValeur une valeur
     * @param pTaille une taille
     */
    public Doublet(final int pValeur, final int pTaille){
        this.aValeur = pValeur;
        this.aTaille = pTaille;
    }

    /**
     * Accesseur à l'attribut aValeur
     * @return la valeur de l'objet courant
     */
    public int getValeur(){
        return this.aValeur;
    }

    /**
     * Accesseur à la taille de l'objet courant
     * @return la taille de l'objet courant
     */
    public int getTaille(){
        return this.aTaille;
    }

    /**
     * Accesseur au ratio valeur sur taille
     * @return le ration valeur/taille de l'objet courant
     */
    public float getRatio(){return (float)this.aValeur/this.aTaille;}

} // fin de classe
