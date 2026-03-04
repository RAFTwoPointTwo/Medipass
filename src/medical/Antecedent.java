package medical;

import utilities.Colors;
import utilities.Tools;

import java.io.Serial;
import java.io.Serializable;

public class Antecedent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String nature;
    private final String description;


    // Constructeur de la classe Antecedent :

    public Antecedent(String nature, String description) {
        this.nature = nature;
        this.description = description;
    }

    public String getNature() { return nature; }

    public String getDescription() { return description; }

    public void show(){
        Tools.makeSmallDelimitation();
        Tools.print(Colors.YELLOW + "* Nature : " + Colors.BLOCK + this.getNature());
        Tools.print(Colors.YELLOW + "* Description : " + Colors.BLOCK + this.getDescription());
        Tools.makeSmallDelimitation();
    }

}
