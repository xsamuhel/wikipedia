package SectionHeaders;

/*
 autor: Patrik Samuhel
 nazov: SectionHeaders
 opis: Parsovanie section headers a subsection headers, staststiky
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
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
	static String outputCountString = "all";

	public static void main(String args[]) throws Exception {
		
		String xmlFileInput = "";
		String xmlFileOutput = "";
		int outputCount = 0;
		
		//nacitanie vstupu z konzoly
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		try{
			System.out.println("Zadajte cestu ku vstupnemu suboru (napr: \"C:\\wiki\\input.xml\"): ");
			xmlFileInput = bufferRead.readLine();
			System.out.println("Zadajte cestu ku vstupnemu suboru (napr: \"C:\\wiki\\output.xml\"): ");
			xmlFileOutput =  bufferRead.readLine();
			System.out.println("Zadajte pocet section headerov vo vystupnom subore (prazdny vstup vypise vsetko): ");
			outputCountString = bufferRead.readLine();
			if(outputCountString.equals("")){
				outputCountString = "all";
			}
			else{
				outputCountString = "";
				outputCount = Integer.parseInt(outputCountString);
			}
		}
		catch(Exception ex){
			//ukoncenie programu v pripade zleho vstupu
			System.out.println("Chybny vstup: " + ex.getMessage());
			return;
		}
		
		//casovac
		start = System.currentTimeMillis();
		Writer out = new BufferedWriter(new OutputStreamWriter(
			    new FileOutputStream(xmlFileOutput), "UTF-8"));
			try {
				//zapis spracovanych do suboru
			    out.write(Start(xmlFileInput, outputCount));
			} finally {
			    out.close();
			}

		stop = System.currentTimeMillis();
		System.out.println("Elapsed time: " + (double)((double)(stop - start)/(double)1000) + " sec");

		out.close();
		return;
	}

	public static String Start(String path, int outputCount) throws IOException {

		//vytvorenie struktury 
		Root root = getItemsFromXml(path);
		
		stop = System.currentTimeMillis();
		System.out.println("Nacitanie dat skoncilo\n"
							+ "Elapsed time: " + (double)((double)(stop - start)/(double)1000) + " sec\n"
							+ "Zaciatok vypoctu pomocou gazetteers a zapis vystupu do suboru");
		
		return Output(root, pages, outputCount);
	}

	public static Root getItemsFromXml(String xmlFile) {

		String startElementName;
		Root root = null;
		HashMap<String, SectionHeader> sectionList;
		String title = "";
		String text = "";
		XMLEventReader inputEventReader;
		XMLInputFactory inputFactory;

		try {
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

			//citanie elementov xml suboru
			while (inputEventReader.hasNext()) {

				XMLEvent event = inputEventReader.nextEvent();

				if (event.isStartElement()) {
					startElementName = event.asStartElement().getName()
							.getLocalPart();

					if (startElementName.equals("title")) {
						title = inputEventReader.getElementText();
					}

					if (startElementName.equals("text")) {
						pages++;
						text += inputEventReader.getElementText();


						ParseText(sectionList, title, text, root);
						text = "";
					}

				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{

		}
		return root;
	}

	private static void ParseText(HashMap<String, SectionHeader> sectionList,
			String title, String text, Root root) {

		String[] lines = text.split("\n");
		String header;
		ArrayList<String> articleList = null;

		for (String line : lines) {
			if (line.startsWith("===")) {
				continue;
			}
			else if (line.startsWith("==") && (line.endsWith("=="))) {
				header = StringUtils.strip(line, "==").trim();
				
				if(sectionList.containsKey(header)){
					articleList = sectionList.get(header).getArticlesList();
					articleList.add(title);
					continue;
				}
				else if (sectionList.isEmpty()) {
					sectionList.put(header, new SectionHeader(header));
					root.SetinoHeadersCount++;
					articleList = sectionList.get(header).getArticlesList();
					articleList.add(title);
					continue;
				}
				else{
					sectionList.put(header, new SectionHeader(header));
					root.SetinoHeadersCount++;
					articleList = sectionList.get(header).getArticlesList();
					articleList.add(title);
					continue;
				}
			}
		}
		return;
	}

	public static String Output(Root root, int pages, int outputCount) throws IOException{

		StringBuilder outputString = new StringBuilder();
		String sectionName;
		SectionHeader sectionData;
		ArrayList<Output> output = new ArrayList<Output>();
		ArrayList<OutputGazetteer> outputGaz = new ArrayList<OutputGazetteer>();
		StringBuilder gazetteersString = new StringBuilder();		
		
		//odkomenovat v pripade pozadovaneho vytvorenia dalsieho vystupu
		//writeArticleNamesIntoFile(root);
		
		HashMap<String, SectionHeader> allSections = root.getListOfSectionHeaders();
		
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
			
			//uvolnenie pamate
			sectionData.getArticlesList().clear();

			//vypocita TOP document frequency
			for(int i = 0; i < output.size(); i++){
				if(output.get(i).documentFrequency <= sectionData.getDocumentFrequency()){
					output.add(i, new Output(sectionData.getname(),sectionData.getDocumentFrequency()));
					break;
				}
				if(outputCountString.equals("all")){
					continue;
				}
				else if(i==outputCount)break;
			}
			if(output.size() == 0){
				output.add(new Output(sectionData.getname(),sectionData.getDocumentFrequency()));
			}
			
		}
		
		List<int[]> matches = gazetteer.find(gazetteersString.toString());
		
		HashMap<String, ArrayList<String>> finalgazetteers = new HashMap<String, ArrayList<String>>();

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
		
		//vypise pozadovane mnozstvo section headerov
		for(int i = 0; i < output.size(); i++){
			outputString.append(output.get(i).sectionHeader + ": " + output.get(i).documentFrequency + "\n");
			
			if(outputCountString.equals("all")){
				continue;
			}
			else if(i==outputCount)break;
		}
		
		outputString.append("\n----------\n");
		
		if(finalgazetteers.isEmpty()){
			outputString.append("Nenasli sa ziadne spojenia headrov\n");
		}
		
		
		
		for (String entry : finalgazetteers.keySet()) {
			
			//vypocita pozadovaneho poctu gazetteers
			for(int i = 0; i < outputGaz.size(); i++){
				if(outputGaz.get(i).sectionHeadersList.size() <= finalgazetteers.get(entry).size()){
					outputGaz.add(i, new OutputGazetteer(entry, finalgazetteers.get(entry)));
					break;
				}
				
				if(outputCountString.equals("all")){
					continue;
				}
				else if(i==outputCount)break;
			}
			if(outputGaz.size() == 0){
				outputGaz.add(new OutputGazetteer(entry ,finalgazetteers.get(entry)));
			}
			
		}
		
		for(int i = 0; i < outputGaz.size(); i++){
			outputString.append(outputGaz.get(i).sectionHeader + " [" + outputGaz.get(i).sectionHeadersList.size() + "]\n");
			
			//vypis headrov v ktorych sa nachadza iny header
//			for (String gaz : outputGaz.get(i).sectionHeadersList) {
//				outputString.append("- " + gaz + "\n");
//			}
			
			if(outputCountString.equals("all")){
				continue;
			}
			else if(i==outputCount)break;
		}
	

		outputString.append("\n----------\n");

		return outputString.toString();
	}
	
	//pridavny vystup pre kontrolu alternativnych mien clankov
	private static void writeArticleNamesIntoFile(Root root) throws IOException{
		
		String sectionName;
		ArrayList<String> articleList;
		
		PrintWriter pw = new PrintWriter(new FileWriter("output - articles.txt"));
		
		HashMap<String, SectionHeader> allSections = root.getListOfSectionHeaders();
		Iterator<String> mapIterator = root.getListOfSectionHeaders().keySet().iterator();
		
		try{
			while(mapIterator.hasNext()){
				sectionName = mapIterator.next();
				articleList = allSections.get(sectionName).getArticlesList();
				
				if(articleList.size() > 1)continue;
				
				for (String article : articleList) {
					pw.println(sectionName + " - " + article );
				}
			}
		}
		catch(Exception e){
			System.out.println("Chyba pri zapise do suboru s nazvami clankov: " + e.getMessage());
		}
		finally{
			pw.close();
		}
	}

	//zaokruhlenie
	public static double round(double d, int decimalPlace){
	    BigDecimal bd = new BigDecimal(Double.toString(d));
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	  }

}
