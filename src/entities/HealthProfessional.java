package entities;

import core.Backup;
import core.MediPassSystem;
import medical.Antecedent;
import medical.Consultation;
import medical.MedicalRecord;
import utilities.Colors;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe ProfessionnelSante du système :

public class HealthProfessional extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String speciality;

    private final Map<Integer , Boolean> medicalsRecordAccess = new HashMap<>();

    // Constructeur de le classe ProfessionnelSante :

    public HealthProfessional(int id, String lastName, String firstNames, String speciality) {
        super(id, lastName, firstNames);
        this.speciality = speciality;
    }

    public String getSpeciality() { return speciality; }

    public boolean getAccessForPatientRecord(int patientID) { return medicalsRecordAccess.getOrDefault(patientID , false); }

    public void setAccessForPatientRecord(int patientID , boolean access){ medicalsRecordAccess.put(patientID , access); }

    // Méthode du professionnel de santé pour la programmation d'une consultation :

    public void programConsultation(MediPassSystem sys){
        if (sys.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucune consultation ne peut être programmée --");
        }else {
            String consultationDate = Tools.askConsultationDate();

            if (!sys.isAvailable(this , consultationDate)){
                Tools.printOrange(":: Désolé ... Mais vous n'êtes pas libre pour une consultation à cette date . ::");
                Tools.sleep(2);
                return;
            }

            Map<Integer , Patient> fetchList = new HashMap<>();
            Tools.print("\n :: Recherchez à présent le patient pour lequel vous souhaitez programmer une consultation ::\n");
            String fetchName = Tools.askPatientNameForFetch().trim().toLowerCase();

            int index = 0;
            for (Patient patient : sys.getPatientsList().values()){
                if (patient.getLastName().trim().toLowerCase().startsWith(fetchName) && !patient.getMedicalRecord().isArchived()){
                    index++;
                    fetchList.put(index , patient);
                }
            }
            if (fetchList.isEmpty()){
                Tools.printOrange("-- Aucun patient de ce nom n'a été trouvé .. --");
                Tools.sleep(2);
            }else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer , Patient> indexedPatient : fetchList.entrySet()){
                    Tools.print(indexedPatient.getKey() + " - " + indexedPatient.getValue().getLastName() + " " + indexedPatient.getValue().getFirstNames() + " [ ID : " + indexedPatient.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(index);
                Patient patientSelected = fetchList.get(choice);
                String consultationReason = Tools.askConsultationReason();
                Consultation consultation = new Consultation(patientSelected , this , consultationDate.trim() , consultationReason);

                if (!this.medicalsRecordAccess.containsKey(patientSelected.getId())) { this.setAccessForPatientRecord(patientSelected.getId() , true); }

                sys.addConsultation(consultation);

                Tools.printGreen(":: Votre consultation a été programmée avec succès !! ::");
                Tools.sleep(2);
            }
        }
    }

    // Méthode pour enregistrer un patient avec son dossier médical dans le système :

    public void registerPatient(MediPassSystem sys){

        List<String> patientInfos = Tools.askPatientInfos();

        Patient patientToRegister = new Patient(sys.givePatientId() , patientInfos.get(0) , patientInfos.get(1) , patientInfos.get(2) , patientInfos.get(3));
        MedicalRecord patientMedicalRecord = new MedicalRecord(patientToRegister);
        patientToRegister.setMedicalRecord(patientMedicalRecord);

        this.setAccessForPatientRecord(patientToRegister.getId() , true);

        Tools.printYellow(":: Enregistrement des informations du patient ...");
        Tools.sleep(1.2);

        sys.addPatient(patientToRegister);

        Tools.printGreen(":: Patient enregistré avec succès ! ::");
        Tools.sleep(1.2);
    }

    // Cette méthode permet de consulter un patient :

    public void consultPatient(MediPassSystem sys) {
        if (sys.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. --");
            Tools.sleep(1.2);
        }else {
            Map<Integer, Patient> patientsFetchList = new HashMap<>();

            Tools.print("\n :: Veuillez rechercher le patient que vous souhaitez consulter ::\n");

            String patientName = Tools.askPatientNameForFetch().trim().toLowerCase();

            int patientIndex = 0;
            for (Patient patient : sys.getPatientsList().values()) {
                if (patient.getLastName().trim().toLowerCase().startsWith(patientName) && !patient.getMedicalRecord().isArchived()) {
                    patientIndex++;
                    patientsFetchList.put(patientIndex, patient);
                }
            }

            if (patientsFetchList.isEmpty()) {
                Tools.printOrange("-- Aucun patient de ce nom n'a été trouvé .. --");
                Tools.sleep(1.2);
            } else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer, Patient> indexedPatient : patientsFetchList.entrySet()) {
                    Tools.print(indexedPatient.getKey() + " - " + indexedPatient.getValue().getLastName() + " " + indexedPatient.getValue().getFirstNames() + " [ ID : " + indexedPatient.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(patientIndex);
                Patient patientSelected = patientsFetchList.get(choice);

                Map<Integer , Consultation> consultationsForThisPatient = new HashMap<>();
                int indexConsultation = 0;
                for (Consultation consultation : sys.getConsultationsToDo()){
                    if (consultation.getHealthPro().equals(this) && consultation.getPatient().equals(patientSelected)){
                        indexConsultation++;
                        consultationsForThisPatient.put(indexConsultation , consultation);
                    }
                }
                if (consultationsForThisPatient.isEmpty()){
                    Tools.printOrange("-- Vous n'avez programmé aucune consultation pour ce patient.. --");
                    Tools.sleep(2);
                }else {
                    Tools.print("\n :: Sélectionnez maintenant la date de la consultation parmi celles programmées pour ce patient :: \n");
                    Tools.sleep(1.2);
                    Tools.print("\n :: Liste des dates des consultations que vous avez programmées pour ce patient : \n");
                    for (Map.Entry<Integer , Consultation> indexedConsultation : consultationsForThisPatient.entrySet()){
                        Tools.print(indexedConsultation.getKey() + " - " + indexedConsultation.getValue().getConsultationDate() + " [ Motif : " + indexedConsultation.getValue().getConsultationReason() + " ]");
                    }
                    Tools.print();
                    Tools.print("* Veuillez entrer le numéro correspondant à la date de cette consultation ( ex : 1 , si c'est la première date de la liste ) : ");
                    int consultationChoosedIndex = Tools.askIndexInResults(indexConsultation);
                    String observations = Tools.askConsultationObservations();
                    Consultation currentConsultation = consultationsForThisPatient.get(consultationChoosedIndex);
                    currentConsultation.setObservations(observations);
                    Tools.printYellow(" :: Enregistrement de la consultation ... ");
                    Tools.sleep(1);
                    sys.registerConsultation(currentConsultation);
                    Tools.printGreen(":: La consultation de ce patient a été enregistrée avec succès ! ::");
                    Tools.sleep(1.3);
                    Tools.print("-- Voulez - vous voir des informations sur ce patient ? --");
                    boolean yesOrNo = Tools.askYesOrNoChoice();
                    if (yesOrNo){
                        patientSelected.showInfos();
                        Tools.sleep(1);
                    }else {
                        Tools.print("\n -- Comme vous voulez . \n");
                        Tools.sleep(1.3);
                    }

                    Tools.print("-- Voulez - vous consulter le dossier médical de ce patient ? --");
                    yesOrNo = Tools.askYesOrNoChoice();
                    if (yesOrNo){
                        if (!this.getAccessForPatientRecord(patientSelected.getId())) {
                            Tools.printOrange("\n !! Désolé .. mais vous n'avez pas accès au dossier médical de ce patient !! \n");
                            Tools.sleep(1.2);
                        }else {
                            Tools.print();
                            Tools.print("-- Dossier médical du Patient --");
                            patientSelected.getMedicalRecord().show();
                            Tools.sleep(1);
                        }
                    }else {
                        Tools.print("\n -- Comme vous voulez . \n");
                        Tools.sleep(1.3);
                    }
                }
            }
        }
    }

    // Méthode pour la consultation du dossier médical  d'un patient :

    public void consultMedicalRecord(MediPassSystem sys){
        if (sys.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucun dossier médical n'est disponible --");
            Tools.sleep(1.2);
        }else {
            Map<Integer, Patient> patientsList = new HashMap<>();

            Tools.print("\n :: Veuillez rechercher le patient pour lequel vous souhaitez consulter le dossier médical ::\n");

            String patientName = Tools.askPatientNameForFetch().trim().toLowerCase();

            int patientIndex = 0;
            for (Patient patient : sys.getPatientsList().values()) {
                if (patient.getLastName().trim().toLowerCase().startsWith(patientName) && !patient.getMedicalRecord().isArchived()) {
                    patientIndex++;
                    patientsList.put(patientIndex, patient);
                }
            }

            if (patientsList.isEmpty()) {
                Tools.printOrange("-- Aucun patient de ce nom n'a été trouvé .. --");
                Tools.sleep(1.2);
            } else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer, Patient> indexedPatient : patientsList.entrySet()) {
                    Tools.print(indexedPatient.getKey() + " - " + indexedPatient.getValue().getLastName() + " " + indexedPatient.getValue().getFirstNames() + " [ ID : " + indexedPatient.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(patientIndex);
                Patient patient = patientsList.get(choice);

                if (!this.getAccessForPatientRecord(patient.getId())) {
                    Tools.printOrange("\n !! Désolé .. mais vous n'avez pas accès au dossier médical de ce patient !! \n");
                    Tools.sleep(1.4);
                    return;
                }

                Tools.printYellow(":: Chargement du dossier médical de ce patient ...");
                Tools.sleep(1.3);
                Tools.print("\n :: Dossier médical du patient " + Colors.GREEN + patient.getLastName() + " " + patient.getFirstNames() + Colors.BLOCK + " :");
                patient.getMedicalRecord().show();
                Tools.sleep(1);
                Tools.print("\n " + Colors.YELLOW + " :: Voulez - vous ajouter un antécédent au dossier de ce patient ? :: " + Colors.BLOCK + " \n");
                boolean yesOrNo = Tools.askYesOrNoChoice();
                if (yesOrNo){
                    List<String> antecedentInfos = Tools.askAntecedentInfos();
                    Antecedent antecedent = new Antecedent(antecedentInfos.get(0) , antecedentInfos.get(1));
                    Tools.printYellow(":: Ajout de l'antécédent au dossier du patient ...");
                    Tools.sleep(2);
                    patient.getMedicalRecord().addAntecedent(antecedent);
                    Backup.save(sys);
                    Tools.printGreen(":: Antécédent ajouté avec succès au dossier du patient ::");
                    Tools.sleep(2);
                    Tools.print("\n :: Dossier médical du patient mis à jour :: \n");
                    patient.getMedicalRecord().show();
                }else {
                    Tools.print("\n -- Comme vous voulez . \n");
                    Tools.sleep(1.5);
                }
            }
        }
    }

    // Cette méthode permet au professionnel de santé d'archiver un dossier médical :

    public void archiveMedicalRecord(MediPassSystem sys){
        if (sys.getPatientsList().isEmpty()) {
            Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucun dossier médical n'est disponible --");
            Tools.sleep(1.2);
        }else {
            Map<Integer, Patient> patients = new HashMap<>();

            Tools.print("\n :: Veuillez rechercher le patient pour lequel vous souhaitez archiver le dossier médical ::\n");

            String patientSearched = Tools.askPatientNameForFetch().trim().toLowerCase();

            int patientIndex = 0;
            for (Patient patient : sys.getPatientsList().values()) {
                if (patient.getLastName().trim().toLowerCase().startsWith(patientSearched) && !patient.getMedicalRecord().isArchived()) {
                    patientIndex++;
                    patients.put(patientIndex, patient);
                }
            }

            if (patients.isEmpty()) {
                Tools.printOrange("-- Aucun patient de ce nom n'a été trouvé .. --");
                Tools.sleep(1.2);
            } else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer, Patient> indexedPatient : patients.entrySet()) {
                    Tools.print(indexedPatient.getKey() + " - " + indexedPatient.getValue().getLastName() + " " + indexedPatient.getValue().getFirstNames() + " [ ID : " + indexedPatient.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(patientIndex);
                Patient patient = patients.get(choice);

                if (!this.getAccessForPatientRecord(patient.getId())) {
                    Tools.printOrange("\n !! Désolé .. mais vous n'avez pas accès au dossier médical de ce patient !! \n");
                    Tools.sleep(1.4);
                    return;
                }

                Tools.printOrange("::: Voulez - vous vraiment archiver ce dossier médical ? :::");
                Tools.printOrange("< En archivant un dossier médical , toutes les consultations en attentes liées au patient seront supprimées >");
                boolean archiveRecord = Tools.askYesOrNoChoice();

                if (!archiveRecord) {
                    Tools.print("\n -- Vous n'avez plus archivé ce dossier -- \n");
                    Tools.sleep(1.2);
                } else {
                    sys.putInArchive(patient.getMedicalRecord());
                    Backup.save(sys);
                    Tools.printYellow("-- Ce dossier médical a bien été archivé --");
                    Tools.sleep(1.1);
                }
            }
        }
    }

    // Cette méthode permet au professionnel de santé de désarchiver un dossier médical :

    public void unarchiveMedicalRecord(MediPassSystem sys){
        if (sys.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucun dossier médical n'est disponible --");
            Tools.sleep(1.2);
        }else if (sys.getArchivedMedicalRecords().isEmpty()){
            Tools.printOrange("-- Aucun dossier médical n'a été archivé dans le système --");
            Tools.sleep(1.2);
        }else {
            Map<Integer, Patient> patients = new HashMap<>();

            Tools.print("\n :: Veuillez rechercher le patient pour lequel vous souhaitez désarchiver le dossier médical ::\n");

            String patientSearched = Tools.askPatientNameForFetch().trim().toLowerCase();

            int patientIndex = 0;
            for (MedicalRecord medicalRecord : sys.getArchivedMedicalRecords()) {
                if (medicalRecord.getPatient().getLastName().trim().toLowerCase().startsWith(patientSearched)) {
                    patientIndex++;
                    patients.put(patientIndex, medicalRecord.getPatient());
                }
            }

            if (patients.isEmpty()) {
                Tools.printOrange("-- Aucun patient de ce nom n'a été trouvé parmi les patients archivés .. --");
                Tools.sleep(1.2);
            } else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer, Patient> indexedPatient : patients.entrySet()) {
                    Tools.print(indexedPatient.getKey() + " - " + indexedPatient.getValue().getLastName() + " " + indexedPatient.getValue().getFirstNames() + " [ ID : " + indexedPatient.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(patientIndex);
                Patient patient = patients.get(choice);

                if (!this.getAccessForPatientRecord(patient.getId())) {
                    Tools.printOrange("\n !! Désolé .. mais vous n'avez pas accès au dossier médical de ce patient !! \n");
                    Tools.sleep(1.4);
                    return;
                }

                Tools.printOrange("::: Voulez - vous vraiment désarchiver ce dossier médical ? :::");
                boolean archiveRecord = Tools.askYesOrNoChoice();

                if (!archiveRecord) {
                    Tools.print("\n -- Vous n'avez plus désarchivé ce dossier -- \n");
                    Tools.sleep(1.2);
                } else {
                    sys.removeFromArchive(patient.getMedicalRecord());
                    Backup.save(sys);
                    Tools.printYellow("-- Ce dossier médical a bien été retiré des archives --");
                    Tools.sleep(1.1);
                }
            }
        }
    }

    public void consultArchivedRecord(MediPassSystem sys){
        if (sys.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucun dossier médical n'est disponible --");
            Tools.sleep(1.2);
        }else if (sys.getArchivedMedicalRecords().isEmpty()){
            Tools.printOrange("-- Aucun dossier médical n'a été archivé dans le système --");
            Tools.sleep(1.2);
        }else {
            Map<Integer, Patient> patientsList = new HashMap<>();

            Tools.print("\n :: Veuillez rechercher le patient pour lequel vous souhaitez consulter le dossier médical (archivé ) ::\n");

            String patientName = Tools.askPatientNameForFetch().trim().toLowerCase();

            int patientIndex = 0;
            for (MedicalRecord medicalRecord : sys.getArchivedMedicalRecords()) {
                if (medicalRecord.getPatient().getLastName().trim().toLowerCase().startsWith(patientName)) {
                    patientIndex++;
                    patientsList.put(patientIndex, medicalRecord.getPatient());
                }
            }

            if (patientsList.isEmpty()) {
                Tools.printOrange("-- Aucun patient de ce nom n'a été retrouvé parmi les patients archivés .. --");
                Tools.sleep(1.2);
            } else {
                Tools.print("\n :: Liste des patients trouvés :: \n");
                for (Map.Entry<Integer, Patient> indexedPatient : patientsList.entrySet()) {
                    Tools.print(indexedPatient.getKey() + " - " + indexedPatient.getValue().getLastName() + " " + indexedPatient.getValue().getFirstNames() + " [ ID : " + indexedPatient.getValue().getId() + " ]");
                }
                Tools.print();
                Tools.print("* Veuillez entrer le numéro du patient que vous voulez sélectionner : ");
                int choice = Tools.askIndexInResults(patientIndex);
                Patient patient = patientsList.get(choice);

                if (!this.getAccessForPatientRecord(patient.getId())) {
                    Tools.printOrange("\n !! Désolé .. mais vous n'avez pas accès au dossier médical de ce patient !! \n");
                    Tools.sleep(1.4);
                    return;
                }

                Tools.printYellow(":: Chargement du dossier médical de ce patient ...");
                Tools.sleep(1.3);
                Tools.printOrange("< Ce dossier médical est ARCHIVÉ ! Vous ne pouvez donc pas le modifier >");
                Tools.sleep(1);
                Tools.print("\n [ Ce dossier médical a été archivé le " + Colors.YELLOW + patient.getMedicalRecord().getArchivingDate() + Colors.BLOCK + " ] \n");
                Tools.sleep(1.1);
                Tools.print("\n :: Dossier médical archivé du patient " + Colors.GREEN + patient.getLastName() + " " + patient.getFirstNames() + Colors.BLOCK + " :");
                patient.getMedicalRecord().show();
                Tools.sleep(1);
            }
        }
    }

    // Profil du professionnel de sante :

    @Override
    public void showProfile(){
        Tools.makeSmallDelimitation();
        Tools.print("* Rôle : < Professionnel de Santé > \n");
        Tools.print("* ID : " + Colors.YELLOW + this.getId() + Colors.BLOCK);
        Tools.print("* Nom : " + Colors.YELLOW + this.getLastName() + Colors.BLOCK);
        Tools.print("* Prénoms  : " + Colors.YELLOW + this.getFirstNames() + Colors.BLOCK);
        Tools.print("* Spécialité  : " + Colors.YELLOW + this.getSpeciality() + Colors.BLOCK);
        Tools.makeSmallDelimitation();
    }

}
