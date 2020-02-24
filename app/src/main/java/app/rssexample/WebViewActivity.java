package app.rssexample;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {

    private static final String COLLECTIONS_PATH = "Articles";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference mCollectionReference = db.collection(COLLECTIONS_PATH);


    private static final String WEB_TITLE = "title";

    private static final String WEB_IMAGE_URL = "image";

    private static final String WEB_ARTICLE_URL = "article url";

    private WebView mWebView;

    private String articleTitle;

    private String articleUrl;

    private String imageUrl;

    public ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mProgressBar = findViewById(R.id.loadingProgressBar_webView);

        Intent intent = getIntent();

        articleTitle = intent.getStringExtra(WEB_TITLE);
        imageUrl = intent.getStringExtra(WEB_IMAGE_URL);
        articleUrl = intent.getStringExtra(WEB_ARTICLE_URL);

        mWebView = findViewById(R.id.webview_container);

        mWebView.setWebViewClient(new AppWebViewClients(mProgressBar));

        mWebView.loadUrl(articleUrl);

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.webview_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WebView");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_addFavourites) {
            saveArticleToDb();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveArticleToDb() {
//        List<Favourites> faveList = new ArrayList<>();
        Favourites faveItem = new Favourites(articleTitle, articleUrl, null, imageUrl,null);
//        faveList.add(faveItem);

        mCollectionReference.add(faveItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                Toast.makeText(WebViewActivity.this, "Successfully added to favourites!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                Toast.makeText(WebViewActivity.this, "Could not add to favourites", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });


    }

    @Override
    public void onBackPressed() {
        //overide backpressed so that the app doesnt close
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }


    }

    class AppWebViewClients extends WebViewClient{
        private ProgressBar mProgressBar;

        public AppWebViewClients(final ProgressBar progressBar) {
            mProgressBar = progressBar;
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
