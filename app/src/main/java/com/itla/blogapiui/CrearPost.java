package com.itla.blogapiui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.itla.blogapiui.entidades.Post;
import com.itla.blogapiui.servicio.BlogApiServices;
import com.itla.blogapiui.servicio.SecurityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearPost extends AppCompatActivity {

    Button btnGuardarPost,btnCancelarPost;
    EditText etTitle, etBody, etTags;
    List<String> tags;

    String token = "";
    SharedPreferences mSharedPreference;
    final SecurityService securityService = BlogApiServices
            .getInstance()
            .getSecurityService();
    SharedPreferences preferences;


    public void Limpiar(ConstraintLayout layout){
        for (int i=0; i < layout.getChildCount(); i++){
            View v = layout.getChildAt(i);
            if (v instanceof EditText){
                ((EditText) v).setText("");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_post);



        btnGuardarPost = (Button) findViewById(R.id.btnGuardarPost);
        btnCancelarPost = (Button) findViewById(R.id.btnCancelarPost);
        etTitle = (EditText) findViewById(R.id.etTitulo);
        etBody = (EditText) findViewById(R.id.etBody);
        etTags = (EditText) findViewById(R.id.etTags);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = (preferences.getString("token","Default_Value"));

        btnGuardarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etTitle.getText().toString().isEmpty() | etBody.getText().toString().isEmpty()
                    | etTags.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Debe completar todos los campos",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Post post = new Post();
                    post.setTitle(etTitle.getText().toString());
                    post.setBody(etTitle.getText().toString());

                    String tgs = etTags.getText().toString();
                    tags = new ArrayList<String>(Arrays.asList(tgs.split(",")));
                    post.setTags(tags);

                    Call<Post> cpost = securityService.createPost(post , "Bearer " + token);

                    cpost.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            if(response.isSuccessful()){
                                Limpiar( (ConstraintLayout) findViewById(R.id.constraintCpost));
                                finish();
                            }else{
                                Toast.makeText(CrearPost.this,"Error, code " + response.code(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {

                        }
                    });

                }
            }
        });

    }
}
