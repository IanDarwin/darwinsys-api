package com.darwinsys.swingui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.*;
import java.time.temporal.*;
import java.util.ResourceBundle;
import java.util.concurrent.*;

/** This class implements a short-term timer for timing e.g.,
 * breaks during a class, lunch breaks, etc.
 * Constructor takes a RootPaneContainer argument
 * to allow use with caller's choice of JFrame or JInternalFrame
 * (other RootPaneContainers are not supported).
 * XXX Set ending time instead of time limit
 * XXX Option to close iframe on done
 */
public class BreakTimer {

	Integer minutes;

	/** Run the BreakTimer as a main program.
	 * @param args The command line arguments ("jf" for JFrame, "ji" for JInternalFrame)
	 */
	public static void main(String[] args) {
		JFrame jf = new JFrame("Break Timer");
		jf.add(BorderLayout.CENTER, new JLabel("Demo"));
		jf.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
		if (args.length == 0 || args[0].toLowerCase().startsWith("jf")) {
			// Build as a JFrame
			new BreakTimer(jf);
			UtilGUI.packAndCenter(jf);
		} else if (args[0].toLowerCase().startsWith("ji")) {
			// Build as a JInternalFrame
			jf.setSize(800, 600);
			JInternalFrame jiffy = new JInternalFrame("Timer", false, true);
			new BreakTimer(jiffy);
			jf.setGlassPane(jiffy);
			jiffy.setVisible(true);
		} else {
			throw new IllegalArgumentException("RootPaneContainer must be JFrame or JInternalFrame");
		}
		jf.setVisible(true);
	}

	private final static String NO_TIME = "00:00";
	private final static String DEFAULT_MESSAGE = "Break ends in...";
	private final static Integer[] TIMES = new Integer[]{5,10,15,30,45,60};
	private final Duration minute = Duration.of(1, ChronoUnit.MINUTES);

	private RootPaneContainer jFrameOrIFrame;
	private Container contentPane;
	private ExecutorService tp = Executors.newSingleThreadExecutor();
	private Duration duration;
	private Future<Void> handle;
	private JLabel timerLabel;
	private JTextField topText;
	private JComboBox<Integer> choice;
	/** The runnable to run when the timer expires. May be null. */
	private Runnable doneAction;
	private ResourceBundle resourceBundle;

	/** Construct a BreakTimer
	 * @param jf The JFrame or JInternalFrame
	 */
	public BreakTimer(RootPaneContainer jf) {
		this.jFrameOrIFrame = jf;
		contentPane = jf.getContentPane();
		
		// XXX add to tray

		contentPane.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();

		resourceBundle = ResourceBundle.getBundle("BreakTimer");

		topText = new JTextField(DEFAULT_MESSAGE);
		topText.setFont(new Font("Helvetica", Font.PLAIN, 48));
		topPanel.add(topText);

		JButton xButton = new JButton("X");
		xButton.addActionListener(ActionEvent ->  {
			topText.setText("");
		});
		topPanel.add(xButton);

		JButton rsButton = new JButton("Reset");
		rsButton.addActionListener(ActionEvent ->  {
			topText.setText(DEFAULT_MESSAGE);
		});
		topPanel.add(rsButton);
		contentPane.add(BorderLayout.NORTH, topPanel);

		JPanel botPanel = new JPanel();
		choice = new JComboBox<>(TIMES);
		choice.setEditable(true);

		JPanel bigPanel = new JPanel();
		timerLabel = new JLabel();
		timerLabel.setFont(new Font("Helvetica", Font.PLAIN, 256));
		timerLabel.setText(NO_TIME);
		bigPanel.add(timerLabel);
		contentPane.add(BorderLayout.CENTER, bigPanel);
		botPanel.add(choice);

		JButton start = new JButton("Start");
		botPanel.add(start);
		start.addActionListener(startAction);
		JButton plus = new JButton("+1");
		botPanel.add(plus);
		plus.addActionListener(ActionEvent ->  {
			duration = duration == null ? minute : duration.plusMinutes(1);
		});
		JButton minus = new JButton("-1");
		botPanel.add(minus);
		minus.addActionListener(ActionEvent ->  {
			duration = duration.minusMinutes(1);
		});
		JButton stop = new JButton("Stop");
		botPanel.add(stop);
		stop.addActionListener(ActionEvent ->  {
			handle.cancel(true);
		});

		JButton help = new JButton("Help");
		botPanel.add(help);
		help.addActionListener(e -> doHelp());

		contentPane.add(BorderLayout.SOUTH, botPanel);

		doneAction = DEFAULT_ACTION;
	}

	/**
	 * The ActionListener to start the timer.
	 */
	@SuppressWarnings("unchecked")
	ActionListener startAction = evt ->  {
		handle = (Future<Void>) tp.submit( () -> {
			minutes = (Integer)choice.getSelectedItem();
			// If toString()ed, would look like PT10M
			duration =
				Duration.of(minutes, ChronoUnit.MINUTES);
			if (Thread.currentThread().isInterrupted()) {
				timerLabel.setText(NO_TIME);
				return;
			}
			while (duration.toSeconds() >= 0) {
				if (duration.toHoursPart() > 0) {
					timerLabel.setText(
						String.format("%2d:%02d:%02d",
						duration.toHoursPart(),
						duration.toMinutesPart(),
						duration.toSecondsPart()));
				} else {
					timerLabel.setText(
						String.format("%2d:%02d",
						duration.toMinutesPart(),
						duration.toSecondsPart()));
				}
				try {
					Thread.sleep(999);
				} catch (InterruptedException cancelMessage) {
					timerLabel.setText(NO_TIME);
					return;
				}
				duration = duration.minusSeconds(1);
			}
			if (doneAction != null) {
				doneAction.run();
			}
		});
	};

	private final static String[] labels = { "OK", "Restart" };

	/** A useful expiry action */
	final private Runnable DEFAULT_ACTION = () -> {
		JFrame theFrame = getJFrame();
		int choice = JOptionPane.showOptionDialog(theFrame,
			String.format("%d minutes are up: %s", minutes, topText.getText()), // Message
			"Time's up",						// title (after message - why?)
			JOptionPane.YES_NO_OPTION,			// Only two buttons, labels above
			JOptionPane.QUESTION_MESSAGE,
			null,
			labels,
			labels[0]);
		switch (choice) {
		case 0:
			if (jFrameOrIFrame instanceof JInternalFrame &&
					"true".equals(resourceBundle.getString("breaktimer.help.text")))
				((JInternalFrame)jFrameOrIFrame).setVisible(false);
		case 1:
			startAction.actionPerformed(null);
			break;
		}
	};

	private JFrame getJFrame() {
		JFrame theFrame = null;
		if (jFrameOrIFrame instanceof JFrame)
			theFrame = (JFrame)jFrameOrIFrame;
		return theFrame;
	}

	/** Set the runnable to run when the timer expires. 
	 * @param runnable The Runnable; may be null. 
	 */
	public void setExpiryAction(Runnable runnable) {
		doneAction = runnable;
	}

	/**
	 * Display the Help text for the BreakTimer
	 */
	public void doHelp() {
		String text = resourceBundle.getString("breaktimer.help.text");
		JOptionPane.showMessageDialog(getJFrame(), text);
	}
}
