package it.prova.dao.impiegato;

import java.util.Date;
import java.util.List;

import it.prova.dao.IBaseDAO;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;

public interface ImpiegatoDAO extends IBaseDAO<Impiegato> {
	
	public List<Impiegato> findAllByCompagnia(Compagnia input) throws Exception;
	public int countByDataFondazioneCompagniaMaggioreDi(Date input)throws Exception;
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int input)throws Exception;
	public List<Impiegato> findAllErroriAssunzioni()throws Exception;
	

}
