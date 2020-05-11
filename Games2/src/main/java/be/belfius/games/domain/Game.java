package be.belfius.games.domain;

import java.io.PrintStream;

public class Game {
	int Id;
	String game_name;
	String editor;
	String author;
	int year_edition;
	String age;
	int min_players;
	int max_players;
	int category_id;
	String play_duration;
	int difficulty_id;
	float price;
	String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Game(int id, String game_name, String editor, String author, int year_edition, String age, int min_players,
			int max_players, int category_id, String play_duration, int difficulty_id, float price, String image) {
		super();
		Id = id;
		this.game_name = game_name;
		this.editor = editor;
		this.author = author;
		this.year_edition = year_edition;
		this.age = age;
		this.min_players = min_players;
		this.max_players = max_players;
		this.category_id = category_id;
		this.play_duration = play_duration;
		this.difficulty_id = difficulty_id;
		this.price = price;
		this.image = image;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getGame_name() {
		return game_name;
	}

	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear_edition() {
		return year_edition;
	}

	public void setYear_edition(int year_edition) {
		this.year_edition = year_edition;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public int getMin_players() {
		return min_players;
	}

	public void setMin_players(int min_players) {
		this.min_players = min_players;
	}

	public int getMax_players() {
		return max_players;
	}

	public void setMax_players(int max_players) {
		this.max_players = max_players;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getPlay_duration() {
		return play_duration;
	}

	public void setPlay_duration(String play_duration) {
		this.play_duration = play_duration;
	}

	public int getDifficulty_id() {
		return difficulty_id;
	}

	public void setDifficulty_id(int difficulty_id) {
		this.difficulty_id = difficulty_id;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Game() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Id=" + Id + ", game_name=" + game_name + ", editor=" + editor + ", author=" + author + "\n\t"
				+ "year_edition=" + year_edition + ", age=" + age + ", min_players=" + min_players + ", max_players="
				+ max_players + "\n\t" + "category_id=" + category_id + ", play_duration=" + play_duration
				+ ", difficulty_id=" + difficulty_id + ", price=" + price + ", image=" + image;
	}

	public PrintStream toStringFmtLight() {
		return System.out.printf("Name=%-40s  Editor=%-25s  Age=%-20s  Price=%6.2f%n", game_name, editor, age, price);
	}

	public String toStringLight2() {
		return "game_name=" + game_name + "\t" + "editor=" + editor + "\t" + "price=" + price;
	}
}
