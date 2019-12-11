package com.itla.blogapiui.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itla.blogapiui.ItemPost;
import com.itla.blogapiui.R;
import com.itla.blogapiui.entidades.Post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.http.POST;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.AdapterPostViewHolder> {


    private Context pContext;
    private ArrayList<Post> pAdapterList;
    private OnItemClickListener mListener;

    public AdapterPost(Context context, ArrayList<Post> adapterLists){
        pContext = context;
        pAdapterList = adapterLists;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onOpenClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public AdapterPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(pContext).inflate(R.layout.activity_item_post,parent,false);
        return new AdapterPostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPostViewHolder holder, int position) {
        Post currentPost = pAdapterList.get(position);

        String tBody = currentPost.getBody();
        String tTitle = currentPost.getTitle();
        List<String> tTags = currentPost.getTags();
        Integer cLikes = currentPost.getLikesPost();
        Integer cComments = currentPost.getComments();
        Integer cViews = currentPost.getViews();
        String cCreator = currentPost.getUserName();

        String longV = currentPost.getCreatedAt().toString();
        long millisecond = Long.parseLong(longV);
        String tDate = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();

        // Asignar los valores a los EditText
        holder.txtBody.setText(tBody);
        holder.txtTitle.setText(tTitle);
        holder.txtTags.setText(tTags.toString());
        holder.txtDate.setText(tDate);
        holder.txtLikes.setText(cLikes.toString());
        holder.txtComments.setText(cComments.toString());
        holder.txtViews.setText(cViews.toString());
        holder.txtCreator.setText(cCreator);

        if(currentPost.isLiked() != true) {
            holder.txtLikes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.desliked, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return pAdapterList.size();
    }

    public class AdapterPostViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle, txtTags, txtDate, txtBody, txtLikes
                , txtComments,txtViews, txtCreator;
        Button btnAbrirPost;

        public AdapterPostViewHolder(@NonNull final View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTags = itemView.findViewById(R.id.txtTags);
            txtBody = itemView.findViewById(R.id.txtBody);
            txtLikes = itemView.findViewById(R.id.txtLikes);
            txtComments = itemView.findViewById(R.id.txtComments);
            txtViews = itemView.findViewById(R.id.txtViews);
            txtCreator = itemView.findViewById(R.id.txtCreator);
            btnAbrirPost = itemView.findViewById(R.id.btnOpen);

            itemView.findViewById(R.id.txtLikes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.findViewById(R.id.btnOpen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onOpenClick(position);
                        }
                    }
                }
            });
        }
    }
}
