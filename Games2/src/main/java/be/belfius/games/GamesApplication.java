package be.belfius.games;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

//import org.apache.logging.log4j.core.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;
//import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import be.belfius.games.exceptions.inputNumericException;
import be.belfius.games.exceptions.inputStringException;
import be.belfius.games.services.GamesServices;
import be.belfius.games.services.Helper;

public class GamesApplication {

	static Logger logger = LoggerFactory.getLogger(GamesApplication.class);

	private Scanner scanner = new Scanner(System.in);
	private GamesServices gamesServices = new GamesServices();
	private static HashMap<Integer, String> mapCategory;
	private static HashMap<Integer, String> mapDifficulty;
	
	static String defaultFolder = "";
	static String defaultOutputFileName = "";

	public static void main(String[] args) {
		GamesApplication myApp = new GamesApplication();
		System.setProperty("org.slf4j.simpleLogger.showDateTime", "true"); // Use this setting to show the date and time
		System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "yyyy-MM-dd HH:mm:ss"); // Use this setting to
																							// format te date and time

		logger.info("GamesApplication starts!");

		// read properties
		logger.trace("reading properties");
		Properties prop = Helper.loadPropertiesFile();
		prop.forEach((k, v) -> System.out.println("key=" + k + " ; value=" + v));

		System.out.println("default directory=" + prop.get("fi.defaultOutputFolder"));
		System.out.println("default output filename=" + prop.get("fi.defaultOutputFileName"));

		logger.info("running with properties for <" + prop.get("exec.env") + "> environment");

		defaultFolder = prop.get("fi.defaultOutputFolder").toString();
		defaultOutputFileName = prop.get("fi.defaultOutputFileName").toString();
		logger.trace("default folder=" + defaultFolder);
		logger.trace("default output filename=" + defaultOutputFileName);

		//String url = "jdbc:mysql://localhost:3306/games";
		//String login = "root";
		//String password = "";
		//String driver = "com.mysql.jdbc.Driver";
		//Connection myCon = myApp.connectToGamesDB(url, login, password, driver);

		// fill the map with category table
		mapCategory = myApp.fillCategoryMap();
		logger.trace("hashmap mapCategory loaded");
		mapCategory.forEach((k, v) -> System.out.println("Key = " + k + ", Value = " + v));

		// fill the map with difficulty table
		mapDifficulty = myApp.fillDifficultyMap();
		logger.trace("hashmap mapDifficulty loaded");
		mapDifficulty.forEach((k, v) -> System.out.println("Key = " + k + ", Value = " + v));
		System.out.println("");
		
		boolean loop = true;
		while (loop) {

			myApp.showMenu0();
			String choice = myApp.askForChoice();
			switch (choice) {
			case "1":
				try {
					myApp.showOneCategoryDetails(myApp.askForCategoryId());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			case "2":
				try {
					myApp.showOneGameDetails(myApp.askForGameId());
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println(e.getMessage());
				}

				break;
			case "3":
				try {
					myApp.showOneBorrowerDetails(myApp.askForBorrowerId());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					// e.printStackTrace();
				}

				break;
			case "4":
				try {
					myApp.showGameSelectedByName(myApp.askForGameName());
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case "5":
				// d'abord via le tri SQL
				System.out.println("1) sorted via SQL");
				myApp.showAllGamesSortedViaSql();
				// ensuite via le tri sur les games de la class Game
				System.out.println("2) sorted via internal sort");
				myApp.showAllGamesSortedViaInternal();
				break;
			case "6":
				// list of all games with category
				System.out.println("All games List");
				System.out.println("--------------");
				myApp.showAllGamesWithCategory(mapCategory);
				try {
					myApp.showAllDetailsGameSelectedByName(myApp.askForGameName(), mapCategory, mapDifficulty);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "7":// borrowed games
				boolean loopOption7 = true;
				while (loopOption7) {
					myApp.showMenuPart7();
					String choice7 = myApp.askForChoice();
					switch (choice7) {
					case "1":// liste de tous les borrowed games
						System.out.println("All Borrowed Games");
						System.out.println("------------------");
						myApp.showAllBorrowedGamesSortedViaSql();
						break;
					case "2":// liste de tous les borrowers
						System.out.println("List of all Borrowers");
						System.out.println("---------------------");
						myApp.showAllBorrowersSortedViaSql();
						break;
					case "3":// introduire le borrower name et donner la liste des
						// games borrowed pour ce borrower name
						try {
							myApp.showAllDetailsBorrowerSelectedByName(myApp.askForBorrowerName());
						} catch (inputStringException e) {
							// nothing to do, just return to the borrower menu
							// e.pprintStackTrace();
							continue;
						}
						break;
					case "X":
					case "x":
						loopOption7 = false;
						break;
					default:
						System.out.println("your choice '" + choice7 + "' is invalid");
						break;
					}
				}
				break;
			case "8":// difficulty level
				// list of difficulty levels
				System.out.println("Difficulty Game Levels");
				System.out.println("----------------------");
				myApp.showDifficultyLevels(mapDifficulty);
				// en then the user enter the level that he wants to show the games
				try {
					// myApp.askForDifficultyLevel(mapDifficulty);
					myApp.showAllGamesWithSelectedDifficultyLevel(myApp.askForDifficultyLevel(mapDifficulty), 
							mapCategory, mapDifficulty);
				} catch (inputStringException e) {
					// e1.printStackTrace();
					System.out.println(e.getMessage());

				}

				break;
			case "9":
				// introduire le borrower name et donner la liste des
				// games borrowed pour ce borrower name
				try {
					myApp.showListOfBorrowerSelectedByName(myApp.askForBorrowerName());
				} catch (inputStringException e) {
					// nothing to do, just return to the borrower menu
					// e.pprintStackTrace();
					continue;
				}
				break;
			case "10":// list of borrowed games between 2 dates
				// demander les 2 dates et vérifier qu'elles sont correctes
				Date startDate = myApp.askForDate("Please enter the start date of the borrowing:");
				if (startDate == null)
					break;
				Date endDate = myApp.askForDate("Please enter the end date of the borrowing:");

				if (endDate == null)
					break;

				// System.out.println("dates are ok");
				// select borrowed games between these 2 dates
				myApp.showBorrowedGamesBetween2Dates(startDate, endDate);

				break;
			case "11":// écrire la liste des games dans un fichier
				try {
					myApp.writeGameDataSelectedByNameToFile(myApp.askForGameName(), mapCategory, mapDifficulty);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case "12"://new game category
			myApp.insertNewLevelDifficulty();
			break;
			case "X":
			case "x":
				//myApp.closeGamesDB(myCon);

				System.out.println("bye bye...");
				loop = false;
				break;

			default:
				System.out.println("Your choice '" + choice + "' is invalid");
				logger.error("Your choice '{}' is invalid", choice);
				break;

			}
			System.out.println("");

		}

	}

	private String askForChoice() {
		return (myInputText("Please choose an option:"));
	}

	private void showMenu0() {
		String[] menu1 = { " 1. Show a category id", " 2. Show a Game id", " 3. Show a Borrower id",
				" 4. Show a game of your choice", " 5. Show all Games", " 6. Show a list of Games and Choose a Game",
				" 7. Show borrowed games", " 8. Advanced search: difficulty", " 9. Complex search : borrowers",
				"10. List of borrowed games between 2 dates", 
				"11. Write a game list of you choice to a file (csv formatted)", 
				"12. Insert a new Difficulty Level", " ",
				"X. Quit the Games Application", " " };
		System.out.println("           Games Application    (by J-Ph. Genicot)");
		System.out.println("           -----------------" + "\n");

		for (int i = 0; i < menu1.length; i++) {
			System.out.println(menu1[i]);
		}
	}

	private void showMenuPart7() {
		String[] menu1 = { "1. Show all the borrowed games", "2. Show a list of all the borrowers",
				"3. Show the borrowed games for a borrower of your choice", " ", "X. Quit this menu", " " };
		System.out.println("Borrowed Games Menu");
		System.out.println("-------------------");

		for (int i = 0; i < menu1.length; i++) {
			System.out.println(menu1[i]);
		}
	}

//	private void closeGamesDB(Connection myCon) {
//		gamesServices.closeGamesDB(myCon);
//	}
//
//	Connection connectToGamesDB(String url, String login, String password, String driver) {
//		try {
//			return gamesServices.connectToGamesDB(url, login, password, driver);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public int askForCategoryId() throws inputNumericException {
		int myReturnInt = 0;
		try {
			myReturnInt = myInputInt("Please enter the Category Id you want to see:");
		} catch (InputMismatchException e) {
			throw new inputNumericException("Wrong entry !  You must enter a number");
		}
		return myReturnInt;
	}

	public int askForGameId() throws inputNumericException {
		int myReturnInt = 0;
		try {
			myReturnInt = myInputInt("Please enter the Game Id you want to see:");
		} catch (InputMismatchException e) {

			throw new inputNumericException("Wrong entry !  You must enter a number");
		}
		return myReturnInt;
	}

	public int askForBorrowerId() throws inputNumericException {
		int myReturnInt = 0;
		try {
			myReturnInt = myInputInt("Please enter the Borrower Id you want to see:");
		} catch (InputMismatchException e) {
			throw new inputNumericException("Wrong entry !  You must enter a number");
		}
		return myReturnInt;
	}

	public String askForGameName() throws inputStringException {
		String myGameName = "";
		try {
			myGameName = myInputText("Please enter the Game name (or a part of) you want to search for:");
			if (myGameName.isEmpty()) {
				throw new inputStringException("Your entry is empty!  You must enter something");
			}

		} catch (InputMismatchException e) {
			throw new inputStringException("Wrong entry !  You must enter something");
		}
		return myGameName;
	}

	public String askForBorrowerName() throws inputStringException {
		String myBorrowerName = "";
		try {
			myBorrowerName = myInputText("Please enter the borrower name (or a part of) you want to search for:");
			if (myBorrowerName.isEmpty()) {
				// if entry empty, return to the menu
				throw new inputStringException("Your entry is empty!  I'm returning to the menu...");
			}

		} catch (InputMismatchException e) {
			throw new inputStringException("Wrong entry !  You must enter something");
		}
		return myBorrowerName;
	}

	public int askForDifficultyLevel(HashMap<Integer, String> mapDifficulty) throws inputStringException {
		int myDifficultyLevel;
		try {
			myDifficultyLevel = myInputInt(
					"Please enter the difficulty level number of the games you want to search for:");
			if (mapDifficulty.get(myDifficultyLevel) != null) {
				System.out.println(
						"You choose the level " + myDifficultyLevel + " = " + mapDifficulty.get(myDifficultyLevel));
			} else {
				throw new inputStringException("The level number " + myDifficultyLevel + " you entered is not valid");
			}

		} catch (InputMismatchException e) {
			throw new inputStringException("Wrong entry !  You must enter a valid number");
		}
		return myDifficultyLevel;

	}

	public void showOneCategoryDetails(int id) {
		gamesServices.showOneCategoryDetails(id);
	}

	public void showOneGameDetails(int id) {
		gamesServices.showOneGameDetails(id);
	}

	public void showOneBorrowerDetails(int id) {
		gamesServices.showOneBorrowerDetails(id);
	}

	public void showGameSelectedByName(String gameName) {
		gamesServices.showGameSelectedByName(gameName);
	}

	public void showAllGamesSortedViaSql() {
		gamesServices.showAllGamesSortedViaSql();
	}

	public void showAllGamesSortedViaInternal() {
		gamesServices.showAllGamesSortedViaInternal();
	}

	public void showAllDetailsGameSelectedByName(String gameName, 
			HashMap<Integer, String> mapCategory, HashMap<Integer, String> mapDifficulty) {
		gamesServices.showAllDetailsGameSelectedByName(gameName, mapCategory, mapDifficulty);
	}

	public void writeGameDataSelectedByNameToFile(String gameName, 
			HashMap<Integer, String> mapCategory, HashMap<Integer, String> mapDifficulty) {

		String myFolder = askForDirectory();
		String myFileName = askForFileName(myFolder);
		gamesServices.writeGameDataSelectedByNameToFile(gameName, myFileName);

	}

	public String askForDirectory() {
		// String folderx = askForNewFolderName("H:/_LAN_JPG/testjava/");
		String folderx = askForNewFolderName(defaultFolder);
		File folder = new File(folderx);
		if (!folder.exists()) {
			folder.mkdir();
			System.out.println("folder=" + folder);
		}
		return folderx;
	}

	public String askForFileName(String folder) {
		// String myFileName = askForNewFileName("outfile.txt");
		String myFileName = askForNewFileName(defaultOutputFileName);
		File fileName = new File(folder + myFileName);
		System.out.println("filename=" + fileName.toString());
		// if (!fileName.exists()) {
		// try {
		// fileName.createNewFile();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		return fileName.toString();
	}

	public String askForNewFolderName(String folderx) {
		System.out.println("The default folder is : " + folderx);
		String newFolderx = myInputText("To change it, enter the new one, otherwise do Enter");
		return (newFolderx.isEmpty() ? folderx : newFolderx);
	}

	public String askForNewFileName(String filex) {
		System.out.println("The default filename is: " + filex);
		String newFilex = myInputText("to change it, enter the new one; otherwise do Enter");
		return (newFilex.isEmpty() ? filex : newFilex);
	}

	private HashMap<Integer, String> fillDifficultyMap() {
		return gamesServices.fillDifficultyMap();
	}

	private HashMap<Integer, String> fillCategoryMap() {
		return gamesServices.fillCategoryMap();

	}

	void insertNewLevelDifficulty() {
		gamesServices.insertNewLevelDifficulty();
		mapDifficulty = fillDifficultyMap();
		logger.trace("hashmap mapDifficuty RE-loaded");
		mapDifficulty.forEach((k, v) -> System.out.println("Key = " + k + ", Value = " + v));
	}
	
	
	private void showAllGamesWithCategory(HashMap<Integer, String> mapCategory) {
		gamesServices.showAllGamesWithCategory(mapCategory);

	}

	public void showAllBorrowedGamesSortedViaSql() {
		gamesServices.showAllBorrowedGamesSortedViaSql();
	}

	public void showBorrowedGamesBetween2Dates(Date startDate, Date endDate) {
		gamesServices.showBorrowedGamesBetween2Dates(startDate, endDate);
	}

	public void showAllBorrowersSortedViaSql() {
		gamesServices.showAllBorrowersSortedViaSql();
	}

	public void showAllDetailsBorrowerSelectedByName(String borrowerName) {
		gamesServices.showAllDetailsBorrowerSelectedByName(borrowerName);
	}

	public void showListOfBorrowerSelectedByName(String borrowerName) {
		gamesServices.showListOfBorrowerSelectedByName(borrowerName);
	}

	public void showDifficultyLevels(HashMap<Integer, String> mapDifficulty) {
		// for (Map.Entry mapentry : mapDifficulty.entrySet()) {
		// System.out.println(mapentry.getKey() + " : " + mapentry.getValue());
		// }
		mapDifficulty.forEach((k, v) -> System.out.println(k + " : " + v));

	}

	public void showAllGamesWithSelectedDifficultyLevel(int difficultyId, HashMap<Integer, String> mapCategory, HashMap<Integer, String> mapDifficulty) {
		gamesServices.showAllGamesWithSelectedDifficultyLevel(difficultyId, mapCategory, mapDifficulty);

	}

	public Date askForDate(String wording) {
		String myDateString = myInputText(wording + " (format dd-mm-ccyy):");

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date mydate = null;
		if (isDateValid(myDateString, "dd-MM-yyyy")) {
			// System.out.println("date ok");
			try {
				mydate = df.parse(myDateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("This value is not a valid date !");
		}
		return mydate;
	}

	public boolean isDateValid(String dateString, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			if (sdf.format(sdf.parse(dateString)).equals(dateString))
				return true;
		} catch (ParseException pe) {
		}
		return false;
	}

	
	
	// *******************************************************
	public String myInputText(String textToDisplay) {
		scanner = new Scanner(System.in);
		System.out.println(textToDisplay);
		String inputTextin = scanner.nextLine();

		return inputTextin;
	}

	public int myInputInt(String textToDisplay) {
		scanner = new Scanner(System.in);
		System.out.println(textToDisplay);
		int myInputInt = scanner.nextInt();

		return myInputInt;
	}

	public double myInputDouble(String textToDisplay) {
		scanner = new Scanner(System.in);
		System.out.println(textToDisplay);
		double myInput = scanner.nextDouble();
		return myInput;
	}

	public Properties loadPropertiesFile(String filePath) {

		Properties prop = new Properties();

		try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
			prop.load(resourceAsStream);
		} catch (IOException e) {
			System.err.println("Unable to load properties file : " + filePath);
		}

		return prop;

	}

}
