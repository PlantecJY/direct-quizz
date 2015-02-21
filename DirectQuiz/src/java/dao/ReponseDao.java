/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entities.*;

/**
 *
 * @author JYP
 */
@Stateless
public class ReponseDao {


    private static final String JPQL_SELECT_BY_ID = "SELECT r FROM Reponse r WHERE r.id=:id";
    
    // Injection du manager qui s'occupe de la connexion avec la BDD
    @PersistenceContext private EntityManager em;

    // Enregistrement d'une nouvelle réponse
    public void creer( Reponse reponse ) throws DAOException {
        try {
            em.persist( reponse );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Suppression d'une réponse donnée par son id
    public void supprimerReponse( Integer id ) throws DAOException {
        Reponse reponse = getById(id);
        try {
            em.remove( reponse );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Recherche d'un réponse à partir de son id
    public Reponse getById( Integer id ) throws DAOException {
        Reponse reponse = null;
        Query requete = em.createQuery( JPQL_SELECT_BY_ID);
        requete.setParameter( "id", id );
        try {
            reponse = (Reponse) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        em.refresh(reponse);
        return reponse;
    }
        
    
    public List<Reponse> getAllReponses() {
        TypedQuery<Reponse> query = em.createQuery(
            "SELECT r FROM Reponse r ORDER by r.id", Reponse.class);
        return query.getResultList();
    }
}    
