package controller;

import datastructures.TrieSearchable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class LiveComboBoxRenderer extends JLabel implements ListCellRenderer {

    public LiveComboBoxRenderer() {
        setOpaque(true);
        setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof TrieSearchable) {
            setText(((TrieSearchable) value).getTrieRepresentation());
        } else {
            setText(value.toString());
        }
        return this;
    }

}