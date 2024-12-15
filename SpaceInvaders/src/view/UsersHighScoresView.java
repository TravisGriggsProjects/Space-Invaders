/*
 * Displays the users high score using a JTable
 */

package view;


import java.awt.Frame;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import model.GameDetails;
import model.UsersHighScoreTableModel;

/**
 *
 */
class UsersHighScoresView extends JDialog{

  public UsersHighScoresView(List<GameDetails> highScores, Frame owner) {
    super(owner, "Your High Scores");
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    JScrollPane pane = new JScrollPane();
    JTable table = new JTable(new UsersHighScoreTableModel(new String[]{"Move Speed", "Firing Interval", "High Score"}, highScores));
    pane.setViewportView(table);
    this.add(pane);
    this.pack();
  }

}
