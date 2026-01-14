package demo;

import com.darwinsys.swingui.ColorPanel;

import javax.swing.JFrame;
import java.awt.Color;
import java.util.function.Consumer;

public class ColorPanelDemo {
	public static void main(String[] args) {
		var jf = new JFrame("ColorPanelDemo");
		Consumer<Color> handler = System.out::println;
		var cp = new ColorPanel(handler);
		jf.add(cp);
		//jf.setSize(300, 300);
		jf.pack();
		jf.setVisible(true);
	}
}
