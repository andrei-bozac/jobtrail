package ro.jobtrail.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name="aplicare")
public class Aplicare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="companie_id",nullable = false)
    private Long companieId;

    @Column(name = "pozitie", nullable=false, length = 150)
    private String pozitie;

    @Column(name="link_anunt", length = 500)
    private String linkAnunt;

    @Enumerated(EnumType.STRING)
    @Column(name = "sursa", nullable = false, columnDefinition = "nvarchar(20)")
    private SursaAplicare sursa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip", nullable = false, columnDefinition = "nvarchar(20)")
    private TipJob tip;

    @Enumerated(EnumType.STRING)
    @Column(name = "mod_lucru", columnDefinition = "nvarchar(20)")
    private ModLucru modLucru;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "nvarchar(20)")
    private StatusAplicare status;

    @Column(name = "salariu", precision = 10, scale = 2)
    private BigDecimal salariu;

    @Column(name = "data_aplicarii", nullable = false)
    private LocalDate dataAplicarii;



    @Column(name = "note", columnDefinition = "nvarchar(max)")
    private String note;

    public Aplicare(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StatusAplicare getStatus() {
        return status;
    }

    public void setStatus(StatusAplicare status) {
        this.status = status;
    }

    public LocalDate getDataAplicarii() {
        return dataAplicarii;
    }

    public void setDataAplicarii(LocalDate dataAplicarii) {
        this.dataAplicarii = dataAplicarii;
    }

    public BigDecimal getSalariu() {
        return salariu;
    }

    public void setSalariu(BigDecimal salariu) {
        this.salariu = salariu;
    }

    public ModLucru getModLucru() {
        return modLucru;
    }

    public void setModLucru(ModLucru modLucru) {
        this.modLucru = modLucru;
    }

    public String getLinkAnunt() {
        return linkAnunt;
    }

    public void setLinkAnunt(String linkAnunt) {
        this.linkAnunt = linkAnunt;
    }

    public String getPozitie() {
        return pozitie;
    }

    public void setPozitie(String pozitie) {
        this.pozitie = pozitie;
    }

    public Long getCompanieId() {
        return companieId;
    }

    public void setCompanieId(Long companieId) {
        this.companieId = companieId;
    }

    public TipJob getTip() {
        return tip;
    }

    public void setTip(TipJob tip) {
        this.tip = tip;
    }

    public SursaAplicare getSursa() {
        return sursa;
    }

    public void setSursa(SursaAplicare sursa) {
        this.sursa = sursa;
    }
}
