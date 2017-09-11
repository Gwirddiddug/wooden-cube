package com.kaa.runtime;

import junit.framework.TestCase;

public class ApproximatorTest extends TestCase {

    public void testNumberrs(){
        int x = 0;
        System.out.println(x++==x++);
        System.out.println(x);

    }


    public void testMultiplyStep() {
        Approximator approximator = new Approximator();
        assertEquals("6*2", "12", approximator.multiplyStep("6"));
        assertEquals("15*2", "30", approximator.multiplyStep("15"));
        assertEquals("29*2", "58", approximator.multiplyStep("29"));
    }


    public void testDegreeOf2(){
        Approximator approximator = new Approximator();
        assertEquals("degree 5", "32", approximator.getDegreeOfTwo(5));
        assertEquals("degree 16", "65536", approximator.getDegreeOfTwo(16));
        System.out.println(approximator.getDegreeOfTwo(20));
        System.out.println(approximator.getDegreeOfTwo(145));
    }
}