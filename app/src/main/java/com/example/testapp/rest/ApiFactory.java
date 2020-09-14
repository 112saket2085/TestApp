package com.example.testapp.rest;

import android.text.TextUtils;

import com.example.testapp.BuildConfig;
import com.example.testapp.app.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 11/08/2020
 * Class contains all retrofit api configuration
 */
public class ApiFactory {

    // http headers
    private static final String HEAD_CONTENT_TYPE = "Content-Type";
    private static final String KEY = "Ocp-Apim-Subscription-Key";
    private static final String contentType = "application/json";
    private TestApi shadiApi;

    public ApiFactory() {
        init();
    }

    private void init() {
        Retrofit retrofit = createRetrofitClient(getOkHttpClient());
        shadiApi = retrofit.create(TestApi.class);
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        if (!BuildConfig.BUILD_TYPE.equals(Constants.BUILD_TYPE_RELEASE)) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpBuilder.addNetworkInterceptor(logging);
        }
        httpBuilder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                builder.addHeader(HEAD_CONTENT_TYPE,contentType);
                builder.addHeader(KEY," 9038a799283f45aaad1ec6bf9b59051a");
                builder.method(originalRequest.method(), originalRequest.body());
                return chain.proceed(builder.build());
            }
        });
        httpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                return hostname.equalsIgnoreCase(BuildConfig.HOST_NAME);
            }
        });
        httpBuilder.connectTimeout(2, TimeUnit.MINUTES);
        httpBuilder.readTimeout(2, TimeUnit.MINUTES);
        return httpBuilder.build();
    }

    /**
     * Method to get Retrofit instance
     */
    private Retrofit createRetrofitClient(OkHttpClient okClient) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
    }

    /**
     * Method to get shadi instance
     * @return shadi instance
     */
    public TestApi getShadiApi() {
        return shadiApi;
    }

}
