package Model;

public class Consultant {

    static String consultant;

    public Consultant(){ setConsultant(consultant); }

    public static String getConsultant() {
        return consultant;
    }
    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }
}
