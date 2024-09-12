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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

import org.apache.poi.xslf.usermodel.XMLSlideShow;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Command for reading in PPTX files.
 * @author jhorvath
 */
public final class LoadPptxCmd extends AbstractFileLoader {
	
	// standard file objects to read in
	private File rawFileA;
	private File rawFileB;
	
	// POI slide-show files for internal processing
	private XMLSlideShow poiFileA;
	private XMLSlideShow poiFileB;
	
	private boolean exactlySameFile;
	
	/**
	 * Constructor. 
	 * @param fileA File 
	 * @param fileB File 
	 */
	public LoadPptxCmd(File fileA, File fileB) {
		this.rawFileA = fileA;
		this.rawFileB = fileB;
	}

	@Override
	public void perform() throws PpdException {
		
		Debugger.printLog("Load and parse PPTX files into POI objects", this.getClass().getName());
		
		success = false;

		nullCheck(rawFileA, rawFileB);
		filesExistsCheck(rawFileA, rawFileB);
		filesArePptxCheck(rawFileA, rawFileB);
		exactSameFileCheck();
		
		loadPptxFiles();
		
		success = true;
	}
	
	/**
	 * Reads in the file data from disk, and creates POI slide-show files 
	 * for processing.
	 * 
	 * @throws PpdException
	 */
	private void loadPptxFiles() throws PpdException {
		Debugger.printLog("loadPptxFiles()", this.getClass().getName());

		try (FileInputStream fisA = new FileInputStream(this.rawFileA);
				FileInputStream fisB = new FileInputStream(this.rawFileB)) {

			this.poiFileA = new XMLSlideShow(fisA);
			this.poiFileB = new XMLSlideShow(fisB);

		} catch (IOException ex) {
			Debugger.printLog(ex.getMessage(), this.getClass().getName(), Level.SEVERE);
			throw new PpdException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Performs comparison checks to determine if the two files are the same exact
	 * file or not.
	 * 
	 * @throws PpdException
	 */
	private void exactSameFileCheck() throws PpdException {
		this.exactlySameFile = true;

		try (InputStream fileA_stream = Files.newInputStream(this.rawFileA.toPath());
			 InputStream fileB_stream = Files.newInputStream(this.rawFileB.toPath());) {

			int fileA_Value;
			int fileB_Value;

			do {
				// read a byte of data from both files
				fileA_Value = fileA_stream.read();
				fileB_Value = fileB_stream.read();

				// as we read the data, check if values are the same
				if (fileA_Value != fileB_Value) {
					// we have found contents that are not the same
					this.exactlySameFile = false;
					// no need to continue
					break;
				}
			} while (fileA_Value >= 0);

		} catch (IOException ex) {
			Debugger.printLog(ex.getMessage(), this.getClass().getName(), Level.SEVERE);
			throw new PpdException(ex.getMessage(), ex);
		}
	}

	public XMLSlideShow getPoiFileA() {
		return poiFileA;
	}

	public XMLSlideShow getPoiFileB() {
		return poiFileB;
	}

	public boolean isExactlySameFile() {
		return exactlySameFile;
	}

}
