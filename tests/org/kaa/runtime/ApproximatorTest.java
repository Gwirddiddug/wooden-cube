package org.kaa.runtime;

import org.junit.jupiter.api.Assertions;

public class ApproximatorTest {


    public void testNumbers(){
        int x = 0;
        System.out.println(x++==x++);
        System.out.println(x);

    }


    public void testMultiplyStep() {
        Approximator approximator = new Approximator();
        Assertions.assertEquals("6*2", "12", approximator.multiplyStep("6"));
        Assertions.assertEquals("15*2", "30", approximator.multiplyStep("15"));
        Assertions.assertEquals("29*2", "58", approximator.multiplyStep("29"));
    }


    public void testDegreeOf2(){
        Approximator approximator = new Approximator();
        Assertions.assertEquals("degree 5", "32", approximator.getDegreeOfTwo(5));
        Assertions.assertEquals("degree 16", "65536", approximator.getDegreeOfTwo(16));
        System.out.println(approximator.getDegreeOfTwo(20));
        System.out.println(approximator.getDegreeOfTwo(145));
    }
}