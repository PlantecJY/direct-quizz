package dao;

import entities.Participant;
import entities.Utilisateur;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author JYP
 */

@Stateless
public class ParticipantDao {
   
	@EJB ParticipantModel m;
	

	public Participant getParticipantByIdentifiant(String identifiant){
                Map<String,Participant> liste = m.getListeParticipants();
                return liste.get(identifiant);
	}

        public Map<String,Participant> listerParticipants(){
		return m.getListeParticipants();
	}
	
}    
