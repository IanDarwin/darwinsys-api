import com.darwinsys.util.WindowCloser;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Demo for MyFilter
 * @author	Ian Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class FilterGUI extends JComponent {
	boolean unsavedChanges = false;
	JButton quitButton;

	/** "main program" method - construct and show */
	public static void main(String[] av) {
		// create a this object, tell it to show up
		final JFrame f = new JFrame("Filter FilterGUI");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FilterGUI comp = new FilterGUI();
		f.getContentPane().add(comp);
		f.addWindowListener(new WindowCloser(f));
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}

	JList addableList;
	MyListModel addableListModel;
	JList currentList;
	MyListModel currentListModel;

	/** Construct the object including its GUI */
	public FilterGUI() {
		super();

		/** Inner class to represent real MyFilter implementations */
		class BasicFilter extends MyFilter {
			String title;
			BasicFilter(String s) {
				title = s;
			}
			public String toString() {
				return title;
			}
			public void write(byte[] data) throws MyFilterException {
				next.write(data);
			}
		}
		MyFilter[] filters = { 
			new BasicFilter("Basic Copy"),
			new BasicFilter("Noise Reduction"),
			new BasicFilter("RLE")
		};
		int DEFAULT_FILTER = 1;	// i.e., filters[DEFAULT_FILTER] is default
		setLayout(new BorderLayout(5, 5));

		addableList = new JList();
		addableListModel = new MyListModel(addableList);
		addableList.setModel(addableListModel);
		addableList.setBorder(BorderFactory.createEtchedBorder());
		// addableList.setText("Addable");
		for (int i=0; i<filters.length; i++)
			if (i != DEFAULT_FILTER)
			addableListModel.add(filters[i]);

		currentList = new JList();
		currentListModel = new MyListModel(currentList);
		currentList.setModel(currentListModel);
		// currentList.setText("Current");
		currentList.setBorder(BorderFactory.createEtchedBorder());
		currentListModel.add(filters[DEFAULT_FILTER]);

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
		addableList.setPrototypeCellValue("Some Filter Name");
		currentList.setPrototypeCellValue("Some Filter Name");
	}

}
