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

package com.horvath.pptdiffer.command.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.command.io.LoadPptxCmd;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Extracts text from the entire file into a single string.
 * @author jhorvath 
 */
public final class ExtractWholeFilerTextCmd extends LoadPptxCmd {

	private File fileA;
	private File fileB;
	
	private String fileA_Text = "";
	private String fileB_Text = "";
	
	private String fileA_metadata = "";
	private String fileB_metadata = "";
	
	/**
	 * Constructor. 
	 * @param fileA File 
	 * @param fileB File 
	 */
	public ExtractWholeFilerTextCmd(File fileA, File fileB) {
		super(fileA, fileB);
		this.fileA = fileA;
		this.fileB = fileB;
	}
	
	@Override
	public void perform() throws PpdException {
		Debugger.printLog("Extract text from the entire file", this.getClass().getName());
		
		success = false;
		
		// do null and exists checks from super class
		super.perform();
		
		fileA_Text = extractText(fileA);
		fileB_Text = extractText(fileB);
		
		fileA_metadata = extractMetaData(fileA);
		fileB_metadata = extractMetaData(fileB);
		
		success = true;
	}

	/**
	 * Extracts text from the entire file into a single string. 
	 * @param inputFile File 
	 * @return String 
	 * @throws PpdException
	 */
	private String extractText(File inputFile) throws PpdException {
		String allText = "";

		try {
			SlideShow<XSLFShape, XSLFTextParagraph> slideshow = 
					new XMLSlideShow(new FileInputStream(inputFile));

			SlideShowExtractor<XSLFShape, XSLFTextParagraph> slideShowExtractor = 
					new SlideShowExtractor<XSLFShape, XSLFTextParagraph>(slideshow);

			slideShowExtractor.setMasterByDefault(false);
			slideShowExtractor.setNotesByDefault(true);

			allText = slideShowExtractor.getText();

			slideShowExtractor.close();
			slideshow.close();

		} catch (IOException ex) {
			throw new PpdException(ex.getLocalizedMessage());
		}

		return allText;
	}
	
	/**
	 * Extracts metadata from file. 
	 * @param inputFile File 
	 * @return String
	 * @throws PpdException
	 */
	private String extractMetaData(File inputFile) throws PpdException {
		String metadata = "";
		
		try {
			SlideShow<XSLFShape, XSLFTextParagraph> slideshow = 
					new XMLSlideShow(new FileInputStream(inputFile));

			SlideShowExtractor<XSLFShape, XSLFTextParagraph> slideShowExtractor = 
					new SlideShowExtractor<XSLFShape, XSLFTextParagraph>(slideshow);
			
			POITextExtractor textExtractor = slideShowExtractor.getMetadataTextExtractor();
			metadata = textExtractor.getText();			

			slideShowExtractor.close();
			slideshow.close();

		} catch (IOException ex) {
			throw new PpdException(ex.getLocalizedMessage());
		}
		
		return metadata;
	}

	public String getFileA_Text() {
		return fileA_Text;
	}

	public String getFileB_Text() {
		return fileB_Text;
	}

	public String getFileA_metadata() {
		return fileA_metadata;
	}

	public String getFileB_metadata() {
		return fileB_metadata;
	}

}
