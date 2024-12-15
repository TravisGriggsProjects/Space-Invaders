/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import model.SettingsListModel;

/**
 *
 */
public class SettingsView extends JDialog implements MouseListener{
  private String result = null;

  public SettingsView(Frame owner, String title) {
    super(owner, title);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    JScrollPane pane = new JScrollPane();
    JList list = new JList(new SettingsListModel());
    list.addMouseListener(this);
    pane.setViewportView(list);
    this.add(pane);
    this.setPreferredSize(new Dimension(170, 250));
    this.pack();    
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2){ //double click
      if (((JList)e.getSource()).getSelectedIndex() >= 0){
        result = ((JList)e.getSource()).getSelectedValue().toString();
      }
      this.setVisible(false);
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  public String getResult(){
    return this.result;
  }

}
