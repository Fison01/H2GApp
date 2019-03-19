package com.example.qtfison.h2gapp.uploading;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qtfison.h2gapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private Context context;
    public List<Upload> uploads;

    public ImageAdapter(Context context, List<Upload> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.image_item,viewGroup,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        windowmanager.getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        Upload uploadCurrent=uploads.get(i);
        imageViewHolder.textViewName.setText(uploadCurrent.getmName());
        Picasso.with(context)
                .load(uploadCurrent.getmImageUri())
                .resize(width-50,height)
                .centerInside()
                .into(imageViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.text_view_name);
            imageView=itemView.findViewById(R.id.image_view_upload);

        }
    }
}
