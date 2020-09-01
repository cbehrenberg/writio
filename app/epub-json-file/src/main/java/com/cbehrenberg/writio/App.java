package xyz.writio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.writio.api.VersionedMessage;
import xyz.writio.api.v1.Book;
import xyz.writio.api.v1.Chapter;
import xyz.writio.api.v1.Page;
import xyz.writio.api.v1.Section;

import nl.siegmann.epublib.domain.MediaType;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

public class App {

	private static final String IN_FILE_OPTION = "in-file";
	private static final String OUT_FILE_OPTION = "out-file";

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] cliArgs)
			throws FileNotFoundException, IOException, XMLStreamException, ParseException {

		// parse CLI args
		CommandLine cli = new DefaultParser().parse(getCliOptions(), cliArgs);

		// show args
		logger.info("args:");
		for (Option option : cli.getOptions()) {
			logger.info("\t--{} = '{}'", option.getLongOpt(), option.getValue());
		}

		// in file
		File inFile = new File(cli.getOptionValue(IN_FILE_OPTION));
		assert inFile.exists() && inFile.isFile() && inFile.canRead() && inFile.length() > 0;

		// out file
		File outFile = new File(cli.getOptionValue(OUT_FILE_OPTION));

		// parse epub file
		logger.info("parsing epub file {}...", inFile.getAbsolutePath());
		nl.siegmann.epublib.domain.Book epub = new EpubReader().readEpub(new FileInputStream(inFile));

		// extract metadata
		logger.info("extracting metadata...");
		String title = extractEpubTitle(epub);
		String language = extractEpubLanguage(epub);

		// extract pages
		logger.info("extracting pages...");
		List<Page> pages = extractEpubPages(epub);

		// build book
		logger.info("building book model from extracted data...");
		Book book = Book.newBuilder().setTitle(title).setLanguage(language).addAllPages(pages).build();

		// serializing to versioned JSON string
		VersionedMessage versionedBook = new VersionedMessage(book);
		logger.info("serializing book to versioned JSON string with version type: '{}'...",
				versionedBook.getVersionType());
		String versionedBookJsonStr = versionedBook.getJsonStr();

		// write to file
		logger.info("writing versioned book JSON string to file {}...", outFile.getAbsolutePath());
		FileUtils.writeStringToFile(outFile, versionedBookJsonStr, StandardCharsets.UTF_8);

		logger.info("done!");
	}

	/**
	 * @return CLI options
	 */
	private static Options getCliOptions() {

		Options options = new Options();

		// file to read from
		Option inFile = Option.builder().longOpt(IN_FILE_OPTION).hasArg().numberOfArgs(1).required().build();

		// file to write to
		Option outFile = Option.builder().longOpt(OUT_FILE_OPTION).hasArg().numberOfArgs(1).required().build();

		options = options.addOption(inFile).addOption(outFile);

		return options;
	}

	private static List<Page> extractEpubPages(nl.siegmann.epublib.domain.Book epub) throws IOException {

		assert epub != null;

		List<Page> pages = new ArrayList<Page>();

		Spine epubSpine = epub.getSpine();
		if (epubSpine != null) {

			List<SpineReference> epubSpineReferences = epubSpine.getSpineReferences();
			if (epubSpineReferences != null) {

				for (SpineReference epubSpineReference : epubSpineReferences) {

					Page page = extractEpubPage(epubSpineReference);

					if (page != null) {
						pages.add(page);
					}
				}
			}
		}

		return pages;
	}

	private static Page extractEpubPage(SpineReference epubSpineReference) throws IOException {

		assert epubSpineReference != null;

		Page page = null;
		List<Chapter> chapters = new ArrayList<Chapter>(1);

		Resource epubPageResource = epubSpineReference.getResource();
		assert epubPageResource != null && epubPageResource.getSize() > 0;

		String epubPageEncoding = epubPageResource.getInputEncoding();

		MediaType epubPageMediaType = epubPageResource.getMediaType();
		assert epubPageMediaType != null && epubPageMediaType.getName() != null;

		if (epubPageMediaType.getName().compareTo("application/xhtml+xml") == 0) {

			InputStream epubPageXhtmlInputStream = null;
			try {

				epubPageXhtmlInputStream = epubPageResource.getInputStream();
				chapters = extractEpubChapters(epubPageEncoding, epubPageXhtmlInputStream);

			} finally {
				epubPageXhtmlInputStream.close();
			}
		}

		page = Page.newBuilder().addAllChapters(chapters).build();

		return page;
	}

	private static List<Chapter> extractEpubChapters(String pageEncoding, InputStream pageXhtmlInputStream)
			throws IOException {

		assert pageEncoding != null;
		assert pageXhtmlInputStream != null;

		final Pattern htmlHeadingRegex = Pattern.compile("h([1-6])");
		final Pattern htmlParagraphRegex = Pattern.compile("p");

		List<Chapter> chapters = new ArrayList<Chapter>(1);

		Element pageXhtmlBody = Jsoup.parse(pageXhtmlInputStream, pageEncoding, "").getElementsByTag("body").first();
		Elements pageXhtmlElements = pageXhtmlBody.children();

		// chapter = heading -> section(s) -> paragraph(s), etc
		// page can have multiple chapters
		// chapter can be lead by dangling text

		boolean isEmptyLine = false;

		Chapter chapter = Chapter.getDefaultInstance();
		Section section = Section.getDefaultInstance();

		for (Element xhtmlElement : pageXhtmlElements) {

			String xhtmlTag = xhtmlElement.tag().getName().toLowerCase().trim();
			String xhtmlText = xhtmlElement.text().strip();

			// empty line
			if (xhtmlText.isEmpty()) {
				isEmptyLine = true;
			} else {

				// paragraph
				if (htmlParagraphRegex.matcher(xhtmlTag).matches()) {

					// capture finished section
					// create new section
					if (isEmptyLine && section.getParagraphsCount() > 0) {

						chapter = chapter.toBuilder().addSections(section).build();
						section = Section.getDefaultInstance();
					}

					section = section.toBuilder().addParagraphs(xhtmlText).build();

					isEmptyLine = false;

				} else {

					// heading
					if (htmlHeadingRegex.matcher(xhtmlTag).matches()) {

						// capture finished chapter and create blank one
						if (chapter.getSectionsCount() > 0 || section.getParagraphsCount() > 0) {

							if (section.getParagraphsCount() > 0) {

								chapter = chapter.toBuilder().addSections(section).build();
								section = Section.getDefaultInstance();
							}

							chapters.add(chapter);
							chapter = Chapter.getDefaultInstance();
						}

						// capture heading title
						if (chapter.getTitle().isEmpty() && section.getParagraphsCount() == 0) {
							chapter = chapter.toBuilder().setTitle(xhtmlText).build();
						}

						isEmptyLine = false;
					}
				}
			}
		}

		// capture final chapter
		if (chapter.getSectionsCount() > 0 || section.getParagraphsCount() > 0) {

			if (section.getParagraphsCount() > 0) {
				chapter = chapter.toBuilder().addSections(section).build();
			}

			chapters.add(chapter);
		}

		return chapters;
	}

	private static String extractEpubLanguage(nl.siegmann.epublib.domain.Book epub) {

		String language = null;

		if (epub != null) {

			Metadata epubMetadata = epub.getMetadata();
			if (epubMetadata != null) {
				language = epubMetadata.getLanguage();
				if (language != null) {
					language = language.strip();
				}
			}
		}

		return language;
	}

	private static String extractEpubTitle(nl.siegmann.epublib.domain.Book epub) {

		String title = null;

		if (epub != null) {
			title = epub.getTitle();
			if (title != null) {
				title = title.strip();
			}
		}

		return title;
	}
}
