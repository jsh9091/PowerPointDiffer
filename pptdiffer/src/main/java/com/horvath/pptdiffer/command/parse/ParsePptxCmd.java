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

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.engine.model.PptxSlide;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Parses POI .pptx objects into PPD .pptx objects.
 * @author jhorvath
 */
public class ParsePptxCmd extends PpdCommand {
	
	private XMLSlideShow xmlFileA;
	private XMLSlideShow xmlFileB;
	
	private PptxSlideShow ppdFileA;
	private PptxSlideShow ppdFileB;
	
	public static final String ERROR_NULL_OBJECT = "POI object cannot be null:";
	
	/**
	 * Constructor. 
	 * 
	 * @param xmlFileA XMLSlideShow
	 * @param xmlFileB XMLSlideShow
	 */
	public ParsePptxCmd(XMLSlideShow xmlFileA, XMLSlideShow xmlFileB) {
		this.xmlFileA = xmlFileA;
		this.xmlFileB = xmlFileB;
		this.ppdFileA = new PptxSlideShow();
		this.ppdFileB = new PptxSlideShow();
	}

	@Override
	public void perform() throws PpdException {
		Debugger.printLog("Parse POI objects into PPD objects", this.getClass().getName());
		
		success = false;
		
		nullCheck();
		
		parseFile(this.xmlFileA, this.ppdFileA);
		parseFile(this.xmlFileB, this.ppdFileB);
		
		success = true;
	}
	
	/**
	 * Parses POI slide-show object to a PPD slide-show object. 
	 * 
	 * @param xmlFile XMLSlideShow
	 * @param ppdFile XMLSlideShow
	 */
	private void parseFile(XMLSlideShow xmlFile, PptxSlideShow ppdFile) {
		
		for (XSLFSlide xmlSlide : xmlFile.getSlides()) {
			PptxSlide ppdSlide = new PptxSlide();
			
			ppdSlide.setSlideName(xmlSlide.getSlideName());
			
			ppdSlide.setSlideNumber(xmlSlide.getSlideNumber());
			
			
			
			
			ppdFile.getSlideList().add(ppdSlide);
		}
	}
	
	/**
	 * Performs null checking operations on POI objects.
	 * 
	 * @throws PpdException
	 */
	private void nullCheck() throws PpdException {
		if (xmlFileA == null || xmlFileB == null) {
			String message = ERROR_NULL_OBJECT;
			if (xmlFileA == null) {
				message = message + " file A"; 
			}
			if (xmlFileB == null) {
				message = message + " file B"; 
			}
			throw new PpdException(message);
		}
	}

	public PptxSlideShow getPpdFileA() {
		return ppdFileA;
	}

	public PptxSlideShow getPpdFileB() {
		return ppdFileB;
	}
	
}
