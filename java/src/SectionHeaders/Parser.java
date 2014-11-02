package SectionHeaders;
/*
	autor: Patrik Samuhel
	nazov: Section_Pareser
	opis: Parsovanie section headers a subsection headers, vyhladavanie a staststiky
 */

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;


public class Parser {

static int pages = 0;
	
	public static void main(String args[]) {
		
		String xmlFile = "C:\\VI\\wiki1.xml";
//		String xmlFile = "data\\sample_input_enwiki-latest-pages-articles1.xml";
		
		System.out.println(Start(xmlFile));
		
		return;
	}
	
	public static String Start(String path){
		

		Root root = getItemsFromXml(path);
		
		return Output(root, pages);
	}

//	@SuppressWarnings("unchecked")
	public static Root getItemsFromXml(String xmlFile) {
		
		String startElementName;
		Root root = null;
		ArrayList<SectionHeader> sectionList;
		String title = "";
		String text = "";
		
		try {
			// create xml reader event with inputstream
			XMLInputFactory inputFactory = XMLInputFactory.newFactory();
			InputStream input = new FileInputStream(xmlFile);
			XMLEventReader inputEventReader = inputFactory
					.createXMLEventReader(input);

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
						event = inputEventReader.nextEvent();
						title = event.asCharacters().getData();
					}

					if (startElementName.equals("text")) {
						pages++;
						event = inputEventReader.nextEvent();
						text += event.asCharacters().getData();
						event = inputEventReader.nextEvent();
						while(!event.isEndElement()) {
							text += event.asCharacters().getData();
							event = inputEventReader.nextEvent();
						}
						
						ParseText(sectionList, title, text, root);
						text = "";
					}

				}

//				if (event.isEndElement()) {
//					EndElement endElement = event.asEndElement();
//					String endElementName = endElement.asEndElement().getName()
//							.getLocalPart();
//					if (endElementName.equals("page")) {
//						//items.add(newItem);
//					}
//				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return root;
	}
	
	private static void ParseText(ArrayList<SectionHeader> sectionList,
			String title, String text, Root root) {
		
		String[] lines = text.split("\n");
		boolean skip;
		String header;
		Article last = null;
		ArrayList<Article> articleList = null;
		
		for (String line : lines) {
			skip = false;
			if(line.startsWith("====")){
				break;
			}
			if (line.startsWith("===")) {
				header = StringUtils.strip(line, "===").trim();
				last.getSubsecionsList().add(new SubSection(header));
			} else if (line.startsWith("==")) {
				header = StringUtils.strip(line, "==").trim();
				if (sectionList.size() == 0) {
					sectionList.add(new SectionHeader(header));
					root.SetinoHeadersCount++;
					articleList = sectionList.get(sectionList.size() - 1)
							.getArticlesList();
					articleList.add(new Article(title));
					last = articleList.get(articleList.size() - 1);
					continue;
				}
				for (SectionHeader sectionHeader : sectionList) {
					if (sectionHeader.getname().equals(header)) {
						sectionHeader.getArticlesList().add(
								new Article(title));
						last = sectionHeader.getArticlesList().get(
								sectionHeader.getArticlesList().size() - 1);
						skip = true;
						break;
					}
				}
				if (skip) {
					continue;
				} else {
					sectionList.add(new SectionHeader(header));
					root.SetinoHeadersCount++;
					articleList = sectionList.get(sectionList.size() - 1)
							.getArticlesList();
					articleList.add(new Article(title));
					last = articleList.get(articleList.size() - 1);
					continue;
				}
			}
		}
		
		return;
		
	}

	public static String Output(Root root, int pages){
		
		String maxDFHeader = "";
		double maxDF = 0;
		String outputString = "";
		
		outputString += "Output\n\n";
		for (SectionHeader sectionHeader : root.getListOfSectionHeaders()) {
			sectionHeader.setDocumentFrequency((double)sectionHeader.getArticlesList().size() / (double)pages);
			outputString += sectionHeader.getname() + ": " + sectionHeader.getDocumentFrequency() + "\n";
			if(maxDF < sectionHeader.documentFrequency){
				maxDF = sectionHeader.documentFrequency;
				maxDFHeader = sectionHeader.getname();
			}
		}
		
		outputString += "\n----------\n";
		outputString += "Biggest DF:\n"+"Section - " + maxDFHeader + "\nDF - " + maxDF;

		return outputString;
	}


}
