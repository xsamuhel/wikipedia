package SectionHeaders;
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

import org.apache.commons.lang3.StringUtils;

public class Parser {

	public static void main(String[] args) throws IOException {

		System.out.println(start("data\\sample_input_enwiki-latest-pages-articles1.xml"));
		System.out.println("koniec");

	}
	
	public static String start(String path){
		mediawiki data;
		Root root;
		
//		if ((data = dataLoad("data\\enwiki-latest-pages-articles25.xml")) == null) {
//			return "";
//		}
		
		if ((data = dataLoad(path)) == null) {
			return "";
		}
		root = parseHeaders(data);
		
		return output(root, data);
	}
	
	public static String output(Root root, mediawiki data){
		
		String maxDFHeader = "";
		double maxDF = 0;
		String outputString = "";
		
		outputString += "Output\n\n";
		for (SectionHeader sectionHeader : root.getListOfSectionHeaders()) {
			sectionHeader.setDocumentFrequency((double)sectionHeader.getArticlesList().size() / (double)data.getPages().size());
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

	private static Root parseHeaders(mediawiki data) {

		String[] textLines;
		String header;
		boolean skip;

		Root root = new Root();

		ArrayList<SectionHeader> headerList = root.getListOfSectionHeaders();

		for (int i = 0; i < data.getPages().size(); i++) {
			page article = data.getPages().get(i);

			textLines = article.getRevision().getText().split("\n");

			Article last = null;
			ArrayList<Article> articleList = null;

			for (String line : textLines) {
				skip = false;
				if(line.startsWith("====")){
					break;
				}
				if (line.startsWith("===")) {
					header = StringUtils.strip(line, "===").trim();
					last.getSubsecionsList().add(new SubSection(header));
				} else if (line.startsWith("==")) {
					header = StringUtils.strip(line, "==").trim();
					if (headerList.size() == 0) {
						headerList.add(new SectionHeader(header));
						root.SetinoHeadersCount++;
						articleList = headerList.get(headerList.size() - 1)
								.getArticlesList();
						articleList.add(new Article(article.getTitle()));
						last = articleList.get(articleList.size() - 1);
						continue;
					}
					for (SectionHeader sectionHeader : headerList) {
						if (sectionHeader.getname().equals(header)) {
							sectionHeader.getArticlesList().add(
									new Article(article.getTitle()));
							last = sectionHeader.getArticlesList().get(
									sectionHeader.getArticlesList().size() - 1);
							skip = true;
							break;
						}
					}
					if (skip) {
						continue;
					} else {
						headerList.add(new SectionHeader(header));
						root.SetinoHeadersCount++;
						articleList = headerList.get(headerList.size() - 1)
								.getArticlesList();
						articleList.add(new Article(article.getTitle()));
						last = articleList.get(articleList.size() - 1);
						continue;
					}
				}
			}
		}

		return root;
	}

	private static mediawiki dataLoad(String inputPath) {

		try {
			InputStream fr = new FileInputStream(inputPath);

			// inicializacia parseru a anotacii jednotlivych xml tagov
			XStream xstream = new XStream();
			xstream.processAnnotations(mediawiki.class);
			xstream.processAnnotations(page.class);
			xstream.processAnnotations(revision.class);
			return (mediawiki) xstream.fromXML(fr);
		} catch (Exception ex) {
			System.out.println("dataLoad error: " + ex.getMessage());
			return null;
		}
	}

}
