package servlets;

import dao.UtilisateurDao;
import entities.Theme;
import entities.Utilisateur;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.ejb.EJB;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;

//@WebServlet(name = "Membre2", urlPatterns = {"/membre"})
public class Membre2 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public static final String ACCES_PUBLIC     = "/formulaire_connexion.jsp";
    public static final String ACCES_INTERDIT     = "/interdit.jsp";
    public static final String ACCES_RESTREINT  = "/restricted";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    
    // Injected DAO EJB:
    @EJB
    private UtilisateurDao utilisateurDao;

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            // encodage
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            // récupération de la session, le cas échéant
            HttpSession session = request.getSession();
            if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
                // Redirection vers la page de connexion
                response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
            } else {
                // Actions restreintes
                String action = request.getParameter("action");
                // liste des membres
                if(action.equals("lister")){
                    // liste
                    request.setAttribute("listeMembres", utilisateurDao.listerMembres());
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_membres.jsp").forward(request, response);
                } 
                // envoi de mail à tous les membres
                else if(action.equals("envoiTousMembres")){
                    String data = request.getParameter("data");
                    if(data.equals("false")){
                        // affichage du formulaire
                        request.setAttribute( "action", action );
                        RequestDispatcher dispatch = request.getRequestDispatcher(ACCES_RESTREINT + "/formulaire_envoi.jsp"); 
                        dispatch.forward(request, response);
                    } else {
                        String message = StringEscapeUtils.escapeHtml4(request.getParameter("message"));
                        // liste
                        List<Utilisateur> listeMembres = utilisateurDao.listerMembres();

                        // mail
                        for (Integer i=0; i<listeMembres.size(); i++){
                            // le membre
                            Utilisateur membre = listeMembres.get(i);
                            String to = membre.getEmail();
                            // envoi de mail
                            try {
                                String host = "localhost";
                                String from = "plantec@insa-toulouse.fr";
                                String subject = "Message de DirectQuiz";
                                String messageText = message + "<br><a href='http://www.directquiz.fr'>DirectQuiz</a>";
                                boolean sessionDebug = false;

                                // Propriétés et Session
                                Properties props = System.getProperties();
                                props.put("mail.host", host);
                                props.put("mail.transport.protocol", "smtp");
                                Session mailSession = Session.getDefaultInstance(props, null);
                                mailSession.setDebug(sessionDebug);

                                // message MIME
                                Message msg = new MimeMessage(mailSession);
                                msg.setFrom(new InternetAddress(from));
                                InternetAddress[] address = {new InternetAddress(to)};
                                msg.setRecipients(Message.RecipientType.TO, address);
                                msg.setSubject(subject);
                                msg.setSentDate(new Date());
                                msg.setContent(messageText, "text/html");

                                // Envoi
                                Transport.send(msg);                        

                            } catch (Exception e){

                            }
                        }
                        // liste
                        request.setAttribute("listeMembres", utilisateurDao.listerMembres());
                        request.setAttribute("message", "message envoyé à tous les membres");
                        request.getRequestDispatcher(ACCES_RESTREINT + "/liste_membres.jsp").forward(request, response);

                    }
                }
                // validation d'un membre
                else if(action.equals("valider")){
                    // id
                    Integer idMembre = Integer.parseInt(request.getParameter("id"));
                    // validation
                    utilisateurDao.validerMembre(idMembre);
                    // le membre
                    Utilisateur membre = utilisateurDao.getById(idMembre);
                    String to = membre.getEmail();
                    // envoi de mail
                    try {
                        String host = "localhost";
                        String from = "plantec@insa-toulouse.fr";
                        String subject = "DirectQuiz : validation de votre inscription";
                        String messageText = "Bonjour.<br>L'administrateur de DirectQuiz a valid&eacute; votre inscription. Rendez-vous sur la page<br><a href='http://www.directquiz.fr'>DirectQuiz</a>";
                        boolean sessionDebug = false;
                        
                        // Propriétés et Session
                        Properties props = System.getProperties();
                        props.put("mail.host", host);
                        props.put("mail.transport.protocol", "smtp");
                        Session mailSession = Session.getDefaultInstance(props, null);
                        mailSession.setDebug(sessionDebug);
                                                
                        // message MIME
                        Message msg = new MimeMessage(mailSession);
                        msg.setFrom(new InternetAddress(from));
                        InternetAddress[] address = {new InternetAddress(to)};
                        msg.setRecipients(Message.RecipientType.TO, address);
                        msg.setSubject(subject);
                        msg.setSentDate(new Date());
                        msg.setContent(messageText, "text/html");

                        // Envoi
                        Transport.send(msg);                        
                        
                    } catch (Exception e){
                        
                    }
                    // liste
                    request.setAttribute("listeMembres", utilisateurDao.listerMembres());
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_membres.jsp").forward(request, response);
                }
                // Dévalidation d'un membre
                else if(action.equals("devalider")){
                    // id
                    Integer idMembre = Integer.parseInt(request.getParameter("id"));
                    // validation
                    utilisateurDao.devaliderMembre(idMembre);
                    // liste
                    request.setAttribute("listeMembres", utilisateurDao.listerMembres());
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_membres.jsp").forward(request, response);
                }
                // Suppression d'un membre
                else if(action.equals("supprimer")){
                    // id
                    Integer idMembre = Integer.parseInt(request.getParameter("id"));
                    // suppression
                    utilisateurDao.supprimerMembre(idMembre);
                    // liste
                    request.setAttribute("listeMembres", utilisateurDao.listerMembres());
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_membres.jsp").forward(request, response);
                }
                // Rendre un membre gestionnaire
                else if(action.equals("rendreGestionnaire")){
                    // id
                    Integer idMembre = Integer.parseInt(request.getParameter("id"));
                    // action
                    utilisateurDao.rendreGestionnaire(idMembre);
                    // liste
                    request.setAttribute("listeMembres", utilisateurDao.listerMembres());
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_membres.jsp").forward(request, response);
                }
                // Rendre un membre gestionnaire
                else if(action.equals("nePlusRendreGestionnaire")){
                    // id
                    Integer idMembre = Integer.parseInt(request.getParameter("id"));
                    // action
                    utilisateurDao.nePlusRendreGestionnaire(idMembre);
                    // liste
                    request.setAttribute("listeMembres", utilisateurDao.listerMembres());
                    request.getRequestDispatcher(ACCES_RESTREINT + "/liste_membres.jsp").forward(request, response);
                }
            
            }
   }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}