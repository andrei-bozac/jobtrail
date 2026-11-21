package ro.jobtrail.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.jobtrail.entity.Aplicare;
import ro.jobtrail.entity.Interviu;
import ro.jobtrail.entity.RezultatInterviu;
import ro.jobtrail.entity.TipInterviu;
import ro.jobtrail.service.AplicareService;
import ro.jobtrail.service.CompanieService;
import ro.jobtrail.service.InterviuService;
import ro.jobtrail.service.ValidareException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@WebServlet("/interviuri/*")
public class InterviuServlet extends HttpServlet {
    private static final String VIEWS = "/WEB-INF/views/interviuri/";

    @EJB
    private InterviuService interviuService;
    @EJB
    private AplicareService aplicareService;
    @EJB
    private CompanieService companieService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String actiune = req.getPathInfo();
        if(actiune==null || actiune.equals("/")){
            afiseazaLista(req,resp);
        } else if (actiune.equals("/nou")) {
            Interviu interviu = new Interviu();
            interviu.setAplicareId(citesteId(req.getParameter("aplicareId")));
            interviu.setTip(TipInterviu.HR);
            interviu.setRezultat(RezultatInterviu.IN_ASTEPTARE);
            afiseazaFormular(req, resp, interviu);
        } else if(actiune.equals("/editare")){
            Interviu interviu = interviuService.findById(citesteId(req.getParameter("id")));
            if(interviu==null){
                trimiteLaLista(req,resp,"Interviul cautat nu exista");
                return;
            }
            afiseazaFormular(req,resp,interviu);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String actiune = req.getPathInfo();
        if("/salveaza".equals(actiune)){
            salveaza(req,resp);
        } else if("/sterge".equals(actiune)){
            sterge(req,resp);
        } else{
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    public void afiseazaLista(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        req.setAttribute("viitoare",interviuService.findViitoare());
        req.setAttribute("interviuri", interviuService.findAll());
        req.setAttribute("aplicari",aplicareService.findAllCaHarta());
        req.setAttribute("companii",companieService.findAllCaHarta());
        req.getRequestDispatcher(VIEWS + "lista.jsp").forward(req,resp);
    }

    private void afiseazaFormular(HttpServletRequest req, HttpServletResponse resp, Interviu interviu)
            throws ServletException, IOException {
        Aplicare aplicare = aplicareService.findById(interviu.getAplicareId());
        if (aplicare == null) {
            trimiteLaLista(req, resp, "Aplicarea pentru acest interviu nu există.");
            return;
        }
        req.setAttribute("interviu", interviu);
        req.setAttribute("aplicare", aplicare);
        req.setAttribute("companie", companieService.findById(aplicare.getCompanieId()));
        req.setAttribute("tipuri", TipInterviu.values());
        req.setAttribute("rezultate", RezultatInterviu.values());
        req.getRequestDispatcher(VIEWS + "formular.jsp").forward(req, resp);
    }
    private void salveaza(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Interviu interviu = new Interviu();
        try {
            interviu.setId(citesteId(req.getParameter("id")));
            interviu.setAplicareId(citesteId(req.getParameter("aplicareId")));
            interviu.setIntervievator(req.getParameter("intervievator"));
            interviu.setIntrebari(req.getParameter("intrebari"));
            interviu.setImpresii(req.getParameter("impresii"));
            interviu.setTip(citesteEnum(TipInterviu.class, req.getParameter("tip"), "Tipul selectat nu este valid."));
            interviu.setRezultat(citesteEnum(RezultatInterviu.class, req.getParameter("rezultat"), "Rezultatul selectat nu este valid."));
            interviu.setDataOra(citesteDataOra(req.getParameter("dataOra")));

            if (interviu.getId() == null) {
                interviuService.create(interviu);
            } else {
                interviuService.update(interviu);
            }
            req.getSession().setAttribute("mesajSucces", "Interviul a fost salvat.");
            resp.sendRedirect(req.getContextPath() + "/aplicari/detalii?id=" + interviu.getAplicareId());

        } catch (ValidareException e) {
            req.setAttribute("eroare", e.getMessage());
            afiseazaFormular(req, resp, interviu);
        }
    }

    private void sterge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Interviu interviu = interviuService.findById(citesteId(req.getParameter("id")));
        if (interviu == null) {
            trimiteLaLista(req, resp, "Interviul pe care vrei să îl ștergi nu există.");
            return;
        }
        Long aplicareId = interviu.getAplicareId();
        try {
            interviuService.delete(interviu.getId());
            req.getSession().setAttribute("mesajSucces", "Interviul a fost șters.");
        } catch (ValidareException e) {
            req.getSession().setAttribute("mesajEroare", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/aplicari/detalii?id=" + aplicareId);
    }

    private LocalDateTime citesteDataOra(String text){
        if(text == null || text.isBlank()){
            return null;
        }
        try{
            return LocalDateTime.parse(text.trim());
        } catch(DateTimeParseException e){
            throw new ValidareException("Data si ora interviului nu sunt valide.");
        }
    }

    private <E extends Enum <E>> E citesteEnum(Class<E> tipEnum, String text, String mesajEroare){
        if (text == null || text.isBlank()){
            return null;
        }
        try {
            return Enum.valueOf(tipEnum, text);
        }catch (IllegalArgumentException e){
            throw new ValidareException(mesajEroare);
        }
    }

    private Long citesteId(String text){
        if(text==null || text.isBlank()){
            return null;
        }
        try {
            return Long.parseLong(text.trim());
        } catch(NumberFormatException e){
            return null;
        }
    }

    private void trimiteLaLista(HttpServletRequest req, HttpServletResponse resp, String mesaj)
        throws IOException{
        req.getSession().setAttribute("mesajEroare",mesaj);
        resp.sendRedirect(req.getContextPath() + "/interviuri");
    }



}
