package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.chatapp.databinding.ActivityRatingBinding;


public class RatingActivity extends AppCompatActivity {

    private ActivityRatingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        }
}