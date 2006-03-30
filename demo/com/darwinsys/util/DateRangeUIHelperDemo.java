package util;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;

import com.darwinsys.util.DateRangeUIHelper;

public class DateRangeUIHelperDemo extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DateRangeUIHelperDemo().setVisible(true);
	}
	
	public DateRangeUIHelperDemo() {
		super(DateRangeUIHelperDemo.class.getName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		final JList jComboBox = new JList();
		cp.add(jComboBox);
		jComboBox.setListData(DateRangeUIHelper.dateRanges);
		JButton jb = new JButton("Action");
		cp.add(jb);
		final JTextField tf = new JTextField(30);
		tf.setEditable(false);
		cp.add(tf);
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateRangeUIHelper.Range r = (DateRangeUIHelper.Range)jComboBox.getSelectedValue();
				Date startDate = DateRangeUIHelper.getDateFromRange(r.getChoiceValue()); 
				tf.setText(startDate.toString());
			}
		});
		pack();
	}

}
