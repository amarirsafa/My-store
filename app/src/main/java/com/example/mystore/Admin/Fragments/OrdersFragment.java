package com.example.mystore.Admin.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mystore.Admin.Adapters.RecyclerViewAdapter_Orders;
import com.example.mystore.Classes.Item;
import com.example.mystore.Classes.Order;
import com.example.mystore.Client.Adapters.RecyclerViewAdapter_Cart;
import com.example.mystore.Client.Fragments.ItemFragment;
import com.example.mystore.LoadingDialog;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


public class OrdersFragment extends Fragment {

    private FirebaseFirestore mDataBaseStore;
    private CollectionReference orders,checkedOrders;
    private FirebaseAuth userAuth;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter_Orders adapter;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_orders, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        orders = mDataBaseStore.collection("users").document(Objects.requireNonNull(userAuth.getUid()))
                .collection("orders");
        checkedOrders =mDataBaseStore.collection("users").document(Objects.requireNonNull(userAuth.getUid()))
                .collection("checkedOrders");

        recyclerView = V.findViewById(R.id.recycler_view_orders);
        LinearLayoutManager LLM = new LinearLayoutManager(getActivity());
        LLM.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LLM);

        setUpRecyclerView();
        return V;
    }
    private void setUpRecyclerView() {
        Query query = orders.orderBy("randomNum",Query.Direction.DESCENDING);
        final FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        adapter = new RecyclerViewAdapter_Orders(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter_Orders.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                loadingDialog.startLoadingDialog();
                Order order = documentSnapshot.toObject(Order.class);
                checkedOrders.document(order.getOrderId()).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "the order is processed!", Toast.LENGTH_SHORT).show();
                    }
                });
                orders.document(order.getOrderId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingDialog.dismissDialog();
                    }
                });
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}