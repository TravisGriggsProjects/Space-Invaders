/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author sruiz
 */
public class IntroductionView extends JDialog{

  private UserEventListener controller;


  public IntroductionView(JFrame owner, UserEventListener controller) {
    super(owner, "WELCOME - GAME INORMATION", true);
    this.controller = controller;
    this.setLayout(new BorderLayout());
    this.add(new IntroductionPanel(), BorderLayout.CENTER);
    this.add(new seeAgainPanel(), BorderLayout.SOUTH);
    this.setResizable(false);
    this.pack();
    this.setLocationByPlatform(true);
  }
  
  class seeAgainPanel extends JPanel{

    public seeAgainPanel() {
      JCheckBox noMorePlease = new JCheckBox("Please do not show me this again");
      this.setLayout(new FlowLayout(FlowLayout.CENTER));
      this.add(noMorePlease);
      noMorePlease.addActionListener(new NoMorePleaseActionHandler());
    }
    
  }

  private class NoMorePleaseActionHandler implements ActionListener {

    public NoMorePleaseActionHandler() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      boolean wantIntroInfo;
      JCheckBox choice = (JCheckBox)e.getSource();
      
      wantIntroInfo = !choice.isSelected();
      controller.changeSettingForWantingIntroInfo(wantIntroInfo);
    }
  }  
}