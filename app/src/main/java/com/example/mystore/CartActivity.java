package com.example.mystore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private FirebaseFirestore mDataBaseStore ;
    private CollectionReference itemsRef;
    private FirebaseAuth userAuth;
    private RecyclerView recyclerView;
    private RacyclerViewAdapter_Cart adapter;
    private List<Item> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        itemsRef = mDataBaseStore.collection("users")
                .document(Objects.requireNonNull(userAuth.getUid())).collection("Cart");

        recyclerView = findViewById(R.id.recycler_view_cart);
        LinearLayoutManager LLM = new LinearLayoutManager(this);
        LLM.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LLM);

        itemsList = new ArrayList<>();

        fillInTheListOfItems();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = itemsRef.orderBy("id",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query,Item.class)
                .build();
        adapter = new RacyclerViewAdapter_Cart(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RacyclerViewAdapter_Cart.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(CartActivity.this,ItemDetailsActivity.class);
                intent.putExtra("Item",itemsList.get(position));
                startActivity(intent);
            }
        });
    }

    private void fillInTheListOfItems() {
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(CartActivity.this, "Error Loading", Toast.LENGTH_SHORT).show();
                    return;
                }
                assert queryDocumentSnapshots != null;
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot == null){
                        Toast.makeText(CartActivity.this, "No items to load", Toast.LENGTH_SHORT).show();
                    }else{
                        Item item_1 = documentSnapshot.toObject(Item.class);
                        itemsList.add(new Item(item_1));
                    }
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
