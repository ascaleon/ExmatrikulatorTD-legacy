package de.diegrafen.towerwars.net;

import com.badlogic.gdx.Gdx;

import static de.diegrafen.towerwars.net.GameState.GameStateType.PLAYER_ONE;
import static de.diegrafen.towerwars.net.GameState.GameStateType.PLAYER_TWO;
import static de.diegrafen.towerwars.net.GameState.GameStateType.SERVER;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 00:16
 */
public class GameState {

    public enum GameStateType {
        PLAYER_ONE, PLAYER_TWO, SERVER;
    }

    private PlayerState playerOneState;

    private PlayerState playerTwoState;

    private GameStateType gameStateType;

    public GameState (GameStateType gameStateType) {
        this.gameStateType = gameStateType;
        playerOneState = new PlayerState();
        playerTwoState = new PlayerState();
    }

    public void reset (GameStateType gameStateType) {
        if (gameStateType == PLAYER_ONE || gameStateType == SERVER) {
            playerOneState = new PlayerState();
        }
        if (gameStateType == PLAYER_TWO || gameStateType == SERVER) {
            playerTwoState = new PlayerState();
        }
    }

    public PlayerState getPlayerOneState() {
        return playerOneState;
    }

    public PlayerState getPlayerTwoState() {
        return playerTwoState;
    }

    public GameStateType getGameStateType() {
        return gameStateType;
    }

    public void setPlayerOneState(PlayerState playerOneState) {
        this.playerOneState = playerOneState;
    }

    public void setPlayerTwoState(PlayerState playerTwoState) {
        this.playerTwoState = playerTwoState;
    }

    public void setGameStateType(GameStateType gameStateType) {
        this.gameStateType = gameStateType;
    }
}
