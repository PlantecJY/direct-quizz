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
import forms.QuestionForm;
import entities.*;
import java.sql.Array;
import java.util.List;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

/**
 *
 * @author iode
 */
//@WebServlet(name = "Question", urlPatterns = {"/question"})
@MultipartConfig
public class QuestionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final String ACCES_PUBLIC     = "/formulaire_connexion.jsp";
    public static final String ACCES_INTERDIT     = "/interdit.jsp";
    public static final String ACCES_RESTREINT  = "/restricted";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    
    public static final String CHEMIN        = "chemin";

    // Injected DAO EJB:
    @EJB
    private QuestionDao questionDao;
    @EJB
    private ReponseDao reponseDao;

    @EJB
    private ThemeDao themeDao;
    
    // thème par défaut
    public Integer themeChoisi;

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            // paramètre
            String chemin = this.getServletConfig().getInitParameter( CHEMIN );

            // encodage
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
             
            // récupération de la session, le cas échéant
            HttpSession session = request.getSession();
            Utilisateur utilisateur = (Utilisateur) session.getAttribute("sessionUtilisateur");
            
            if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
                // Redirection vers la page de connexion
                response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
            } else {
                // Actions restreintes
                String action = request.getParameter("action");
                // Ajout de question
                if(action.equals("ajouter")){
                    String data = request.getParameter("data");
                    String idTheme = request.getParameter("idTheme");
                    if(data.equals("false")){
                        // récupération des thèmes
                        List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                        request.setAttribute("listeThemes", listeThemes);                       
                        request.setAttribute("idTheme", idTheme);                       
                        // affichage du formulaire
                        request.setAttribute( "action", action );
                        RequestDispatcher dispatch = request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_question.jsp"); 
                        dispatch.forward(request, response);
                    } else {
                        // gestion des données du formulaire
                        QuestionForm gestionFormulaire = new QuestionForm( questionDao, reponseDao, themeDao, response );
                        // vérification des données de la requête 
                        Question question = gestionFormulaire.creerQuestion( request, action, 0 );
                        // résultat du traitement et bean dans la requête
                        if(!gestionFormulaire.getErreurs().isEmpty()){
                           // traitement des données
                            request.setAttribute( "form", gestionFormulaire );
                            request.setAttribute( "question", question );
                            request.setAttribute( "action", action );
                            // récupération des thèmes
                            List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                            request.setAttribute("listeThemes", listeThemes); 
                            request.setAttribute("idTheme", idTheme); 
                            // page
                            request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_question.jsp").forward(request, response); 
                        } else {
                            // retour à la liste
                            Theme theme = question.getTheme();
                            if(theme == null){
                                // récupération des thèmes
                                List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                                request.setAttribute("listeThemes", listeThemes);     
                                request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur));
                                request.setAttribute("idTheme","0");
                                request.getRequestDispatcher(ACCES_RESTREINT + "/liste_questions.jsp").forward(request, response);
                            } else {
                                themeChoisi = theme.getId();
                                //request.getRequestDispatcher("/question?action=lister&idTheme="+themeChoisi).forward(request, response);
                                // PROV
                                 // récupération des thèmes
                                List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                                request.setAttribute("listeThemes", listeThemes); 
                                request.setAttribute("idTheme", idTheme); 
                                // questions du thème choisi
                                theme = themeDao.getById(Integer.parseInt(idTheme));
                                request.setAttribute("listeQuestions", questionDao.listerQuestionsParTheme(utilisateur,theme));                            
                                request.getRequestDispatcher(ACCES_RESTREINT + "/liste_questions.jsp").forward(request, response);
                           }
                        }

                    } 
                }
                // édition d'une question
                else if(action.equals("editer")){
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    // question
                    Question question = questionDao.getById(id);
                    Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
                    if ( user.getId().equals(question.getUtilisateur().getId() ) ) {
                        request.setAttribute("question", question);
                        request.setAttribute( "action", "mettre_a_jour" );
                        request.setAttribute( "id", id );
                        // récupération des thèmes
                        List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                        request.setAttribute("listeThemes", listeThemes);     
                        request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_question.jsp").forward(request, response);
                    } else {
                        // Redirection
                        request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur));
                        request.setAttribute("message", "action interdite");
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_questions.jsp").forward(request, response);
                    }
                } 
                // Mise à jour de question
                else if(action.equals("mettre_a_jour")){
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    // thème choisi ou thème par défaut (d'abord String car peut être null)
                    String idTheme = request.getParameter("idTheme");
                    // gestion des données du formulaire
                    QuestionForm gestionFormulaire = new QuestionForm( questionDao, reponseDao, themeDao, response );
                    // vérification des données de la requête 
                    Question question = gestionFormulaire.creerQuestion( request, action, id );
                    // résultat du traitement et bean dans la requête
                    if(!gestionFormulaire.getErreurs().isEmpty()){
                        // traitement des données
                        List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                        request.setAttribute( "listeThemes", listeThemes);     
                        request.setAttribute( "idTheme", idTheme);     
                        request.setAttribute( "form", gestionFormulaire );
                        request.setAttribute( "question", question );
                        request.setAttribute( "action", action );
                        request.setAttribute( "id", id );
                        request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_question.jsp").forward(request, response); 
                    } else {
                        // retour à la liste avec le idTheme qui a été mémorisé
                        if(idTheme == null) {
                            request.getRequestDispatcher("/question?action=lister").forward(request, response);
                        } else {
                            request.getRequestDispatcher("/question?action=lister&idTheme="+idTheme).forward(request, response);
                        }
                    }
                }
                // liste des questions
                else if(action.equals("lister")){
                    // éventuellement id de la question
                    String idQuestion = request.getParameter("id");
                    if(idQuestion != null){
                        Integer idQ = Integer.parseInt(idQuestion); 
                        Question question = questionDao.getById(idQ);
                        // question à afficher en dessous de la liste
                        request.setAttribute( "question", question );
                    }
                    // thème choisi ou thème par défaut (d'abord String car peut être null)
                    String idTheme = null;
                    idTheme = request.getParameter("idTheme");
                    // récupération des thèmes
                    List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                    request.setAttribute("listeThemes", listeThemes);     
                    // page
                    if(idTheme == null || idTheme.equals("")){
                        // test sur themeChoisi
                        if(themeChoisi == null) {
                            // toutes les questions
                            request.setAttribute("idTheme","0");
                            request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur)); 
                        } else {
                            // questions du thème choisi
                            Theme theme = themeDao.getById(themeChoisi);
                            request.setAttribute("idTheme", themeChoisi);
                            request.setAttribute("listeQuestions", questionDao.listerQuestionsParTheme(utilisateur,theme));                            
                        }
                    } else {
                        // test sur idTheme
                        if(idTheme.equals("0")){
                            // toutes les questions
                            request.setAttribute("idTheme", "0");
                            request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur)); 
                        } else {
                            // selon le thème
                            Integer idTh = Integer.parseInt(idTheme);
                            Theme theme = themeDao.getById(idTh);
                            request.setAttribute("idTheme", idTheme);
                            request.setAttribute("listeQuestions", questionDao.listerQuestionsParTheme(utilisateur,theme));
                            this.themeChoisi = idTh;
                        }
                    }
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_questions.jsp").forward(request, response);
                } 
                // suppression d'un question
                else if(action.equals("supprimer")){
                    // id
                    Integer idQuestion = Integer.parseInt(request.getParameter("id"));
                    // thème choisi ou thème par défaut (d'abord String car peut être null)
                   
                    String idTheme = request.getParameter("idTheme");
                    request.setAttribute("idTheme", idTheme);
                    // suppression
                    questionDao.supprimerQuestion(idQuestion);
                    // récupération des thèmes
                    List<Theme> listeThemes = themeDao.listerThemes(utilisateur);
                    request.setAttribute("listeThemes", listeThemes);     
                    // affichage
                    if(idTheme.equals("0") || idTheme.equals("")){
                        // toutes les questions
                        request.setAttribute("idTheme","0");
                        request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur)); 
                    } else {
                        // selon le thème
                        Integer idTh = Integer.parseInt(idTheme);
                        Theme theme = themeDao.getById(idTh);
                        request.setAttribute("idTheme", idTheme);
                        request.setAttribute("listeQuestions", questionDao.listerQuestionsParTheme(utilisateur,theme));
                        this.themeChoisi = idTh;
                    }
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_questions.jsp").forward(request, response);
                } 
                /* voir une question // n'est plus utilisé - on passe par lister
                else if(action.equals("voir")){
                    // id
                    Integer idQuestion = Integer.parseInt(request.getParameter("id"));
                    // question
                    Question question = questionDao.getById(idQuestion);
                    // question à afficher en dessous de la liste
                    request.setAttribute( "question", question );
                    // liste
                    request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur));
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_questions.jsp").forward(request, response);
                } */
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