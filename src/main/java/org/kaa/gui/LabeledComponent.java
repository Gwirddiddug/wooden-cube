package org.kaa.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Gwirggiddug on 05.06.2016.
 */
public class LabeledComponent extends JPanel {

	public LabeledComponent(String label) {
		setSize(200, 100);
//        setBorder(BorderFactory.);
		setLayout(new GridLayout(2, 1));
		add(new JLabel(label + ":"));
		add(new JLabel());
	}
}
