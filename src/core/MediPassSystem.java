package core;

import entities.Administrator;
import entities.HealthProfessional;
import entities.Patient;
import entities.User;
import medical.Consultation;
import medical.MedicalRecord;
import utilities.Colors;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;


public class MediPassSystem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int currentUserId = 0;

    private int currentPatientId = 0;

    private final Map<Integer, Patient> patientsList = new HashMap<>();

    private final Map<Integer, User> usersList = new HashMap<>();

    private final List<HealthProfessional> healthProfessionals = new ArrayList<>();

    private final List<Administrator> admins = new ArrayList<>();

    private final Set<String> loginList = new HashSet<>();

    private final List<Consultation> consultationsToDoList = new ArrayList<>();

    private final List<Consultation> consultationsDoneList = new ArrayList<>();

    private final List<MedicalRecord> archivedMedicalRecords = new ArrayList<>();



    // Constructeur de le classe Système ( aucune initialisation n'était nécessaire ) :

    public MediPassSystem(){ }

    public Map<Integer, User> getUsersList() { return usersList; }

    public List<Administrator> getAdmins() { return admins; }

    public List<HealthProfessional> getHealthPros() { return healthProfessionals; }

    public Map<Integer, Patient> getPatientsList() { return patientsList; }

    public List<MedicalRecord> getArchivedMedicalRecords(){ return archivedMedicalRecords; }

    public void addInLoginList(String login) { loginList.add(login.trim().toLowerCase()); }

    public void removeFromLoginList(String login) { loginList.remove(login.trim().toLowerCase()); }

    public List<Consultation> getConsultationsToDo() { return consultationsToDoList; }

    public List<HealthProfessional> getProfessionals() { return healthProfessionals; }

    // Ajouter une consultation à faire dans le sytème :

    public void addConsultation(Consultation consultation){
        consultationsToDoList.add(consultation);
        Backup.save(this);
    }

    // Cette méthode permet d'enregister la consultation faite dans le système , et de l'ajouter au dossier médical du patient :

    public void registerConsultation(Consultation consultation){
        consultationsToDoList.remove(consultation);
        consultationsDoneList.add(consultation);
        consultation.getPatient().getMedicalRecord().addConsultation(consultation);
        Backup.save(this);
    }

    // Cette méthode affiche la liste des dossiers médicaux archivés :

    public void showArchivedMedicalRecords(){
        if (this.getPatientsList().isEmpty()){
            Tools.printOrange("-- Aucun patient n'est encore enregistré dans le système .. Donc aucun dossier médical n'est disponible --");
            Tools.sleep(1.2);
        }else if (this.getArchivedMedicalRecords().isEmpty()){
            Tools.printOrange("-- Aucun dossier médical n'a été archivé dans le système --");
            Tools.sleep(1.2);
        }else{
            Tools.print("\n :: Dossiers médicaux archivés :: \n");
            for (MedicalRecord medicalRecord : archivedMedicalRecords){
                Tools.print("+==========================================================================");
                Tools.print("|* Dossier médical de " + Colors.YELLOW + medicalRecord.getPatient().getLastName() + " " + medicalRecord.getPatient().getFirstNames() + Colors.BLOCK);
                Tools.print("|* Archivé le " + Colors.YELLOW + medicalRecord.getArchivingDate() + Colors.BLOCK);
                Tools.print("+==========================================================================");
            }
        }
        Tools.sleep(1.1);
    }

    // Cette méthode permet de stocker un dossier médical dans les archives :

    public void putInArchive(MedicalRecord medicalRecord){
        archivedMedicalRecords.add(medicalRecord);
        medicalRecord.archive();
        List<Consultation> consultationsToRemove = new ArrayList<>();
        for (Consultation consultation : consultationsToDoList){
            if (consultation.getPatient().equals(medicalRecord.getPatient())) consultationsToRemove.add(consultation);
        }
        for (Consultation consultation : consultationsToRemove){ consultationsToDoList.remove(consultation); }
    }

    // Cette méthode permet de désarchiver un dossier médical :

    public void removeFromArchive(MedicalRecord medicalRecord){
        archivedMedicalRecords.remove(medicalRecord);
        medicalRecord.unarchive();
    }

    // Permet de savoir si un login entré lors d'un changement du login est valide ou pas :

    public boolean isValidLogin(String login){ return !loginList.contains(login.trim().toLowerCase()); }

    // Cette méthode vérifie si un utilisateur possède son login dans le système :

    public boolean isLoginInBase(String login){ return loginList.contains(login.trim().toLowerCase()); }

    // Cette méthode permet de donner un nouvel ID à un patient :

    public int givePatientId(){
        currentPatientId++;
        return currentPatientId;
    }

    // Cette méthode permet de donner un nouvel ID à un utilisateur du système :

    public int giveUserId(){
        currentUserId++;
        return currentUserId;
    }

    // Vérifie l'existence d'un admin dans le système :

    public boolean adminExists(){
        if (usersList.isEmpty()){
            return false;
        }else {
            for (User user : usersList.values()){
                if (user instanceof Administrator) return true;
            }
        }
        return false;
    }

    // Ajouter un utilisateur dans le sytème :

    public void addUser(User user){
        usersList.put(user.getId() , user);
        if (user instanceof HealthProfessional) healthProfessionals.add((HealthProfessional) user);
        if (user instanceof Administrator) admins.add((Administrator) user);
        addInLoginList(user.getLogin());
        Backup.save(this);
    }

    // Ajouter un patient dans le système :

    public void addPatient(Patient patient){
        patientsList.put(patient.getId() , patient);
        Backup.save(this);
    }

    // Vérifie la disponibiité d'un professionnel de santé :

    public boolean isAvailable(HealthProfessional healthPro , String date){
        for (Consultation consultation : consultationsToDoList){
            if (consultation.getHealthPro().equals(healthPro) && consultation.getConsultationDate().equals(date)){ return false; }
        }
        return true;
    }

    // Permet d'obtenir un utilisateur du système :

    public User getUser(String login , String password){
        String hashedPassword = Tools.passwordHasher(password);
        for (User user : usersList.values()){
            if (user.getLogin().equals(login) && user.getPassword().equals(hashedPassword)) return user;
        }
        return null;
    }

    // Cette méthode permet de vérifier si quelqu'un est bien dans le système , de par ses identifiants :

    public boolean isUserInBase(String login , String password){
        String hashedPassword = Tools.passwordHasher(password);
        for (User user : usersList.values()){
            if (user.getLogin().equals(login) && user.getPassword().equals(hashedPassword)) return true;
        }
        return false;
    }

                    // Méthodes pour les statistiques du système :


    public int obtainUsersNumber(){ return usersList.size(); }

    public int obtainPatientsNumber(){ return patientsList.size(); }

    public int obtainHealthProNumber(){ return healthProfessionals.size(); }

    public int obtainAdminNumber(){ return admins.size(); }

    public int obtainConsultationsToDoNumber(){ return consultationsToDoList.size(); }

    public int obtainConsultationsDoneNumber(){ return consultationsDoneList.size(); }

    public int obtainArchivedRecordsNumber(){ return archivedMedicalRecords.size(); }

    public int obtainUnarchivedPatientsNumber(){ return patientsList.size() - archivedMedicalRecords.size(); }

    // Professionnels de santé par catégorie :

    public void showProByCategory(){

        List<HealthProfessional> doctors = new ArrayList<>();
        List<HealthProfessional> nurses = new ArrayList<>();
        List<HealthProfessional> pharmacists = new ArrayList<>();
        List<HealthProfessional> pediatricians = new ArrayList<>();
        String emptyMessage = "-- Aucun professionnel de santé de cette spécialité ... --";

        for (HealthProfessional healthPro : healthProfessionals){
            String speciality = healthPro.getSpeciality();
            switch (speciality){
                case "Médecin":
                    doctors.add(healthPro);
                    break;
                case "Infirmier":
                    nurses.add(healthPro);
                    break;
                case "Pharmacien":
                    pharmacists.add(healthPro);
                    break;
                case "Pédiatre":
                    pediatricians.add(healthPro);
                    break;
            }
        }

        Tools.print("\n :: Liste des professionnels de santé par Catégorie :: \n");

        Tools.makeLargeDelimitation();

        Tools.print(":: Médecins : \n");
        if (!doctors.isEmpty()){
            for (HealthProfessional doctor : doctors){
                Tools.showBaseUserInfos(doctor);
            }
        }else { Tools.printOrange(emptyMessage); }

        Tools.makeLargeDelimitation();

        Tools.print(":: Infirmiers : \n");
        if (!nurses.isEmpty()){
            for (HealthProfessional nurse : nurses){
                Tools.showBaseUserInfos(nurse);
            }
        }else { Tools.printOrange(emptyMessage); }

        Tools.makeLargeDelimitation();

        Tools.print(":: Pharmaciens : \n");
        if (!pharmacists.isEmpty()){
            for (HealthProfessional pharmacist : pharmacists){
                Tools.showBaseUserInfos(pharmacist);
            }
        }else { Tools.printOrange(emptyMessage); }

        Tools.makeLargeDelimitation();

        Tools.print(":: Pédiatres : \n");
        if (!pediatricians.isEmpty()){
            for (HealthProfessional pediatrician : pediatricians){
                Tools.showBaseUserInfos(pediatrician);
            }
        }else { Tools.printOrange(emptyMessage); }

        Tools.makeLargeDelimitation();
    }

    // Cette méthode permet de regrouper toutes les consultations d'une liste donnée (faites ou en attente ) , par période ( mois/année ) :

    public Map<String , List<Consultation>> obtainConsultationsByPeriod(List<Consultation> consultationsList){
        Map<String , List<Consultation>> consultationsByPeriod = new HashMap<>();
        for (Consultation consultation : consultationsList){
            String period = Tools.takeMonthYear(consultation.getConsultationDate());

            if (!consultationsByPeriod.containsKey(period)){ consultationsByPeriod.put(period , new ArrayList<>()); }

            consultationsByPeriod.get(period).add(consultation);
        }

        return consultationsByPeriod;
    }

    // Méthode générique , permettant d'afficher les consultations par période suivant une liste de consultations données :

    public void showConsultationsByPeriod(String title , List<Consultation> consultationsList , boolean done){
        Tools.print("\n " + title + " \n");

        if (consultationsList.isEmpty()) {
            Tools.printOrange("- Aucune consultation trouvée -");
            return;
        }

        Map<String , List<Consultation>> consultationsMap = obtainConsultationsByPeriod(consultationsList);
        String format;
        int periodSize;
        for (String period : consultationsMap.keySet()) {
            periodSize = consultationsMap.get(period).size();
            format = Tools.pluralOrSingular(periodSize , "consultation");
            Tools.print("\n - Période du " + Colors.YELLOW + period + Colors.BLOCK  + " ( " + Colors.GREEN + periodSize + Colors.BLOCK + " " + format +" ) - \n");
            for (Consultation consultation : consultationsMap.get(period)) {
                Tools.showBaseConsultationInfos(consultation , done);
            }
        }
    }

    // Cette méthode permet d'afficher toutes les consultations déjà faites par période ( mois/année ) :

    public void showConsultationsDoneByPeriod(){ showConsultationsByPeriod("--- Consultations effectuées ---" , consultationsDoneList , true); }

    // Cette méthode permet d'afficher toutes les consultations en attente , par période ( mois/année ) :

    public void showConsultationsToDoByPeriod(){ showConsultationsByPeriod("--- Consultations en attente ---" , consultationsToDoList , false); }

    // Pourcentage des patients consultés au moins 1 fois :

    public double obtainConsultedPatientsPercent(){

        if (patientsList.isEmpty()) return 0.0;

        int consultedPatientsNumber = 0;
        for (Patient patient : patientsList.values()){
            if (patient.getMedicalRecord().getConsultationsNumber() > 0){ consultedPatientsNumber++; }
        }
        return (consultedPatientsNumber * 100.0) / patientsList.size();
    }

    // Pourcentage des patients jamais consultés :

    public double obtainNeverConsultedPatientsPercent(){
        if (patientsList.isEmpty()) return 0.0;
        return 100.0 - this.obtainConsultedPatientsPercent();
    }

    // Affichage du profile des utilisateurs du système :

    public void showAccountsList(){
        String usersNumber = (usersList.size() == 1) ? "\n < Le système possède actuellement " + Colors.YELLOW + "1" + Colors.BLOCK + " seul utilisateur > \n" : "\n < Le système possède actuellement " + Colors.YELLOW + usersList.size() + Colors.BLOCK + " utilisateurs > \n";
        Tools.print(usersNumber);
        Tools.print(":: Liste des utilisateurs du système ::");
        Tools.makeLargeDelimitation();
        for (Map.Entry<Integer, User> user : usersList.entrySet()) {
            Tools.print("-- Utilisateur no " + user.getKey() + " --");
            user.getValue().showProfile();
        }
        Tools.makeLargeDelimitation();
        Tools.sleep(1);
    }

    // Affichage de la liste des patients à consulter, d'un professionnel de santé :

    public void showPatientsToConsult(HealthProfessional healthPro){
        if (patientsList.isEmpty()){
            Tools.printOrange("-- Aucun patient n'est enregistré dans le système , donc vous ne possédez aucune consultation en attente --");
            Tools.sleep(1);
        }else {
            List<Consultation> consultationsToDo = new ArrayList<>();
            for (Consultation consultation : consultationsToDoList){
                if (consultation.getHealthPro().equals(healthPro)) consultationsToDo.add(consultation);
            }
            if (consultationsToDo.isEmpty()){
                Tools.printOrange("-- Vous n'avez aucune consultation en attente --");
                Tools.sleep(1);
            }else { Tools.showBasePatientInfos(consultationsToDo); }
        }
    }

    // Affichage des statistiques du système :

    public void showStatistics(){
        String formatPatientsNumber = Tools.pluralOrSingular(patientsList.size() , " patient");
        String formatUsersNumber = Tools.pluralOrSingular(usersList.size() , " utilisateur");
        String formatHealthProsNumber = Tools.pluralOrSingular(healthProfessionals.size() , " professionnel");
        String formatAdminsNumber = Tools.pluralOrSingular(admins.size() , " admin");
        String formatConsultationDoneNumber = Tools.pluralOrSingular(consultationsDoneList.size() , " consultation");
        String formatConsultationToDoNumber = Tools.pluralOrSingular(consultationsToDoList.size() , " consultation");
        String formatArchivedNumber = Tools.pluralOrSingular(archivedMedicalRecords.size() , " dossier");
        String formatUnarchivedNumber = Tools.pluralOrSingular(this.obtainUnarchivedPatientsNumber() , " patient");

        Tools.print(Colors.YELLOW + "::::: Statistiques du Système d'Information Médical :::::" + Colors.BLOCK);
        Tools.makeLargeDelimitation();
        Tools.print("* Nombre de patients enregistrés dans le système : " + Colors.YELLOW + this.obtainPatientsNumber() + Colors.BLOCK + formatPatientsNumber);
        Tools.print();
        Tools.print("* Nombre de dossiers médicaux archivés dans le système : " + Colors.YELLOW + this.obtainArchivedRecordsNumber() + Colors.BLOCK + formatArchivedNumber);
        Tools.print();
        Tools.print("* Nombre de patients actifs : " + Colors.YELLOW + this.obtainUnarchivedPatientsNumber() + Colors.BLOCK + formatUnarchivedNumber);
        Tools.print();
        Tools.print("* Nombre d'utilisateurs du système : " + Colors.YELLOW + this.obtainUsersNumber() + Colors.BLOCK + formatUsersNumber);
        Tools.print();
        Tools.print("* Nombre de Professionnels de Santé : " + Colors.YELLOW + this.obtainHealthProNumber() + Colors.BLOCK + formatHealthProsNumber + " de santé");
        Tools.print();
        Tools.print("* Nombre d'Admins du système : " + Colors.YELLOW + this.obtainAdminNumber() + Colors.BLOCK + formatAdminsNumber);
        Tools.print();
        Tools.print("* Nombre total de consultations effectuées par les professionnels de santé : " + Colors.YELLOW + this.obtainConsultationsDoneNumber() + Colors.BLOCK + formatConsultationDoneNumber);
        Tools.print();
        Tools.print("* Nombre total de consultations en attente : " + Colors.YELLOW + this.obtainConsultationsToDoNumber() + Colors.BLOCK + formatConsultationToDoNumber);
        Tools.print();
        Tools.print("* Pourcentage de patients consultés au moins une fois : " + Colors.YELLOW + String.format("%.2f" , this.obtainConsultedPatientsPercent()) + "%" +  Colors.BLOCK);
        Tools.print();
        Tools.print("* Pourcentage de patients jamais consultés depuis leur enregistrement : " + Colors.YELLOW + String.format("%.2f", this.obtainNeverConsultedPatientsPercent()) + "%" +  Colors.BLOCK);
        Tools.print();
        this.showProByCategory();
        Tools.print("::: Consultations Par Période :::");
        Tools.print();
        Tools.print("( Les consultations sont regroupées par période sous le format : " + Colors.YELLOW + "< mois/année >" + Colors.BLOCK + " )");
        this.showConsultationsDoneByPeriod();
        this.showConsultationsToDoByPeriod();
        Tools.makeLargeDelimitation();
        Tools.sleep(1.2);
    }

}
