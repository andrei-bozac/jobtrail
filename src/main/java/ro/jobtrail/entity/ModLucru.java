package ro.jobtrail.entity;

public enum ModLucru {
    REMOTE("Remote"),
    HIBRID("Hibrid"),
    BIROU("La birou");
    private final String eticheta;
    ModLucru(String eticheta){
        this.eticheta = eticheta;
    }
    public String getEticheta(){
        return eticheta;
    }
}
