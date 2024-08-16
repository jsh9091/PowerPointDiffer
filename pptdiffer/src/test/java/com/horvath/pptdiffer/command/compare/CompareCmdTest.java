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

package com.horvath.pptdiffer.command.compare;

import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.engine.AbstractTestHelper;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests operations of the CompareCmd class.
 * @author jhorvath
 */
public class CompareCmdTest extends AbstractTestHelper {
	
	@Test
	public void preform_validStateExactSameFiles_Success() {
		// these files are exactly the same
		File file1 = new File(EXACT_FILE_1);
		File file2 = new File(EXACT_FILE_2);
		
		PpdState state = PpdState.getInstance();
		state.setReport("");
		
		state.setFileA(file1);
		state.setFileB(file2);
		
		CompareCmd cmd = new CompareCmd();
		cmd.perform();
		
		Assert.assertTrue(cmd.isSuccess());
		Assert.assertEquals(CompareCmd.EXACT_SAME_FILE_MESSAGE, cmd.getMessage());
		Assert.assertEquals(cmd.getReport(), state.getReport());
		Assert.assertTrue(state.isComparisonDone());
	}

	@Test
	public void perform_fakeFileA_Failure() {
		File file1 = new File("fake.pptx");
		File file2 = new File(BASIC_FILE_A);
		
		PpdState state = PpdState.getInstance();
		state.setReport("");
		
		state.setFileA(file1);
		state.setFileB(file2);
		
		CompareCmd cmd = new CompareCmd();
		cmd.perform();
		
		Assert.assertFalse(cmd.isSuccess());
		System.out.println(state.getReport());
		Assert.assertFalse(state.isComparisonDone());
	}

	@Test
	public void perform_wrongFileBType_Failure() {
		File file1 = new File(BASIC_FILE_A);
		File file2 = new File(NOT_PPTX_B);
		
		PpdState state = PpdState.getInstance();
		state.setReport("");
		
		state.setFileA(file1);
		state.setFileB(file2);
		
		CompareCmd cmd = new CompareCmd();
		cmd.perform();
		
		Assert.assertFalse(cmd.isSuccess());
		Assert.assertFalse(state.isComparisonDone());
	}
}
