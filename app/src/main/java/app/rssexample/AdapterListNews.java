package app.rssexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdapterListNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RSSItem> items;

    private Context ctx;

    public AdapterListNews(Context context, List<RSSItem> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView date;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            date = v.findViewById(R.id.date);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            RSSItem n = items.get(position);
            view.title.setText(n.getDescription());
            view.subtitle.setText(n.getTitle());
            view.date.setText(n.getPubdate());

            String thumbUrl = n.getThumburl();
            if (!thumbUrl.isEmpty()){
                Picasso.get()
                        .load(thumbUrl)
                        .into(view.image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
