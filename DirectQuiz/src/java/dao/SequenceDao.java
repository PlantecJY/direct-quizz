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
public class SequenceDao {


    private static final String JPQL_SELECT_BY_ID = "SELECT s FROM Sequence s WHERE s.id=:id";
    private static final String JPQL_SELECT_BY_CODE_AND_PASS = "SELECT s FROM Sequence s WHERE s.code=:code ANd s.motDePasse=:motDePasse";
    
    // Injection du manager qui s'occupe de la connexion avec la BDD
    @PersistenceContext private EntityManager em;

    // Enregistrement d'une nouvelle séquence
    public void creer( Sequence sequence ) throws DAOException {
        try {
            em.persist( sequence );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

     // Mise à jour  d'une séquence : mode
    public void changerMode( Integer id, String mode ) throws DAOException {
        try {
            //Transaction tx = (Transaction) em.getTransaction();
            Sequence sequence = em.find(Sequence.class, id);
            sequence.setMode(mode);
            em.flush();
            //em.getTransaction().commit();
            
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Mise à jour  d'une séquence
    public void update( Sequence sequence, Integer id ) throws DAOException {
        try {
            //Transaction tx = (Transaction) em.getTransaction();
            Sequence old_sequence = em.find(Sequence.class, id);
            old_sequence.setCode(sequence.getCode());
            old_sequence.setMotDePasse(sequence.getMotDePasse());
            old_sequence.setQuestions(sequence.getQuestions());
            em.flush();
            //em.getTransaction().commit();
            
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Suppression d'une séquence donnée par son id
    public void supprimerSequence( Integer id ) throws DAOException {
        Sequence sequence = getById(id);
        try {
            em.remove( sequence );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Recherche d'une séquence à partir de son id
    public Sequence getById( Integer id ) throws DAOException {
        Sequence sequence = null;
        Query requete = em.createQuery( JPQL_SELECT_BY_ID);
        requete.setParameter( "id", id );
        try {
            sequence = (Sequence) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        // rafraîchissement des données de l'entité avec celles contenues dans la base de données
        em.refresh(sequence);
        return sequence;
    }
    
    // Recherche d'une séquence à partir de son code et motDePasse
    public Sequence getByCodeAndPassword(String code, String motDePasse) throws DAOException {
        Sequence sequence = null;
        Query requete = em.createQuery( JPQL_SELECT_BY_CODE_AND_PASS);
        requete.setParameter( "code", code );
        requete.setParameter( "motDePasse", motDePasse );
        try {
            sequence = (Sequence) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return sequence;
    }
    
    // Lister les séquences pour l'utilisateur courant
    public List<Sequence> listerSequences(Utilisateur utilisateur) throws DAOException {
    	List<Sequence> listeSequences = null;
        Query q = em.createQuery("select s from Sequence s WHERE s.utilisateur=:utilisateur ORDER BY s.code");
        q.setParameter( "utilisateur", utilisateur );
        try {
        	listeSequences = q.getResultList();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return listeSequences;

    }

    
    public List<Sequence> getAllSequences() {
        TypedQuery<Sequence> query = em.createQuery(
            "SELECT s FROM Sequence s ORDER BY s.id", Sequence.class);
        return query.getResultList();
    }
}    
