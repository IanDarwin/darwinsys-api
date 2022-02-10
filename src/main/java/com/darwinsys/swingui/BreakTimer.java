package com.darwinsys.swingui;

import java.awt.*;
import javax.swing.*;
import java.time.*;
import java.time.temporal.*;
import java.util.concurrent.*;

/** This class implements a short-term timer for timing e.g.,
 * breaks during a class, lunch breaks, etc.
 * Constructor takes a RootPaneContainer argument
 * to allow use with caller's choice of JFrame or JInternalFrame
 * (other RootPaneContainers are not supported).
 */
public class BreakTimer {

	/** Run the BreakTimer as a main program */
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

	final static String NO_TIME = "00:00";
	final static String DEFAULT_MESSAGE = "Break ends in...";
	final static Integer[] TIMES = new Integer[]{5,10,15,30,60};
	final Duration minute = Duration.of(1, ChronoUnit.MINUTES);

	RootPaneContainer jf;
	ExecutorService tp = Executors.newSingleThreadExecutor();
	Duration duration;
	Future handle;

	/** Construct a BreakTimer
	 * @param jf The JFrame or JInternalFrame
	 */
	public BreakTimer(RootPaneContainer jf) {
		this.jf = jf;
		var cp = jf.getContentPane();

		// XXX add to tray

		cp.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();

		JTextField topText = new JTextField(DEFAULT_MESSAGE);
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
		cp.add(BorderLayout.NORTH, topPanel);

		JPanel botPanel = new JPanel();
		JComboBox<Integer> choice = new JComboBox<>(TIMES);

		JPanel bigPanel = new JPanel();
		JLabel timerLabel = new JLabel();
		timerLabel.setFont(new Font("Helvetica", Font.PLAIN, 256));
		timerLabel.setText(NO_TIME);
		bigPanel.add(timerLabel);
		cp.add(BorderLayout.CENTER, bigPanel);
		botPanel.add(choice);
		JButton start = new JButton("Start");
		botPanel.add(start);
		start.addActionListener(ActionEvent ->  {
			handle = tp.submit( () -> {
				// When toString()ed, will look like PT10M
				duration =
					Duration.of((Integer)choice.getSelectedItem(), ChronoUnit.MINUTES);
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
			});
		});
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

		cp.add(BorderLayout.SOUTH, botPanel);
	}
}
