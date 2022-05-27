/**
 * Classe Main où on exécute tout le programme pour éviter toute duplication de code
 */
public class Main {
    public static void main(String[] Args){
        Csv csv = new Csv();
        EvaluationStatisque eval = new EvaluationStatisque();

        // Chemin de somme maximum
        // System.out.println("Evaluation statistique pour le chemin de somme maximum : ");
        /*CheminDeSommeMaximum chemin = new CheminDeSommeMaximum();
        float[] Dchemin = chemin.calculerD();
        csv.fichierCsv("Chemin_de_somme_maximum.csv", Dchemin);
        eval.es(Dchemin);*/


        // Chemin du petit robot
        /*System.out.println("\n\nEvaluation statistique pour le chemin du petit robot : ");
        CheminDuPetitRobot robot = new CheminDuPetitRobot();
        float[] Drobot = robot.calculerD();
        csv.fichierCsv("Chemin_du_petit_robot.csv", Drobot);
        eval.es(Drobot);*/

        // Sac de valeur maximum
        System.out.println("\n\nEvaluation statistique pour le sac de valeur maximum : ");
        System.out.println("Stratégie gloutonne par valeur décroissante");
        SacDeValeurMaximum sac = new SacDeValeurMaximum();
        float[] Dsac = sac.calculerDValeur();
        csv.fichierCsv("Sac_de_valeur_maximum_glouton_par_valeur.csv", Dsac);
        eval.es(Dsac);

        System.out.println("\nStratégie gloutonne par ratios décroissant : ");
        float[] Dratio = sac.calculerDRatio();
        csv.fichierCsv("Sac_de_valeur_maximum_glouton_par_ratio.csv", Dratio);
        eval.es(Dratio);

        // Répartition de stock d'entrepôt



    }
}
