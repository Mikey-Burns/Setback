/**
 * This file was developed for fun by Michael Burns for a private
 * implementation of the card game Setback, also known as Pitch.
 */
package setback.networking;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import setback.common.PlayerNumber;
import setback.common.SetbackException;
import setback.game.SetbackGameController;
import setback.game.SetbackGameFactory;
import setback.networking.command.Command;
import setback.networking.command.CommandMessage;

/**
 * This class will test the PlayerController class.
 * @author Michael
 * @version Dec 26, 2013
 */
public class PlayerControllerTest {
	
	private SetbackGameFactory factory;
	private SetbackGameController game;
	private PlayerController controllerOne;
	private PlayerController controllerTwo;
	private PlayerController controllerThree;
	private PlayerController controllerFour;
	
	@Before
	public void setup() {
		factory = SetbackGameFactory.getInstance();
		game = factory.makeDeltaSetbackGame(0);
		controllerOne = new PlayerController(game);
		controllerTwo = new PlayerController(game);
		controllerThree = new PlayerController(game);
		controllerFour = new PlayerController(game);
	}

	@Test
	public void nullInput() {
		final String result = controllerOne.processInput(null);
		final String expected = "No command";
		assertEquals(expected, result);
	}
	
	@Test
	public void noCommandInput() {
		final String result = controllerOne.processInput(new CommandMessage(Command.NO_COMMAND));
		final String expected = "No command";
		assertEquals(expected, result);
	}
	
	@Test
	public void exitInput() {
		final String result = controllerOne.processInput(new CommandMessage(Command.EXIT));
		final String expected = "EXIT";
		assertEquals(expected, result);
	}
	
	@Test
	public void requestPlayerOneSelected() {
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_ONE));
		final String expected = "Player one selected";
		assertEquals(expected, result);
		assertEquals(PlayerNumber.PLAYER_ONE, controllerOne.getMyNumber());
	}
	
	@Test
	public void requestPlayerOneRejected() {
		controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_ONE));
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_ONE));
		final String expected = "Player one rejected";
		assertEquals(expected, result);
	}
	
	@Test
	public void requestPlayerTwoSelected() {
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_TWO));
		final String expected = "Player two selected";
		assertEquals(expected, result);
		assertEquals(PlayerNumber.PLAYER_TWO, controllerOne.getMyNumber());
	}
	
	@Test
	public void requestPlayerTwoRejected() {
		controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_TWO));
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_TWO));
		final String expected = "Player two rejected";
		assertEquals(expected, result);
	}
	
	@Test
	public void requestPlayerThreeSelected() {
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_THREE));
		final String expected = "Player three selected";
		assertEquals(expected, result);
		assertEquals(PlayerNumber.PLAYER_THREE, controllerOne.getMyNumber());
	}
	
	@Test
	public void requestPlayerThreeRejected() {
		controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_THREE));
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_THREE));
		final String expected = "Player three rejected";
		assertEquals(expected, result);
	}
	
	@Test
	public void requestPlayerFourSelected() {
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_FOUR));
		final String expected = "Player four selected";
		assertEquals(expected, result);
		assertEquals(PlayerNumber.PLAYER_FOUR, controllerOne.getMyNumber());
	}
	
	@Test
	public void requestPlayerFourRejected() {
		controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_FOUR));
		final String result = controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_FOUR));
		final String expected = "Player four rejected";
		assertEquals(expected, result);
	}
	
	@Test
	public void attemptBetWithoutFourPlayers() {
		final String arguments[] = {"PASS"};
		controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_ONE));
		final String result = controllerOne.processInput(new CommandMessage(Command.PLACE_BET, arguments));
		final String expected = "You must start the game!";
		assertEquals(expected, result);
	}
	
	@Test
	public void playerTwoValidBet() {
		initializeFourControllers();
		final String arguments[] = {"PASS"};
		final String result = controllerTwo.processInput(new CommandMessage(Command.PLACE_BET, arguments));
		final String expected = "PLAYER_TWO BET PASS";
		assertEquals(expected, result);
	}
	
	@Test
	public void playerOneEarlyBet() {
		initializeFourControllers();
		final String arguments[] = {"PASS"};
		final String result = controllerOne.processInput(new CommandMessage(Command.PLACE_BET, arguments));
		final String expected = "It is not your turn to bet!";
		assertEquals(expected, result);
	}
	
	@Test
	public void playerOnePlaysValidCard() {
		playerOneWinsBet();
		final String card[] = {"Ace-of-Spades"};
		final String result = controllerOne.processInput(new CommandMessage(Command.PLAY_CARD, card));
		final String expected = "PLAYER_ONE PLAYED Ace-of-Spades";
		assertEquals(expected, result);
	}
	
	@Test
	public void playerOnePlaysInvalidCard() {
		playerOneWinsBet();
		final String card[] = {"Queen-of-Spades"};
		final String result = controllerOne.processInput(new CommandMessage(Command.PLAY_CARD, card));
		final String expected = "You don't have that card!";
		assertEquals(expected, result);
	}
	
	@Test
	public void playerTwoPlaysCardEarly() {
		playerOneWinsBet();
		final String card[] = {"Queen-of-Spades"};
		final String result = controllerTwo.processInput(new CommandMessage(Command.PLAY_CARD, card));
		final String expected = "It is not your turn! It is PLAYER_ONE's turn!";
		assertEquals(expected, result);
	}

	/**
	 * Helper function that initializes all four
	 * PlayerControllers for the four players.
	 */
	private void initializeFourControllers() {
		controllerOne.processInput(new CommandMessage(Command.REQUEST_PLAYER_ONE));
		controllerTwo.processInput(new CommandMessage(Command.REQUEST_PLAYER_TWO));
		controllerThree.processInput(new CommandMessage(Command.REQUEST_PLAYER_THREE));
		controllerFour.processInput(new CommandMessage(Command.REQUEST_PLAYER_FOUR));
	}

	/**
	 * Helper function that calls the initializeFourControllers
	 * method, and then places bets for the four players, resulting
	 * in PLAYER_ONE winning with a bet of two.
	 */
	private void playerOneWinsBet() {
		initializeFourControllers();
		final String pass[] = {"PASS"};
		final String two[] = {"TWO"};
		controllerTwo.processInput(new CommandMessage(Command.PLACE_BET, pass));
		controllerThree.processInput(new CommandMessage(Command.PLACE_BET, pass));
		controllerFour.processInput(new CommandMessage(Command.PLACE_BET, pass));
		controllerOne.processInput(new CommandMessage(Command.PLACE_BET, two));
	}
}
