package utilities;

import core.MediPassSystem;
import entities.Administrator;
import entities.HealthProfessional;
import entities.Patient;
import medical.MedicalRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvExport {

    // Cet attribut contient le nom du dossier de sauvegarde commun des exportations en .csv de l'utilisateur :
    public static String exportsDirectory = "exportsDirectory";

    // Cette méthode permet de nettoyer les différents champs , afin de ne pas avoir d'erreurs d'affichage .csv :

    private static String cleanField(String value) {
        if (value == null) return "\"\"";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    // Exportation de la liste des Admins en .csv :

    public static void exportAdmins(MediPassSystem sys) {

        if (sys.getAdmins().isEmpty()){
            Tools.printOrange("-- Aucun admin n'est enregistré dans le système .. --");
            Tools.sleep(1.2);
        }else {
            String fileName = "liste_Admins.csv";

            String filePath = Tools.generateDirectoryAndFilePath(exportsDirectory , fileName);

            try (FileWriter writer = new FileWriter(filePath)) {

                writer.append("ID,Nom,Prénoms\n");

                for (Administrator admin : sys.getAdmins()) {
                    writer.append(String.valueOf(admin.getId())).append(",");
                    writer.append(cleanField(admin.getLastName())).append(",");
                    writer.append(cleanField(admin.getFirstNames()));
                    writer.append("\n");
                }

                Tools.printGreen("-- Liste des admins exportée avec succès ! --");
                Tools.sleep(1.2);
                Tools.print("[ Récupérez le fichier exporté : " + Colors.YELLOW + fileName + Colors.BLOCK + " dans le dossier : " + Colors.YELLOW + exportsDirectory + Colors.BLOCK + " ]");
                Tools.sleep(1.5);

            } catch (IOException e){
                Tools.print("!! Une erreur s'est produite lors de l'exportation : " + Colors.ORANGE + e.getMessage() + Colors.ORANGE + " !!");
            }
        }
    }

    // Exportation de la liste des Professionnels de Santé en .csv :

    public static void exportHealthPros(MediPassSystem sys) {
        if (sys.getHealthPros().isEmpty()){
            Tools.printOrange("-- Aucun professionnel de santé n'est enregistré dans le système .. --");
            Tools.sleep(1.2);
        }else {
            String fileName = "liste_Professionnels_Sante.csv";

            String filePath = Tools.generateDirectoryAndFilePath(exportsDirectory , fileName);

            try (FileWriter writer = new FileWriter(filePath)) {

                writer.append("ID,Nom,Prénoms,Spécialité\n");

                for (HealthProfessional healthPro : sys.getHealthPros()) {
                    writer.append(String.valueOf(healthPro.getId())).append(",");
                    writer.append(cleanField(healthPro.getLastName())).append(",");
                    writer.append(cleanField(healthPro.getFirstNames())).append(",");
                    writer.append(cleanField(healthPro.getSpeciality()));
                    writer.append("\n");
                }

                Tools.printGreen("-- Liste des professionnels de santé exportée avec succès ! --");
                Tools.sleep(1.2);
                Tools.print("[ Récupérez le fichier exporté : " + Colors.YELLOW + fileName + Colors.BLOCK + " , dans le dossier : " + Colors.YELLOW + exportsDirectory + Colors.BLOCK + " ]");
                Tools.sleep(1.5);

            } catch (IOException e){
                Tools.print("!! Une erreur s'est produite lors de l'exportation : " + Colors.ORANGE + e.getMessage() + Colors.ORANGE + " !!");
            }
        }
    }

    // Exportation de la liste des Patients en .csv :

    public static void exportActivePatients(MediPassSystem sys) {
        if (sys.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est enregistré dans le système .. --");
            Tools.sleep(1.2);
        }else {
            List<Patient> unarchivedPatients = new ArrayList<>();

            for (Patient patient : sys.getPatientsList().values()){
                if (!patient.getMedicalRecord().isArchived()) unarchivedPatients.add(patient);
            }

            if (unarchivedPatients.isEmpty()){
                Tools.printOrange("-- Aucun patient actif n'a été retrouvé .. --");
                Tools.sleep(1.2);
                return;
            }

            String fileName = "liste_Patients_Actifs.csv";

            String filePath = Tools.generateDirectoryAndFilePath(exportsDirectory , fileName);

            try (FileWriter writer = new FileWriter(filePath)) {

                writer.append("ID,Nom,Prénoms,Date de naissance,Sexe\n");

                for (Patient patient : unarchivedPatients) {
                    writer.append(String.valueOf(patient.getId())).append(",");
                    writer.append(cleanField(patient.getLastName())).append(",");
                    writer.append(cleanField(patient.getFirstNames())).append(",");
                    writer.append(cleanField(patient.getBirthDate())).append(",");
                    writer.append(cleanField(patient.getGender()));
                    writer.append("\n");
                }

                Tools.printGreen("-- Liste des patients actifs exportée avec succès ! --");
                Tools.sleep(1.2);
                Tools.print("[ Récupérez le fichier exporté : " + Colors.YELLOW + fileName + Colors.BLOCK + " , dans le dossier : " + Colors.YELLOW + exportsDirectory + Colors.BLOCK + " ]");
                Tools.sleep(1.5);

            } catch (IOException e){
                Tools.print("!! Une erreur s'est produite lors de l'exportation : " + Colors.ORANGE + e.getMessage() + Colors.ORANGE + " !!");
            }
        }
    }

    // Exportation de la liste des Patients à dossier médical archivé, en .csv :

    public static void exportArchivedPatients(MediPassSystem sys) {
        if (sys.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est enregistré dans le système .. --");
            Tools.sleep(1.2);
        }else if (sys.getArchivedMedicalRecords().isEmpty()) {
            Tools.printOrange("-- Aucun patient n'a été archivé dans le système --");
            Tools.sleep(1.2);
        }else {
            String fileName = "liste_Patients_Archives.csv";

            String filePath = Tools.generateDirectoryAndFilePath(exportsDirectory , fileName);

            try (FileWriter writer = new FileWriter(filePath)) {

                writer.append("ID,Nom,Prénoms,Date de naissance,Sexe\n");

                for (MedicalRecord medicalRecord : sys.getArchivedMedicalRecords()) {
                    Patient patient = medicalRecord.getPatient();
                    writer.append(String.valueOf(patient.getId())).append(",");
                    writer.append(cleanField(patient.getLastName())).append(",");
                    writer.append(cleanField(patient.getFirstNames())).append(",");
                    writer.append(cleanField(patient.getBirthDate())).append(",");
                    writer.append(cleanField(patient.getGender()));
                    writer.append("\n");
                }

                Tools.printGreen("-- Liste des patients archivés exportée avec succès ! --");
                Tools.sleep(1.2);
                Tools.print("[ Récupérez le fichier exporté : " + Colors.YELLOW + fileName + Colors.BLOCK + " , dans le dossier : " + Colors.YELLOW + exportsDirectory + Colors.BLOCK + " ]");
                Tools.sleep(1.5);

            } catch (IOException e){
                Tools.print("!! Une erreur s'est produite lors de l'exportation : " + Colors.ORANGE + e.getMessage() + Colors.ORANGE + " !!");
            }
        }
    }

}
