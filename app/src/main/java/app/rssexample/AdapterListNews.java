package app.rssexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdapterListNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RSSItem> items;

    private Context ctx;

    private OnItemClickedInterface mListener;



    public AdapterListNews(Context context, List<RSSItem> items, OnItemClickedInterface listener) {
        this.items = items;
        this.ctx = context;
        this.mListener = listener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView date;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.fave_image);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            date = v.findViewById(R.id.date);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int position = getAdapterPosition();
                    if (mListener!=null && position!= RecyclerView.NO_POSITION ){
                        mListener.onClick(position);
                    }
                }
            });

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


    public interface OnItemClickedInterface{
        void onClick(int position);
    }

}
