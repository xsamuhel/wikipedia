package Test;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SectionHeadersTest {

	@Test()
	public void test() throws IOException{
		

		Assert.assertEquals(SectionHeaders.Parser.Start("data\\sample_input_enwiki-latest-pages-articles1.xml", 0),testString);
	}

	String testString = "Output:\n\nExternal links: 1.0\nReferences: 1.0\nSee also: 1.0\nMission highlights: 1.0\nCrew: 1.0\nBibliography: 0.66667\nMission insignia: 0.66667\nMission parameters: 0.66667\nPlanning and training: 0.66667\nSpacecraft locations: 0.33333\nHardware: 0.33333\nGallery: 0.33333\nDepiction in popular culture: 0.33333\nScandals: 0.33333\nVisibility from space: 0.33333\nSpacecraft location: 0.33333\nLunar subsatellite PFS-2: 0.33333\nMultimedia: 0.33333\n\n----------\nNenasli sa ziadne spojenia headrov\n\n----------\n";
}
