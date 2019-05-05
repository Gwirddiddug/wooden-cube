package org.kaa.utils;

import org.kaa.model.Atom;
import org.kaa.model.Figure;
import org.kaa.model.RealSpace;
import org.kaa.model.Solution;
import org.kaa.solver.PuzzleSolver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-kopilov-aa
 * @version 5.0
 * @since 19/12/2014
 * Методы для работы с сохранением и загрузкой
 */
public class IOUtils {

    public static void clear(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                System.err.println("Файл не был удалён: " + fileName);
            }
        }
    }

    public static void saveSolution(Solution solution, String fileName){
        FileWriter writer;
        try {
            writer = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(solution.getTextView());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveVariantsOptimized(List<RealSpace> solutions, String fileName) {
        Integer[][][] prepared = prepareSolution(solutions);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileName, false);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(prepared);
//            System.out.println(prepared.length + " put:" + prepared.length);
            out.close();
        } catch (FileNotFoundException e ) {
            System.err.println("Не найден файл: " + fileName);
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    private static Integer[][][] prepareSolution(final List<RealSpace> solutions) {
        Integer[][][] result =  new Integer[solutions.size()][][];

        int solutionIndex = 0;
        for (RealSpace solution : solutions) {
            Integer[][] arrFigures = solution.preSerialize();
            result[solutionIndex++] = arrFigures;
        }
        return result;
    }

    private static List<RealSpace> parseSolution(final Integer[][][] solutions, final RealSpace space) {
        List<RealSpace> result = new ArrayList<>(solutions.length);

        for (int i = 0; i < solutions.length; i++) {
            Integer[][] figures = solutions[i];
            RealSpace solution = space.clone();
            for (int j = 0; j < figures.length; j++) {
                Integer[] atoms = figures[j];
                Figure figure = new Figure();
                for (int k = 0; k < atoms.length; k++) {
                    Atom atom = Atom.fromIndex(atoms[k], space.getCubeSize());
                    figure.addAtom(atom);
                }
                solution.putFigure(figure);
            }
            result.add(solution);
        }
        return result;
    }

    public static List<RealSpace> loadVariantsOptimized(final String fileForRead, final RealSpace space) {
        Integer[][][] spaces = new Integer[PuzzleSolver.SERIALIZATION_PACK_SIZE][][];
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileForRead));
            spaces = (Integer[][][]) in.readObject();
            in.close();
            clear(fileForRead);
        } catch (IOException e) {
            System.err.println("Не найден файл: " + fileForRead);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parseSolution(spaces, space);
    }
}
