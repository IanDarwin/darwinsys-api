package notepad;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.darwinsys.swingui.UtilGUI;

/**
 * Simple text editor, making Swing do the work.
 */
public class Notepad {
	
	JFrame jf;
	
	JTextArea ta;

	private int windowsCreated = 0;
	private int windowCount = 0;
	
	public Notepad() {
		jf = new JFrame();
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				okToClose();
				if (--windowCount <= 0) {
					System.exit(0);
				}
			}
		
		});

		ta = new JTextArea(30,70);
		jf.setContentPane(ta);
		jf.pack();
		
		createMenus();
		
		UtilGUI.centre(jf);
		Point loc = jf.getLocation();
		loc.x += windowsCreated * 20;
		loc.y += windowsCreated * 20;
		++windowsCreated;
		++windowCount;
		jf.setLocation(loc);
		jf.setVisible(true);
	}
	
	Action openAction = new OpenAction();
	class OpenAction extends AbstractAction {
		private static final long serialVersionUID = 134810980912890L;
		OpenAction() {
			super("Open");
		}
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(jf, 
				"OPEN");
		}		
	};
	Action saveAction = new SaveAction();
	class SaveAction extends AbstractAction {
		private static final long serialVersionUID = 134810980912890L;
		SaveAction() {
			super("Save");
		}
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(jf, 
				"SAVE");
		}		
	};
	Action closeAction = new CloseAction();
	class CloseAction extends AbstractAction {
		private static final long serialVersionUID = -5036606682572726640L;
		CloseAction() {
			super("Close");
		}
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(jf, 
				"CLOSE");
		}		
	};
	Action printAction = new PrintAction();
	class PrintAction extends AbstractAction {
		private static final long serialVersionUID = 42434480912890L;
		PrintAction() {
			super("Print");
		}
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(jf, 
				"PRINT");
		}		
	};

	Action exitAction = new ExitAction();
	class ExitAction extends AbstractAction {
		private static final long serialVersionUID = 31310980912890L;
		ExitAction() {
			super("Exit");
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}		
	};

	Action helpAboutAction = new HelpAboutAction();
	class HelpAboutAction extends AbstractAction {
		HelpAboutAction() {
			super("About");
		}
		private static final long serialVersionUID = 20191980912890L;
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(jf, 
				"Notepad 0.0");
		}		
	};

	private void createMenus() {
		JMenuBar mb = new JMenuBar();
		/** File, Help */
		JMenu fm, hm;
		
		jf.setJMenuBar(mb);		// Frame implements MenuContainer

		// The File Menu...
		fm = new JMenu("File");
		fm.add(openAction);
		fm.add(closeAction);
		fm.addSeparator();
		fm.add(printAction);
		fm.addSeparator();
		fm.add(exitAction);
		mb.add(fm);
		
		// The Help Menu...
		hm = new JMenu("Help");
		hm.add(helpAboutAction);
		mb.add(hm);

	}

	private boolean okToClose() {
		// TODO if unsaved changes
		// confirm via JOptionPane
		return true;
	}
	
	public void load(String fileName) throws IOException {		
		if (fileName == null) {
			throw new NullPointerException("filename is null");
		}
		BufferedReader is = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line = is.readLine()) != null) {
			ta.append(line);
			ta.append("\n");
		}
		is.close();		
	}
}
