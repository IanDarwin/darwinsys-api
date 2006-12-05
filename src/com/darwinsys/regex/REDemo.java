package com.darwinsys.regex;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/** Standalone Swing GUI application for demonstrating REs.
 * @author	Ian Darwin, http://www.darwinsys.com/
 * @version $Id$
 */
public class REDemo extends JPanel {

	private static final long serialVersionUID = 3257563988576317490L;
	protected Pattern pattern;
	protected Matcher matcher;
	protected JTextField patternTF, stringTF;
	protected JCheckBox compiledOK;
	protected JRadioButton match, find, findAll;
	protected JTextField matchesTF;
	protected JTextArea logTextArea;
	private static final Color[] Colors = {
		Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.LIGHT_GRAY,
		Color.MAGENTA, Color.ORANGE, Color.PINK, Color.WHITE
	};
	/** "tag" used in highlighting */
	Object onlyHighlight;
	Highlighter highlighter;

	/** "main program" method - construct and show
	 * @throws BadLocationException */
	public static void main(String[] av) throws BadLocationException {
		JFrame f = new JFrame("REDemo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		REDemo comp = new REDemo();
		f.setContentPane(comp);
		f.pack();
		f.setLocation(200, 200);
		f.setVisible(true);
	}

	/** Construct the REDemo object including its GUI
	 * @throws BadLocationException */
	public REDemo() throws BadLocationException {
		super();

		JPanel top = new JPanel();
		top.add(new JLabel("Pattern:", JLabel.RIGHT));
		patternTF = new JTextField(20);
		patternTF.getDocument().addDocumentListener(new PatternListener());
		top.add(patternTF);
		top.add(new JLabel("Syntax OK?"));
		compiledOK = new JCheckBox();
		top.add(compiledOK);

		ChangeListener cl = new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				tryMatch();
			}
		};
		JPanel switchPane = new JPanel();
		ButtonGroup bg = new ButtonGroup();
		match = new JRadioButton("Match");
		match.setSelected(true);
		match.addChangeListener(cl);
		bg.add(match);
		switchPane.add(match);
		find = new JRadioButton("Find");
		find.addChangeListener(cl);
		bg.add(find);
		switchPane.add(find);
		findAll = new JRadioButton("Find All");
		findAll.addChangeListener(cl);
		bg.add(findAll);
		switchPane.add(findAll);
		bg.setSelected(find.getModel(), true);

		JPanel strPane = new JPanel();
		strPane.add(new JLabel("String:", JLabel.RIGHT));
		stringTF = new JTextField(20);
		stringTF.getDocument().addDocumentListener(new StringListener());
		highlighter = stringTF.getHighlighter();
		onlyHighlight = highlighter.addHighlight(0, 0, DefaultHighlighter.DefaultPainter);
		strPane.add(stringTF);
		strPane.add(new JLabel("Matches:"));
		matchesTF = new JTextField(3);
		strPane.add(matchesTF);

		setLayout(new GridLayout(0, 1, 5, 5));
		add(top);
		add(strPane);
		add(switchPane);
		add(logTextArea = new JTextArea(5,40));
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

	boolean isMatch() {
		return matches;
	}

	protected void setMatches(int n) {
		matchesTF.setText(Integer.toString(n));
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

	protected boolean tryMatch() {
		if (pattern == null) {
			return false;
		}
		logTextArea.setText("");

		setMatches(false);
		setHighlightFromMatcher(null);

		int n = matcher.groupCount();
		matcher.reset(stringTF.getText());
		if (match.isSelected() && matcher.matches()) {
			setMatches(true);
			setHighlightFromMatcher(matcher);
			logTextArea.setText("");
			for (int i = 0; i <= matcher.groupCount(); i++) {
				logTextArea.append(i + " " + matcher.group(i) + "\n");
			}
		} else if (find.isSelected() && matcher.find()) {
			setMatches(true);
			setHighlightFromMatcher(matcher);
			logTextArea.setText(matcher.group());
		} else if (findAll.isSelected()) {
			int i;
			for (i = 0; i < n; i++) {
				matcher.find();
				logTextArea.append(i + ": " + matcher.group(i) + "\n");
			}
			if (i > 0) {
				setHighlightFromMatcher(matcher);
				setMatches(true);
				return true;
			}
		}

		return isMatch();
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
			System.err.println(e);
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
