package dao;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import entities.Participant;
import entities.Sequence;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.Startup;

// singleton initialisé au début par le container EJB
@Startup
@Singleton
public class ParticipantModel {
	
        // liste des participants : map clé = identifiant, valeur = objet participant
	public static Map<String, Participant> listeParticipants = new HashMap<String, Participant>();
	public static Map<Integer, Sequence> listeSequencesOuvertes = new HashMap<Integer, Sequence>();
	public static int id =0;
	
        public ParticipantModel(){
	}
	
	public Map<String, Participant> getListeParticipants() {
		return listeParticipants;
	}

	public Map<String, Participant> getListeParticipantsSequence(Integer id) {
                Map<String, Participant> liste = new HashMap<String, Participant>();
                for (Map.Entry<String, Participant> entry : listeParticipants.entrySet()) {
                    if(entry.getValue().getSequence().getId() == id){
                        liste.put(entry.getKey(), entry.getValue());
                    }
                }
		return liste;
	}

        public void addParticipant(Participant participant) {
		listeParticipants.put(participant.getIdentifiant(), participant);
	}

        public void setListeParticipants(Map<String, Participant> c) {
		this.listeParticipants = c;
	}
	
	public int getNextId(){
		return id++;
	}
	
	public Participant getParticipantByIdentifiant(String identifiant){
                return listeParticipants.get(identifiant);
	}
        
	public void removeParticipantsForSequence(int id) {
            // Iterator.remove is the only safe way to modify a collection during iteration
            Iterator iterator = listeParticipants.entrySet().iterator();
            while (iterator.hasNext()) {
		Map.Entry entry = (Map.Entry) iterator.next();
                Participant participant = (Participant) entry.getValue();
		if(participant.getSequence().getId() == id){
                        iterator.remove();
                }
            }   

	}
        
        public void addSequence(Sequence sequence) {
		listeSequencesOuvertes.put(sequence.getId(), sequence);
	}

        public Boolean isOpen(Sequence sequence) {
            Boolean resultat = false;
            // test d'existence
            if(listeSequencesOuvertes.get(sequence.getId()) != null){
                    resultat = true;
            }
            return resultat;
	}

        public void removeSequence(Sequence sequence) {
            listeSequencesOuvertes.remove(sequence.getId());
	}
}
