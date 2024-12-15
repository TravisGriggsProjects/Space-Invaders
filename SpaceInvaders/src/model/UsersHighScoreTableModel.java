/*
 * Provides the data necessary for the JTable to display the users high scores
 */
package model;

import java.util.List;
import javax.swing.table.*;
import model.GameDetails;

/**
 *
 */
@SuppressWarnings("serial")
public class UsersHighScoreTableModel extends DefaultTableModel {

  List<GameDetails> highScores;
  String[] columnNames;

  public UsersHighScoreTableModel(String[] columnNames, List<GameDetails> highScores) {
    this.columnNames = columnNames;
    this.highScores = highScores;
  }

  public String getColumnName(int column) {
    return columnNames[column];
  }

  public int getRowCount() {
    if (highScores == null)
      return 0;
    return highScores.size();
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if (columnIndex == 0) { //move speed
      return highScores.get(rowIndex).getGameSettings().getMoveSpeed();
    } else if (columnIndex == 1) { // firing interval
      return highScores.get(rowIndex).getGameSettings().getFiringInterval();
    } else { // high score (== 2)
      return highScores.get(rowIndex).getHighScore();
    }
  }
}
