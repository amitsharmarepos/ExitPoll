package com.samyotech.exitpoll.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.samyotech.exitpoll.R;
import com.samyotech.exitpoll.activity.ViewDetailsActivity;
import com.samyotech.exitpoll.dto.Album;

import java.util.Collections;
import java.util.List;


public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {
    List<Album> albumList = Collections.emptyList();

    Context context;

    public Recycler_View_Adapter(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, final int position) {
        holder.title.setText(albumList.get(position).name);
        holder.description.setText(albumList.get(position).post);
//        holder.imageView.setImageResource(albumList.get(position).img);
        ImageLoader.getInstance().displayImage("http://samyotechlabs.com/exitpoll/" + albumList.get(position).img, holder.imageView);
        holder.layout_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ViewDetailsActivity.class);

                in.putExtra("politicsid", albumList.get(position).id);
                in.putExtra("name", albumList.get(position).name);
                in.putExtra("post", albumList.get(position).post);
                in.putExtra("party", albumList.get(position).partyname);
                in.putExtra("whichpost", albumList.get(position).whichpost);
                in.putExtra("education", albumList.get(position).education);
                in.putExtra("age", albumList.get(position).age);
                in.putExtra("img", albumList.get(position).img);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }
        });
        animate(holder);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView
    public void insert(int position, Album album) {
        albumList.add(position, album);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing the Data object
    public void remove(Album album) {
        int position = albumList.indexOf(album);
        albumList.remove(position);
        notifyItemRemoved(position);
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipate_overshoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

}
