package com.darwinsys.swingui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Demo of LabelText JavaBean
 * @author Ian Darwin, http://www.darwinsys.com/
 */
public class LabelTextBeansDemo extends JFrame {

	private static final long serialVersionUID = 8039393121973901048L;

	/** Initializes the Form */
	public LabelTextBeansDemo() {
		initComponents();
		pack();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exitForm(evt);
			}
		});
		getContentPane().setLayout(new BorderLayout());

		jPanel1 = new javax.swing.JPanel();
		jPanel1.setLayout(new FlowLayout());

		jButton1 = new javax.swing.JButton();
		jButton1.setText("Display");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		jPanel1.add(jButton1);

		jButton2 = new javax.swing.JButton();
		jButton2.setText("Exit");
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		jPanel1.add(jButton2);

		getContentPane().add(jPanel1, "South");

		jPanel2 = new javax.swing.JPanel();
		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, 1));

		bean1 = new LabelText();
		bean1.setLabel("Name:");
		jPanel2.add(bean1);

		bean2 = new LabelText();
		bean2.setLabel("Address:");
		jPanel2.add(bean2);

		getContentPane().add(jPanel2, "Center");

	}// GEN-END:initComponents

	void jButton1ActionPerformed(ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		String name = bean1.getText();
		String addr = bean2.getText();
		JOptionPane.showMessageDialog(this, "Name = " + name + "\n" + "Addr = "
				+ addr, "Info", JOptionPane.INFORMATION_MESSAGE);
	}// GEN-LAST:event_jButton1ActionPerformed

	void jButton2ActionPerformed(ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		exitForm(null);
	}// GEN-LAST:event_jButton2ActionPerformed

	/** Exit the Application */
	void exitForm(WindowEvent evt) {// GEN-FIRST:event_exitForm
		System.exit(0);
	}// GEN-LAST:event_exitForm

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private JPanel jPanel1;

	private JButton jButton1;

	private JButton jButton2;

	private JPanel jPanel2;

	private LabelText bean1;

	private LabelText bean2;

	// End of variables declaration//GEN-END:variables

	public static void main(String[] args) {
		new LabelTextBeansDemo().setVisible(true);
	}

}
