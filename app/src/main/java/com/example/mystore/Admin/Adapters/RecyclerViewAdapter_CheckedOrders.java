package com.example.mystore.Admin.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Classes.Order;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecyclerViewAdapter_CheckedOrders extends FirestoreRecyclerAdapter<Order,RecyclerViewAdapter_CheckedOrders.myViewHolder> {

    public RecyclerViewAdapter_CheckedOrders.OnItemClickListener listener;
    public RecyclerViewAdapter_CheckedOrders(@NonNull FirestoreRecyclerOptions<Order> options) { super(options); }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull RecyclerViewAdapter_CheckedOrders.myViewHolder holder, int position, @NonNull Order model) {
        holder.orderID.setText(model.getItemToOrder().getId()+"");
        holder.orderDate.setText(model.getCurrentDate()+"");
        holder.itemTitle.setText(model.getItemToOrder().getTitle()+"");
        holder.orderQty.setText(model.getItemToOrder().getAmount()+"");
        holder.orderAddress.setText(model.getUser().getAddress().toString());
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_CheckedOrders.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_checked,parent,false);
        return new RecyclerViewAdapter_CheckedOrders.myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView orderID,itemTitle,orderAddress,orderQty,orderDate;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.oder_date);
            itemTitle = itemView.findViewById(R.id.item_title);
            orderQty = itemView.findViewById(R.id.amount);
            orderAddress = itemView.findViewById(R.id.oder_address);
            itemView.findViewById(R.id.delete_order).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(RecyclerViewAdapter_CheckedOrders.OnItemClickListener listener){
        this.listener=listener;
    }
}
