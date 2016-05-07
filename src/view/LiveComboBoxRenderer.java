package view;

import datastructures.TrieSearchable;

import javax.swing.*;
import java.awt.*;

public class LiveComboBoxRenderer extends JLabel implements ListCellRenderer {

    public LiveComboBoxRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof TrieSearchable) {
            setText(((TrieSearchable) value).getTrieRepresentation());
        } else {
            setText(value.toString());
        }

        JList.DropLocation dropLocation = list.getDropLocation();
        if (isSelected) {
            setForeground(Color.WHITE);
            setBackground(Color.RED);
        } else {
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
        };

        return this;
    }

}