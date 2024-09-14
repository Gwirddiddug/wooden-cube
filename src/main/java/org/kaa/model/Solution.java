package org.kaa.model;

/**
 * Created by kopylov-aa on 04.12.2014.
 */
public class Solution extends RealSpace {
	public Solution(RealSpace variant) {
		super(variant);
//        super.figures = variant.figures;
		super.compactFigures = variant.compactFigures;
	}
}
