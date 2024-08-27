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

package com.horvath.pptdiffer.command.compare;

import com.horvath.pptdiffer.Differ;
import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Command for building report text.
 * @author jhorvath 
 */
public class GenerateReportTextCmd extends PpdCommand {

	private Differ differ;
	private StringBuilder sb;
	private String reportText;
	
	private static final String EOL = System.lineSeparator();
	
	/**
	 * Constructor. 
	 * @param differ Differ
	 */
	public GenerateReportTextCmd(Differ differ) {
		this.differ = differ;
	}
	
	@Override
	public void perform() throws PpdException {
		
		Debugger.printLog("Generate diffing report text", this.getClass().getName());
		
		success = false;
		
		sb = new StringBuilder();
		
		exactFileCheck();
		slideCountsCheck();

		this.reportText = sb.toString();
		
		success = true;
	}
	
	/**
	 * Builds report text for exact files check. 
	 */
	private void exactFileCheck() {
		
		sb.append("Exact file check: Checks if the two files are exactly the same file or not.");
		sb.append(EOL);
		sb.append("Result: ");
		if (differ.isSameFile()) {
			sb.append("The two files appear to be the same exact file.");
		} else {
			sb.append("In reading the data in the two files, it was found that the two files are not the same file.");
		}
		sb.append(EOL);
		sb.append(EOL);
	}
	
	/**
	 * Builds report text for comparing slide counts. 
	 */
	private void slideCountsCheck() {
		Debugger.printLog("compareSlideCounts()", this.getClass().getName());
		
		sb.append("Slide Count: Compares the number of slides in the two files."); 
		sb.append(EOL);
		sb.append(EOL);
		
		boolean sameSlideCount = differ.fileA_SlideCount() == differ.fileB_SlideCount();
		
		if (sameSlideCount) {
			sb.append("Both files contain "); 
			sb.append(differ.fileA_SlideCount()); 
			sb.append(differ.fileA_SlideCount() == 1 ? " slide." : " slides."); 
			sb.append(EOL);
			sb.append(EOL);
			
		} else {
			sb.append("The slide counts are not the same."); 
			sb.append(EOL);
			sb.append("File "); 
			sb.append(differ.getPpdFileA().getFileName()); 
			sb.append(" contains "); 
			sb.append(differ.fileA_SlideCount()); 
			sb.append(differ.fileA_SlideCount() == 1 ? " slide." : " slides."); 
			sb.append(EOL);
			sb.append("File "); 
			sb.append(differ.getPpdFileB().getFileName()); 
			sb.append(" contains "); 
			sb.append(differ.fileB_SlideCount()); 
			sb.append(differ.fileB_SlideCount() == 1 ? " slide." : " slides."); 
			sb.append(EOL);
			sb.append(EOL);
		}
	}

	public String getReportText() {
		return reportText;
	}

}
