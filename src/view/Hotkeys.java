package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Hotkeys extends JDialog {
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public Hotkeys() {
		setTitle("Hotkeys");
		setBounds(100, 100, 550, 156);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// Text area
		JTextArea textArea = new JTextArea("Scroll wheel UP \t = Zoom in \n" +
				"Scroll Wheel DOWN \t = Zoom Out \n" +
				"Mouse Click \t\t = Panning \n" +
				"CTRL + Mouse Click \t = Drag a box to zoom \n" +
				"ALT  + Mouse Click \t = Choose road for route search \n");
        textArea.setColumns(35);
        textArea.setRows(5);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
		JLabel lb = new JLabel();
        textArea.setBackground(lb.getBackground());
		contentPanel.add(textArea);
		
		// OK button
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		// Center
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation(screenSize.width / 4, screenSize.height / 4);

		// JDialog options
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(550, 175));
		pack();
		setVisible(true);
	}

}
