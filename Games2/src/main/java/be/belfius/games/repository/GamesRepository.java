package be.belfius.games.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import be.belfius.games.domain.Game;

public class GamesRepository {

	public Connection connectToGamesDB(String url, String login, String password, String driver)
			throws ClassNotFoundException {
		// System.out.println("url="+url+"/login="+login+"/driver="+driver);
		Class.forName(driver);
		Connection myConnection = null;
		try {
			myConnection = DriverManager.getConnection(url, login, password);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("problème lors de la connection à la DB Games");
			e.printStackTrace();
		}
		return myConnection;
	}

	public void closeGamesDB(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("problème lors du close de la connection de la DB Games");
			e.printStackTrace();
		}
	}

	public ResultSet selectOneCategoryById(int id, Connection connection) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM Category WHERE id = ?");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			statement.setDouble(1, id);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectOneBorrowerById(int id, Connection connection) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM Borrower WHERE id = ?");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			statement.setDouble(1, id);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectOneGameById(int id, Connection connection) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM Game WHERE id = ?");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			statement.setDouble(1, id);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
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
			//Stream myStream = myList.stream();
			//myStream.forEach(System.out::println);
			//Stream myStream2 = myList.stream();
			//myStream.forEach(System.out::println);
			myList.forEach((x) -> {
				String lineToPrint = String.valueOf(x.getId()) + sep + x.getGame_name() + sep + x.getEditor() + sep + x.getAuthor()
						+ sep + x.getYear_edition() + sep + x.getAge() + sep + x.getMin_players() + sep
						+ x.getMax_players();
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

	public ResultSet selectGameByName(String gameName, Connection connection) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM Game WHERE " + "UPPER(game_name) LIKE UPPER(?)  ");

			// System.out.println("instructionSQL="+statement.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			statement.setString(1, "%" + gameName + "%");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectAllGamesSortedOrNot(Connection connection) {
		return selectAllGamesSortedOrNot(connection, "");
	}

	public ResultSet selectAllGamesSortedOrNot(Connection connection, String orderByClause) {
		PreparedStatement statement = null;
		try {
			statement = connection
					.prepareStatement("SELECT * FROM Game " + (orderByClause.isEmpty() ? "" : " " + orderByClause));
			// System.out.println("instructionSQL="+statement.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectAllCategory(Connection myCon) {
		PreparedStatement statement = null;
		try {
			statement = myCon.prepareStatement("SELECT * FROM Category ");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet selectAllDifficulty(Connection myCon) {
		PreparedStatement statement = null;
		try {
			statement = myCon.prepareStatement("SELECT * FROM Difficulty");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet selectAllBorrowedGamesSortedOrNot(Connection connection, String orderByClause) {
		PreparedStatement statement = null;
		try {
			statement = connection
					.prepareStatement("SELECT t1.game_name, t2.borrower_name, " + "t3.borrow_date, t3.return_date"
							+ " FROM Game t1, Borrower t2, Borrow t3 " + " where t1.id = t3.game_id "
							+ "and t2.id = t3.borrower_id" + (orderByClause.isEmpty() ? "" : " " + orderByClause));
			// System.out.println("instructionSQL="+statement.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet selectBorrowedGames(Connection myCon, String orderByClause, String whereClause) {
		PreparedStatement statement = null;
		try {
			statement = myCon
					.prepareStatement("SELECT t1.game_name, t2.borrower_name, " + "t3.borrow_date, t3.return_date"
							+ " FROM Game t1, Borrower t2, Borrow t3 " + " WHERE t1.id = t3.game_id "
							+ "and t2.id = t3.borrower_id" + (whereClause.isEmpty() ? "" : " " + whereClause)
							+ (orderByClause.isEmpty() ? "" : " " + orderByClause));
			// System.out.println("instructionSQL="+statement.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet selectAllBorrowersSortedOrNot(Connection connection, String orderByClause) {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("SELECT borrower_name, city, email" + " FROM Borrower"
					+ (orderByClause.isEmpty() ? "" : " " + orderByClause));
			// System.out.println("instructionSQL="+statement.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectBorrowerByName(String borrowerName, Connection connection) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM Borrower WHERE "
					+ "UPPER(borrower_name) LIKE UPPER(?)  " + " ORDER BY borrower_name");

			// System.out.println("instructionSQL="+statement.toString());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			statement.setString(1, "%" + borrowerName + "%");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

	public ResultSet selectGamesByDifficultyId(int difficultyId, Connection connection) {
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM Game WHERE difficulty_id = ?");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			statement.setDouble(1, difficultyId);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return resultSet;

	}

}
