package com.itla.blogapiui.servicio;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogApiServices {

    private static final String PATH_API = "http://itla.hectorvent.com/api/";
    private static BlogApiServices BAS;

    Retrofit retrofit;

    private BlogApiServices(){
        retrofit = new Retrofit.Builder()
                .baseUrl(PATH_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    /*
     * Decirle a retrofit que debe crear una instancia de la clase SecurityService
     * */
    public SecurityService getSecurityService(){
        return retrofit.create(SecurityService.class);
    }

    public static BlogApiServices getInstance() {
        if(BAS == null){
            BAS = new BlogApiServices();
        }
        return BAS;
    }
}
