package ro.jobtrail.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.jobtrail.entity.Companie;
import ro.jobtrail.entity.Contact;
import ro.jobtrail.service.CompanieService;
import ro.jobtrail.service.ContactService;
import ro.jobtrail.service.ValidareException;

import java.io.IOException;

@WebServlet("/contacte/*")
public class ContactServlet extends HttpServlet {
    private static final String VIEWS = "/WEB-INF/views/contacte/";
    @EJB
    private ContactService contactService;
    @EJB
    private CompanieService companieService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
        String actiune  = req.getPathInfo();
        if("/nou".equals(actiune)){
            Companie companie = companieService.findById(citesteId(req.getParameter("companieId")));
            if(companie==null){
                trimiteLaCompanii(req,resp,"Compania pentru care vrei sa adaugi contactul nu exista!");
                return;
            }
            afiseazaFormular(req,resp, new Contact(),companie);
        } else if("/editare".equals(actiune)){
            Contact contact = contactService.findById(citesteId(req.getParameter("id")));
            if(contact == null){
                trimiteLaCompanii(req,resp,"Contactul pe care incerci sa il modifici nu exista!");
                return;
            }
            afiseazaFormular(req,resp,contact,contact.getCompanie());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
        String actiune = req.getPathInfo();
        if("/salveaza".equals(actiune)){
            salveaza(req,resp);
        } else if ("/sterge".equals(actiune)) {
            sterge(req,resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void afiseazaFormular(HttpServletRequest req, HttpServletResponse resp,
                                  Contact contact, Companie companie)
            throws ServletException, IOException {
        req.setAttribute("contact", contact);
        req.setAttribute("companie", companie);
        req.getRequestDispatcher(VIEWS + "formular.jsp").forward(req, resp);
    }

    private void salveaza(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
        Long companieId = citesteId(req.getParameter("companieId"));

        Contact contact = new Contact();
        contact.setId(citesteId(req.getParameter("id")));
        contact.setNume(req.getParameter("nume"));
        contact.setFunctie(req.getParameter("functie"));
        contact.setEmail(req.getParameter("email"));
        contact.setTelefon(req.getParameter("telefon"));
        contact.setLinkedin(req.getParameter("linkedin"));

        try {
            if (contact.getId() == null) {
                contactService.create(contact, companieId);
            } else {
                contactService.update(contact, companieId);
            }
            req.getSession().setAttribute("mesajSucces", "Contactul a fost salvat.");
            resp.sendRedirect(req.getContextPath() + "/companii/detalii?id=" + companieId);

        } catch (ValidareException e) {
            Companie companie = companieService.findById(companieId);
            if (companie == null) {
                trimiteLaCompanii(req, resp, e.getMessage());
                return;
            }
            req.setAttribute("eroare", e.getMessage());
            afiseazaFormular(req, resp, contact, companie);
        }
    }

    private void sterge(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Contact contact = contactService.findById(citesteId(req.getParameter("id")));
        if(contact==null){
            trimiteLaCompanii(req,resp,"Contactul pe care incerci sa il stergi nu exista");
            return;
        }

        Long companieId = contact.getCompanie().getId();

        try{
            contactService.delete(contact.getId());
            req.getSession().setAttribute("mesajSucces","Contactul a fost sters");
        }catch(ValidareException e){
            req.getSession().setAttribute("mesajEroare", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/companii/detalii?id=" + companieId);
    }

    private void trimiteLaCompanii(HttpServletRequest req, HttpServletResponse resp, String mesaj) throws IOException{
            req.getSession().setAttribute("mesajEroare",mesaj);
            resp.sendRedirect(req.getContextPath() + "/companii");
    }

    private Long citesteId(String text){
        if(text==null || text.isBlank()){
            return null;
        }
        try{
            return Long.parseLong(text.trim());
        } catch(NumberFormatException e){
            return null;
        }
    }
}