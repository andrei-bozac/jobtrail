package ro.jobtrail.service;



import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.jobtrail.entity.Aplicare;
import ro.jobtrail.entity.Interviu;
import ro.jobtrail.entity.StatusAplicare;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class InterviuService {
    @PersistenceContext (unitName = "oraclePU")
    private EntityManager em;
    @EJB
    private AplicareService aplicareService;

    public List<Interviu> findAll() {
        return em.createQuery("SELECT i FROM Interviu i ORDER BY i.dataOra DESC", Interviu.class)
                .getResultList();
    }

    public List<Interviu> findByAplicare(Long aplicareId) {
        return em.createQuery(
                        "SELECT i FROM Interviu i WHERE i.aplicareId = :aplicareId ORDER BY i.dataOra",
                        Interviu.class)
                .setParameter("aplicareId", aplicareId)
                .getResultList();
    }

    public Interviu findById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(Interviu.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Interviu> findViitoare() {
        return em.createQuery(
                        "SELECT i FROM Interviu i WHERE i.dataOra >= :acum ORDER BY i.dataOra",
                        Interviu.class)
                .setParameter("acum", LocalDateTime.now())
                .getResultList();
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean existaPentruAplicare(Long aplicareId) {
        Long numar = em.createQuery(
                        "SELECT COUNT(i) FROM Interviu i WHERE i.aplicareId = :aplicareId", Long.class)
                .setParameter("aplicareId", aplicareId)
                .getSingleResult();
        return numar > 0;
    }

    public Interviu create(Interviu interviu){
        interviu.setId(null);
        Aplicare aplicare = curataSiValideaza(interviu);
        if(aplicare.getStatus() == StatusAplicare.RESPINS || aplicare.getStatus() == StatusAplicare.RETRAS){
            throw new ValidareException("Nu poti adauga un interviu la o aplicare respinsa sau retrasa");
        }
        em.persist(interviu);
        return interviu;
    }

    public Interviu update(Interviu interviu) {
        if (interviu.getId() == null || findById(interviu.getId()) == null) {
            throw new ValidareException("Interviul pe care încerci să îl modifici nu există.");
        }
        curataSiValideaza(interviu);
        return em.merge(interviu);
    }

    public void delete(Long id) {
        Interviu interviu = findById(id);
        if (interviu == null) {
            throw new ValidareException("Interviul pe care încerci să îl ștergi nu există.");
        }
        em.remove(interviu);
    }

    private Aplicare curataSiValideaza(Interviu i) {
        i.setIntervievator(curata(i.getIntervievator()));
        i.setIntrebari(curata(i.getIntrebari()));
        i.setImpresii(curata(i.getImpresii()));

        if (i.getTip() == null) {
            throw new ValidareException("Tipul interviului este obligatoriu.");
        }
        if (i.getRezultat() == null) {
            throw new ValidareException("Rezultatul interviului este obligatoriu.");
        }
        if (i.getDataOra() == null) {
            throw new ValidareException("Data și ora interviului sunt obligatorii.");
        }
        if (i.getIntrebari() != null && i.getIntrebari().length() > 2000) {
            throw new ValidareException("Întrebările pot avea cel mult 2000 de caractere.");
        }
        if (i.getImpresii() != null && i.getImpresii().length() > 2000) {
            throw new ValidareException("Impresiile pot avea cel mult 2000 de caractere.");
        }
        Aplicare aplicare = aplicareService.findById(i.getAplicareId());
        if (aplicare == null) {
            throw new ValidareException("Aplicarea selectată nu există.");
        }
        if (i.getDataOra().toLocalDate().isBefore(aplicare.getDataAplicarii())) {
            throw new ValidareException("Data interviului nu poate fi înaintea datei aplicării ("
                    + aplicare.getDataAplicarii() + ").");
        }
        return aplicare;
    }

    private String curata(String text){
        if(text == null || text.isBlank()){
            return null;
        }
        return text.trim();
    }

}
