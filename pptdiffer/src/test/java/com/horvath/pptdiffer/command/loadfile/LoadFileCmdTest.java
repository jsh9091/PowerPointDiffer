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

package com.horvath.pptdiffer.command.loadfile;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.utility.FileMode;

/**
 * Tests operations of the LoadFileCmd class.
 * @author jhorvath
 */
public class LoadFileCmdTest extends AbstractTestHelper {
	
	@Test
	public void perform_LoadFileA_FileALoaded() {
		
		File file = new File(BASIC_FILE_A);
		Assert.assertTrue(file.exists());
		
		PpdState state = PpdState.getInstance();
		state.setFileA(null);
		state.setFileB(null);

		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		LoadFileCmd cmd = new LoadFileCmd(FileMode.File_A, file);
		cmd.perform();
		
		Assert.assertTrue(cmd.getMessage().isEmpty());
		
		// check that the state has been updated with  file A
		Assert.assertNotNull(state.getFileA());
		Assert.assertEquals(file.getAbsolutePath(), state.getFileA().getAbsolutePath());	
		
		// check that the state value for file B has not been updated
		Assert.assertNull(state.getFileB());
	}
	
	@Test
	public void perform_LoadFileB_FileBLoaded() {
		
		File file = new File(BASIC_FILE_B);
		Assert.assertTrue(file.exists());
		
		PpdState state = PpdState.getInstance();
		state.setFileA(null);
		state.setFileB(null);

		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		LoadFileCmd cmd = new LoadFileCmd(FileMode.File_B, file);
		cmd.perform();
		
		Assert.assertTrue(cmd.getMessage().isEmpty());
		
		// check that the state has been updated with  file B
		Assert.assertNotNull(state.getFileB());
		Assert.assertEquals(file.getAbsolutePath(), state.getFileB().getAbsolutePath());	
		
		// check that the state value for file A has not been updated
		Assert.assertNull(state.getFileA());
	}

	@Test
	public void perform_NullFile_NothingLoadedErrorMessage() {
		PpdState state = PpdState.getInstance();
		state.setFileA(null);
		state.setFileB(null);

		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		LoadFileCmd cmd = new LoadFileCmd(FileMode.File_A, null);
		cmd.perform();

		// nothing was loaded to state
		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		Assert.assertEquals(LoadFileCmd.ERROR_FILE_IS_NULL, cmd.getMessage());
	}
	
	@Test 
	public void perform_FileNotReal_NothingLoadedErrorMessage() {
		
		File file = new File("fakeFile.ppptx");
		Assert.assertTrue(!file.exists());
		
		PpdState state = PpdState.getInstance();
		state.setFileA(null);
		state.setFileB(null);

		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		LoadFileCmd cmd = new LoadFileCmd(FileMode.File_A, file);
		cmd.perform();

		// nothing was loaded to state
		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		Assert.assertEquals(LoadFileCmd.ERROR_FILE_NOT_FOUND, cmd.getMessage());
	}
	
	@Test
	public void perform_NullMode_NothingLoadedErrorMessage() {
		
		File file = new File(BASIC_FILE_A);
		Assert.assertTrue(file.exists());
		
		PpdState state = PpdState.getInstance();
		state.setFileA(null);
		state.setFileB(null);

		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		LoadFileCmd cmd = new LoadFileCmd(null, file);
		cmd.perform();

		// nothing was loaded to state
		Assert.assertNull(state.getFileA());
		Assert.assertNull(state.getFileB());
		
		Assert.assertEquals(LoadFileCmd.ERROR_MODE_MUST_NOT_BE_NULL, cmd.getMessage());
	}
	
	@Test 
	public void perform_UploadFileWithReportInState_reportCleared() {
		
		PpdState state = PpdState.getInstance();
		// set a report value in the application state
		state.setReport("This is some report text.");
		
		File file = new File(BASIC_FILE_A);
		
		new LoadFileCmd(FileMode.File_A, file).perform();
		
		// verify that the state has been cleared of stale data
		Assert.assertTrue(state.getReport().isEmpty());
	}
}
