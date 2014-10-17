package Test;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SectionHeadersTest {

	@Test()
	public void test() {
		

		Assert.assertEquals(SectionHeaders.Parser.start("data\\sample_input_enwiki-latest-pages-articles1.xml"),testString);
	}

	String testString = "Output\n\nCrew: 1.0\nMission parameters: 0.6666666666666666\nMission highlights: 1.0\nMission insignia: 0.6666666666666666\nSpacecraft location: 0.3333333333333333\nGallery: 0.3333333333333333\nSee also: 1.0\nReferences: 1.0\nBibliography: 0.6666666666666666\nExternal links: 1.0\nPlanning and training: 0.6666666666666666\nHardware: 0.3333333333333333\nScandals: 0.3333333333333333\nVisibility from space: 0.3333333333333333\nMultimedia: 0.3333333333333333\nDepiction in popular culture: 0.3333333333333333\nLunar subsatellite PFS-2: 0.3333333333333333\nSpacecraft locations: 0.3333333333333333\n\n----------\nBiggest DF:\nSection - Crew\nDF - 1.0";
}
