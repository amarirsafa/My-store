package com.example.mystore.Admin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Admin.Adapters.RecyclerViewAdapter_CheckedOrders;
import com.example.mystore.Admin.Adapters.RecyclerViewAdapter_Orders;
import com.example.mystore.Classes.Order;
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

public class CheckedOrdersFragment extends Fragment {
    private FirebaseFirestore mDataBaseStore;
    private CollectionReference checkedOrders;
    private FirebaseAuth userAuth;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter_CheckedOrders adapter;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_checked_orders, container, false);
        loadingDialog = new LoadingDialog(getActivity());
        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
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
        Query query = checkedOrders.orderBy("randomNum",Query.Direction.DESCENDING);
        final FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        adapter = new RecyclerViewAdapter_CheckedOrders(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter_CheckedOrders.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                loadingDialog.startLoadingDialog();
                Order order = documentSnapshot.toObject(Order.class);
                assert order != null;
                checkedOrders.document(order.getOrderId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "the order is deleted!", Toast.LENGTH_SHORT).show();
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
