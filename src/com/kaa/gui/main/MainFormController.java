package com.kaa.gui.main;

import com.kaa.model.Figure;
import com.kaa.model.Puzzle;
import com.kaa.model.RealSpace;
import com.kaa.model.Space;
import com.kaa.runtime.Runtime;

import java.awt.event.WindowEvent;

/**
 * Created by Gwirggiddug on 05.06.2016.
 */
public class MainFormController {

    private MainForm form;
    private MainFormListener listener;

    public MainFormController() {
        this.listener = new MainFormListener() {
            @Override
            public void run() {
                Puzzle puzzle = new Puzzle();
                puzzle.setSpace(getCurrentSpace());
                puzzle.setFigure(getCurrentFigure());
                Runtime runtime = new Runtime(puzzle);
                runtime.execute();
            }

            @Override
            public void close() {
                form.dispatchEvent(new WindowEvent(form, WindowEvent.WINDOW_CLOSING));
            }
        };

        this.form = new MainForm(listener);
    }

    public void init() {
        form.setVisible(true);
    }

    private Figure getCurrentFigure() {
        return form.getFigure();
    }

    public RealSpace getCurrentSpace(){
        return form.getSpace();
    }

}
