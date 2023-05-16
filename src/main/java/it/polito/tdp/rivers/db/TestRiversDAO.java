package it.polito.tdp.rivers.db;

public class TestRiversDAO {

	public static void main(String[] args) {
//		RiversDAO dao = new RiversDAO();
//		System.out.println(dao.getAllRivers() + "\n" + dao.getFlowsRiver(dao.getAllRivers().get(6)));
		
		int max = 100; //Parametri utili per gestire l'irrigazione casuale.
        int min = 1;
        int range = max - min + 1;
        
        int rand = (int)(Math.random() * range) + min;
        System.out.println(rand);
	}

}
