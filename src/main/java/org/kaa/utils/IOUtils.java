package org.kaa.utils;

import org.kaa.model.RealSpace;
import org.kaa.model.Solution;

import java.io.*;
import java.util.LinkedList;
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
            file.delete();
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

    public static void saveVariants(List<RealSpace> solutions, String fileName) {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName, false);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(solutions);
            System.out.println("\n" + solutions.size()+ " put:" + solutions.size());
            out.close();
        } catch (FileNotFoundException e ) {
            e.printStackTrace();
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }


    public static List<RealSpace> loadVariants(String fileForRead) {
        List<RealSpace> spaces = new LinkedList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileForRead));
            spaces = (List<RealSpace>) in.readObject();
            in.close();
            new File(fileForRead).delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return spaces;
    }
}
