import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** FilterGUI implements a back-and-forth list, i.e., two columns
 * and items can be moved back and forth between them with "Add" and "Del"
 * buttons.
 * <p>
 * TODO: fix balancing (have main pack() then call adjustWidths()?).
 * @author	Ian Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class FilterGUI extends JComponent {

	JList addableList;
	MyListModel addableListModel;
	JList currentList;
	MyListModel currentListModel;

	/** Construct the object including its GUI */
	public FilterGUI(Object[] data, int defaultIndex) {
		super();

		setLayout(new BorderLayout(5, 5));

		addableList = new JList();
		addableListModel = new MyListModel(addableList);
		addableList.setModel(addableListModel);
		addableList.setBorder(BorderFactory.createEtchedBorder());
		// addableList.setText("Addable");
		for (int i=0; i<data.length; i++)
			if (i != defaultIndex)
			addableListModel.add(data[i]);

		currentList = new JList();
		currentListModel = new MyListModel(currentList);
		currentList.setModel(currentListModel);
		// currentList.setText("Current");
		currentList.setBorder(BorderFactory.createEtchedBorder());
		if (defaultIndex >= 0)
			currentListModel.add(data[defaultIndex]);

		add(BorderLayout.WEST, addableList);
		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		JButton addButton, delButton;
		c.add(addButton = new JButton("-->"));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int i = addableList.getSelectedIndex();
				if (i < 0 || i >= addableListModel.size())
					return;
				Object o = addableList.getSelectedValue();
				if (o == null)
					return;
				addableListModel.remove(o);
				addableList.setSelectedIndex(-1);
				currentListModel.add(o);
			}
		});
		c.add(delButton = new JButton("<--"));
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int i = currentList.getSelectedIndex();
				if (i < 0 || i >= currentListModel.size())
					return;
				Object o = currentList.getSelectedValue();
				if (o == null)
					return;
				currentListModel.remove(o);
				currentList.setSelectedIndex(-1);
				addableListModel.add(o);
			}
		});
		add(BorderLayout.CENTER, c);
		add(BorderLayout.EAST, currentList);

		// Balance Widths
		// Should get longest toString() from list.
		addableList.setPrototypeCellValue("Some Filter Name");
		currentList.setPrototypeCellValue("Some Filter Name");
	}
}
