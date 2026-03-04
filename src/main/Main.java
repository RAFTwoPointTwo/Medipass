package main;

import core.Backup;
import core.MediPassSystem;
import entities.Administrator;
import entities.HealthProfessional;
import entities.User;
import utilities.CsvExport;
import utilities.Tools;

public class Main {
    public static void main(String[] args) {

        // Ceci permet de charger la dernière sauvegarde du système , ou de générer un nouveau système fonctionnel :
        MediPassSystem mediPassSystem = Tools.systemLoader();

        // Cette ligne de code s'occupe de la connexion d'un utilisateur au système :
        User user = Tools.logUser(mediPassSystem);

        // Pour s'assurer que l'utilisateur est bien chargé :
        if (user == null){
            Tools.printOrange("!! Une erreur s'est produite ... !!");
            return;
        }

        Administrator administrator = null;
        HealthProfessional healthProfessional = null;

        if (user instanceof Administrator admin){
            // Ce block de codes permet de sauvegarder le premier admin qui crée son compte dans le système , et de lui afficher un message de bienvenue :
            if (!mediPassSystem.isLoginInBase(admin.getLogin())){
                mediPassSystem.addUser(admin);
                Tools.print();
                Tools.printWelcome();
                Tools.sleep(1);
            }

            administrator = admin;

        }else { healthProfessional = (HealthProfessional) user; }

        boolean systemIsActive = true;

        while (systemIsActive){
            if (administrator != null){
                int choice = Tools.askChoiceToAdmin();
                switch (choice){
                    case 1:
                        administrator.createAccount(mediPassSystem);
                        break;
                    case 2:
                        administrator.removeRecordAccess(mediPassSystem);
                        break;
                    case 3:
                        administrator.giveRecordAccess(mediPassSystem);
                        break;
                    case 4:
                        Tools.print();
                        Tools.print("::::: Profil :::::");
                        administrator.showProfile();
                        Tools.sleep(1);
                        break;
                    case 5:
                        mediPassSystem.showAccountsList();
                        Tools.sleep(1);
                        break;
                    case 6:
                        Tools.print();
                        mediPassSystem.showStatistics();
                        break;
                    case 7:
                        administrator.changeIdentifiers(mediPassSystem);
                        break;
                    case 8:
                        CsvExport.exportAdmins(mediPassSystem);
                        break;
                    case 9:
                        CsvExport.exportHealthPros(mediPassSystem);
                        break;
                    case 10:
                        systemIsActive = Tools.logout();
                        break;
                }
            }else {
                int choice = Tools.askChoiceToHealthPro();
                switch (choice){
                    case 1:
                        healthProfessional.registerPatient(mediPassSystem);
                        break;
                    case 2:
                        healthProfessional.programConsultation(mediPassSystem);
                        break;
                    case 3:
                        healthProfessional.consultPatient(mediPassSystem);
                        break;
                    case 4:
                        healthProfessional.consultMedicalRecord(mediPassSystem);
                        break;
                    case 5:
                        healthProfessional.consultArchivedRecord(mediPassSystem);
                        break;
                    case 6:
                        healthProfessional.archiveMedicalRecord(mediPassSystem);
                        break;
                    case 7:
                        healthProfessional.unarchiveMedicalRecord(mediPassSystem);
                        break;
                    case 8:
                        mediPassSystem.showArchivedMedicalRecords();
                        break;
                    case 9:
                        mediPassSystem.showPatientsToConsult(healthProfessional);
                        break;
                    case 10:
                        Tools.print();
                        Tools.print("::::: Profil :::::");
                        healthProfessional.showProfile();
                        Tools.sleep(1);
                        break;
                    case 11:
                        healthProfessional.changeIdentifiers(mediPassSystem);
                        break;
                    case 12:
                        CsvExport.exportActivePatients(mediPassSystem);
                        break;
                    case 13:
                        CsvExport.exportArchivedPatients(mediPassSystem);
                        break;
                    case 14:
                        systemIsActive = Tools.logout();
                        break;
                }
            }
        }

        Tools.print("\n ::: Vous vous êtes déconnecté ::: \n");
        Tools.sleep(0.8);


        // Sauvegarde du système après déconnexion de l'utilisateur :
        Backup.save(mediPassSystem);

        Tools.printYellow("-- Passez une bonne journée --");
        Tools.sleep(1);

    }
}
