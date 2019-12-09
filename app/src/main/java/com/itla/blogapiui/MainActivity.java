package com.itla.blogapiui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itla.blogapiui.Adapter.AdapterPost;
import com.itla.blogapiui.entidades.Post;
import com.itla.blogapiui.servicio.BlogApiServices;
import com.itla.blogapiui.servicio.SecurityService;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity implements AdapterPost.OnItemClickListener{

    SharedPreferences preferences;
    TextView txtLikes;
    TextView btnLike;
    Call<List<Post>> cposts;
    Map<String, String> headers = new HashMap<>();

    // Varias para RecyclerView y AdapterPost
    private RecyclerView pRecyclerView;
    private AdapterPost pAdapterPost;
    private ArrayList<Post> pAdapdaterList;

    String token = "";
    SharedPreferences mSharedPreference;

    final SecurityService securityService = BlogApiServices
            .getInstance()
            .getSecurityService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLikes = findViewById(R.id.txtLikes);

        pRecyclerView = findViewById(R.id.postRecyclerView);
        pRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pAdapdaterList = new ArrayList<>();

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        token =(mSharedPreference.getString("token", "Default_Value"));

        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + token);

        cposts = securityService.getPosts(headers);

        cposts.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Error" + token, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Post> posts = response.body();

                String content = "";

                for(Post post : posts){
                    Post pShow = new Post();
                    pShow.setId(post.getId());
                    pShow.setTitle(post.getTitle());
                    pShow.setBody(post.getBody());
                    pShow.setTags(post.getTags());
                    pShow.setCreatedAt(post.getCreatedAt());
                    pShow.setLikesPost(post.getLikesPost());
                    pShow.setLikedPost(post.isLiked());
                    pShow.setComments(post.getComments());
                    pShow.setViews(post.getViews());
                    pShow.setUserName(post.getUserName());
                    pAdapdaterList.add(pShow);
                }

                pAdapterPost = new AdapterPost(MainActivity.this, pAdapdaterList);
                pRecyclerView.setAdapter(pAdapterPost);
                pAdapterPost.setOnItemClickListener(MainActivity.this);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, " Dió " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(final int position) {

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        token =(mSharedPreference.getString("token", "Default_Value"));

        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + token);

        final Post postClicked = pAdapdaterList.get(position);



        if(postClicked.isLiked()){
            Call<Void> postCall = securityService.dislikePost(postClicked.getId(), headers);

            postCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if(response.isSuccessful()){
//                        reiniciarActivity(MainActivity.this);
                    }else{
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });
        }
        else{
            Call<Void> postCall = securityService.likePost(postClicked.getId(), headers);
            postCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 200 || !postClicked.isLiked()){
                        reiniciarActivity(MainActivity.this);
                    }else{

                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    reiniciarActivity(MainActivity.this);
                }
            });
//            reiniciarActivity(this);
        }


    }

    //reinicia una Activity
    public static void reiniciarActivity(Activity actividad){
        Intent intent=new Intent();
        intent.setClass(actividad, actividad.getClass());
        //llamamos a la actividad
        actividad.startActivity(intent  );
        actividad.finish();
        //finalizamos la actividad actual
    }

}
