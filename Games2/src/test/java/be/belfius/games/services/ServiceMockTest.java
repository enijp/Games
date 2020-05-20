package be.belfius.games.services;

import be.belfius.games.domain.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import be.belfius.games.repository.GamesRepository;

@RunWith(MockitoJUnitRunner.class)

public class ServiceMockTest {

	@Mock // create a fake gamesRepository
	private GamesRepository gamesRepository;
	private HashMap<Integer, String> myDiffMapInit = new HashMap<Integer, String>(); ;

	@Captor
	ArgumentCaptor<Category> categoryArgumentCaptor;

	@InjectMocks // inject the fake repository into the class to test
	private GamesServices gamesService = new GamesServices();

	@Before // si par exemple on a des méthodes qui renvoient des valeurs, on peut définir
			// ces valeurs ici
	public void setUp() throws Exception {
		ArrayList<Category> myList = new ArrayList<Category>();
		// myList.add(new Category(1, "ss"));
		myList.add(new Category(2, "zz"));
		myList.add(new Category(40, "stratego"));
		Mockito.when(gamesRepository.selectOneCategoryById(1)).thenReturn(myList);
		Mockito.when(gamesRepository.selectOneCategoryById(40)).thenReturn(myList);

		myDiffMapInit.put(1, "very easy");
		myDiffMapInit.put(2, "easy");
		myDiffMapInit.put(3, "average");
		Mockito.when(gamesRepository.selectAllDifficulty()).thenReturn(myDiffMapInit);
	}

	@Test
	public void verifyShowOneCategoryDetails2BeenCalledOnce() {
		String catName = gamesService.showOneCategoryDetails2(1);
		Mockito.verify(gamesRepository, Mockito.times(1)).selectOneCategoryById(1);
		assertEquals("zz", catName);
		//Mockito.verifyNoMoreInteractions(gamesRepository);
	}

	@Test
	public void verifyShowOneCategoryDetails2ReturnsRightResult() {
		String catName = gamesService.showOneCategoryDetails2(40);
		assertEquals("stratego", catName);
		// Mockito.verifyNoMoreInteractions(gamesRepository);
	}

	@Test
	public void verifyIfDifficultyMapIsWellFilled() {
		HashMap<Integer, String> myDiffMapResult = gamesService.fillDifficultyMap();
		myDiffMapResult.forEach((k, v) -> System.out.println("Key = " + k + ", Value = " + v));
		assertEquals(myDiffMapInit, myDiffMapResult);
	}

//	 @Test
//	    public void addAnimal() {
//	        Animal animal = new Animal.Builder(AnimalType.BEAR)
//	                .withId(9).withName("Max").withFood(new Food(1, FoodType.MEAT, "fish")).build();
//	        animalService.addAnimal(animal);
//	        verify(animalRepository).addAnimal(animalArgumentCaptor.capture());
//	        
//	        assertEquals("Max",animalArgumentCaptor.getValue().getName());
//	    }
}
