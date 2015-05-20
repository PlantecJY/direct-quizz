package forms;

import mails.SMTPManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import entities.Utilisateur;
import dao.UtilisateurDao;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public final class InscriptionForm {

    // champs utilisés dans le jsp
    private static final String CHAMP_EMAIL = "email";
    private static final String CHAMP_PASS = "mot_de_passe";
    private static final String CHAMP_CONF = "confirmation";
    private static final String CHAMP_LOGIN = "login";
    private static final String CHAMP_DATE = "date";

    private String resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();

    private UtilisateurDao utilisateurDao;

    public InscriptionForm(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    /**
     * Retourne le mot de passe chiffré à l'aide de MD5.
     *
     * @param mdp : le mot de passe en clair
     * @param email : l'email de l'utilisateur
     * @return
     */
    public static String chiffrerMdp(String mdp, String login) {
        String password = mdp + "DirectQuiz" + login;
        byte[] uniqueKey = password.getBytes();
        byte[] hash = null;

        try {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("No MD5 support in this VM.");
        }

        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else {
                hashString.append(hex.substring(hex.length() - 2));
            }
        }
        return hashString.toString();
    }

    public Utilisateur inscrireUtilisateur(HttpServletRequest request) {
        String email = getValeurChamp(request, CHAMP_EMAIL);
        String mot_de_passe = getValeurChamp(request, CHAMP_PASS);
        String confirmation = getValeurChamp(request, CHAMP_CONF);
        String login = getValeurChamp(request, CHAMP_LOGIN);
        String mot_de_passeMD5 = chiffrerMdp(mot_de_passe, login);
        String confirmationMD5 = chiffrerMdp(confirmation, login);
        Date date = new java.sql.Date(System.currentTimeMillis());

        Utilisateur utilisateur = new Utilisateur();

        try {
            validationEmail(email);
        } catch (Exception e) {
            setErreur(CHAMP_LOGIN, e.getMessage());
        }
        utilisateur.setEmail(email);

        try {
            validationMotsDePasse(mot_de_passeMD5, confirmationMD5);
        } catch (Exception e) {
            setErreur(CHAMP_PASS, e.getMessage());
            setErreur(CHAMP_CONF, null);
        }
        // la base de données est alimentée avec le mot de passe chiffré
        utilisateur.setMotDePasse(mot_de_passeMD5);

        try {
            validationLogin(login);
        } catch (Exception e) {
            setErreur(CHAMP_LOGIN, e.getMessage());
        }
        utilisateur.setLogin(login);

        // date
        utilisateur.setDateInscription(date);

        // initialement non valide et non gestionnaire
        utilisateur.setValide(0);
        utilisateur.setGestionnaire(0);

        if (erreurs.isEmpty()) {
            String code = SMTPManager.generate();
            utilisateur.setCode(code);
            utilisateurDao.creer(utilisateur);
            SMTPManager.sendCreateConfirmation(utilisateur.getEmail(), utilisateur.getLogin(), code);
            resultat = "Votre demande d'inscription a bien été prise en compte. Vous allez reçevoir un lien de confirmation par e-mail, veuillez le suivre. ";
            /*// envoi de mail à l'admin
             String to = "plantec@insa-toulouse.fr";
             // envoi de mail
             try {
             String host = "localhost";
             String from = "plantec@insa-toulouse.fr";
             String subject = "DirectSondage : nouvelle inscription";
             String messageText = "Bonjour.<br>Une nouvelle inscription vient d'avoir lieu. Rendez-vous sur la page<br><a href='http://www.directquiz.fr'>DirectQuiz</a>";
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
                        
             }*/

        } else {
            resultat = "";
        }

        return utilisateur;
    }

    private void validationEmail(String email) throws Exception {
        if (email != null) {
            if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
                throw new Exception("Merci de saisir une adresse mail valide.");
            }
        } else {
            throw new Exception("Merci de saisir une adresse mail.");
        }
    }

    private void validationMotsDePasse(String motDePasse, String confirmation) throws Exception {
        if (motDePasse != null && confirmation != null) {
            if (!motDePasse.equals(confirmation)) {
                throw new Exception("Les mots de passe entrés sont différents, merci de les saisir à nouveau.");
            } else if (motDePasse.length() < 3) {
                throw new Exception("Les mots de passe doivent contenir au moins 3 caractères.");
            }
        } else {
            throw new Exception("Merci de saisir et confirmer votre mot de passe.");
        }
    }

    private void validationLogin(String login) throws Exception {
        // test sur taille
        if (login != null && login.length() < 3) {
            throw new Exception("Le login d'utilisateur doit contenir au moins 3 caractères.");
        } // vérification de non existence
        else {
            List<Utilisateur> listeMembres = utilisateurDao.listerMembres();
            for (Integer i = 0; i < listeMembres.size(); i++) {
                if (listeMembres.get(i).getLogin().equals(login)) {
                    throw new Exception("Ce login est déjà utilisé.");
                }
            }
        }

    }
    /*
     * Simple initialisation de la propriété dateInscription du bean avec la
     * date courante.
     */

    private void traiterDate(Date date, Utilisateur utilisateur) {
        utilisateur.setDateInscription(date);
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur(String champ, String message) {
        erreurs.put(champ, message);
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
        String valeur = request.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur.trim();
        }
    }
}
