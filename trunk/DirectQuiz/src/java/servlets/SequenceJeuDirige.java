package servlets;

import dao.ParticipantDao;
import dao.ParticipantModel;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class for Servlet: UserServlet
 *
 */

//@WebServlet(name = "SequenceJeuDirige", urlPatterns = {"/sequenceJeuDirige"})
public class SequenceJeuDirige extends javax.servlet.http.HttpServlet implements MessageListener {
         
        public HttpServletRequest request;
        public HttpServletResponse response;
        
	static final long serialVersionUID = 1L;

        // topic
	//@Resource(name="jms/topicConnectionFactory") - ne marche pas !
	private TopicConnectionFactory topicConnectionFactory;
	//@Resource(name="jms/testQueue") - ne marche pas !
	private Topic topic;
        public static TopicSession                  topicSessionP;
        public static TopicSession                  topicSessionS;
        public static TopicPublisher                topicPublisher;
        public static TopicSubscriber               topicSubscriber;
        public static TopicConnection               topicConnection;
         
        public static QueueSession                  queueSessionP;

        @EJB ParticipantModel m;
        
        public String message_question = "";
        public String derniere_question;
   
	public SequenceJeuDirige() {
		super();
	}   	
	
        @Override
        public void init() { 
        }

        /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
        @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
                this.response = response;
                this.request = request;
                
            // paramètres
            String action = request.getParameter("action"); 
            String identifiant = request.getParameter("identifiant"); 
            
            // participer 
            if(action.equals("participer")){
                
                derniere_question = "";
                // connexion au topic venant du maître du jeu
                try {       
                       Context ctx = new InitialContext();
                       topicConnectionFactory = (TopicConnectionFactory)ctx.lookup("jms/TestConnectionFactory");
                       topic = (Topic)ctx.lookup("jms/testTopic");
                       if(topicConnectionFactory == null) {
                               //out.println(" Topic Connection Factory lookup has failed");
                               return;
                       }

                       if(topic == null) {
                               //out.println("Queue lookup has failed");
                               return;
                       }
                } catch (NamingException ex) {
                    Logger.getLogger(SequenceJeuDirige.class.getName()).log(Level.SEVERE, null, ex);
                }


                topicConnection = null;
                try {    
                    topicConnection = topicConnectionFactory.createTopicConnection();                        
                    topicSessionS = topicConnection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
                    topicSubscriber = topicSessionS.createSubscriber(topic);
                    topicSubscriber.setMessageListener(this);
                    topicConnection.start();               

                } catch (JMSException e) {
                            e.printStackTrace();
                }
                
                // retour page de question
                request.setAttribute("identifiant", identifiant);
                request.getRequestDispatcher("/jeu_dirige.jsp").forward(request, response);

            // fin de participation
            } else if(action.equals("finir_participer")){
                message_question = "";
                derniere_question = "";
                try { 
                    topicConnection.stop();
                } catch (JMSException e) {
                     e.printStackTrace();
                }
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                
            // question suivante
            } else if(action.equals("recuperer_question")){
                // réception synchrone
                /*
                try {
                    //Message m = topicSubscriber.receive(100);//NOK
                    //Message m = topicSubscriber.receiveNoWait;//NOK
                    Message m = topicSubscriber.receive(1000);//OK
                    if (m != null && m instanceof TextMessage) { 
                            TextMessage message = (TextMessage) m; 
                            String question = message.getText();
                            response.setContentType("text/xml");
                            response.setHeader("Cache-Control", "no-cache");
                            response.getWriter().write("<message><![CDATA["+question+"]]></message>");
                    } else {
                            String question_vide = "";
                            response.setContentType("text/xml");
                            response.setHeader("Cache-Control", "no-cache");
                            response.getWriter().write("<message><![CDATA["+question_vide+"]]></message>"); 
                    }
                } catch (JMSException e) {
			e.printStackTrace();
                }
                */
                // réception asynchrone : fonctionne, mais message_question est renvoyé à chaque appel
                // (le code ci-dessous dans le onMessage() ne fonctionne pas...
                if(!derniere_question.equals(message_question)){
                    response.setContentType("text/xml");
                    response.setHeader("Cache-Control", "no-cache");
                    response.getWriter().write("<message><![CDATA["+message_question+"]]></message>");  
                    derniere_question = message_question;
                }
                
            // envoi de la réponse
            } else if(action.equals("envoi_reponse")){
                // paramètres
                String numeroQuestion = request.getParameter("numeroQuestion" );
                String idQuestion = request.getParameter("idQuestion" );
                String[] listeValeursTransmises = request.getParameterValues("valeurs" );

                // transmission seulement si des valeurs ont été cochées
                if(numeroQuestion != null && listeValeursTransmises != null){
                    // sauvegarde du choix : récupération du participant ; mise à jour de la liste des réponses
                    Participant participant = m.getParticipantByIdentifiant(identifiant);

                    // getListeResultats > ArrayList
                    Map<String, String[]> resultat = new HashMap<String, String[]>();
                    resultat.put(idQuestion, listeValeursTransmises);
                    participant.getListeResultats().add(resultat);

                    // getListeResultats2 > HashMap
                    participant.getListeResultats2().put(idQuestion, listeValeursTransmises);
                }

                // retour page de question
                request.setAttribute("identifiant", identifiant);
                request.getRequestDispatcher("/jeu_dirige.jsp").forward(request, response);
            }
            
        }
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
        @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
        
        @Override
        public void onMessage(Message message) {  
        
            TextMessage msg = null;
            try {
                if (message instanceof TextMessage) {
                    msg = (TextMessage) message;
                    message_question = msg.getText();
                    /*
                    response.setContentType("text/xml");
                    response.setHeader("Cache-Control", "no-cache");
                    response.getWriter().write("<message><![CDATA["+message_question+"]]></message>"); 
                    * */
                } 
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (Throwable te) {
                te.printStackTrace();
            }
    }
        
 }
