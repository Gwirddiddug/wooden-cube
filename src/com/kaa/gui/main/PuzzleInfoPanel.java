package com.kaa.gui.main;

import com.kaa.gui.BasePanel;
import com.kaa.gui.LabeledComponent;
import com.kaa.model.Figure;
import com.kaa.model.RealSpace;
import com.kaa.utils.ClassUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Gwirggiddug on 05.06.2016.
 * Панель с информацией о решаемой задаче
 */
public class PuzzleInfoPanel extends BasePanel {

    private final FigureInfo figureInfo;
    private final SpaceInfo spaceInfo;

    public PuzzleInfoPanel() {
        setSize(300, 200);
        setLayout(new GridLayout(3,2));
        spaceInfo = new SpaceInfo();
        add(spaceInfo);
        figureInfo = new FigureInfo();
        add(figureInfo);
        add(getSolutionInfo());
//        add(new LabeledComponent("Total"), BorderLayout.NORTH);
    }

    private JPanel getSolutionInfo() {
        JPanel solutionPanel = new BasePanel("Info");
        solutionPanel.setLayout(new GridLayout(2,1));

        solutionPanel.add(new LabeledComponent("Checked"));
        solutionPanel.add(new LabeledComponent("Max"));

        return solutionPanel;
    }


    /**
     * Строит выпадающий список с доступными классами
     * @param packageName имя пакета в котором ищем классы
     * @return
     */
    private JComboBox buildComboBoxByPackage(String packageName) {
        JComboBox comboBox = new JComboBox();

        Class[] classes = ClassUtils.getClasses(packageName);

        ArrayList instances = new ArrayList();
        for (Class aClass : classes) {
            try {
                Object instance = aClass.newInstance();
                instances.add(instance);
            } catch (InstantiationException e) {
                //not a problem
            } catch (IllegalAccessException e) {
                //not a problem
            }
        }
        Object[] values = instances.toArray(new Object[instances.size()]);
        comboBox.setModel(new DefaultComboBoxModel<>(values));
        return comboBox;
    }

    public RealSpace getSpace() {
        return spaceInfo.getSpace();
    }

    public Figure getFigure() {
        return figureInfo.getFigure();
    }


    /**
     * Строит панель для отображения информации о заполняемой полости
     */
     private class SpaceInfo extends BasePanel{
        JComboBox comboBox;

        public SpaceInfo() {
            super("Source");
            comboBox = buildComboBoxByPackage("com.kaa.puzzle.spaces");
            add(comboBox, BorderLayout.NORTH);
            add(new LabeledComponent("size"), BorderLayout.CENTER);
        }

        public RealSpace getSpace() {
            return (RealSpace) comboBox.getSelectedItem();
        }
    }

    /**
     * Панель отображения информации о фигуре
     */
    private class FigureInfo extends BasePanel{
        JComboBox comboBox;
        public FigureInfo() {
            super("Figure");
            comboBox = buildComboBoxByPackage("com.kaa.puzzle.figures");
            add(comboBox, BorderLayout.NORTH);
            add(new LabeledComponent("size"));
        }

        public Figure getFigure() {
            return (Figure) comboBox.getSelectedItem();
        }
    }
}
