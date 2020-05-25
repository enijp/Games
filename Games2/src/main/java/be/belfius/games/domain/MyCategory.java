package be.belfius.games.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAll", query = "select mycat from MyCategory mycat"),
    @NamedQuery(name = "findByName", query = "select mycat from MyCategory mycat where mycat.catName = :name")
})
public class MyCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	//@NotNull
	//@Size(min = 2, max = 200)
	private String catName;

	public MyCategory() {

	}

	public MyCategory(String catName) {
		super();
		// this.id = id;
		this.catName = catName;
	}

	 @Override
	public String toString() {
		return "MyCategory [id=" + id + ", catName=" + catName + "]";
	}
		
	
//	    
//	    
//		public MyCategory(int id, String catName) {
//			super();
//			this.id = id;
//			this.catName = catName;
//		}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

}
