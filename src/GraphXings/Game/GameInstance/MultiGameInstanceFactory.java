package GraphXings.Game.GameInstance;


import java.util.ArrayList;
import java.util.Random;

/**
 * A factory that randomly selects amongst a selection of GameInstanceFactories to create a new GameInstance.
 */
public class MultiGameInstanceFactory implements GameInstanceFactory
{
	/**
	 * A list of GameInstanceFactories.
	 */
	ArrayList<GameInstanceFactory> factories;
	/**
	 * A random object for deciding the next factory to use.
	 */
	Random r;

	@Override
	public GameInstance getGameInstance()
	{
		return factories.get(r.nextInt(factories.size())).getGameInstance();
	}

	/**
	 * Creates a new MultiGameInstanceFactory
	 * @param factory The first factory to be included, must be passed so to avoid NullPointerExceptions.
	 * @param seed The seed for the random number generator.
	 */
	public MultiGameInstanceFactory(GameInstanceFactory factory, long seed)
	{
		factories = new ArrayList<>();
		factories.add(factory);
		r = new Random(seed);
	}

	/**
	 * Adds another GameInstanceFactory to the list of eligible factories.
	 * @param factory The new GameInstanceFactory.
	 */
	public void addFactory(GameInstanceFactory factory)
	{
		factories.add(factory);
	}
}
