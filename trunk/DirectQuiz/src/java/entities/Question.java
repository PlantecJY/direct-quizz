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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Null;

/**
 *
 * @author JYP
 */
@Entity
@Table(name = "Question")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
    @NamedQuery(name = "Question.findById", query = "SELECT q FROM Question q WHERE q.id = :id"),
    @NamedQuery(name = "Question.findByTypeId", query = "SELECT q FROM Question q WHERE q.typeId = :typeId"),
    @NamedQuery(name = "Question.findByUtilisateur", query = "SELECT q FROM Question q WHERE q.utilisateur = :utilisateur"),
    @NamedQuery(name = "Question.findByTitre", query = "SELECT q FROM Question q WHERE q.titre = :titre"),
    @NamedQuery(name = "Question.findByPoints", query = "SELECT q FROM Question q WHERE q.points = :points")})
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "type_id")
    private Integer typeId;
    
    // plusieurs questions possèdent le même un utilisateur ; pas de cascade, car on ne crée pas d'autre utilisateur
    @ManyToOne
    @JoinColumn(name="utilisateur_id")
    Utilisateur utilisateur;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "titre")
    private String titre;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "enonce")
    private String enonce;
    
    @Basic(optional = true)
    @NotNull
    @Column(name = "points")
    private Integer points;
    
    @Basic(optional = true)
    @Column(name = "image")
    private String image;
    
    @Basic(optional = true)
    @Column(name = "imageRealName")
    private String imageRealName;
    

    // une question possède plusieurs réponses
    @OneToMany (cascade = CascadeType.ALL, mappedBy="Question")
    public List<Reponse> reponses ;
    
    // une question est dans plusieurs séquences : fetch = FetchType.LAZY, cascade = CascadeType.ALL
    @ManyToMany
    @JoinTable(name = "Question_Sequence",
    joinColumns = {
        @JoinColumn(name="question_id") 
    },
    inverseJoinColumns = {
        @JoinColumn(name="sequence_id")
    }
    )
    public List<Sequence> sequences ;
    
    // plusieurs questions ont le même thème ; pas de cascade, car on ne crée pas d'autre theme
    @ManyToOne(optional = true)
    @JoinColumn(name="theme_id")
    Theme theme;
    
    public Question() {
    }

    public Question(Integer id) {
        this.id = id;
    }

    public Question(Integer id, Integer typeId, Utilisateur utilisateur, String titre, String enonce, Integer points, Theme theme, String image, String imageRealName) {
        this.id = id;
        this.typeId = typeId;
        this.utilisateur = utilisateur;
        this.titre = titre;
        this.enonce = enonce;
        this.points = points;
        this.theme = theme;
        this.image = image;
        this.imageRealName = imageRealName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getTitre() {
        return titre;
    }

    public String getImage() {
        return image;
    }

    public String getImageRealName() {
        return imageRealName;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageRealName(String image) {
        this.imageRealName = image;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }

    public List<Sequence> getSequences() {
        return sequences;
    }

    public void setSequences(List<Sequence> sequences) {
        this.sequences = sequences;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
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
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dao.Question[ id=" + id + " ]";
    }
    
}
