package it.polito.tdp.rivers.model;

import java.util.List;
import java.util.Map;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {

	private RiversDAO dao;
	private Map<Integer, River> riversIdMap;
	private double occupazioneMedia;
	private Integer nGiorniCritici;
	
	public Map<Integer, River> getRiversIdMap() {
		return riversIdMap;
	}

	public Model() {
		this.dao = new RiversDAO();
		this.riversIdMap = dao.getAllRivers();
	}
	
	public List<Flow> getFlowsRiver(River river){
		return dao.getFlowsRiver(river);
	}
	
	public Double avgFlow(List<Flow> flows) {
		double sum = 0;
		for (Flow f : flows) {
			sum = sum + f.getFlow();
		}
		return sum/flows.size();
	}
	
	public void simula(River river, Integer fattoreDiScala) {
		
		this.nGiorniCritici = 0; //Inizializzo i KPI all'avvio di una nuova simulazione.
		this.occupazioneMedia = 0;
		
		List<Flow> flows = this.getFlowsRiver(river);
		
		Integer k = fattoreDiScala; //Fattore di scala;
		
		//Parametri in ingresso
		Double fMed = this.avgFlow(flows);
		Double Q = k * fMed * 30 * 86400;   //Capienza totale
											//Attenzione a passare da metri_cubi/s a mtri_cubi/giorno.
		Double C = Q/2; //Occupazione iniziale del bacino.
		Double fOutMin = 0.8 * fMed * 86400;
		
		int max = 100; //Parametri utili per gestire l'irrigazione casuale.
        int min = 1;
        int range = max - min + 1;
        
        //Parametri in uscita
        Double occupazioneTotale = C;
        
		for (Flow flow : flows) {
			
			Double fInToday = flow.getFlow() * 86400;
			Double fOutToday;
			Double variazione;
			
	        int rand = (int)(Math.random() * range) + min;
	        
	        if (rand <= 5) {
	        	fOutToday = 10 * fOutMin; //Gestisco l'evento di irrigazione dei campi in giornata.
	        
	        }
	        else {
	        	fOutToday = fOutMin;
	        }
	        
	        variazione = fInToday - fOutToday;
	        
	        if ((C + variazione) > Q) {
	        	C = Q; //Gestione dell'evento di tracimazione: scarico l'eccesso
	        }
	        else if ((C + variazione) < 0) {
	        	this.nGiorniCritici++; //In questo caso non ho abbastanza acqua da mandare in uscita.
	        }
	        else {
	        	C = C + variazione; //Condizione normale in cui posso garantire l'uscita
	        }
	        
	        occupazioneTotale = occupazioneTotale + C;
		}
		
		Double occupazioneMedia = occupazioneTotale / flows.size();
		this.occupazioneMedia = occupazioneMedia;
	}

	public Double getOccupazioneMedia() {
		return occupazioneMedia;
	}

	public Integer getnGiorniCritici() {
		return nGiorniCritici;
	}
	
	
}
