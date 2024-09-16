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

package com.horvath.pptdiffer;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.command.compare.GenerateReportTextCmd;
import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Performs tests on main differ class.
 * @author jhorvath 
 */
public class DifferTest extends AbstractTestHelper {
	
	@Test
	public void constructor_goodFiles_modelsCreatedWithFileNames() {
		
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			Assert.assertEquals(fileA.getName(), diff.getPpdFileA().getFileName());
			Assert.assertEquals(fileB.getName(), diff.getPpdFileB().getFileName());			
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void constructor_goodFiles_modelsCreatedWithSameNumOfSlides() {
		
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			Assert.assertEquals(diff.getPoiXmlFileA().getSlides().size(), diff.getPpdFileA().getSlideList().size());
			Assert.assertEquals(diff.getPoiXmlFileB().getSlides().size(), diff.getPpdFileB().getSlideList().size());
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void constructor_nullFiles_exception() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);
		
		boolean caughtException = false;
		try {
			// file A is null
			new Differ(null, fileB);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
		
		// reset
		caughtException = false;
		try {
			// file B is null
			new Differ(fileA, null);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
		
		// reset
		caughtException = false;
		try {
			// both files null
			new Differ(null, null);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);		
	}
	
	@Test
	public void constructor_filesDontExist_exception() {
		File fakeFileA = new File("fakeA.pptx");
		File fakeFileB = new File("fakeA.pptx");
		File goodFile = new File(BASIC_FILE_A);
		
		Assert.assertTrue(goodFile.exists());
		Assert.assertFalse(fakeFileA.exists());
		Assert.assertFalse(fakeFileB.exists());
		
		boolean caughtException = false;
		try {
			// file A is fake
			new Differ(fakeFileA, goodFile);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
		
		// reset
		caughtException = false;
		try {
			// file B is fake
			new Differ(goodFile, fakeFileB);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
		
		// reset
		caughtException = false;
		try {
			// both files fake
			new Differ(fakeFileA, fakeFileB);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);		
	}

	@Test
	public void constructor_filesNotPptx_exception() {
		File notPptxFileA = new File(NOT_PPTX_A);
		File notPptxFileB = new File(NOT_PPTX_B);
		File goodFile = new File(BASIC_FILE_A);
		
		Assert.assertTrue(goodFile.exists());
		Assert.assertTrue(notPptxFileA.exists());
		Assert.assertTrue(notPptxFileB.exists());
		
		boolean caughtException = false;
		try {
			// file A is fake
			new Differ(notPptxFileA, goodFile);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
		
		// reset
		caughtException = false;
		try {
			// file B is fake
			new Differ(goodFile, notPptxFileB);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
		
		// reset
		caughtException = false;
		try {
			// both files fake
			new Differ(notPptxFileA, notPptxFileB);
			
		} catch (PpdException ex) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);		
	}
	
	@Test
	public void constructor_sameFile_reportedToBeSame() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_A);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			Assert.assertTrue(diff.isSameFile());		
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void constructor_diferentFiles_reportedToBeDifferent() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			Assert.assertFalse(diff.isSameFile());		
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void generateReport_differentFiles_reportGenerated() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(SLIDE_COUNT_3_4SLIDES);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			final String report = diff.generateReport();
			
			Assert.assertNotNull(report);
			Assert.assertFalse(report.isEmpty());
			
			Assert.assertNotEquals(diff.slideCount_fileA(), diff.slideCount_fileB());
			
			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXACT_CHECK_DIFFERENT));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_COUNT_DIFFERENT));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void generateReport_sameFiles_reportGenerated() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_A);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			final String report = diff.generateReport();
			
			Assert.assertNotNull(report);
			Assert.assertFalse(report.isEmpty());
			
			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXACT_CHECK_SAME));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_COUNT_SAME));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}

	/**
	 * Test method serves as an example of usage of the PPD as a test helper library. 
	 */
	@Test
	public void exampleUsage() {
		File fileA = new File(EXACT_FILE_1);
		File fileB = new File(EXACT_FILE_2);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			// we expect at least the meta-data in files to be different // TODO revise new test files to have same content but different metadata
			Assert.assertTrue(diff.isSameFile());
			// we expect the files to have the same number of slides
			Assert.assertEquals(diff.slideCount_fileA(), diff.slideCount_fileB());
			
			// iterate over all slides in slide shows
			for (int i = 0; i < diff.slideCount_fileA(); i++) {
				// slide names in each file are expected to be the same
				Assert.assertEquals(diff.slideName_fileA(i), diff.slideName_fileB(i));
				// compare text parsed from slide 
				Assert.assertEquals(diff.slideText_fileA(i), diff.slideText_fileB(i));
			}
			
		} catch (PpdException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
