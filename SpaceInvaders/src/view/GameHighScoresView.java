/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.awt.Frame;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import model.GameDetails;
import model.GameHighScoreTableModel;

/**
 *
 */
public class GameHighScoresView  extends JDialog{

  public GameHighScoresView(List<GameDetails> highScores, Frame owner) {
    super(owner, "All High Scores");
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    JScrollPane pane = new JScrollPane();
    JTable table = new JTable(new GameHighScoreTableModel(new String[]{"Player", "Move Speed", "Firing Interval", "High Score"}, highScores));
    pane.setViewportView(table);
    this.add(pane);
    this.pack();
  }

}

