package GraphXings.NewFiles;

import java.util.Random;

import GraphXings.Algorithms.NewPlayer;
import GraphXings.Game.GameMove;
import GraphXings.Data.*;

/**
 * A player performing random moves.
 */
public class NewMixingPlayer implements NewPlayer {
    /**
     * The name of the Good palyer
     */
    private String name;
    /**
     * The strategies of the player
     */
    private NewPlayer strategy1;
    private NewPlayer strategy2;
    /**
     * The percentage value with which to choose strategy1 over strategy2
     */
    private int percentage;

    /**
     * Creates a player with the assigned name.
     * that mixes two strategies
     * 
     * @param name
     */
    public NewMixingPlayer(String name) {
        this.name = name;
    }

    @Override
    public GameMove maximizeCrossings(GameMove lastMove) {
        Random r = new Random(100);
        if (r.nextInt() < percentage) {
            return strategy1.maximizeCrossings(lastMove);
        } else {
            return strategy2.maximizeCrossings(lastMove);
        }
    }

    @Override
    public GameMove minimizeCrossings(GameMove lastMove) {
        Random r = new Random(100);
        if (r.nextInt() < percentage) {
            return strategy1.minimizeCrossings(lastMove);
        } else {
            return strategy2.minimizeCrossings(lastMove);
        }
    }

    @Override
    public void initializeNextRound(Graph g, int width, int height, Role role) {
        this.strategy1.initializeNextRound(g, width, height, role);
        this.strategy2.initializeNextRound(g, width, height, role);
    }

    @Override
    public String getName() {
        return name;
    }

    public String getStrategies() {
        return name + " mixes " + strategy1.getName() + " and " + strategy2.getName();
    }

}
