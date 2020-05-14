package be.belfius.games.domain;

public class Borrowed_game {

	private Game game;
	private Borrower borrower;
	private Borrow borrow;
	
	@Override
	public String toString() {
		return "Borrowed_game [game=" + game + "\nborrower=" + borrower + "\nborrow=" + borrow + "]";
	}


	public Borrow getBorrow() {
		return borrow;
	}


	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}


	public Game getGame() {
		return game;
	}


	public void setGame(Game game) {
		this.game = game;
	}


	public Borrower getBorrower() {
		return borrower;
	}


	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}


	public Borrowed_game() {
		super();
	}
	
}
