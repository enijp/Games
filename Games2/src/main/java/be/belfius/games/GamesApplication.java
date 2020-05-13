package be.belfius.games;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.belfius.games.exceptions.inputNumericException;
import be.belfius.games.exceptions.inputStringException;
import be.belfius.games.services.GamesServices;
import be.belfius.games.utils.Helper;

public class GamesApplication {

	private static Logger logger = LoggerFactory.getLogger(GamesApplication.class);

	private Scanner scanner = new Scanner(System.in);
	private GamesServices gamesServices = new GamesServices();
	private static HashMap<Integer, String> mapCategory;
	private static HashMap<Integer, String> mapDifficulty;

	public static void main(String[] args) {
		GamesApplication myApp = new GamesApplication();
		System.setProperty("org.slf4j.simpleLogger.showDateTime", "true"); // Use this setting to show the date and time
		System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "yyyy-MM-dd HH:mm:ss"); // Use this setting to
																							// format te date and time
		try {
			Class.forName(Helper.loadPropertiesFile().getProperty("db.driverClassName"));
		} catch (ClassNotFoundException e) {
			logger.error("Error mysql driver: problems : problems can occurs...", e);
		}
		logger.info("GamesApplication starts!");
		myApp.displayProperties();

		// fill the map with category table
		mapCategory = myApp.fillCategoryMap();

		// fill the map with difficulty table
		mapDifficulty = myApp.fillDifficultyMap();

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
					logger.error(e.getMessage());
				}
				break;
			case "2":
				try {
					myApp.showOneGameDetails(myApp.askForGameId());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				break;
			case "3":
				try {
					myApp.showOneBorrowerDetails(myApp.askForBorrowerId());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				break;
			case "4":
				try {
					myApp.showGameSelectedByName(myApp.askForGameName());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				break;
			case "5":
				// d'abord via le tri SQL
				logger.info("1) sorted via SQL");
				myApp.showAllGamesSortedViaSql();
				// ensuite via le tri sur les games de la class Game
				logger.info("2) sorted via internal sort");
				myApp.showAllGamesSortedViaInternal();
				break;
			case "6":
				// list of all games with category
				myApp.showAllGamesWithCategory(mapCategory);
				try {
					myApp.showAllDetailsGameSelectedByName(myApp.askForGameName(), mapCategory, mapDifficulty);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				break;
			case "7":// borrowed games
				boolean loopOption7 = true;
				while (loopOption7) {
					myApp.showMenuPart7();
					String choice7 = myApp.askForChoice();
					switch (choice7.toLowerCase()) {
					case "1":// liste de tous les borrowed games
						myApp.showAllBorrowedGamesSortedViaSql();
						break;
					case "2":// liste de tous les borrowers
						myApp.showAllBorrowersSortedViaSql();
						break;
					case "3":// introduire le borrower name et donner la liste des
						// games borrowed pour ce borrower name
						try {
							myApp.showAllDetailsBorrowerSelectedByName(myApp.askForBorrowerName());
						} catch (inputStringException e) {
							logger.error(e.getMessage());
						}
						break;
					case "x":
						loopOption7 = false;
						break;
					default:
						if (choice7.isEmpty()) {
							logger.error("Your entry is empty; you have to make a choice:");
						} else {
							logger.error("Your choice '{}' is invalid", choice7);
						}
						break;
					}
				}
				break;
			case "8":// difficulty level
				// list of difficulty levels
				myApp.showDifficultyLevels(mapDifficulty);
				// then the user enter the minimum level of the games he wants to show
				try {
					myApp.showAllGamesWithSelectedDifficultyLevel(myApp.askForDifficultyLevel(mapDifficulty), mapCategory,
							mapDifficulty);
				} catch (inputStringException e) {
					logger.error(e.getMessage());
				}
				break;
			case "9":
				// introduire le borrower name et donner la liste des
				// games borrowed pour ce borrower name
				try {
					myApp.showListOfBorrowerSelectedByName(myApp.askForBorrowerName());
				} catch (inputStringException e) {
					logger.error(e.getMessage());
				}
				break;
			case "10":// list of borrowed games between 2 dates
				// ask start and end dates and check if they are valid
				Date startDate = myApp.askForDate("Please enter the start date of the borrowing:");
				if (startDate == null)
					break;
				Date endDate = myApp.askForDate("Please enter the end date of the borrowing:");
				if (endDate == null)
					break;
				myApp.showBorrowedGamesBetween2Dates(startDate, endDate);
				break;
			case "11":// écrire la liste des games dans un fichier
				try {
					myApp.writeGameDataSelectedByNameToFile(myApp.askForGameName(), mapCategory, mapDifficulty);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				break;
			case "12":// new game category
				myApp.insertNewLevelDifficulty();
				break;
			case "X":
			case "x":
				logger.info("GamesApplication ends... bye bye !");
				loop = false;
				break;
			default:
				if (choice.isEmpty()) {
					logger.error("Your entry is empty; you have to make a choice:");
				} else {
					logger.error("Your choice '{}' is invalid", choice);
				}
				break;
			}
			System.out.println("");
		}
	}

	private String askForChoice() {
		return (myInputText("Please choose an option:"));
	}

	private void showMenu0() {
		String[] menu = { " 1. Show a game category of your choice", " 2. Show a Game of your choice",
				" 3. Show a Borrower id", " 4. Show a game of your choice", " 5. Show all Games",
				" 6. Show a list of Games and Choose a Game", " 7. Show borrowed games",
				" 8. Advanced search: difficulty", " 9. Complex search : borrowers",
				"10. List of borrowed games between 2 dates",
				"11. Write a game list of you choice to a file (csv formatted)", "12. Insert a new Difficulty Level",
				" ", "X. Quit the Games Application", " " };
		System.out.println("           Games Application    (by J-Ph. Genicot)");
		System.out.println("           -----------------" + "\n");
		for (int i = 0; i < menu.length; i++) {
			System.out.println(menu[i]);
		}
	}

	private void showMenuPart7() {
		String[] menu = { "1. Show all the borrowed games", "2. Show a list of all the borrowers",
				"3. Show the borrowed games for a borrower of your choice", " ", "X. Quit this menu", " " };
		System.out.println("Borrowed Games Menu");
		System.out.println("-------------------");
		for (int i = 0; i < menu.length; i++) {
			System.out.println(menu[i]);
		}
	}

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
				throw new inputStringException("Your entry is empty!  You must enter something");
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
					"Please enter the minimum difficulty level number of the games you want to search for:");
			if (mapDifficulty.get(myDifficultyLevel) != null) {
				System.out.println(
						"You choose the minimum level " + 
				myDifficultyLevel + " = " + mapDifficulty.get(myDifficultyLevel) + "\n");
			} else {
				throw new inputStringException("The minimum level number " + myDifficultyLevel + " you entered is not valid");
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

	public void showAllDetailsGameSelectedByName(String gameName, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		gamesServices.showAllDetailsGameSelectedByName(gameName, mapCategory, mapDifficulty);
	}

	public void writeGameDataSelectedByNameToFile(String gameName, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		String myFolder = askForDirectory();
		String myFileName = askForFileName(myFolder);
		gamesServices.writeGameDataSelectedByNameToFile(gameName, myFileName);

	}

	public String askForDirectory() {
		File folder = new File(askForNewFolderName(Helper.loadPropertiesFile().getProperty("fi.defaultOutputFolder")));
		if (!folder.exists()) {
			folder.mkdir();
			logger.info("a new folder=" + folder + " has been created");
		}
		return folder.toString();
	}

	public String askForFileName(String folder) {
		File fileName = new File(folder + "/" +askForNewFileName(Helper.loadPropertiesFile().getProperty("fi.defaultOutputFileName")));
		//check if the filename has a .txt extension : if not, add it
		String myFileName = fileName.toString();
		String ext = myFileName.substring(myFileName.lastIndexOf(".") + 1); 
		if (!ext.equalsIgnoreCase("txt")) myFileName += ".txt";
		logger.info("output filename=" + myFileName);
		return myFileName;
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
		System.out.println("All games List");
		System.out.println("--------------");
		gamesServices.showAllGamesWithCategory(mapCategory);
		System.out.println("--------------");
	}

	public void showAllBorrowedGamesSortedViaSql() {
		System.out.println("All Borrowed Games");
		System.out.println("------------------");
		gamesServices.showAllBorrowedGamesSortedViaSql();
		System.out.println("------------------");
	}

	public void showBorrowedGamesBetween2Dates(Date startDate, Date endDate) {
		gamesServices.showBorrowedGamesBetween2Dates(startDate, endDate);
	}

	public void showAllBorrowersSortedViaSql() {
		System.out.println("List of all Borrowers");
		System.out.println("---------------------");
		gamesServices.showAllBorrowersSortedViaSql();
		System.out.println("---------------------");
	}

	public void showAllDetailsBorrowerSelectedByName(String borrowerName) {
		gamesServices.showAllDetailsBorrowerSelectedByName(borrowerName);
	}

	public void showListOfBorrowerSelectedByName(String borrowerName) {
		gamesServices.showListOfBorrowerSelectedByName(borrowerName);
	}

	public void showDifficultyLevels(HashMap<Integer, String> mapDifficulty) {
		System.out.println("Difficulty Game Levels");
		System.out.println("----------------------");
		mapDifficulty.forEach((k, v) -> System.out.println(k + " : " + v));
	}

	public void showAllGamesWithSelectedDifficultyLevel(int difficultyId, HashMap<Integer, String> mapCategory,
			HashMap<Integer, String> mapDifficulty) {
		gamesServices.showAllGamesWithSelectedDifficultyLevel(difficultyId, mapCategory, mapDifficulty);

	}

	public Date askForDate(String wording) {
		String myDateString = myInputText(wording + " (format dd-mm-ccyy):");
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date mydate = null;
		if (isDateValid(myDateString, "dd-MM-yyyy")) {
			try {
				mydate = df.parse(myDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			logger.error("This value is not a valid date in the format dd-mm-ccyy!");
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
		return scanner.nextLine();
	}

	public int myInputInt(String textToDisplay) {
		scanner = new Scanner(System.in);
		System.out.println(textToDisplay);
		int myInputInt = 0;
		Boolean loop = true;
		while (loop) {
			try {
				myInputInt = Integer.parseInt(scanner.nextLine());
				loop = false;
			} catch (NumberFormatException e) {
				logger.error("Your entry is not valid; Please enter a number");
			}
		}
		return myInputInt;
	}

	public double myInputDouble(String textToDisplay) {
		scanner = new Scanner(System.in);
		System.out.println(textToDisplay);
		double myInput = scanner.nextDouble();
		return myInput;
	}

	public void displayProperties() {
		// read properties
		logger.trace("reading properties:");
		Properties prop = Helper.loadPropertiesFile();
		prop.forEach((k, v) -> logger.trace("key=" + k + " ; value=" + v));

		logger.info("running with properties for <" + prop.get("exec.env") + "> environment");

		logger.trace("default folder=" + Helper.loadPropertiesFile().getProperty("fi.defaultOutputFolder"));
		logger.trace("default output filename=" + Helper.loadPropertiesFile().getProperty("fi.defaultOutputFileName"));
	}
}
