package servlets;

import dao.ParticipantDao;
import dao.ParticipantModel;
import dao.SequenceDao;
import entities.Participant;
import entities.Question;
import entities.Reponse;
import entities.Sequence;
import entities.Utilisateur;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class for Servlet: UserServlet
 *
 */
 //@WebServlet(name = "UserServletTopic", urlPatterns = {"/UserServletTopic"})
 public class UserServletTopic extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	 //@Resource(name="jms/TestConnectionFactory") - ne marche pas !
	 private TopicConnectionFactory topicConnectionFactory;
	 //@Resource(name="jms/testQueue") - ne marche pas !
	 private Topic topic;
         
         public static TopicSession		topicSessionP;
         public static TopicPublisher	       	topicPublisher;
         public static TopicSubscriber       	topicSubscriber;
    
	 static final long serialVersionUID = 1L;
         public static final String ACCES_RESTREINT  = "/restricted";
    
     // DAO EJB Injectés
    @EJB
    private SequenceDao sequenceDao;
    @EJB
    private ParticipantModel participantModel;
    
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public UserServletTopic() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
                
            // récupération des données de requête
            Integer id = Integer.parseInt(request.getParameter("id"));
            Integer numeroQuestion = Integer.parseInt(request.getParameter("numeroQuestion"));
            String identifiant = request.getParameter("identifiant");
            // récupération de la question
            Sequence sequence = sequenceDao.getById(id);
            List<Question> questions = sequence.getQuestions();
            
            String texteQuestion = "";
            String retourTopic = "";
                        
            
            // numeroQuestion < ou = nombre de questions
            if(numeroQuestion <= questions.size()){               
                
                // nouvelle question
                Question question = questions.get(numeroQuestion-1);
                // texte de la question
                texteQuestion = "<input type=\"hidden\" name=\"idQuestion\" value=\"" + question.getId() + "\"/>";
                texteQuestion = texteQuestion + "<h2 class=\"titre\">Question n°"+numeroQuestion+"</h2>";
                texteQuestion = texteQuestion + "<br><b>" + question.getTitre() + "</b><br>" + question.getEnonce();
                texteQuestion = texteQuestion +"<ul>";
                Integer nbReponses = 0;
                for(int j = 0; j < question.getReponses().size(); j++){
                    nbReponses++;
                    Reponse reponse = question.getReponses().get(j);
                    int idReponse = reponse.getId();
                    texteQuestion = texteQuestion + "<li><input type=\"checkbox\" name=\"valeurs\" value=\""+idReponse+"\"/>" + reponse.getTexte()+"</li>";
                }
                texteQuestion = texteQuestion +"</ul>";
                texteQuestion = texteQuestion+"<br><input id=\"bouton\" type=\"submit\" value=\"Soumettre\" onclick=\"shadow()\"/>";

                try {
                       Context ctx = new InitialContext();
                       topicConnectionFactory = (TopicConnectionFactory)ctx.lookup("jms/TestConnectionFactory");
                       topic = (Topic)ctx.lookup("jms/testTopic");
                       if(topicConnectionFactory == null) {
                               out.println(" Topic Connection Factory lookup has failed");
                               return;
                       }
                       if(topic == null) {
                               out.println("Topic lookup has failed");
                               return;
                       }
                } catch (NamingException ex) {
                    Logger.getLogger(UserServletTopic.class.getName()).log(Level.SEVERE, null, ex);
                }

                TopicConnection topicConnection = null;
                try {    
                    topicConnection = topicConnectionFactory.createTopicConnection();
                    topicConnection.start();
                    
                    //topicSessionP = topicConnection.createTopicSession(false,1);
                    topicSessionP = topicConnection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
                    topicPublisher = topicSessionP.createPublisher(topic);   
                    TextMessage message = topicSessionP.createTextMessage();
                    message.setText(texteQuestion);
                    topicPublisher.publish(message);
                    // message non-textuel (Message) de contrôle indiquant la fin des messages
                    //topicPublisher.publish(topicSessionP.createMessage());

                } catch (JMSException e) {
                    e.printStackTrace();
                } finally {
                    if(topicConnection != null) {
                        try {
                            topicConnection.close();
                        } catch (JMSException e1) { }
                    }
                }          

                // page de jeu
                request.setAttribute( "sequence", sequence );
                retourTopic = "";
                
                if(numeroQuestion == questions.size()){
                    // plus de question
                    retourTopic = "Dernière question envoyée";
                    String fin = "oui";
                    request.setAttribute("fin", fin);            
                } else {
                    retourTopic = "Question envoyée";
                }

                numeroQuestion++;
                request.setAttribute("numeroQuestion", numeroQuestion );
                request.setAttribute("idQuestion", question.getId() );
                request.setAttribute("retourTopic", retourTopic);
                request.setAttribute("mode", "dirige");
                
                request.getRequestDispatcher(ACCES_RESTREINT + "/jouer_sequence.jsp").forward(request, response);   
                
            } else {
                // texte de réponse
                texteQuestion = "<p>Merci de vous être prêtés à ce sondage !</p>";

                try {
                       Context ctx = new InitialContext();
                       topicConnectionFactory = (TopicConnectionFactory)ctx.lookup("jms/TestConnectionFactory");
                       topic = (Topic)ctx.lookup("jms/testTopic");
                       if(topicConnectionFactory == null) {
                               out.println(" Topic Connection Factory lookup has failed");
                               return;
                       }

                       if(topic == null) {
                               out.println("Topic lookup has failed");
                               return;
                       }
                } catch (NamingException ex) {
                    Logger.getLogger(UserServletTopic.class.getName()).log(Level.SEVERE, null, ex);
                }

                TopicConnection topicConnection = null;
                try {    
                    topicConnection = topicConnectionFactory.createTopicConnection();
                    topicConnection.start();
                    //topicSessionP = topicConnection.createTopicSession(false,1);
                    topicSessionP = topicConnection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
                    topicPublisher = topicSessionP.createPublisher(topic);   
                    TextMessage message = topicSessionP.createTextMessage();
                    message.setText(texteQuestion);
                    topicPublisher.publish(message);
                    // message non-textuel (Message) de contrôle indiquant la fin des messages
                    //topicPublisher.publish(topicSessionP.createMessage());

                } catch (JMSException e) {
                    e.printStackTrace();
                } finally {
                    if(topicConnection != null) {
                        try {
                            topicConnection.close();
                        } catch (JMSException e1) { }
                    }
                }          

                // page de jeu
                request.setAttribute( "sequence", sequence );
                retourTopic = "";
                String fin = "display";

                request.setAttribute("numeroQuestion", numeroQuestion );
                request.setAttribute("mode", "dirige");
                request.setAttribute("fin", fin);
                
                request.getRequestDispatcher(ACCES_RESTREINT + "/jouer_sequence.jsp").forward(request, response);   
                           
           }
	}
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
        
 }