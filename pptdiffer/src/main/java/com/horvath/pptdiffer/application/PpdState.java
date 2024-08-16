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

package com.horvath.pptdiffer.application;

import java.io.File;

/**
 * The application state.
 * Contains the working set of data for the application. 
 * @author jhorvath
 */
public final class PpdState {

	private static PpdState instance = null;
	private File fileA = null;
	private File fileB = null;
	private String report = "";
	
	private PpdState() {
		Debugger.printLog("Initializing state", this.getClass().getName());
	}
	
	/**
	 * Returns an instance of the state. 
	 * @return PpdState
	 */
	public static PpdState getInstance() {
		if (instance == null) {
			instance = new PpdState();
		}
		return instance;
	}
	
	/**
	 * Determines if the application is ready to compare the PowerPoint files or not.
	 * @return boolean 
	 */
	public boolean isReadForDiff() {
		 return fileA != null && fileB != null;
	}
	
	/**
	 * Returns true if a comparison operation has been run, false otherwise.
	 * @return boolean
	 */
	public boolean isComparisonDone() {
		// if a comparison has been completed, report will be populated
		return !report.isEmpty();
	}

	public File getFileA() {
		return fileA;
	}

	public void setFileA(File fileA) {
		this.fileA = fileA;
	}

	public File getFileB() {
		return fileB;
	}

	public void setFileB(File fileB) {
		this.fileB = fileB;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	@Override
	public String toString() {
		return "PpdState [fileA=" + fileA + ", fileB=" + fileB + ", report=" + report + "]";
	}
	
}
