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
        csv.fichierCsv("test", Dchemin);
        eval.es(Dchemin);


        // Chemin du petit robot
        System.out.println("\n\nEvaluation statistique pour le chemin du petit robot : ");
        CheminDuPetitRobot robot = new CheminDuPetitRobot();
        float[] Drobot = robot.calculerD();
        csv.fichierCsv("text_robot", Drobot);
        eval.es(Drobot);
    }
}
