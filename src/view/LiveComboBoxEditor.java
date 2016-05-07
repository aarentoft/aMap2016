package view;

import datastructures.TrieSearchable;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

public class LiveComboBoxEditor extends BasicComboBoxEditor {

    public LiveComboBoxEditor() {
        editor = new JTextField();
    }

    public Component getEditorComponent() {
        return this.editor;
    }

    public Object getItem() {
        return this.editor.getText();
    }

    public void setItem(Object item) {
        if (item == null)
            return;

        if (item instanceof TrieSearchable)
            this.editor.setText(((TrieSearchable) item).getTrieRepresentation());
        else
            this.editor.setText(item.toString());
    }
}
