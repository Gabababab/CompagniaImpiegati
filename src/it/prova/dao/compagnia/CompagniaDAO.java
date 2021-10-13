package it.prova.dao.compagnia;


import java.util.Date;
import java.util.List;

import it.prova.dao.IBaseDAO;
import it.prova.model.*;

public interface CompagniaDAO extends IBaseDAO<Compagnia> {

	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date input) throws Exception;
	public List<Compagnia> findAllByRagioneSocialeContiene(String input)throws Exception;
	
	
}
