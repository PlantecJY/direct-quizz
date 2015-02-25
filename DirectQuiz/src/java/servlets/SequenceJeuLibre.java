package servlets;

import dao.ParticipantDao;
import dao.ParticipantModel;
import dao.SequenceDao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlets.UserServletTopic;

import entities.Participant;
import entities.Question;
import entities.Reponse;
import entities.Sequence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class for Servlet: UserServlet
 *
 */

//@WebServlet(name = "SequenceJeuDirige", urlPatterns = {"/sequenceJeuDirige"})
public class SequenceJeuLibre extends javax.servlet.http.HttpServlet {
         
        public HttpServletRequest request;
        public HttpServletResponse response;
        
	static final long serialVersionUID = 1L;

        // DAO EJB Injectés
        @EJB
        private SequenceDao sequenceDao;
        @EJB ParticipantModel m;
        
   
	public SequenceJeuLibre() {
		super();
	}   	
	
        @Override
        public void init() { 
        }

        @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
                this.response = response;
                this.request = request;
                
                // paramètres
                String action = request.getParameter("action"); 

                // fin de participation
                if(action.equals("finir_participer")){
                    request.getRequestDispatcher("/index.jsp").forward(request, response);

                // question suivante
                } else if(action.equals("recuperer_question_et_envoi")){
                    
                    // paramètres
                    String identifiant = request.getParameter("identifiant"); 
                    Integer id = Integer.parseInt(request.getParameter("idSequence")); 
                    String idQuestion = request.getParameter("idQuestion"); 
                    Integer numeroQuestion = Integer.parseInt(request.getParameter("numeroQuestion")); 
                    String[] listeValeursTransmises = request.getParameterValues("valeurs" );
                    
                    // récupération de la question
                    Sequence sequence = sequenceDao.getById(id);
                    List<Question> questions = sequence.getQuestions();
                    String texteQuestion = "";        
                
                    // transmission seulement si pas la question 0 et si des valeurs ont été cochées
                    if(numeroQuestion != 1 && listeValeursTransmises != null){

                        // sauvegarde du choix : récupération du participant ; mise à jour de la liste des réponses
                        Participant participant = m.getParticipantByIdentifiant(identifiant);

                        // getListeResultats > ArrayList
                        Map<String, String[]> resultat = new HashMap<String, String[]>();
                        resultat.put(idQuestion, listeValeursTransmises);
                        participant.getListeResultats().add(resultat);

                        // getListeResultats2 > HashMap
                        participant.getListeResultats2().put(idQuestion, listeValeursTransmises);
                    }

                    // numeroQuestion < ou = nombre de questions
                    if(numeroQuestion <= questions.size()){               

                        // nouvelle question
                        Question question = questions.get(numeroQuestion-1);
                        // texte de la question
                        texteQuestion = "<p><input type=\"hidden\" name=\"idQuestion\" value=\"" + question.getId() + "\"/>";
                        texteQuestion = texteQuestion + "<h2 class=\"titre\">Question n°"+numeroQuestion+"</h2>";
                        texteQuestion = texteQuestion + "<br><b>" + question.getTitre() + "</b><br>" + question.getEnonce();
                        if(question.getImageRealName() != null){
                            texteQuestion = texteQuestion + "<br><img src='./img/" + question.getImageRealName() + "' width='200px'><br>";
                        }
                        texteQuestion = texteQuestion +"<ul>";
                        Integer nbReponses = 0;
                        for(int j = 0; j < question.getReponses().size(); j++){
                            nbReponses++;
                            Reponse reponse = question.getReponses().get(j);
                            int idReponse = reponse.getId();
                            texteQuestion = texteQuestion + "<li><input type=\"checkbox\" name=\"valeurs\" value=\""+idReponse+"\"/>" + reponse.getTexte()+"</li>";
                            texteQuestion = texteQuestion +"<label for=\"checkbox"+idReponse+"\" class=\"topcoat-checkbox\">  <input id=\"checkbox"+idReponse+"\" aria-labelledby=\"checkbox3-label\" aria-describedby=\"checkbox3-description\" name=\"valeurs\" type=\"checkbox\" value=\""+idReponse+"\"/>  <div class=\"topcoat-checkbox__checkmark\"></div> <span id=\"checkbox"+idReponse+"-label\">"+reponse.getTexte()+"</span></label>";
                        }
                        texteQuestion = texteQuestion +"</ul>";
                        texteQuestion = texteQuestion+"<br><input id=\"bouton\" type=\"submit\" value=\"Soumettre\" \"/></p>";
                        
                        // incrémentation
                        numeroQuestion++;
                        
                        // page de jeu
                        request.setAttribute( "texteQuestion", texteQuestion );
                        request.setAttribute( "identifiant", identifiant );
                        request.setAttribute( "idSequence", id );
                        request.setAttribute( "numeroQuestion", numeroQuestion );
                        request.setAttribute( "idSequence", id );

                        request.getRequestDispatcher("/jeu_libre.jsp").forward(request, response);   

                    } else {
                        // texte de réponse
                        texteQuestion = "<p>Merci de vous être prêtés à ce sondage !</p>";

                        // page de jeu
                        request.setAttribute( "texteQuestion", texteQuestion );
                        request.getRequestDispatcher("/jeu_libre.jsp").forward(request, response);   
                    }

                }

        }
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
        @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
                
 }
