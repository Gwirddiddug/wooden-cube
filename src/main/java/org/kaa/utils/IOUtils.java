package org.kaa.utils;

import lombok.extern.slf4j.Slf4j;
import org.kaa.model.Atom;
import org.kaa.model.Figure;
import org.kaa.model.RealSpace;
import org.kaa.model.Solution;
import org.kaa.solver.PuzzleSolver;
import org.kaa.solver.ResultPrinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-kopilov-aa
 * @version 5.0
 * @since 19/12/2014
 * Методы для работы с сохранением и загрузкой
 */
@Slf4j
public class IOUtils {

	private IOUtils() {}

	public static void clear(String fileName) {
		File file = new File(fileName);
		if (file.exists() && !file.delete()) {
			log.error("Файл не был удалён: " + fileName);
		}
	}

	public static void saveSolution(Solution solution, String fileName) {
		ResultPrinter printer = new ResultPrinter(solution);
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
			bufferedWriter.write("--------------------------\n");
			bufferedWriter.write(printer.buildSolutionOutput(solution));
		} catch (IOException e) {
			log.error("Fail on save solution", e);
		}
	}

	public static void saveVariantsOptimized(List<RealSpace> solutions, String fileName) {
		Integer[][][] prepared = prepareSolution(solutions);

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName, false))) {
			out.writeObject(prepared);
//            System.out.println(prepared.length + " put:" + prepared.length);
		} catch (FileNotFoundException e) {
			log.error("Не найден файл: {}", fileName);
		} catch (IOException e) {
			log.error("Ошибка при сохранении вариантов", e);
		}
	}

	private static Integer[][][] prepareSolution(final List<RealSpace> solutions) {
		Integer[][][] result = new Integer[solutions.size()][][];

		int solutionIndex = 0;
		for (RealSpace solution : solutions) {
			Integer[][] arrFigures = solution.preSerialize();
			result[solutionIndex++] = arrFigures;
		}
		return result;
	}

	private static List<RealSpace> parseSolution(final Integer[][][] solutions, final RealSpace space) {
		List<RealSpace> result = new ArrayList<>(solutions.length);

		for (Integer[][] figures : solutions) {
			RealSpace solution = space.buildClone();
			for (Integer[] atoms : figures) {
				Figure figure = new Figure();
				for (Integer integer : atoms) {
					Atom atom = Atom.fromIndex(integer, space.getCubeSize());
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
