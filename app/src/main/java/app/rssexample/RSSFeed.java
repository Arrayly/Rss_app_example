package app.rssexample;

import java.util.ArrayList;
import java.util.List;

//This class is used to save information about our rss feed

public class RSSFeed {

    private String title = null;

    private String description = null;

    private String link = null;

    private String pubdate = null;

    private List<RSSItem> itemList;

    RSSFeed() {
        itemList = new ArrayList<>();
    }

    void addItem(RSSItem item) {
        itemList.add(item);
    }

    public RSSItem getItem(int location) {
        return itemList.get(location);
    }

    public List<RSSItem> getList() {
        return itemList;
    }

    void setTitle(String value) {
        title = value;
    }

    void setDescription(String value) {
        description = value;
    }

    void setLink(String value) {
        link = value;
    }

    void setPubdate(String value) {
        pubdate = value;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    String getPubdate() {
        return pubdate;
    }

}
