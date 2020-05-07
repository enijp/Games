package be.belfius.games.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import be.belfius.games.domain.Game;
import be.belfius.games.services.Helper;

public class GamesRepository {

//	public Connection connectToGamesDB(String url, String login, String password, String driver)
//			throws ClassNotFoundException {
//		// System.out.println("url="+url+"/login="+login+"/driver="+driver);
//		Class.forName(driver);
//		Connection myConnection = null;
//		try {
//			myConnection = DriverManager.getConnection(url, login, password);
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			System.out.println("problème lors de la connection à la DB Games");
//			e.printStackTrace();
//		}
//		return myConnection;
//	}
//
//	public void closeGamesDB(Connection connection) {
//		try {
//			connection.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			System.out.println("problème lors du close de la connection de la DB Games");
//			e.printStackTrace();
//		}
//	}

	public ResultSet selectOneCategoryById(int id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));

			statement = myCon.prepareStatement("SELECT * FROM Category WHERE id = ?");
			statement.setDouble(1, id);
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectOneBorrowerById(int id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));

			statement = myCon.prepareStatement("SELECT * FROM Borrower WHERE id = ?");
			statement.setDouble(1, id);
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectOneGameById(int id) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon.prepareStatement("SELECT * FROM Game WHERE id = ?");
			statement.setDouble(1, id);
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;
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

	public ResultSet selectGameByName(String gameName) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));

			statement = myCon.prepareStatement("SELECT * FROM Game WHERE " + "UPPER(game_name) LIKE UPPER(?)  ");
			statement.setString(1, "%" + gameName + "%");
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

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
				int nbIns= statement.executeUpdate();
				System.out.println("nbre insert="+ nbIns);
				//myCon.commit();
				System.out.println("nbre insert="+ nbIns);
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public ResultSet selectAllGamesSortedOrNot() {
		return selectAllGamesSortedOrNot("");
	}

	public ResultSet selectAllGamesSortedOrNot(String orderByClause) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection myCon;
		try {
			myCon = DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url"),
					Helper.loadPropertiesFile().getProperty("db.username"),
					Helper.loadPropertiesFile().getProperty("db.password"));
			statement = myCon
					.prepareStatement("SELECT * FROM Game " + (orderByClause.isEmpty() ? "" : " " + orderByClause));
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

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
