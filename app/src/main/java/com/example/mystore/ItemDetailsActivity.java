package com.example.mystore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemDetailsActivity extends AppCompatActivity {
    private Item item;
    private ImageView imageView;
    private TextView item_title,item_price,item_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        imageView = findViewById(R.id.image_gallery);
        item_title = findViewById(R.id.product_name);
        item_price = findViewById(R.id.product_price);
        item_description = findViewById(R.id.product_description);


        Intent intent = getIntent();
        Item item = intent.getParcelableExtra("Item");

        //imageView.setImageURI(Uri.parse(item.getPictures().get(0)));
        Picasso.get().load(item.getPictures().get(0)).fit().centerCrop().into(imageView);
        item_title.setText(item.getTitle());
        item_price.setText(item.getPrice().toString());
        String item_description2 = item.getDescription()+"\n Item category: \n"+item.getCategory()+
                "\n this item was made by: "+ item.getWho_ma_it()+"\n and it's :"+item.getWhat_is_it();
        item_description.setText(item_description2);

    }
}
