package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Handles mouse events for the OSM copyright notice in the lower left corner of the map.
 */
public class OSMCopyrightNoticeMouseAdapter extends MouseAdapter {
    private String copyrightURL = "";

    public OSMCopyrightNoticeMouseAdapter(String copyrightURL) {
        this.copyrightURL = copyrightURL;
    }

    /**
     * Attempt to open the URL associated with the copyright notice in a browser.
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(copyrightURL));
        } catch (URISyntaxException | IOException ex) {
            JOptionPane.showMessageDialog(
                            null,
                            "The link " + copyrightURL + " could not be opened.\n"
                                    + "---\n"
                                    + ex.getMessage(),
                            "Could not open link", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Change the cursor to indicate that the copyright notice is clickable.
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        ((Component) e.getSource()).setCursor(Cursor
                .getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Reset the cursor icon.
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
        ((Component) e.getSource()).setCursor(Cursor
                .getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
