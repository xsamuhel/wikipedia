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
		mediawiki data;

		if ((data = dataLoad("data\\sample_input_enwiki-latest-pages-articles1.xml")) == null) {
			return;
		}

		Root root = parseHeaders(data);

		System.out.println("koniec");
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
