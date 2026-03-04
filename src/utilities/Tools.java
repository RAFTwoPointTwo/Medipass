package utilities;

import core.Backup;
import core.MediPassSystem;
import entities.Administrator;
import entities.User;
import medical.Consultation;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Tools {

    // Cette classe Tools regroupe toutes les méthodes utilitaires que nous pouvons appeler dans notre programme au besoin , pour privilégier la modularité du code :

    private static final Scanner scanner = new Scanner(System.in);

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String saveFileName =  "save/saveFile.txt";


    public static <T> void print(T args){ System.out.println(args); }

    public static void print(){ System.out.println(); }

    public static void printEnumeration(int num , String str){ print(Colors.YELLOW + num + " - " + Colors.BLOCK + str); }

    public static void printGreen(String str){
        print();
        print(Colors.GREEN + str + Colors.BLOCK);
        print();
    }

    public static void printOrange(String str){
        print();
        print(Colors.ORANGE + str + Colors.BLOCK);
        print();
    }

    public static void printYellow(String str){
        print();
        print(Colors.YELLOW + str + Colors.BLOCK);
        print();
    }

    // Affichage du message de bienvenue de notre application :

    public static void printWelcome(){
        print("                         +=================================================================+");
        print("                         |                                                                 |");
        print("                         |                                                                 |");
        print("                         |     " + Colors.YELLOW + "::: BIENVENUE dans le Système d'Information Médical :::" + Colors.BLOCK + "     |");
        print("                         |                                                                 |");
        print("                         |                                                                 |");
        print("                         +=================================================================+");
    }



    // Ces deux méthodes sleep permettent d'améliorer l'expérience utilisateur de notre application, en ralentissement légèrement
    // l'affichage de certains blocks de texte , afin qu'ils ne s'affichent pas instantanément dans la console :

    public static void sleep(int timeInSecond){
        try {
            int duration = timeInSecond * 1000;
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public static void sleep(double timeInSecond){
        try {
            long duration = Math.round(timeInSecond * 1000.0);
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    // C'est une méthode qui sera appelée pour demander le choix d'un professionnel de santé qui se
    // connecte , sans avoir à reprendre toute la logique dans le fichier principal Main.java :

    public static int askChoiceToHealthPro(){

        int choice;

        while (true){

        print("\n ::: Que voulez-vous faire ? \n");

        printEnumeration(1 , "Enregistrer un patient ?");
        printEnumeration(2 , "Programmer une consultation ?");
        printEnumeration(3 , "Consulter un patient ?");
        printEnumeration(4 , "Consulter un dossier médical , ou consulter un dossier et y ajouter un antécédent ?");
        printEnumeration(5 , "Consulter un dossier médical archivé ?");
        printEnumeration(6 , "Archiver un dossier médical ?");
        printEnumeration(7 , "Désarchiver un dossier médical ?");
        printEnumeration(8 , "Afficher la liste des dossiers médicaux archivés ?");
        printEnumeration(9 , "Voir la liste des patients à consulter ?");
        printEnumeration(10 , "Voir votre profil ?");
        printEnumeration(11 , "Changer vos identifiants ?");
        printEnumeration(12 , "Exporter la liste des patients actifs en .csv ?");
        printEnumeration(13 , "Exporter la liste des patients archivés en .csv ?");
        printEnumeration(14 , "Déconnexion ?");
        printYellow("< En vous déconnectant , les données du système sont sauvegardées , et le système s'éteint >");

        System.out.print("\n * Entrez votre choix : ");

            try{
                choice = scanner.nextInt();
                if (choice < 1 || choice > 14) {
                    print("\n !! Veuillez choisir une proposition entre 1 et 14 !! \n");
                    sleep(1.2);
                }else {
                    scanner.nextLine();
                    return choice;
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer l'une des valeurs proposées ( ex : 3 ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }
    }

    // Cette méthode joue le même rôle que la précédente méthode , sauf qu'elle est lancée pour un Admin qui se connecte :

    public static int askChoiceToAdmin(){

        int choice;

        while (true){

            print("\n ::: Que voulez-vous faire ? \n");

            printEnumeration(1 , "Créer un compte ?");
            printEnumeration(2 , "Retirer l'accès d'un dossier médical à un professionnel de santé ?");
            printEnumeration(3 , "Donner l'accès d'un dossier médical à un professionnel de santé ?");
            printEnumeration(4 , "Voir votre profil ?");
            printEnumeration(5 , "Afficher la liste des comptes du système ?");
            printEnumeration(6 , "Afficher les statistiques du système ?");
            printEnumeration(7 , "Changer vos identifiants ?");
            printEnumeration(8 , "Exporter la liste des Admins en .csv ?");
            printEnumeration(9, "Exporter la liste des professionnels de santé en .csv ?");
            printEnumeration(10 , "Déconnexion ?");
            printYellow("< En vous déconnectant , les données du système sont sauvegardées , et le système s'éteint >");

            System.out.print("\n * Entrez votre choix : ");

            try{
                choice = scanner.nextInt();
                if (choice < 1 || choice > 10) {
                    print("\n !! Veuillez choisir une proposition entre 1 et 10 !! \n");
                    sleep(1.2);
                }else {
                    scanner.nextLine();
                    return choice;
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer l'une des valeurs numériques proposées ( ex : 2 ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }
    }

    public static void makeSmallDelimitation(){
        print();
        print("===================================================================");
        print();
    }

    public static void makeLargeDelimitation(){
        print();
        print("======================================================================================================================================================================");
        print();
    }

    // Méthode permettant d'afficher le nom , les prénoms et l'ID d'un utilisateur :

    public static void showBaseUserInfos(User user){ print("- " + user.getLastName() + " " + user.getFirstNames() + " [ ID : " + user.getId() + " ] "); }

    // Méthode permettant d'afficher les informations de base d'une consultation pour le rendu dans les statistiques , selon qu'elle ait déjà été faite ou non :

    public static void showBaseConsultationInfos(Consultation consultation , boolean done){
        String consultedText = done ? " ayant consulté " : " devant consulter ";
        String doneText = done ? " faite le " : " à faire le ";

        print("+================================================================================================");
        print("| * Consultation" + doneText + consultation.getConsultationDate() + " ; motif : " + consultation.getConsultationReason() + " .");
        print("| * Professionnel de santé " + consultation.getHealthPro().getLastName() + " " + consultation.getHealthPro().getFirstNames() + "," + consultedText + "le patient : " + consultation.getPatient().getLastName() + " " + consultation.getPatient().getFirstNames() + " .");
        print("+================================================================================================");
    }

    // Cette méthode permet d'afficher les informations de base d'un patient dans la liste des patients à consulter d'un professionnel de santé :

    public static void showBasePatientInfos(List<Consultation> consultationsList){
        for (Consultation consultation : consultationsList){
            print();
            print("+================================================================================");
            print("| * Patient : " + Colors.YELLOW + consultation.getPatient().getLastName() + " " + consultation.getPatient().getFirstNames() + Colors.BLOCK + ", à consulter le : " + Colors.YELLOW + consultation.getConsultationDate() + Colors.BLOCK + " .");
            print("+================================================================================");
        }
        sleep(1.2);
    }

    // Cette méthode permet de ne pas alourdir la logique de changement d'identifiants ( la méthode .changeIdentifiers() de User )
    // dans la classe User , afin que le code soit plus modulaire :

    public static List<String> askIdentifiers(){
        String login;
        String password;
        List<String> identifiers = new ArrayList<>();

        print();
        print("\n :: Veuillez entrer vos identifiants actuels : \n");
        do {
            System.out.print("\n * Votre login : ");
            login = scanner.nextLine();
            if (login.isBlank()) print("\n" + Colors.ORANGE + " !! Veuillez entrer votre login !!" + Colors.BLOCK +" \n");
        }while (login.isBlank());

        print();

        do {
            System.out.print("\n * Votre mot de passe : ");
            password = scanner.nextLine();
            if (password.isBlank()) print("\n" + Colors.ORANGE + " !! Veuillez entrer votre mot de passe !!" + Colors.BLOCK +" \n");
        }while (password.isBlank());

        identifiers.add(login.trim());
        identifiers.add(password.trim());

        return identifiers;
    }

    // Cette méthode joue le même role que la méthode précédente , sauf qu'elle est appelée pour recueillir les nouveaux identifiants de l'utilisateur , après changement :

    public static List<String> askIdentifiersForChange(MediPassSystem sys){
        String login;
        String password;
        List<String> identifiers = new ArrayList<>();

        print();
        print("\n " + Colors.YELLOW + ":::" + Colors.BLOCK + " Veuillez à présent entrer vos nouveaux identifiants : \n");
        do {
            System.out.print("\n * Entrez un login : ");
            login = scanner.nextLine();
            if (login.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un login !!");
                print();
                sleep(1.2);
            } else if (!sys.isValidLogin(login)) { printOrange("!! Ce login est déjà pris par un autre utilisateur !!"); }
        }while (login.isBlank() || !sys.isValidLogin(login));

        print();

        do {
            System.out.print("\n * Entrez un mot de passe : ");
            password = scanner.nextLine();
            if (password.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un mot de passe !!");
                print();
                sleep(1.2);
            } else if (password.length() < 8) {
                print();
                printOrange("!! Le mot de passe doit contenir au minimum 8 caractères !!");
                print();
                sleep(1.2);
            }
        }while (password.isBlank() || password.length() < 8);

        identifiers.add(login.trim());
        identifiers.add(password.trim());

        return identifiers;
    }

    // Cette méthode permet de hasher les mots de passe des utilisateurs , afin qu'ils ne soient pas enregistrés en claire dans notre système :

    public static String passwordHasher(String password) {

        try {
            MessageDigest baseAlgorithm = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = baseAlgorithm.digest(password.getBytes());

            StringBuilder builder = new StringBuilder();

            for (byte b : hashBytes) { builder.append(String.format("%02x", b)); }

            return builder.toString();

        } catch (NoSuchAlgorithmException e) { throw new RuntimeException("\n !! Une erreur de hashage de mot de passe s'est produite ... !! \n"); }

    }

    // Cette méthode permet de récupérer la portion mois/année d'une date jour/mois/année :

    public static String takeMonthYear(String date){ return date.substring(3); }

    // Cette méthode permet d'afficher un mot au pluriel ou au singulier selon le nombre d'occurences à l'objet en question ; pour les mots dont le pluriel est par "s" :

    public static String pluralOrSingular(int count , String singularWord){ return (count > 1) ? singularWord + "s" : singularWord; }

    // Cette méthode permet de demander au professionnel de santé qui souhaite enregistrer un patient , les informations dont le
    // programme a besoin pour construire le dossier médical du patient et l'enregistrer dans le système :

    public static List<String> askPatientInfos(){
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int day = 0;
        int month = 0;
        int year = 0;
        boolean notValidDay = true;

        String lastName;
        String firstNames;
        String birthDate;
        String gender = "";

        List<String> patientInfos = new ArrayList<>();

        print("\n :: Veuillez renseigner les informations concernant le patient à enregistrer : \n");
        sleep(1.2);

        do {
            System.out.print("\n * Quel est le nom du patient ? : ");
            lastName = scanner.nextLine();
            if (lastName.isBlank()) {
                print();
                printOrange("!! Veuillez entrer son Nom !!");
                print();
                sleep(1.2);
            }
        }while (lastName.isBlank());

        print();

        do {
            System.out.print("\n * Quels sont les prénoms du patient ? : ");
            firstNames = scanner.nextLine();
            if (firstNames.isBlank()) {
                print();
                printOrange("!! Veuillez entrer ses Prénoms !!");
                print();
                sleep(1.2);
            }
        }while (firstNames.isBlank());

        print();

        do {
            System.out.print("* En quel année le patient est - il né  ? : ");
            try{
                year = scanner.nextInt();
                if (year < 1800 || year > currentYear) {
                    print("\n !! Veuillez choisir une année valide ( Entre 1800 et " + currentYear + " ) !! \n");
                    sleep(1.2);
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer une valeur numérique représentant une année ( ex : 2014 ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while (year < 1800 || year > currentYear);

        print();
        scanner.nextLine();

        do {
            System.out.print("* En quel mois est né le patient ? ( ex : 10 pour Octobre ) : ");
            try{
                month = scanner.nextInt();
                if (month < 1 || month > 12 || (year == currentYear && month > currentMonth)) {
                    print("\n !! Veuillez choisir un mois valide et non futur !! \n");
                    sleep(1.2);
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer une valeur numérique représentant un mois ( ex : 8 pour Août ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while (month < 1 || month > 12 || (year == currentYear && month > currentMonth));

        print();
        scanner.nextLine();

        do {
            System.out.print("* Quel jour du mois est né le patient ? ( ex : 23 ) : ");
            try{
                day = scanner.nextInt();
                boolean verification1 = (day < 1) || (year == currentYear && month == currentMonth && day > currentDay) || (month == 2 && day > 29);
                boolean verification2 = ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) || ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31);
                notValidDay = verification1 || verification2;
                if (day < 1){
                    print("\n !! Veuillez choisir un jour valide !! \n");
                    sleep(1.2);
                } else if (year == currentYear && month == currentMonth && (day > currentDay)){
                    print("\n !! Le patient ne peut pas être né à une date le futur; veuillez choisir un jour inférieur à la date d'aujourd'hui !! \n");
                    sleep(1.2);
                } else if (month == 2 && day > 29) {
                    print("\n !! Veuillez choisir un jour valide pour le mois de février ( Entre 1 et 29 ) !! \n");
                    sleep(1.2);
                } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30){
                    print("\n !! Le mois que vous avez choisi possède 30 jours ; veuillez entrer un jour entre 1 et 30 !! \n");
                    sleep(1.2);
                } else if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31){
                    print("\n !! Le mois que vous avez choisi possède 31 jours ; veuillez entrer un jour entre 1 et 31 !! \n");
                    sleep(1.2);
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer une valeur numérique représentant le jour dans un mois ( ex : 17 pour le 17e jour du mois ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while (notValidDay);

        print();
        scanner.nextLine();

        do {
            System.out.print("* Quel est le sexe du patient ? : ");
            print();
            print("1 - Homme ?");
            print("2 - Femme ?");
            System.out.print("* Sexe du patient : ");
            try{
                int choice;
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    print("\n !! Veuillez choisir un chiffre parmi ceux proposés ( ex : 2 pour Femme ) !! \n");
                    sleep(1.2);
                }else{ gender = (choice == 1 ) ? "Homme" : "Femme"; }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer une valeur numérique représentant le genre du Patient !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while (gender.isBlank());

        scanner.nextLine();

        String strDay = day < 10 ? "0" + day : day + "";
        String strMonth = month < 10 ? "0" + month : month + "";

        birthDate = strDay + "/" + strMonth + "/" + year;

        patientInfos.add(lastName);
        patientInfos.add(firstNames);
        patientInfos.add(birthDate);
        patientInfos.add(gender);

        return patientInfos;
    }

    // Cette méthode permet de demander la date d'une consultation qui doit être faite, sachant qu'une
    // consultation ne peut pas être faite à une date passée par rapport au jour actuel de la programmation :

    public static String askConsultationDate(){
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int day = 0;
        int month = 0;
        int year = 0;
        boolean notValidDay = true;

        String date;

        print("\n :: Veuillez renseigner les informations concernant la date de la consultation : \n");
        sleep(0.8);

        do {
            System.out.print("* En quel année doit se faire la consultation du patient ? : ");
            try{
                year = scanner.nextInt();
                if (year < currentYear) {
                    print("\n !! Veuillez choisir une année valide ( supérieure ou égale à " + currentYear + " ) !! \n");
                    sleep(1.2);
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer une valeur numérique représentant une année ( ex : 2026 ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while (year < currentYear);

        print();
        scanner.nextLine();

        do {
            System.out.print("* En quel mois doit se faire cette consultation ? ( ex : 11 pour Novembre ) : ");
            try{
                month = scanner.nextInt();
                if ((year > currentYear && (month < 1 || month > 12)) || (year == currentYear && (month < currentMonth || month > 12))) {
                    print("\n !! Veuillez choisir un mois valide et non passé !! \n");
                    sleep(1.2);
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer une valeur numérique représentant un mois ( ex : 8 pour Août ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while ((year > currentYear && (month < 1 || month > 12)) || (year == currentYear && (month < currentMonth || month > 12)));

        print();
        scanner.nextLine();

        do {
            System.out.print("* Quel jour du mois doit se faire la consultation du patient ? ( ex : 23 ) : ");
            try{
                day = scanner.nextInt();
                boolean verification1 = (day < 1) || (year == currentYear && month == currentMonth && day < currentDay) || (month == 2 && day > 29);
                boolean verification2 = ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) || ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31);
                notValidDay = verification1 || verification2;
                if (day < 1){
                    print("\n !! Veuillez choisir un jour valide pour ce mois !! \n");
                    sleep(1.2);
                } else if (year == currentYear && month == currentMonth && (day < currentDay)){
                    print("\n !! Veuillez choisir un jour valide pour ce mois, supérieur ou égale au jour actuel ( Nous sommes le " + currentDay + " aujourd'hui ) !! \n");
                    sleep(1.2);
                } else if (month == 2 && day > 29) {
                    print("\n !! Veuillez choisir un jour valide pour le mois de février ( Entre 1 et 29 ) !! \n");
                    sleep(1.2);
                } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30){
                    print("\n !! Le mois que vous avez choisi possède 30 jours ; veuillez entrer un jour entre 1 et 30 !! \n");
                    sleep(1.2);
                } else if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31){
                    print("\n !! Le mois que vous avez choisi possède 31 jours ; veuillez entrer un jour entre 1 et 31 !! \n");
                    sleep(1.2);
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer une valeur numérique représentant le jour dans un mois ( ex : 17 pour le 17e jour du mois ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        } while (notValidDay);

        print();
        scanner.nextLine();

        String strDay = day < 10 ? "0" + day : day + "";
        String strMonth = month < 10 ? "0" + month : month + "";

        date = strDay + "/" + strMonth + "/" + year;

        return date.trim();
    }

    // Cette méthode permet de demander les nom et prénoms d'un utilisateur du système:

    public static List<String> askNames(){
        String lastName;
        String firstNames;
        List<String> names = new ArrayList<>();

        print();
        print("\n :: Veuillez entrer les nom et prénoms du sujet du compte : \n");
        do {
            System.out.print("\n * Quel est son nom ? : ");
            lastName = scanner.nextLine();
            if (lastName.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un Nom !!");
                print();
                sleep(1.2);
            }
        }while (lastName.isBlank());

        print();

        do {
            System.out.print("\n * Quels sont ses prénoms ? : ");
            firstNames = scanner.nextLine();
            if (firstNames.isBlank()) {
                print();
                printOrange("!! Veuillez entrer ses prénmoms !!");
                print();
                sleep(1.2);
            }
        }while (firstNames.isBlank());

        names.add(lastName);
        names.add(firstNames);

        return names;
    }

    // Cette méthode demande le nom de famille du patient que le professionnel de santé recherche :

    public static String askPatientNameForFetch(){

        String lastName;

        do {
            System.out.print("\n :: Entrez le nom de famille du patient que vous recherchez : ");
            lastName = scanner.nextLine();
            if (lastName.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un Nom !!");
                print();
                sleep(1.2);
            }
        }while (lastName.isBlank());

        return lastName;
    }

    // Cette méthode demande l'index d'un résultat de recherche , dans la liste des résultats générés par cette recherche :

    public static int askIndexInResults(int numberOfFetchResults){
        int choice;
        String invalidMessage = (numberOfFetchResults > 1) ? "\n !! Veuillez choisir une proposition entre 1 et " + numberOfFetchResults + " !! \n" : "\n !! Veuillez choisir la proposition 1 ( la seule proposition ) !! \n";

        do {
            System.out.print("* Votre choix : ");
            try{
                choice = scanner.nextInt();
                if (choice < 1 || choice > numberOfFetchResults) {
                    print(invalidMessage);
                    sleep(1.2);
                }else {
                    scanner.nextLine();
                    return choice;
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer l'une des valeurs numériques proposées ( ex : 1 ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while (true);
    }

    // Cette méthode permet de demander au professionnel de santé le motif d'une consultation :

    public static String askConsultationReason(){

        String reason;

        do {
            System.out.print("\n :: Entrez le motif de cette consultation : ");
            reason = scanner.nextLine();
            if (reason.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un motif pour cette consultation !!");
                print();
                sleep(1.2);
            }
        }while (reason.isBlank());

        return reason;
    }

    // Cette méthode permet de demander au professionnel de santé les observations d'une consultation :

    public static String askConsultationObservations(){

        String observations;

        do {
            System.out.print("\n :: Entrez vos observations faites lors de cette consultation : ");
            observations = scanner.nextLine();
            if (observations.isBlank()) {
                print();
                printOrange("!! Veuillez entrer vos observations pour cette consultation !!");
                print();
                sleep(1.2);
            }
        }while (observations.isBlank());

        return observations;
    }

    // Cette méthode permet de demander au professionnel de santé des informtions sur un antécédent du patient :

    public static List<String> askAntecedentInfos(){

        String nature;
        String description;

        List<String> infos = new ArrayList<>();

        do {
            System.out.print("\n :: Quelle est la nature de cet antécédent ? ( Allergie , Toux réccurentes , etc ) : ");
            nature = scanner.nextLine();
            if (nature.isBlank()) {
                print();
                printOrange("!! Veuillez renseigner la nature de cet antécédent !!");
                print();
                sleep(1.2);
            }
        }while (nature.isBlank());

        do {
            System.out.print("\n :: Donnez une description de cet antécédent ( détails sur l'antécédent ) . \n :: Vous pouvez simplement entrer un '/' si une description n'est pas nécessaire : ");
            description = scanner.nextLine();
            if (description.isBlank()) {
                print();
                printOrange("!! Veuillez renseigner une description de cet antécédent !!");
                print();
                sleep(1.2);
            }
        }while (description.isBlank());

        infos.add(nature);
        infos.add(description);

        return infos;
    }

    // Cette méthode permet de recuillir la réponse Oui ou Non de l'utilisateur :

    public static boolean askYesOrNoChoice(){
        int choice;
        do {
            print(":: Oui ou Non ? : ");
            print("1 - Oui ? ");
            print("2 - Non ? ");
            System.out.print("* Votre choix : ");
            try{
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    print("\n !! Veuillez choisir l'une des propositions entre 1 et 2 !! \n");
                    sleep(1.2);
                }else {
                    scanner.nextLine();
                    return choice == 1;
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer l'une des valeurs numériques proposées ( ex : 1 pour Oui , et 2 pour Non ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }

        }while (true);
    }

    // Cette méthode permet de demander à l'admin qui crée un compte , s'il s'agit du compte d'un Administrateur , ou d'un Professionnel de Santé :

    public static boolean askRoleChoice(){
        int choice;
        do {
            print("\n :: S'agit - il du compte d'un Administrateur ou d'un Professionnel de Santé ? : ");
            print();
            print("1 - Compte Administrateur ? ");
            print("2 - Compte d'un Professionnel de Santé ? ");
            print();
            System.out.print("* Votre choix : ");
            try{
                choice = scanner.nextInt();
                if (choice < 1 || choice > 2) {
                    print("\n !! Veuillez choisir l'une des propositions entre 1 et 2 !! \n");
                    sleep(1.2);
                }else {
                    scanner.nextLine();
                    return choice == 1;
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer l'une des valeurs numériques proposées ( ex : 1 pour la création d'un compte Administrateur ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }
        }while (true);
    }

    // Cette méthode permet de rechercher un utilisateur dans le système :

    public static User searchUser(MediPassSystem sys) {
        String userName;
        Map<Integer, User> fetchList = new HashMap<>();

        do {
            System.out.print(":: Entrez le nom de famille de l'utilisateur que vous recherchez : ");
            userName = scanner.nextLine();
            if (userName.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un Nom !!");
                print();
                sleep(1.2);
            }
        } while (userName.isBlank());

        userName = userName.trim().toLowerCase();

        int index = 0;
        for (User user : sys.getUsersList().values()) {
            if (user.getLastName().trim().toLowerCase().startsWith(userName)) {
                index++;
                fetchList.put(index, user);
            }
        }
        if (fetchList.isEmpty()) {
            printOrange("-- Aucun utilisateur de ce nom n'a été trouvé .. --");
            sleep(1.2);
        } else {
            print("\n :: Liste des Utilisateurs trouvés :: \n");
            for (Map.Entry<Integer, User> indexedUser : fetchList.entrySet()) {
                print(indexedUser.getKey() + " - " + indexedUser.getValue().getLastName() + " " + indexedUser.getValue().getFirstNames() + " [ ID : " + indexedUser.getValue().getId() + " ]");
            }
            print();
            System.out.print("* Veuillez entrer le numéro de l'utilisateur que vous voulez sélectionner : ");
            print();
            int choice = askIndexInResults(index);
            return fetchList.get(choice);
        }

        return null;
    }

    // Cette méthode permettra de spécifier à tout admin qui crée un compte , de transmettre les identifiants de 1ère connexion au détenteur du compte :

    public static void printInfosCreatedAccount(User user){
        makeLargeDelimitation();
        printOrange("!! Important !!");
        print("* Veuillez transmettre les identifiants de < Première connexion > au propriétaire du compte , afin qu'il puisse se connecter au système !");
        sleep(1.2);
        makeSmallDelimitation();
        print(":: Identifiants de première connexion ::");
        print();
        print("* Login : " + Colors.YELLOW + user.getLogin() + Colors.BLOCK);
        print("* Mot de passe : " + Colors.YELLOW + "password" + user.getId() + Colors.BLOCK);
        makeSmallDelimitation();
        sleep(1.2);
        print("* Cela lui permettra de personnaliser ses identifiants , dès sa première connexion pour plus de sécurité .");
        makeLargeDelimitation();
        sleep(1.2);
    }

    // Cette méthode permet de demander la spécialité du professionnel de santé pour le compte créé par un admin du système :

    public static String askSpeciality(){
        int choice;
        do {
            print("\n :: Veuillez renseigner la spécialité du professionnel de santé :: \n");
            print("1 - Médecin ? ");
            print("2 - Infirmier ? ");
            print("3 - Pharmacien ? ");
            print("4 - Pédiatre ? ");
            print();
            System.out.print("* Votre choix : ");
            try{
                choice = scanner.nextInt();
                if (choice < 1 || choice > 4) {
                    print("\n !! Veuillez choisir l'une des propositions entre 1 et 4 !! \n");
                    sleep(2);
                }else {
                    scanner.nextLine();
                    switch (choice){
                        case 1: return "Médecin";
                        case 2: return  "Infirmier";
                        case 3: return "Pharmacien";
                        case 4: return "Pédiatre";
                    }
                }
            }catch (InputMismatchException e){
                print("\n !! Veuillez entrer l'une des valeurs numériques proposées ( ex : 1 (pour Médecin) ) !! \n");
                scanner.nextLine();
                sleep(1.2);
            }

        }while (true);
    }

    // Cette méthode permet de toujours charger le système à un état fonctionnel au début du programme , en tenant compte de l'existence d'une sauvegarde :

    public static MediPassSystem systemLoader(){
        File file = new File(saveFileName);
        if (file.exists()) {
            MediPassSystem sys = (MediPassSystem) Backup.load();
            if (sys != null) return sys;
        }

        print("\n --- Génération d'un système fonctionnel --- \n");
        sleep(1.2);
        print();
        printGreen(":: Système généré avec succès ::");
        sleep(1.2);

        return new MediPassSystem();
    }

    // Cette méthode permet à un utilisateur du système de se connecter :

    public static User logUser(MediPassSystem sys){
        if (sys.adminExists()){
            boolean isValid;
            printWelcome();
            sleep(1.2);
            print();
            while (true){
                print("\n :: Veuillez renseigner vos identifiants pour vous connecter au système :: \n");
                List<String> identifiers = askIdentifiers();
                String login = identifiers.get(0);
                String password = identifiers.get(1);

                isValid = sys.isUserInBase(login , password);

                if (isValid) {
                    User user = sys.getUser(login , password);
                    if (user != null){
                        print("\n ::: Bienvenue " + Colors.YELLOW + user.getLogin() + Colors.BLOCK + " ::: \n");
                        sleep(1.5);
                        if (user.getMustChangeIdentifiers()) firstLogin(user , sys);
                        return user;
                    }else {
                        printOrange("!! Un problème est subvenu ... L'utilisateur n'a pas pu être chargé depuis le système !!");
                        sleep(3);
                        return null;
                    }
                }else {
                    printOrange("!! Vos identifiants sont invalides ... Veuillez réessayer !!");
                    sleep(1.2);
                }
            }
        }else { return createFirstAdmin(sys); }
    }

    // Cette méthode permet à l'utilisateur qui établit sa première connexion , de modifier ses identifiants , afin qu'ils lui soient désormais personnalisés :

    public static void firstLogin(User user , MediPassSystem sys){
        boolean identifiersChanged = false;
        while (!identifiersChanged){
            print(":: Cher utilisateur , il s'agit de votre première connexion ..");
            sleep(1.2);
            print(":: Vous devez donc modifier vos identifiants afin de les personnaliser pour plus de sécurité .");
            sleep(1.2);
            print("\n -- Rensignez d'abord vos identifiants actuels ( " + Colors.YELLOW + "Ceux que vous venez d'utiliser pour vous connecter" + Colors.BLOCK + " ) --");
            sleep(1.2);
            identifiersChanged = user.changeIdentifiers(sys);
        }

        // Validation de la modification des identifiants :
        user.identifiersChangingDone();

        // Sauvegarde nécessaire pour que même après redémarrage du programme , les données soient sauvegardées :
        Backup.save(sys);
    }

    // Méthode permettant de créer le premier utilisateur ( Admin ) du système :

    public static Administrator createFirstAdmin(MediPassSystem sys){
        String lastName;
        String firstNames;
        String login;
        String password;

        if (sys.obtainUsersNumber() == 0){ printOrange("--- Ce système d'information médical ne possède encore aucun utilisateur ---"); }
        else { printOrange("--- Ce système d'information médical ne possède aucun admin ---"); }
        sleep(1.2);
        print("\n ::: Veuillez créer un compte en tant qu'Admin , afin d'administrer ce système . \n");
        sleep(1.2);

        print();
        print("\n -- Renseignez vos identifiants pour la création du compte -- \n");
        sleep(1.2);

        do {
            System.out.print("\n * Quel est votre nom ? : ");
            lastName = scanner.nextLine();
            if (lastName.isBlank()) {
                print();
                printOrange("!! Veuillez entrer votre Nom !!");
                print();
                sleep(1.2);
            }
        }while (lastName.isBlank());

        print();

        do {
            System.out.print("\n * Quels sont vos prénoms ? : ");
            firstNames = scanner.nextLine();
            if (firstNames.isBlank()) {
                print();
                printOrange("!! Veuillez entrer vos prénoms !!");
                print();
                sleep(1.2);
            }
        }while (firstNames.isBlank());

        print();

        do {
            System.out.print("\n * Entrez un login pour votre compte ( Un nom d'utilisateur ; indispensable pour la connexion au système ! ) : ");
            login = scanner.nextLine();
            if (login.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un login !!");
                print();
                sleep(1.2);
            }
        }while (login.isBlank());

        print();

        do {
            System.out.print("\n * Entrez un mot de passe pour votre compte ( Indispensable pour vous connecter au système ! Ne l'oubliez pas ! ) : ");
            password = scanner.nextLine();
            if (password.isBlank()) {
                print();
                printOrange("!! Veuillez entrer un mot de passe !!");
                print();
                sleep(1.2);
            } else if (password.length() < 8) {
                print();
                printOrange("!! Le mot de passe doit contenir au minimum 8 caractères !!");
                print();
                sleep(1.2);
            }
        }while (password.isBlank() || password.length() < 8);

        printGreen(":: Votre compte Admin a été créé avec succès ::");
        sleep(1.5);

        return new Administrator(sys.giveUserId() , lastName , firstNames , login.trim() , passwordHasher(password.trim()));

    }

    // Cette méthode gère la déconnexion d'un utilisateur :

    public static boolean logout() {
        printYellow(":: Déconnexion ...");
        sleep(1.2);
        return false;
    }

    // Cette méthode permet de générer le dossier d'exportation des .csv , s'il n'existe pas encore ; ainsi que le chemin du fichier .csv à exporter :

    public static String generateDirectoryAndFilePath(String exportsDirectory , String fileName){
        File directory = new File(exportsDirectory);
        if (!directory.exists()){
            boolean created = directory.mkdir();
            if (!created) Tools.print("\n !! Impossible de créer le répertoire de sauvegarde : " + exportsDirectory + " \n");
        }
        return exportsDirectory + "/" + fileName;
    }

    // Cette méthode permet de formater la date actuelle et la retourner au format jour/mois/année :

    public static String getCurrentDate(){ return LocalDate.now().format(dateFormat); }

}
