package SectionHeaders;

/*
 autor: Patrik Samuhel
 nazov: SectionHeaders
 opis: Parsovanie section headers a subsection headers, staststiky
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.lang3.StringUtils;

import sk.sav.ui.ikt.nlp.gazetteer.character.CharacterGazetteer;
import sk.sav.ui.ikt.nlp.gazetteer.character.CharacterGazetteer.Representation;

public class Parser {

	static int pages = 0;
	static double start, stop;

	public static void main(String args[]) throws Exception {
		
		//"C:\\VI\\enwiki-latest-pages-articles.xml.bz2";
		//"C:\\VI\\enwiki-latest-pages-articles25";
		//"C:\\VI\\enwiki-latest-pages-articles3.xml-p000025001p000055000";		
		//"C:\\VI\\wiki2.xml";
		//"data\\sample_input_enwiki-latest-pages-articles1.xml";
		String xmlFile = "";
		PrintWriter out = new PrintWriter("output.txt");

		System.out.println("Zadajte cestu ku vstupnemu suboru (napr: \"C:\\wiki\\input.xml\"): ");
		
		//nacitanie suboru z konzoly
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		try{
			xmlFile = bufferRead.readLine();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
		//casovac
		start = System.currentTimeMillis();
		out.println(Start(xmlFile));
		stop = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (double)((double)(stop - start)/(double)1000) + " miliSec");

		out.close();
		return;
	}

	public static String Start(String path) {

		Root root = getItemsFromXml(path);
		
		stop = System.currentTimeMillis();
		System.out.println("Nacitanie dat skoncilo\n"
							+ "Elapsed time: " + (double)((double)(stop - start)/(double)1000) + " miliSec\n"
							+ "Zaciatok vypoctu pomocou gazetteers a zapis vystupu do suboru");
		
		return Output(root, pages);
	}

	// @SuppressWarnings("unchecked")
	public static Root getItemsFromXml(String xmlFile) {

		String startElementName;
		Root root = null;
		LinkedHashMap<String, SectionHeader> sectionList;
		String title = "";
		String text = "";
		XMLEventReader inputEventReader;
		XMLInputFactory inputFactory;

		try {
			// create xml reader event with inputstream
			
			
			//kontrola ci ide o xml alebo bz2
			if((xmlFile.substring(xmlFile.lastIndexOf('.') + 1, xmlFile.length())).equals("bz2")){
				
				FileInputStream fin = new FileInputStream(xmlFile);
			    BufferedInputStream bis = new BufferedInputStream(fin);
			    CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
			    BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
			    inputFactory = XMLInputFactory.newFactory();
			    inputEventReader = inputFactory
						.createXMLEventReader(br2);
			}
			else{
				FileInputStream input = new FileInputStream(xmlFile);
				BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
				inputFactory = XMLInputFactory.newFactory();
				inputEventReader = inputFactory
						.createXMLEventReader(br2);
			}
			


			root = new Root();
			sectionList = root.getListOfSectionHeaders();

			while (inputEventReader.hasNext()) {
				// xml reader event to get event and determine start element and
				// end element
				XMLEvent event = inputEventReader.nextEvent();

				if (event.isStartElement()) {
					startElementName = event.asStartElement().getName()
							.getLocalPart();

					if (startElementName.equals("title")) {
						//event = inputEventReader.nextEvent();
						title = inputEventReader.getElementText(); //event.asCharacters().getData();
					}

					if (startElementName.equals("text")) {
						pages++;
						//event = inputEventReader.nextEvent();
						text += inputEventReader.getElementText();//event.asCharacters().getData();
						//event = inputEventReader.nextEvent();
//						while (!event.isEndElement()) {
//							text += event.asCharacters().getData();
//							event = inputEventReader.nextEvent();
//						}

						ParseText(sectionList, title, text, root);
						text = "";
					}

				}

				// if (event.isEndElement()) {
				// EndElement endElement = event.asEndElement();
				// String endElementName = endElement.asEndElement().getName()
				// .getLocalPart();
				// if (endElementName.equals("page")) {
				// //items.add(newItem);
				// }
				// }
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{

		}
		return root;
	}

	private static void ParseText(LinkedHashMap<String, SectionHeader> sectionList,
			String title, String text, Root root) {

		String[] lines = text.split("\n");
		String header;
		Article last = null;
		ArrayList<Article> articleList = null;

		for (String line : lines) {
			if (line.startsWith("===")) {
				if (line.startsWith("====") || (last == null)) {
					continue;
				}
				header = StringUtils.strip(line, "===").trim();
				last.getSubsecionsList().add(new SubSection(header));
			} else if (line.startsWith("==") && (line.endsWith("=="))) {
				header = StringUtils.strip(line, "==").trim();
				if (sectionList.isEmpty()) {
					sectionList.put(header, new SectionHeader(header));
					root.SetinoHeadersCount++;
					articleList = sectionList.get(header).getArticlesList();
					articleList.add(new Article(title));
					last = articleList.get(articleList.size() - 1);
					continue;
				}
				if(sectionList.containsKey(header)){
					articleList = sectionList.get(header).getArticlesList();
					articleList.add(new Article(title));
					last = articleList.get(articleList.size() - 1);
					continue;
				}
				else{
					sectionList.put(header, new SectionHeader(header));
					root.SetinoHeadersCount++;
					articleList = sectionList.get(header).getArticlesList();
					articleList.add(new Article(title));
					last = articleList.get(articleList.size() - 1);
					continue;
				}
			}
			
		}

		return;

	}

	public static String Output(Root root, int pages) {

		StringBuilder outputString = new StringBuilder();
		String sectionName;
		SectionHeader sectionData;
		ArrayList<Output> output = new ArrayList<Output>();
		ArrayList<OutputGazetteer> outputGaz = new ArrayList<OutputGazetteer>();
		StringBuilder gazetteersString = new StringBuilder();
		boolean pridane;
		
		LinkedHashMap<String, SectionHeader> allSections = root.getListOfSectionHeaders();
		
		CharacterGazetteer gazetteer = new CharacterGazetteer(Representation.CHILDSIBLING, true);

		outputString.append("Output:\n\n");

		Iterator<String> mapIterator = root.getListOfSectionHeaders().keySet().iterator();
		
		while(mapIterator.hasNext()){
			sectionName = mapIterator.next();
			sectionData = allSections.get(sectionName);
			
			gazetteer.insert(sectionName);
			gazetteersString.append("¤" + sectionName + "¤");

			sectionData.setDocumentFrequency(round((double) sectionData.getArticlesList().size() / (double) pages, 5));
			sectionData.setCollectionFrequency(round((double) sectionData.getArticlesList().size() / (double) root.SetinoHeadersCount, 5));
			
			pridane = false;
			
			//vypocita TOP document frequency
			for(int i = 0; i < output.size(); i++){
				if(output.get(i).documentFrequency < sectionData.getDocumentFrequency()){
					output.add(i, new Output(sectionData.getname(),sectionData.getDocumentFrequency()));
					pridane = true;
					break;
				}
				
				//if(i==9)break;
			}
			if(output.size() == 0 || pridane == false){
				output.add(new Output(sectionData.getname(),sectionData.getDocumentFrequency()));
			}
			
		}
		

		
		List<int[]> matches = gazetteer.find(gazetteersString.toString());
		
		LinkedHashMap<String, ArrayList<String>> finalgazetteers = new LinkedHashMap<String, ArrayList<String>>();

		String found = "";
		int start;
		for (int[] match : matches) {
			start = match[0];
			
			//najde zaciatok stringu
			while(gazetteersString.charAt(start) != '¤'){
				start--;
			}
			start++;
			//najde koniec stringu
			while(gazetteersString.charAt(start) != '¤'){
				found+= gazetteersString.charAt(start);
				start++;
			}
			
			//vklada hodnoty do HashMapy podla klucoveho Stringu
			if(!(gazetteersString.substring(match[0], match[1]).equals(found))){
				if(!finalgazetteers.containsKey(gazetteersString.substring(match[0], match[1]))){
					finalgazetteers.put(gazetteersString.substring(match[0], match[1]), new ArrayList<String>());
					finalgazetteers.get(gazetteersString.substring(match[0], match[1])).add(found);
				}
				else{
					finalgazetteers.get(gazetteersString.substring(match[0], match[1])).add(found);
				}
			}
			found = "";
		}
		
		//vypise TOP10 document frequency
		for(int i = 0; i < output.size(); i++){
			outputString.append(output.get(i).sectionHeader + ": " + output.get(i).documentFrequency + "\n");
			
			//if(i==9)break;
		}
		
		outputString.append("\n----------\n");
		
		if(finalgazetteers.isEmpty()){
			outputString.append("Nenasli sa ziadne spojenia headrov\n");
		}
		
		
		
		for (String entry : finalgazetteers.keySet()) {
			
			pridane = false;
			
			//vypocita TOP gazetteers
			for(int i = 0; i < outputGaz.size(); i++){
				if(outputGaz.get(i).sectionHeadersList.size() < finalgazetteers.get(entry).size()){
					outputGaz.add(i, new OutputGazetteer(entry, finalgazetteers.get(entry)));
					pridane = true;
					break;
				}
				
				//if(i==9)break;
			}
			if(outputGaz.size() == 0 || pridane == false){
				outputGaz.add(new OutputGazetteer(entry ,finalgazetteers.get(entry)));
			}
			
		}
		
		for(int i = 0; i < outputGaz.size(); i++){
			outputString.append(outputGaz.get(i).sectionHeader + " [" + outputGaz.get(i).sectionHeadersList.size() + "]:\n");
			for (String gaz : outputGaz.get(i).sectionHeadersList) {
				outputString.append("- " + gaz + "\n");
			}
			//if(i==9)break;
		}
	

		outputString.append("\n----------\n");

		return outputString.toString();
	}
	
	public static double round(double d, int decimalPlace){
	    // see the Javadoc about why we use a String in the constructor
	    // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
	    BigDecimal bd = new BigDecimal(Double.toString(d));
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	  }

}
