package be.belfius.games.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.belfius.games.domain.Borrow;
import be.belfius.games.domain.Borrowed_game;
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

	public String addElementWithSeparator(String lx, String strToAdd, String sep) {
		return lx.concat(strToAdd + sep);
	}

	static ArrayList<Method> findGetters(Class<?> c, String fName) {
		ArrayList<Method> list = new ArrayList<Method>();
		Method[] methods = c.getDeclaredMethods();
		// System.out.println("search for " + fName);
		for (Method method : methods) {
			// System.out.println("method=" + method.getName());
			if (method.getName().equals("get" + fName))
				list.add(method);
		}
		return list;
	}

	// test to be continued
	// write the values dynamically
//				myList.forEach((x) -> {
//					String lx = "";
//					lx = addElementWithSeparator(lx, String.valueOf(x.getId()), sep);
//					for (Field field2 : field) {
//					String name = field2.getName();
//					System.out.println("processing field=" + name);
//					String nx = name.toUpperCase().substring(0, 1);
//					System.out.println("nx=" + nx);
//					String nn = name.substring(1);
//
//					// find the getters of this field
//					for (Method method : findGetters(Game.class, nx + nn)) {
//						System.out.println("method found=" + method);
//							try {
//								System.out.println("invoke=" + method.invoke(x, ""));
//							} catch (IllegalAccessException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (IllegalArgumentException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (InvocationTargetException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//					}

	public void WriteSelectGamesToFile(List<Game> myList, String fileName) {
		// logger.info("output filename=" + fileName);
		String sep = ";";
		try (BufferedWriter outLine = new BufferedWriter(new FileWriter(fileName))) {
			Field[] field = Game.class.getDeclaredFields();
			String lx1 = "";
			logger.trace("fields of the Game class:");
			for (Field field2 : field) {
				logger.trace("name=" + field2.getName() + " / type=" + field2.getType());
			}
			// write the labels of the fields
			for (Field field2 : field) {
				lx1 = lx1.concat(field2.getName() + sep);
			}
			// write this 1rst lien without the last separator
			outLine.write(lx1.substring(0, lx1.lastIndexOf(sep)));
			outLine.newLine();

			myList.forEach((x) -> {
				String lx = "";
				lx = addElementWithSeparator(lx, String.valueOf(x.getId()), sep);
				lx = addElementWithSeparator(lx, x.getGame_name(), sep);
				lx = addElementWithSeparator(lx, x.getEditor(), sep);
				lx = addElementWithSeparator(lx, x.getAuthor(), sep);
				lx = addElementWithSeparator(lx, String.valueOf(x.getYear_edition()), sep);
				lx = addElementWithSeparator(lx, x.getAge(), sep);
				lx = addElementWithSeparator(lx, String.valueOf(x.getMin_players()), sep);
				lx = addElementWithSeparator(lx, String.valueOf(x.getMax_players()), sep);
				lx = addElementWithSeparator(lx, String.valueOf(x.getCategory_id()), sep);
				lx = addElementWithSeparator(lx, x.getPlay_duration(), sep);
				lx = addElementWithSeparator(lx, String.valueOf(x.getDifficulty_id()), sep);
				lx = addElementWithSeparator(lx, String.valueOf(x.getPrice()), sep);
				lx = addElementWithSeparator(lx, x.getImage(), "");
				try {
					outLine.write(lx);
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				try {
					outLine.newLine();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
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

			statement = myCon.prepareStatement("INSERT INTO difficulty (difficulty_name) "
			// statement = myCon.prepareStatement("INSERT INTO difficulty (id,
			// difficulty_name) "
//					+ "values( ((SELECT LAST_INSERT_ID()),?)");
					+ "values(?)");
			// + "VALUES ( 96, ?)");
			// System.out.println("statement=" + statement);
			statement.setString(1, difficulty);
			// System.out.println("statement=" + statement);
			try {
				int nbIns = statement.executeUpdate();
				// System.out.println("nbre insert=" + nbIns);
				// myCon.commit();
				// System.out.println("nbre insert=" + nbIns);
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
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon
					.prepareStatement("SELECT * FROM Game " + (orderByClause.isEmpty() ? "" : " " + orderByClause));
			return makeGameList(statement.executeQuery());
		} catch (SQLException e) {
			logger.error("in selectAllGamesSortedOrNot", e);
			e.printStackTrace();
		}
		return Collections.emptyList();

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

	@SuppressWarnings("unchecked")
	public HashMap<Integer, String> selectAllCategory() {
		HashMap<Integer, String> mapCategory = new HashMap<Integer, String>();
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon.prepareStatement("SELECT * FROM Category ");
			ResultSet myResultSet = statement.executeQuery();
			while (myResultSet.next()) {
				mapCategory.put(myResultSet.getInt("id"), myResultSet.getString("category_name"));
			}
			logger.trace("hashmap mapCategory loaded");
			mapCategory.forEach((k, v) -> logger.trace("Key = " + k + ", Value = " + v));
			return mapCategory;
		} catch (SQLException e) {
			logger.error("in selectAllCategory", e);
			e.printStackTrace();
		}
		return (HashMap<Integer, String>) Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	public HashMap<Integer, String> selectAllDifficulty() {
		HashMap<Integer, String> mapDifficulty = new HashMap<Integer, String>();
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon.prepareStatement("SELECT * FROM Difficulty ");
			ResultSet myResultSet = statement.executeQuery();
			while (myResultSet.next()) {
				mapDifficulty.put(myResultSet.getInt("id"), myResultSet.getString("difficulty_name"));
			}
			logger.trace("hashmap mapDifficulty loaded");
			mapDifficulty.forEach((k, v) -> logger.trace("Key = " + k + ", Value = " + v));
			return mapDifficulty;
		} catch (SQLException e) {
			logger.error("in selectAllDifficulty", e);
			e.printStackTrace();
		}
		return (HashMap<Integer, String>) Collections.emptyList();
	}

	public List<Borrowed_game> selectAllBorrowedGamesSortedOrNot(String orderByClause) {
		return selectBorrowedGames(orderByClause, " ");
	}

	public List<Borrowed_game> selectBorrowedGames(String orderByClause, String whereClause) {
		ArrayList<Borrowed_game> myList = new ArrayList<Borrowed_game>();
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon
					.prepareStatement("SELECT t1.game_name, t2.borrower_name, " + "t3.borrow_date, t3.return_date"
							+ " FROM Game t1, Borrower t2, Borrow t3 " + " WHERE t1.id = t3.game_id "
							+ "and t2.id = t3.borrower_id" + (whereClause.isEmpty() ? "" : " " + whereClause)
							+ (orderByClause.isEmpty() ? "" : " " + orderByClause));
			ResultSet myResultSet = statement.executeQuery();
			while (myResultSet.next()) {
				Borrowed_game borrowed_game = new Borrowed_game();
				Game game = new Game();
				Borrow borrow = new Borrow();
				Borrower borrower = new Borrower();
				game.setGame_name(myResultSet.getString("game_name"));
				borrower.setBorrower_name(myResultSet.getString("borrower_name"));
				borrow.setBorrow_date(myResultSet.getDate("borrow_date"));
				borrow.setReturn_date(myResultSet.getDate("return_date"));
				borrowed_game.setGame(game);
				borrowed_game.setBorrower(borrower);
				borrowed_game.setBorrow(borrow);
				myList.add(borrowed_game);
			}
			return myList;
		} catch (SQLException e) {
			logger.error("in electAllBorrowedGamesSortedOrNot", e);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public List<Borrower> selectAllBorrowersSortedOrNot(String orderByClause) {
		List<Borrower> myList = new ArrayList<Borrower>();
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon.prepareStatement("SELECT borrower_name, city, email" + " FROM Borrower"
					+ (orderByClause.isEmpty() ? "" : " " + orderByClause));
			ResultSet myResultSet = statement.executeQuery();
			while (myResultSet.next()) {
				Borrower borrower = new Borrower();
				borrower.setBorrower_name(myResultSet.getString("borrower_name"));
				borrower.setCity(myResultSet.getString("city"));
				borrower.setEmail(myResultSet.getString("email"));
				myList.add(borrower);
			}
			return myList;
		} catch (SQLException e) {
			logger.error("in selectAllBorrowedGamesSortedOrNot", e);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public List<Borrower> selectBorrowerByName(String borrowerName) {
		List<Borrower> myList = new ArrayList<Borrower>();
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon.prepareStatement("SELECT * FROM Borrower WHERE "
					+ "UPPER(borrower_name) LIKE UPPER(?)  " + " ORDER BY borrower_name");
			statement.setString(1, "%" + borrowerName + "%");
			ResultSet myResultSet = statement.executeQuery();
			while (myResultSet.next()) {
				Borrower borrower = new Borrower();
				borrower.setId(myResultSet.getInt("id"));
				borrower.setBorrower_name(myResultSet.getString("borrower_name"));
				borrower.setStreet(myResultSet.getString("street"));
				borrower.setHouse_number(myResultSet.getString("house_number"));
				borrower.setBus_number(myResultSet.getString("bus_number"));
				borrower.setPostcode(myResultSet.getInt("postcode"));
				borrower.setCity(myResultSet.getString("city"));
				borrower.setTelephone(myResultSet.getString("telephone"));
				borrower.setEmail(myResultSet.getString("email"));
				myList.add(borrower);
			}
			return myList;
		} catch (SQLException e) {
			logger.error("in selectBorrowerByNamet", e);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public List<Game> selectGamesByDifficultyId(int difficultyId) {
		try (Connection myCon = new Database().createConnection()) {
			PreparedStatement statement = myCon
					.prepareStatement("SELECT * FROM Game WHERE difficulty_id >= ? ORDER BY difficulty_id");
			statement.setDouble(1, difficultyId);
			return makeGameList(statement.executeQuery());
		} catch (SQLException e) {
			logger.error("in selectGamesByDifficultyId", e);
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
