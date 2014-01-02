package com.darwinsys.swingui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;

/** FilterGUI implements a back-and-forth list, ie, two columns of items, in
 * which items can be moved back and forth between them with "Add" and "Del"
 * buttons.
 * This is meant to provide a framework for Java programs that want
 * to provide chaining of various filters, or any other example where
 * you want to move items between an inactive list and an active list.
 * It does some neat things, including balancing the widths of the
 * two JLists.
 * <p>The test classes are called Filters because this type of selection GUI
 * is often used to select chained filters, but the FilterGUI is much
 * more general than this: a list of files to include/exclude, a list of
 * users, or a list of filters; any of these can be used.
 * <p>
 * TODO: fix balancing (have main pack() then call adjustWidths()?).
 * Set single-selection mode on the scrolling lists.
 * And maybe find a more standard name for this thing.
 * @author	Ian Darwin, http://www.darwinsys.com/contact.jsp
 */
public class FilterGUI<T> extends JComponent {

	private static final long serialVersionUID = -4192901768427017840L;
	protected final JList addableList;
	protected final FilterGUIListModel<T> addableListModel;
	protected final JList currentList;
	protected final FilterGUIListModel<T> currentListModel;

	/** Construct the object including its GUI */
	@SuppressWarnings("unchecked")
	public FilterGUI(final T[] data, final int defaultIndex) {
		super();

		setLayout(new BorderLayout(5, 5));

		addableList = new JList();
		addableListModel = new FilterGUIListModel<T>(addableList);
		addableList.setModel(addableListModel);
		addableList.setBorder(BorderFactory.createEtchedBorder());
		// addableList.setText("Addable");
		for (int i=0; i<data.length; i++) {
			if (i != defaultIndex)
				addableListModel.add(data[i]);
		}

		currentList = new JList();
		currentListModel = new FilterGUIListModel<T>(currentList);
		currentList.setModel(currentListModel);
		// currentList.setText("Current");
		currentList.setBorder(BorderFactory.createEtchedBorder());
		if (defaultIndex >= 0)
			currentListModel.add(data[defaultIndex]);

		add(BorderLayout.WEST, addableList);
		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		JButton addButton;
		c.add(addButton = new JButton("-->"));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int i = addableList.getSelectedIndex();
				if (i < 0 || i >= addableListModel.size())
					return;
				T o = (T)addableList.getSelectedValue();
				if (o == null)
					return;
				addableListModel.remove(o);
				addableList.setSelectedIndex(-1);
				currentListModel.add(o);
			}
		});
		JButton delButton;
		c.add(delButton = new JButton("<--"));
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int i = currentList.getSelectedIndex();
				if (i < 0 || i >= currentListModel.size())
					return;
				T o = (T) currentList.getSelectedValue();
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

	/** Retrieve the list of currently-selected objects */
	public java.util.List<T> getSelected() {
		return currentListModel;
	}
}
