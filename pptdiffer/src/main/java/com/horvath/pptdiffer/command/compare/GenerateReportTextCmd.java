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
	
	public static final String METADATA_SAME = "The metadata in File A and File B appear to be the same.";
	public static final String METADATA_DIFFERENT = "The metadata in File A and File contain different information.";
	
	public static final String SLIDE_COUNT_DESCRIPTION = "Slide Count: Compares the number of slides in the two files.";
	public static final String SLIDE_COUNT_SAME = "Both files contain ";
	public static final String SLIDE_COUNT_DIFFERENT = "The slide counts are not the same.";
	
	public static final String SLIDE_NAME_DIFFERENT = "Slides for Files A and B are different at (zero-based) index: ";
	
	public static final String SLIDE_TEXT_SAME = "Slide text for Files A and B are the same at (zero-based) index: ";
	public static final String SLIDE_TEXT_DIFFERENT = "Slide text for Files A and B are different at (zero-based) index: ";
	
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
		metadataCheck();
		slideComparionCheck();

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
		if (differ.slideCount_fileA() == differ.slideCount_fileB()) {
			sb.append(SLIDE_COUNT_SAME); 
			sb.append(differ.slideCount_fileA()); 
			sb.append(differ.slideCount_fileA() == 1 ? " slide." : " slides."); 
			sb.append(EOL);
			sb.append(EOL);
			
		} else {
			sb.append(SLIDE_COUNT_DIFFERENT); 
			sb.append(EOL);
			sb.append("File "); 
			sb.append(differ.getPpdFileA().getFileName()); 
			sb.append(" contains ");
			sb.append(differ.slideCount_fileA()); 
			sb.append(differ.slideCount_fileA() == 1 ? " slide." : " slides."); 
			sb.append(EOL);
			sb.append("File "); 
			sb.append(differ.getPpdFileB().getFileName()); 
			sb.append(" contains "); 
			sb.append(differ.slideCount_fileB()); 
			sb.append(differ.slideCount_fileB() == 1 ? " slide." : " slides."); 
			sb.append(EOL);
			sb.append(EOL);
		}
	}
	
	/**
	 * Performs high level check on metadata and includes basic finding in report. 
	 */
	private void metadataCheck() {
		if (differ.metadata_FileA().equals(differ.metadata_FileB())) {
			sb.append(METADATA_SAME).append(EOL).append(EOL);
			
		} else {
			sb.append(METADATA_DIFFERENT).append(EOL).append(EOL);
		}
	}
	
	/**
	 * Calls checks for individual comparisons. 
	 * 
	 * @throws PpdException
	 */
	private void slideComparionCheck() throws PpdException {
		
		for (int i = 0; i < differ.slideCount_fileA(); i++) {
			slideNameComparionsCheck(i);
			slideTextComparionsCheck(i);
		}
	}
	
	/**
	 * Compares the names of slides in Files A & B for a given index.
	 * 
	 * @param index int 
	 * @throws PpdException
	 */
	private void slideNameComparionsCheck(int index) throws PpdException {
		
		final String slideNameA = differ.slideName_fileA(index).trim();
		final String slideNameB = differ.slideName_fileB(index).trim();
		
		// only add to report if slide names are different
		if (!slideNameA.equals(slideNameB)) {
			sb.append(SLIDE_NAME_DIFFERENT);
			sb.append(index);
			sb.append(EOL);
			sb.append("File A: slide name: ");
			sb.append(slideNameA);
			sb.append(EOL);
			sb.append("File B: slide name: ");
			sb.append(slideNameB);
			sb.append(EOL);
			sb.append(EOL);
		}
	}
	
	/**
	 * Performs high level comparison for slide text contents between files A and B.
	 * 
	 * @param index int 
	 * @throws PpdException
	 */
	private void slideTextComparionsCheck(int index) throws PpdException {
		
		final String slideTextA = differ.slideText_fileA(index);
		final String slideTextB = differ.slideText_fileB(index);
		
		if (slideTextA.equals(slideTextB)) {
			sb.append(SLIDE_TEXT_SAME);
			sb.append(index);
			sb.append(EOL);
			sb.append(EOL);
			
		} else {
			sb.append(SLIDE_TEXT_DIFFERENT);
			sb.append(index);
			sb.append(EOL);
			sb.append(EOL);
		}
	}

	public String getReportText() {
		return reportText;
	}

}
