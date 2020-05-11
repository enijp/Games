package be.belfius.games.domain;

public class Category {

	int Id;
	String category_name;
		
	
	@Override
	public String toString() {
		return "Category Id=" + Id + ": category_name=" + category_name ;
	}
	public Category(int id, String category_name) {
		super();
		Id = id;
		this.category_name = category_name;
	}
	public Category() {
		super();
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
}
