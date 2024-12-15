package model;

/**
 * Used for driving the options in the firing interval drop down list
 */
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

@SuppressWarnings("serial")
public class FiringIntervalComboBoxModel extends AbstractListModel implements ComboBoxModel {

    public static final int[] choices = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
    private Integer selection;

    public FiringIntervalComboBoxModel() {
        selection = null;
    }

    @Override
    public Object getElementAt(int index) {
        return choices[index];
    }

    @Override
    public int getSize() {
        return choices.length;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        // to select and register an
        selection = (Integer) anItem;
    } // item from the pull-down list

    // Methods implemented from the interface ComboBoxModel
    @Override
    public Object getSelectedItem() {
        // to add the selection to the combo box
        return selection;
    }

}
