package com.itla.blogapiui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itla.blogapiui.entidades.Login;
import com.itla.blogapiui.entidades.User;
import com.itla.blogapiui.servicio.BlogApiServices;
import com.itla.blogapiui.servicio.SecurityService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginApi extends AppCompatActivity {

    Button btnLogin,btnRegistrar;
    EditText edUser, edPassword;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_api);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarse);

        edUser = (EditText) findViewById(R.id.edUserName);
        edPassword = (EditText) findViewById(R.id.edPassword);

        final SecurityService securityService = BlogApiServices
                .getInstance()
                .getSecurityService();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edUser.getText().toString().isEmpty()){
                    Log.i("Usuario: ","Debe ingresar su usuario");
                }else if(edUser.getText().toString().isEmpty()){
                    Log.i("Password: ","Debe ingresar su password");
                }else{
                    Login login = new Login();
                    login.setEmail(edUser.getText().toString());
                    login.setPassword(edPassword.getText().toString());

                    Call<User> cuser = securityService.Login(login);

                    cuser.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.code() == 201){
                                User user = response.body();
//                                Toast.makeText(LoginApi.this, "Autenticado", Toast.LENGTH_SHORT).show();

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("token", response.body().getToken());

                                editor.commit();

                                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainActivity);

                            }else{
                                Toast.makeText(LoginApi.this, "Fallo la autenticacion", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrar = new Intent(getApplicationContext(), Register.class);
                startActivity(registrar);
            }
        });

    }



}
