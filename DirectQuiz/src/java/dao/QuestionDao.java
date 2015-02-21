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
import java.io.File;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;

/**
 *
 * @author JYP
 */
@Stateless
public class QuestionDao {


    // Injection du manager qui s'occupe de la connexion avec la BDD
    @PersistenceContext private EntityManager em;

    // Enregistrement d'une nouvelle question
    public void creer( Question question ) throws DAOException {
        try {
            //EntityTransaction transac = em.getTransaction();
            //transac.begin();
            em.persist( question );
            //transac.commit();
            //em.close();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

     // Mise à jour d'une question
    public void update( Question question, Integer id, Theme theme ) throws DAOException {
        try {
            //Transaction tx = (Transaction) em.getTransaction();
            Question old_question = em.find(Question.class, id);
            em.refresh(old_question);            
            old_question.setTitre(question.getTitre());
            old_question.setEnonce(question.getEnonce());
            old_question.setImage(question.getImage());
            old_question.setImageRealName(question.getImageRealName());
            old_question.setPoints(question.getPoints());
            old_question.setTheme(theme);
            // merge() sinon création d'un nouveau thème...
            em.merge(old_question);
            em.flush();
            //em.getTransaction().commit();
            
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Lister les questions pour l'utilisateur courant (pas utilisé)
    public List<Question> listerQuestions(Utilisateur utilisateur) throws DAOException {
    	List<Question> listeQuestions = null;
        //Query q = em.createQuery("select q from Question q Reponse r WHERE q.utilisateur=:utilisateur AND q.id=:r.question_id");
        Query q = em.createQuery("select q from Question q WHERE q.utilisateur=:utilisateur ORDER BY q.titre");
        q.setParameter( "utilisateur", utilisateur );
        //Query q = em.createQuery("select q from Question q");
        try {
        	listeQuestions = q.getResultList();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return listeQuestions;

    }
    
    // Lister les questions pour l'utilisateur courant
    public List<Question> listerQuestionsParTheme(Utilisateur utilisateur, Theme theme) throws DAOException {
    	List<Question> listeQuestions = null;
        //Query q = em.createQuery("select q from Question q Reponse r WHERE q.utilisateur=:utilisateur AND q.id=:r.question_id");
        Query q = em.createQuery("select q from Question q WHERE q.utilisateur=:utilisateur AND q.theme=:theme");
        q.setParameter( "utilisateur", utilisateur );
        q.setParameter( "theme", theme );
        //Query q = em.createQuery("select q from Question q");
        try {
        	listeQuestions = q.getResultList();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return listeQuestions;

    }

    // Suppression d'une question donnée par son id
    public void supprimerQuestion( Integer id ) throws DAOException {
        Question question = getById(id);
        // supprimer l'image attachée
        try {
            if(question.getImageRealName() != null){
                File file = new File("/var/webapp/img/"+question.getImageRealName());
                boolean success = file.delete();
            }
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        try {
            em.remove( question );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
    // Recherche d'une question à partir de son id
    public Question getById( Integer id ) throws DAOException {
        Question question = null;
        Query q = em.createQuery("select q from Question q WHERE q.id=:id");
        q.setParameter( "id", id );
        try {
            question = (Question) q.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        em.refresh(question);
        return question;
    }

}    
