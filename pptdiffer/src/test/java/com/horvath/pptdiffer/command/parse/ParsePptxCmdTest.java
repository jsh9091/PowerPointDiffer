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

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.command.io.LoadPptxCmd;
import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 *  Tests operations of ParsePptxCmd.
 *  @author jhorvath 
 */
public class ParsePptxCmdTest extends AbstractTestHelper {

	@Test
	public void perform_fileANull_exception() {
		boolean caughtException = false;
		try {
			// file A is null
			ParsePptxCmd cmd = new ParsePptxCmd(null, new XMLSlideShow());
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParsePptxCmd.ERROR_NULL_OBJECT));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_fileBNull_exception() {
		boolean caughtException = false;
		try {
			// file B is null
			ParsePptxCmd cmd = new ParsePptxCmd(new XMLSlideShow(), null);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParsePptxCmd.ERROR_NULL_OBJECT));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_bothFilesNull_exception() {
		boolean caughtException = false;
		try {
			// both files are null
			ParsePptxCmd cmd = new ParsePptxCmd(null, null);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParsePptxCmd.ERROR_NULL_OBJECT));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_goodFiles_parsed() {
		
		// get POI XML objects from actual PPTX files
		XMLSlideShow[] array = loadPptxFilesHelper(BASIC_FILE_A, BASIC_FILE_B);
		XMLSlideShow xmlFileA = array[0];
		XMLSlideShow xmlFileB = array[1];
		
		try {
			ParsePptxCmd cmd = new ParsePptxCmd(xmlFileA, xmlFileB);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());
			
			Assert.assertNotNull(cmd.getPpdFileA());
			Assert.assertNotNull(cmd.getPpdFileB());

			Assert.assertEquals(xmlFileA.getSlides().size(), cmd.getPpdFileA().getSlideList().size());
			Assert.assertEquals(xmlFileB.getSlides().size(), cmd.getPpdFileB().getSlideList().size());
			
		} catch (PpdException ex) {
			Assert.fail();
		}
	}
	
	/**
	 * Helper method to read in .pptx files and return POI xml slide-show objects in an array.
	 * 
	 * @param path String
	 * @return XMLSlideShow[]
	 */
	private XMLSlideShow[] loadPptxFilesHelper(final String pathA, final String pathB) {
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
	
}
