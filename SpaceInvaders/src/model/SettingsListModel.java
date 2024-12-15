package model;

import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class SettingsListModel extends AbstractListModel {

  @Override
  public int getSize() {
    return 10;
  }

  @Override
  public Object getElementAt(int index) {
    return (index + 1) * 100;
  }
}
