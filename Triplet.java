public class Triplet {

    private int aNumero;
    private int aValeur;
    private int aTaille;

    public Triplet(final int pNumero, final int pValeur, final int pTaille){
        this.aNumero = pNumero;
        this.aValeur = pValeur;
        this.aTaille = pTaille;
    }

    public int getNumero(){
        return this.aNumero;
    }

    public int getValeur(){
        return this.aValeur;
    }

    public int getTaille(){
        return this.aTaille;
    }
}
