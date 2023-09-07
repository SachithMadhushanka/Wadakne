package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.adapters.JobsAdapter;
import com.example.chatapp.adapters.RecentConversationAdapter;
import com.example.chatapp.adapters.UsersAdapter;
import com.example.chatapp.databinding.ActivityHomePage2Binding;
import com.example.chatapp.models.ChatMessage;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    com.example.chatapp.databinding.ActivityHomePage2Binding binding;
    private PreferenceManager preferenceManager;

    RecyclerView recyclerView;
    FirebaseFirestore database;

    ArrayList<jobs> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePage2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager =new PreferenceManager(getApplicationContext());
        recyclerView = binding.jobRecyclerView;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        loadUserDetails();
        getToken();
        setListeners();
        getJobs();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                Intent intent=new Intent(getApplicationContext(),HomePageActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.chat) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }else if (item.getItemId() == R.id.search) {
                Intent intent=new Intent(getApplicationContext(), SearchActivity.class);
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
        
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setListeners(){
        binding.imageSignOut.setOnClickListener(v -> signOut());
    }

    private void loadUserDetails(){
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = android.util.Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        DocumentReference documentReference=
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }
    private void signOut(){
        showToast("Signing out... ");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference=
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }


    private void getJobs() {
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        database.collection("Job")
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        List<jobs> jobsList = new ArrayList<>();
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                            jobs jobs =new jobs();
                                            jobs.jobName = queryDocumentSnapshot.getString("Job Name");
                                            jobs.jobCategory = queryDocumentSnapshot.getString("Category");
                                            jobs.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                                            jobs.jobLocation = queryDocumentSnapshot.getString("Location");
                                            jobs.name=queryDocumentSnapshot.getString("name");
                                            jobsList.add(jobs);
                        }
                                        if (jobsList.size() > 0){
                                            JobsAdapter jobsAdapter= new JobsAdapter(jobsList,this);
                                            binding.jobRecyclerView.setAdapter(jobsAdapter);
                                            binding.jobRecyclerView.setVisibility(View.VISIBLE);

                        // Now you have a list of jobs. You can do something with it here.
                                            } else {
                        // Handle the case where the Firestore query was unsuccessful.
                    }
                }});
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
}