/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.*;
import forms.ThemeForm;
import entities.*;
import java.sql.Array;
import java.util.List;
import javax.servlet.http.HttpSession;

/**
 *
 * @author iode
 */
//@WebServlet(name = "Theme", urlPatterns = {"/theme"})
public class ThemeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final String ACCES_PUBLIC     = "/formulaire_connexion.jsp";
    public static final String ACCES_INTERDIT     = "/interdit.jsp";
    public static final String ACCES_RESTREINT  = "/restricted";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";

    // Injected DAO EJB:
    @EJB
    private ThemeDao themeDao;

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            // encodage
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
             
            // récupération de la session, le cas échéant
            HttpSession session = request.getSession();
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("sessionUtilisateur");
            
            if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
                // Redirection vers la page de connexion
                response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
            } else {
                // Actions restreintes
                String action = request.getParameter("action");
                // Ajout de thème
                if(action.equals("ajouter")){
                    String data = request.getParameter("data");
                    if(data.equals("false")){
                        // affichage du formulaire
                        request.setAttribute( "action", action );
                        RequestDispatcher dispatch = request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_theme.jsp"); 
                        dispatch.forward(request, response);
                    } else {
                        // gestion des données du formulaire
                        ThemeForm gestionFormulaire = new ThemeForm(themeDao );
                        // vérification des données de la requête 
                        Theme theme = gestionFormulaire.creerTheme( request, action, 0 );
                        // résultat du traitement et bean dans la requête
                        if(!gestionFormulaire.getErreurs().isEmpty()){
                           // traitement des données
                            request.setAttribute( "form", gestionFormulaire );
                            request.setAttribute( "theme", theme );
                            request.setAttribute( "action", action );
                            // page
                            request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_theme.jsp").forward(request, response); 
                        } else {
                            // retour à la liste
                            request.setAttribute("listeThemes", themeDao.listerThemes(utilisateur));
                            request.getRequestDispatcher(ACCES_RESTREINT + "/liste_themes.jsp").forward(request, response);
                        }

                    } 
                }
                // édition d'un thème
                else if(action.equals("editer")){
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    // thme
                    Theme theme = themeDao.getById(id);
                    Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
                    if ( user.getId().equals(theme.getUtilisateur().getId() ) ) {
                        request.setAttribute("theme", theme);
                        request.setAttribute( "action", "mettre_a_jour" );
                        request.setAttribute( "id", id );
                        request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_theme.jsp").forward(request, response);
                
                    } else {
                        // Redirection 
                        request.setAttribute("listeThemes", themeDao.listerThemes(utilisateur));
                        request.setAttribute("message", "action interdite");
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_themes.jsp").forward(request, response);
                    }
                }
                // Mise à jour de thème
                else if(action.equals("mettre_a_jour")){
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    // gestion des données du formulaire
                    ThemeForm gestionFormulaire = new ThemeForm(themeDao );
                    // vérification des données de la requête 
                    Theme theme = gestionFormulaire.creerTheme( request, action, id );
                        // résultat du traitement et bean dans la requête
                        if(!gestionFormulaire.getErreurs().isEmpty()){
                           // traitement des données
                            request.setAttribute( "form", gestionFormulaire );
                            request.setAttribute( "theme", theme );
                            request.setAttribute( "action", action );
                            request.setAttribute( "id", id );
                            request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_theme.jsp").forward(request, response); 
                        } else {
                            // retour à la liste
                            request.setAttribute("listeThemes", themeDao.listerThemes(utilisateur));
                            request.getRequestDispatcher(ACCES_RESTREINT + "/liste_themes.jsp").forward(request, response);
                        }
                }
                // liste des thèmes
                else if(action.equals("lister")){
                    // liste
                    request.setAttribute("listeThemes", themeDao.listerThemes(utilisateur));
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_themes.jsp").forward(request, response);
                } 
                // suppression d'un thème
                else if(action.equals("supprimer")){
                    // id
                    Integer idTheme = Integer.parseInt(request.getParameter("id"));
                    // suppression
                    themeDao.supprimerTheme(idTheme);
                    // liste
                    request.setAttribute("listeThemes", themeDao.listerThemes(utilisateur));
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_themes.jsp").forward(request, response);
                } 
            }
   }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }
    
}