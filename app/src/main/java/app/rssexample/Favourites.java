package app.rssexample;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;
import java.io.Serializable;
import java.util.Date;

public class Favourites implements Serializable {

    @DocumentId
    private String documentId;

    private String name;

    private String url;

    @ServerTimestamp
    private Date timeStamp;

    private String imageUrl;

    public Favourites() {
    }

    public Favourites(final String name, final String url, final Date timeStamp, final String imageUrl, final String documentId) {
        this.name = name;
        this.url = url;
        this.timeStamp = timeStamp;
        this.imageUrl = imageUrl;
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final String documentId) {
        this.documentId = documentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(final Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
