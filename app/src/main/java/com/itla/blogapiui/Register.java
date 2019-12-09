package com.itla.blogapiui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itla.blogapiui.entidades.Regist;
import com.itla.blogapiui.entidades.User;
import com.itla.blogapiui.servicio.BlogApiServices;
import com.itla.blogapiui.servicio.SecurityService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText edUNRegister, edPasswordRegister,edEmailRegister;
    Button btnRegistrar, btnCancelar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUNRegister = findViewById(R.id.edUNRegister);
        edPasswordRegister = findViewById(R.id.edPasswordRegister);
        edEmailRegister = findViewById(R.id.edEmailRegister);

        btnCancelar = findViewById(R.id.btnCancelarR);
        btnRegistrar = findViewById(R.id.btnRegistrarR);
        progressBar = findViewById(R.id.progress_circular);

        btnRegistrar.setOnClickListener(this);


    }

    final SecurityService securityService = BlogApiServices
            .getInstance()
            .getSecurityService();

    @Override
    public void onClick(View v) {

        if(edEmailRegister.getText().toString().trim().isEmpty()
        || edUNRegister.getText().toString().trim().isEmpty() ||
           edPasswordRegister.getText().toString().trim().isEmpty()){
            Toast.makeText(Register.this,"Hubo un campo sin completar, favor llenar",Toast.LENGTH_LONG).show();
        }else{
            switch (v.getId()){
                case R.id.btnRegistrarR:
                    progressBar.setVisibility(v.VISIBLE);

                    Regist regist1 = new Regist();

                    regist1.setName(edUNRegister.getText().toString().trim());
                    regist1.setEmail(edEmailRegister.getText().toString().trim());
                    regist1.setPassword(edPasswordRegister.getText().toString().trim());

                    Call<Regist> cregist = securityService.Register(regist1);

                    cregist.enqueue(new Callback<Regist>() {
                        @Override
                        public void onResponse(Call<Regist> call, Response<Regist> response) {

                            progressBar.setVisibility(View.GONE);

                            Regist rg = response.body();

                            if(response.isSuccessful() && rg != null){
                                Toast.makeText(Register.this,String.format("El usuario %s con el correo %s fue creado con el id %s",
                                        rg.getName(),rg.getEmail(),rg.getId()), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(Register.this,
                                        String.format("Response is %s", String.valueOf(response.code()))
                                        , Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Regist> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Register.this,
                                    "Error es " + t.getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
            }
        }

    }
}
