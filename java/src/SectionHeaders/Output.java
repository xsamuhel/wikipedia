package SectionHeaders;

import java.util.ArrayList;

//struktura pre prvu cast vystupneho suboru
class Output {
	public String sectionHeader;
	public double documentFrequency;
	
	Output(String sectionHeader, double documentFrequency){
		this.documentFrequency = documentFrequency;
		this.sectionHeader = sectionHeader;
	}
}

//struktura pre druhu cast vystupneho suboru
class OutputGazetteer {
	public String sectionHeader;
	public ArrayList<String> sectionHeadersList;
	
	OutputGazetteer(String sectionHeader, ArrayList<String> sectionHeadersList){
		this.sectionHeadersList = sectionHeadersList;
		this.sectionHeader = sectionHeader;
	}
}
