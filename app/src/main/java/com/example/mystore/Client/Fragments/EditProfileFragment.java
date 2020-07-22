package com.example.mystore.Client.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore.Classes.Address;
import com.example.mystore.Classes.User;
import com.example.mystore.LoadingDialog;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {
    private final int CODE_IMAGE_PICKER = 10;
    private FirebaseAuth userAuth;
    private DocumentReference userRef;
    private FirebaseFirestore mDataBaseStore;
    private StorageReference mStorageRef;
    private TextView country, province, city, streetAdd, postalCode, userPhone, userCIN, userGender;
    private ImageView profileImage;
    private Address address;
    private View V;
    private User user;
    private Uri mImageUri;
    private String downloadUrl;
    private LoadingDialog loadingAnimation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Users");
        loadingAnimation = new LoadingDialog(getActivity());
        userRef = mDataBaseStore.collection("users").document(Objects.requireNonNull(userAuth.getUid()));

        city = V.findViewById(R.id.city);
        country = V.findViewById(R.id.country);
        province = V.findViewById(R.id.province);
        postalCode = V.findViewById(R.id.postal_code);
        streetAdd = V.findViewById(R.id.street_address);
        userPhone = V.findViewById(R.id.phone_number);
        userGender = V.findViewById(R.id.gender);
        userCIN = V.findViewById(R.id.CIN);
        profileImage = V.findViewById(R.id.profile_image);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                TextView t = V.findViewById(R.id.User_Name);
                t.setText(user.getName());
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, CODE_IMAGE_PICKER);
            }
        });

        V.findViewById(R.id.Save_changes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                address = addAddress();//this function will add the address to the users class before storing it in the database!
                if (mImageUri != null) {
                    uploadPicture();
                }
                //editUser(); //this function will load everything to the database;
            }
        });

        return V;
    }


    private void editUser() {
        userRef.update("address", address, "cin", userCIN.getText() + "", "gender", userGender.getText() + ""
                , "phoneNumber", Integer.valueOf(String.valueOf(userPhone.getText())))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getActivity(), "Information saved!", Toast.LENGTH_SHORT).show();
//                        ProfileFragment ProfileFragment = new ProfileFragment();
//                        getFragmentManager().beginTransaction().replace(R.id.frame_layout, ProfileFragment).
//                                commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Address addAddress() {
        address = new Address();
        address.setCity(city.getText().toString());
        address.setCountry(country.getText().toString());
        address.setProvince(province.getText().toString());
        address.setPostalCode(postalCode.getText() + "");
        address.setStreet(streetAdd.getText().toString());
        return address;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_IMAGE_PICKER && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            profileImage.setImageURI(mImageUri);
        }
    }

    private void uploadPicture() {
        loadingAnimation.startLoadingDialog();
        final StorageReference fileRef = mStorageRef.child(Objects.requireNonNull(userAuth.getUid()));
        fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getImage();
            }
        });
    }

    private void getImage() {
        mStorageRef.child(Objects.requireNonNull(userAuth.getUid())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadUrl = uri.toString();
                userRef.update("address", address
                        ,"cin", userCIN.getText() + ""
                        ,"gender", userGender.getText() + ""
                        ,"phoneNumber", Integer.valueOf(String.valueOf(userPhone.getText()))
                        ,"picture", downloadUrl)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingAnimation.dismissDialog();
                                Toast.makeText(getActivity(), "Information saved!", Toast.LENGTH_SHORT).show();
                                ProfileFragment ProfileFragment = new ProfileFragment();
                                getFragmentManager().beginTransaction().replace(R.id.frame_layout, ProfileFragment).
                                        commit();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
