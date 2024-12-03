/*
 * MIT License
 * 
 * Copyright (c) 2024 Joshua Horvath
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

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.junit.Assert;

import com.horvath.pptdiffer.command.io.LoadPptxCmd;
import com.horvath.pptdiffer.command.parse.ParsePptxCmd;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

public class AbstractTestHelper {
	
	public static final String RESOURCES_DIRECTORY = "src" + File.separator + "test" 
			+ File.separator + "resources";
	
	public static final String BASIC_FILE_A = RESOURCES_DIRECTORY + File.separator 
			+ "basicfiles" + File.separator + "BasicFileA.pptx";
	
	public static final String BASIC_FILE_B = RESOURCES_DIRECTORY + File.separator 
			+ "basicfiles" + File.separator + "BasicFileB.pptx";
	
	public static final String BASIC_FILE_C = RESOURCES_DIRECTORY + File.separator 
			+ "basicfiles" + File.separator + "BasicFileC.pptx";
	
	public static final String BASIC_FILE_D = RESOURCES_DIRECTORY + File.separator 
			+ "basicfiles" + File.separator + "BasicFileD.pptx";
	
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
	
	/**
	 * Files under slide counts are for the purpose of testing features related to
	 * counting the number of slides in a PowerPoint workbook.
	 */
	public static final String SLIDE_COUNTS = RESOURCES_DIRECTORY + File.separator + "slideCounts";

	public static final String SLIDE_COUNT_1_3SLIDES = SLIDE_COUNTS + File.separator + "SlideCount1-3slides.pptx";

	public static final String SLIDE_COUNT_2_3SLIDES = SLIDE_COUNTS + File.separator + "SlideCount2-3slides.pptx";

	public static final String SLIDE_COUNT_3_4SLIDES = SLIDE_COUNTS + File.separator + "SlideCount3-4slides.pptx";
	
	public static final String PARSE_TEXT_DIRECTORY = RESOURCES_DIRECTORY + File.separator + "parseText";

	public static final String TABLE_TEXT = PARSE_TEXT_DIRECTORY + File.separator + "TableText.pptx";
	
	public static final String WHOLE_TEXT = PARSE_TEXT_DIRECTORY + File.separator + "WholeTextExtract.pptx";

	public static final String REPORT_FILES = RESOURCES_DIRECTORY + File.separator + "reportFiles";
	
	public static final String EXTRA_TEXT = REPORT_FILES + File.separator + "ExtraText.pptx";
	public static final String EXTRA_TEXT_MISSING = REPORT_FILES + File.separator + "ExtraText_B.pptx";
	
	public static final String SHAPE_FILES = RESOURCES_DIRECTORY + File.separator + "shapeCounts";
	public static final String SHAPE_TEST_FILE = SHAPE_FILES + File.separator + "shapeCounts.pptx";
	
	/**
	 * Helper method to read in .pptx files and return POI xml slide-show objects in an array.
	 * 
	 * @param path String
	 * @return XMLSlideShow[]
	 */
	protected XMLSlideShow[] loadPptxFilesHelper(final String pathA, final String pathB) {
		File fileA = new File(pathA);
		File fileB = new File(pathB);
		
		LoadPptxCmd cmd = new LoadPptxCmd(fileA, fileB);
		try {
			cmd.perform();

		} catch (PpdException ex) {
			Assert.fail();
		}

		return new XMLSlideShow[] {cmd.getPoiFileA(), cmd.getPoiFileB()};
	}
	
	/**
	 * Helper method to parse POI XMLSlideShow objects into PPD PptxSlideShow objects. 
	 * 
	 * @param fileA XMLSlideShow
	 * @param fileB XMLSlideShow 
	 * @return PptxSlideShow[]
	 */
	protected PptxSlideShow[] parsePptxSlideShowsHelper(XMLSlideShow fileA, XMLSlideShow fileB) {
		
		ParsePptxCmd cmd = new ParsePptxCmd(fileA, fileB);
		try {
			cmd.perform();

		} catch (PpdException ex) {
			Assert.fail();
		}
		
		return new PptxSlideShow[] {cmd.getPpdFileA(), cmd.getPpdFileB()};
	}
}
