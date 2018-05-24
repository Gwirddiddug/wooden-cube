package com.kaa.gui.main;

import com.kaa.gui.BasePanel;
import com.kaa.gui.LabeledComponent;
import com.kaa.model.Figure;
import com.kaa.model.RealSpace;
import com.kaa.puzzle.spaces.CommonCube;
import com.kaa.utils.ClassUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Gwirggiddug on 05.06.2016.
 * Панель с информацией о решаемой задаче
 */
public class PuzzleInfoPanel extends BasePanel {

    private final FigureInfo figureInfo;
    private final SpaceInfo spaceInfo;
    private final JPanel solutionInfo;

    public PuzzleInfoPanel() {
        setSize(300, 200);
        setLayout(new GridLayout(3,2));
        spaceInfo = new SpaceInfo();
        add(spaceInfo);
        figureInfo = new FigureInfo();
        add(figureInfo);
        solutionInfo = getSolutionInfo();
        add(solutionInfo);
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
                instances.add(aClass.newInstance());
            } catch (InstantiationException|IllegalAccessException  e) {
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
        JLabel size;
        JLabel x, y, z;

        public SpaceInfo() {
            super("Source");
            setLayout(new GridLayout(5, 1));
            comboBox = buildComboBoxByPackage("com.kaa.puzzle.spaces");

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (comboBox.getActionCommand().equals(e.getActionCommand())){
                        RealSpace space = getSpace();
                        spaceInfo.setCountInfo(space.getSize());
                        CommonCube cube = (CommonCube) space;
                        spaceInfo.setSizeInfo(cube.getX(), cube.getY(), cube.getZ());
                    }
                }
            };
            comboBox.addActionListener(actionListener);
            add(comboBox, BorderLayout.NORTH);

            size = new JLabel();
            add(size);
            x = new JLabel("X:");
            add(x);
            y = new JLabel("Y:");
            add(y);
            z = new JLabel("Z:");
            add(z);

//            add(new LabeledComponent("size"), BorderLayout.CENTER);
        }

        private void setCountInfo(int size) {
            this.size.setText("size: \t" + String.valueOf(size));
        }

        private void setSizeInfo(int x, int y, int z) {
            this.x.setText("X:\t" + String.valueOf(x));
            this.y.setText("Y:\t" + String.valueOf(y));
            this.z.setText("Z:\t" + String.valueOf(z));
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
        JLabel size;

        public FigureInfo() {
            super("Figure");
            setLayout(new GridLayout(5, 1));
            comboBox = buildComboBoxByPackage("com.kaa.puzzle.figures");

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (comboBox.getActionCommand().equals(e.getActionCommand())){
                        Figure figure = getFigure();
                        figureInfo.setSize(figure.size());
//                    figureInfo.setX(figure.getxRC());
//                    figureInfo.setY(figure.getyRC());
//                    figureInfo.setZ(figure.getzRC());
                    }
                }
            };
            comboBox.addActionListener(actionListener);

            add(comboBox, BorderLayout.NORTH);

            size = new JLabel();
            add(size);
            setSize(String.valueOf(getFigure().size()));


        }

        public Figure getFigure() {
            return (Figure) comboBox.getSelectedItem();
        }

        public void setSize(String value) {
            this.size.setText("size:\t" + value);
        }

        public void setSize(int value) {
            setSize(String.valueOf(value));
        }
    }
}
