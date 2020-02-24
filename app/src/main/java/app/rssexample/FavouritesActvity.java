package app.rssexample;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import app.rssexample.FaveAdapter.OnFaveItemClickedInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FavouritesActvity extends AppCompatActivity implements OnFaveItemClickedInterface {

    private static final String COLLECTIONS_PATH = "Articles";

    private static final String WEB_ARTICLE_URL = "article url";

    private List<Favourites> mFavourites = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private FaveAdapter listAdapter;

    private ListenerRegistration mListenerRegistration;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference mCollectionReference = db.collection(COLLECTIONS_PATH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_actvity);

        initToolbar();

        mRecyclerView = findViewById(R.id.fave_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(new SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull final RecyclerView recyclerView, @NonNull final ViewHolder viewHolder,
                    @NonNull final ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final ViewHolder viewHolder, final int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteItem(position);

            }
        }).attachToRecyclerView(mRecyclerView);

    }

    private void deleteItem(final int position) {
        String articleId = mFavourites.get(position).getDocumentId();
        mCollectionReference.document(articleId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(final Void aVoid) {
                        Toast.makeText(FavouritesActvity.this, "Article successfully deleted!", Toast.LENGTH_SHORT)
                                .show();

                    }
                });

    }

    @Override
    public void onClick(final int position) {
        String itemUrl = mFavourites.get(position).getUrl();
        Intent intent = new Intent(FavouritesActvity.this, WebViewActivity.class);
        intent.putExtra(WEB_ARTICLE_URL, itemUrl);
        startActivity(intent);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.fave_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_80));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Favourites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mListenerRegistration = mCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable final QuerySnapshot queryDocumentSnapshots,
                    @Nullable final FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(FavouritesActvity.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } else if (queryDocumentSnapshots != null) {
                    getDataFromSnapshot(queryDocumentSnapshots);
                }
            }
        });
    }

    private void getDataFromSnapshot(final QuerySnapshot queryDocumentSnapshots) {
        if (!queryDocumentSnapshots.isEmpty()) {
            mFavourites.clear();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                mFavourites.add(snapshot.toObject(Favourites.class));
            }

            listAdapter = new FaveAdapter(this, mFavourites, this);
            mRecyclerView.setAdapter(listAdapter);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mListenerRegistration.remove();
    }
}
