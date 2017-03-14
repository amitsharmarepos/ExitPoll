package com.samyotech.exitpoll.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.samyotech.exitpoll.R;

import de.hdodenhof.circleimageview.CircleImageView;


//The adapters View Holder
public class View_Holder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;
    TextView description;
    CircleImageView imageView;
    RelativeLayout layout_root;

    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        imageView = (CircleImageView) itemView.findViewById(R.id.imageView);
        layout_root = (RelativeLayout) itemView.findViewById(R.id.layout_root);
    }

}
