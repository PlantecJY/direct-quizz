package forms;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import entities.*;
import dao.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringEscapeUtils;

public final class QuestionForm {
    
    // champs utilisés dans le jsp
    private static final String CHAMP_TITRE  = "titre";
    private static final String CHAMP_ENONCE   = "enonce";
    private static final String CHAMP_POINTS   = "points";
    private static final String CHAMP_REPONSES   = "reponses";
    private static final String CHAMP_THEME   = "theme";
    private static final String CHAMP_IMAGE   = "image";
    private static final String CHAMP_IMAGE_OLD   = "image_old";
    private static final String CHAMP_IMAGE_REALNAME_OLD   = "imageRealName_old";
    
    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
    
    private QuestionDao      questionDao;
    private ReponseDao      reponseDao;
    private ThemeDao      themeDao;
    private HttpServletResponse      response;
    
    private final static Logger LOGGER = 
            Logger.getLogger(QuestionForm.class.getCanonicalName());
    
    public QuestionForm( QuestionDao questionDao, ReponseDao reponseDao, ThemeDao themeDao, HttpServletResponse response ) {
        this.questionDao = questionDao;
        this.reponseDao = reponseDao;
        this.themeDao = themeDao;
        this.response = response;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Question creerQuestion( HttpServletRequest request, String action , Integer id) throws UnsupportedEncodingException {

        // paramètres passés en POST
        String titre = getValeurChamp( request, CHAMP_TITRE );
        String enonce = getValeurChamp( request, CHAMP_ENONCE );
        String points = getValeurChamp( request, CHAMP_POINTS );
        String idTh = getValeurChamp( request, CHAMP_THEME );
        // image
        String image_old = getValeurChamp( request, CHAMP_IMAGE_OLD );
        String imageRealName_old = getValeurChamp( request, CHAMP_IMAGE_REALNAME_OLD );
        String image = null;
        Part filePart = null;
        Boolean imgOK = true;
        try {
            filePart = request.getPart("image");
            image = getFileName(filePart);
        } catch (IOException ex) {
            // si une erreur au niveau des répertoires de stockage survient
            setErreur( CHAMP_IMAGE, "Erreur de configuration du serveur." );
        } catch (IllegalStateException e) {
            // si la taille des données dépasse les limites définies dans la section <multipart-config> de web.xml
            setErreur( CHAMP_IMAGE, "Les données envoyées sont trop volumineuses");
            imgOK = false;
        } catch (ServletException ex) {
            // si la requête n'est pas de type multipart/form-data (arrive si piratage)
            setErreur( CHAMP_IMAGE,
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
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
        
        // timeStamp
        java.util.Date date= new java.util.Date();
	System.out.println();
        String timeStamp = "";
        timeStamp = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
        
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
        if(image.equals("") || image == null || !imgOK){
           // pas d'image dans le champ input
           question.setImage(image_old);
           question.setImageRealName(imageRealName_old);
           // pas de changement sur imageRealName (peut rester égal à quelque chose, mais ne sera pas affiché si image = null)
        } else {
            // une image est définie dans le champ input
            try {
                // sauvegarde du fichier
                validationImage( image, filePart, timeStamp );
                question.setImage( image );
                question.setImageRealName(timeStamp+image);
            } catch ( Exception e ) {
                setErreur( CHAMP_IMAGE, e.getMessage() ); 
            }
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
                      
        // traitement des beans
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
            } else if (titre.length() > 50) {
                throw new Exception( "Le titre doit contenir moins de 50 caractères." );
            }
        }
    }
        
    private void validationEnonce( String enonce ) throws Exception {
        if ( enonce == null ) {
            throw new Exception( "L'énoncé ne doit pas être vide." );
        }
    }
    private void validationImage( String fileName, Part filePart, String timeStamp ) throws Exception {
        String extension = null;
        long fileSize = 0;     
        // l'image peut ne pas exister
        if(fileName != null){
            int fileNameSize = fileName.length();
            String fileNameSizeReal = timeStamp+fileName;
            extension = fileName.substring(fileName.lastIndexOf(".")+1, fileNameSize);
            // test sur l'extension
            if ( extension.equals("png") || extension.equals("gif") || extension.equals("jpg") || extension.equals("jpeg") ){
                // test sur la taille
                fileSize = filePart.getSize()/8/100; // en ko
                if ( fileSize < 300 ){ // 300ko - http://mon-ip.awardspace.com/convertisseur.php
                    String path = "/var/webapp/img"; // $cd /var + sudo mkdir webapp + sudo mkdir images // écriture OK avec les droits 777
                    //String path = "/tmp/img2";// visible dans le terminal : $ cd /tmp // écriture OK avec les droits 777
                    OutputStream out = null;
                    InputStream filecontent = null;;
                    try {
                        out = new FileOutputStream(new File(path + File.separator
                            + fileNameSizeReal));
                        filecontent = filePart.getInputStream();
                        int read = 0;
                        final byte[] bytes = new byte[1024];
                        while ((read = filecontent.read(bytes)) != -1) {
                            out.write(bytes, 0, read);
                        }
                        LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", 
                            new Object[]{fileName, path});
                    } catch (FileNotFoundException fne) {
                        LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", 
                        new Object[]{fne.getMessage()});
                        throw new Exception("Le fichier "+ fileName + " n'a pu être chargé. "+ fne.getMessage());
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                        if (filecontent != null) {
                            filecontent.close();
                        }
                    }
                } else {
                    throw new Exception( "La taille de ce fichier ("+fileSize+" ko) est supérieure à la valeur admissible : 300ko");                
                }
            } else {
                throw new Exception( "Le fichier doit avoir l'extension png, gif, jpeg ou jpg.");                
            }
        }
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
