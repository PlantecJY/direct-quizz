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
//@WebServlet(name = "Resultats", urlPatterns = {"/resultats"})
public class Resultats extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final String ACCES_PUBLIC     = "/formulaire_connexion.jsp";
    public static final String ACCES_RESTREINT  = "/restricted";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String NO_RESULT     = "Aucun résultat";

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
            
                
                // récupération de la session, le cas échéant
                HttpSession session = request.getSession();
                Utilisateur utilisateur = (Utilisateur) session.getAttribute("sessionUtilisateur");

                if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
                    // Redirection vers la page de connexion
                    response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
                } else {

                    String action = request.getParameter("action");
                    Integer idSequence = Integer.parseInt(request.getParameter("id"));

                    String texteReponses3 = "";
                    
                    // initialisation de la liste des participants
                    Map<String, Participant> listeParticipants = null;
                    listeParticipants = m.getListeParticipantsSequence(idSequence);
                    
                    // Affichage des résultats
                    if(listeParticipants.isEmpty() || listeParticipants == null){
                        texteReponses3 = NO_RESULT;
                        
                    } else {   
                        Sequence sequence = sequenceDao.getById(idSequence);
                                               
                        // test 3
                        texteReponses3 = "<table class=\"defaut\">";
                        // Map des sommes par questions
                        Map <Integer, Integer> somme = new HashMap<Integer, Integer>();
                        // Nombre de points totaux
                        Integer totalPoints = 0;
                        // première ligne
                        texteReponses3 = texteReponses3 + "<tr><td class=\"part\">Part.</td>";
                        // autant de colonnes que de questions
                        for(Question question : sequence.getQuestions()){
                            // initialisation de la map somme
                            somme.put(question.getId(), 0);
                            // incrémentation du nombre de points
                            totalPoints = totalPoints + question.getPoints();
                            texteReponses3 = texteReponses3 + "<td>Q: " + question.getId(); 
                            // chaine des réponses justes
                            String chaineR = "";
                            // boucle sur les réponses
                            for(Reponse reponse : question.getReponses()){
                                // test si la réponse est à cocher
                                if(reponse.getValeur() == 1){
                                   chaineR = chaineR + reponse.getId() +"-";
                                }
                            }
                            //texteReponses3 = texteReponses3 + "<br>" + chaineR;
                            texteReponses3 = texteReponses3 + "</td>";
                        }
                        // dernière colonne
                        texteReponses3 = texteReponses3 + "<td class=\"score\"></td>";
                        texteReponses3 = texteReponses3 + "</tr>";
                            
                        //boucle / participants
                        Integer nbParticipants = listeParticipants.size();
                        for (Map.Entry<String, Participant> entry : listeParticipants.entrySet()) {
                            // Nombre de points obtenus
                            Integer score = 0;
                            // autant de lignes que de participants enregistrés sur cette séquence
                            Participant participant = entry.getValue();
                            if(participant.getSequence().getId() == idSequence){
                                texteReponses3 = texteReponses3 + "<tr>";
                                texteReponses3 = texteReponses3 + "<td class=\"part\">" + participant.getIdentifiant() + "</td>";
                                // boucle sur les questions de la sequence (le participant y a répondu ou pas)
                                for(Question question : sequence.getQuestions()){
                                    // chaine des id des réponses justes
                                    String chaineR = "";
                                    // boucle sur les réponses
                                    for(Reponse reponse : question.getReponses()){
                                        // test si la réponse est à cocher
                                        if(reponse.getValeur() == 1){
                                            chaineR = chaineR + reponse.getId().toString() +"-";
                                        }
                                    }

                                    // autant de cellules que de questions
                                    texteReponses3 = texteReponses3 + "<td ";
                                    // boucle sur les résultats du participant et recherche d'un résultat correspondant à la question
                                    String trouve = "0";
                                    Map<String, String[]> listeResultats2 = participant.getListeResultats2(); 
                                    for(String resultat : listeResultats2.keySet()){
                                        // un résultat = paire clé-valeur : String idQuestion - String[] tableau des réponses
                                        // on cherche le résultat correspondant à la question courante
                                        if (Integer.parseInt(resultat) == question.getId()){
                                            // ce résultat correspond à la question courante
                                            trouve = "1";
                                            // boucle sur les valeurs cochées
                                            String chaineRP = "";
                                            for(String valeur : listeResultats2.get(resultat)){
                                                chaineRP = chaineRP + valeur + "-";
                                            }
                                            // test si la réponse est juste
                                            if(chaineR.equals(chaineRP)){
                                                texteReponses3 = texteReponses3 + "class=\"OK\">OK";
                                                // score
                                                score = score + question.getPoints();
                                                // somme
                                                Integer valeur = somme.get(question.getId())+1;
                                                somme.remove(question.getId());
                                                somme.put(question.getId(), valeur);
                                            } else {
                                                // création de la chaîne des résultats à transmettre pour cette question et ce participant
                                                //String resultatsParticipant = question.getEnonce() + "";
                                                String idQuestion = question.getId().toString();
                                                //texteReponses3 = texteReponses3 + "class=\"NOK\"><a href=\"#\" onmouseover=\"javascript:afficheReponsesParticipant("+idQuestion+")\">NOK</a>";
                                                texteReponses3 = texteReponses3 + "class=\"NOK\"><a href=\"#\" id=\"q"+idQuestion+"\" reponses=\""+chaineRP+"\">NOK</a>";
                                            }
                                            break;

                                        }
                                    }
                                    if(trouve.equals("0")){ 
                                        // aucun résultat trouvé
                                        texteReponses3 = texteReponses3 + "class=\"gris\">-";
                                    }
                                    texteReponses3 = texteReponses3 + "</td>"; 
                                }
                                // dernière colonne
                                texteReponses3 = texteReponses3 + "<td class=\"score\">"+score+"/"+totalPoints+"</td>";
                                texteReponses3 = texteReponses3 + "</tr>";
                            }
                        }
                        // ligne de bilan
                        texteReponses3 = texteReponses3 + "<tr><td class=\"part\"></td>";
                        for(Question question : sequence.getQuestions()){
                            texteReponses3 = texteReponses3 + "<td>"+somme.get(question.getId())+"/"+nbParticipants+"</td>";
                        }
                        texteReponses3 = texteReponses3 + "</tr>";
                        
                        texteReponses3 = texteReponses3 + "</table>";
                    }
                    
                    // traitement selon action
                    if(action.equals("bilan")){
                        // consultation des derniers résultats
                        if(!texteReponses3.equals(NO_RESULT)){
                            texteReponses3 = texteReponses3 + "<p><a href=\"sequence?action=vider&id="+idSequence+"\">Vider les résultats</a></p>";
                        }
                        request.setAttribute( "texteReponses3", texteReponses3 );
                            
                    } else if(action.equals("maj")){
                        // affichage temps réél
                        //<![CDATA[ ... ]]>
                        response.setContentType("text/xml");
                        response.setHeader("Cache-Control", "no-cache");
                        response.getWriter().write("<message><![CDATA["+texteReponses3+"]]></message>");
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