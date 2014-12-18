package ru.tykvin.view;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FilePanel extends JPanel {

    protected static final Color MOUSE_MOVED_BACKGROUND = new Color(80, 154, 80);
    protected static final Color BASIC_BACKGROUND = new Color(54, 104, 54);
    protected static final Color BORDER_COLOR = new Color(140, 59, 59);

    private static final long serialVersionUID = 1L;

    private File file;

    public FilePanel(File f) {
        this.file = f;
        String text;
        if (f.getParentFile().getPath().length() > 10) {
            text = String.format("%10s... %s", f.getParentFile().getPath(), f.getName());
        } else {
            text = null;
        }
        JLabel label = new JLabel(String.format("%10s... %s", args) f.getName());
        setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        setBackground(BASIC_BACKGROUND);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                setBackground(MOUSE_MOVED_BACKGROUND);
                updateUI();
            }
        };
    }

}
