package com.example.mystore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private FirebaseFirestore mDataBaseStore ;
    private StorageReference mStorageRef;
    private CollectionReference itemsRef ;
    private RecyclerView recyclerView;
    private List<Item> itemsReady;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        mDataBaseStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Items");
        itemsRef = mDataBaseStore.collection("Products");
        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridVM = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridVM);
        itemsReady = new ArrayList<>();
        ImageView imgtest = findViewById(R.id.image_holder);





        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(ItemsActivity.this, "Error Loading", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot == null){
                        Toast.makeText(ItemsActivity.this, "No items to load", Toast.LENGTH_SHORT).show();
                    }else{
                        Item item_1 = documentSnapshot.toObject(Item.class);
                        itemsReady.add(new Item(item_1));
                    }
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(ItemsActivity.this,itemsReady);
                recyclerView.setAdapter(adapter);
            }
        });


    }



}
