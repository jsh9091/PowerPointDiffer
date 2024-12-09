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

import com.horvath.pptdiffer.application.Debugger;
import com.horvath.pptdiffer.engine.model.PptxSlideShow;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Handles operations of getting slide text from PPD slide-show data models.
 * @author jhorvath 
 */
public final class GetSlideTextForCompareCmd extends AbstractCompareCmd {
	
	private int index;
	private PptxSlideShow slideshow;
	private String slideText = "";
	
	/**
	 * Constructor. 
	 * 
	 * @param index index 
	 * @param slideshow PptxSlideShow
	 */
	public GetSlideTextForCompareCmd(int index, PptxSlideShow slideshow) {
		this.index = index;
		this.slideshow = slideshow;
	}
	

	@Override
	public void perform() throws PpdException {
		Debugger.printLog("Get the slide text for external test and comparison.", this.getClass().getName());
		success = false;
		
		if (rangeCheck(index, slideshow)) {
			slideText = slideshow.getSlideList().get(index).getText();
		}
		
		success = true;
	}

	public String getSlideText() {
		return slideText;
	}

}
