package ro.jobtrail.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "companie")
public class Companie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nume", nullable = false, length = 100)
    private String nume;
    @Column(name="domeniu", length=100)
    private String domeniu;
    @Column(name = "oras", length = 100)
    private String oras;
    @Column(name = "website", length=255)
    private String website;
    @Enumerated(EnumType.STRING)
    @Column(name = "dimensiune", length=20)
    private DimensiuneCompanie dimensiune;
    @Column(name = "note")
    private String note;
    public Companie() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public String getNume(){return nume;}
    public void setNume(String nume) {this.nume = nume;}

    public String getDomeniu() {return domeniu;}
    public void setDomeniu(String domeniu) {this.domeniu = domeniu;}

    public String getOras() {return oras;}
    public void setOras(String oras){this.oras = oras;}

    public String getWebsite(){return website;}
    public void setWebsite(String website) {this.website = website;}

    public DimensiuneCompanie getDimensiune(){return dimensiune;}
    public void setDimensiune(DimensiuneCompanie dimensiune) {this.dimensiune = dimensiune;}

    public String getNote(){return note;}
    public void setNote(String note){this.note = note;}



}
