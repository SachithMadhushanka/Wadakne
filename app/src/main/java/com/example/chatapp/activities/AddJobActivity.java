package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddJobActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    com.example.chatapp.databinding.ActivityAddJobBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.chatapp.databinding.ActivityAddJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager =new PreferenceManager(getApplicationContext());
        setListeners();
        loadUserDetails();
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),HomePageActivity.class)));
        binding.buttonPostJob.setOnClickListener(v -> {
            if (isValidPostDetails()){
                addJob();
                startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
            }
        });
    }
    private void loadUserDetails(){
        byte[] bytes = android.util.Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
    private Boolean isValidPostDetails(){
        if(binding.inputJob.getText().toString().trim().isEmpty()) {
            showToast("Enter Job");
            return false;
        } else if (binding.inputCategory.getText().toString().trim().isEmpty()) {
            showToast("Enter Job category");
            return false;
        } else if (binding.inputLocation.getText().toString().trim().isEmpty()) {
            showToast("Enter Location");
            return false;
        } else {
            return true;
        }
    }
    private void addJob(){
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        HashMap<String,Object> user=new HashMap<>();
        user.put(Constants.KEY_NAME,preferenceManager.getString(Constants.KEY_NAME));
        user.put(Constants.KEY_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE));
        user.put("Job Name",binding.inputJob.getText().toString());
        user.put("Category",binding.inputCategory.getText().toString());
        user.put("Location",binding.inputLocation.getText().toString());
        database.collection("Job")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    showToast("Successful");
                })
                .addOnFailureListener(exception->{
                    showToast(exception.getMessage());
                });
    }

}