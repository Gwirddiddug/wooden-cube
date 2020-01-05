package org.kaa.runtime;

import org.kaa.puzzle.figures.Zigzag;
import org.kaa.puzzle.spaces.CommonCube;

public class Main {
	public static void main(String[] args) {
//        MainFormController controller = new MainFormController();
//        controller.init();
		Runtime runtime = new Runtime(new CommonCube(5, 4, 4), new Zigzag());
		runtime.execute();
	}

}
