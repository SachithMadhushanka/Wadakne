package com.example.chatapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private DocumentReference documentReference;
    com.example.chatapp.databinding.ActivityEditProfileBinding binding;

    private String previousName,previousPassword,previousEmail,previousImage;
    private String newName,newPassword,newEmail,newImage;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.chatapp.databinding.ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager =new PreferenceManager(getApplicationContext());
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        setListeners();

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                previousEmail = value.getString(Constants.KEY_EMAIL);
                previousName = value.getString(Constants.KEY_NAME);
                previousPassword = value.getString(Constants.KEY_PASSWORD);
            }
        });
    }
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),ProfileActivity.class)));
        binding.buttonEditProfile.setOnClickListener(v -> {
            if (isChangeDetails()){
                updateProfile();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private String encodedImage(Bitmap bitmap){
        int previewWidth=150;
        int previewHeight= bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap=Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()==RESULT_OK){
                    Uri imageUri=result.getData().getData();
                    try {
                        InputStream inputStream=getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        binding.imageProfile.setImageBitmap(bitmap);
                        binding.textAddImage.setVisibility(View.GONE);
                        encodedImage=encodedImage(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }
    );

    private void updateProfile(){
        updateName();
        updateEmail();
        updatePassword();
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    private void updateName(){
        newName = binding.inputName.getText().toString().trim();
        documentReference.update(Constants.KEY_NAME, newName);

    }
    private void updateEmail(){
        newEmail = binding.inputEmail.getText().toString().trim();
        documentReference.update(Constants.KEY_EMAIL, newEmail);

    }
    private void updatePassword(){
        newPassword = binding.inputPassword.getText().toString().trim();
        documentReference.update(Constants.KEY_PASSWORD, newPassword);

    }

    private void updateImage(){
        newImage = encodedImage;
        documentReference.update(Constants.KEY_IMAGE, newImage);
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private Boolean isChangeDetails(){
        if (previousName == binding.inputName.getText().toString().trim() || binding.inputName.getText().toString().trim().isEmpty() ) {
            newName = previousName;
            return false;
        } else if (previousEmail == binding.inputEmail.getText().toString().trim() || binding.inputEmail.getText().toString().trim().isEmpty()) {
            newEmail = previousEmail;
            return false;
        }  else if (previousPassword == binding.inputPassword.getText().toString().trim() || binding.inputPassword.getText().toString().trim().isEmpty()) {
            newPassword = previousPassword;
            return false;
        } else if (previousImage == encodedImage || encodedImage==null) {
            newImage = previousImage;
            return false;
        } else {
            return true;
        }
    }
}