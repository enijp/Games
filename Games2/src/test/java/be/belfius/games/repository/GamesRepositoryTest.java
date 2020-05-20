package be.belfius.games.repository;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.Timeout;
import static java.lang.Thread.sleep;
import be.belfius.games.domain.Game;
import be.belfius.games.utils.Database;

public class GamesRepositoryTest {

	private GamesRepository gamesRepository = new GamesRepository();
	private static Connection connection;

	// @Rule
	// public DatabaseRule databaseRule = new DatabaseRule();
	// @Rule
	// public FileWriterRule fileWriterRule = new FileWriterRule();
	// Assert.assertTrue(Files.exists(Paths.get("./test.txt")));
	@Rule
	public Timeout timeoutRule = new Timeout(5000);

	// si on veut se rappeler qu'on n'a pas encore fini le test, on peut provoquer
	// un failure
	// @Test
	// public void testSomething(){
	// Assert.fail("not ready yet");
	// }

	@BeforeClass
	public static void generalSetup() throws SQLException {
		// reset difficulty table
		connection = new Database().createConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("delete FROM Difficulty WHERE id > 10",
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		preparedStatement.execute();
		System.out.println("cleanup difficulty...");
		preparedStatement = connection.prepareStatement("select * FROM Difficulty",
				ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		resultSet.last();
		assertEquals(5, resultSet.getRow());
		
		connection.close();
	}

	@Before
	public void setUp() throws Exception {
		gamesRepository = new GamesRepository();
		connection = new Database().createConnection();
	}

	@After
    public void destroy() throws SQLException {
        connection.close();
    }
	
	@Test
	public void getGames() throws SQLException {
		String gameName = "str";
		List<Game> games = gamesRepository.selectGameByName(gameName);
		assertFalse(games.isEmpty());
		// PreparedStatement preparedStatement =
		// databaseRule.getConnection().prepareStatement(
		PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT * FROM Game WHERE " + "UPPER(game_name) LIKE UPPER(?)  ", ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		preparedStatement.setString(1, "%" + gameName + "%");
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		resultSet.last();
		assertEquals(games.size(), resultSet.getRow());
		assertNotEquals(games.size(), resultSet.getRow());
	}

	// on ajoute un difficulty level pour voir si la liste est différente
	@Test
	public void addNewDifficulty() {
		HashMap<Integer, String> initialDifficulty = gamesRepository.selectAllDifficulty();
		System.out.println(initialDifficulty.toString());

		gamesRepository.insertNewLevelDifficulty("impossible");
		HashMap<Integer, String> updatedDifficulty = gamesRepository.selectAllDifficulty();
		System.out.println(updatedDifficulty.toString());

		assertNotEquals(initialDifficulty, updatedDifficulty);

	}

	@Test
	@Category(Slow.class)
	public void timeout() throws InterruptedException {
		sleep(10000); // just a small trick to let this test run a long time. Not a real
						// implementation.
//		sleep(1000000); // just a small trick to let this test run a long time. Not a real
//		// implementation.
	}

}
