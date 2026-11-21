package ro.jobtrail.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.jobtrail.entity.Companie;
import ro.jobtrail.entity.DimensiuneCompanie;
import ro.jobtrail.service.AplicareService;
import ro.jobtrail.service.CompanieService;
import ro.jobtrail.service.ContactService;
import ro.jobtrail.service.ValidareException;

import java.io.IOException;

@WebServlet("/companii/*")
public class CompanieServlet extends HttpServlet {
    private static final String VIEWS = "/WEB-INF/views/companii/";

    @EJB
    private CompanieService companieService;
    @EJB
    private ContactService contactService;
    @EJB
    private AplicareService aplicareService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
            String actiune = req.getPathInfo();
            if(actiune == null || actiune.equals("/")){
                afiseazaLista(req,resp);
            } else if(actiune.equals("/nou")){
                afiseazaFormular(req,resp, new Companie());
            } else if(actiune.equals("/editare")){
                Companie companie = companieService.findById(citesteId(req.getParameter("id")));
                if(companie == null){
                    req.getSession().setAttribute("mesajEroare","Compania cautata nu exista");
                    resp.sendRedirect(req.getContextPath() + "/companii");
                    return;
                }
                afiseazaFormular(req, resp, companie);
            } else if(actiune.equals("/detalii")){
                afiseazaDetalii(req,resp);
            }
            else{
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String actiune = req.getPathInfo();

        if("/salveaza".equals(actiune)){
            salveaza(req, resp);
        } else if("/sterge".equals(actiune)){
            sterge(req,resp);
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void afiseazaLista(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException{
            req.setAttribute("companii", companieService.findAll());
            req.getRequestDispatcher(VIEWS+ "lista.jsp").forward(req,resp);
        }


     private void afiseazaDetalii(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
     {
        Companie companie = companieService.findById(citesteId(req.getParameter("id")));
        if(companie==null){
            req.getSession().setAttribute("mesajEroare","Compania cautata nu exista");
            resp.sendRedirect(req.getContextPath() + "/companii");
            return;
        }
        req.setAttribute("companie", companie);
        req.setAttribute("contacte",contactService.findByCompanie(companie.getId()));
        req.setAttribute("aplicari",aplicareService.findByCompanie(companie.getId()));
        req.getRequestDispatcher(VIEWS + "detalii.jsp").forward(req, resp);
     }

        private void afiseazaFormular(HttpServletRequest req, HttpServletResponse resp, Companie companie)
            throws ServletException, IOException {
        req.setAttribute("companie",companie);
        req.setAttribute("dimensiuni", DimensiuneCompanie.values());
        req.getRequestDispatcher(VIEWS + "formular.jsp").forward(req,resp);
        }

        private void salveaza(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
          Companie companie = new Companie();
          try{
              completeazaDinFormular(companie, req);

              if(companie.getId() == null){
                  companieService.create(companie);
              } else {
                  companieService.update(companie);
              }

              req.getSession().setAttribute("mesajSucces", "Compania a fost salvata");
              resp.sendRedirect(req.getContextPath() + "/companii");
          } catch(ValidareException e){
              req.setAttribute("eroare", e.getMessage());
              afiseazaFormular(req,resp,companie);
          }
        }

    private void sterge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            companieService.delete(citesteId(req.getParameter("id")));
            req.getSession().setAttribute("mesajSucces", "Compania a fost ștearsă.");
        } catch (ValidareException e) {
            req.getSession().setAttribute("mesajEroare", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/companii");
    }

        private void completeazaDinFormular(Companie c, HttpServletRequest req) {
        c.setId(citesteId(req.getParameter("id")));
        c.setNume(req.getParameter("nume"));
        c.setDomeniu(req.getParameter("domeniu"));
        c.setOras(req.getParameter("oras"));
        c.setWebsite(req.getParameter("website"));
        c.setNote(req.getParameter("note"));
        c.setDimensiune(citesteDimensiune(req.getParameter("dimensiune")));
        }

        private Long citesteId(String text){
        if(text == null || text.isBlank()){
            return null;
        }
        try{
            return Long.parseLong(text.trim());
        }catch(NumberFormatException e){
            return null;
        }
    }

    private DimensiuneCompanie citesteDimensiune(String text){
        if(text == null || text.isBlank()){
            return null;
        }
        try{
            return DimensiuneCompanie.valueOf(text);
        } catch(IllegalArgumentException e){
            throw new ValidareException("Dimensiunea selectata nu este valida.");
        }
    }


}
