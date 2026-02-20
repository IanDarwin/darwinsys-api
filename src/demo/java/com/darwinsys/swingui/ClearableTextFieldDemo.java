// Demo main for ClearableTextField

void main() {
	SwingUtilities.invokeLater(() -> {

		JFrame frame = new JFrame("ClearableTextField Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(new Color(245, 245, 250));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill   = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;

		ClearableTextField search = new ClearableTextField("Search…");
		search.setPreferredSize(new Dimension(300, 36));

		ClearableTextField name = new ClearableTextField("Full name");
		name.setPreferredSize(new Dimension(300, 36));

		gbc.gridy = 0; panel.add(search, gbc);
		gbc.gridy = 1; panel.add(name,   gbc);

		frame.setContentPane(panel);
		frame.setVisible(true);
	});
}
