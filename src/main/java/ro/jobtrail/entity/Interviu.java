package ro.jobtrail.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "interviu")
public class Interviu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "aplicare_id", nullable = false)
    private Long aplicareId;

    @Column(name = "data_ora", nullable = false)
    private LocalDateTime dataOra;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip", nullable = false, length = 20)
    private TipInterviu tip;

    @Column(name = "intervievator", length = 100)
    private String intervievator;

    @Column(name = "intrebari", length = 4000)
    private String intrebari;

    @Column(name = "impresii", length = 4000)
    private String impresii;

    @Enumerated(EnumType.STRING)
    @Column(name = "rezultat", nullable = false, length = 20)
    private RezultatInterviu rezultat;

    public Interviu(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RezultatInterviu getRezultat() {
        return rezultat;
    }

    public void setRezultat(RezultatInterviu rezultat) {
        this.rezultat = rezultat;
    }

    public String getImpresii() {
        return impresii;
    }

    public void setImpresii(String impresii) {
        this.impresii = impresii;
    }

    public String getIntrebari() {
        return intrebari;
    }

    public void setIntrebari(String intrebari) {
        this.intrebari = intrebari;
    }

    public String getIntervievator() {
        return intervievator;
    }

    public void setIntervievator(String intervievator) {
        this.intervievator = intervievator;
    }

    public Long getAplicareId() {
        return aplicareId;
    }

    public void setAplicareId(Long aplicareId) {
        this.aplicareId = aplicareId;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public TipInterviu getTip() {
        return tip;
    }

    public void setTip(TipInterviu tip) {
        this.tip = tip;
    }

    private static final DateTimeFormatter FORMAT_AFISARE = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    public String getDataOraText() {
        return (dataOra == null) ? "" : dataOra.format(FORMAT_AFISARE);
    }
    public boolean isViitor() {
        return dataOra != null && dataOra.isAfter(LocalDateTime.now());
    }
}
