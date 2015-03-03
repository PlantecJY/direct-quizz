package servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UtilisateurDao;
import entities.Utilisateur;
import forms.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@WebServlet( urlPatterns = { "/connexion" } )
public class Connexion extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String ATT_USER = "utilisateur";
    public static final String ATT_FORM = "form";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";

    // Injection de la façade Utilisateur (EJB Stateless)
    @EJB
    private UtilisateurDao utilisateurDao;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // récupération du paramètre
        String action = request.getParameter("action");
        // enregistrement
        if (action.equals("enregistrement")) {
            String data = request.getParameter("data");
            if (data.equals("false")) {
                // affichage du formulaire
                RequestDispatcher dispatch = request.getRequestDispatcher("formulaire_membre.jsp");
                dispatch.forward(request, response);
            } else {
                // gestion des données du formulaire
                InscriptionForm gestionFormulaire = new InscriptionForm(utilisateurDao);
                // vérification des données de la requête / nouvel utilisateur
                Utilisateur utilisateur = gestionFormulaire.inscrireUtilisateur(request);
                utilisateur.setValide(0);
                // résultat du traitement et bean dans la requête
                request.setAttribute(ATT_FORM, gestionFormulaire);
                request.setAttribute(ATT_USER, utilisateur);
                // affichage du formulaire avec résultat de la soumission
                RequestDispatcher dispatch = request.getRequestDispatcher("formulaire_membre.jsp");
                dispatch.forward(request, response);
            }
        } else if (action.equals("connexion")) {
            // connexion
            String data = request.getParameter("data");
            if (data.equals("false")) {
                // affichage du formulaire
                RequestDispatcher dispatch = request.getRequestDispatcher("formulaire_connexion.jsp");
                dispatch.forward(request, response);
            } else {
                // gestion des données du formulaire
                ConnexionForm gestionFormulaire = new ConnexionForm(utilisateurDao);
                // vérification des données de la requête / nouvel utilisateur
                Utilisateur utilisateur = gestionFormulaire.connecterUtilisateur(request);
                // Récupération de la session depuis la requête
                HttpSession session = request.getSession();
                // si aucune erreur de validation n'a eu lieu, alors ajout du bean à la session :
                // la session peut être détruite dans plusieurs circonstances :
                //      l'utilisateur ferme son navigateur
                //      la session expire après une période d'inactivité de l'utilisateur
                // l'utilisateur se déconnecte.

                String page = "";
                if (utilisateur != null && utilisateur.getValide() == 1) {
                    session.setAttribute(ATT_SESSION_USER, utilisateur);
                    page = "index.jsp";
                } else {
                    session.setAttribute(ATT_SESSION_USER, null);
                    page = "formulaire_connexion.jsp";
                }
                // résultat du traitement et bean dans la requête
                if (utilisateur.getGestionnaire()==1) {
                    deleteLateUsers();
                }
                request.setAttribute(ATT_FORM, gestionFormulaire);
                request.setAttribute(ATT_USER, utilisateur);
                // affichage 
                RequestDispatcher dispatch = request.getRequestDispatcher(page);
                dispatch.forward(request, response);
            }
        } else if (action.equals("deconnexion")) {
            // connexion
            // Récupération et destruction de la session en cours 
            HttpSession session = request.getSession();
            session.invalidate();
            // Redirection
            //response.sendRedirect( URL_REDIRECTION );
            RequestDispatcher dispatch = request.getRequestDispatcher("formulaire_connexion.jsp");
            dispatch.forward(request, response);
        } else if (action.equals("recuperer")) {
            // récupération
            String data = request.getParameter("data");
            if (data.equals("false")) {
                // affichage du formulaire
                RequestDispatcher dispatch = request.getRequestDispatcher("formulaire_recuperation.jsp");
                dispatch.forward(request, response);
            } else {
                // gestion des données du formulaire
                ConnexionForm gestionFormulaire = new ConnexionForm(utilisateurDao);
                // vérification des données de la requête / nouvel utilisateur
                Utilisateur utilisateur = gestionFormulaire.recupererUtilisateur(request);
                request.setAttribute(ATT_FORM, gestionFormulaire);
                request.setAttribute(ATT_USER, utilisateur);

                RequestDispatcher dispatch = request.getRequestDispatcher("formulaire_recuperation.jsp");
                dispatch.forward(request, response);
            }

        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void deleteLateUsers() {
        List<Utilisateur> utils = utilisateurDao.getAllMembres();
        for (int i = 0; i < utils.size(); i++) {
            Utilisateur utilisateur=utils.get(i);
            if (utilisateur.getValide() == 0) {
                Date inscr = utilisateur.getDateInscription();
                Date actuelle = new Date();
                System.out.println(utilisateur.getLogin());
                if ((inscr.getTime() + 172800000) < actuelle.getTime()) {
                    utilisateurDao.supprimerMembre(utilisateur.getId());
                }
            }
        }
    }
}
