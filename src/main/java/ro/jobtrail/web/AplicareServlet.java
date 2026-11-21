package ro.jobtrail.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.jobtrail.entity.*;
import ro.jobtrail.service.AplicareService;
import ro.jobtrail.service.CompanieService;
import ro.jobtrail.service.InterviuService;
import ro.jobtrail.service.ValidareException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/aplicari/*")

public class AplicareServlet extends HttpServlet {
    private static final String VIEWS = "/WEB-INF/views/aplicari/";
    @EJB
    private AplicareService aplicareService;

    @EJB
    private CompanieService companieService;

    @EJB
    private InterviuService interviuService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException{
            String actiune = req.getPathInfo();
            if(actiune==null || actiune.equals("/")){
                afiseazaLista(req,resp);
            } else if(actiune.equals("/nou")){
                Aplicare aplicare = new Aplicare();
                aplicare.setCompanieId(citesteId(req.getParameter("companieId")));
                aplicare.setStatus(StatusAplicare.APLICAT);
                aplicare.setDataAplicarii(LocalDate.now());
                afiseazaFormular(req,resp, aplicare);
            } else if(actiune.equals("/editare")){
                Aplicare aplicare = aplicareService.findById(citesteId(req.getParameter("id")));
                if(aplicare == null){
                    trimiteLaLista(req, resp, "Aplicarea cautata nu exista");
                    return;
                }
                afiseazaFormular(req,resp,aplicare);
            } else if(actiune.equals("/detalii")){
                afiseazaDetalii(req,resp);
            }

            else{
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException
    {
        String actiune = req.getPathInfo();
        if("/salveaza".equals(actiune)){
            salveaza(req,resp);
        } else if("/sterge".equals(actiune)){
            sterge(req,resp);
        } else{
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void afiseazaLista(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        StatusAplicare filtru = citesteStatus(req.getParameter("status"));
        req.setAttribute("aplicari",aplicareService.findByStatus(filtru));
        req.setAttribute("companii",companieService.findAllCaHarta());
        req.setAttribute("statusuri",StatusAplicare.values());
        req.setAttribute("filtruStatus",filtru);
        req.getRequestDispatcher(VIEWS + "lista.jsp").forward(req,resp);
    }

    private void afiseazaFormular(HttpServletRequest req, HttpServletResponse resp, Aplicare aplicare)
            throws ServletException, IOException {
        req.setAttribute("aplicare", aplicare);
        req.setAttribute("companii", companieService.findAll());
        req.setAttribute("statusuri", StatusAplicare.values());
        req.setAttribute("surse", SursaAplicare.values());
        req.setAttribute("tipuri", TipJob.values());
        req.setAttribute("moduriLucru", ModLucru.values());
        req.setAttribute("azi", LocalDate.now());
        req.getRequestDispatcher(VIEWS + "formular.jsp").forward(req, resp);
    }

    private void afiseazaDetalii(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Aplicare aplicare = aplicareService.findById(citesteId(req.getParameter("id")));
        if (aplicare == null) {
            trimiteLaLista(req, resp, "Aplicarea căutată nu există.");
            return;
        }
        req.setAttribute("aplicare", aplicare);
        req.setAttribute("companie", companieService.findById(aplicare.getCompanieId()));
        req.setAttribute("interviuri",interviuService.findByAplicare(aplicare.getId()));
        req.getRequestDispatcher(VIEWS + "detalii.jsp").forward(req, resp);
    }

    private void salveaza(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Aplicare aplicare = new Aplicare();
        try {
            completeazaDinFormular(aplicare, req);

            Aplicare salvata = (aplicare.getId() == null)
                    ? aplicareService.create(aplicare)
                    : aplicareService.update(aplicare);

            req.getSession().setAttribute("mesajSucces", "Aplicarea a fost salvată.");
            resp.sendRedirect(req.getContextPath() + "/aplicari/detalii?id=" + salvata.getId());

        } catch (ValidareException e) {
            req.setAttribute("eroare", e.getMessage());
            afiseazaFormular(req, resp, aplicare);
        }
    }

    private void sterge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = citesteId(req.getParameter("id"));
        try {
            aplicareService.delete(id);
            req.getSession().setAttribute("mesajSucces", "Aplicarea a fost ștearsă.");
            resp.sendRedirect(req.getContextPath() + "/aplicari");
        } catch (ValidareException e) {
            req.getSession().setAttribute("mesajEroare", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/aplicari/detalii?id=" + id);
        }
    }

    private void completeazaDinFormular(Aplicare a, HttpServletRequest req) {
        a.setId(citesteId(req.getParameter("id")));
        a.setCompanieId(citesteId(req.getParameter("companieId")));
        a.setPozitie(req.getParameter("pozitie"));
        a.setLinkAnunt(req.getParameter("linkAnunt"));
        a.setNote(req.getParameter("note"));

        a.setSursa(citesteEnum(SursaAplicare.class, req.getParameter("sursa"), "Sursa selectată nu este validă."));
        a.setTip(citesteEnum(TipJob.class, req.getParameter("tip"), "Tipul selectat nu este valid."));
        a.setModLucru(citesteEnum(ModLucru.class, req.getParameter("modLucru"), "Modul de lucru selectat nu este valid."));
        a.setStatus(citesteEnum(StatusAplicare.class, req.getParameter("status"), "Statusul selectat nu este valid."));

        a.setDataAplicarii(citesteData(req.getParameter("dataAplicarii")));
        a.setSalariu(citesteSuma(req.getParameter("salariu")));
    }

    private <E extends Enum<E>> E citesteEnum(Class<E> tipEnum, String text, String mesajEroare) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(tipEnum, text);
        } catch (IllegalArgumentException e) {
            throw new ValidareException(mesajEroare);
        }
    }

    private StatusAplicare citesteEnumFiltru(String text) {
        try {
            return citesteEnum(StatusAplicare.class, text, "");
        } catch (ValidareException e) {
            return null;
        }
    }

    private LocalDate citesteData(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim());
        } catch (DateTimeParseException e) {
            throw new ValidareException("Data aplicării nu este validă.");
        }
    }

    private BigDecimal citesteSuma(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(text.trim().replace(',', '.'));   // acceptăm și „3500,50”
        } catch (NumberFormatException e) {
            throw new ValidareException("Salariul trebuie să fie un număr.");
        }
    }

    private Long citesteId(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void trimiteLaLista(HttpServletRequest req, HttpServletResponse resp, String mesaj)
            throws IOException {
        req.getSession().setAttribute("mesajEroare", mesaj);
        resp.sendRedirect(req.getContextPath() + "/aplicari");
    }


    private StatusAplicare citesteStatus(String text){
            if (text == null || text.isBlank()){
                return null;
            }
            try{
                return StatusAplicare.valueOf(text);
            } catch(IllegalArgumentException e){
                return null;
            }
    }
}

