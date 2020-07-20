package com.example.mystore.Client.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mystore.Client.Activities.LoginActivity;
import com.example.mystore.Classes.User;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

import static com.example.mystore.R.id.profile_image;

public class ProfileFragment extends Fragment {
    private View V;
    private FirebaseAuth userAuth;
    private DocumentReference userRef;
    private FirebaseFirestore mDataBaseStore ;
    private TextView address,userPhone,userGender,userName,userEmail;
    private ImageView profileImage;
    private DocumentReference userRef1;
    private User user;
    private Uri mImageUri;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_profile,container,false);
        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        profileImage = V.findViewById(R.id.profile_image);
        userRef = mDataBaseStore.collection("users")
                .document(Objects.requireNonNull(userAuth.getUid()));
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                TextView t = V.findViewById(R.id.User_Name);
                t.setText(user.getName());
            }
        });

        address = V.findViewById(R.id.user_address);
        userPhone = V.findViewById(R.id.user_phone_number);
        userGender = V.findViewById(R.id.user_gender);
        userName = V.findViewById(R.id.user_name_value);
        userEmail = V.findViewById(R.id.user_Email);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                assert user != null;
                userName.setText(user.getName());
                userEmail.setText(user.getEmail());
                if(user.getGender()!=null){
                    userGender.setText(user.getGender());
                }
                if(user.getPhoneNumber()!= null){
                    userPhone.setText(user.getPhoneNumber()+"");
                }
                if(user.getAddress() != null){
                    address.setText(user.getAddress().toString());
                }
                if(user.getPicture() != null){
                    Picasso.get().
                            load(Uri.parse(user.getPicture())).
                            fit().
                            centerCrop().
                            into(profileImage);
                }

            }
        });
        V.findViewById(R.id.editProfileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, editProfileFragment).
                        commit();
            }
        });
        V.findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAuth.signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });


        return V;
    }
}
