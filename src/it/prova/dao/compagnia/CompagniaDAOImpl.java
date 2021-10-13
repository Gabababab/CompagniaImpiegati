package it.prova.dao.compagnia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.dao.AbstractMySQLDAO;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;


public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}
	
	@Override
	public List<Compagnia> list() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTmp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {

			while (rs.next()) {
				compagniaTmp = new Compagnia();
				compagniaTmp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTmp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTmp.setDataFondazione(rs.getDate("datafondazione"));
				compagniaTmp.setId(rs.getLong("id"));
				result.add(compagniaTmp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Compagnia get(Long idInput) throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Compagnia result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setRagioneSociale(rs.getString("ragionesociale"));
					result.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					result.setDataFondazione(rs.getDate("datafondazione"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Compagnia input) throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE compagnia SET ragionesociale=?, fatturatoannuo=?, datafondazione=? where id=?;")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO compagnia (ragionesociale, fatturatoannuo, datafondazione) VALUES (?, ?, ?);")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Compagnia input) throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM compagnia WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Compagnia> findByExample(Compagnia input) throws Exception {
		// TODO Auto-generated method stub
		
		if (isNotActive())
				throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

			ArrayList<Compagnia> result = new ArrayList<Compagnia>();
			Compagnia compagniaTmp = null;
			String query = "select * from compagnia where 1=1 ";

			if (input.getRagioneSociale() != null && !input.getRagioneSociale().isEmpty())
				query += "AND ragionesociale LIKE '%" + input.getRagioneSociale() + "%'";

			if (input.getFatturatoAnnuo() != 0)
				query += "AND fatturatoannuo LIKE '%" + input.getFatturatoAnnuo() + "%'";

			if (input.getDataFondazione() != null)
				query += "AND datafondazione='" + new java.sql.Date(input.getDataFondazione().getTime()) + "'";

			try (Statement ps = connection.createStatement()) {
				ResultSet rs = ps.executeQuery(query);

				while (rs.next()) {
					compagniaTmp = new Compagnia();
					compagniaTmp.setRagioneSociale(rs.getString("ragionesociale"));
					compagniaTmp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compagniaTmp.setDataFondazione(rs.getDate("datafondazione"));
					compagniaTmp.setId(rs.getLong("id"));
					result.add(compagniaTmp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			return result;
	}

	

	@Override
	public List<Compagnia> findAllByRagioneSocialeContiene(String input) throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTmp = null;

		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where ragionesociale LIKE '%"+input+"%'")) {

			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					compagniaTmp = new Compagnia();
					compagniaTmp.setRagioneSociale(rs.getString("ragionesociale"));
					compagniaTmp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compagniaTmp.setDataFondazione(rs.getDate("datafondazione"));
					compagniaTmp.setId(rs.getLong("id"));
					result.add(compagniaTmp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(java.util.Date input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTmp = null;

		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato i inner join compagnia c on c.id=i.compagnia_id where i.dataassunzione>?")) {
			ps.setDate(1, new java.sql.Date(input.getTime()));

			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					impiegatoTmp = new Impiegato();
					impiegatoTmp.setNome(rs.getString("nome"));
					impiegatoTmp.setCognome(rs.getString("cognome"));
					impiegatoTmp.setDataAssunzione(rs.getDate("dataassunzione"));
					impiegatoTmp.setDataNascita(rs.getDate("datanascita"));
					impiegatoTmp.setId(rs.getLong("id"));
					impiegatoTmp.setCodiceFiscale(rs.getString("codicefiscale"));
					result.add(impiegatoTmp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}
