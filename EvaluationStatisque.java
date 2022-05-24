import java.util.Arrays;

public class EvaluationStatisque {

    /**
     * Fonction de l'évaluation statistique
     * es = Evaluation Statistique
     */
    public void es(float[] D){
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
    public float moyenne(float[] D){
        int n = D.length;

        // calcul de la moyenne
        float moyenne = 0;

        for (float v : D) {
            moyenne += v;
        }

        moyenne /= n;
        return moyenne;
    }

    /**
     * Fonction qui calcule la mediane des valeurs du tableau D
     * @param D le tableau des valeurs de la distance relative entre chaque run
     * @return la mediane des valeurs du tableau D
     */
    public float mediane(float[] D){
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
    public float ecartType(float[] D, float moyenne){
        int n = D.length;

        // calcul de l'écart-type
        float ecartType = 0;

        for (float v : D) {
            ecartType += (v - moyenne) * (v - moyenne);
        }

        ecartType /= n;
        ecartType = (float) Math.sqrt(ecartType);

        return ecartType;
    }
}
