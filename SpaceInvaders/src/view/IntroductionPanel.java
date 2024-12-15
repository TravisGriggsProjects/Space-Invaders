/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.GameDetails;

/**
 *
 * @author sruiz
 */
public class IntroductionPanel extends JPanel {

  public IntroductionPanel() {

    JTextArea info = new JTextArea(25,100);
    info.append(
              "                              Welcome to Space Invaders \n"
            + "\n"
            + " This version has two setting that you can adjust\n"
            + "        Firing Interval - This is the minimum time that will be allowed between shots\n"
            + "                          The higher the value the slower you can shoot.\n"
            + "        Move Speed      - This is the speed at which your ship can move\n"
            + "                          The higher the value the faster you can move.\n"
            + "\n"
            + " You move by using the keyboard arrow keys.\n"
            + " You shoot by using the spacebar.\n"
            + "\n"
            + " Scoring is based on\n"
            + "\n"
            + " Minus "
            + GameDetails.POINTS_LOST_FOR_WASTED_SHOT
            + " points for every shot you make.\n"
            + " Plus "
            + GameDetails.POINTS_GAINED_FOR_SHIP_DESTROYED
            + " points for every alien you hit.\n"
            + "\n"
            + " The game required a login in order to store your scores.\n"
            + "\n"
            + " Scores are kept for every possible combination of Firing Interval and Move Speed\n"
            + " Both your personal high score and the high score for all registered users is kept - the Game High score."
            + "\n"
            + "\n"
            + "                                       HAVE FUN!");
    this.setLayout(new BorderLayout());
    this.add(info, BorderLayout.CENTER);
  }
}
