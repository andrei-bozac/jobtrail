package ro.jobtrail.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ro.jobtrail.entity.Companie;
import ro.jobtrail.entity.Contact;
import java.util.List;

@Stateless
public class ContactService {
        @PersistenceContext(unitName = "pgPU")
        private EntityManager em;
        public List<Contact> findByCompanie(Long companieId){
            return em.createQuery(
                    "SELECT ct FROM Contact ct WHERE ct.companie.id = :companieId ORDER BY ct.nume",
                    Contact.class).setParameter("companieId",companieId).getResultList();


        }

        public Contact findById(Long id){
            if(id==null) {
                return null;
            }
            return em.find(Contact.class,id);
        }

        public Contact create(Contact contact, Long companieId){
            contact.setId(null);
            contact.setCompanie(gasesteCompania(companieId));
            curataSiValideaza(contact);
            em.persist(contact);
            return contact;
        }

        public Contact update(Contact contact, Long companieId){
            if(contact.getId()==null || findById(contact.getId())==null){
                throw new ValidareException("Contractul pe care incerci sa il modific nu exista");
            }
            contact.setCompanie(gasesteCompania(companieId));
            curataSiValideaza(contact);
            return em.merge(contact);
        }

        public void delete(Long id){
            Contact contact = findById(id);
            if(contact == null){
                throw new ValidareException("Contactul pe care incerci sa il stergi nu exista");
            }
            em.remove(contact);
        }

        private Companie gasesteCompania(Long companieId) {
            Companie companie = (companieId==null) ? null: em.find(Companie.class, companieId);
            if(companie == null){
                throw new ValidareException("Contactul trebuie sa apartina unei companii existente");
            }
            return companie;
        }

        private void curataSiValideaza(Contact ct){
            ct.setNume(curata(ct.getNume()));
            ct.setFunctie(curata(ct.getFunctie()));
            ct.setEmail(curata(ct.getEmail()));
            ct.setTelefon(curata(ct.getTelefon()));
            ct.setLinkedin(curata(ct.getLinkedin()));

            if(ct.getNume() == null){
                throw new ValidareException("Numele contactului este onligatoriu");
            }
            if(ct.getEmail() != null && !ct.getEmail().contains("@")){
                throw new ValidareException("Adresa de email nu este valida");
            }

        }

        private String curata(String text){
            if (text == null || text.isBlank()){
                return null;
            }
            return text.trim();
        }
}
