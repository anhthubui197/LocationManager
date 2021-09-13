package com.geocomply.locationmanager.repository.remote;

import com.geocomply.locationmanager.BuildConfig;
import com.geocomply.locationmanager.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static final OkHttpClient client = new OkHttpClient.Builder()
//            .addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request()
//                            .newBuilder()
//                            .addHeader("key", BuildConfig.API_KEY).build();
//                    return chain.proceed(request);
//                }
//            })
            .addInterceptor(logging)
            .build();

    private static final Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    //.client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit.Builder retrofitBuilderGoogle =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_GOOGLE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    public static LocationApi getLocationApi(){
        return retrofitBuilder.build().create(LocationApi.class);
    }
    public static GoogleApi getGoogleApi(){
        return retrofitBuilderGoogle.build().create(GoogleApi.class);
    }
}
