/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dao.UtilisateurDao;
import entities.Utilisateur;
import forms.ConnexionForm;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static servlets.Connexion.ATT_FORM;
import static servlets.Connexion.ATT_SESSION_USER;
import static servlets.Connexion.ATT_USER;

/**
 *
 * @author Samih
 */
@WebServlet(name = "VerificationCompte", urlPatterns = {"/VerificationCompte"})
public class VerificationCompte extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // Injection de la façade Utilisateur (EJB Stateless)
    @EJB
    private UtilisateurDao utilisateurDao;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        if (code == null || name == null) {

            request.setAttribute("txt", "Cet utilisateur n'existe pas.");
            RequestDispatcher dispatch = request.getRequestDispatcher("verif_compte.jsp");
            dispatch.forward(request, response);
        } else {
            ConnexionForm gestionFormulaire = new ConnexionForm(utilisateurDao);
            // vérification des données de la requête / nouvel utilisateur
            Utilisateur utilisateur = utilisateurDao.getByLogin(name);
            if (utilisateur == null) {
                request.setAttribute("txt", "Cet utilisateur n'existe pas.");
            } else {
                if (utilisateur.getValide() == 1) {
                    request.setAttribute("txt", "Cet utilisateur a déjà été validé.");
                } else {
                    if (utilisateur.verfCode(code)) {
                        utilisateurDao.validerMembre(utilisateur.getId());
                        request.setAttribute("txt", "Félicitations " + utilisateur.getLogin() + ", vous venez de valider votre compte ! ");
                    } else {
                        request.setAttribute("txt", "Le lien de vérification est erroné, veuillez vérifier le mail que vous avez reçu.");
                    }
                }
            }
            // affichage 
            RequestDispatcher dispatch = request.getRequestDispatcher("verif_compte.jsp");
            dispatch.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
