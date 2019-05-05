package org.kaa.gui.main;

import org.kaa.model.Figure;
import org.kaa.model.RealSpace;

public interface InfoPanelListener {
    void selectSpace(RealSpace space);

    void selectFigure(Figure figure);
}
