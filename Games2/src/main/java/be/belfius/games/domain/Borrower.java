package be.belfius.games.domain;

public class Borrower {
	int Id;
	String borrower_name;
	String street;
	String house_number;
	String bus_number;
	int postcode;
	String city;
	String telephone;
	String email;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getBorrower_name() {
		return borrower_name;
	}
	public void setBorrower_name(String borrower_name) {
		this.borrower_name = borrower_name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouse_number() {
		return house_number;
	}
	public void setHouse_number(String house_number) {
		this.house_number = house_number;
	}
	public String getBus_number() {
		return bus_number;
	}
	public void setBus_number(String bus_number) {
		this.bus_number = bus_number;
	}
	public int getPostcode() {
		return postcode;
	}
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Borrower(int id, String borrower_name, String street, String house_number, String bus_number, int postcode,
			String city, String telephone, String email) {
		super();
		Id = id;
		this.borrower_name = borrower_name;
		this.street = street;
		this.house_number = house_number;
		this.bus_number = bus_number;
		this.postcode = postcode;
		this.city = city;
		this.telephone = telephone;
		this.email = email;
	}
	@Override
	public String toString() {
		return "Borrower [Id=" + Id + ", borrower_name=" + borrower_name + ", street=" + street + ", house_number="
				+ house_number + ", bus_number=" + bus_number + ", postcode=" + postcode + ", city=" + city
				+ ", telephone=" + telephone + ", email=" + email + "]";
	}
	public Borrower() {
		super();
	}
	
}
