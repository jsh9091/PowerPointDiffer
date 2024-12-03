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

import java.io.File;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.command.io.LoadPptxCmd;
import com.horvath.pptdiffer.command.parse.ShapeCountCmd;
import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Tests operations of ShapeCountCmd.
 * @author jhorvath
 */
public class ShapeCountCmdTest extends AbstractTestHelper {

	@Test
	public void perform_nullSlide_exception() {
		boolean caughtException = false;
		
		try {
			ShapeCountCmd cmd = new ShapeCountCmd(null);
			cmd.perform();
			
		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(ShapeCountCmd.ERROR_SLIDE_NULL));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_slidesHaveShapes_countsMatchExpected() {		
		try {
			File file = new File(SHAPE_TEST_FILE);
			Assert.assertTrue(file.exists());
			
			// load our file and get POI object for PowerPoint
			// we don't care about File B, basic file A
			LoadPptxCmd loadCmd = new LoadPptxCmd(new File(SHAPE_TEST_FILE), new File(BASIC_FILE_A));
			loadCmd.perform();
			XMLSlideShow slideshow = loadCmd.getPoiFileA();
			
			int count = 0;
			for (XSLFSlide slide : slideshow.getSlides()) {
				ShapeCountCmd cmd = new ShapeCountCmd(slide);
				cmd.perform();
				
				final int actual = cmd.getCount();
				
				// compare actual count of shapes found on slide vs expected value
				Assert.assertEquals(count, actual);
				count++;
			}
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
}
