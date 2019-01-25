package com.gbeatty.arxivexplorer.network;

import android.util.Xml;

import com.gbeatty.arxivexplorer.helpers.ISO8601;
import com.gbeatty.arxivexplorer.models.Paper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class Parser {

    public static List<Paper> parse(InputStream in) throws XmlPullParserException, IOException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readPapers(parser);
        } finally {
            in.close();
        }
    }

    private static List<Paper> readPapers(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        List<Paper> papers = new CopyOnWriteArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                papers.add(readPaper(parser));
            } else {
                skip(parser);
            }
        }
        return papers;
    }

    private static Paper readPaper(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, null, "entry");
        String title = null;
        StringBuilder author = null;
        String summary = null;
        String updated = null;
        String published = null;
        String id = null;
        String pdfLink = null;
        String url = null;
        StringBuilder categories = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("author")) {
                if (author == null) {
                    author = new StringBuilder(readAuthor(parser));
                } else {
                    author.append(", ").append(readAuthor(parser));
                }
            } else if (name.equals("summary")) {
                summary = readSummary(parser);
                summary = summary.replace("<", "&lt;");
                summary = summary.replace(">", "&gt;");
            } else if (name.equals("updated")) {
                updated = readDate(parser);
            } else if (name.equals("published")) {
                published = readDate(parser);
            } else if (name.equals("id")) {
                String[] idUrl = readID(parser);
                id = idUrl[0];
                url = idUrl[1];
            } else if (name.equals("link") && parser.getAttributeValue(null, "title") != null &&
                    parser.getAttributeValue(null, "title").equals("pdf")) {
                pdfLink = readPDFURL(parser);
            } else if (name.equals("category")) {
                if (categories == null) {
                    categories = new StringBuilder(readCategory(parser));
                } else {
                    categories.append(", ").append(readCategory(parser));
                }
            } else {
                skip(parser);
            }
        }
        return new Paper(title, author.toString(), summary, updated, published, id, pdfLink, categories.toString(), url);
    }

    private static String readDate(XmlPullParser parser) throws IOException, XmlPullParserException, ParseException {
        Calendar cal = ISO8601.toCalendar(readText(parser));
        String date = new SimpleDateFormat("MM.dd.yyyy", Locale.getDefault()).format(cal.getTime()).toLowerCase(Locale.getDefault());
        return date;
    }

    private static String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "author");
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "name");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "name");
        do {
            try {
                parser.require(XmlPullParser.END_TAG, null, "author");
                break;
            } catch (Exception e) {
                parser.next();
            }
        } while (true);

        return author;
    }

    // For the tags title and summary, extracts their text values.
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        result = result.replaceAll("\\s+", " ");
        return result;
    }

    private static String readPDFURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String pdfLink = parser.getAttributeValue(null, "href");
        parser.next();
//        parser.require(XmlPullParser., null, "link");
        return pdfLink;
    }

    private static String readCategory(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "category");
        String pdfLink = parser.getAttributeValue(null, "term");
        parser.next();
//        parser.require(XmlPullParser., null, "link");
        return pdfLink;
    }

    private static String[] readID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "id");
        String url = readText(parser);
        String id = url.substring(url.lastIndexOf("/") + 1);
        parser.require(XmlPullParser.END_TAG, null, "id");
        return new String[] {id, url};
    }

//    private static DateTime readPublished(XmlPullParser parser) throws IOException, XmlPullParserException {
//        parser.require(XmlPullParser.START_TAG, null, "published");
//        DateTime publishedDate = readDateTime(parser);
//        parser.require(XmlPullParser.END_TAG, null, "published");
//        return publishedDate;
//    }

    private static String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "summary");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "summary");
        summary = summary.trim();
        return summary;
    }

    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }


    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
