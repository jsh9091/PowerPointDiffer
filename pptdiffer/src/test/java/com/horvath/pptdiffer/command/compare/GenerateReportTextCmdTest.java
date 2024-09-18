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

package com.horvath.pptdiffer.command.compare;

import com.horvath.pptdiffer.Differ;
import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests operations of the GenerateReportTextCmd class.
 * @author jhorvath
 */
public class GenerateReportTextCmdTest extends AbstractTestHelper {

	@Test
	public void perform_nullDiffer_exception() {
		boolean caughtException = false;
		try {
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(null);
			cmd.perform();
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(GenerateReportTextCmd.ERROR_NULL_DIFFER));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_sameFile_reportValid() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_A);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());
			
			final String report = cmd.getReportText();
			
			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXACT_CHECK_SAME));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_COUNT_SAME));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test 
	public void perform_differentFiles_reportValid() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(SLIDE_COUNT_3_4SLIDES);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXACT_CHECK_DIFFERENT));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_COUNT_DIFFERENT));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_sameMetadata_reportUpdated() {
		File fileA = new File(EXACT_FILE_1);
		File fileB = new File(EXACT_FILE_2);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.METADATA_SAME));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_differentMetadata_reportUpdated() {
		File fileA = new File(BASIC_FILE_C);
		File fileB = new File(BASIC_FILE_D);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.METADATA_DIFFERENT));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_differentTextBothSlides_reportUpdated() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_TEXT_DIFFERENT + 0));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_TEXT_DIFFERENT + 1));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_sameTextBothSlides_reportUpdated() {
		File fileA = new File(EXACT_FILE_1);
		File fileB = new File(EXACT_FILE_2);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_TEXT_SAME + 0));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_TEXT_SAME + 1));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_wholeTextSame_reportUpdated() {
		File fileA = new File(EXACT_FILE_1);
		File fileB = new File(EXACT_FILE_2);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.WHOLE_TEXT_SAME));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_wholeTextDifferent_reportUpdated() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.WHOLE_TEXT_DIFFERENT));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}	
}
