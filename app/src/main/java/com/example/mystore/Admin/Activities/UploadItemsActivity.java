package com.example.mystore.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystore.Client.Classes.Item;
import com.example.mystore.LoadingDialog;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class UploadItemsActivity extends AppCompatActivity {
    private final int CODE_MULTI_IMG_GALLARY=10;
    private FirebaseAuth fAuth;
    private String what_is_it,who_made_it;
    private Item item;
    private ArrayList<ImageView> imgs = new ArrayList<>();
    private ArrayList<Uri> productImages = new ArrayList<>();
    private ArrayList<String> productImagesString = new ArrayList<>();
    private FirebaseFirestore mDataBaseStore;
    private StorageReference mStorageRef;
    private EditText title;
    private LoadingDialog loadingAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_items);
        fAuth = FirebaseAuth.getInstance();
        loadingAnimation  = new LoadingDialog(UploadItemsActivity.this);

        mDataBaseStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Items");

        imgs.add((ImageView) findViewById(R.id.img1));
        imgs.add((ImageView) findViewById(R.id.img2));
        imgs.add((ImageView) findViewById(R.id.img3));
        imgs.add((ImageView) findViewById(R.id.img4));
        imgs.add((ImageView) findViewById(R.id.img5));

        title = findViewById(R.id.item_title);

        item = new Item();
        item.setId((int) System.currentTimeMillis());

        Spinner spinner1 = findViewById(R.id.what_is_it_spinner);
        Spinner spinner2 = findViewById(R.id.who_made_it_spinner);

        final ArrayAdapter<CharSequence> myadaptor1 = ArrayAdapter.createFromResource(this,
                R.array.what_is_it,android.R.layout.simple_list_item_1);
        myadaptor1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(myadaptor1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        what_is_it="";
                        break;
                    case 1:
                        what_is_it="Finished product";
                        break;
                    case 2:
                        what_is_it="supplies";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                what_is_it="The seller did not specify";
            }
        });

        final ArrayAdapter<CharSequence> myadaptor2 = ArrayAdapter.createFromResource(this,
                R.array.who_made_it,android.R.layout.simple_list_item_1);
        myadaptor2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(myadaptor2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        who_made_it = "";
                        break;
                    case 1 :
                        who_made_it = "I made it";
                        break;
                    case 2 :
                        who_made_it = "Member of my shop";
                        break;
                    case 3 :
                        who_made_it = "An other company";
                        break;
                    case 4 :
                        who_made_it = "My cooperative";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                who_made_it = "The seller did not specify";
            }
        });




        findViewById(R.id.add_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,CODE_MULTI_IMG_GALLARY);
            }
        });

        findViewById(R.id.save_item_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingAnimation.startLoadingDialog();
                fillTheItemForm();


            }
        });
    }

    private void uploadImages() {
        String id = item.getId()+"";
        final StorageReference fileRef = mStorageRef.child(id);
        for(int i=0;i<productImages.size();i++){
            final int finalI = i;
            final int finalI1 = productImages.size()-1;
            fileRef.child("Image number_"+i+".jpg").putFile(productImages.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    productImagesString.add(downloadUrl+"");
                        if(finalI == finalI1){
                            uploadItem();
                        }
                    }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadItemsActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fillTheItemForm() {
        TextView titleTextView = findViewById(R.id.item_title);
        TextView categoryTextView = findViewById(R.id.item_category);
        TextView descriptionTextView = findViewById(R.id.item_description);
        EditText priceEditText = findViewById(R.id.item_price);
        EditText quantityEditText = findViewById(R.id.item_quantity);

        String title = titleTextView.getText().toString();
        String category = categoryTextView.getText().toString();
        String description = descriptionTextView.getText().toString();
        String price1 = priceEditText.getText().toString();
        String quantity1 = quantityEditText.getText().toString();

        item.setSellerId(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        if(title.isEmpty()){
            titleTextView.setError("Enter your product title");
        }else{
            item.setTitle(title);
        }
        if(category.isEmpty()){
            categoryTextView.setError("Enter category");
        }else{
            item.setCategory(category);
        }
        if(description.isEmpty()){
            descriptionTextView.setError("Add your item description");
        }else{
            item.setDescription(description);
        }
        if(price1.isEmpty()){
            priceEditText.setError("Set a price");
        }else{
            Double price = Double.parseDouble(priceEditText.getText().toString());
            item.setPrice(price);
        }
        if(quantity1.isEmpty()){
            quantityEditText.setError("set a quantity");
        }else{
            Double quantity = Double.parseDouble(quantity1);
            item.setQuantity(quantity);

        }
        if(what_is_it == "" || who_made_it == "" || productImages.isEmpty()){
            if(productImages.isEmpty()){
                Toast.makeText(UploadItemsActivity.this, "Upload images", Toast.LENGTH_SHORT).show();
            }else if(what_is_it == ""){
                Toast.makeText(UploadItemsActivity.this, "select what is it", Toast.LENGTH_SHORT).show();
            }else if(who_made_it == ""){
                Toast.makeText(UploadItemsActivity.this, "select who made it", Toast.LENGTH_SHORT).show();
            }
        }else{
            item.setPictures(productImagesString);
            item.setWhat_is_it(what_is_it);
            item.setWho_ma_it(who_made_it);
        }
        uploadImages();
    }

    private void uploadItem() {
        String id = item.getId()+"";
        DocumentReference dRef = mDataBaseStore.collection("Products").document(id);
        DocumentReference storeRef = mDataBaseStore.collection("users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
                .collection("store").document(id);
        storeRef.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UploadItemsActivity.this, "Item added to store", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadItemsActivity.this, "Item not added something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        dRef.set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadingAnimation.dismissDialog();
                Toast.makeText(UploadItemsActivity.this, "The item has been successfully saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadItemsActivity.this, "Something went wrong!ITEM NOT SAVED", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_MULTI_IMG_GALLARY) {
            assert data != null;
            if(data != null){
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item_11 = clipData.getItemAt(i);
                        imgs.get(i).setImageURI(item_11.getUri());
                        productImages.add(item_11.getUri());
                    }
                }
            }else{
                Toast.makeText(this, "Select at least two images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
