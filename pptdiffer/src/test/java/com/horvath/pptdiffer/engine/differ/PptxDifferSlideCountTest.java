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
 * Performs tests on the PptxDiffer to check the number of slides in the two
 * files.
 * 
 * @author jhorvath
 */
public class PptxDifferSlideCountTest extends AbstractTestHelper {

	@Test
	public void slideCountTest_sameNumberOfSlides_SameCountFound() throws PpdException {
		// load two files with the same number of slides
		File file1 = new File(SLIDE_COUNT1_3SLIDES);
		File file2 = new File(SLIDE_COUNT2_3SLIDES);

		// create the differ engine
		PptxDiffer differ = new PptxDiffer(file1, file2);

		// check the outputs
		Assert.assertFalse(differ.isExactSameFile());
		Assert.assertTrue(differ.isSameSlideCount());
	}

	@Test
	public void slideCountTest_differentNumberOfSlides_differentCountFound() throws PpdException {
		// load two files with different numbers of slides
		File file1 = new File(SLIDE_COUNT1_3SLIDES);
		File file2 = new File(SLIDE_COUNT3_4SLIDES);

		// create the differ engine
		PptxDiffer differ = new PptxDiffer(file1, file2);

		// check the outputs
		Assert.assertFalse(differ.isExactSameFile());
		Assert.assertFalse(differ.isSameSlideCount());
	}

}
