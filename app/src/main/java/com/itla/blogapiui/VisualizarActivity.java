package com.itla.blogapiui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itla.blogapiui.Adapter.AdapterComment;
import com.itla.blogapiui.entidades.Comment;
import com.itla.blogapiui.entidades.Post;
import com.itla.blogapiui.servicio.BlogApiServices;
import com.itla.blogapiui.servicio.SecurityService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizarActivity extends AppCompatActivity {

    EditText etNewComment, etBodyD;
    TextView txtTitleD,txtCreateAtD, txtTagsD,txtLikesD,
    txtCommentsD,txtViewsD, txtCreatorD;

    Button btnAddNewComment;

    RecyclerView rvPostDatail;
    AdapterComment adapterComment;
    RecyclerView.LayoutManager mLayoutManager;

    SecurityService securityService;

    SharedPreferences preferences;
    String token;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        txtTitleD = (TextView) findViewById(R.id.txtTitleD);
        txtCreateAtD = (TextView) findViewById(R.id.txtCreatedD);
        txtCommentsD = (TextView) findViewById(R.id.txtCommentsD);
        txtCreatorD = (TextView) findViewById(R.id.txtCreatorD);
        txtViewsD = (TextView) findViewById(R.id.txtViewsD);
        txtTagsD = (TextView) findViewById(R.id.txtTagsD);
        etBodyD = (EditText) findViewById(R.id.etBodyD);
        txtLikesD = (TextView) findViewById(R.id.txtLikesD);
        btnAddNewComment = (Button) findViewById(R.id.btnAddCommentD);
        etNewComment = (EditText) findViewById(R.id.etNewComment);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");


        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        token =(preferences.getString("token", "Default_Value"));

        securityService = BlogApiServices.getInstance()
                          .getSecurityService();

        loadPost(id,token);
        loadComments();

        btnAddNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newComment = etNewComment.getText().toString();
                insertItem(newComment);
            }
        });


    }

    public void insertItem(String newComment){

        Comment comment = new Comment();
        comment.setBody(newComment);

        Call<Comment> commentCall = securityService.newComment(id,comment,"Bearer " + token);
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.isSuccessful()){
                    loadComments();
                    adapterComment.notifyDataSetChanged();
                }else{
                    Toast.makeText(VisualizarActivity.this,"ERROR CODE " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
    }

    private void loadComments() {
        final ArrayList<Comment> arrayListPost = new ArrayList<>();

        Call<List<Comment>> listComments = securityService.getComments(id, "Bearer " + token);

        listComments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                List<Comment> posts = response.body();

               if(response.isSuccessful()){
                   for(Comment comment : posts){
                       Comment postComment  = new Comment();
                       postComment.setBody(comment.getBody().toString());
                       postComment.setCreatedAt(comment.getCreatedAt());
                       postComment.setId(comment.getId());
                       postComment.setPostId(comment.getPostId());
                       postComment.setUserEmail(comment.getUserEmail());
                       postComment.setUserName(comment.getUserName());
                       postComment.setUserId(comment.getUserId());
                       postComment.setImageResource(R.drawable.comments);
                       arrayListPost.add(postComment);
                   }
                   rvPostDatail = findViewById(R.id.rvPostDetail);
                   rvPostDatail.setHasFixedSize(true);
                   mLayoutManager = new LinearLayoutManager(VisualizarActivity.this);
                   Collections.reverse(arrayListPost);
                   adapterComment = new AdapterComment(arrayListPost);
                   rvPostDatail.setLayoutManager(mLayoutManager);
                   rvPostDatail.setAdapter(adapterComment);
               }else{
                   Toast.makeText(VisualizarActivity.this,"ERROR CODE " + response.code(), Toast.LENGTH_LONG).show();
               }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });


    }

    private void loadPost(int id, String token) {
        Call<Post> cpost = securityService.
                            getOnePost(id,"Bearer " + token);
        cpost.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()){
//                    Toast.makeText(VisualizarActivity.this,"EXITO",Toast.LENGTH_LONG).show();
                    Post post = new Post();
                    txtTitleD.setText(response.body().getTitle().toString());
                    txtCreateAtD.setText(response.body().getCreatedAt().toString());
                    txtLikesD.setText(String.valueOf(response.body().getLikesPost()));
                    txtCommentsD.setText(String.valueOf(response.body().getComments()));
                    txtViewsD.setText(String.valueOf(response.body().getViews()));
                    etBodyD.setText(response.body().getBody().toString());
                    txtCreatorD.setText(response.body().getUserName());
                }else{
                    Toast.makeText(VisualizarActivity.this,"ERROR CODE " + response.code(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
}
