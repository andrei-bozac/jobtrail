package ro.jobtrail.entity;

public enum SursaAplicare {
    LINKEDIN("LinkedIn"),
    EJOBS("eJobs"),
    BESTJOBS("BestJobs"),
    RECOMANDARE("Recomandare"),
    SITE_COMPANIE("Site-ul companiei"),
    ALTA("Alta sursa");

    private final String eticheta;

    SursaAplicare(String eticheta){
        this.eticheta = eticheta;
    }
    public String getEticheta(){
        return eticheta;
    }
}
