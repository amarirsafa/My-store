package com.example.mystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ItemDetailsActivity extends AppCompatActivity {
    private Item item;
    private ImageView imageView;
    private TextView item_title, item_price, item_description;
    private TextView amount_display;
    private FirebaseAuth userAuth;
    private Button Add_to_cart,Add_to_wishlist;
    private int count =0;
    private CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        userAuth = FirebaseAuth.getInstance();
        userRef = FirebaseFirestore.getInstance().collection("users");


        Add_to_cart = findViewById(R.id.add_to_cart_button);
        Add_to_wishlist = findViewById(R.id.add_to_wishlist_buttton);

        imageView = findViewById(R.id.image_gallery);
        item_title = findViewById(R.id.product_name);
        item_price = findViewById(R.id.product_price);
        item_description = findViewById(R.id.product_description);
        amount_display = findViewById(R.id.amount);


        Intent intent = getIntent();
        final Item item = intent.getParcelableExtra("Item");

        findViewById(R.id.plus_button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                assert item != null;
                item.setAmount(++count);
                amount_display.setText(count+"");
            }
        });
        findViewById(R.id.minus_button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(count > 0){
                    assert item != null;
                    item.setAmount(--count);
                    amount_display.setText(""+count);
                }
            }
        });


        //imageView.setImageURI(Uri.parse(item.getPictures().get(0)));
        Picasso.get().load(item.getPictures().get(0)).fit().centerCrop().into(imageView);
        item_title.setText(item.getTitle());
        item_price.setText(item.getPrice().toString());
        String item_description2 = item.getDescription() + "\n Item category: " + item.getCategory() +
                "\n this item was made by: " + item.getWho_ma_it() + "\n and it's :" + item.getWhat_is_it();
        item_description.setText(item_description2);

        Add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.document(Objects.requireNonNull(userAuth.getUid())).collection("Cart")
                        .document(item.getId() + "")
                        .set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ItemDetailsActivity.this, "The item added to cart", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ItemDetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
                ;

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
                        Toast.makeText(ItemDetailsActivity.this, "Item added to WishList", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ItemDetailsActivity.this, "Couldn't load item to WishList", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


}
