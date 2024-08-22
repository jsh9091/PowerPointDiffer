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

package com.horvath.pptdiffer;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

public class DifferTest extends AbstractTestHelper {
	
	@Test
	public void constructor_goodFiles_modelsCreatedWithFileNames() {
		// TODO uncomment test and update getters in assert statements when updated versions available
//		File fileA = new File(BASIC_FILE_A);
//		File fileB = new File(BASIC_FILE_B);
//		
//		try {
//			Differ diff = new Differ(fileA, fileB);
//			
//			Assert.assertEquals(fileA.getName(), diff.getFileA().getFileName());
//			Assert.assertEquals(fileB.getName(), diff.getFileB().getFileName());
//		} catch (PpdException ex) {
//			Assert.fail();
//		}
	}

	// TODO create tests for null, not exists, and non-.pptx file cases
}
