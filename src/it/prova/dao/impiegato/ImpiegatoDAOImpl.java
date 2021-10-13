package it.prova.dao.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.dao.AbstractMySQLDAO;
import it.prova.dao.compagnia.CompagniaDAO;
import it.prova.model.Compagnia;
import it.prova.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Impiegato> list() throws Exception {

		List<Impiegato> result = new ArrayList<>();
		try (Statement s = connection.createStatement();
				ResultSet rs = s
						.executeQuery("select * from impiegato i inner join compagnia c on c.id=i.compagnia_id")) {

			while (rs.next()) {
				Impiegato impiegatoTmp = new Impiegato();
				impiegatoTmp.setNome(rs.getString("nome"));
				impiegatoTmp.setCognome(rs.getString("cognome"));
				impiegatoTmp.setDataAssunzione(rs.getDate("dataassunzione"));
				impiegatoTmp.setDataNascita(rs.getDate("datanascita"));
				impiegatoTmp.setId(rs.getLong("id"));
				impiegatoTmp.setCodiceFiscale(rs.getString("codicefiscale"));

				Compagnia compagniaTmp = new Compagnia();
				compagniaTmp.setRagioneSociale(rs.getString("ragionesociale"));
				compagniaTmp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compagniaTmp.setDataFondazione(rs.getDate("datafondazione"));
				compagniaTmp.setId(rs.getLong("id"));

				impiegatoTmp.setCompagnia(compagniaTmp);
				result.add(impiegatoTmp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setDataAssunzione(rs.getDate("dataassunzione"));
					result.setDataNascita(rs.getDate("datanascita"));
					result.setId(rs.getLong("id"));
					result.setCodiceFiscale(rs.getString("codicefiscale"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Impiegato input) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE impiegato SET nome=?, cognome=?, codicefiscale=?, datanascita=?, dataassunzione=? where id=?;")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO impiegato (nome, cognome, codicefiscale, datanascita, dataassunzione, compagnia_id) VALUES (?, ?, ?, ?, ?, ?);")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getCompagnia().getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTmp = null;
		String query = "select * from impiegato where 1=1 ";

		if (input.getNome() != null && !input.getNome().isEmpty())
			query += "AND nome LIKE '%" + input.getNome() + "%'";

		if (input.getCognome() != null && !input.getCognome().isEmpty())
			query += "AND cognome LIKE '%" + input.getCognome() + "%'";

		if (input.getCodiceFiscale() != null && !input.getCodiceFiscale().isEmpty())
			query += "AND codicefiscale LIKE '%" + input.getCodiceFiscale() + "%'";

		if (input.getDataAssunzione() != null)
			query += "AND dataassunzione='" + new java.sql.Date(input.getDataAssunzione().getTime()) + "'";

		if (input.getDataAssunzione() != null)
			query += "AND dataassunzione='" + new java.sql.Date(input.getDataAssunzione().getTime()) + "'";

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

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
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia input) throws Exception {

		List<Impiegato> result = new ArrayList<>();
		Impiegato impiegatoTmp;

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on c.id=i.compagnia_id where c.id=?")) {
			ps.setLong(1, input.getId());

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

	@Override
	public int countByDataFondazioneCompagniaMaggioreDi(Date input) throws Exception {

		int count = 0;

		try (PreparedStatement ps = connection.prepareStatement(
				"select COUNT(i.id) from compagnia c inner join impiegato i on c.id=i.compagnia_id where c.datafondazione>?")) {
			ps.setDate(1, new java.sql.Date(input.getTime()));

			//Query corretta, l'errore sta dentro il while
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					count++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return count;
	}

	@Override
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int input) throws Exception {

		List<Impiegato> result = new ArrayList<>();
		Impiegato impiegatoTmp;

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on c.id=i.compagnia_id where c.fatturatoannuo>?")) {
			ps.setInt(1, input);

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

	@Override
	public List<Impiegato> findAllErroriAssunzioni() throws Exception {

		List<Impiegato> result = new ArrayList<>();
		Impiegato impiegatoTmp;

		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on c.id=i.compagnia_id where c.datafondazione<i.dataassunzione")) {

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
