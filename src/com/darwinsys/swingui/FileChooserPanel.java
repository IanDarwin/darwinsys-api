package com.darwinsys.swingui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	private final JTextField fileNameTextField;
	private List<PropertyChangeListener> listeners;

	/** Construct a FileChooserPanel.
	 * @param par A JFrame for use in dialogs; may be null.
	 * @param label The label to use to describe the text field.
	 */
	public FileChooserPanel(JFrame par, String label) {
		this.parent = par;
		chooser = new JFileChooser();
		
		listeners = new ArrayList<PropertyChangeListener>();

		add(new JLabel(label));
		ActionListener goListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// If there is a value in the TF, use it as the starting point.
				String old = fileNameTextField.getText();
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
					fileNameTextField.setText(file.getAbsolutePath());
					repaint();
					firePropertyChangeEvent(file);
				}
			}
		};
		
		fileNameTextField = new JTextField(30);
		fileNameTextField.addActionListener(goListener);
		add(fileNameTextField);
		
		JButton b = new JButton("...");
		b.addActionListener(goListener);
		add(b);		
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
	
	public void setFileName(String text) {
		fileNameTextField.setText(text);
	}
	
	// PROPERT CHANGE SUPPORT
	
	/** Minimal property change notification
	 */
	public void firePropertyChangeEvent(File chosenFile) {
		PropertyChangeEvent evt = 
			new PropertyChangeEvent(this, "chosen", null, chosenFile);
		for (PropertyChangeListener list : listeners) {
			list.propertyChange(evt);
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.remove(listener);
	}
}

