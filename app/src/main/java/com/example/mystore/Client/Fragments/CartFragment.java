package com.example.mystore.Client.Fragments;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Admin.Activities.UploadItemsActivity;
import com.example.mystore.Classes.Order;
import com.example.mystore.Classes.User;
import com.example.mystore.Client.Adapters.RecyclerViewAdapter_Cart;
import com.example.mystore.Classes.Item;
import com.example.mystore.ExampleDialog;
import com.example.mystore.LoadingDialog;
import com.example.mystore.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment {
    private FirebaseFirestore mDataBaseStore;
    private CollectionReference itemsRef, adminRef;
    private FirebaseAuth userAuth;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter_Cart adapter;
    private ArrayList<Item> itemsToCheckOut;
    private LoadingDialog loadingAnimation;
    private User currentUser;
    private String orderId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_cart, container, false);
        loadingAnimation = new LoadingDialog(getActivity());
        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        adminRef = mDataBaseStore.collection("users");

        itemsToCheckOut = new ArrayList<>();
        itemsRef = mDataBaseStore.collection("users")
                .document(Objects.requireNonNull(userAuth.getUid())).collection("Cart");

        recyclerView = V.findViewById(R.id.recycler_view_cart);
        LinearLayoutManager LLM = new LinearLayoutManager(getActivity());
        LLM.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LLM);
        V.findViewById(R.id.empty_cart_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBaseStore.collection("users")
                        .document(Objects.requireNonNull(userAuth.getUid())).collection("Cart")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        }
                    }
                });
            }
        });
        V.findViewById(R.id.checkout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBaseStore.collection("users").document(Objects.requireNonNull(userAuth.getUid()))
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            currentUser = documentSnapshot.toObject(User.class);
                            if (currentUser.getAddress().getStreet() == null || currentUser.getAddress().getCity() == null
                                    || currentUser.getAddress().getCountry() == null || currentUser.getAddress().getPostalCode() == null
                                    || currentUser.getAddress().getProvince() == null || currentUser.getCIN() == null) {
                                openDialog();
                            } else {
                                loadingAnimation.startLoadingDialog();
                                itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                        for (final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            final Item item_1 = documentSnapshot.toObject(Item.class);
                                            adminRef.document(item_1.getSellerId()).get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(final DocumentSnapshot documentSnapshot) {
                                                            final Order order = new Order();
                                                            order.setItemToOrder(item_1);
                                                            order.setUser(currentUser);
                                                            adminRef.document(item_1.getSellerId()).collection("orders")
                                                                    .add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    order.setOrderId(documentReference.getId());
                                                                    documentReference.update("orderId",order.getOrderId());
                                                                    loadingAnimation.dismissDialog();
                                                                    Toast.makeText(getActivity(), "Oder Complete", Toast.LENGTH_SHORT).show();

                                                                }
                                                            });
                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        setUpRecyclerView();
        return V;
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getFragmentManager(), "Error");
    }

    private void setUpRecyclerView() {
        Query query = itemsRef.orderBy("id", Query.Direction.DESCENDING);
        final FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        adapter = new RecyclerViewAdapter_Cart(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter_Cart.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", options.getSnapshots().get(position));
                ItemFragment itemFragment = new ItemFragment();
                itemFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, itemFragment).
                        commit();
            }

            @Override
            public void onDeleteClick(DocumentSnapshot documentSnapshot, int position) {
                itemsRef.document(String.valueOf(options.getSnapshots().get(position).getId())).delete();
            }

            @Override
            public void onSaveClick(DocumentSnapshot documentSnapshot, int position) {
                mDataBaseStore.collection("users")
                        .document(Objects.requireNonNull(userAuth.getUid()))
                        .collection("WishList").add(options.getSnapshots().get(position));
                itemsRef.document(String.valueOf(options.getSnapshots().get(position).getId())).delete();
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
