package forms;


import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import entities.*;
import dao.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringEscapeUtils;


public final class QuestionForm_1 {
    // champs utilisés dans le jsp
    private static final String CHAMP_TITRE  = "titre";
    private static final String CHAMP_ENONCE   = "enonce";
    private static final String CHAMP_POINTS   = "points";
    private static final String CHAMP_REPONSES   = "reponses";
    private static final String CHAMP_THEME   = "theme";
    private static final String CHAMP_IMAGE   = "image";
    
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
    
    private QuestionDao      questionDao;
    private ReponseDao      reponseDao;
    private ThemeDao      themeDao;
    
    private final static Logger LOGGER = 
            Logger.getLogger(QuestionForm.class.getCanonicalName());
    
    public QuestionForm_1( QuestionDao questionDao, ReponseDao reponseDao, ThemeDao themeDao ) {
        this.questionDao = questionDao;
        this.reponseDao = reponseDao;
        this.themeDao = themeDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Question creerQuestion( HttpServletRequest request, String action , Integer id) throws UnsupportedEncodingException {

        
        String titre = getValeurChamp( request, CHAMP_TITRE );
        String enonce = getValeurChamp( request, CHAMP_ENONCE );
        String points = getValeurChamp( request, CHAMP_POINTS );
        String idTh = getValeurChamp( request, CHAMP_THEME );
        String image = getValeurChamp( request, CHAMP_IMAGE );
        final Part filePart;
        try {
            filePart = request.getPart("image");
            final String fileName = getFileName(filePart);
        } catch (IOException ex) {
            Logger.getLogger(QuestionForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            Logger.getLogger(QuestionForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // id du thème
        Integer idTheme = Integer.parseInt(idTh);
        // nombre de réponses
        Integer nbReponses = Integer.parseInt(getValeurChamp( request, "nbReponses" ));
        // valeurs (contient la liste des n° de réponse)
        String[] listeValeursTransmises = request.getParameterValues("valeurs" );
        // réponses
        String[] listeReponses = new String[nbReponses];
        Integer[] listeValeurs = new Integer[nbReponses];
        for (Integer i=0; i<nbReponses; i++){
            // texte de la réponse
            int numeroReponse = i+1;
            listeReponses[i] = StringEscapeUtils.escapeHtml4(getValeurChamp( request, "reponse"+numeroReponse ));
            // initialisation des valeurs
            listeValeurs[i] = 0;
        }
        // valeurs des réponses
        if(listeValeursTransmises != null){
            for (int k = 0; k < listeValeursTransmises.length; k++){ 
                listeValeurs[Integer.parseInt(listeValeursTransmises[k])-1] = 1; 
            }
        } 
        
        Question question = new Question();
        
        // initialisation de la liste de séquences
        /*List<Sequence> listeSequences = new ArrayList<Sequence>();
        question.setSequences(listeSequences);
        question.getSequences().add(null);*/
        
        // utilisateur : récupération de la session depuis la requête
        Utilisateur utilisateur = new Utilisateur();
        HttpSession session = request.getSession();
        utilisateur = (Utilisateur) session.getAttribute("sessionUtilisateur");
        question.setUtilisateur(utilisateur);
                
        // thème si choisi
        Theme theme = null;
        if(idTheme != 0 && idTheme != null){
            theme = themeDao.getById(idTheme);
        }
        question.setTheme(theme);

        // titre
        try {
            validationTitre( titre );
        } catch ( Exception e ) {
            setErreur( CHAMP_TITRE, e.getMessage() );
        }
        question.setTitre( titre );
        
        // enoncé
        try {
            validationEnonce( enonce );
        } catch ( Exception e ) {
            setErreur( CHAMP_ENONCE, e.getMessage() );
        }
        question.setEnonce( enonce );

        // points
        try {
            validationPoints( points );
            question.setPoints( Integer.parseInt(points) );
        } catch ( Exception e ) {
            setErreur( CHAMP_POINTS, e.getMessage() );
            question.setPoints( 0 );
        }
       
        // image
        try {
            validationImage( image );
            question.setImage( image );
        } catch ( Exception e ) {
            //setErreur( CHAMP_IMAGE, e.getMessage() );
            question.setImage(null);
        }

        // initialement type 0 
        question.setTypeId(0);

        // validation des réponses
        try {
            validationReponses(listeReponses);
        } catch ( Exception e ) {
            setErreur( CHAMP_REPONSES, e.getMessage() );
        }
        // réponses
        ArrayList<Reponse> reponses = new ArrayList<Reponse>();
                        
        if ( erreurs.isEmpty() ) {
                if(action.equals("ajouter")){
                    // question
                    questionDao.creer( question );
                    
                    // réponses
                    for (Integer j=0; j<listeReponses.length; j++){
                        // on enlève les réponses avec texte vide
                        if (listeReponses[j] != null){
                            Reponse reponse = new Reponse();
                            reponse.setTexte(listeReponses[j]);
                            reponse.setValeur(listeValeurs[j]);
                            // association de la réponse à la question
                            reponse.setQuestion(question);
                            reponseDao.creer(reponse);
                        }    
                    }
                    
                    resultat = "Question créée";
                    
                } else if (action.equals("mettre_a_jour")){
                    // question : mise à jour des champs (mais pas de la liste de réponses)
                    questionDao.update( question, id, theme );

                    // suppression des anciennes réponses
                    Question old_question = questionDao.getById(id);
                    List<Reponse> old_reponses = old_question.getReponses();
                    for (Integer k=0; k<old_reponses.size(); k++){
                        int old_reponse_id = old_reponses.get(k).getId();
                        reponseDao.supprimerReponse(old_reponse_id);
                    }
                    // nouvelles réponses
                    for (Integer j=0; j<listeReponses.length; j++){
                        // on enlève les réponses avec texte vide
                        if (listeReponses[j] != null){
                            Reponse reponse = new Reponse();
                            reponse.setTexte(listeReponses[j]);
                            reponse.setValeur(listeValeurs[j]);
                            // association de la réponse à la question
                            reponse.setQuestion(questionDao.getById(id));
                            reponseDao.creer(reponse);
                        }    
                    }
 
                    resultat = "Question mise à jour";
                    
                }
                 
        } else {
            resultat = "";
            for (Integer j=0; j<listeReponses.length; j++){
                Reponse reponse = new Reponse();
                reponse.setTexte(listeReponses[j]);
                reponse.setValeur(listeValeurs[j]);
                // association de la réponse à la question
                reponse.setQuestion(question);
                reponses.add(reponse);
            }
            question.setReponses(reponses);
        }

        return question;
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    private void validationTitre( String titre ) throws Exception {
        if ( titre == null) {
            throw new Exception( "Le titre ne doit pas être vide." );
        } else {
            if (titre.length() < 3) {
                throw new Exception( "Le titre doit contenir au moins 3 caractères." );
            }
            if (titre.length() > 50) {
                throw new Exception( "Le titre doit contenir moins de 50 caractères." );
            }
        }
    }
        
    private void validationEnonce( String enonce ) throws Exception {
        if ( enonce == null ) {
            throw new Exception( "L'énoncé ne doit pas être vide." );
        }
    }
    private void validationImage( String image ) throws Exception {
        // l'image peut ne pas exister
    }
    private void validationPoints( String points ) throws Exception {
        if ( points != null && !points.matches( "[0-9]*" ) ) {
            throw new Exception( "Merci de saisir un nombre." );
        }
    }
    private void validationReponses( String[] reponses ) throws Exception {
        Integer nbReponsesNonVides = 0;
         for (Integer j=0; j<reponses.length; j++){
             // réduction du tableau
             if (reponses[j] != null){
                nbReponsesNonVides++; 
                if (reponses[j].length() < 1) {
                    throw new Exception( "La réponse doit contenir au moins 1 caractère." );
                }
                if (reponses[j].length() > 150) {
                    throw new Exception( "La réponse doit contenir moins de 150 caractères." );
                }
             }
        }
        if ( nbReponsesNonVides == 0 ) {
            throw new Exception( "Il n'y a aucune réponse" );
        }
    }
    
    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        // String results = StringEscapeUtils.escapeJava(str);
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur.trim();
        }
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
