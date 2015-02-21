package forms;

import dao.ParticipantDao;
import dao.ParticipantModel;
import dao.SequenceDao;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import dao.UtilisateurDao;

import entities.Utilisateur;
import java.util.Date;

import entities.Sequence;
import entities.Participant;
import java.util.ArrayList;
import javax.ejb.EJB;

public final class ParticipantForm {
    private static final String CHAMP_CODE  = "code";
    private static final String CHAMP_PASS   = "motDePasse";
    private static final String CHAMP_IDENTIFIANT   = "identifiant";
    
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();

    private SequenceDao         sequenceDao;
    private ParticipantModel    m;
    
    public ParticipantForm( ParticipantModel m, SequenceDao sequenceDao ) {
        this.sequenceDao = sequenceDao;
        this.m = m;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getMode(String code, String motDePasse) {
        Sequence sequence = sequenceDao.getByCodeAndPassword(code, motDePasse);
        return sequence.getMode();
    }

    public Integer getId(String code, String motDePasse) {
        Sequence sequence = sequenceDao.getByCodeAndPassword(code, motDePasse);
        return sequence.getId();
    }

    public Sequence getSequence(String code, String motDePasse) {
        Sequence sequence = sequenceDao.getByCodeAndPassword(code, motDePasse);
        return sequence;
    }

    public Participant creerParticipant( HttpServletRequest request ) {
        /* Récupération des champs du formulaire */
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String code = getValeurChamp( request, CHAMP_CODE );
        String identifiant = getValeurChamp( request, CHAMP_IDENTIFIANT );
        
        /* Validation du champ code et test séquence ouverte */
        try {
            validationCode( code );
        } catch ( Exception e ) {
            setErreur( CHAMP_CODE, e.getMessage() );
        }

        /* Validation du champ motDePasse. */
        try {
            validationMotDePasse( motDePasse );
        } catch ( Exception e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
        }

        /* Validation de l'identifiant. */
        try {
            validationIdentifiant( identifiant );
        } catch ( Exception e ) {
            setErreur( CHAMP_IDENTIFIANT, e.getMessage() );
        }

        /* Initialisation du résultat global de la validation. */
        Participant participant = new Participant();      
        if ( erreurs.isEmpty() ) {
            // recherche d'une séquence avec ce code et ce password
            Sequence sequence = sequenceDao.getByCodeAndPassword(code, motDePasse);
            if(sequence != null){
                // test si la séquence est ouverte
                Boolean isOpen = m.isOpen(sequence);
                if(isOpen){
                    // id unique
                    int idp = m.getNextId();
                    participant.setIdentifiant(idp+"-"+identifiant);
                    participant.setSequence(sequence);
                    participant.setListeResultats(new ArrayList());
                    participant.setId(idp);
                    m.addParticipant(participant);
                } else {
                    setErreur( "resultat", "Séquence non ouverte.");
                    resultat = "Séquence non ouverte."; 
                    participant = null;
                }

            } else {
                setErreur( "resultat", "Aucune séquence en cours avec ces identifiants.");
                resultat = "Aucune séquence en cours avec ces identifiants.";
                participant = null;
            }
        } else {
            resultat = "Échec de la connexion.";
            participant = null;
        }

        return participant;
    }

    /**
     * Valide le code saisi.
     */
    private void validationCode( String code ) throws Exception {
        if ( code == null ) {
            throw new Exception( "Merci de saisir un code." );
        }
    }

    /**
     * Valide le password saisi.
     */
    private void validationMotDePasse( String motDePasse ) throws Exception {
        if ( motDePasse == null ) {
            throw new Exception( "Merci de saisir un mot de passe." );
        }
    }

    /**
     * Valide le password saisi.
     */
    private void validationIdentifiant( String identifiant ) throws Exception {
        if ( identifiant == null ) {
            throw new Exception( "Merci de saisir un identifiant." );
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