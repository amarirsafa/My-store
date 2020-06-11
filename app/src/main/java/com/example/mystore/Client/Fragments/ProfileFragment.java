package com.example.mystore.Client.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore.Client.Classes.Address;
import com.example.mystore.Client.Classes.User;
import com.example.mystore.R;
import com.google.android.gms.tasks.OnFailureListener;
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
    private TextView country,province,city,streetAdd,postalCode,userPhone,userCIN,userGender;
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
        city = V.findViewById(R.id.city);
        country = V.findViewById(R.id.country);
        province = V.findViewById(R.id.province);
        postalCode = V.findViewById(R.id.postal_code);
        streetAdd = V.findViewById(R.id.street_address);
        userPhone = V.findViewById(R.id.phone_number);
        userGender = V.findViewById(R.id.gender);
        userCIN = V.findViewById(R.id.CIN);

        V.findViewById(R.id.Save_changes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = addAddress();//this fucntion will add the address to the users class before storing it in the database!
                editUser(); //this function will load everything to the database;
            }
        });

        return V;
    }


    private void editUser() {
        userRef.update("gender",userGender.getText().toString(),"phoneNumber"
                ,userPhone.getText().toString(),"CIN",userCIN.getText()+"","address",address.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Information saved!", Toast.LENGTH_SHORT).show();
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
        address.setPostalCode(postalCode.getText()+"");
        address.setStreet(streetAdd.getText().toString());
        return address;
    }
}
