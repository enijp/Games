package be.belfius.games.exceptions;

public class emptyResultException extends Exception {

	static String mess = "Sorry, no result is correponding to your selection";
	
	public emptyResultException() {
		super(mess);
		// TODO Auto-generated constructor stub
	}

	public emptyResultException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
