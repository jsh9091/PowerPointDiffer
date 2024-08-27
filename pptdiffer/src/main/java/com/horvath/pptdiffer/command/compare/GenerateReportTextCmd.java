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
	
	public static final String ERROR_NULL_DIFFER = "";
	
	public static final String EOL = System.lineSeparator();
	
	public static final String EXACT_CHECK_DESCRIPTION = "Exact file check: Checks if the two files are exactly the same file or not.";
	public static final String EXACT_CHECK_SAME = "The two files appear to be the same exact file.";
	public static final String EXACT_CHECK_DIFFERENT = "In reading the data in the two files, it was found that the two files are not the same file.";
	
	public static final String SLIDE_COUNT_DESCRIPTION = "Slide Count: Compares the number of slides in the two files.";
	public static final String SLIDE_COUNT_SAME = "Both files contain ";
	public static final String SLIDE_COUNT_DIFFERENT = "The slide counts are not the same.";
	
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

		if (differ == null) {
			throw new PpdException(ERROR_NULL_DIFFER);
		}
		
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
		
		sb.append(EXACT_CHECK_DESCRIPTION);
		sb.append(EOL);
		sb.append("Result: ");
		if (differ.isSameFile()) {
			sb.append(EXACT_CHECK_SAME);
		} else {
			sb.append(EXACT_CHECK_DIFFERENT);
		}
		sb.append(EOL);
		sb.append(EOL);
	}
	
	/**
	 * Builds report text for comparing slide counts. 
	 */
	private void slideCountsCheck() {
		
		sb.append(SLIDE_COUNT_DESCRIPTION); 
		sb.append(EOL);
		
		// if both files have the same number of slides
		if (differ.fileA_SlideCount() == differ.fileB_SlideCount()) {
			sb.append(SLIDE_COUNT_SAME); 
			sb.append(differ.fileA_SlideCount()); 
			sb.append(differ.fileA_SlideCount() == 1 ? " slide." : " slides."); 
			sb.append(EOL);
			sb.append(EOL);
			
		} else {
			sb.append(SLIDE_COUNT_DIFFERENT); 
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
