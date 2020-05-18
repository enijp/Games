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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.belfius.games.domain.Borrowed_game;
import be.belfius.games.domain.Borrower;
import be.belfius.games.domain.Category;
import be.belfius.games.domain.Game;
import be.belfius.games.repository.*;

public class GamesServices {

	private static Logger logger = LoggerFactory.getLogger(GamesServices.class);

	private GamesRepository gamesRepository = new GamesRepository();

	public void showOneCategoryDetails(int id) {
		List<Category> myList = gamesRepository.selectOneCategoryById(id);
		if (myList.isEmpty())
			showMessageEmptySelection();
		else {
			myList.forEach(System.out::println);
		}
	}

	public void showOneBorrowerDetails(int id) {
		List<Borrower> myList = gamesRepository.selectOneBorrowerById(id);
		if (myList.isEmpty())
			showMessageEmptySelection();
		else {
			myList.forEach((x) -> x.toStringFmtLight());
		}
	}

	public void showAllDetailsGameSelectedByName(String gameName, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		List<Game> myList = gamesRepository.selectGameByName(gameName);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game is correponding to the game name you entered");
		} else {
			System.out.println("-----------------" + "\n" + "Game Détail:");
			myList.forEach((x) -> showAllDetailsGame(x, mapCategory, mapDifficulty));
		}
	}

	public void showAllDetailsGame(Game game, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		System.out.printf("Name: %-45s  Editor: %s / Image: %s\n", game.getGame_name(), game.getEditor().trim(),
				(game.getImage() == null ? "---" : game.getImage().trim()));
		System.out.printf("Age: %s / Players: Min=%2d-Max=%2d / Author: %s / Year Edition:%-4d / Price:%5.2f\n",
				game.getAge().trim(), game.getMin_players(), game.getMax_players(),
				(game.getAuthor() == null ? "unknown" : game.getAuthor().trim()), game.getYear_edition(),
				game.getPrice());
		System.out.printf("Category: %s / Difficulty: %s / Play duration: %s\n",
				mapCategory.get(game.getCategory_id()).trim(), mapDifficulty.get(game.getDifficulty_id()).trim(),
				game.getPlay_duration().trim());
		System.out.println("-----------------------------------------------");
	}

	public void writeGameDataSelectedByNameToFile(String gameName, String myFileName) {
		List<Game> myList = gamesRepository.selectGameByName(gameName);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game is correponding to your selection");
		} else {
			gamesRepository.WriteSelectGamesToFile(myList, myFileName);
		}
	}

	public void showGameSelectedByName(String gameName) {
		displayGameListDetails(gamesRepository.selectGameByName(gameName), "light");
	}

	public void showOneGameDetails(int id) {
		displayGameListDetails(gamesRepository.selectOneGameById(id));
	}

	public void displayGameListDetails(List<Game> myList) {
		displayGameListDetails(myList, "full");
	}

	public void displayGameListDetails(List<Game> myList, String typeDetails) {
		System.out.println("-----------------" + "\n" + "Game Détail:");
		if (myList.isEmpty())
			showMessageEmptySelection();
		else {
			switch (typeDetails) {
			case "light":
				// myList.forEach((x) -> System.out.println(x.toStringFmtLight()));
				// avec ci-dessus, j'ai un problème de format : des données supplémentaires qui
				// s'affichent à la fin du stream !!!
				myList.forEach((x) -> x.toStringFmtLight());
				break;
			case "light2":
				myList.forEach((x) -> x.toStringFmtLight2());
				break;
			default:
				myList.forEach((x) -> System.out.println(x.toString()));
				break;
			}
		}
		System.out.println("-----------------");
	}

	private void showMessageEmptySelection() {
		System.out.println("Sorry, no result is correponding to your selection");
	}

//	public List<Game> makeGameList(ResultSet myResultSet) {
//		List<Game> myGameList = new ArrayList<>();
//		try {
//			while (myResultSet.next()) {
//				Game game = new Game();
//				game.setId(myResultSet.getInt("id"));
//				game.setGame_name(myResultSet.getString("game_name"));
//				game.setEditor(myResultSet.getString("editor"));
//				game.setAuthor(myResultSet.getString("author"));
//				game.setYear_edition(myResultSet.getInt("year_edition"));
//				game.setAge(myResultSet.getString("age"));
//				game.setMin_players(myResultSet.getInt("min_players"));
//				game.setMax_players(myResultSet.getInt("max_players"));
//				game.setCategory_id(myResultSet.getInt("category_id"));
//				game.setPlay_duration(myResultSet.getString("play_duration"));
//				game.setDifficulty_id(myResultSet.getInt("difficulty_id"));
//				game.setPrice(myResultSet.getFloat("price"));
//				game.setImage(myResultSet.getString("image"));
//
//				myGameList.add(game);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return myGameList;
//	}

	public void showAllGamesSortedViaSql() {
		displayGameListDetails(gamesRepository.selectAllGamesSortedOrNot("order by game_name"), "light2");
	}

	public void showAllGamesSortedViaInternal() {
		List<Game> myList = gamesRepository.selectAllGamesSortedOrNot();
		displayGameListDetails(sortGamesList(myList, "byName"), "light2");
	}

	public void showAllGamesWithCategory(HashMap<Integer, String> mapCategory) {
		List<Game> myList = gamesRepository.selectAllGamesSortedOrNot("order by game_name");
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game exists");
		} else {
			myList.forEach(
					(x) -> System.out.printf("%-50s\t%-30s\n", x.getGame_name(), mapCategory.get(x.getCategory_id())));
		}
	}

	public HashMap<Integer, String> fillDifficultyMap() {
		return gamesRepository.selectAllDifficulty();
	}

	public HashMap<Integer, String> fillCategoryMap() {
		return gamesRepository.selectAllCategory();
	}

	public void showAllBorrowersSortedViaSql() {
		List<Borrower> myList = gamesRepository.selectAllBorrowersSortedOrNot("order by borrower_name asc, city asc");
		if (myList.isEmpty())
			showMessageEmptySelection();
		else {
			System.out.printf("%-40s  %-30s  %-40s\n", "Borrower name", "City", "email");
			System.out.printf("%-40s  %-30s  %-40s\n", "------------------------------", "-----------------------",
					"---------------------------");
			myList.forEach(
					(x) -> System.out.printf("%-40s  %-30s  %-40s\n", x.getBorrower_name(), x.getCity(), x.getEmail()));
		}
	}

	public void showAllBorrowedGamesSortedViaSql() {
		listBorrowedGames(
				gamesRepository.selectAllBorrowedGamesSortedOrNot("order by borrower_name asc, borrow_date asc"));
	}

	public void listBorrowedGames(List<Borrowed_game> myList) {
		if (myList.isEmpty())
			showMessageEmptySelection();
		else {
			System.out.printf("%-45s  %-35s  %-12s  %-12s\n", "Game", "Borrower name", "Borrow date", "Return date");
			System.out.printf("%-45s  %-35s  %-12s  %-12s\n", "------------------------------",
					"-----------------------", "-----------", "-----------");
			myList.forEach((x) -> System.out.printf("%-45s  %-35s  %-12s  %-12s\n", x.getGame().getGame_name(),
					x.getBorrower().getBorrower_name(), x.getBorrow().getBorrow_date(),
					(x.getBorrow().getReturn_date() == null ? " " : x.getBorrow().getReturn_date())));
		}
	}

	public void showBorrowedGamesBetween2Dates(Date startDate, Date endDate) {
		String sortClause = "ORDER BY game_name asc";
		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start = myDateFormat.format(startDate);
		String end = myDateFormat.format(endDate);
		String whereClause = "AND (borrow_date BETWEEN '" + start + "'" + " AND '" + end + "')";

		listBorrowedGames(gamesRepository.selectBorrowedGames(sortClause, whereClause));
	}

	public void showAllDetailsBorrowerSelectedByName(String borrowerName) {
		List<Borrower> myList = gamesRepository.selectBorrowerByName(borrowerName);
		if (myList.isEmpty())
			showMessageEmptySelection();
		else {
			System.out.println("-----------------" + "\n" + "Borrower Détail:");
			myList.forEach((x) -> {
				System.out.printf("Name: %-40s  Id:%d\n", x.getBorrower_name(), x.getId());
				System.out.printf("Street: %-30s  House nr:%-5s\n", x.getStreet(), x.getHouse_number());
				System.out.printf("Bus nr: %-5s  PostCode: %6d  City: %-30s\n",
						(x.getBus_number() == null ? "---" : x.getBus_number()), x.getPostcode(), x.getCity());
				System.out.printf("Telephone: %-10s  email: %-40s\n", x.getTelephone(), x.getEmail());
				System.out.println("-----------------------------");
			});
		}
	}

	public void showListOfBorrowerSelectedByName(String borrowerName) {
		List<Borrower> myList = gamesRepository.selectBorrowerByName(borrowerName);
		if (myList.isEmpty())
			showMessageEmptySelection();
		else {
			System.out.println("-----------------" + "\n" + "Borrower List:");
			System.out.printf("%-40s  %-30s  %-10s  %-40s\n", "Borrower name", "City", "Telephone", "e-mail");
			System.out.printf("%-40s  %-30s  %-40s\n", "------------------------------", "-----------------------",
					"----------  --------------------------------");
			myList.forEach((x) -> System.out.printf("%-40s  %-30s  %-10s  %-40s\n", x.getBorrower_name(), x.getCity(),
					(x.getTelephone() == null ? "---" : x.getTelephone()), x.getEmail()));
		}
	}

	public void showAllGamesWithSelectedDifficultyLevel(int difficultyId, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		List<Game> myList = gamesRepository.selectGamesByDifficultyId(difficultyId);
		if (myList.isEmpty()) {
			System.out.println("Sorry, no game is correponding to the difficulty level you entered");
		} else {
			System.out.println(
					"List and details of games of your selection\n" + "-------------------------------------------");
			myList.forEach((x) -> showAllDetailsGame(x, mapCategory, mapDifficulty));
		}

	}

	public void insertNewLevelDifficulty() {
		gamesRepository.insertNewLevelDifficulty("insoluble");
	}

	// ---------------------------------------------
	// ----------------------------------------------
	// Comparator pour le tri des games par titre
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
		default:
			break;
		}
		return myGameList;
	}

}
