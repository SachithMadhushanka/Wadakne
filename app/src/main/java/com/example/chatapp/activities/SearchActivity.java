package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.chatapp.R;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;

public class SearchActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    com.example.chatapp.databinding.ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.chatapp.databinding.ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager =new PreferenceManager(getApplicationContext());
        setListeners();
        loadUserDetails();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.search) {
                Intent intent=new Intent(this, SearchActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.chat) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }else if (item.getItemId() == R.id.home){
                Intent intent=new Intent(getApplicationContext(),HomePageActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.profile) {
                Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.addJob) {
                Intent intent=new Intent(getApplicationContext(), AddJobActivity.class);
                startActivity(intent);
                finish();
            }

            return true;
        });
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),HomePageActivity.class)));
    }
    private void loadUserDetails(){
        byte[] bytes = android.util.Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
}