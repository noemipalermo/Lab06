package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	
	List<Citta> allCitta;
	List<Citta> best;
	
	public Model() {
		MeteoDAO meteo = new MeteoDAO();	
		allCitta = meteo.getAllCitta();
	}

	List<Citta> getAllCitta(){
		return allCitta;
	}
	// of course you can change the String output with what you think works best
	public Map<String, Integer> getUmiditaMedia(int mese) {
		MeteoDAO meteo = new MeteoDAO();
		Map<String, Integer> medieUmiditaCitta = new HashMap<>();
		int media;
		int sum= 0;
		List<Rilevamento> rilevamentiLocalitaData = new ArrayList<>();
		for(Citta c: allCitta) {
			rilevamentiLocalitaData = meteo.getAllRilevamentiLocalitaMese(mese, c);
			
			for(Rilevamento r : rilevamentiLocalitaData) {
				sum += r.getUmidita();
			}
			
			media = sum/rilevamentiLocalitaData.size();
			medieUmiditaCitta.put(c.getNome(), media);
		}
		

		
		return medieUmiditaCitta;
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		List<Citta> parziale = new ArrayList<>();
		this.best = null;
		
		MeteoDAO meteo = new MeteoDAO();
		
		for(Citta c: allCitta) { // aggiungo i rilevamenti nell'oggetto città
			c.setRilevamenti(meteo.getAllRilevamentiLocalitaMese(mese, c));
		}
		
		cerca(parziale,0);
		return best;
		
	}
	
	
	public void cerca(List<Citta> parziale, int livello){
		
		//terminazione
		if(livello == NUMERO_GIORNI_TOTALI) {
			Double costo = calcolaCosto(parziale);
			
			if(best == null || costo<calcolaCosto(best)) {
				best = new ArrayList<>(parziale);			
			}
			
		}else {
			
			for(Citta cProva : allCitta) {
				
				if(aggiuntaValida(cProva, parziale)) {
					parziale.add(cProva);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1); // backtracking
				}
			}
		}
	}
	
	
	// Controllo che la città sia stata inserita un numero di volte coerente con le specifiche del problema
	private boolean aggiuntaValida(Citta cProva, List<Citta> parziale) {
	
		int count = 0; // numero di volte in cui ho inserito una città
		for(Citta precedente : parziale) {
			if(precedente.equals(cProva)){
				count++;
			}
		}
		
		
		//se si è visitata una città per 6 gg consecutivi -> false
		if(count>=NUMERO_GIORNI_CITTA_MAX) {
			return false;
		}
		// il primo gionro posso inserire qualsiasi citta
		if(parziale.size()==0) {
			return true;
		}
		// se sono al secondo o terzo giorno non posso cambiare
		//e quindi ritorno true solo se la città che provo a inserire è uguale alla precedente
		if(parziale.size()==1 || parziale.size()==2) {
			return parziale.get(parziale.size()-1).equals(cProva);
		}
		//caso generale : ho passato i primi controlli quindi posso rimanere sempre
		if(parziale.get(parziale.size()-1).equals(cProva)) {
			return true;
		}
		// se la citta cambia verifico che i 3 giorni precedenti non ho cambiato citta
		if(parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) 
				&& parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3))) {
			return true;
		}
		
		return false;
	}

	private Double calcolaCosto(List<Citta> parziale) {
		double costo = 0.0;
		for(int g = 1; g <= NUMERO_GIORNI_TOTALI; g++) {
			
			Citta c = parziale.get(g-1);
			// dalla citta di parziale che sto valutando prendo i rilevamenti del giorno in questione e quindi l'umidita
			double umidita = c.getRilevamenti().get(g-1).getUmidita();
			costo += umidita;
		}
		
		// aggiungo 100 per ogni volta che cambio città
		for(int g=2; g<=NUMERO_GIORNI_TOTALI; g++) {
			if(!parziale.get(g-1).equals(parziale.get(g-2))){
				costo += COST;
			}
		}
		
		return costo;
	}
	
	
	
}
