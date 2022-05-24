import java.io.FileWriter;
import java.io.IOException;


public class Csv {
    /**
     * Fonction qui écrit les données dans un fichier csv, une donnée par ligne
     * @param Filename qui est le nom du fichier
     * @param data, la liste des données à écrire
     */
    public void fichierCsv(String Filename, float[] data){
        try{
            FileWriter writer = new FileWriter(Filename, true);
            for(float s : data){
                writer.write(s + "\n");
            }
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}