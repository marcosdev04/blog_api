package com.itla.blogapiui.Adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itla.blogapiui.R;
import com.itla.blogapiui.entidades.Comment;

import java.util.ArrayList;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.AdapterCommentHolder> {

    @NonNull
    @Override
    public AdapterCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        AdapterCommentHolder aCommentHolder = new AdapterCommentHolder(v);
        return aCommentHolder;
    }

    ArrayList<Comment> commentList;
    public AdapterComment(ArrayList<Comment> commentList){
        this.commentList = commentList;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCommentHolder holder, int position) {
        Comment currentComment = commentList.get(position);
        holder.imageView.setImageResource(currentComment.getImageResource());
        holder.txtComment.setText(currentComment.getBody());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class AdapterCommentHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        TextView txtComment;

        public AdapterCommentHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iconComment);
            txtComment = itemView.findViewById(R.id.txtCommentItem);

        }
    }
}
