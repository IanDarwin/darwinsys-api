package com.darwinsys.regex;

import com.darwinsys.swingui.FontChooser;
import com.darwinsys.swingui.UtilGUI;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.io.Serial;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/** Standalone Swing GUI application for demonstrating REs.
 * @author	Ian Darwin, http://www.darwinsys.com/
 */
public class REDemo extends JPanel {

	@Serial
	private static final long serialVersionUID = 3257563988576317490L;
	protected Pattern pattern;
	protected Matcher matcher;
	protected JLabel pattLabel, stringLabel;
	protected JTextField patternTF, stringTF;
	protected JCheckBox compiledOK;
	protected JRadioButton match, findButton, findAll;
	protected JTextField matchesTF;
	protected JTextArea logTextArea;
	/** UI components to update when the font changes */
	protected Component[] fontChangers;
	private static final Color[] Colors = {
		Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.LIGHT_GRAY,
		Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE
	};
	/** "tag" used in highlighting */
	Object onlyHighlight;
	Highlighter highlighter;

	public static void main(String[] args) throws BadLocationException {
		JFrame f = new JFrame("DarwinSys.com REDemo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		REDemo reDemo = new REDemo(f);
		f.setContentPane(reDemo);
		if (args.length == 2 && "-s".equals(args[0])) {
			reDemo.setString(args[1]);
		}
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}

	/** Construct the REDemo object including its GUI
	 * @param parent an existing JFrame in which to display
	 * @throws BadLocationException on error
	 */
	public REDemo(final JFrame parent) throws BadLocationException {
		super();

		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		bar.add(file);
		JMenuItem quitItem = new JMenuItem("Exit");
		file.add(quitItem);
		quitItem.addActionListener(evt -> System.exit(0));

		JMenu options = new JMenu("Options");
		bar.add(options);
		JMenuItem fontItem = new JMenuItem("Font");
		options.add(fontItem);
		final FontChooser fontChooser = new FontChooser(parent);
		fontItem.addActionListener(evt -> {
			fontChooser.setVisible(true);
			Font font = fontChooser.getSelectedFont();
			if (font == null) {
				System.out.println("Nothing selected");
				return;
			}
			System.out.println(font);
			for (Component c : fontChangers) {
				if (c != null) {
					c.setFont(font);
				}
			}
			parent.pack();
		});
		parent.setJMenuBar(bar);

		JPanel top = new JPanel();
		pattLabel = new JLabel("Pattern:", JLabel.RIGHT);
		top.add(pattLabel);
		patternTF = new JTextField(40);
		patternTF.getDocument().addDocumentListener(new PatternListener());
		top.add(patternTF);
		top.add(new JLabel("Syntax OK?"));
		compiledOK = new JCheckBox();
		top.add(compiledOK);

		ChangeListener cl = ce -> tryMatch();
		JPanel switchPane = new JPanel();
		ButtonGroup buttonGroup = new ButtonGroup();
		match = new JRadioButton("Match");
		match.setSelected(true);
		match.addChangeListener(cl);
		buttonGroup.add(match);
		switchPane.add(match);
		findButton = new JRadioButton("Find");
		findButton.addChangeListener(cl);
		buttonGroup.add(findButton);
		switchPane.add(findButton);
		findAll = new JRadioButton("Find All");
		findAll.addChangeListener(cl);
		buttonGroup.add(findAll);
		switchPane.add(findAll);
		buttonGroup.setSelected(findButton.getModel(), true);

		JPanel strPane = new JPanel();
		stringLabel = new JLabel("String:", JLabel.RIGHT);
		strPane.add(stringLabel);
		stringTF = new JTextField(40);
		stringTF.getDocument().addDocumentListener(new StringListener());
		highlighter = stringTF.getHighlighter();
		onlyHighlight = highlighter.addHighlight(0, 0, DefaultHighlighter.DefaultPainter);
		strPane.add(stringTF);
		strPane.add(new JLabel("Matches:"));
		matchesTF = new JTextField(3);
		strPane.add(matchesTF);
		
		JPanel bottomPanel = new JPanel();
		final JButton copyButton = new JButton("Copy Pattern");
		bottomPanel.add(copyButton);
		copyButton.addActionListener(evt -> UtilGUI.setSystemClipboardContents(REDemo.this, patternTF.getText()));
		final JButton copyDoubledButton = new JButton("Copy Pattern Backslashed");
		bottomPanel.add(copyDoubledButton);
		copyDoubledButton.addActionListener(evt -> UtilGUI.setSystemClipboardContents(REDemo.this, patternTF.getText().replaceAll("\\\\", "\\\\\\\\")));
		final JButton quitButton = new JButton("Exit");
		bottomPanel.add(quitButton);
		quitButton.addActionListener(evt -> System.exit(0));

		setLayout(new GridLayout(0, 1, 5, 5));
		add(top);
		add(strPane);
		add(switchPane);
		add(logTextArea = new JTextArea(5,40));
		add(bottomPanel);

		// Now that the components are created, add them to the list
		// that change fonts.
		fontChangers = new Component[]{
			pattLabel, patternTF, stringLabel, stringTF, 
			match, findButton, findAll,
			matchesTF, logTextArea
		};
	}

	private void setString(String s) {
		stringTF.setText(s);
	}

	boolean matches;

	protected void setMatches(boolean b) {
		matches = b;
		if (b) {
			matchesTF.setText("Yes");
		} else {
			matchesTF.setText("No");
		}
	}

	protected void tryAll() {
		tryCompile();
		String data = stringTF.getText();
		if (data != null && data.length() > 0) {
			tryMatch();
		}
	}

	protected void tryCompile() {
		pattern = null;
		try {
			pattern = Pattern.compile(patternTF.getText());
			matcher = pattern.matcher("");
			compiledOK.setSelected(true);
		} catch (PatternSyntaxException ex) {
			compiledOK.setSelected(false);
		}
	}

	protected void tryMatch() {
		if (pattern == null) {
			return;
		}
		logTextArea.setText("");

		setMatches(false);
		setHighlightFromMatcher(null);

		matcher.reset(stringTF.getText());
		if (match.isSelected() && matcher.matches()) {
			setMatches(true);
			setHighlightFromMatcher(matcher);
			logTextArea.setText("");
			for (int i = 0; i <= matcher.groupCount(); i++) {
				logTextArea.append(i + " " + matcher.group(i) + "\n");
			}
		} else if (findButton.isSelected() && matcher.find()) {
			setMatches(true);
			setHighlightFromMatcher(matcher);
			logTextArea.setText(matcher.group());
		} else if (findAll.isSelected()) {
			int i = 0;
			while (matcher.find()) {
				logTextArea.append(i++ + ": " + matcher.group() + "\n");
			}
			if (i > 0) {
				setMatches(true);
			}
		}

	}

	private void setHighlightFromMatcher(Matcher matcher) {
		int start, end;
		if (matcher == null) {
			start = end = 0;
		} else {
			start = matcher.start();
			end = matcher.end();
		}
		try {
			// System.out.printf("setHighlightFromMatcher(): %d...%d%n", start, end);
			highlighter.changeHighlight(onlyHighlight, start, end);
		} catch (BadLocationException e) {
			System.err.println("setHighlight failed with " + e);
		}
	}

	/** Any change to the pattern tries to compile the result. */
	class PatternListener implements DocumentListener {

		public void changedUpdate(DocumentEvent ev) {
			tryAll();
		}

		public void insertUpdate(DocumentEvent ev) {
			tryAll();
		}

		public void removeUpdate(DocumentEvent ev) {
			tryAll();
		}
	}

	/** Any change to the input string tries to match the result */
	class StringListener implements DocumentListener {

		public void changedUpdate(DocumentEvent ev) {
			tryMatch();
		}

		public void insertUpdate(DocumentEvent ev) {
			tryMatch();
		}

		public void removeUpdate(DocumentEvent ev) {
			tryMatch();
		}
	}

	public Color getColor(int n) {
		return Colors[n%Colors.length];
	}
}
