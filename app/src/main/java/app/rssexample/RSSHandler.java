package app.rssexample;

import android.text.Html;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class RSSHandler extends DefaultHandler {


    private State currentState = State.unknown;

    private RSSFeed feed;

    private RSSItem item;

    private boolean itemFound = false;

    private StringBuilder tagContent;

    public RSSHandler() {
        feed = new RSSFeed();
        item = new RSSItem();
    }

    //localName = the XML element encapsulating the data e.g. <item>

    @Override
    public void startElement(final String uri, final String localName,
            final String qName, final Attributes attributes)
            throws SAXException {

        //Reset the state to unknown, unless a desired element is found
        currentState = State.unknown;

        //Create a new content object for storing information from within the elements
        tagContent = new StringBuilder();

        //Ignoring case sensitivity, check localName against defined conditions
        if (localName.equalsIgnoreCase("item") || localName.equalsIgnoreCase("entry")) {
            itemFound = true;
            item = new RSSItem();
            currentState = State.unknown;
        } else if (qName.equalsIgnoreCase("title")) {
            currentState = State.title;
        } else if (qName.equalsIgnoreCase("description") || qName.equalsIgnoreCase("content:encoded") || qName
                .equalsIgnoreCase("content")) {
            currentState = State.description;
        } else if (qName.equalsIgnoreCase("link") || qName.equalsIgnoreCase("origLink")) {
            currentState = State.link;
        } else if (qName.equalsIgnoreCase("pubdate") || qName.equalsIgnoreCase("published")) {
            currentState = State.pubdate;
        } else if (qName.equalsIgnoreCase("media:thumbnail")) {
            currentState = State.media;
            String attrValue = attributes.getValue("url");
            item.setThumburl(attrValue);
        } else if (qName.equalsIgnoreCase("media:content")) {
            currentState = State.media;
            String attrValue = attributes.getValue("url");
            if (attributes.getValue("type") == null || attributes == null) {
                return;
            } else if (attributes.getValue("type").startsWith("image")) {
                item.setThumburl(attrValue);
            } else if (attributes.getValue("type").startsWith("video")) {
                item.setVideourl(attrValue);
            } else if (attributes.getValue("type").startsWith("audio")) {
                item.setAudiourl(attrValue);
            }
        } else if (qName.equalsIgnoreCase("enclosure")) {
            currentState = State.media;
            String attrValue = attributes.getValue("url");
            if (attributes == null || attributes.getValue("type") == null) {
                return;
            } else if (attributes.getValue("type").startsWith("image")) {
                item.setThumburl(attrValue);
            } else if (attributes.getValue("type").startsWith("video")) {
                item.setVideourl(attrValue);
            } else if (attributes.getValue("type").startsWith("audio")) {
                item.setAudiourl(attrValue);
            }
        }
    }


    @Override
    public void endElement(final String uri, final String localName,
            final String qName) throws SAXException {

        //Only once we have reached the end tag of <item/> we add it to the list.
        if (localName.equalsIgnoreCase("item") || localName.equalsIgnoreCase("entry")) {
            feed.addItem(item);
        }

        if (itemFound) {
            // "item" tag found, load the item's parameter
            switch (currentState) {
                case title:
                    item.setTitle(Html.fromHtml(tagContent.toString().trim()).toString());
                    break;
                case description:
                    item.setDescription(tagContent.toString());

                    //if thumburl not already set, scan for it in the description
                    if (item.getThumburl() == null || item.getThumburl().equals("")) {
                        String html = tagContent.toString();
                        org.jsoup.nodes.Document docHtml = Jsoup
                                .parse(html);
                        Elements imgEle = docHtml.select("img");
                        String source = imgEle.attr("src");
                        item.setThumburl(source);
                    }
                    break;
                case link:
                    item.setLink(tagContent.toString());
                    break;
                case pubdate:
                    item.setPubdate(tagContent.toString());
                    break;
                case media:
                    break;
                default:
                    break;
            }
        } else {
            // no "item" tag found, we store it's feed's parameter
            switch (currentState) {
                case title:
                    feed.setTitle(tagContent.toString());
                    break;
                case description:
                    feed.setDescription(tagContent.toString());
                    break;
                case link:
                    feed.setLink(tagContent.toString());
                    break;
                case pubdate:
                    feed.setPubdate(tagContent.toString());
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void characters(final char[] ch, final int start, final int length)
            throws SAXException {

        //Here we will receive the contents of within the XML element
        tagContent.append(ch, start, length);
    }

    public RSSFeed getFeed() {
        return feed;
    }

    //Identify the current state of our handler data handler process se we can correctly sift through the data.
    public enum State {
        unknown, title, description, link, pubdate, media

    }

}
