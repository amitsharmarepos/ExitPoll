package com.samyotech.exitpoll.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.activity.DetailsActivity;
import com.samyotech.exitpoll.dto.Album;
import com.samyotech.exitpoll.dto.Favdto;
import com.samyotech.exitpoll.sharedpref.SharedPrefrence;


import java.util.List;

/**
 * Created by Varun on 30/11/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    protected ImageLoader imageLoader;
    DisplayImageOptions options;
    private SharedPrefrence preference;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        Album album;
        Favdto fav;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            preference = SharedPrefrence.getInstance(mContext);

        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.defaultimg)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        holder.album = albumList.get(position);
        holder.title.setText(holder.album.getName());
        holder.count.setText(holder.album.getPost());

        ImageLoader.getInstance().displayImage("http://samyotechlabs.com/exitpoll/" + holder.album.getImg(), holder.thumbnail, options);

        //Glide.with(mContext).load(album.getImg()).into(holder.thumbnail);




        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(mContext, DetailsActivity.class);

                in.putExtra("politicsid", holder.album.getId());
                in.putExtra("name", holder.album.getName());
                in.putExtra("post", holder.album.getPost());
                in.putExtra("party", holder.album.getPartyname());
                in.putExtra("whichpost", holder.album.getWhichpost());
                in.putExtra("education", holder.album.getEducation());
                in.putExtra("age", holder.album.getAge());
                in.putExtra("img", holder.album.getImg());

                Log.e("img", holder.album.getImg());

                mContext.startActivity(in);
            }
        });
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
