package app.rssexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class TouchHelper extends ItemTouchHelper {

    public TouchHelper(@NonNull final Callback callback) {
        super(callback);
    }

    @Override
    public void attachToRecyclerView(@Nullable final RecyclerView recyclerView) {
        super.attachToRecyclerView(recyclerView);
    }
}
