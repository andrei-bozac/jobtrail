package ro.jobtrail.entity;

public enum TipInterviu {
    HR("HR"),
    TEHNIC("Tehnic"),
    FINAL("Final");

    private final String eticheta;
    TipInterviu(String eticheta){
        this.eticheta = eticheta;
    }

    public String getEticheta(){
        return eticheta;
    }

}
