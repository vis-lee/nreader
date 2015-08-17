package com.ptv.JNI;

public class Cpp_example {

	/*
	 * steps to implement JNI:
	 * 1. compile the java code to .class
	 *    ex: C:\Users\Redmine\Documents\workspace\git\ptv-daemon\src\test\java\com\ptv\JNI
	 *        >"C:\Program Files\Java\jdk1.8.0_51\bin\javac" Cpp_example.java
	 *        
	 * 2. generate header files by javah
	 *    ex: >"C:\Program Files\Java\jdk1.8.0_51\bin\javah" -v -cp ../../.. com.ptv.JNI.Cpp_example
	 *    
	 * 3. xcopy com_ptv_JNI_Cpp_example.h
	 * 
	 * 4. implement com_ptv_JNI_Cpp_example.cpp in your project
	 * 
	 * compile to dll
	 * 1. g++ -Wl,--add-stdcall-alias -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -shared -o visTest.dll com_ptv_JNI_Cpp_example.cpp vis_JNI_impl.cpp
	 * 
	 * 2. xcopy visTest.dll to your folder
	 * 
	 */
	
	/*
	 *  this lib (dll in windows naming) would placed in %PATH% or
	 *  manually include via -Djava.library.path="C:\path\to\DLLs" 
	 */
	static { System.loadLibrary("vistest"); }
	
    public static native String helloVis(String str);
	
    
    /*
     * NOTE:
     * 1. issue: the library couldn't execute on this platform
     *    cause: if it is because you installed TDM-GCC-64 and didn't 
     *         set flag to indicate 32bit is needed, it will compile
     *         the src to 64bit.
     *    sol: add flag: "-m32"
     *    
     * 2. issue: java.lang.unsatisfiedlinkerror no in java.library.path
     *    cause: couldn't find the lib in %PATH%
     *    sol: manually include via -Djava.library.path="C:\path\to\DLLs" 
     * 
     * 3. issue: java.lang.UnsatisfiedLinkError Ljava/lang/String
     *    cause: couldn't find the corresponding function name in lib
     *    sol: the flag "-Wl,--add-stdcall-alias" is necessary
     *    
     * 4. execute on cmd prompt as: 
     *      java -Djava.library.path=. Cpp_example_test
     *    
     */
    
}
