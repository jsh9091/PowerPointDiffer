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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.pptdiffer.Differ;
import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.engine.AbstractTestHelper;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Tests operations of the WriteReportCmd class.
 * @author jhorvath
 */
public class WriteReportCmdTest extends AbstractTestHelper {
	
	private static final String REPORT_FILES_DIRECTORY = RESOURCES_DIRECTORY + File.separator + "reportFiles" + File.separator;
	private static final String SUFFIX = ".txt";
	private static final String CONTROL = "_control";
	
	@Test
	public void perform_nullFile_exception() {
		
		boolean exceptionCaught = false;
		
		try {
			WriteReportCmd cmd = new WriteReportCmd(null);
			cmd.perform();
			
		} catch (PpdException ex) {
			exceptionCaught = true;
			Assert.assertEquals(WriteReportCmd.ERROR_NULL_FILE, ex.getMessage());
		}
		
		Assert.assertTrue(exceptionCaught);
	}
	
	@Test
	public void perform_sameFiles_filesMatch() {
		
		PpdState state = PpdState.getInstance();
		// set files that are the same in the state
		state.setFileA(new File(EXACT_FILE_1));
		state.setFileB(new File(EXACT_FILE_2));
		
		try {
			Differ differ = new Differ(state.getFileA(), state.getFileB());
			state.setReport(differ.generateReport());
			
			final String reportFile = REPORT_FILES_DIRECTORY + "sameFiles";
			
			File actualFile = new File(reportFile + SUFFIX);
			File controlFile = new File(reportFile + CONTROL + SUFFIX);

			// run command we are here to test
			WriteReportCmd cmd = new WriteReportCmd(actualFile);
			cmd.perform();
			
			Assert.assertTrue(actualFile.exists());
			Assert.assertTrue(controlFile.exists());
			
			// perform actual check
			Assert.assertTrue(compareExactTextFiles(controlFile, actualFile));
			
			// cleanup 
			Assert.assertTrue(actualFile.delete());
			
		} catch (PpdException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test 
	public void perform_sameFilesDifferentMetaData_filesMatch() {
		
		PpdState state = PpdState.getInstance();
		// set files that are the same, except metadata, in the state
		state.setFileA(new File(EXACT_FILE_1));
		state.setFileB(new File(EXACT_FILE_4));
		
		try {
			Differ differ = new Differ(state.getFileA(), state.getFileB());
			state.setReport(differ.generateReport());
			
			final String reportFile = REPORT_FILES_DIRECTORY + "sameFilesDifferentMetaData";
			
			File actualFile = new File(reportFile + SUFFIX);
			File controlFile = new File(reportFile + CONTROL + SUFFIX);

			// run command we are here to test
			WriteReportCmd cmd = new WriteReportCmd(actualFile);
			cmd.perform();
			
			Assert.assertTrue(actualFile.exists());
			Assert.assertTrue(controlFile.exists());
			
			// perform actual check
			Assert.assertTrue(compareExactTextFiles(controlFile, actualFile));
			
			// cleanup 
			Assert.assertTrue(actualFile.delete());
			
		} catch (PpdException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void perform_differentSlideCounts_filesMatch() {
		
		PpdState state = PpdState.getInstance();
		// set files that have different slide counts in the state
		state.setFileA(new File(SLIDE_COUNT_1_3SLIDES));
		state.setFileB(new File(SLIDE_COUNT_3_4SLIDES));
		
		try {
			Differ differ = new Differ(state.getFileA(), state.getFileB());
			state.setReport(differ.generateReport());
			
			final String reportFile = REPORT_FILES_DIRECTORY + "differentSlideCounts";
			
			File actualFile = new File(reportFile + SUFFIX);
			File controlFile = new File(reportFile + CONTROL + SUFFIX);

			// run command we are here to test
			WriteReportCmd cmd = new WriteReportCmd(actualFile);
			cmd.perform();
			
			Assert.assertTrue(actualFile.exists());
			Assert.assertTrue(controlFile.exists());

			// perform actual check
			Assert.assertTrue(compareExactTextFiles(controlFile, actualFile));
			
			// cleanup 
			Assert.assertTrue(actualFile.delete());
			
		} catch (PpdException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	/**
	 * Compares two given text files. Returns true if and only if the contents of the files are exactly the same.
	 * @param fileA File
	 * @param fileB File
	 * @return boolean 
	 */
	private static boolean compareExactTextFiles(File fileA, File fileB) { 

		try {
			boolean result = true;
			List<String> fileAText = Files.readAllLines(Paths.get(fileA.getAbsolutePath()));
			List<String> fileBText = Files.readAllLines(Paths.get(fileA.getAbsolutePath()));
			
			if (fileAText.size() != fileBText.size()) {
				result = false;
			}
			
			if (result) {
				for (String s : fileAText){
		            if (!fileBText.contains(s)){
		                result = false;
		                break;
		            }
		        }
			}

		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
			Assert.fail();
			
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			Assert.fail();
		}

		// if we have gotten here, then the files are the same
		return true;
	}

}
