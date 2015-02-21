package dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.Utilisateur;
import javax.persistence.TypedQuery;

@Stateless
public class UtilisateurDao {
    private static final String JPQL_SELECT_BY_ID = "SELECT u FROM Utilisateur u WHERE u.id=:id";
    private static final String JPQL_SELECT_BY_EMAIL = "SELECT u FROM Utilisateur u WHERE u.email=:email";
    private static final String JPQL_SELECT_BY_LOGIN_AND_PASS = "SELECT u FROM Utilisateur u WHERE u.login=:login AND u.motDePasse=:motDePasse";
    private static final String JPQL_SELECT_BY_LOGIN = "SELECT u FROM Utilisateur u WHERE u.login=:login";

    // Injection du manager qui s'occupe de la connexion avec la BDD
    @PersistenceContext private EntityManager em;
    
    // utile sur les formats de sortie : http://weblogs.java.net/blog/mb124283/archive/2007/04/java_persistenc.html

    // Enregistrement d'un nouvel utilisateur
    public void creer( Utilisateur utilisateur ) throws DAOException {
        try {
            em.persist( utilisateur );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Suppression d'un membre donné par son id
    public void supprimerMembre( Integer id ) throws DAOException {
        Utilisateur utilisateur = getById(id);
        try {
            em.remove( utilisateur );
            em.flush();
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    // Validation d'un membre donné par son id
    public void validerMembre( Integer id ) {
        Utilisateur utilisateur = getById(id);
        utilisateur.setValide(1);
    }


    // Dévalidation d'un membre donné par son id
    public void devaliderMembre( Integer id ) {
        Utilisateur utilisateur = getById(id);
        utilisateur.setValide(0);
    }
    
    // Rendre gestionnaire un membre donné par son id 
    public void rendreGestionnaire( Integer id ) {
        Utilisateur utilisateur = getById(id);
        utilisateur.setGestionnaire(1);
    }

    // Ne plus rendre gestionnaire un membre donné par son id 
    public void nePlusRendreGestionnaire( Integer id ) {
        Utilisateur utilisateur = getById(id);
        utilisateur.setGestionnaire(0);
    }

    // Recherche d'un utilisateur à partir de son id
    public Utilisateur getById( Integer id ) throws DAOException {
        Utilisateur utilisateur = null;
        Query requete = em.createQuery( JPQL_SELECT_BY_ID);
        requete.setParameter( "id", id );
        try {
            utilisateur = (Utilisateur) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return utilisateur;
    }

    // Recherche d'un utilisateur à partir de son login et de son pass
    public Utilisateur getByLoginAndPassword( String login, String mot_de_passe ) throws DAOException {
        Utilisateur utilisateur = null;
        Query requete = em.createQuery( JPQL_SELECT_BY_LOGIN_AND_PASS );
        // paramètres (attention : les attributs du bean vis-vis de JPA, donc motDePasse)
        requete.setParameter( "login", login );
        requete.setParameter( "motDePasse", mot_de_passe );
        try {
            utilisateur = (Utilisateur) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return utilisateur;
    }
    
    // Recherche d'un utilisateur à partir de son login
    public Utilisateur getByLogin( String login ) throws DAOException {
        Utilisateur utilisateur = null;
        Query requete = em.createQuery( JPQL_SELECT_BY_LOGIN );
        // paramètres (attention : les attributs du bean vis-vis de JPA, donc motDePasse)
        requete.setParameter( "login", login );
        try {
            utilisateur = (Utilisateur) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return utilisateur;
    }

    // Recherche d'un utilisateur à partir de son login
    public Utilisateur getByEmail( String email ) throws DAOException {
        Utilisateur utilisateur = null;
        Query requete = em.createQuery( JPQL_SELECT_BY_EMAIL );
        // paramètres (attention : les attributs du bean vis-vis de JPA, donc motDePasse)
        requete.setParameter( "email", email );
        try {
            utilisateur = (Utilisateur) requete.getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return utilisateur;
    }

    // Lister les membres (on exclut le superadmin)
    public List<Utilisateur> listerMembres() throws DAOException {
    	List<Utilisateur> listeMembres = null;
        Query q = em.createQuery("select u from Utilisateur u WHERE u.login NOT IN ('superadmin') ORDER BY u.login");
        try {
        	listeMembres = q.getResultList();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
        return listeMembres;

    }
    
    
    public List<Utilisateur> getAllMembres() {
        TypedQuery<Utilisateur> query = em.createQuery(
            "SELECT u FROM Utilisateur u ORDER BY u.id", Utilisateur.class);
        return query.getResultList();
    }
}    
