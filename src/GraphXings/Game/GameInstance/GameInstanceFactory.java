package GraphXings.Game.GameInstance;

/**
 * An interface for classes that can generate game instances.
 */
public interface GameInstanceFactory
{
	/**
	 * Constructs a new instance for the GraphXings game!
	 * @return A new instance of the GraphXings game.
	 */
	public GameInstance getGameInstance();
}
