package SectionHeaders;

import java.util.ArrayList;
import java.util.HashMap;

public class Root {

	public int SetinoHeadersCount = 0;

	private HashMap<String, SectionHeader> SectionsList = new HashMap<String, SectionHeader>();

	public HashMap<String, SectionHeader> getListOfSectionHeaders() {
		return this.SectionsList;
	}
}

class SectionHeader {

	private String name;
	private ArrayList<String> articles;
	public Double documentFrequency = 0.0;
	public Double collectionFrequency = 0.0;

	public Double getCollectionFrequency() {
		return collectionFrequency;
	}

	public void setCollectionFrequency(Double collectionFrequency) {
		this.collectionFrequency = collectionFrequency;
	}

	public SectionHeader(String name) {
		this.name = name;
		this.articles = new ArrayList<String>();
	}

	public String getname() {
		return this.name;
	}

	public ArrayList<String> getArticlesList() {
		return this.articles;
	}

	public void setDocumentFrequency(double value) {
		this.documentFrequency = value;
	}
	

	public double getDocumentFrequency() {
		return documentFrequency;
	}
}

class Article {

	private String title;

	public Article(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}
}

class Gazetteer{

	public String string1;
	public String string2;
	
	public Gazetteer(String string1, String string2) {
		this.string1 = string1;
		this.string2 = string2;
	}

}