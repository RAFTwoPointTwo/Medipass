package medical;

import entities.HealthProfessional;
import entities.Patient;
import utilities.Colors;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;

public class Consultation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Patient patient;

    private final HealthProfessional healthPro;

    private final String consultationDate;

    private final String consultationReason;

    private String observations;


    public Consultation (Patient patient , HealthProfessional healthPro , String consultationDate , String consultationReason) {
        this.patient = patient;
        this.healthPro = healthPro;
        this.consultationDate = consultationDate;
        this.consultationReason = consultationReason;
    }

    public HealthProfessional getHealthPro() { return healthPro; }

    public Patient getPatient() { return patient; }

    public String getConsultationDate() { return consultationDate; }

    public String getConsultationReason() { return consultationReason; }

    public String getObservations() { return observations; }

    public void setObservations(String observations) { this.observations = observations; }

    public void show(){
        String observations = (this.getObservations().isEmpty()) ? "/" : this.getObservations();
        Tools.makeSmallDelimitation();
        Tools.print(Colors.YELLOW + "* Professionnel de Santé : " + Colors.BLOCK + this.getHealthPro().getLastName() + " " + this.getHealthPro().getFirstNames() + " [ " + this.getHealthPro().getSpeciality() + " ] ");
        Tools.print(Colors.YELLOW + "* Patient : " + Colors.BLOCK + this.getPatient().getLastName() + " " + this.getPatient().getFirstNames());
        Tools.print(Colors.YELLOW + "* Date de la consultation : " + Colors.BLOCK + this.getConsultationDate());
        Tools.print(Colors.YELLOW + "* Motif de la consultation : " + Colors.BLOCK  + this.getConsultationReason());
        Tools.print(Colors.YELLOW + "* Observations : " + Colors.BLOCK  + observations);
        Tools.makeSmallDelimitation();
    }
}