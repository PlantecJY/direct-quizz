package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JYP
 */

public class Participant {

	private String identifiant = new String();
	private Sequence sequence = new Sequence();
        private int id;
        private ArrayList listeResultats = new ArrayList(); 
        private Map<String, String[]> listeResultats2 = new HashMap<String, String[]>(); 
	
	public Participant(){
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Sequence getSequence() {
		return sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

        public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

        public ArrayList getListeResultats() {
		return listeResultats;
	}

	public void setListeResultats(ArrayList listeResultats) {
		this.listeResultats = listeResultats;
	}

        public Map getListeResultats2() {
		return listeResultats2;
	}

	public void setListeResultats2(Map listeResultats2) {
		this.listeResultats2 = listeResultats2;
	}

}
