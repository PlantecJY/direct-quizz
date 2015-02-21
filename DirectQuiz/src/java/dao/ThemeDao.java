/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.Utilisateur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entities.*;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;

/**
 *
 * @author JYP
 */
@Stateless
public class ThemeDao {


    // Injection du manager qui s'occupe de la connexion avec la BDD
    @PersistenceContext private EntityManager em;

    // Enregistrement d'un nouveau thème
    public void creer( Theme theme ) throws DAOException {
        try {
            //EntityTransaction transac = em.getTransaction();
            //transac.begin();
            em.persist( theme );
            //transac.commit();
            //em.close();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

     // Mise à jour  d'un thème
    public void update( String titre, Integer id ) throws DAOException {
        try {
            Theme theme = em.find(Theme.class, id);
            theme.setTitre(titre);
            em.flush();            
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Lister les thèmes pour l'utilisateur courant
    public List<Theme> listerThemes(Utilisateur utilisateur) throws DAOException {
    	List<Theme> listeThemes = null;
        Query q = em.createQuery("select t from Theme t WHERE t.utilisateur=:utilisateur");
        q.setParameter( "utilisateur", utilisateur );
        try {
        	listeThemes = q.getResultList();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return listeThemes;

    }
    
    // Suppression d'un thème donné par son id
    public void supprimerTheme( Integer id ) throws DAOException {
        Theme theme = getById(id);
        try {
            em.remove( theme );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
    // Recherche d'un thème à partir de son id
    public Theme getById( Integer id ) throws DAOException {
        Theme theme = null;
        Query q = em.createQuery("select t from Theme t WHERE t.id=:id");
        q.setParameter( "id", id );
        try {
            theme = (Theme) q.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return theme;
    }

}    
