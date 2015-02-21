/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "Reponse")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reponse.findAll", query = "SELECT r FROM Reponse r"),
    @NamedQuery(name = "Reponse.findById", query = "SELECT r FROM Reponse r WHERE r.id = :id"),
    @NamedQuery(name = "Reponse.findByQuestion", query = "SELECT r FROM Reponse r WHERE r.question = :question"),
    @NamedQuery(name = "Reponse.findByTexte", query = "SELECT r FROM Reponse r WHERE r.texte = :texte"),
    @NamedQuery(name = "Reponse.findByValeur", query = "SELECT r FROM Reponse r WHERE r.valeur = :valeur")})
public class Reponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    // plusieurs réponses appartiennent à une question
    @ManyToOne
    @JoinColumn(name="question_id")
    Question question;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "texte")
    private String texte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valeur")
    private Integer valeur;

    public Reponse() {
    }

    public Reponse(Integer id) {
        this.id = id;
    }

    public Reponse(Integer id, Question question, String texte, int valeur) {
        this.id = id;
        this.question = question;
        this.texte = texte;
        this.valeur = valeur;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Integer getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
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
        if (!(object instanceof Reponse)) {
            return false;
        }
        Reponse other = (Reponse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dao.Reponse[ id=" + id + " ]";
    }
    
}
