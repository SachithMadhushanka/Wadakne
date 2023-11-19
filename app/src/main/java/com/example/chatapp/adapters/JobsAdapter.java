package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.activities.ChatActivity;
import com.example.chatapp.activities.HomePageActivity;
import com.example.chatapp.activities.jobs;
import com.example.chatapp.databinding.ItemContainerJobBinding;

import java.util.ArrayList;
import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsViewHolder> {

    private final List<jobs> jobs;

    public JobsAdapter(List<com.example.chatapp.activities.jobs> jobs, HomePageActivity homePageActivity) {
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerJobBinding itemContainerJobBinding = ItemContainerJobBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new JobsViewHolder(itemContainerJobBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsViewHolder holder, int position) {
        holder.setJobsData(jobs.get(position));
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    class JobsViewHolder extends RecyclerView.ViewHolder{
        ItemContainerJobBinding binding;

        JobsViewHolder(ItemContainerJobBinding itemContainerJobBinding){
            super(itemContainerJobBinding.getRoot());
            binding = itemContainerJobBinding;
        }
        void setJobsData(jobs jobs){
            binding.jobName.setText(jobs.jobName);
            binding.name.setText(jobs.name);
            binding.jobCategory.setText(jobs.jobCategory);
            binding.jobLocation.setText(jobs.jobLocation);
            binding.imageProfile.setImageBitmap(getUserImage(jobs.image));
        }
    }
    public void searchDataList(ArrayList<jobs> searchList){

    }
    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
