package ro.jobtrail.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.jobtrail.entity.Companie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Stateless
public class CompanieService {
    @PersistenceContext(unitName = "pgPU")
    private EntityManager em;
    @EJB
    private AplicareService aplicareService;
    public List <Companie> findAll() {
        return em.createQuery("SELECT c FROM Companie c ORDER BY c.nume", Companie.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<Long, Companie> findAllCaHarta() {
        Map<Long, Companie> harta = new HashMap<>();
        for (Companie c : findAll()) {
            harta.put(c.getId(), c);
        }
        return harta;
    }

    public Companie findById(Long id){
        if(id == null){
            return null;
        }
        return em.find(Companie.class, id);
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

    public boolean exists(Long id){
        return findById(id)!=null;
    }

    public Companie create(Companie companie){
        companie.setId(null);
        curataSiValideaza(companie);
        em.persist(companie);
        return companie;
    }

    public Companie update(Companie companie){
        if(companie.getId()==null || !exists(companie.getId())){
            throw new ValidareException("Compania pe care incerci sa o modific nu exista!");
        }
        curataSiValideaza(companie);
        return em.merge(companie);
    }

    public void delete(Long id){
        Companie companie = findById(id);
        if(companie==null) {
            throw new ValidareException("Compania pe care incerci sa o stergi nu exista");

        }
        if(aplicareService.existaPentruCompanie(id)){
            throw new ValidareException("Compania nu poate fi stearsa daca are aplicari inregistrate." + "Sterge mai intai aplicarile ei");
        }

        em.remove(companie);
    }

    private void curataSiValideaza(Companie c){
        c.setNume(curata(c.getNume()));
        c.setDomeniu(curata(c.getDomeniu()));
        c.setOras(curata(c.getOras()));
        c.setWebsite(curata(c.getWebsite()));
        c.setNote(curata(c.getNote()));

        if(c.getNume() == null){
            throw new ValidareException("Numele companiei este obligatoriu");
        }
        if(c.getNume().length()>100){
            throw new ValidareException("Numele companiei poate contine maxim 100 de carctere");
        }

    }
    private String curata(String text){
        if (text == null || text.isBlank()){
            return null;
        }
        return text.trim();
    }


}
