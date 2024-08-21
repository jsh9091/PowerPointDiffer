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

package com.horvath.pptdiffer.engine.differ;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Unit tests for exact file tests of the differ tool.
 * @author jhorvath
 * @deprecated
 */
public class PptxDifferExactTest extends AbstractTestHelper {

	@Test
	public void exactTest_bothSameFile_Same() throws PpdException {
		// these files are exactly the same
		File file1 = new File(EXACT_FILE_1);
		File file2 = new File(EXACT_FILE_2);

		PptxDiffer differ = new PptxDiffer(file1, file2);

		Assert.assertNotNull(differ.getReport());
		Assert.assertTrue(differ.getReport().length() > 0);
		
		Assert.assertTrue(differ.isExactSameFile());
	}

	@Test
	public void exactTest_sameFileDifferentNames_Same() throws PpdException {
		// these files are exactly the same, but have different file names
		File file1 = new File(EXACT_FILE_1);
		File file2 = new File(EXACT_FILE_3);

		PptxDiffer differ = new PptxDiffer(file1, file2);

		Assert.assertTrue(differ.isExactSameFile());
	}

	@Test
	public void exactTest_sameContentsDifferentMetadata_Different() throws PpdException {
		// these files are exactly the same, content,
		// but have different metadata
		File file1 = new File(EXACT_FILE_1);
		File file2 = new File(EXACT_FILE_4);

		PptxDiffer differ = new PptxDiffer(file1, file2);

		Assert.assertFalse(differ.isExactSameFile());
	}

	@Test
	public void exactTest_DifferentContent_Different() throws PpdException {
		// these files are mostly the same, but does have some content differences
		File file1 = new File(EXACT_FILE_1);
		File file2 = new File(EXACT_FILE_5);

		PptxDiffer differ = new PptxDiffer(file1, file2);

		Assert.assertFalse(differ.isExactSameFile());
	}

}
