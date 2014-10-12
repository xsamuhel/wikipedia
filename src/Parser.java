/*
	autor: Patrik Samuhel
	nazov: Section_Pareser
	opis: Parsovanie section headers a subsection headers, vyhladavanie a staststiky
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



import com.thoughtworks.xstream.XStream;

public class Parser {

    
    public static void main(String[] args) throws IOException
    {
    	mediawiki data;
    	String[] textLines;
    	
    	if((data = dataLoad("data\\sample_input_enwiki-latest-pages-articles1.xml")) == null){
    		return;
    	}

        //System.out.println("Number of bans = " + data.pages.size());
        //String[] lines = data.pages.get(0).getRevision().getText().split("\n");
        
    	Root root = new Root();
    	ArrayList<SectionHeader> headerList = root.getListOfSectionHeaders();
    	
        for(int i = 0; i< data.getPages().size(); i++){
        	page article = data.getPages().get(i);
        	
        	textLines = article.getRevision().getText().split("\n");
        	
        	for (String line : textLines) {
				if(line.startsWith("===")){
					System.out.println(line);
				}
				else if(line.startsWith("==")){
					headerList.add(new SectionHeader(line));
				}
			}
        	headerList.add(new SectionHeader(article.getTitle()));
        	root.SetinoHeadersCount++;
        	
			//System.out.println("Title" + i + ": " + article.getTitle() + "\n");
		

			
			System.out.println();
		}
        
        
        System.out.println("konec");
    }
    
    private static mediawiki dataLoad(String inputPath){
    	
    	try{
    	InputStream fr = new FileInputStream(inputPath);
        
    	//inicializacia parseru a anotacii jednotlivych xml tagov
        XStream xstream = new XStream();
        xstream.processAnnotations(mediawiki.class);
        xstream.processAnnotations(page.class);
        xstream.processAnnotations(revision.class);
        return (mediawiki) xstream.fromXML(fr);
    	}
    	catch(Exception ex){
    		System.out.println("dataLoad error: " + ex.getMessage());
    		return null;
    	}
    }
    
}













