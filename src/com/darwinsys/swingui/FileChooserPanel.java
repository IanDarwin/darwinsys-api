package com.darwinsys.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.darwinsys.util.Debug;

/** A Panel with a Filename TextField, browse button, and JFileChooser.
 * The JFileChooser is a Property and can be configured in the usual ways, e.g.,
 * <pre>
 * filePanel.getChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
 * </pre>or<pre>
 * filePanel.getChooser().addChoosableFileFilter(myFileFilter);
 * </pre>
 */
public class FileChooserPanel extends JPanel {

	private static final String DEFAULT_LABEL = "Location";
	private static final long serialVersionUID = -3104535593905377745L;
	private final JFrame parent;
	private final JFileChooser chooser;
	private final JTextField nameField;

	/** Construct a FileChooserPanel.
	 * @param par A JFrame for use in dialogs; may be null.
	 * @param label The label to use to describe the text field.
	 */
	public FileChooserPanel(JFrame par, String label) {
		this.parent = par;
		chooser = new JFileChooser();

		add(new JLabel(label));
		
		nameField = new JTextField(30);
		add(nameField);
		
		JButton b = new JButton("...");
		add(b);
		
		// If there is a value in the TF, try to use it as the starting point.
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String old = nameField.getText();
				if (old != null) {
					File f = new File(old);
					if (f.isFile()) {
						chooser.setCurrentDirectory(f.getParentFile());
					} else if (f.isDirectory()) {
						chooser.setCurrentDirectory(f);
					}
				}
				int returnVal = chooser.showOpenDialog(parent);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					Debug.println("chooser", "You chose a "
							+ (file.isFile() ? "file" : "directory")
							+ " named: " + file.getPath());
					nameField.setText(file.getAbsolutePath());
					repaint();
				}
			}
		});
	}
	
	/** Construct a FileChooserPanel with a default label
	 * @param par
	 */
	public FileChooserPanel(JFrame par) {
		this(par, DEFAULT_LABEL);
	}

	/** Get the JFileChooser, so it can be configured.
	 * @return This component's JFileChooser
	 */
	public JFileChooser getChooser() {
		return chooser;
	}
}

