/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JYP
 */
@Entity
@Table(name = "Sequence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sequence.findAll", query = "SELECT s FROM Sequence s"),
    @NamedQuery(name = "Sequence.findById", query = "SELECT s FROM Sequence s WHERE s.id = :id"),
    @NamedQuery(name = "Sequence.findByUtilisateur", query = "SELECT s FROM Sequence s WHERE s.utilisateur = :utilisateur"),
    @NamedQuery(name = "Sequence.findByCode", query = "SELECT s FROM Sequence s WHERE s.code = :code"),
    @NamedQuery(name = "Sequence.findByMotDePasse", query = "SELECT s FROM Sequence s WHERE s.motDePasse = :motDePasse")})

public class Sequence implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    // plusieurs séquences appartiennent à un utilisateur
    @ManyToOne
    @JoinColumn(name="utilisateur_id")
    Utilisateur utilisateur;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "motDePasse")
    private String motDePasse;
    @Basic(optional = true)
    @Size(min = 1, max = 20)
    @Column(name = "mode")
    private String mode;
    
    // une séquence possède plusieurs questions (pas de CascadeType.ALL sinon création d'une nouvelle question - http://blog.paumard.org/cours/jpa/chap03-entite-relation.html)
    @ManyToMany(cascade=CascadeType.DETACH)
    @JoinTable(name = "Question_Sequence",
    joinColumns = {
    @JoinColumn(name="sequence_id") 
    },
    inverseJoinColumns = {
    @JoinColumn(name="question_id")
    }
    )
    public List<Question> questions ;

    public Sequence() {
    }

    public Sequence(Integer id) {
        this.id = id;
    }

    public Sequence(Integer id, Utilisateur utilisateur, String code, String motDePasse) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.code = code;
        this.motDePasse = motDePasse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getCode() {
        return code;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sequence)) {
            return false;
        }
        Sequence other = (Sequence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Sequence[ id=" + id + " ]";
    }
    
}
