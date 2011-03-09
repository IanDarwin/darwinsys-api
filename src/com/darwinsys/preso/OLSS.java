package com.darwinsys.preso;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/** OLSS - the One Line Slide Show presenter: one line per slide */
public class OLSS {
	JFrame jf;
	JLabel tf;
	
	int DEFAULT_SIZE = 72;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OLSS driver = new OLSS();
		if (args.length != 1) {
			JOptionPane.showMessageDialog(null, "Usage: OLSS script", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		driver.loadShow(args[0]);
		driver.runShow();
	}

	private List<String> show = new ArrayList<String>();

	private static Pattern urlPattern = 
		Pattern.compile("http://([a-z.])+(/[\\w\\d.]+)+");

	public void loadShow(String fileName) {
		try {
			BufferedReader is = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = is.readLine()) != null) {
				if (line.length() == 0 || line.startsWith("#"))
					continue;
//				final Matcher matcher = urlPattern.matcher(line);
//				if (matcher.find()) {
//					String url = matcher.group(0);
//					line += "<img src='" + QRFormatter.format(url)+ "'>";
//					System.out.println(line);
//				}
				show.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void quit() {
		System.exit(0);
	}
	
	public void runShow() {
		System.out.println("There are " + show.size() + " slides");
		if (jf == null) {
			jf = new JFrame();
			jf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		if (tf == null) {
			tf = new JLabel();
			tf.setHorizontalAlignment(JLabel.CENTER);
			tf.setFont(new Font("Serif", Font.BOLD, DEFAULT_SIZE));
		}
		showPage(0);
		jf.setContentPane(tf);
		jf.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				// Auto-generated method stub				
			}

			public void keyReleased(KeyEvent e) {
				// Auto-generated method stub	
			}

			public void keyTyped(KeyEvent e) {
				final char keyChar = e.getKeyChar();
				switch (keyChar) {
				case '+':
				case ' ':
				case '>':
				case 'j':
					forward();
					break;
				case '-':
				case '<':
				case 'k':
					back();
					break;
				case 'q':
					quit();
				default:
					System.out.printf("Unknown keyChar %c %d%n", keyChar, (int)keyChar);
				}
			}
			
		});
	}

	private void forward() {
		page = Math.min(page+1, show.size() - 1);
		showPage(page);
	}
	private void back() {
		page = Math.max(page-1, 0);
		showPage(page);
	}
	int page = 0;
	
	private void showPage(int n) {
		tf.setText("<html>" + show.get(n));
		jf.setVisible(true);
		page = n;
	}


}
