package be.belfius.games.domain;

import java.util.Date;

public class Borrow {

	int Id;
	int game_id;
	int borrow_id;
	Date borrow_date;
	Date return_date;

	public Borrow(int id, int game_id, int borrow_id, Date borrow_date, Date return_date) {
		super();
		Id = id;
		this.game_id = game_id;
		this.borrow_id = borrow_id;
		this.borrow_date = borrow_date;
		this.return_date = return_date;
	}

	public Borrow() {
		super();
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public int getBorrow_id() {
		return borrow_id;
	}

	public void setBorrow_id(int borrow_id) {
		this.borrow_id = borrow_id;
	}

	public Date getBorrow_date() {
		return borrow_date;
	}

	public void setBorrow_date(Date borrow_date) {
		this.borrow_date = borrow_date;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}

	@Override
	public String toString() {
		return "Borrow [Id=" + Id + ", game_id=" + game_id + ", borrow_id=" + borrow_id + ", borrow_date=" + borrow_date
				+ ", return_date=" + return_date + "]";
	}
}
