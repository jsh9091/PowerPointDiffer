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

package com.horvath.pptdiffer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.horvath.pptdiffer.command.compare.CompareCmdTest;
import com.horvath.pptdiffer.command.compare.GenerateReportTextCmdTest;
import com.horvath.pptdiffer.command.compare.GetSlideNameForCompareCmdTest;
import com.horvath.pptdiffer.command.io.LoadPptxCmdTest;
import com.horvath.pptdiffer.command.io.WriteReportCmdTest;
import com.horvath.pptdiffer.command.loadfile.LoadFileCmdTest;
import com.horvath.pptdiffer.command.parse.ExtractWholeFilerTextCmdTest;
import com.horvath.pptdiffer.command.parse.ParsePptxCmdTest;
import com.horvath.pptdiffer.engine.differ.PptxDifferExactTest;
import com.horvath.pptdiffer.engine.differ.PptxDifferSlideCountTest;
import com.horvath.pptdiffer.engine.differ.PptxDifferTest;
import com.horvath.pptdiffer.gui.PpdWindowTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	LoadFileCmdTest.class,
	CompareCmdTest.class,
	PpdWindowTest.class,
	WriteReportCmdTest.class,
	PptxDifferTest.class,
	PptxDifferSlideCountTest.class,
	PptxDifferExactTest.class,
	LoadPptxCmdTest.class,
	DifferTest.class,
	ParsePptxCmdTest.class,
	GenerateReportTextCmdTest.class,
	ExtractWholeFilerTextCmdTest.class,
	GetSlideNameForCompareCmdTest.class
})

public class PpdTestSuite { }
