import com.darwinsys.util.WindowCloser;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

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

	Vector addable = new Vector();
	Vector current = new Vector();
	JList toAddList;
	JList currentList;

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
		setLayout(new BorderLayout());
		for (int i=0; i<filters.length; i++)
			addable.add(filters[i]);

		toAddList = new JList(addable);
		toAddList.setBorder(BorderFactory.createEtchedBorder());
		// toAddList.setText("Addable");
		currentList = new JList(current);
		// currentList.setText("Current");
		currentList.setBorder(BorderFactory.createEtchedBorder());

		current.add(filters[1]);
		add(BorderLayout.WEST, toAddList);
		JPanel c = new JPanel();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		JButton addButton, delButton;
		c.add(addButton = new JButton("-->"));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Object o = toAddList.getSelectedValue();
				if (o == null)
					return;
				addable.remove(o);
				current.add(o);
				updateViews();
			}
		});
		c.add(delButton = new JButton("<--"));
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Object o = currentList.getSelectedValue();
				if (o == null)
					return;
				addable.add(o);
				current.remove(o);
				updateViews();
			}
		});
		add(BorderLayout.CENTER, c);
		add(BorderLayout.EAST, currentList);
	}

	private void updateViews() {
		toAddList.setListData(addable);
		currentList.setListData(current);
	}

}
