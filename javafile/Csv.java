package javafile;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe qui permet l'écriture dans un fichier CSV
 */
public class Csv {
    /**
     * Fonction qui écrit les données fourni dans un tableau dans un fichier csv
     * en respectant la contrainte d'une donnée par ligne
     * @param Filename le nom du fichier
     * @param data le tableau des données à écrire
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

} // fin de classe
