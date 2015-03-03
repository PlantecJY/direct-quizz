package mails;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Classe gérant l'envoi de mail à travers le serveur SMTP.
 *
 * @author mohamedsqualli
 */
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMTPManager {

    public static String mailUtil = "noreply.directquizz@gmail.com";
    public static String passUtil = "quizz123**";

    public static String generate() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // On supprime les lettres dont tu ne veux pas
        String pass = "";
        for (int x = 0; x < 10; x++) {
            int i = (int) Math.floor(Math.random() * 62); // On supprime des lettres en diminuant ce nombre
            pass += chars.charAt(i);
        }
        System.out.println(pass);
        return pass;
    }

    /**
     * Envoi d'une confirmation lors de la création d'un compte à l'adresse
     * "address" avec le mot de passe "mdp"
     */
    public static void sendCreateConfirmation(String adresse, String name, String code) {
        String lien="http://localhost:8080/DirectQuiz/verif?name="+name+"&code="+code;
        String mess = "Bonjour,\n\n" + "Votre compte sur l'application DirectQuizz est en cours de création.\nAfin de terminer le processus de création, veuillez suivre le lien suivant :\n\n"+lien+"\n\nA bientôt sur DirectQuizz : \n\n Note : Ce mail est un mail automatique, veuillez ne pas y répondre. Pour tout problème ou réclamations, adressez vous à un administrateur : mhaidir.ayoub@gmail.com";
        String subject = "Création de votre compte sur DirectQuizz";
        SMTPManager.sendMail(adresse, mess,subject);
    }

    /**
     * Fonction générique pour envoyez un mail à l'adresse "address" dont le
     * contenu est "msg" et le sujet "subject"
     */
    public static void sendMail(String address, String msg, String subject) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailUtil, passUtil);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply.carpooling.sopra@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(address));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fonction utile uniquement pour les tests, n'est absolument pas utilisée
     * lors du déroulement de l'application
     */
    public static void main(String[] args) {
        //SMTPManager.sendMail("squallih@etud.insa-toulouse.fr", "Voici mon message,"
        //        + "\n Hehe cocoo !", "Ca marche !");
        
    }
}
