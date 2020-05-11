package be.belfius.games.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.belfius.games.domain.Borrower;
import be.belfius.games.domain.Category;
import be.belfius.games.domain.Game;
import be.belfius.games.utils.Database;
import be.belfius.games.utils.Helper;

public class GamesRepository {

	private static Logger logger = LoggerFactory.getLogger(GamesRepository.class);

	public List<Category> selectOneCategoryById(int id) {
		ArrayList<Category> myList = new ArrayList<Category>();
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon.prepareStatement("SELECT * FROM Category WHERE id = ?");
			statement.setDouble(1, id);
			ResultSet myResultSet = statement.executeQuery();
			while (myResultSet.next()) {
				Category category = new Category();
				category.setId(myResultSet.getInt("id"));
				category.setCategory_name(myResultSet.getString("category_name"));
				myList.add(category);
			}
			return myList;
		} catch (SQLException e) {
			logger.error("selectOneCategoryById", e);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public List<Borrower> selectOneBorrowerById(int id) {
		ArrayList<Borrower> myList = new ArrayList<Borrower>();
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon.prepareStatement("SELECT * FROM Borrower WHERE id = ?");
			statement.setDouble(1, id);
			ResultSet myResultSet = statement.executeQuery();
			while (myResultSet.next()) {
				Borrower borrower = new Borrower();
				borrower.setId(myResultSet.getInt("id"));
				borrower.setBorrower_name(myResultSet.getString("borrower_name"));
				borrower.setCity(myResultSet.getString("city"));
				myList.add(borrower);
			}
			return myList;
		} catch (SQLException e) {
			logger.error("selectOneBorrowerById", e);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public List<Game> selectOneGameById(int id) {
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon.prepareStatement("SELECT * FROM Game WHERE id = ?");
			statement.setDouble(1, id);
			return makeGameList(statement.executeQuery());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return Collections.emptyList();
	}

	// ****
	public void WriteSelectGamesToFile(List<Game> myList, String fileName) {
		fileName.concat(".csv");
		System.out.println("nom du fichier output:" + fileName);
		// String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		// System.out.println("extension du fichier="+ext);
		try (BufferedWriter outLine = new BufferedWriter(new FileWriter(fileName))) {
			char sep = ',';
			myList.forEach((x) -> {
				String lineToPrint = String.valueOf(x.getId()) + sep + x.getGame_name() + sep + x.getEditor() + sep
						+ x.getAuthor() + sep + x.getYear_edition() + sep + x.getAge() + sep + x.getMin_players() + sep
						+ x.getMax_players() + x.getCategory_id() + x.getPlay_duration() + x.getDifficulty_id()
						+ x.getPrice() + x.getImage();
				try {
					outLine.write(lineToPrint);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outLine.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// write a .csv file

	}

	public List<Game> selectGameByName(String gameName) {
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon
					.prepareStatement("SELECT * FROM Game WHERE " + "UPPER(game_name) LIKE UPPER(?)  ");
			statement.setString(1, "%" + gameName + "%");
			return makeGameList(statement.executeQuery());
		} catch (SQLException e) {
			logger.error("selectGameByName", e);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public void insertNewLevelDifficulty(String difficulty) {
		PreparedStatement statement = null;
		try {
			Connection myCon;

			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));

			statement = myCon.prepareStatement("INSERT INTO difficulty (id, difficulty_name) "
					// + "values( ((SELECT LAST_INSERT_ID()),?)");
					+ "VALUES ( 96, ?)");
			System.out.println("statement=" + statement);
			statement.setString(1, difficulty);
			System.out.println("statement=" + statement);
			try {
				int nbIns = statement.executeUpdate();
				System.out.println("nbre insert=" + nbIns);
				// myCon.commit();
				System.out.println("nbre insert=" + nbIns);
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List<Game> selectAllGamesSortedOrNot() {
		return selectAllGamesSortedOrNot("");
	}

	public List<Game> selectAllGamesSortedOrNot(String orderByClause) {
		List<Game> myList = null;
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon
					.prepareStatement("SELECT * FROM Game " + (orderByClause.isEmpty() ? "" : " " + orderByClause));
			myList = makeGameList(statement.executeQuery());
		} catch (SQLException e) {
			logger.error("in selectAllGamesSortedOrNot", e);
			e.printStackTrace();
		}
		return myList;

	}

	public List<Game> makeGameList(ResultSet myResultSet) {
		List<Game> myGameList = new ArrayList<>();
		try {
			while (myResultSet.next()) {
				Game game = new Game();
				game.setId(myResultSet.getInt("id"));
				game.setGame_name(myResultSet.getString("game_name"));
				game.setEditor(myResultSet.getString("editor"));
				game.setAuthor(myResultSet.getString("author"));
				game.setYear_edition(myResultSet.getInt("year_edition"));
				game.setAge(myResultSet.getString("age"));
				game.setMin_players(myResultSet.getInt("min_players"));
				game.setMax_players(myResultSet.getInt("max_players"));
				game.setCategory_id(myResultSet.getInt("category_id"));
				game.setPlay_duration(myResultSet.getString("play_duration"));
				game.setDifficulty_id(myResultSet.getInt("difficulty_id"));
				game.setPrice(myResultSet.getFloat("price"));
				game.setImage(myResultSet.getString("image"));
				myGameList.add(game);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myGameList;
	}

	public ResultSet selectAllCategory() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon.prepareStatement("SELECT * FROM Category ");
			resultSet = statement.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectAllDifficulty() {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));

			statement = myCon.prepareStatement("SELECT * FROM Difficulty");
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet selectAllBorrowedGamesSortedOrNot(String orderByClause) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon
					.prepareStatement("SELECT t1.game_name, t2.borrower_name, " + "t3.borrow_date, t3.return_date"
							+ " FROM Game t1, Borrower t2, Borrow t3 " + " where t1.id = t3.game_id "
							+ "and t2.id = t3.borrower_id" + (orderByClause.isEmpty() ? "" : " " + orderByClause));
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet selectBorrowedGames(String orderByClause, String whereClause) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon
					.prepareStatement("SELECT t1.game_name, t2.borrower_name, " + "t3.borrow_date, t3.return_date"
							+ " FROM Game t1, Borrower t2, Borrow t3 " + " WHERE t1.id = t3.game_id "
							+ "and t2.id = t3.borrower_id" + (whereClause.isEmpty() ? "" : " " + whereClause)
							+ (orderByClause.isEmpty() ? "" : " " + orderByClause));
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet selectAllBorrowersSortedOrNot(String orderByClause) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon.prepareStatement("SELECT borrower_name, city, email" + " FROM Borrower"
					+ (orderByClause.isEmpty() ? "" : " " + orderByClause));
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectBorrowerByName(String borrowerName) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon.prepareStatement("SELECT * FROM Borrower WHERE " + "UPPER(borrower_name) LIKE UPPER(?)  "
					+ " ORDER BY borrower_name");
			statement.setString(1, "%" + borrowerName + "%");
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectGamesByDifficultyId(int difficultyId) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon.prepareStatement("SELECT * FROM Game WHERE difficulty_id = ?");
			statement.setDouble(1, difficultyId);
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

}
