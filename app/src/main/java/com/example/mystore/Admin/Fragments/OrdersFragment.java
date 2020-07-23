package com.example.mystore.Admin.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystore.Admin.Adapters.RecyclerViewAdapter_Orders;
import com.example.mystore.Classes.Item;
import com.example.mystore.Classes.Order;
import com.example.mystore.Client.Adapters.RecyclerViewAdapter_Cart;
import com.example.mystore.Client.Fragments.ItemFragment;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


public class OrdersFragment extends Fragment {

    private FirebaseFirestore mDataBaseStore;
    private CollectionReference orders;
    private FirebaseAuth userAuth;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter_Orders adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_orders, container, false);
        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        orders = mDataBaseStore.collection("users").document(Objects.requireNonNull(userAuth.getUid()))
                .collection("orders");

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