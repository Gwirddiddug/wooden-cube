package com.kaa.model;

/**
 * Created by kopylov-aa on 04.12.2014.
 */
public class Solution extends RealSpace {
    public Solution(RealSpace variant) {
        super(variant.cubeSize);
        super.figures = variant.figures;
    }
}
