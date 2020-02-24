package app.rssexample;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.rssexample.AdapterListNews.OnItemClickedInterface;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class MainActivity extends AppCompatActivity implements OnItemClickedInterface {

    private static final String TAG = "MainActivity";

    private static final String WEB_TITLE = "title";
    private static final String WEB_IMAGE_URL = "image";
    private static final String WEB_ARTICLE_URL = "article url";

    private RSSFeed rssFeed = null;

    private List<RSSItem> postsList = new ArrayList<>();

    private String url = "https://www.wired.com/feed";

    private RecyclerView mRecyclerView;

    private AdapterListNews listAdapter;

    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        mRecyclerView = findViewById(R.id.list);
        mProgressBar = findViewById(R.id.loadingProgressBar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onResume() {
        super.onResume();
        getRssItems();
    }

    @Override
    public void onClick(final int position) {
        Intent intent = new Intent(MainActivity.this,WebViewActivity.class);

        intent.putExtra(WEB_TITLE,postsList.get(position).getTitle());
        intent.putExtra(WEB_IMAGE_URL,postsList.get(position).getThumburl());
        intent.putExtra(WEB_ARTICLE_URL,postsList.get(position).getLink());
        startActivity(intent);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("RSS Example");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getRssItems() {
        new RssTask().execute();
    }


    private class RssTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (postsList != null) {
                postsList.clear();
            }
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(final Void... voids) {

            try {

                //Create a code representation of the URL String
                URL rssUrl = new URL(url);

                //Init factory API: Will enable us to configure and
                //obtain a SAX based parser to parse XML documents.
                SAXParserFactory mySAXParserFactory = SAXParserFactory.newInstance();

                //Obtain SAX parser (Simple API for XML)
                SAXParser mySAXParser = mySAXParserFactory.newSAXParser();

                //Get an XML reader interface
                XMLReader myXMLReader = mySAXParser.getXMLReader();

                //This class will handle and process the XML document
                RSSHandler myRSSHandler = new RSSHandler();

                //Register our handler class
                myXMLReader.setContentHandler(myRSSHandler);

                //Open a connection to the URL using openStream()
                //Create a byte Stream object, representation of the XML
                InputSource myInputSource = new InputSource(rssUrl.openStream());

                //Parse the byte stream
                myXMLReader.parse(myInputSource);

                //Get our feed from the handler class.
                rssFeed = myRSSHandler.getFeed();

            } catch (ParserConfigurationException | IOException | SAXException e) {
                Log.d(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);

            mProgressBar.setVisibility(View.GONE);

            if (rssFeed != null && rssFeed.getList().size() > 0) {
                postsList.addAll(rssFeed.getList());

                //Once the task has finished, load our recycler view
                listAdapter = new AdapterListNews(MainActivity.this, postsList,MainActivity.this); mRecyclerView.setAdapter(listAdapter);

                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getRssItems();
        }else if (item.getItemId() == R.id.action_favourites){
            startActivity(new Intent(MainActivity.this,FavouritesActvity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
