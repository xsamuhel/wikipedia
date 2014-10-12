import java.util.ArrayList;


public class Root {
	
	public int SetinoHeadersCount = 0;
	
	private ArrayList<SectionHeader> SectionsList = new  ArrayList<SectionHeader>();
	
	public ArrayList<SectionHeader> getListOfSectionHeaders(){
		return this.SectionsList;
	}
}

class SectionHeader{
	
	private String name;
	private ArrayList<Article> articles;
	
	public SectionHeader(String name){
		this.name = name;
		this.articles = new ArrayList<Article>();
	}
	
	public String getname(){
		return this.name;
	}
	
	public ArrayList<Article> getArticlesList(){
		return this.articles;
	}
}

class Article{
	
	private String title;
	private ArrayList<SubSection> subsections;
	
	public Article(String title){
		this.title = title;
		subsections = new ArrayList<SubSection>();
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public ArrayList<SubSection> getSubsecionsList(){
		return this.subsections;
	}
}

class SubSection{
	private String SubName;
	
	public SubSection(String name) {
		this.SubName = name;
	}
	public SubSection() {
		// TODO Auto-generated constructor stub
	}
	
	public String getSubName(){
		return SubName;
	}
}