package com.darwinsys.util;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import com.darwinsys.util.DateRangeUIHelper;

public class DateRangeUIHelperDemo extends JFrame {

	private static final long serialVersionUID = -1621211822931751327L;

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
		cp.add(new JLabel("Modified in last:"));
		final JList jList = new JList();
		cp.add(jList);
		jList.setListData(DateRangeUIHelper.dateRanges);
		jList.setSelectedIndex(0);
		JButton jb = new JButton("Show starting date:");
		cp.add(jb);
		final JTextField tf = new JTextField(30);
		tf.setEditable(false);
		cp.add(tf);
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateRangeUIHelper.Range r = (DateRangeUIHelper.Range)jList.getSelectedValue();
				Date startDate = DateRangeUIHelper.getDateFromRange(r.getChoiceValue()); 
				tf.setText(startDate.toString());
			}
		});
		pack();
	}

}
