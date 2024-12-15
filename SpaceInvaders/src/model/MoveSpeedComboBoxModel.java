/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 */
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

@SuppressWarnings("serial")
public class MoveSpeedComboBoxModel extends AbstractListModel implements ComboBoxModel {

	public static final double[] speeds = { 100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0 };
	private Double selection;

	public MoveSpeedComboBoxModel() {
		selection = null;
	}

	@Override
	public Object getElementAt(int index) {
		return speeds[index];
	}

	@Override
	public int getSize() {
		return speeds.length;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		// to select and register an
		selection = (Double) anItem;
	} // item from the pull-down list

	// Methods implemented from the interface ComboBoxModel
	@Override
	public Object getSelectedItem() {
		// to add the selection to the combo box
		return selection;
	}

}
