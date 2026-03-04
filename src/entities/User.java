package entities;

import core.Backup;
import core.MediPassSystem;
import utilities.Colors;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;

    private final String lastName;

    private final String firstNames;

    private String login;

    private String password;

    // Cet attribut booléen permet au système de savoir s'il s'agit de la première
    // connexion d'un utilisateur au système ( lorsqu'il est à true ) , ou non ( lorsqu'il est à false ) :
    private boolean mustChangeIdentifiers;


    // Constructeur par défaut de le classe Utilisateur :

    public User(int id, String lastName , String firstNames) {
        this.id = id;
        this.lastName = lastName;
        this.firstNames = firstNames;
        this.login = "login" + id;
        this.password = Tools.passwordHasher("password" + id);
        this.mustChangeIdentifiers = true;
    }

    // Constructeur secondaire de le classe Utilisateur ( le champ password est directement injecté , parce qu'il est déjà hashé ) :

    public User(int id, String lastName , String firstNames , String login , String password) {
        this.id = id;
        this.lastName = lastName;
        this.firstNames = firstNames;
        this.login = login;
        this.password = password;
        this.mustChangeIdentifiers = false;
    }

    public int getId() { return id; }

    public String getLastName() { return lastName; }

    public String getFirstNames(){ return firstNames; }

    public String getLogin(){ return login; }

    public String getPassword() { return password; }

    public boolean getMustChangeIdentifiers() { return mustChangeIdentifiers; }

    public void identifiersChangingDone(){ this.mustChangeIdentifiers = false; }

    public boolean changeIdentifiers(MediPassSystem sys){

        List<String> identifiersEntered = Tools.askIdentifiers();

        String loginEntered = identifiersEntered.get(0);
        String passwordEntered = identifiersEntered.get(1);

        boolean isValid = sys.isUserInBase(loginEntered , passwordEntered);

        if (isValid){

            // Nous enlevons d'abord le login , pour permettre à l'utilisateur de le réutilisier , s'il le souhaite :

            sys.removeFromLoginList(loginEntered);

            List<String> newIdentifiers = Tools.askIdentifiersForChange(sys);

            this.login = newIdentifiers.get(0);

            this.password = Tools.passwordHasher(newIdentifiers.get(1));

            sys.addInLoginList(this.login);

            Backup.save(sys);

            Tools.printGreen("::: Vos identifiants ont été modifiés avec succès ! :::");
            Tools.sleep(1.5);

            return true;
        }else {
            Tools.printOrange("!! Vos identifiants sont incorrects !! ");
            Tools.sleep(1.5);
            return false;
        }
    }

    // Profil d'un utilisateur , overridé par ses classes filles ( Administrator et HealthProfessionnal ) :

    public void showProfile(){
        Tools.makeSmallDelimitation();
        Tools.print("* ID : " + Colors.YELLOW + this.getId() + Colors.BLOCK);
        Tools.print("* Nom : " + Colors.YELLOW + this.getLastName() + Colors.BLOCK);
        Tools.print("* Prénoms  : " + Colors.YELLOW + this.getFirstNames() + Colors.BLOCK);
        Tools.makeSmallDelimitation();
    }
}