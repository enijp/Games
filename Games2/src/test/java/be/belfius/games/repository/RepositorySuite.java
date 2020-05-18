package be.belfius.games.repository;


import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Suite.SuiteClasses({GamesRepositoryTest.class})
@Categories.ExcludeCategory(Slow.class)
public class RepositorySuite {
}
