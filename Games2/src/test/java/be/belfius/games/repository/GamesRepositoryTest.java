package be.belfius.games.repository;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static java.lang.Thread.sleep;
import be.belfius.games.domain.Game;

public class GamesRepositoryTest {

	private GamesRepository gamesRepository = new GamesRepository();
	@Rule
	public DatabaseRule databaseRule = new DatabaseRule();
	@Rule
	// public FileWriterRule fileWriterRule = new FileWriterRule();
	// @Rule
	public Timeout timeoutRule = new Timeout(5000);

	@Test
	public void getGames() throws SQLException {
		String gameName = "str";
		List<Game> games = gamesRepository.selectGameByName(gameName);
		assertFalse(games.isEmpty());
		PreparedStatement preparedStatement = databaseRule.getConnection().prepareStatement(
				"SELECT * FROM Game WHERE " + "UPPER(game_name) LIKE UPPER(?)  ", ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		preparedStatement.setString(1, "%" + gameName + "%");
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		resultSet.last();
		assertEquals(games.size(), resultSet.getRow());
		assertNotEquals(games.size(), resultSet.getRow());
	}

//	@Test
//	public void timeout() throws InterruptedException {
//		sleep(1000000); // just a small trick to let this test run a long time. Not a real
//						// implementation.
//	}
}
