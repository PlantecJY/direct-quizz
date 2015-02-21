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
import forms.SequenceForm;
import entities.*;
import forms.ParticipantForm;
import forms.QuestionForm;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 *
 * @author JYP
 */
//@WebServlet(name = "Sequence", urlPatterns = {"/sequence"})
public class SequenceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final String ACCES_PUBLIC     = "/formulaire_connexion.jsp";
    public static final String ACCES_INTERDIT     = "/interdit.jsp";
    public static final String ACCES_RESTREINT  = "/restricted";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";

    // DAO EJB Injectés
    @EJB
    private SequenceDao sequenceDao;
    @EJB
    private QuestionDao questionDao;
    @EJB
    public ParticipantModel m;// = new ParticipantModel();

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            // encodage
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            // Actions publiques
            String action = request.getParameter("action");
            if(action.equals("participer")){
                // participation
                String data = request.getParameter("data");
                if(data.equals("false")){
                    // formulaire
                    RequestDispatcher dispatch = request.getRequestDispatcher("/formulaire_participation.jsp"); 
                    dispatch.forward(request, response);
                } else {
                    // gestion des données du formulaire
                    ParticipantForm gestionFormulaire = new ParticipantForm( m, sequenceDao );
                    // vérification des données de la requête / nouveau participant
                    Participant participant = gestionFormulaire.creerParticipant( request );
                    // résultat du traitement et bean dans la requête
                    request.setAttribute( "form", gestionFormulaire ); 
                    if(participant != null){
                        request.setAttribute( "identifiant", participant.getIdentifiant());
                    } else {
                        request.setAttribute( "code", request.getParameter("code"));
                        request.setAttribute( "motDePasse", request.getParameter("motDePasse"));
                        request.setAttribute( "identifiant", request.getParameter("identifiant"));
                    }
                    if(!gestionFormulaire.getErreurs().isEmpty()){
                        // renvoi de la page
                        request.getRequestDispatcher("/formulaire_participation.jsp").forward(request, response); 
                    } else {
                        // mode
                        Sequence sequence = gestionFormulaire.getSequence(request.getParameter("code"),request.getParameter("motDePasse"));
                        String mode = sequence.getMode();
                        Integer idSequence = sequence.getId();
                        request.setAttribute( "mode", mode);
                        request.setAttribute( "idSequence", idSequence);
                        request.getRequestDispatcher("/participation_sequence.jsp").forward(request, response);
                    }
                }
            } else if (action.equals("quitter")){
                request.setAttribute( "participant", null );
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                
            }  else {
                // actions privées 
                
                // récupération de la session, le cas échéant
                HttpSession session = request.getSession();
                Utilisateur utilisateur = (Utilisateur) session.getAttribute("sessionUtilisateur");

                if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
                    // Redirection vers la page de connexion
                    response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
                } else {
                    // Actions restreintes : 
                    // Ajout de séquence
                    if(action.equals("ajouter")){
                        String data = request.getParameter("data");
                        if(data.equals("false")){
                            // liste des questions
                            request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur));
                            request.setAttribute( "action", action );
                            // affichage du formulaire
                            RequestDispatcher dispatch = request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_sequence.jsp"); 
                            dispatch.forward(request, response);
                        } else {
                            // gestion des données du formulaire
                            SequenceForm gestionFormulaire = new SequenceForm( sequenceDao, questionDao );
                            // vérification des données de la requête / nouvel utilisateur
                            Sequence sequence = gestionFormulaire.creerSequence( request, action, 0  );
                            // résultat du traitement et bean dans la requête
                            request.setAttribute( "form", gestionFormulaire );
                            request.setAttribute( "sequence", sequence );
                            request.setAttribute( "action", action );
                            if(!gestionFormulaire.getErreurs().isEmpty()){
                                // liste des questions
                                request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur));
                               // traitement des données
                                request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_sequence.jsp").forward(request, response); 
                            } else {
                                // retour à la liste
                                request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                                request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);
                            }
                        } 
                    }
                    // édition d'une séquence
                    else if(action.equals("editer")){
                        Integer id = Integer.parseInt(request.getParameter("id"));
                        // séquence
                        Sequence sequence = sequenceDao.getById(id);
                        Utilisateur user = (Utilisateur) session.getAttribute(ATT_SESSION_USER);
                        if ( user.getId().equals(sequence.getUtilisateur().getId() ) ) {
                            request.setAttribute("sequence", sequence);
                            // liste des questions
                            request.setAttribute("listeQuestions", questionDao.listerQuestions(utilisateur));
                            request.setAttribute( "action", "mettre_a_jour" );
                            request.setAttribute( "id", id );
                            request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_sequence.jsp").forward(request, response);
                        } else {
                            // Redirection 
                            request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                            request.setAttribute("message", "action interdite");
                            request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);
                        }
                    } 
                    // Ajout de séquence
                    else if(action.equals("mettre_a_jour")){
                        Integer id = Integer.parseInt(request.getParameter("id"));
                        // gestion des données du formulaire
                        SequenceForm gestionFormulaire = new SequenceForm( sequenceDao, questionDao );
                        // vérification des données de la requête 
                        Sequence sequence = gestionFormulaire.creerSequence( request, action, id  );
                            // résultat du traitement et bean dans la requête
                            if(!gestionFormulaire.getErreurs().isEmpty()){
                               // traitement des données
                                request.setAttribute( "form", gestionFormulaire );
                                request.setAttribute( "sequence", sequence );
                                request.setAttribute( "action", action );
                                request.setAttribute( "id", id );
                                request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_sequence.jsp").forward(request, response); 
                            } else {
                                // retour à la liste
                                request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                                request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);
                            }
                    } else if (action.equals("lister")){
                        // liste des sequences
                        request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);

                    } else if(action.equals("supprimer")){
                        // suppression d'une sequence
                        // id
                        Integer idSequence = Integer.parseInt(request.getParameter("id"));
                        // suppression
                        sequenceDao.supprimerSequence(idSequence);
                        // liste
                        request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);
                    } else if(action.equals("voir")){
                        // voir une sequence
                        // id
                        Integer idSequence = Integer.parseInt(request.getParameter("id"));
                        Sequence sequence = sequenceDao.getById(idSequence);
                        // séquence à afficher en dessous de la liste
                        request.setAttribute( "sequence", sequence );
                        // inclusion de texteReponses3 (créé par la servlet Resultat)
                        request.getRequestDispatcher("/Resultats?action=bilan&id="+idSequence).include(request, response) ;
                        // liste
                        request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);

                    } else if(action.equals("jouer")){
                        // jouer une sequence en mode dirigé ou libre
                        // mode
                        String mode = request.getParameter("mode");
                        // séquence
                        Integer idSequence = Integer.parseInt(request.getParameter("id"));
                        Sequence sequence = sequenceDao.getById(idSequence);
                        sequenceDao.changerMode(idSequence, mode);
                        request.setAttribute( "sequence", sequence );
                        // ajout au modèle
                        m.addSequence(sequence);
                        
                        Integer numeroQuestion = 1;
                        if(mode.equals("dirige")){
                            request.setAttribute( "numeroQuestion", numeroQuestion );
                        }
                        request.setAttribute( "mode", mode );
                        // page de jeu
                        request.getRequestDispatcher(ACCES_RESTREINT + "/jouer_sequence.jsp").forward(request, response);
                        
                    } else if (action.equals("vider")){
                        // vider les résultats
                        // id
                        Integer idSequence = Integer.parseInt(request.getParameter("id"));
                        m.removeParticipantsForSequence(idSequence);
                        Sequence sequence = sequenceDao.getById(idSequence);
                        // retour à la séquence
                        // séquence à afficher en dessous de la liste
                        request.setAttribute( "sequence", sequence );
                        // inclusion de texteReponses3 (créé par la servlet Resultat)
                        request.getRequestDispatcher("/Resultats?action=bilan&id="+idSequence).include(request, response) ;
                        //request.setAttribute( "texteReponses3", "Aucun résultat");
                        // liste
                        request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);
                    }   else if (action.equals("fermer")){
                        // clôturer
                        // id
                        Integer idSequence = Integer.parseInt(request.getParameter("id"));
                        Sequence sequence = sequenceDao.getById(idSequence);
                        // mode par défaut
                        sequenceDao.changerMode(idSequence, "0");
                        m.removeSequence(sequence);
                        // retour à la séquence
                        // séquence à afficher en dessous de la liste
                        request.setAttribute( "sequence", sequence );
                        // inclusion de texteReponses3 (créé par la servlet Resultat)
                        request.getRequestDispatcher("/Resultats?action=bilan&id="+idSequence).include(request, response) ;
                        // liste
                        request.setAttribute("listeSequences", sequenceDao.listerSequences(utilisateur));
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_sequences.jsp").forward(request, response);
                    }
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