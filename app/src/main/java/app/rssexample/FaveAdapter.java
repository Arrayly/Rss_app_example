package app.rssexample;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class FaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Favourites> mFavourites;

    private Context ctx;

    private OnFaveItemClickedInterface mListener;


    public FaveAdapter(Context context, List<Favourites> favourites, OnFaveItemClickedInterface listener) {
        this.mFavourites = favourites;
        this.ctx = context;
        this.mListener = listener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public LinearLayout rootLayout;

        public TextView name;

        public TextView time;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.fave_image);
            name = v.findViewById(R.id.fave_name);
            time = v.findViewById(R.id.fave_time);
            rootLayout = v.findViewById(R.id.fave_item_rootLayout);

            rootLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int position = getAdapterPosition();
                    if (mListener!=null && position!= RecyclerView.NO_POSITION){
                        mListener.onClick(position);
                    }
                }
            });

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fave_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            Favourites fave = mFavourites.get(position);
            view.name.setText(fave.getName());
            view.time.setText("Added on" + fave.getTimeStamp().toString());

            Picasso.get()
                    .load(fave.getImageUrl())
                    .into(view.image);
        }
    }

    @Override
    public int getItemCount() {
        return mFavourites.size();
    }


    public interface OnFaveItemClickedInterface {

        void onClick(int position);
    }
}
