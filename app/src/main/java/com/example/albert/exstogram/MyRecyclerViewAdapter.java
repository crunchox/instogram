package com.example.albert.exstogram;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<ParseObject> listParseObject;
    private Context context;

    public MyRecyclerViewAdapter(Context context, List<ParseObject> listParseObject) {
        this.listParseObject = listParseObject;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ParseObject obj = listParseObject.get(position);


        if (obj.getParseFile("image").getUrl() != ""){
            Picasso.with(context).load(obj.getParseFile("image").getUrl()).into(holder.imgThumb);
        }
        holder.txtDateUploaded.setText(obj.getCreatedAt().toString());
        holder.txtCaption.setText(obj.getString("caption"));
    }

    @Override
    public int getItemCount() {
        return listParseObject.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        ImageView imgThumb;
        TextView txtDateUploaded;
        TextView txtCaption;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgThumb = (ImageView) itemView.findViewById(R.id.iv_upload);
            txtDateUploaded = (TextView) itemView.findViewById(R.id.txt_title);
            txtCaption = (TextView) itemView.findViewById(R.id.etCaption);

        }
    }
}
