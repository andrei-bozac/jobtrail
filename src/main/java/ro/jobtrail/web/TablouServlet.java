package ro.jobtrail.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.jobtrail.entity.StatusAplicare;
import ro.jobtrail.service.AplicareService;
import ro.jobtrail.service.TablouService;
import ro.jobtrail.service.ValidareException;

import java.io.IOException;

@WebServlet("/tablou/*")
public class TablouServlet extends HttpServlet {

    private static final String VIEWS = "/WEB-INF/views/tablou/";

    @EJB
    private TablouService tablouService;

    @EJB
    private AplicareService aplicareService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String actiune = req.getPathInfo();
        if (actiune == null || actiune.equals("/")) {
            req.setAttribute("tablou", tablouService.construiesteTablou());
            req.setAttribute("statusuri", StatusAplicare.values());
            req.getRequestDispatcher(VIEWS + "tablou.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("/status".equals(req.getPathInfo())) {
            try {
                Long id = Long.parseLong(req.getParameter("aplicareId"));
                StatusAplicare statusNou = StatusAplicare.valueOf(req.getParameter("status"));
                aplicareService.schimbaStatus(id, statusNou);     // scriere doar în SQL Server
                req.getSession().setAttribute("mesajSucces", "Statusul aplicării a fost actualizat.");
            } catch (ValidareException e) {
                req.getSession().setAttribute("mesajEroare", e.getMessage());
            } catch (IllegalArgumentException | NullPointerException e) {
                req.getSession().setAttribute("mesajEroare", "Datele trimise nu sunt valide.");
            }
            resp.sendRedirect(req.getContextPath() + "/tablou");
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}