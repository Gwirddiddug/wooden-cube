package com.kaa.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Created by Gwirggiddug on 05.06.2016.
 * базовая панель для всех панелей форм
 */
public class BasePanel extends JPanel {

//    private final Border defaultBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    private final Border defaultBorder = BorderFactory.createTitledBorder("");
    private final TitledBorder border;

    public BasePanel() {
        super();
        setLayout(new BorderLayout(1,1));
//        setBorder(defaultBorder);
        border = new TitledBorder("");
        setBorder(border);
    }

    public BasePanel(String title) {
        this();
        setTitle(title);
    }

    protected void setTitle(String title){
        border.setTitle(title);
    }

    @Override
    public Component add(Component comp) {
        Component add = super.add(comp);
        if (add instanceof JComponent){
            ((JComponent)comp).setBorder(defaultBorder);
        }
        return add;
    }
}
