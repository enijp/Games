package be.belfius.games.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import be.belfius.games.domain.Borrower;
import be.belfius.games.domain.Game;
import be.belfius.games.repository.*;

public class GamesServices {

	private GamesRepository gamesRepository = new GamesRepository();

//	public Connection connectToGamesDB(String url, String login, String password, String driver)
//			throws ClassNotFoundException {
//		return gamesRepository.connectToGamesDB(url, login, password, driver);
//	}
//
//	public void closeGamesDB(Connection connection) {
//		gamesRepository.closeGamesDB(connection);
//	}

	public void showOneCategoryDetails(int id) {
		ResultSet myResultSet = gamesRepository.selectOneCategoryById(id);
		try {
			if (myResultSet.next()) {
				// System.out.println(myResultSet);
				System.out.println("Category Id=" + myResultSet.getInt("id") + "\n" + "Category Name="
						+ myResultSet.getString("category_name"));

			} else {
				System.out.println("Sorry, no Game Category is correponding to the category id you entered");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showOneBorrowerDetails(int id) {
		ResultSet myResultSet = gamesRepository.selectOneBorrowerById(id);
		try {
			if (myResultSet.next()) {
				// System.out.println(myResultSet);
				System.out.println("Borrower Id=" + myResultSet.getInt("id") + "\n" + "name: "
						+ myResultSet.getString("borrower_name") + "\t" + "City: " + myResultSet.getString("city"));

			} else {
				System.out.println("Sorry, no Borrower is correponding to the borrower id you entered");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showAllDetailsGameSelectedByName(String gameName, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		ResultSet myResultSet = gamesRepository.selectGameByName(gameName);
		List<Game> myList = makeGameList(myResultSet);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game is correponding to the game name you entered");
		} else {
			// System.out.println("-----------------" + "\n" + "Game Détail:(before
			// 23/04/2020)");
			// for (Game game : myList) {
			// showAllDetailsGame(game, mapCategory, mapDifficulty);
			// System.out.println("-----------------------------");
			// }
			// (since 23/04/2020)
			System.out.println("-----------------" + "\n" + "Game Détail:");
			myList.forEach((x) -> showAllDetailsGame(x, mapCategory, mapDifficulty));

		}

	}

	public void showAllDetailsGame(Game game, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		System.out.printf("Name: %-50s  Editor:%-50s\n", game.getGame_name(), game.getEditor());
		System.out.printf(" Author: %-40s  Year Edition:%-4d\n", game.getAuthor(), game.getYear_edition());
		System.out.printf(" Age: %-20s  Players: Min=%3d/Max=%3d\n", game.getAge(), game.getMin_players(),
				game.getMax_players());
		System.out.printf(" Category: %-30s  Play duration:%20s\n", mapCategory.get(game.getCategory_id()),
				game.getPlay_duration());
		System.out.printf(" Difficulty: %-30s  Price:%5.2f  Image:%-25s\n", mapDifficulty.get(game.getDifficulty_id()),
				game.getPrice(), game.getImage());
		System.out.println("-----------------------------");
	}

	public void writeGameDataSelectedByNameToFile(String gameName, String myFileName) {
		ResultSet myResultSet = gamesRepository.selectGameByName(gameName);
		List<Game> myList = makeGameList(myResultSet);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game is correponding to your selection");
		} else {
			gamesRepository.WriteSelectGamesToFile(myList, myFileName);
		}
	}

	public void showGameSelectedByName(String gameName) {
		ResultSet myResultSet = gamesRepository.selectGameByName(gameName);
		List<Game> myList = makeGameList(myResultSet);
		displayGameListDetails(myList, "light");
	}

	public void showOneGameDetails(int id) {
		ResultSet myResultSet = gamesRepository.selectOneGameById(id);
		List<Game> myList = makeGameList(myResultSet);
		displayGameListDetails(myList);
	}

	public void displayGameListDetails(List<Game> myList) {
		displayGameListDetails(myList, "full");
	}

	public void displayGameListDetails(List<Game> myList, String typeDetails) {
		System.out.println("-----------------" + "\n" + "Game Détail:");
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game is correponding to your selection");
		} else {
			switch (typeDetails) {
			case "light":
				// System.out.println("detail light");
				// for (Game game : myList) {
				// System.out.println(game.toStringLight());
				// }
				myList.forEach((x) -> System.out.println(x.toStringLight()));

				break;
			case "light2":
				// System.out.println("detail light2");
				// for (Game game : myList) {
				// System.out.println(game.toStringLight2());
				// }

				myList.forEach((x) -> System.out.println(x.toStringLight2()));
				break;
			default:
				// System.out.println("detail default");
				// for (Game game : myList) {
				// System.out.println(game.toString());
				// }
				myList.forEach((x) -> System.out.println(x.toString()));
				break;
			}

		}
		System.out.println("-----------------");
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

	public void showAllGamesSortedViaSql() {
		ResultSet myResultSet = gamesRepository.selectAllGamesSortedOrNot("order by game_name");
		List<Game> myList = makeGameList(myResultSet);
		displayGameListDetails(myList, "light2");

	}

	public void showAllGamesSortedViaInternal() {
		ResultSet myResultSet = gamesRepository.selectAllGamesSortedOrNot();
		List<Game> myList = makeGameList(myResultSet);
		List<Game> mySortedList = sortGamesList(myList, "byName");
		displayGameListDetails(mySortedList, "light2");

	}

	public void showAllGamesWithCategory(HashMap<Integer, String> mapCategory) {
		ResultSet myResultSet = gamesRepository.selectAllGamesSortedOrNot();
		List<Game> myList = makeGameList(myResultSet);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game exists");
		} else {
			for (Game game : myList) {
				System.out.printf("%-50s\t%-30s\n", game.getGame_name(), mapCategory.get(game.getCategory_id()));
			}
		}
	}

	public HashMap<Integer, String> fillDifficultyMap() {
		HashMap<Integer, String> mapDifficulty = new HashMap<Integer, String>();
		ResultSet myResultSet = gamesRepository.selectAllDifficulty();
		try {
			while (myResultSet.next()) {
				mapDifficulty.put(myResultSet.getInt("id"), myResultSet.getString("difficulty_name"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapDifficulty;
	}

	public HashMap<Integer, String> fillCategoryMap() {
		HashMap<Integer, String> mapCategory = new HashMap<Integer, String>();

		ResultSet myResultSet = gamesRepository.selectAllCategory();
		try {
			while (myResultSet.next()) {
				mapCategory.put(myResultSet.getInt("id"), myResultSet.getString("category_name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapCategory;

	}

	public void showAllBorrowedGamesSortedViaSql() {
		ResultSet myResultSet = gamesRepository
				.selectAllBorrowedGamesSortedOrNot("order by borrower_name asc, borrow_date asc");
		try {
			if (myResultSet.next()) {
				System.out.printf("%-50s  %-40s  %-12s  %-12s\n", "Game", "Borrower name", "Borrow date",
						"Return date");
				System.out.printf("%-50s  %-40s  %-16s  %-16s\n", "------------------------------",
						"-----------------------", "-----------", "-----------");
				myResultSet.beforeFirst();
			}
			while (myResultSet.next()) {
				// si date return est vide, indiquer "not yet returned"
				String myReturnDatex = "";
				String myBorrowDatex = "";
				// System.out.println("return date="+myResultSet.getDate("return_date"));
				if (myResultSet.getDate("return_date") == null) {
					myReturnDatex = "not yet returned";
				} else {
					Date myReturnDate = myResultSet.getDate("return_date");
					myReturnDatex = myReturnDate.toString();
				}
				if (myResultSet.getDate("borrow_date") == null) {
					myBorrowDatex = "not yet borrowed";
				} else {
					Date myReturnDate = myResultSet.getDate("borrow_date");
					myBorrowDatex = myReturnDate.toString();
				}
				System.out.printf("%-50s  %-40s  %-16s  %-16s\n", myResultSet.getString("game_name"),
						myResultSet.getString("borrower_name"), myBorrowDatex, myReturnDatex);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Sorry, no Borrowed game found");
			e.printStackTrace();
		}
	}

	public void showAllBorrowersSortedViaSql() {
		ResultSet myResultSet = gamesRepository.selectAllBorrowersSortedOrNot("order by borrower_name asc, city asc");
		try {
			if (myResultSet.next()) {
				System.out.printf("%-40s  %-30s  %-40s\n", "Borrower name", "City", "email");
				System.out.printf("%-40s  %-30s  %-40s\n", "------------------------------", "-----------------------",
						"---------------------------");
				myResultSet.beforeFirst();
			}
			while (myResultSet.next()) {
				System.out.printf("%-40s  %-30s  %-40s\n", myResultSet.getString("borrower_name"),
						myResultSet.getString("city"), myResultSet.getString("email"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Sorry, no Borrowed game found");
			e.printStackTrace();
		}
	}

	public void showBorrowedGamesBetween2Dates(Date startDate, Date endDate) {
		String sortClause = "ORDER BY game_name asc";
		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start = myDateFormat.format(startDate);
		String end = myDateFormat.format(endDate);
		String whereClause = "AND (borrow_date BETWEEN '" + start + "'" + " AND '" + end + "')";

		// System.out.println("where clause=" + whereClause);
		ResultSet myResultSet = gamesRepository.selectBorrowedGames(sortClause, whereClause);
		try {
			if (myResultSet.next()) {
				System.out.printf("%-50s  %-40s  %-12s      %-12s\n", "Game", "Borrower name", "Borrow date",
						"Return date");
				System.out.printf("%-50s  %-40s  %-16s  %-16s\n", "------------------------------",
						"-----------------------", "-----------", "-----------");
				myResultSet.beforeFirst();

				while (myResultSet.next()) {

					String myReturnDatex = (myResultSet.getDate("return_date") == null ? "not yet returned"
							: myResultSet.getDate("return_date").toString());

					String myBorrowDatex = (myResultSet.getDate("borrow_date") == null ? "not yet borrowed"
							: myResultSet.getDate("borrow_date").toString());
					System.out.printf("%-50s  %-40s  %-16s  %-16s\n", myResultSet.getString("game_name"),
							myResultSet.getString("borrower_name"), myBorrowDatex, myReturnDatex);
				}
			} else {
				System.out.println("Sorry, no borrowed game during this period");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showAllDetailsBorrowerSelectedByName(String borrowerName) {
		ResultSet myResultSet = gamesRepository.selectBorrowerByName(borrowerName);
		List<Borrower> myList = makeBorrowerList(myResultSet);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no borrower is correponding to the name you entered");
		} else {
			System.out.println("-----------------" + "\n" + "Borrower Détail:");
			// for (Borrower borrower : myList) {
			// showAllDetailsBorrower(borrower);
			// System.out.println("-----------------------------");
			myList.forEach((x) -> showAllDetailsBorrower(x));
		}

	}

	public void showListOfBorrowerSelectedByName(String borrowerName) {
		ResultSet myResultSet = gamesRepository.selectBorrowerByName(borrowerName);
		List<Borrower> myList = makeBorrowerList(myResultSet);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no borrower is correponding to the name you entered");
		} else {
			System.out.println("-----------------" + "\n" + "Borrower List:");
			System.out.printf("%-40s  %-30s  %-10s  %-40s\n", "Borrower name", "City", "Telephone", "e-mail");
			System.out.printf("%-40s  %-30s  %-40s\n", "------------------------------", "-----------------------",
					"----------  --------------------------------");
			// for (Borrower borrower : myList) {
			// show4ColumnsBorrower(borrower);
			myList.forEach((x) -> show4ColumnsBorrower(x));
		}
	}

	public void showAllDetailsBorrower(Borrower borrower) {
		System.out.printf("Name: %-40s  Id:%d\n", borrower.getBorrower_name(), borrower.getId());
		System.out.printf("Street: %-30s  House nr:%-5s\n", borrower.getStreet(), borrower.getHouse_number());
		System.out.printf("Bus nr: %-5s  PostCode: %6d  City: %30s\n",
				(borrower.getBus_number() == null ? "---" : borrower.getBus_number()), borrower.getPostcode(),
				borrower.getCity());
		System.out.printf("Telephone: %-10s  email: %-401s\n", borrower.getTelephone(), borrower.getEmail());
		System.out.println("-----------------------------");
	}

	public void show4ColumnsBorrower(Borrower borrower) {
		System.out.printf("%-40s  %-30s  %-10s  %-40s\n", borrower.getBorrower_name(), borrower.getCity(),
				(borrower.getTelephone() == null ? "---" : borrower.getTelephone()), borrower.getEmail());
	}

	public List<Borrower> makeBorrowerList(ResultSet myResultSet) {
		List<Borrower> myBorrowerList = new ArrayList<>();
		try {
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

				myBorrowerList.add(borrower);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myBorrowerList;
	}

	public void showAllGamesWithSelectedDifficultyLevel(int difficultyId, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		ResultSet myResultSet = gamesRepository.selectGamesByDifficultyId(difficultyId);
		List<Game> myList = makeGameList(myResultSet);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game is correponding to the difficulty level you entered");
		} else {
			System.out.println("----------------------------" + "\n"
					+ "List and details of games with the difficulty level <" + mapDifficulty.get(difficultyId) + ">\n"
					+ "---------------------------------------------------");
			// for (Game game : myList) {
			// showAllDetailsGame(game, mapCategory, mapDifficulty);
			myList.forEach((x) -> showAllDetailsGame(x, mapCategory, mapDifficulty));
		}

	}

	public void insertNewLevelDifficulty() {
		gamesRepository.insertNewLevelDifficulty("insoluble");
	}
	
	
	
	
	// ---------------------------------------------
	// ----------------------------------------------
	// Comparator pour le tri des media par titre
	public static Comparator<Game> ComparatorName = new Comparator<Game>() {

		@Override
		public int compare(Game m1, Game m2) {
			// si titre identique, trier sur editor
			if (m1.getGame_name().compareTo(m2.getGame_name()) == 0) {
				// alors on compare les noms des editeurs
				return m1.getEditor().compareTo(m2.getEditor());
			} else {
				// les noms sont différents : tri sur cette zone
				return m1.getGame_name().compareTo(m2.getGame_name());
			}
		}
	};

	public List<Game> sortGamesList(List<Game> myList, String sortedBy) {
		List<Game> myGameList = new ArrayList<>(myList);
		// myMediaList = medias;
		switch (sortedBy) {
		case "byName":
			Collections.sort(myGameList, ComparatorName);
			break;
		/*
		 * case "bySinger": Collections.sort(myMediaList, ComparatorSinger); break; case
		 * "byMediaType": Collections.sort(myMediaList, ComparatorMediaType); break;
		 * case "byPrice": Collections.sort(myMediaList, ComparatorMediaPrice); break;
		 */
		default:
			break;
		}
		return myGameList;
	}

}
