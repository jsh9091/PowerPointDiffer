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

package com.horvath.pptdiffer.engine.differ;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Class for general tests with the differ tool. 
 * @author jhorvath
 */
public class PptxDifferTest extends AbstractTestHelper {
	
	@Test
	public void initialization_fakeFileA_exception() { 
		boolean caughtException = false;

		File fileA = new File("fake.pptx");
		File fileB = new File(BASIC_FILE_A);

		try {
			new PptxDiffer(fileA, fileB);
			Assert.fail();

		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(fileA.getName()));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void initialization_fakeFileB_exception() {
		boolean caughtException = false;

		File fileA = new File(BASIC_FILE_A);
		File fileB = new File("fake.pptx");

		try {
			new PptxDiffer(fileA, fileB);
			Assert.fail();

		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(fileB.getName()));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void initialization_fakeFileAandFakeFileB_exception() {
		boolean caughtException = false;

		File fileA = new File("fake1.pptx");
		File fileB = new File("fake2.pptx");

		try {
			new PptxDiffer(fileA, fileB);
			Assert.fail();

		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(fileA.getName()));
			Assert.assertTrue(ex.getMessage().contains(fileB.getName()));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void initialization_nonPpptxFileA_exception() {
		boolean caughtException = false;

		File fileA = new File(NOT_PPTX_A);
		File fileB = new File(BASIC_FILE_A);
		
		Assert.assertTrue(fileA.exists());

		try {
			new PptxDiffer(fileA, fileB);
			Assert.fail();

		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(fileA.getName()));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void initialization_nonPpptxFileB_exception() {
		boolean caughtException = false;

		File fileA = new File(BASIC_FILE_A);
		File fileB = new File(NOT_PPTX_B);

		Assert.assertTrue(fileB.exists());

		try {
			new PptxDiffer(fileA, fileB);
			Assert.fail();

		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(fileB.getName()));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void initialization_nonPpptxFileAandFileB_exception() {
		boolean caughtException = false;

		File fileA = new File(NOT_PPTX_A);
		File fileB = new File(NOT_PPTX_B);
		
		Assert.assertTrue(fileA.exists());
		Assert.assertTrue(fileB.exists());

		try {
			new PptxDiffer(fileA, fileB);
			Assert.fail();

		} catch (PpdException ex) {
			Assert.assertTrue(ex.getMessage().contains(fileA.getName()));
			Assert.assertTrue(ex.getMessage().contains(fileB.getName()));
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
	}
}
