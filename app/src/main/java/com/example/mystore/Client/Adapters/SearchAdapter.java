package com.example.mystore.Client.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Classes.Item;
import com.example.mystore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<Item> itemArrayList;
    private OnItemClickListener mListener;

    public SearchAdapter(Context context,ArrayList<Item> itemArrayList){
        this.context=context;
        this.itemArrayList=itemArrayList;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Item model = itemArrayList.get(position);
        holder.item_name.setText(model.getTitle());
        holder.item_price.setText(model.getPrice().toString());
        Picasso.get().
                load(Uri.parse(model.getPictures().get(0))).
                fit().
                centerCrop().
                into(holder.item_image);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView item_name,item_price;
        ImageView item_image;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.Item_Name);
            item_price = itemView.findViewById(R.id.Item_Price);
            item_image = itemView.findViewById(R.id.Item_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
