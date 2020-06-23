package com.example.mystore.Client.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mystore.Client.Adapters.ViewPagerAdapter;
import com.example.mystore.Client.Classes.Item;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ItemFragment extends Fragment {
    private int count =0;
    private ViewPager viewPager;
    private TextView item_title, item_price, item_description,amount_display;
    private Button Add_to_cart,Add_to_wishlist;
    private CollectionReference userRef;
    private FirebaseAuth userAuth;
    private ArrayList<String> pictures = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_item,container,false);
        userAuth = FirebaseAuth.getInstance();
        userRef = FirebaseFirestore.getInstance().collection("users");


        Add_to_cart = V.findViewById(R.id.add_to_cart_button);
        Add_to_wishlist = V.findViewById(R.id.add_to_wishlist_buttton);

        viewPager = V.findViewById(R.id.image_gallery);
        item_title = V.findViewById(R.id.product_name);
        item_price = V.findViewById(R.id.product_price);
        item_description = V.findViewById(R.id.product_description);
        amount_display = V.findViewById(R.id.amount);


        Bundle bundle = this.getArguments();
        final Item item = bundle.getParcelable("item");

        V.findViewById(R.id.plus_button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                assert item != null;
                item.setAmount(++count);
                amount_display.setText(count+"");
            }
        });
        V.findViewById(R.id.minus_button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(count > 0){
                    assert item != null;
                    item.setAmount(--count);
                    amount_display.setText(count+"");
                }
            }
        });

        assert item != null;
        @SuppressLint("CutPasteId") ViewPager viewPager = V.findViewById(R.id.image_gallery);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), item.getPictures());
        viewPager.setAdapter(adapter);
        //Picasso.get().load(item.getPictures().get(0)).fit().centerCrop().into(imageView);
        item_title.setText(item.getTitle());
        item_price.setText(item.getPrice().toString());
        String item_description2 = item.getDescription() + "\n Item category: " + item.getCategory() +
                "\n this item was made by: " + item.getWho_ma_it() + "\n and it's :" + item.getWhat_is_it();
        item_description.setText(item_description2);


        Add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getAmount() != 0){
                    userRef.document(Objects.requireNonNull(userAuth.getUid())).collection("Cart")
                            .document(item.getId() + "")
                            .set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "The item added to cart", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Select a quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.document(Objects.requireNonNull(userAuth.getUid())).collection("WishList")
                        .document(item.getId()+"")
                        .set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Item added to WishList", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Couldn't load item to WishList", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return V;
    }

}

