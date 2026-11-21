package ro.jobtrail.entity;

public enum TipJob {
    INTERNSHIP("Internship"),
    PART_TIME("Part-time"),
    FULL_TIME("Full-Time");

    private final String eticheta;
    TipJob(String eticheta){
        this.eticheta = eticheta;
    }
    public String getEticheta(){
        return eticheta;
    }
}
