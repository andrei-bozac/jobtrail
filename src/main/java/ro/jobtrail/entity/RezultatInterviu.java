package ro.jobtrail.entity;

public enum RezultatInterviu {
    IN_ASTEPTARE("In asteptare","warning"),
    TRECUT("Trecut","success"),
    RESPINS("Respins","danger");

    private final String eticheta;
    private final String culoare;

    RezultatInterviu(String eticheta, String culoare){
        this.eticheta = eticheta;
        this.culoare = culoare;
    }
    public String getEticheta() {
        return eticheta;
    }

    public String getCuloare() {
        return culoare;
    }
}
