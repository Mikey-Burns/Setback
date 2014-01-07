/**
 * This file was developed for fun by Michael Burns for a private
 * implementation of the card game Setback, also known as Pitch.
 */
package setback.application.client.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import setback.application.client.SetbackClientController;
import setback.application.client.SetbackClientView;
import setback.common.PlayerNumber;

/**
 * This class is the GUI for Placing Bets
 * @author Michael Burns
 * @version Jan 2, 2014
 */
public class PlaceBetsView extends SetbackClientView {

	private Timer connectionTimer;
	private Timer neighborBetTimer;
	private Timer bettingResolvedTimer;

	private JLabel pleaseWait;
	private JLabel myBet;
	private JLabel leftBet;
	private JLabel centerBet;
	private JLabel rightBet;

	private JButton passButton;
	private JButton twoButton;
	private JButton threeButton;
	private JButton fourButton;
	private JButton fiveButton;
	private JButton takeButton;

	/**
	 * Create the GUI for placing bets.  Just call the
	 * super constructor.
	 * @param controller  The SetbackClientController that
	 * will handle all of the communication with the server.
	 * @param frame The JFrame that the application runs in.
	 */
	public PlaceBetsView(SetbackClientController controller, JFrame frame) {
		super(controller, frame);
	}

	/**
	 * This function initializes the Place Bets screen.
	 * The background and visibility is handled by calling
	 * the super version of initialize.
	 */
	public void initialize() {
		// Background and visibility
		super.initialize();
		// Please wait message
		pleaseWait = new JLabel("Please Wait for other players");
		pleaseWait.setBounds(350, 300, 300, 20);
		frame.getContentPane().add(pleaseWait);
		// Update timer
		ActionListener updateAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String handContents = controller.userInput("SHOW_HAND");
				if (!handContents.equals("You do not have a hand yet!")) {
					connectionTimer.stop();
					displayHand(handContents);
					//displayNeighborHands(); TODO: ENABLE THIS
					bettingInitialization();
					PlayerNumber currentPlayer = PlayerNumber.valueOf(controller.userInput("GET_CURRENT_PLAYER").toUpperCase());
					if (currentPlayer.equals(controller.getLeft())) {
						waitOnPlayer(controller.getLeft());
					}
					else if (currentPlayer.equals(controller.getCenter())) {
						waitOnPlayer(controller.getCenter());
					}
					else if (currentPlayer.equals(controller.getRight())) {
						waitOnPlayer(controller.getRight());
					}
					else {
						// I am the current player
						toggleButtons(true);
					}
				}
			}
		};
		connectionTimer = new Timer(DELAY, updateAction);
		connectionTimer.start();
	}

	/**
	 * This helper function creates the betting buttons and
	 * the betting resolved timer, but leaves the buttons
	 * as unclickable.
	 */
	private void bettingInitialization() {
		final String betString = controller.getMyNumber().toString() + " BET ";
		// Pass button
		passButton = new JButton("Pass");
		passButton.setBounds(170, 250, 75, 75);
		passButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String response = controller.userInput("PLACE_BET PASS");
				if (response.startsWith(betString + "PASS")) {
					toggleButtons(false);
					myBet.setText("YOU PASSED");
					waitOnPlayer(controller.getLeft());
				}
			}
		});
		frame.getContentPane().add(passButton);
		// Two button
		twoButton = new JButton("Two");
		twoButton.setBounds(255, 250, 75, 75);
		twoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String response = controller.userInput("PLACE_BET TWO");
				if (response.startsWith(betString + "TWO")) {
					toggleButtons(false);
					myBet.setText("YOU BET TWO");
					waitOnPlayer(controller.getLeft());
				}
			}
		});
		frame.getContentPane().add(twoButton);
		// Three button
		threeButton = new JButton("Three");
		threeButton.setBounds(340, 250, 75, 75);
		threeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String response = controller.userInput("PLACE_BET THREE");
				if (response.startsWith(betString + "THREE")) {
					toggleButtons(false);
					myBet.setText("YOU BET THREE");
					waitOnPlayer(controller.getLeft());
				}
			}
		});
		frame.getContentPane().add(threeButton);
		// Four button
		fourButton = new JButton("Four");
		fourButton.setBounds(425, 250, 75, 75);
		fourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String response = controller.userInput("PLACE_BET FOUR");
				if (response.startsWith(betString + "FOUR")) {
					toggleButtons(false);
					myBet.setText("YOU BET FOUR");
					waitOnPlayer(controller.getLeft());
				}
			}
		});
		frame.getContentPane().add(fourButton);
		// Five button
		fiveButton = new JButton("Five");
		fiveButton.setBounds(510, 250, 75, 75);
		fiveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String response = controller.userInput("PLACE_BET FIVE");
				if (response.startsWith(betString + "FIVE")) {
					toggleButtons(false);
					myBet.setText("YOU BET FIVE");
					waitOnPlayer(controller.getLeft());
				}
			}
		});
		frame.getContentPane().add(fiveButton);
		// Take button
		takeButton = new JButton("Take");
		takeButton.setBounds(595, 250, 75, 75);
		takeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String response = controller.userInput("PLACE_BET TAKE");
				if (response.equals(betString + "TAKE")) {
					toggleButtons(false);
					myBet.setText("YOU TOOK THE BET");
					waitOnPlayer(controller.getLeft());
				}
			}
		});
		frame.getContentPane().add(takeButton);
		// Disable all of the buttons
		toggleButtons(false);
		// Betting resolved timer
		ActionListener bettingResolvedAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String response = controller.userInput("NO_COMMAND");
				if (response.contains("BETTING_RESOLVED")) {
					update(response);
					bettingResolvedTimer.stop();
				}
			}
		};
		bettingResolvedTimer = new Timer(DELAY, bettingResolvedAction);
		//bettingResolvedTimer.start();
		// My bet label
		myBet = new JLabel();
		myBet.setHorizontalAlignment(SwingConstants.CENTER);
		myBet.setBounds(340, 350, 160, 40);
		frame.getContentPane().add(myBet);
	}

	/**
	 * This helper function enables or disables
	 * all of the betting buttons based on the input.
	 * @param toggle The boolean flag that determines
	 * if the buttons should be enabled or disabled.
	 */
	private void toggleButtons(boolean toggle) {
		passButton.setEnabled(toggle);
		twoButton.setEnabled(toggle);
		threeButton.setEnabled(toggle);
		fourButton.setEnabled(toggle);
		fiveButton.setEnabled(toggle);
		takeButton.setEnabled(toggle);
	}

	/**
	 * This helper function handles waiting for other
	 * people to place their bets.
	 * @param playerToWaitOn
	 */
	private void waitOnPlayer(final PlayerNumber playerToWaitOn) {
		toggleButtons(false);
		ActionListener singleBetAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String response = controller.userInput("NO_COMMAND");
				if (!response.equals("No command")) {
					neighborBetTimer.stop();
					if (response.contains("BETTING_RESOLVED")) {
						System.out.println("Betting has been resolved");
					}
					if (playerToWaitOn == controller.getLeft()) {
						// Left label
						String array[] = response.split(" ");
						leftBet = new JLabel(array[0] + " " + array[1] + " " + array[2]);
						leftBet.setBounds(170, 185, 160, 40);
						frame.getContentPane().add(leftBet);
						frame.getContentPane().add(leftBet);
						frame.repaint();
						waitOnPlayer(controller.getCenter());
					} else if (playerToWaitOn == controller.getCenter()) {
						// Center label
						String array[] = response.split(" ");
						centerBet = new JLabel(array[0] + " " + array[1] + " " + array[2]);
						centerBet.setHorizontalAlignment(SwingConstants.CENTER);
						centerBet.setBounds(340, 160, 160, 40);
						frame.getContentPane().add(centerBet);
						frame.repaint();
						waitOnPlayer(controller.getRight());
					}
					else {
						// Right label
						String array[] = response.split(" ");
						rightBet = new JLabel(array[0] + " " + array[1] + " " + array[2]);
						rightBet.setHorizontalAlignment(SwingConstants.RIGHT);
						rightBet.setBounds(510, 185, 160, 40);
						frame.getContentPane().add(rightBet);
						frame.repaint();
						// It's my turn to bet.
						toggleButtons(true);
					}
				}
			}
		};
		neighborBetTimer = new Timer(DELAY, singleBetAction);
		neighborBetTimer.start();
	}
}
