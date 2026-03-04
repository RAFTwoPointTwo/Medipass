package core;

import utilities.Colors;
import utilities.Tools;

import java.io.*;

public class Backup {

    // Cette classe me permet de sauvegarder les données du système et de les recharger lors de la connexion suivante :

    private static final String saveDirectoryName = "save";

    private static final String saveFileName =  saveDirectoryName + "/saveFile.txt";

    // Méthode de sauvegarde du système :

    public static void save(Object object) {
        File directory = new File(saveDirectoryName);

        if (!directory.exists()){
            boolean created = directory.mkdir();
            if (!created) Tools.print("\n !! Impossible de créer le répertoire de sauvegarde : " + saveDirectoryName + " \n");
        }

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(saveFileName))){
            objectOutputStream.writeObject(object);
            Tools.print("\n -- Sauvegarde des données du système ... \n");
            Tools.sleep(1);
            Tools.print(Colors.GREEN + " ::: Données sauvegardées ::: " + Colors.BLOCK);
            Tools.sleep(0.8);
            Tools.print();
        } catch (Exception e) {
            System.out.println("\n !! Une erreur est subvenue lors de la sauvegarde ... !!\n");
            Tools.sleep(2);
        }
    }

    // Méthode de chargement du système :

    public static Object load() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(saveFileName))){
            Tools.print();
            Tools.print("\n -- Chargement des données du système ... \n");
            Tools.sleep(1.2);
            System.out.println("\n ::: Données chargées avec succès ::: \n");
            Tools.sleep(1);
            Tools.print();
            return objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("\n !! Aucune sauvegarde trouvée ... !! \n");
            Tools.sleep(2);
            return null;
        }
    }
}