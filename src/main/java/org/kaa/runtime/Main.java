package org.kaa.runtime;

import org.kaa.puzzle.figures.Teewee;
import org.kaa.puzzle.spaces.CommonCube;

public class Main {
	public static void main(String[] args) {
//        MainFormController controller = new MainFormController();
//        controller.init();
//		CommonCube space = new CommonCube(5, 5, 5);
//		CommonCube space = new CommonCube(5, 4, 4);
		CommonCube space = new CommonCube(6, 6, 4);
//		CommonCube space = new CommonCube(5, 4, 4);
//		CommonCube space = new CommonCube(5, 5, 2);
//		Runtime runtime = new Runtime(space, new Zigzag());
		Runtime runtime = new Runtime(space, new Teewee());
		runtime.execute();
	}

}
