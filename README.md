# PowerPoint Differ 
PowerPoint Differ (PPD) is a tool for comparing a pair of PowerPoint (.pptx) files. The PPD can be used as a desktop application to generate a detailed comparison report. Alternatively, the PPD can be used as a library for making comparisons between a pair of pptx files. A use case for using the PPD as a library, is in an application that generates new or edits existing PowerPoint files and using the PPD as a library in unit testing for these generation or editing PowerPoint features.

## GUI Quick Start

After starting up the PPD desktop application, click a Select PPTX to display an “Open As” type of dialog to locate and select a file for comparison. Perform this action of selecting a PowerPoint file for both File A and File B, as labeled in the main application window. With two files selected, click the “Compare” button to run the comparison operation. When the comparison is complete, a popup dialog is displayed presenting a detailed report with findings of differences between the two selected pptx files. 

## Library Quick Start

Instantiate a “Differ” object passing a pair of java.io.File objects to the Differ constructor. The first File object passed to the constructor is “File A” and the second File passed to the constructor is “File B”. With the Differ object initialized, call methods on the Differ object to make comparisons on the files. 


	@Test
	public void exampleUsage() {
		File fileA = new File("fileA.pptx");
		File fileB = new File("fileB.pptx");
		
		try {
			Differ diff = new Differ(fileA, fileB);
			
			// we expect at least the meta-data in files to be different
			Assert.assertFalse(diff.isSameFile());
			// we expect the files to have the same number of slides
			Assert.assertEquals(diff.slideCount_fileA(), diff.slideCount_fileB());
			// parsed text from the PPTX file as the whole is the same
			Assert.assertEquals(diff.wholeFileText_FileA(), diff.wholeFileText_FileB());
			// metadata is not the same
			Assert.assertNotEquals(diff.metadata_FileA(), diff.metadata_FileB());
			
			// iterate over all slides in slide shows
			// if we got here, we know both files have the same slide count
			for (int i = 0; i < diff.slideCount_fileA(); i++) {
				// slide names in each file are expected to be the same
				Assert.assertEquals(diff.slideName_fileA(i), diff.slideName_fileB(i));
				// compare text parsed from slide 
				Assert.assertEquals(diff.slideText_fileA(i), diff.slideText_fileB(i));
			}
			
		} catch (PpdException ex) {
			System.err.println(ex.getMessage());
		}
	}


