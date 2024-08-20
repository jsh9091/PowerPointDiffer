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
import com.horvath.pptdiffer.application.PpdState;
import com.horvath.pptdiffer.command.PpdCommand;
import com.horvath.pptdiffer.engine.differ.PptxDiffer;
import com.horvath.pptdiffer.exception.PpdException;

/**
 * Command for controlling comparison operations. 
 * @author jhorvath
 */
public class CompareCmd extends PpdCommand {
	
	private boolean success;
	private String report;
	
	public static final String WARNING_MESSAGE = "WARNING: Comparison operations failed: ";
	public static final String EXACT_SAME_FILE_MESSAGE = "The two PowerPoint files are exactly the same.";

	@Override
	public void perform() {
		Debugger.printLog("Command for controling diff operations and result processing.", 
				this.getClass().getName());
		
		PpdState state = PpdState.getInstance();

		try {
			PptxDiffer differ = new PptxDiffer(state.getFileA(), state.getFileB());
			
			if (differ.isExactSameFile()) {
				message = EXACT_SAME_FILE_MESSAGE;
				
			} else {
				// TODO: this is not a permanent feature block
				message = "The two PowerPoint files contain differences.";
			}
			
			// set the report in the application state
			this.report = differ.getReport().trim();
			state.setReport(this.report);

			success = true;
			
		} catch (PpdException ex) {
			message = WARNING_MESSAGE + ex.getMessage();
			success = false;
		}
	}

	public boolean isSuccess() {
		return success;
	}
	
	public String getReport() {
		return report;
	}

}
