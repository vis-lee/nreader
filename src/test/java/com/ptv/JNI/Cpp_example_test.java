/**
 * 
 */
package com.ptv.JNI;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Vis.Lee
 *
 */
public class Cpp_example_test extends TestCase {


    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( Cpp_example_test.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
        
        String ret_srt= Cpp_example.helloVis("Vis");
        
        System.out.println(ret_srt);
    }
    
    
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
        String ret_srt= Cpp_example.helloVis("Vis");
        
        System.out.println(ret_srt);

	}

}
