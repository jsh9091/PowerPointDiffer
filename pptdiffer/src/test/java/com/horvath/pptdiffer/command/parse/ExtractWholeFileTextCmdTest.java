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

package com.horvath.pptdiffer.command.parse;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Tests operations of ExtractWholeFilerTextCmd.
 * @author jhorvath
 */
public final class ExtractWholeFileTextCmdTest extends AbstractTestHelper {

	@Test
	public void perform_wholeText_retrieved() {
		File fileA = new File(WHOLE_TEXT);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
			String actual = cmd.getFileA_Text();
			
			Assert.assertTrue(actual.contains("Whole Text File"));
			Assert.assertTrue(actual.contains("All Text should be collected."));
			Assert.assertTrue(actual.contains("Note for title slide."));
			Assert.assertTrue(actual.contains("1"));
			Assert.assertTrue(actual.contains("Bullet Slide"));
			Assert.assertTrue(actual.contains("Bullet 1"));
			Assert.assertTrue(actual.contains("Bullet 2"));
			Assert.assertTrue(actual.contains("Bullet 3"));
			Assert.assertTrue(actual.contains("Note for bullet slide."));
			Assert.assertTrue(actual.contains("2"));
			Assert.assertTrue(actual.contains("Whole Text Table Slide"));
			Assert.assertTrue(actual.contains("Name	Age	ID	Start Date"));
			Assert.assertTrue(actual.contains("Miller, Barney	44	123456789	1975"));
			Assert.assertTrue(actual.contains("Potter, Sherman	60	987654321	1975"));
			Assert.assertTrue(actual.contains("Note for table slide."));
			Assert.assertTrue(actual.contains("3"));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_metadata_retrieved() {
		File fileA = new File(WHOLE_TEXT);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
			String actual = cmd.getFileA_metadata();
			
			Assert.assertTrue(actual.contains("Creator = Josh Horvath"));
			Assert.assertTrue(actual.contains("LastModifiedBy = Josh Horvath"));
			Assert.assertTrue(actual.contains("Application = Microsoft Macintosh PowerPoint"));
			Assert.assertTrue(actual.contains("PresentationFormat = Widescreen"));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_fileANull_Exception() {
		boolean caughtException = false;
		File fileA = null;
		File fileB = new File(BASIC_FILE_B);
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ExtractWholeFileTextCmd.ERROR_FILE_NULL));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_fileBNull_Exception() {
		boolean caughtException = false;
		File fileA = new File(BASIC_FILE_A);
		File fileB = null;
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ExtractWholeFileTextCmd.ERROR_FILE_NULL));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileAFake_Exception() {
		boolean caughtException = false;
		File fileA = new File("fake.pptx");
		File fileB = new File(BASIC_FILE_B);
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ExtractWholeFileTextCmd.ERROR_FILE_NOT_EXIST));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileBFake_Exception() {
		boolean caughtException = false;
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File("fake.pptx");
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ExtractWholeFileTextCmd.ERROR_FILE_NOT_EXIST));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileANotPptx_Exception() {
		boolean caughtException = false;
		File fileA = new File(NOT_PPTX_A);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ExtractWholeFileTextCmd.ERROR_FILE_NOT_PPTX));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileBNotPptx_Exception() {
		boolean caughtException = false;
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(NOT_PPTX_B);
		
		try {
			ExtractWholeFileTextCmd cmd = new ExtractWholeFileTextCmd(fileA, fileB);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ExtractWholeFileTextCmd.ERROR_FILE_NOT_PPTX));
		}
		Assert.assertTrue(caughtException);
	}
}
