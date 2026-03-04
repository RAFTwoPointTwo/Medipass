package medical;

import entities.Patient;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Patient patient;

    private final List<Consultation> consultations = new ArrayList<>();

    private final List<Antecedent> antecedents = new ArrayList<>();

    private boolean archived;

    private String archivingDate;

    // Constructeur de la classe DossierMedical :

    public MedicalRecord(Patient patient){
        this.patient = patient;
        this.archived = false;
    }

    public Patient getPatient() { return patient; }

    public String getArchivingDate() { return archivingDate; }

    public boolean isArchived() { return archived; }

    public void archive(){
        this.archived = true;
        this.archivingDate = Tools.getCurrentDate();
    }

    public void unarchive(){
        this.archived = false;
        this.archivingDate = "/";
    }

    public int getConsultationsNumber() { return consultations.size(); }

    public void addConsultation(Consultation consultation){ consultations.add(consultation); }

    public void addAntecedent(Antecedent antecedent){ antecedents.add(antecedent); }

    public void showConsultations(){
        if(consultations.isEmpty()){
            Tools.print("\n -- Aucune consultation n'a été faite pour ce patient -- \n");
        }else{
            int index = 1;
            for (Consultation consultation : consultations){
                Tools.print(":: Consultation no " + index + " :");
                consultation.show();
                index++;
            }
        }
    }

    public void showAntecedents(){
        if(antecedents.isEmpty()){
            Tools.print("\n -- Aucun antécédent n'a été retrouvé pour ce patient -- \n");
        }else{
            int index = 1;
            for (Antecedent antecedent : antecedents){
                Tools.print(":: Antécédent no " + index + " :");
                antecedent.show();
                index++;
            }
        }
    }

    public void show(){
        Tools.makeLargeDelimitation();
        Tools.print(":: Informations sur le patient :");
        Tools.print();
        Tools.print("* Nom du patient : " + this.getPatient().getLastName());
        Tools.print("* Prénoms du patient : " + this.getPatient().getFirstNames());
        Tools.print("* Date de naissance du patient : " + this.patient.getBirthDate());
        Tools.print();
        Tools.print(":: Liste des antécédents du patient :");
        Tools.print();
        this.showAntecedents();
        Tools.print();
        Tools.print(":: Liste des consultations passées par le patient : ");
        Tools.print();
        this.showConsultations();
        Tools.makeLargeDelimitation();
    }

}
