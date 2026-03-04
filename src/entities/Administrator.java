package entities;

import core.Backup;
import core.MediPassSystem;
import utilities.Colors;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe Administrateur du système :

public class Administrator extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    // Constructeur par défaut de le classe Administrateur :

    public Administrator(int id, String lastName, String firstNames){
        super(id, lastName, firstNames);
    }

    // Constructeur secondaire de le classe Administrateur :

    public Administrator(int id, String lastName, String firstNames , String login , String password){
        super(id, lastName, firstNames , login , password);
    }

    // Méthode pour créer un compte :

    public void createAccount(MediPassSystem sys){
        Tools.print("\n ::: Création de compte :::");
        Tools.sleep(1.2);

        List<String> names = Tools.askNames();

        boolean choice = Tools.askRoleChoice();

        if (choice){
            Tools.printYellow("-- Création d'un compte admin --");
            Tools.sleep(1.2);
            Administrator admin = new Administrator(sys.giveUserId() , names.get(0) , names.get(1));
            sys.addUser(admin);
            Tools.printGreen(":: Compte Administrateur créé avec succès ::");
            Tools.sleep(1);
            Tools.printInfosCreatedAccount(admin);
        }else {
            Tools.printYellow("-- Création du compte d'un professionnel de santé --");
            Tools.sleep(1.2);
            String speciality = Tools.askSpeciality();
            HealthProfessional healthPro = new HealthProfessional(sys.giveUserId() , names.get(0) , names.get(1) , speciality);
            sys.addUser(healthPro);
            Tools.printGreen(":: Compte du Professionnel de Santé créé avec succès ::");
            Tools.sleep(1);
            Tools.printInfosCreatedAccount(healthPro);
        }
    }

    // Cette méthode permet de donner le droit d'accès d'un dossier médical à un professionnel de santé :

    public void giveRecordAccess(MediPassSystem sys) {
        if (sys.getPatientsList().isEmpty() || sys.getProfessionals().isEmpty()){
            if (sys.getPatientsList().isEmpty()) { Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucun dossier médical n'est disponible --"); }
            else if (sys.getProfessionals().isEmpty()){ Tools.printOrange("-- Aucun professionnel de santé n'est encore enregistré dans le système .. --"); }
            Tools.sleep(1.2);
        }else {
            Map<Integer, Patient> patientsList = new HashMap<>();

            Tools.print("\n :: Veuillez rechercher le patient concerné ::\n");

            String patientName = Tools.askPatientNameForFetch().trim().toLowerCase();

            int patientIndex = 0;
            for (Patient patient : sys.getPatientsList().values()) {
                if (patient.getLastName().trim().toLowerCase().startsWith(patientName)) {
                    patientIndex++;
                    patientsList.put(patientIndex, patient);
                }
            }

            if (patientsList.isEmpty()) {
                Tools.printOrange("-- Aucun patient de ce nom n'a été trouvé .. --");
                Tools.sleep(1.2);
            } else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer, Patient> patientsIndexed : patientsList.entrySet()) {
                    Tools.print(patientsIndexed.getKey() + " - " + patientsIndexed.getValue().getLastName() + " " + patientsIndexed.getValue().getFirstNames() + " [ ID : " + patientsIndexed.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(patientIndex);
                Patient selectedPatient = patientsList.get(choice);
                Tools.print();
                Tools.print(":: Renseignez maintenant le professionnel de santé auquel vous voulez donner l'accès au dossier ::");
                Tools.print();
                User searchedUser = Tools.searchUser(sys);
                if (searchedUser == null){ return; }
                if (!(searchedUser instanceof HealthProfessional searchedHealthPro)) {
                    Tools.printOrange("\n !! Cet utilisateur n'est pas un professionnel de santé ; il s'agit d'un admin du système !! \n");
                    Tools.sleep(2);
                    return;
                }
                searchedHealthPro.setAccessForPatientRecord(selectedPatient.getId() , true);
                Backup.save(sys);
                Tools.printGreen(":: Le professionnel de santé choisi a maintenant accès au dossier médical du patient sélectionné ::");
                Tools.sleep(1.2);
            }
        }
    }

    // Cette méthode permet de retirer le droit d'accès d'un dossier médical à un professionnel de santé :

    public void removeRecordAccess(MediPassSystem sys) {
        if (sys.getPatientsList().isEmpty() || sys.getProfessionals().isEmpty()){
            if (sys.getPatientsList().isEmpty()) { Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucun dossier médical n'est disponible --"); }
            else if (sys.getProfessionals().isEmpty()){ Tools.printOrange("-- Aucun professionnel de santé n'est encore enregistré dans le système .. --"); }
            Tools.sleep(1.2);
        }else {
            Map<Integer, Patient> patientsList = new HashMap<>();

            Tools.print("\n :: Veuillez rechercher le patient concerné ::\n");

            String patientName = Tools.askPatientNameForFetch().trim().toLowerCase();

            int patientIndex = 0;
            for (Patient patient : sys.getPatientsList().values()) {
                if (patient.getLastName().trim().toLowerCase().startsWith(patientName)) {
                    patientIndex++;
                    patientsList.put(patientIndex, patient);
                }
            }

            if (patientsList.isEmpty()) {
                Tools.printOrange("-- Aucun patient de ce nom n'a été trouvé .. --");
            } else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer, Patient> patientsIndexed : patientsList.entrySet()) {
                    Tools.print(patientsIndexed.getKey() + " - " + patientsIndexed.getValue().getLastName() + " " + patientsIndexed.getValue().getFirstNames() + " [ ID : " + patientsIndexed.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(patientIndex);
                Patient selectedPatient = patientsList.get(choice);
                Tools.print();
                Tools.print(":: Renseignez maintenant le professionnel de santé auquel vous voulez retirer l'accès au dossier ::");
                Tools.print();
                User searchedUser = Tools.searchUser(sys);
                if (searchedUser == null){ return; }
                if (!(searchedUser instanceof HealthProfessional searchedHealthPro)) {
                    Tools.printOrange("\n !! Cet utilisateur n'est pas un professionnel de santé ; il s'agit d'un admin du système !! \n");
                    Tools.sleep(2);
                    return;
                }
                searchedHealthPro.setAccessForPatientRecord(selectedPatient.getId() , false);
                Backup.save(sys);
                Tools.printGreen(":: Le professionnel de santé choisi n'a plus accès au dossier médical du patient sélectionné ::");
                Tools.sleep(1.2);
            }
        }
    }

    // Profil de l'administrateur :

    @Override
    public void showProfile(){
        Tools.makeSmallDelimitation();
        Tools.print("* Rôle : < ADMIN du système > \n");
        Tools.print("* ID : " + Colors.YELLOW + this.getId() + Colors.BLOCK);
        Tools.print("* Nom : " + Colors.YELLOW + this.getLastName() + Colors.BLOCK);
        Tools.print("* Prénoms  : " + Colors.YELLOW + this.getFirstNames() + Colors.BLOCK);
        Tools.makeSmallDelimitation();
    }

}
