/**
 * Classe Main où on exécute tout le programme pour éviter toute duplication de code
 */
public class Main {
    public static void main(String[] Args){
        Csv csv = new Csv();
        EvaluationStatisque eval = new EvaluationStatisque();

        // Chemin de somme maximum
        System.out.println("Evaluation statistique pour le chemin de somme maximum : ");
        CheminDeSommeMaximum chemin = new CheminDeSommeMaximum();
        float[] Dchemin = chemin.calculerD();
        csv.fichierCsv("Chemin_de_somme_maximum.csv", Dchemin);
        eval.es(Dchemin);


        // Chemin du petit robot
        System.out.println("\n\nEvaluation statistique pour le chemin du petit robot : ");
        CheminDuPetitRobot robot = new CheminDuPetitRobot();
        float[] Drobot = robot.calculerD();
        csv.fichierCsv("Chemin_du_petit_robot.csv", Drobot);
        eval.es(Drobot);


        // Sac de valeur maximum
        System.out.println("\n\nEvaluation statistique pour le sac de valeur maximum : ");
        System.out.println("Stratégie gloutonne par valeur décroissante");
        SacDeValeurMaximum sac = new SacDeValeurMaximum();
        float[] Dsac = sac.calculerDValeur();
        csv.fichierCsv("Sac_valeur_maximum_glouton_valeur.csv", Dsac);
        eval.es(Dsac);

        System.out.println("\nStratégie gloutonne par ratios décroissant : ");
        float[] Dratio = sac.calculerDRatio();
        csv.fichierCsv("Sac_valeur_maximum_glouton_densite_valeur.csv", Dratio);
        eval.es(Dratio);


        // Répartition de stock d'entrepôt
        System.out.println("\n\nEvaluation statistique pour la répartition optimale sur un ensemble d'entrepot : ");
        RepartitionStockEntrepots repartition = new RepartitionStockEntrepots();
        float[] Drepartition = repartition.calculerD();
        csv.fichierCsv("Repartition_stock_entrepots.csv", Drepartition);
        eval.es(Drepartition);


        // Répartition optimale d'un temps de travail sur un ensemble d'unités
        System.out.println("\n\nEvaluation statistique pour la répartition optimale sur un ensemble d'unités");
        Juliette unite = new Juliette();
        float[] Dunite = unite.calculerD();
        csv.fichierCsv("Repartition_unites_heures.csv", Dunite);
        eval.es(Dunite);

    }
}

// TODO : commentaires codes + rapport