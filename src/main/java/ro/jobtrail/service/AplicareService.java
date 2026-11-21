package ro.jobtrail.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.jobtrail.entity.Aplicare;
import ro.jobtrail.entity.StatusAplicare;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class AplicareService {
    @PersistenceContext(unitName = "mssqlPU")
    private EntityManager em;
    @EJB
    private CompanieService companieService;
    @EJB
    private InterviuService interviuService;
    public List<Aplicare> findAll() {
        return em.createQuery("SELECT a FROM Aplicare a ORDER BY a.dataAplicarii DESC", Aplicare.class)
                .getResultList();
    }

    public List<Aplicare> findByStatus(StatusAplicare status){
        if(status == null){
            return findAll();
        }
        return em.createQuery(
                "SELECT a FROM Aplicare a WHERE a.status = :status ORDER BY a.dataAplicarii DESC",
                Aplicare.class)
                .setParameter("status",status)
                .getResultList();

    }

    public List<Aplicare> findByCompanie(Long companieId){
        return em.createQuery(
                "SELECT a FROM Aplicare a WHERE a.companieId = :companieId ORDER BY a.dataAplicarii DESC",
                Aplicare.class)
                .setParameter("companieId",companieId)
                .getResultList();

    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Aplicare findById(Long id){
        if (id == null){
            return null;
        }
        return em.find(Aplicare.class, id);
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean exists(Long id){
        return findById(id) != null;
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean existaPentruCompanie(Long companieId){
        Long numar = em.createQuery(
                "SELECT COUNT(a) FROM Aplicare a WHERE a.companieId = :companieId",
                Long.class).setParameter("companieId",companieId).getSingleResult();
        return numar>0;
    }

    public Aplicare create(Aplicare aplicare){
        aplicare.setId(null);
        curataSiValideaza(aplicare);
        em.persist(aplicare);
        return aplicare;
    }

    public Aplicare update(Aplicare aplicare){
        if (aplicare.getId() == null || findById(aplicare.getId()) == null){
            throw new ValidareException("Aplicare pe care incerci sa o modifici nu exista");
        }
        curataSiValideaza(aplicare);
        return em.merge(aplicare);
    }

    public void schimbaStatus(Long id, StatusAplicare statusNou){
        Aplicare aplicare = findById(id);
        if(aplicare==null){
            throw new ValidareException("Aplicarea nu exista");
        }
        if(statusNou==null){
            throw new ValidareException("Statusul nou este obligatoriu");
        }
        aplicare.setStatus(statusNou);
    }

    public void delete(Long id){
        Aplicare aplicare = findById(id);
        if(aplicare==null){
            throw new ValidareException("Aplicarea pe care incerci sa o stergi nu exista");
        }
        if(interviuService.existaPentruAplicare(id)){
            throw new ValidareException("Aplicarea nu poate fi stearsa deoarece are interviuri." + "Sterge mai intai interviurile asociate acestei aplicari");
        }
        em.remove(aplicare);
    }

    private void curataSiValideaza(Aplicare a){
        a.setPozitie(curata(a.getPozitie()));
        a.setLinkAnunt(curata(a.getLinkAnunt()));
        a.setNote(curata(a.getNote()));

        if(a.getPozitie() == null){
            throw new ValidareException("Pozitia este obligatorie");
        }
        if (a.getPozitie().length() >150){
            throw new ValidareException("Pozitia poate avea cel mult 150 de caractere");
        }
        if(a.getSursa() == null){
            throw new ValidareException("Sursa aplicarii este obligatorie");
        }
        if(a.getTip() == null){
            throw new ValidareException("Tipul jobului este obligatoriu");
        }
        if (a.getStatus() == null) {
            throw new ValidareException("Statusul este obligatoriu.");
        }
        if (a.getDataAplicarii() == null) {
            throw new ValidareException("Data aplicării este obligatorie.");
        }
        if (a.getDataAplicarii().isAfter(LocalDate.now())) {
            throw new ValidareException("Data aplicării nu poate fi în viitor.");
        }
        if (a.getSalariu() != null && a.getSalariu().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidareException("Salariul nu poate fi negativ.");
        }
        if(a.getCompanieId()==null || !companieService.exists(a.getCompanieId())){
            throw new ValidareException("Compania selectata nu exista");
        }
    }

    private String curata(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return text.trim();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, Aplicare> findAllCaHarta() {
        Map<Long, Aplicare> harta = new HashMap<>();
        for (Aplicare a : findAll()) {
            harta.put(a.getId(), a);
        }
        return harta;
    }

}
