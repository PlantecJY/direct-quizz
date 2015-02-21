package forms;


import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import entities.*;
import dao.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

public final class SequenceForm {
    // champs utilisés dans le jsp
    private static final String CHAMP_CODE  = "code";
    private static final String CHAMP_PASS   = "motDePasse";
    private static final String CHAMP_QUESTIONS   = "questions";
    
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
    
    private SequenceDao      sequenceDao;
    private QuestionDao      questionDao;
    
    public SequenceForm( SequenceDao sequenceDao, QuestionDao questionDao) {
        this.sequenceDao = sequenceDao;
        this.questionDao = questionDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Sequence creerSequence( HttpServletRequest request, String action , Integer id ) {
        String code = getValeurChamp( request, CHAMP_CODE );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
               
        Sequence sequence = new Sequence();
        List<Question> listeQuestions = new ArrayList<Question>();
        sequence.setQuestions(listeQuestions);
        
        // utilisateur : récupération de la session depuis la requête
        Utilisateur utilisateur = new Utilisateur();
        HttpSession session = request.getSession();
        utilisateur = (Utilisateur) session.getAttribute("sessionUtilisateur");
        sequence.setUtilisateur(utilisateur);
        
        // valeurs (contient la liste des n° de réponse)
        String[] listeValeursTransmises = request.getParameterValues("valeurs" );
        if(listeValeursTransmises == null){
            setErreur( CHAMP_QUESTIONS, "Aucune question n'est sélectionnée" );
        } else {
            // recherche des questions sélectionnées
            for (int k = 0; k < listeValeursTransmises.length; k++){
                Integer idQuestion = Integer.parseInt(listeValeursTransmises[k]);
                //Question question = new Question();
                Question question = questionDao.getById(idQuestion);
                // ajout de la séquence courante dans cette question
                //question.getSequences().add(sequence);
                // ajout de la question courante dans cette séquence
                sequence.getQuestions().add(question); 
                // suppression de la question créée
                question = null;
            }
        }

        // code
        try {
            validationCode( code );
        } catch ( Exception e ) {
            setErreur( CHAMP_CODE, e.getMessage() );
        }
        sequence.setCode( code );
        
        // mot de passe
        try {
            validationMotDePasse( motDePasse );
        } catch ( Exception e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
        }
        sequence.setMotDePasse( motDePasse );
        
        // test d'existence dans le cas d'une création
        if(action.equals("ajouter")){
            try {
                validationMotDePasseEtSequence( code, motDePasse );
            } catch ( Exception e ) {
                setErreur( CHAMP_CODE, e.getMessage() );
            }
        }
        
        if ( erreurs.isEmpty() ) {
                /*
                // ajout des questions
            for (int k = 0; k < listeValeursTransmises.length; k++){
                Integer idQuestion = Integer.parseInt(listeValeursTransmises[k]);
                //Question question = new Question();
                Question question = questionDao.getById(idQuestion);
                // ajout de la séquence courante dans cette question
                question.getSequences().add(sequence);
                // ajout de la question courante dans cette séquence
                sequence.getQuestions().add(question); 
                // suppression de la question créée
                question = null;
            }
            * */
                if(action.equals("ajouter")){
                    sequenceDao.creer( sequence );
                    resultat = "Séquence créée";
                } else if (action.equals("mettre_a_jour")){
                    //question.setId(id);
                    sequenceDao.update( sequence, id );
                    resultat = "Séquence mise à jour";
                }
        } else {
            resultat = "";
        }
        
        return sequence;
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    private void validationCode( String code ) throws Exception {
        if ( code == null) {
            throw new Exception( "Le code ne doit pas être vide." );
        } else {
            if (code.length() < 3) {
                throw new Exception( "Le code doit contenir au moins 3 caractères." );
            }
        }
    }

    private void validationMotDePasse( String motDePasse ) throws Exception {
        if ( motDePasse == null) {
            throw new Exception( "Le mot de passe ne doit pas être vide." );
        } else {
            if (motDePasse.length() < 3) {
                throw new Exception( "Le mot de passe doit contenir au moins 3 caractères." );
            }
        }
    }
    
    private void validationMotDePasseEtSequence( String code, String motDePasse ) throws Exception {
        Sequence sequence = sequenceDao.getByCodeAndPassword(code, motDePasse);
        if ( sequence != null) {
            throw new Exception( "Une séquence existe déjà avec ce code et ce mot de passe." );
        }
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
            return valeur.trim();
        }
    }
}
