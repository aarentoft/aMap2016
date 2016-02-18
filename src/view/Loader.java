package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * A JFrame which shows an image while loading the data from our data set.
 */
@SuppressWarnings("serial")
public class Loader extends JFrame {
	protected BufferedImage img;

	protected JPanel contentPane;
	protected JProgressBar progressBar;

	/**
	 * Create the graphical loader.
	 */
	public Loader() {
		try {
			// Loader image
			img = ImageIO.read(getClass().getResourceAsStream("/resources/placeholder.png"));
			setPreferredSize(new Dimension(img.getWidth(), img.getHeight() + 50));
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Add the image to the content pane
		JPanel panel_1 = new JPanel();
		JLabel lbl = new JLabel(new ImageIcon(img));
		panel_1.add(lbl);
		contentPane.add(panel_1, BorderLayout.CENTER);

		// Setup the progress bar
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setString("Loading points...");
		progressBar.setStringPainted(true);
		contentPane.add(progressBar, BorderLayout.SOUTH);

		// No windowframe and center the loader
		setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setSize(screenSize.width / 2, screenSize.height / 2);
	    setLocation(screenSize.width / 4, screenSize.height / 4);

		pack();
		setVisible(true);
	}

	/**
	 * 
	 * @param label
	 * @param progress
	 * @param max
	 */
	public void setProgress(String label, int progress, int max) {
		progressBar.setIndeterminate(false);
		progressBar.setMaximum(max);
		progressBar.setValue(progress);
		progressBar.setString(label);
	}
	
	/**
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		progressBar.setValue(progress);
	}
}