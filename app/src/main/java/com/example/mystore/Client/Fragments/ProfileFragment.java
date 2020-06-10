package com.example.mystore.Client.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore.Client.Classes.Address;
import com.example.mystore.Client.Classes.User;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    private FirebaseAuth userAuth;
    private DocumentReference userRef;
    private FirebaseFirestore mDataBaseStore ;
    private TextView user_Name;
    private Address address;
    private View V;
    private DocumentReference userRef1;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V =  inflater.inflate(R.layout.fragment_profile,container,false);

        userAuth = FirebaseAuth.getInstance();
        mDataBaseStore = FirebaseFirestore.getInstance();
        userRef = mDataBaseStore.collection("users").document(Objects.requireNonNull(userAuth.getUid()));

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                TextView t = V.findViewById(R.id.User_Name);
                t.setText(user.getName());
            }
        });

        V.findViewById(R.id.Save_changes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress();//this fucntion will add the address to the users class before storing it in the database!
                editUser(); //this function will load everything to the database;
            }
        });

        return V;
    }


    private void editUser() {
        userRef.update("gender",V.findViewById(R.id.gender).toString(),"phoneNumber"
                ,V.findViewById(Integer.parseInt(String.valueOf(V.findViewById(R.id.phone_number)))),"address",address.toString());
    }

    private void addAddress() {
        address.setCity(V.findViewById(R.id.city).toString());
        address.setCountry(V.findViewById(R.id.country).toString());
        address.setProvince(V.findViewById(R.id.province).toString());
        address.setPostalCode(Integer.parseInt(String.valueOf(V.findViewById(R.id.postal_code))));
        address.setStreet(V.findViewById(R.id.street_address).toString());

    }
}
