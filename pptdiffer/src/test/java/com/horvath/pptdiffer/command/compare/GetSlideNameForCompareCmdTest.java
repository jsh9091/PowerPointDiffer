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

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Tests operations of GetSlideNameForCompareCmd.
 *  @author jhorvath 
 */
public class GetSlideNameForCompareCmdTest extends AbstractTestHelper {
	
	@Test
	public void perform_nullFile_exception() {
		boolean caughtException = false;
		
		try {
			GetSlideNameForCompareCmd cmd = new GetSlideNameForCompareCmd(0, null);
			cmd.perform();
			
		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(GetSlideNameForCompareCmd.ERROR_NULL_SLIDESHOW));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_invalidIndex_exception() {
		boolean caughtException = false;
		
		try {
			GetSlideNameForCompareCmd cmd = new GetSlideNameForCompareCmd(-1, new PptxSlideShow());
			cmd.perform();
			
		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(GetSlideNameForCompareCmd.ERROR_NEGATIVE_INDEX));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test 
	public void perform_indexTooLargeForFile_emtpyString() {
		
		XMLSlideShow[] xmlArray = loadPptxFilesHelper(BASIC_FILE_A, BASIC_FILE_B);
		PptxSlideShow[] ppdArray = parsePptxSlideShowsHelper(xmlArray[0], xmlArray[1]);
		
		try {
			GetSlideNameForCompareCmd cmd = new GetSlideNameForCompareCmd(100, ppdArray[0]);
			cmd.perform();
			
			Assert.assertTrue(cmd.getSlideName().isEmpty());
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	@Test 
	public void perform_validData_slideNameReturned() {
		XMLSlideShow[] xmlArray = loadPptxFilesHelper(BASIC_FILE_A, BASIC_FILE_B);
		PptxSlideShow[] ppdArray = parsePptxSlideShowsHelper(xmlArray[0], xmlArray[1]);
		
		try {
			GetSlideNameForCompareCmd cmd = new GetSlideNameForCompareCmd(0, ppdArray[0]);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());
			
			String slideName = cmd.getSlideName();
			
			Assert.assertNotNull(slideName);
			Assert.assertTrue(slideName.contains("Slide1"));
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}

}
