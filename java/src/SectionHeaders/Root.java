package SectionHeaders;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Root {

	public int SetinoHeadersCount = 0;

	private LinkedHashMap<String, SectionHeader> SectionsList = new LinkedHashMap<String, SectionHeader>();

	public LinkedHashMap<String, SectionHeader> getListOfSectionHeaders() {
		return this.SectionsList;
	}
}

class SectionHeader {

	private String name;
	private ArrayList<Article> articles;
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
		this.articles = new ArrayList<Article>();
	}

	public String getname() {
		return this.name;
	}

	public ArrayList<Article> getArticlesList() {
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
	private ArrayList<SubSection> subsections;

	public Article(String title) {
		this.title = title;
		subsections = new ArrayList<SubSection>();
	}

	public String getTitle() {
		return this.title;
	}

	public ArrayList<SubSection> getSubsecionsList() {
		return this.subsections;
	}
}

class SubSection {
	private String SubName;

	public SubSection(String name) {
		this.SubName = name;
	}

	public SubSection() {
		// TODO Auto-generated constructor stub
	}

	public String getSubName() {
		return SubName;
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