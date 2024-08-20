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

package com.horvath.pptdiffer.engine.differ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.apache.poi.xslf.usermodel.XMLSlideShow;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Tool for comparing two PowerPoint (.PPTX) files.
 * Comparison operations are automatically run the 
 * PptxDiffer is initialized. Call methods on the PptxDiffer
 * object reference to gather and process results of the
 * comparison operations. 
 * @author jhorvath
 */
public class PptxDiffer {
	
	// PowerPoint files under comparison
	private File fileA;
	private File fileB;
	private XMLSlideShow pptA;
	private XMLSlideShow pptB;
	
	// result fields
	private StringBuilder report;
	private boolean exactSameFile;
	private boolean sameSlideCount;
	
	private static final String EOL =  System.lineSeparator();

	/**
	 * Constructor. 
	 * @param fileA File 
	 * @param fileB File 
	 * @throws PpdEngineException
	 */
	public PptxDiffer(File fileA, File fileB) throws PpdException {
		this.fileA = fileA;
		this.fileB = fileB;
		this.report = new StringBuilder();
		
		// TODO: do not run these operations on instantiation of object
		checkFilesExist();
		checkFilesArePPTX();
		performDiff();
	}
	
	/**
	 * Checks that the provided files can actually be found. 
	 * If one or both files cannot be found, an exception is 
	 * thrown reporting the problem.
	 * @throws PpdEngineException
	 */
	private void checkFilesExist() throws PpdException {
		/* Note: this check is needed for library functionality.  */ 

		String message = null;
		
		if (!fileA.exists()) {
			message = "ERROR: File A: " + fileA.getAbsolutePath() + " was not found.";
		}
		
		if (!fileB.exists()) {
			final String fileBMessage = "ERROR: File B: " + fileB.getAbsolutePath() + " was not found.";
			if (message != null) {
				message += EOL + fileBMessage;
			} else {
				message = fileBMessage;
			}
		}
		
		// if a problem was found, report it
		if (message != null) {
			throw new PpdException(message);
		}
	}
	
	/**
	 * Checks that the provided files are PowerPoint (.PPTX) files. 
	 * If one or both files is not .PPTX, an exception is 
	 * thrown reporting the problem.
	 * @throws PpdEngineException 
	 */
	private void checkFilesArePPTX() throws PpdException {
		/* Note: this check is needed for library functionality.  */ 

		String message = null;

		if (!fileA.getName().toLowerCase().endsWith(".pptx")) {
			message = "ERROR: File A: " + fileA.getAbsolutePath() + " is not a PowerPoint (.pptx) file.";
		}
		
		if (!fileB.getName().toLowerCase().endsWith(".pptx")) {
			final String fileBMessage = "ERROR: File B: " + fileB.getAbsolutePath() + " is not a PowerPoint (.pptx) file.";
			if (message != null) {
				message += EOL + fileBMessage;
			} else {
				message = fileBMessage;
			}
		}
		
		// if a problem was found, report it
		if (message != null) {
			throw new PpdException(message);
		}
	}
	
	/**
	 * High level method that controls operations 
	 * of running comparisons of the two files. 
	 * @throws PpdEngineException
	 */
	private void performDiff() throws PpdException { 
		Debugger.printLog("performDiff() starting...", this.getClass().getName());

		if (!exactSameFileCheck()) {
			// the files are not the same, so keep checking
			loadPptxFiles();
			compareSlideCounts();
		}
		
		Debugger.printLog("performDiff() completed.", this.getClass().getName());
	}
	
	/**
	 * Performs comparison checks to determine if the 
	 * two files are the same exact file or not.
	 * @throws PpdEngineException
	 */
	private boolean exactSameFileCheck() throws PpdException {
		Debugger.printLog("exactSameFileCheck()", this.getClass().getName());

		report.append("Exact file check: Checks if the two files are exactly the same file or not.");
		report.append(EOL);
		report.append(EOL);

		// perform a quick byte length check
		if (fileA.length() != fileB.length()) {
			// we have found that the two files cannot be the same
			exactSameFile = false;

			// build report update
			report.append("The byte length of the two files is not the same. ");
			report.append(fileA.getName());
			report.append(" has a byte length of ");
			report.append(fileA.length());
			report.append(" and ");
			report.append(fileB.getName());
			report.append(" has a byte length of ");
			report.append(fileB.length());
			report.append('.');
			report.append(EOL);
			report.append(EOL);
			return exactSameFile;
		}

		// read in the two files 
		try (InputStream fileA_stream = java.nio.file.Files.newInputStream(fileA.toPath());
			 InputStream fileB_stream = java.nio.file.Files.newInputStream(fileB.toPath());) {

			int fileA_Value;
			int fileB_Value;

			do {
				// read a byte of data from both files
				fileA_Value = fileA_stream.read();
				fileB_Value = fileB_stream.read();
				
				// as we read the data, check if values are the same
				if (fileA_Value != fileB_Value) {
					// we have found contents that are not the same
					exactSameFile = false;
					
					// build report update
					report.append("In reading the data in the two files, it was found that the two files are not the same file.");
					report.append(EOL);
					report.append(EOL);
					return exactSameFile;
				}
			} while (fileA_Value >= 0);

		} catch (FileNotFoundException ex) {
			exactSameFile = false;
			final String message = "ERROR: There appears to have been a problem reading one or both of the files.";
			throw new PpdException(message, ex);
			
		} catch (IOException ex) {
			exactSameFile = false;
			final String message = "ERROR: There appears to have been an error reading one or both of the of the files.";
			throw new PpdException(message, ex);
		}
		report.append("The two files appear to be the same exact file.");
		report.append(EOL);
		report.append(EOL);
		exactSameFile = true;
		return exactSameFile;
	}
	
	/**
	 * Loads the pptx files into memory as XMLSlideShow objects.
	 * @throws PpdEngineException
	 */
	private void loadPptxFiles() throws PpdException {
		Debugger.printLog("loadPptxFiles()", this.getClass().getName());

		try (
				FileInputStream fisA = new FileInputStream(this.fileA);
				FileInputStream fisB = new FileInputStream(this.fileB)) {

			this.pptA = new XMLSlideShow(fisA);
			this.pptB = new XMLSlideShow(fisB);
			
		} catch (FileNotFoundException ex) {
			Debugger.printLog(ex.getMessage(), this.getClass().getName(), Level.SEVERE);
			throw new PpdException(ex.getMessage(), ex);
		} catch (IOException ex) {
			Debugger.printLog(ex.getMessage(), this.getClass().getName(), Level.SEVERE);
			throw new PpdException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Compares the number of slides in the two files.
	 */
	private void compareSlideCounts() {
		Debugger.printLog("compareSlideCounts()", this.getClass().getName());
		
		report.append("Slide Count: Compares the number of slides in the two files."); 
		report.append(EOL);
		report.append(EOL);
		
		sameSlideCount = this.pptA.getSlides().size() == this.pptB.getSlides().size();
		
		if (sameSlideCount) {
			report.append("Both files contain "); 
			report.append(pptA.getSlides().size()); 
			report.append(pptA.getSlides().size() == 1 ? " slide." : " slides."); 
			report.append(EOL);
			report.append(EOL);
			
		} else {
			report.append("The slide counts are not the same."); 
			report.append(EOL);
			report.append("File "); 
			report.append(fileA.getName()); 
			report.append(" contains "); 
			report.append(pptA.getSlides().size()); 
			report.append(" slides."); 
			report.append(EOL);
			report.append("File "); 
			report.append(fileB.getName()); 
			report.append(" contains "); 
			report.append(pptB.getSlides().size()); 
			report.append(" slides."); 
			report.append(EOL);
			report.append(EOL);
		}
	}
	
	/**
	 * Returns true if and only if there are no differences 
	 * between the two selected files, including document meta data.
	 * @return boolean 
	 */
	public boolean isExactSameFile() {
		return exactSameFile;
	}
	
	/**
	 * Returns true if the two files have the same number of slides.
	 * @return boolean
	 */
	protected boolean isSameSlideCount() {
		return sameSlideCount;
	}

	/**
	 * Returns an informational report of operations performs in 
	 * comparing the selected PowerPoint files, and the results 
	 * of those comparisons. 
	 * @return String 
	 */
	public String getReport() {
		return report.toString();
	}
	
}
