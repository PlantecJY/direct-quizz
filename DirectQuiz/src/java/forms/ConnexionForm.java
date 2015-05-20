package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import dao.UtilisateurDao;

import entities.Utilisateur;
import static forms.InscriptionForm.chiffrerMdp;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class ConnexionForm {
    private static final String CHAMP_LOGIN  = "login";
    private static final String CHAMP_PASS   = "motDePasse";
    private static final String CHAMP_EMAIL   = "email";

    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();

    private UtilisateurDao      utilisateurDao;

    public ConnexionForm( UtilisateurDao utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Utilisateur connecterUtilisateur( HttpServletRequest request ) {
        /* Récupération des champs du formulaire */
        //String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String login = getValeurChamp( request, CHAMP_LOGIN );
        String email = getValeurChamp( request, CHAMP_EMAIL );
        
        String motDePasse = chiffrerMdp(getValeurChamp(request, CHAMP_PASS), login);
        /* Validation du champ login. */
        try {
            validationLogin( login );
        } catch ( Exception e ) {
            setErreur( CHAMP_LOGIN, e.getMessage() );
        }

        /* Validation du champ mot de passe. */
        try {
            validationUnMotDePasse( motDePasse );
        } catch ( Exception e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
        }
        Utilisateur utilisateur = new Utilisateur();
        if ( erreurs.isEmpty() ) {
            // recherche d'un utilisation validé avec ce login et ce password
            utilisateur = utilisateurDao.getByLoginAndPassword(login, motDePasse);
            if (utilisateur != null){
                // l'utilisateur existe
                if(utilisateur.getValide() == 1){
                    resultat = "Succès de la connexion.";
                } else {
                    resultat = "Utilisateur non encore validé. Merci de suivre le lien que vous avez reçu par mail.";
                }
            } else {
                // l'utilisateur n'existe pas
                resultat = "Pas de membre avec ces identifiants.";
            }
        } else {
            // erreurs dans le formulaire
            resultat = "Échec de la connexion.";
            utilisateur = null;
        }

        return utilisateur;
    }

    public Utilisateur recupererUtilisateur( HttpServletRequest request ) {
        /* Récupération des champs du formulaire */
        String login = getValeurChamp( request, CHAMP_LOGIN );
        String email = getValeurChamp( request, CHAMP_EMAIL );

        /* pas de validation des champs, car l'un ou l'autre peuvent être vides : on teste directement */
        /*
        try {
            validationLogin( login );
        } catch ( Exception e ) {
            setErreur( CHAMP_LOGIN, e.getMessage() );
        }

        try {
            validationEmail( email );
        } catch ( Exception e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        * */
        
        Utilisateur utilisateur = new Utilisateur();
        if ( erreurs.isEmpty() ) {
            // recherche d'un utilisation validé avec ce login
            utilisateur = utilisateurDao.getByLogin(login);
            if (utilisateur != null){
                // l'utilisateur existe
                if(utilisateur.getValide() == 1){
                    sendMailTOUser(utilisateur);
                    resultat = "Un mail vous a été envoyé à l'adresse "+utilisateur.getEmail()+".";
                } else {
                    resultat = "Utilisateur non encore validé. Merci de patienter.";
                    utilisateur = null;
                }
            } else {
                // l'utilisateur n'existe pas avec ce login : on cherche avec cet email
                utilisateur = utilisateurDao.getByEmail(email);
                if (utilisateur != null){
                    // l'utilisateur existe
                    if(utilisateur.getValide() == 1){
                        sendMailTOUser(utilisateur);
                        resultat = "Un mail vous a été envoyé à l'adresse "+utilisateur.getEmail()+".";
                    } else {
                        resultat = "Utilisateur non encore validé. Merci de patienter.";
                        utilisateur = null;
                    }
                } else {
                    // l'utilisateur n'existe pas avec cet email non plus : on cherche avec cet email
                    resultat = "Pas de membre avec cette/ces entrée/entrées.";
                }
            }
        } else {
            // erreurs dans le formulaire
            resultat = "Échec de la connexion.";
            utilisateur = null;
        }

        return utilisateur;
    }

    /* envoi de mail */
    private void sendMailTOUser(Utilisateur membre){
                    String to = membre.getEmail();
                    // envoi de mail
                    try {
                        String host = "localhost";
                        String from = "plantec@insa-toulouse.fr";
                        String subject = "DirectQuiz : récupération d'identifiants";
                        String messageText = "Bonjour.<br>Vos identifiants pour DirectQuiz : <br>"+membre.getLogin()+"<br>"+membre.getMotDePasse()+"<br>Rendez-vous sur la page<br><a href='http://www.directquiz.fr'>DirectQuiz</a>";
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
    
    /**
     * Valide l'adresse email saisie.
     */
    private void validationEmail( String email ) throws Exception {
        if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
            throw new Exception( "Merci de saisir une adresse mail valide." );
        }
    }

    /**
     * Valide le login
     */
    private void validationLogin ( String login ) throws Exception {
        
    }

    /**
     * Valide le mot de passe saisi.
     */
    private void validationUnMotDePasse( String motDePasse ) throws Exception {
        if ( motDePasse != null ) {
            if ( motDePasse.length() < 3 ) {
                throw new Exception( "Le mot de passe doit contenir au moins 3 caractères." );
            }
        } else {
            throw new Exception( "Merci de saisir votre mot de passe." );
        }
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
}
