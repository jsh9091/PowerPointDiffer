/*
 * MIT License
 * 
 * Copyright (c) 2021 Joshua Horvath
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.horvath.pptdiffer.engine;

import java.io.File;

public class AbstractTestHelper {
	
	public static final String RESOURCES_DIRECTORY = "src" + File.separator + "test" 
			+ File.separator + "resources";
	
	public static final String BASIC_FILE_A = RESOURCES_DIRECTORY + File.separator 
			+ "basicfiles" + File.separator + "BasicFileA.pptx";
	
	public static final String BASIC_FILE_B = RESOURCES_DIRECTORY + File.separator 
			+ "basicfiles" + File.separator + "BasicFileB.pptx";
	
	public static final String EXACTFILES = RESOURCES_DIRECTORY + File.separator + "exactfiles";
	
	/**
	 * Control file for exact file tests.
	 */
	public static final String EXACT_FILE_1 = EXACTFILES + File.separator 
			+ "File-1" + File.separator + "ExactFileA.pptx";
	
	/**
	 * This file is the same as EXACT_FILE_1 in every way.
	 */
	public static final String EXACT_FILE_2 = EXACTFILES + File.separator 
			+ "File-2-Same" + File.separator + "ExactFileA.pptx";
	
	/**
	 * This file is the same as EXACT_FILE_1, except that it has a different file name.
	 * The file name does not affect comparisons, but want to test this situation anyway.
	 */
	public static final String EXACT_FILE_3 = EXACTFILES + File.separator 
			+ "File-3-DifferentName" + File.separator + "ExactFileC.pptx";
	
	/**
	 * This file is the same as EXACT_FILE_1, except that the metadata has been modified.
	 */
	public static final String EXACT_FILE_4 = EXACTFILES + File.separator 
			+ "FIle-4-DifferentMetadata" + File.separator + "ExactFileA.pptx";
	
	/**
	 * This file is mostly the same as EXACT_FILE_1, except some of the contents are different.
	 */
	public static final String EXACT_FILE_5 = EXACTFILES + File.separator 
			+ "File-5-DifferentContent" + File.separator + "ExactFileA.pptx";


	public static final String NOT_PPTX_FILES = RESOURCES_DIRECTORY + File.separator + "notpptx";
	
	public static final String NOT_PPTX_A = NOT_PPTX_FILES + File.separator + "notpptx-A.txt";
	
	public static final String NOT_PPTX_B = NOT_PPTX_FILES + File.separator + "notpptx-B.txt";
	

	public static final String SLIDE_COUNTS = RESOURCES_DIRECTORY + File.separator + "slideCounts";

	public static final String SLIDE_COUNT1_3SLIDES = SLIDE_COUNTS + File.separator + "SlideCount1-3slides.pptx";

	public static final String SLIDE_COUNT2_3SLIDES = SLIDE_COUNTS + File.separator + "SlideCount2-3slides.pptx";

	public static final String SLIDE_COUNT3_4SLIDES = SLIDE_COUNTS + File.separator + "SlideCount3-4slides.pptx";

}
