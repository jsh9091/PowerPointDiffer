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

package com.horvath.pptdiffer.command.io;

import java.io.File;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Tests operations of LoadPptxCmdTest. 
 * @author jhorvath
 */
public class LoadPptxCmdTest extends AbstractTestHelper {

	@Test
	public void perform_fileANull_exception() {
		boolean caughtException = false;
		try {
			// file A is null
			LoadPptxCmd cmd = new LoadPptxCmd(null, new File(""));
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NULL));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_fileBNull_exception() {
		boolean caughtException = false;
		try {
			// file B is null
			LoadPptxCmd cmd = new LoadPptxCmd(new File(""), null);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NULL));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_bothFilesNull_exception() {
		boolean caughtException = false;
		try {
			// both files are null
			LoadPptxCmd cmd = new LoadPptxCmd(null, null);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NULL));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileANotExist_exception() {
		boolean caughtException = false;
		final String fakeName = "fake.pptx";
		try {
			// file A does not exist
			LoadPptxCmd cmd = new LoadPptxCmd(new File(fakeName), new File(BASIC_FILE_B));
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NOT_EXIST));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileBNotExist_exception() {
		boolean caughtException = false;
		final String fakeName = "fake.pptx";
		try {
			// file B does not exist
			LoadPptxCmd cmd = new LoadPptxCmd(new File(BASIC_FILE_A), new File(fakeName));
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NOT_EXIST));
			Assert.assertTrue(ex.getMessage().contains(fakeName));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_bothFilesNotExist_exception() {
		boolean caughtException = false;
		final String fakeNameA = "fakeA.pptx";
		final String fakeNameB = "fakeB.pptx";
		try {
			// neither file exists
			LoadPptxCmd cmd = new LoadPptxCmd(new File(fakeNameA), new File(fakeNameB));
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NOT_EXIST));
			Assert.assertTrue(ex.getMessage().contains(fakeNameA));
			Assert.assertTrue(ex.getMessage().contains(fakeNameB));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileANotPptx_exception() {
		boolean caughtException = false;
		File fileA = new File(NOT_PPTX_A);
		try {
			// file A not a PPTX
			LoadPptxCmd cmd = new LoadPptxCmd(fileA, new File(BASIC_FILE_B));
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NOT_PPTX));
			Assert.assertTrue(ex.getMessage().contains(fileA.getName()));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileBNotPptx_exception() {
		boolean caughtException = false;
		File fileb = new File(NOT_PPTX_B);
		try {
			// file B not a PPTX
			LoadPptxCmd cmd = new LoadPptxCmd(new File(BASIC_FILE_A), fileb);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NOT_PPTX));
			Assert.assertTrue(ex.getMessage().contains(fileb.getName()));
		}
		Assert.assertTrue(caughtException);
	}
	
	
	@Test
	public void perform_bothFilesNotPptx_exception() {
		boolean caughtException = false;
		File fileA = new File(NOT_PPTX_A);
		File fileB = new File(NOT_PPTX_B);
		try {
			// neither file is a PPTX
			LoadPptxCmd cmd = new LoadPptxCmd(fileA, fileB);
			cmd.perform();
			
		} catch (PpdException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(LoadPptxCmd.ERROR_FILE_NOT_PPTX));
			Assert.assertTrue(ex.getMessage().contains(fileA.getName()));
			Assert.assertTrue(ex.getMessage().contains(fileB.getName()));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_goodFiles_returnTypesValid() {
		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(BASIC_FILE_B);

		try {
			LoadPptxCmd cmd = new LoadPptxCmd(fileA, fileB);
			cmd.perform();

			Assert.assertTrue(cmd.isSuccess());

			Assert.assertNotNull(cmd.getPoiFileA());
			Assert.assertNotNull(cmd.getPoiFileB());

			Assert.assertTrue(cmd.getPoiFileA() instanceof XMLSlideShow);
			Assert.assertTrue(cmd.getPoiFileB() instanceof XMLSlideShow);

		} catch (PpdException ex) {
			Assert.fail();
		}
	}

}
