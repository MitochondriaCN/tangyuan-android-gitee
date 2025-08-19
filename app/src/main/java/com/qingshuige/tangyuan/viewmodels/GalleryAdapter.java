package com.qingshuige.tangyuan.viewmodels;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingshuige.tangyuan.R;
import com.qingshuige.tangyuan.data.DataTools;
import com.qingshuige.tangyuan.network.ApiHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final List<String> imageUrls = new ArrayList<>();
    private int squareLengthDp = 0;
    private OnItemClickListener onItemClickListener;
    Context context;

    public GalleryAdapter(Context context, List<String> imageGuids, int squareLengthDp) {
        this.context = context;
        for (String guid : imageGuids) {
            imageUrls.add(ApiHelper.getFullImageURL(guid));
        }
        this.squareLengthDp = squareLengthDp;
    }

    public GalleryAdapter(Context context, List<String> imageGuids) {
        this.context = context;
        for (String guid : imageGuids) {
            imageUrls.add(ApiHelper.getFullImageURL(guid));
        }
    }

    public GalleryAdapter(Context context, int squareLengthDp) {
        this.context = context;
        this.squareLengthDp = squareLengthDp;
    }

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_image_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (squareLengthDp != 0) {
            //将ImageView设为正方形
            holder.getImageView().getLayoutParams().width = DataTools.dpToPx(context, squareLengthDp);
            holder.getImageView().getLayoutParams().height = DataTools.dpToPx(context, squareLengthDp);
        }

        Picasso.get()
                .load(imageUrls.get(position))
                .resize(1280, 0)
                .centerInside()
                .placeholder(R.drawable.img_placeholder)
                .into(holder.getImageView(), new Callback() {
                    @Override
                    public void onSuccess() {
                        if (onItemClickListener != null) {
                            holder.getImageView().setOnClickListener(view ->
                                    onItemClickListener.onItemClick(holder.getImageView().getDrawable()));
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(imageUrls, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void addImage(String url) {
        imageUrls.add(url);
        notifyItemInserted(imageUrls.size() - 1);
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public interface OnItemClickListener {
        void onItemClick(Drawable drawable);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.navAvatarView);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
