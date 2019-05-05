package org.kaa.gui.main;

import org.kaa.gui.BasePanel;
import org.kaa.gui.LabeledComponent;
import org.kaa.model.Figure;
import org.kaa.model.RealSpace;
import org.kaa.puzzle.spaces.CommonCube;
import org.kaa.utils.ClassUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Gwirggiddug on 05.06.2016.
 * Панель с информацией о решаемой задаче
 * Puzzle = space + figure
 */
public class PuzzleInfoPanel extends BasePanel {

    private final FigureInfo figureInfo;
    private final SpaceInfo spaceInfo;
    private final JPanel solutionInfo;
    private InfoPanelListener controller;

    PuzzleInfoPanel(InfoPanelListener controller) {
        this.controller = controller;
        setSize(300, 200);
        setLayout(new GridLayout(3,2));
        spaceInfo = new SpaceInfo();
        add(spaceInfo);
        figureInfo = new FigureInfo();
        add(figureInfo);
        solutionInfo = getSolutionInfo();
        add(solutionInfo);
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
    private JComboBox<RealSpace> buildComboBoxByPackage(String packageName) {

        JComboBox<RealSpace> comboBox = new JComboBox<>();
        Class<? extends RealSpace>[] classes = ClassUtils.getClasses(packageName);

        ArrayList<RealSpace> instances = new ArrayList<>();
        for (Class<? extends RealSpace> aClass : classes) {
            try {
                instances.add(aClass.newInstance());
            } catch (InstantiationException|IllegalAccessException  e) {
                //not a problem
            }
        }
        RealSpace[] values = instances.toArray(new RealSpace[0]);
        comboBox.setModel(new DefaultComboBoxModel<>(values));
        return comboBox;
    }

    RealSpace getSpace() {
        return spaceInfo.getSpace();
    }

    public Figure getFigure() {
        return figureInfo.getFigure();
    }

    public SpaceInfo getSpaceInfo() {
        return spaceInfo;
    }

    /**
     * Строит панель для отображения информации о заполняемой полости
     */
     public class SpaceInfo extends BasePanel{
        public static final String SPACE_LOCATION = "com.kaa.puzzle.spaces";
        JComboBox<RealSpace> comboBox;
        JLabel size;
        JLabel x, y, z;

        SpaceInfo() {
            super("Source");
            setLayout(new GridLayout(5, 1));
            comboBox = buildComboBoxByPackage(SPACE_LOCATION);
            if (comboBox.getItemCount()>0) {
                comboBox.setSelectedIndex(0);
            }
            add(comboBox, BorderLayout.NORTH);

            x = addLabel("Size:");

            x = addLabel("X:");
            y = addLabel("Y:");
            z = addLabel("Z:");

            ActionListener actionListener = e -> {
                if (comboBox.getActionCommand().equals(e.getActionCommand())){
                        controller.selectSpace(getSpace());

                    CommonCube cube = (CommonCube) getSpace();
                    spaceInfo.setCountInfo(cube.getSize());
                    spaceInfo.setSizeInfo(cube.getX(), cube.getY(), cube.getZ());

                }
            };
            comboBox.addActionListener(actionListener);



        }

        private JLabel addLabel(String caption){
            JLabel label = new JLabel(caption);
            add(label);
            return label;
        }

        void setCountInfo(int size) {
            this.size.setText("size: " + size);
        }

        void setSizeInfo(int x, int y, int z) {
            this.x.setText("X: " + x);
            this.y.setText("Y: " + y);
            this.z.setText("Z: " + z);
        }

        RealSpace getSpace() {
            return (RealSpace) comboBox.getSelectedItem();
        }
    }

    /**
     * Панель отображения информации о фигуре
     */
    private class FigureInfo extends BasePanel{
        static final String FIGURES_LOCATION = "com.kaa.puzzle.figures";
        JComboBox comboBox;
        JLabel size;

        FigureInfo() {
            super("Figure");
            setLayout(new GridLayout(5, 1));
            comboBox = buildComboBoxByPackage(FIGURES_LOCATION);
            if (comboBox.getItemCount()>0) {
                comboBox.setSelectedIndex(0);
            }
            ActionListener actionListener = e -> {
                if (comboBox.getActionCommand().equals(e.getActionCommand())){
                    Figure figure = getFigure();
                    figureInfo.setSize(figure.size());
                }
            };
            comboBox.addActionListener(actionListener);

            add(comboBox, BorderLayout.NORTH);

            size = new JLabel();
            add(size);
            int size = 0;
            if (getFigure() != null) {
                size = getFigure().size();
            }
            setSize(String.valueOf(size));
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
