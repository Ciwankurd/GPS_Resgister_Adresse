package s351927.oslomet.mappe3;

public class lagrehus {
    long husId;
    String beskrivelse;
    String adresse;
    double KooordinatX;
    double KooordinatY;
    int etasjer;

    public lagrehus(long husId, String beskrivelse, String adresse, double kooordinatX, double kooordinatY, int etasjer) {
        this.husId = husId;
        this.beskrivelse = beskrivelse;
        this.adresse = adresse;
        KooordinatX = kooordinatX;
        KooordinatY = kooordinatY;
        this.etasjer = etasjer;
    }

    public lagrehus() {
    }

    public long getHusId() {
        return husId;
    }

    public void setHusId(long husId) {
        this.husId = husId;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getKooordinatX() {
        return KooordinatX;
    }

    public void setKooordinatX(double kooordinatX) {
        KooordinatX = kooordinatX;
    }

    public double getKooordinatY() {
        return KooordinatY;
    }

    public void setKooordinatY(double kooordinatY) {
        KooordinatY = kooordinatY;
    }

    public int getEtasjer() {
        return etasjer;
    }

    public void setEtasjer(int etasjer) {
        this.etasjer = etasjer;
    }
}
