package com.example.mystore.Client.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Client.Adapters.RecyclerViewAdapter;
import com.example.mystore.Classes.Item;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FavoritesFragment extends Fragment {
    private FirebaseFirestore mDataBaseStore ;
    private CollectionReference itemsRef;
    private FirebaseAuth userAuth;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_home,container,false);
        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        itemsRef = mDataBaseStore.collection("users")
                .document(Objects.requireNonNull(userAuth.getUid())).collection("WishList");

        recyclerView = V.findViewById(R.id.recycler_view);
        GridLayoutManager gridVM = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridVM);

        setUpRecyclerView();
        return V;
    }
    private void setUpRecyclerView() {
        Query query = itemsRef.orderBy("id",Query.Direction.DESCENDING);
        final FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query,Item.class)
                .build();
        adapter = new RecyclerViewAdapter(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item",options.getSnapshots().get(position));
                ItemFragment itemFragment = new ItemFragment();
                itemFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout,itemFragment)
                        .addToBackStack(null).
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
