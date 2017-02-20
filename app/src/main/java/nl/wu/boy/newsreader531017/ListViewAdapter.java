package nl.wu.boy.newsreader531017;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boy.newsreader531017.R;

import java.net.URL;
import java.util.List;

/**
 * Created by Boy on 9-10-2016.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private List<Article> mItems;
    private LayoutInflater mInflater;
    private ListEventListener listener;
    private int position;

    public ListViewAdapter(Context context, List<Article> result, ListEventListener listEventListener) {
        mInflater = LayoutInflater.from(context);
        mItems = result;
        listener = listEventListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item, parent, false));
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.position = position;
        Article item =  getItem(position);

        holder.image.setImageBitmap(urlToBMP(item.Image));
        holder.title.setText(item.Title);

        if(item.IsLiked == true)
            holder.like.setVisibility(View.VISIBLE);
        else
            holder.like.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToDetail(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Article getItem(int position) { return mItems.get(position); }

    public void editItem(int position, Article article) {
        mItems.set(position, article);
        notifyDataSetChanged();
    }

    public void addItems(List<Article> articles) {
        mItems.addAll(articles);
        Log.e("count", String.valueOf(mItems.size()));
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return position + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public ImageView like;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            title = (TextView) itemView.findViewById(R.id.item_title);
            like = (ImageView) itemView.findViewById(R.id.item_like);
        }
    }

    public Bitmap urlToBMP(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(src);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;
        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
