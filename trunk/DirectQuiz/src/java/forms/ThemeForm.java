package forms;


import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import entities.*;
import dao.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

public final class ThemeForm {
    // champs utilisés dans le jsp
    private static final String CHAMP_TITRE  = "titre";

    private String              resultat;
    private Map<String, String> erreurs      = new HashMap<String, String>();
    
    private ThemeDao      themeDao;
    
    public ThemeForm( ThemeDao themeDao ) {
        this.themeDao = themeDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Theme creerTheme( HttpServletRequest request, String action , Integer id) throws UnsupportedEncodingException {

        
        String titre = getValeurChamp( request, CHAMP_TITRE );
                
        Theme theme = new Theme();
                
        // utilisateur : récupération de la session depuis la requête
        Utilisateur utilisateur = new Utilisateur();
        HttpSession session = request.getSession();
        utilisateur = (Utilisateur) session.getAttribute("sessionUtilisateur");
        theme.setUtilisateur(utilisateur);
                
        // titre
        try {
            validationTitre( titre );
        } catch ( Exception e ) {
            setErreur( CHAMP_TITRE, e.getMessage() );
        }
        theme.setTitre( titre );
                                
        if ( erreurs.isEmpty() ) {
                if(action.equals("ajouter")){
                    // theme
                    themeDao.creer( theme );
                                        
                    resultat = "Theme créée";
                    
                } else if (action.equals("mettre_a_jour")){
                    // theme : mise à jour des champs (mais pas de la liste de réponses)
                    themeDao.update( titre, id ); 
                    resultat = "Theme mise à jour";
                    
                }
                 
        } else {
            resultat = "";
        }

        return theme;
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
}
