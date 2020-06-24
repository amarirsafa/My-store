package com.example.mystore.Client.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Client.Adapters.RecyclerViewAdapter;
import com.example.mystore.Client.Classes.Item;
import com.example.mystore.UnfinishedWork.ItemDetailsActivity;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

public class HomeFragment extends Fragment {
    private FirebaseFirestore mDataBaseStore ;
    private CollectionReference itemsRef ;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Item> itemsList,searchedItems;
    private EditText searchBar;
    private String stringToSearch;
    //private ImageView searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_home,container,false);
        mDataBaseStore = FirebaseFirestore.getInstance();
        itemsRef = mDataBaseStore.collection("Products");

        recyclerView = V.findViewById(R.id.recycler_view);
        GridLayoutManager gridVM = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridVM);

        itemsList = new ArrayList<>();
        searchedItems = new ArrayList<>();
        searchBar = V.findViewById(R.id.theBar);
        stringToSearch = searchBar.toString();
        V.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fillInSearchedItems();
                //setUpRecyclerView();
            }
        });
        setUpRecyclerView();
        return V;
    }

    private void fillInSearchedItems() {
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(getActivity(), "Error Loading", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot == null){
                        Toast.makeText(getActivity(), "No items to load", Toast.LENGTH_SHORT).show();
                    }else{
                        Item item_1 = documentSnapshot.toObject(Item.class);

                        if(stringToSearch != null &&  item_1.getTitle().contains(stringToSearch)){
                            searchedItems.add(item_1);
                        }
                    }
                }
            }
        });
    }

    private void fillInListOfItems() {
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(getActivity(), "Error Loading", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot == null){
                        Toast.makeText(getActivity(), "No items to load", Toast.LENGTH_SHORT).show();
                    }else{
                        Item item_1 = documentSnapshot.toObject(Item.class);
                        itemsList.add(new Item(item_1));
                    }
                }
            }
        });
    }


    private void setUpRecyclerView() {
        Query query = itemsRef.orderBy("id",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query,Item.class)
                .build();
        adapter = new RecyclerViewAdapter(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item",itemsList.get(position));
                ItemFragment itemFragment = new ItemFragment();
                itemFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout,itemFragment).
                        commit();
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
