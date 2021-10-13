package it.prova.test;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.prova.connection.MyConnection;
import it.prova.dao.*;
import it.prova.dao.compagnia.CompagniaDAO;
import it.prova.dao.compagnia.CompagniaDAOImpl;
import it.prova.dao.impiegato.ImpiegatoDAO;
import it.prova.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;

public class TestCompagniaImpiegati {
	public static void main(String[] args) {

		CompagniaDAO compagniaDAOInstance = null;
		ImpiegatoDAO impiegatoDAOInstance = null;
		
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			compagniaDAOInstance = new CompagniaDAOImpl(connection);
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);

			System.out.println("--------TEST INSERT--------");
//			Compagnia compagniaDaInserire=new Compagnia("Auchan", 130000, new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-01"));
//			Impiegato impiegatoDaInserire = new Impiegato("giuseppe", "Rossi", "fsdcvhju",
//					new SimpleDateFormat("yyyy-MM-dd").parse("1999-02-01"),
//				new SimpleDateFormat("yyyy-MM-dd").parse("1900-02-01"));
//			compagniaDAOInstance.insert(compagniaDaInserire);
//			impiegatoDaInserire.setCompagnia(compagniaDAOInstance.get(2L));
//			impiegatoDAOInstance.insert(impiegatoDaInserire);
			
			System.out.println("--------TEST LIST--------");
			System.out.println(compagniaDAOInstance.list());
			System.out.println(impiegatoDAOInstance.list());

			//TEST METODI COMPAGNIA
			System.out.println("--------TEST findAllByDataAssunzioneMaggiore--------");
			String dataDaCuiPartire = "2019-01-31";
			Date dateCreatedInput = new SimpleDateFormat("yyyy-MM-dd").parse(dataDaCuiPartire);
			System.out.println(compagniaDAOInstance.findAllByDataAssunzioneMaggioreDi(dateCreatedInput));

			System.out.println("--------TEST findAllByRagioneSociale--------");
			System.out.println(compagniaDAOInstance.findAllByRagioneSocialeContiene("Adidas"));

			//TEST METODI IMPIEGATO
			System.out.println("--------TEST findAllByCompagnia--------");
			System.out.println(impiegatoDAOInstance.findAllByCompagnia(compagniaDAOInstance.get(9L)));
			
			System.out.println("--------TEST countByDataFondazioneCompagniaMaggioreDi--------");
			String dataTmp = "1900-01-31";
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dataTmp);
			System.out.println(impiegatoDAOInstance.countByDataFondazioneCompagniaMaggioreDi(date));
			
			System.out.println("--------TEST findAllByCompagniaConFatturatoMaggioreDi--------");
			System.out.println(impiegatoDAOInstance.findAllByCompagniaConFatturatoMaggioreDi(130));
			
			System.out.println("--------TEST findAllErroriAssunzioni--------");
//			Impiegato impiegatoDaInserire2 = new Impiegato("giuseppe", "Rossi", "fsdcvhju",
//					new SimpleDateFormat("yyyy-MM-dd").parse("1999-02-01"),
//				new SimpleDateFormat("yyyy-MM-dd").parse("1900-02-01"));
//			impiegatoDaInserire2.setCompagnia(compagniaDAOInstance.get(2L));
//			impiegatoDAOInstance.insert(impiegatoDaInserire2);
			System.out.println(impiegatoDAOInstance.findAllErroriAssunzioni());
			

			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
