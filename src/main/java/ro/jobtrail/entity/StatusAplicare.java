package ro.jobtrail.entity;

public enum StatusAplicare {
    SALVAT("Salvat", "secondary"),
    APLICAT("Aplicat", "primary"),
    INTERVIU("Interviu", "warning"),
    OFERTA("Ofertă", "success"),
    RESPINS("Respins", "danger"),
    RETRAS("Retras", "dark");

    private final String eticheta;
    private final String culoare;

    StatusAplicare(String eticheta, String culoare) {
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