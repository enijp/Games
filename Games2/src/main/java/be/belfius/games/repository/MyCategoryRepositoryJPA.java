package be.belfius.games.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import be.belfius.games.domain.MyCategory;

public class MyCategoryRepositoryJPA {
	 	
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gamesPu");
	private EntityManager entityManager = entityManagerFactory.createEntityManager();

    public void save(MyCategory myCategory){
        entityManager.getTransaction().begin();
        entityManager.persist(myCategory);
        entityManager.getTransaction().commit();
    }

        public List<MyCategory> findAll() {
        return entityManager.createNamedQuery("findAll", MyCategory.class).getResultList();
    }

    public MyCategory findById(int id) {
        return entityManager.find(MyCategory.class, id);
    }

    public void remove(MyCategory myCategory){
        entityManager.getTransaction().begin();
        entityManager.remove(myCategory);
        entityManager.getTransaction().commit();
    }

	 public MyCategory findByName(String name) {
        return entityManager.createNamedQuery("findByName", MyCategory.class).setParameter("name" , name).getSingleResult();
    }
    
}

