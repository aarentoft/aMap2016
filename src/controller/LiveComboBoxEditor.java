package controller;

import datastructures.Trie;
import datastructures.TrieSearchable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class LiveComboBoxEditor extends BasicComboBoxEditor {
    public LiveComboBoxEditor() {
        createEditorComponent();
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
