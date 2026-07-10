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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.horvath.pptdiffer.Differ;
import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Command for building report text.
 * @author jhorvath 
 */
public class GenerateReportTextCmd extends AbstractCompareCmd {

	// tool for collecting information for report
	private Differ differ;
	// utility for building report string
	private StringBuilder sb;
	// final return value 
	private String reportText;
	
	public static final String ERROR_NULL_DIFFER = "";
	
	public static final String EOL = System.lineSeparator();
	
	private static final String SECTION_SEPARATOR = "----------------------------------------------------------------";
	
	public static final String EXACT_CHECK_DESCRIPTION = "Exact file check: Checks if the two files are exactly the same file or not.";
	public static final String EXACT_CHECK_SAME = "The two files appear to be the same exact file.";
	public static final String EXACT_CHECK_DIFFERENT = "In reading the data in the two files, it was found that the two files are not the same file.";

	public static final String WHOLE_TEXT_EMPTY = "Neither file contains any text.";
	public static final String WHOLE_TEXT_SAME = "Both files seem to contain the exact same text.";
	public static final String WHOLE_TEXT_DIFFERENT = "There are differences in the text in the two files.";
	
	public static final String METADATA_SAME = "The metadata in File A and File B appear to be the same.";
	public static final String METADATA_DIFFERENT = "The metadata in File A and File B contain different information.";
	
	public static final String SLIDE_COUNT_DESCRIPTION = "Slide Count: Compares the number of slides in the two files.";
	public static final String SLIDE_COUNT_SAME = "Both files contain ";
	public static final String SLIDE_COUNT_DIFFERENT = "The slide counts are not the same.";

	public static final String SLIDE_COMP_ENDED = "Slide comparison checked ended due to File B having fewer slides than File A. ";

	public static final String IMAGE_COUNT_DESCRIPTION = "Images Count: Compares the number of images in the two files.";
	public static final String IMAGE_COUNT_SAME = "Both files contain ";
	public static final String IMAGE_COUNT_DIFFERENT = "The image counts are not the same.";
	
	public static final String IMAGE_INFO_TEXT = " image information:";
	
	public static final String SLIDE_NAME_DIFFERENT = "Slides for Files A and B are different at (zero-based) index: ";
	
	public static final String SLIDE_LAYOUTS_DIFFERENT = "Slides for Files A and B have different layouts: ";
	
	public static final String SLIDE_SHAPE_NAMES_FILE_A = "File A shape names: ";
	public static final String SLIDE_SHAPE_NAMES_FILE_B = "File B shape names: ";
	
	public static final String SLIDE_TEXT_SAME = "Slide text for Files A and B are the same at (zero-based) index: ";
	public static final String SLIDE_TEXT_DIFFERENT = "Slide text for Files A and B are different at (zero-based) index: ";
	
	public static final String SLIDE_TEXT_EXPECTED = " expected (File A) \"";
	public static final String SLIDE_TEXT_ACTUAL = "\" but actually found (File B) \"";
	public static final String SLIDE_TEXT_CLOSE = "\".";
	
	public static final String EXTRA_TEXT = "Extra Text: ";
	public static final String EXTRA_TEXT_FILE_A = "Extra Text found in File A.";
	public static final String EXTRA_TEXT_FILE_B = "Extra Text found in File B.";
	
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
		
		overviewLabel();
		
		// high level checks on file
		exactFileCheck();
		wholeTextComparisonCheck();
		metadataCheck();
		slideCountsCheck();
		imageCountCheck();
		imageInfoCheck();
		
		// individual slide checks
		slideComparisonCheck();

		this.reportText = sb.toString();

		success = true;
	}
	
	/**
	 * Writes overview label for report. 
	 */
	private void overviewLabel() {
		sb.append("PowerPoint File Comparison Report");
		sb.append(EOL);
		sb.append(getDateInfo());
		sb.append(EOL);
		sb.append(EOL);
		
		sb.append("Comparison: "); 
		sb.append(differ.getPpdFileA().getFileName()); 
		sb.append(" vs "); 
		sb.append(differ.getPpdFileB().getFileName());

		sb.append(EOL);
		sb.append(EOL);
		sb.append("File A: ");
		sb.append(differ.getRawFileA().getAbsolutePath());
		sb.append(EOL);
		sb.append("File B: ");
		sb.append(differ.getRawFileB().getAbsolutePath());
		sb.append(EOL);
		sb.append(SECTION_SEPARATOR);
		sb.append(EOL);
	}
	
	/**
	 * Gets and formats current date. 
	 * @return String
	 */
	private String getDateInfo() {
		StringBuilder sb = new StringBuilder();
		LocalDate today = LocalDate.now();
		
		String dayOfWeek = today.getDayOfWeek().toString().toLowerCase();
		dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);
		
		String month = today.getMonth().toString().toLowerCase();
		month = month.substring(0, 1).toUpperCase() + month.substring(1);
		
		sb.append(dayOfWeek);
		sb.append(", ");
		sb.append(month);
		sb.append(" ");
		sb.append(today.getDayOfMonth());
		sb.append(", ");
		sb.append(today.getYear());
		
		return sb.toString();
	}
	
	/**
	 * Writes slide label. 
	 * 
	 * @param index int (not zero based)
	 */
	private void slideLabel(int index) {
		sb.append("SLIDE: ");
		sb.append(index);
		sb.append(EOL);
		sb.append(SECTION_SEPARATOR);
		sb.append(EOL);
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
	 * Performs check comparing all of the text of File A, and all of the text from File B.
	 */
	private void wholeTextComparisonCheck() {
		
		final String slideTextA = differ.wholeFileText_FileA();
		final String slideTextB = differ.wholeFileText_FileB();
		
		if (slideTextA.isEmpty() && slideTextB.isEmpty()) {
			sb.append(WHOLE_TEXT_EMPTY);
			sb.append(EOL);
			sb.append(EOL);
			
		} else if (slideTextA.equals(slideTextB)) {
			sb.append(WHOLE_TEXT_SAME);
			sb.append(EOL);
			sb.append(EOL);
			
		} else {
			sb.append(WHOLE_TEXT_DIFFERENT);
			sb.append(EOL);
			sb.append(EOL);
		}
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
	 * Builds the report text for comparing image counts. 
	 */
	private void imageCountCheck() {
		
		sb.append(IMAGE_COUNT_DESCRIPTION);
		sb.append(EOL);
		
		// if both files have the same number of images
		if (differ.imageCount_fileA() == differ.imageCount_fileB()) {
			sb.append(IMAGE_COUNT_SAME); 
			sb.append(differ.imageCount_fileA()); 
			sb.append(differ.imageCount_fileB() == 1 ? " images." : " images."); 
			sb.append(EOL);
			sb.append(EOL);
			
		} else {
			sb.append(IMAGE_COUNT_DIFFERENT); 
			sb.append(EOL);
			sb.append("File "); 
			sb.append(differ.getPpdFileA().getFileName()); 
			sb.append(" contains ");
			sb.append(differ.imageCount_fileA()); 
			sb.append(differ.imageCount_fileA() == 1 ? " images." : " images."); 
			sb.append(EOL);
			sb.append("File "); 
			sb.append(differ.getPpdFileB().getFileName()); 
			sb.append(" contains "); 
			sb.append(differ.imageCount_fileB()); 
			sb.append(differ.imageCount_fileB() == 1 ? " images." : " images."); 
			sb.append(EOL);
			sb.append(EOL);
		}
	}
	
	/*
	 * Builds report data when image information does not match. 
	 */
	private void imageInfoCheck() {
		boolean reportUpdated = false;
		String[] infoFileA = differ.imageInfo_fileA();
		String[] infoFileB = differ.imageInfo_fileB();
		
		if (infoFileA.length == 0 && infoFileB.length == 0) {
			return;
			
		} else if (infoFileA.length > 0 && infoFileB.length > 0) { 
			for (int i = 0; i < infoFileA.length; i++) {
				if (infoFileB.length <= i) {
					break;
				}
				
				// if we have image information that does not match
				if (!infoFileA[i].equals(infoFileB[i])) {
					reportUpdated = true; 
					
					sb.append("File A ");
					sb.append(i + 1);
					sb.append(IMAGE_INFO_TEXT);
					sb.append(EOL);
					sb.append("\t");
					sb.append(infoFileA[i]);
					sb.append(EOL);
					
					sb.append("File B ");
					sb.append(i + 1);
					sb.append(IMAGE_INFO_TEXT);
					sb.append(EOL);
					sb.append("\t");
					sb.append(infoFileB[i]);
					sb.append(EOL);
				}
			}
		}
		if (reportUpdated) {
			sb.append(EOL);
			sb.append(EOL);
		}
	}
	
	/**
	 * Calls checks for individual comparisons. 
	 * 
	 * @throws PpdException
	 */
	private void slideComparisonCheck() throws PpdException {
		
		for (int i = 0; i < differ.slideCount_fileA(); i++) {
			if (rangeCheck(i, differ.getPpdFileB())) {
				slideLabel(i + 1);
				slideNameComparisonsCheck(i);
				slideLayoutComparisonsCheck(i);
				slideTextComparisonsCheck(i);
				slideShapeCountCheck(i);
				slideShapeNamesCheck(i);
				slideTableCountCheck(i);
				sb.append(EOL);
				
			} else {
				sb.append(SLIDE_COMP_ENDED);
				sb.append(EOL);
				sb.append("File A: ");
				sb.append(differ.getPpdFileA().getSlideList().size());
				sb.append(differ.getPpdFileA().getSlideList().size() == 1 ? " slide. " : " slides ");
				sb.append(" File B: ");
				sb.append(differ.getPpdFileB().getSlideList().size());
				sb.append(differ.getPpdFileB().getSlideList().size() == 1 ? " slide. " : " slides ");
				sb.append(EOL);
				return;				
			}
		}
	}
	
	/**
	 * Compares the names of slides in Files A & B for a given index.
	 * 
	 * @param index int 
	 * @throws PpdException
	 */
	private void slideNameComparisonsCheck(int index) throws PpdException { 
		
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
		}
	}
	
	/**
	 * Compares the layouts of slides in Files A & B for a given index.
	 * 
	 * @param index int 
	 */
	private void slideLayoutComparisonsCheck(int index) {
		
		final String slideLayoutA = differ.slideLayout_fileA(index);
		final String slideLayoutB = differ.slideLayout_fileB(index);
		
		// only add to report if slide names are different
		if (!slideLayoutA.equals(slideLayoutB)) {
			sb.append(SLIDE_LAYOUTS_DIFFERENT);
			sb.append(EOL);
			sb.append("File A: slide layout: ");
			sb.append(slideLayoutA);
			sb.append(EOL);
			sb.append("File B: slide layout: ");
			sb.append(slideLayoutB);
			sb.append(EOL);
		}
	}
	
	/**
	 * Performs high level comparison for slide text contents between files A and B.
	 * 
	 * @param index int 
	 * @throws PpdException
	 */
	private void slideTextComparisonsCheck(int index) throws PpdException {
		
		final String slideTextA = differ.slideText_fileA(index);
		final String slideTextB = differ.slideText_fileB(index);
		
		if (slideTextA.equals(slideTextB)) {
			sb.append(SLIDE_TEXT_SAME);
			sb.append(index);
			sb.append(EOL);
			
		} else {
			sb.append(SLIDE_TEXT_DIFFERENT);
			sb.append(index);
			sb.append(EOL);
			showExpectedAndActual(index);
		}
	}
	
	/**
	 * When slide text is found to have differences, show expected and actual words in report. 
	 * 
	 * @param index int
	 * @throws PpdException
	 */
	private void showExpectedAndActual(int index) throws PpdException {
		
		String[] slideA = differ.slideText_fileA(index).split(" ");
		String[] slideB = differ.slideText_fileB(index).split(" ");
		
		String expected = "";
		String actual = "";
		for (int i = 0; i < slideA.length; i++) {
			
			if (i >= slideB.length) {
				// have not found a change yet
				// and there is no more slide B text
				break;
			}
			
			String wordA = slideA[i];
			String wordB = slideB[i];
			// if the current iteration has different values
			if (!wordA.equals(wordB)) {
				expected = wordA;
				actual = wordB;
				break;
			}
		}
		
		// if we didn't find the difference 
		if (expected.isEmpty() && actual.isEmpty()) {
			// try searching for additional text
			searchForExtraText(slideA, slideB);
			sb.append(EOL);
			
		} else {
			// do reporting
			sb.append("On slide index ");
			sb.append(index);
			sb.append(SLIDE_TEXT_EXPECTED);
			sb.append(expected);
			sb.append(SLIDE_TEXT_ACTUAL);
			if (actual.isEmpty()) {
				sb.append("[EMPTY]");
			} else {
				sb.append(actual);
			}
			sb.append(SLIDE_TEXT_CLOSE);
			sb.append(EOL);
		}
	}
	
	/**
	 * Looks for circumstance where the text on both slides are the same, but one
	 * slide has additional text that was not found in the liner comparison search.
	 * 
	 * @param slideA String[]
	 * @param slideB String[]
	 */
	private void searchForExtraText(String[] slideA, String[] slideB) {
		StringBuilder extraWordsSb = new StringBuilder();
		// if there is a different number of words on the two slides
		if (slideA.length != slideB.length) {
			// more words on slide A
			if (slideA.length > slideB.length) {
				extraWordsSb.append(EXTRA_TEXT_FILE_A).append(EOL).append(EXTRA_TEXT);
				for (int i = slideB.length; i < slideA.length; i++) {
					extraWordsSb.append(slideA[i]).append(" ");
				}

			} else {
				// more words on slide B
				extraWordsSb.append(EXTRA_TEXT_FILE_B).append(EOL).append(EXTRA_TEXT);
				;
				for (int i = slideA.length; i < slideB.length; i++) {
					extraWordsSb.append(slideB[i]).append(" ");
				}
			}
			sb.append(extraWordsSb.toString()).append(EOL);
		}
	}
	
	/**
	 * Builds report text for comparing the number of shapes on a given slide index. 
	 * @param index int
	 */
	private void slideShapeCountCheck(int index) {
		final int fileAShapeCount = differ.getPpdFileA().getSlideList().get(index).getShapeCount();
		final int fileBShapeCount = differ.getPpdFileB().getSlideList().get(index).getShapeCount();
				
		// do reporting
		sb.append("On slide index ");
		sb.append(index);
		sb.append(" File A contains ");
		sb.append(fileAShapeCount);
		sb.append(fileAShapeCount == 1 ? " shape." : " shapes.");
		sb.append(" File B contains ");
		sb.append(fileBShapeCount);
		sb.append(fileBShapeCount == 1 ? " shape." : " shapes.");
		sb.append(EOL);
	}
	
	/**
	 * Builds report text for comparing the human readable names of shapes on a given slide index. 
	 * 
	 * @param index int 
	 */
	private void slideShapeNamesCheck(int index) {
		List<String> namesFileA = differ.shapeNames_fileA(index);
		List<String> namesFileB = differ.shapeNames_fileB(index);
		
		for (int i = 0; i < namesFileA.size(); i++) {
			if (i >= namesFileB.size()) {
				break;
			}
			String nameA = namesFileA.get(i);
			String nameB = namesFileB.get(i);
			
			// only update report if something does not match
			if (!nameA.equals(nameB)) {
				sb.append("On slide index ");
				sb.append(index);
				sb.append(":");
				sb.append(EOL);
				sb.append(SLIDE_SHAPE_NAMES_FILE_A);
				sb.append(listToString(namesFileA));
				sb.append(EOL);
				sb.append(SLIDE_SHAPE_NAMES_FILE_B);
				sb.append(listToString(namesFileB));
				sb.append(EOL);
				break;
			}
		}
	}
	
	/**
	 * Builds report text for comparing the number of tables on a given slide index. 
	 * 
	 * @param index int
	 */
	private void slideTableCountCheck(int index) {
		final int tableCountA = differ.tableCount_fileA(index);
		final int tableCountB = differ.tableCount_fileB(index);
		
		if (tableCountA > 0 || tableCountB > 0) {
			// do reporting
			sb.append("On slide index ");
			sb.append(index);
			sb.append(" File A contains ");
			sb.append(tableCountA);
			sb.append(tableCountA == 1 ? " table." : " tables.");
			sb.append(" File B contains ");
			sb.append(tableCountB);
			sb.append(tableCountB == 1 ? " table." : " tables.");
			sb.append(EOL);

		}
	}
	
	/**
	 * Formats a list of strings for easy display in a single string. 
	 * 
	 * @param list of strings
	 * @return String
	 */
	private String listToString(List<String> list) {
		return list.stream().collect(Collectors.joining(", ", "[", "]"));
	}

	public String getReportText() {
		return reportText;
	}

}
