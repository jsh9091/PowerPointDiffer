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
import java.time.LocalDate;

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
	public void perform_wholeTextSame_reportUpdatedEmpty() {
		File fileA = new File(IMAGE_FILE_A);
		File fileB = new File(IMAGE_FILE_B);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.WHOLE_TEXT_EMPTY));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_wholeTextSame_reportUpdatedSame() {
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
	public void perform_wholeTextDifferent_reportUpdatedDifferent() {
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
	
	@Test
	public void perform_differentText_reportShowsExpectedActual() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);

		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_TEXT_EXPECTED + "Order"
					+ GenerateReportTextCmd.SLIDE_TEXT_ACTUAL + "Go" + GenerateReportTextCmd.SLIDE_TEXT_CLOSE));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}	

	@Test
	public void perform_extraTextFileA_reportShowsExtraTextFinding() {
		File fileA = new File(EXTRA_TEXT);
		File fileB = new File(EXTRA_TEXT_MISSING);

		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXTRA_TEXT_FILE_A));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXTRA_TEXT + "The quick brown fox jumps over the lazy dog."));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}

	@Test
	public void perform_extraTextFileB_reportShowsExtraTextFinding() {
		File fileA = new File(EXTRA_TEXT_MISSING);
		File fileB = new File(EXTRA_TEXT);

		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXTRA_TEXT_FILE_B));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.EXTRA_TEXT + "The quick brown fox jumps over the lazy dog."));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_shapeCounts_reportProperlyContainsShapeCounts() {
		File fileA = new File(SLIDE_COUNT_1_3SLIDES);
		File fileB = new File(SHAPE_TEST_FILE);
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains("On slide index 0 File A contains 2 shapes. File B contains 0 shapes."));
			Assert.assertTrue(report.contains("On slide index 1 File A contains 2 shapes. File B contains 1 shape."));
			Assert.assertTrue(report.contains("On slide index 2 File A contains 2 shapes. File B contains 2 shapes."));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_tableCounts_reportProperlyContainsTableCounts() {
		File fileA = new File(BASIC_FILE_E);
		File fileB = new File(BASIC_FILE_D);
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains("On slide index 1 File A contains 1 table. File B contains 1 table."));
			Assert.assertTrue(report.contains("On slide index 2 File A contains 2 tables. File B contains 0 tables."));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_fileBhasFewerSlides_ReportUpdated() {
		File fileA = new File(BASIC_FILE_C); // three slides
		File fileB = new File(BASIC_FILE_A); // two slides 
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_COMP_ENDED));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_imageCounts_reportProperlyContainsShapeCounts() {
		File fileA = new File(IMAGE_FILE_A);
		File fileB = new File(BASIC_FILE_A);
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.IMAGE_COUNT_DIFFERENT));
			Assert.assertTrue(report.contains("File " + fileA.getName() + " contains 2 images."));
			Assert.assertTrue(report.contains("File " + fileB.getName() + " contains 0 images."));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_noImages_noImageInfoInReport() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertFalse(report.contains(GenerateReportTextCmd.IMAGE_INFO_TEXT));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_images_imageInfoInReport() {
		File fileA = new File(IMAGE_FILE_A);
		File fileB = new File(IMAGE_FILE_C);
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.IMAGE_INFO_TEXT));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_reportgenerated_dateInfoPresent() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(SLIDE_COUNT_3_4SLIDES);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();
			
			// build up control data
			LocalDate today = LocalDate.now();
			String dayOfWeek = today.getDayOfWeek().toString().toLowerCase();
			dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);
			String month = today.getMonth().toString().toLowerCase();
			month = month.substring(0, 1).toUpperCase() + month.substring(1);

			Assert.assertTrue(report.contains(dayOfWeek));
			Assert.assertTrue(report.contains(month));
			Assert.assertTrue(report.contains(Integer.toString(today.getYear())));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_reportgenerated_fileNamesPresent() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(SLIDE_COUNT_3_4SLIDES);
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(diff.getPpdFileA().getFileName() + " vs " + diff.getPpdFileB().getFileName()));
			Assert.assertTrue(report.contains(diff.getRawFileA().getAbsolutePath()));
			Assert.assertTrue(report.contains(diff.getRawFileB().getAbsolutePath()));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_differentShapes_shapeNamesInReport() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(IMAGE_FILE_A);
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_SHAPE_NAMES_FILE_A));
			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_SHAPE_NAMES_FILE_B));
			// slide 1
			Assert.assertTrue(report.contains("[Title 1, Subtitle 2]"));
			Assert.assertTrue(report.contains("[Picture 1]"));
			// slide 2
			Assert.assertTrue(report.contains("[Title 1, Content Placeholder 2]"));
			Assert.assertTrue(report.contains("[Picture 3]"));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void perform_slideHaveDifferentLayouts_layoutsInreport() {
		File fileA = new File(BASIC_FILE_F);
		File fileB = new File(BASIC_FILE_A);
		
		try {
			Differ diff = new Differ(fileA, fileB);

			GenerateReportTextCmd cmd = new GenerateReportTextCmd(diff);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			final String report = cmd.getReportText();

			Assert.assertTrue(report.contains(GenerateReportTextCmd.SLIDE_LAYOUTS_DIFFERENT));
			Assert.assertTrue(report.contains("File A: slide layout: Section Header"));
			Assert.assertTrue(report.contains("File B: slide layout: Title and Content"));

		} catch (PpdException ex) {
			Assert.fail();
		}
	}
}
