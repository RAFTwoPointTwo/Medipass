package entities;

import medical.MedicalRecord;
import utilities.Colors;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;

    // Classe Patient du système :

public class Patient implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int id ;

    private MedicalRecord medicalRecord;

    private final String lastName;

    private final String firstNames;

    private final String birthDate;

    private final String gender ;


    // Constructeur de le classe Patient :

    public Patient(int id , String lastName , String firstNames , String birthDate , String gender){
        this.id = id;
        this.lastName = lastName;
        this.firstNames = firstNames;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public int getId(){ return id ; }

    public MedicalRecord getMedicalRecord() { return medicalRecord; }

    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

    public String getLastName() { return lastName; }

    public String getFirstNames() { return firstNames; }

    public String getBirthDate() { return birthDate; }

    public String getGender() { return gender; }

    // Informations sur le patient :

    public void showInfos(){
        Tools.makeSmallDelimitation();
        Tools.print("* ID du patient : " + Colors.YELLOW + this.getId() + Colors.BLOCK);
        Tools.print("* Nom du patient : " + Colors.YELLOW + this.getLastName() + Colors.BLOCK);
        Tools.print("* Prénoms du patient : " + Colors.YELLOW + this.getFirstNames() + Colors.BLOCK);
        Tools.print("* Date de naissance du patient : " + Colors.YELLOW + this.getBirthDate() + Colors.BLOCK);
        Tools.print("* Sexe du patient : " + Colors.YELLOW + this.getGender() + Colors.BLOCK);
        Tools.makeSmallDelimitation();
    }

}
