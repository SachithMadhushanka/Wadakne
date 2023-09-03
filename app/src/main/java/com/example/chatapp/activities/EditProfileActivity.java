package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatapp.R;
import com.example.chatapp.utilities.PreferenceManager;

public class EditProfileActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    com.example.chatapp.databinding.ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.chatapp.databinding.ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager =new PreferenceManager(getApplicationContext());
        setListeners();
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),ProfileActivity.class)));}
}