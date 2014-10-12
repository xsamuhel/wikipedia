import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;



@XStreamAlias("mediawiki")
class mediawiki{
    
    @XStreamImplicit(itemFieldName = "page")
    public ArrayList<page> pages = new ArrayList<page>();
    
    ArrayList<page> getPages(){
    	return pages;
    }
}

@XStreamAlias("page")
class page{
    
    @XStreamAlias("title")
    private String title;
    
    @XStreamAlias("ns")
    private String ns;
    
    @XStreamAlias("id")
    private String id;
    
    @XStreamAlias("revision")
    private revision revision;
    
    String getTitle(){
    	return title; 
    }
    
    revision getRevision(){
    	return revision;
    }
}

@XStreamAlias("revision")
class revision{
    @XStreamAlias("id")
    private String id;
    
    @XStreamAlias("parentid")
    private String parentid;
    
    @XStreamAlias("timestamp")
    private String timestamp;
    
    @XStreamAlias("contributor")
    private contributor contributor;
    
    @XStreamAlias("minor")
    private String minor;
    
    @XStreamAlias("comment")
    private String comment;
    
    @XStreamAlias("text")
    private String text;
    
    @XStreamAlias("sha1")
    private String sha1;
    
    @XStreamAlias("model")
    private String model;
    
    @XStreamAlias("format")
    private String format;
    
    contributor getContributor(){
    	return contributor;
    }
    
    String getText(){
    	return text;
    }
}

@XStreamAlias("contributor")
class contributor{
    @XStreamAlias("username")
    private String username;
    
    @XStreamAlias("id")
    private String id;
}
