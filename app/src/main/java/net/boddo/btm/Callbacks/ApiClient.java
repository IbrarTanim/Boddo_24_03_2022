package net.boddo.btm.Callbacks;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import net.boddo.btm.BuildConfig;
import net.boddo.btm.Services.NetworkConnectionInterceptor;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.NetworkUtils;

import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static Gson gson;
    private static Retrofit retrofit;

    public static Retrofit getApiClient() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
        if (retrofit == null) {
            synchronized (ApiClient.class) {
                if (retrofit == null) {
                    createRetrofit();
                }
            }
        }
        return retrofit;
    }

    private static void createRetrofit() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        OkHttpClient client = clientBuilder.readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor(getLogInterceptor())
                .addInterceptor(getGenericResponseHandler())
                .addInterceptor(getNetworkConnectionInterceptor())
                .build();


        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();

    }

    private static Interceptor getNetworkConnectionInterceptor() {

        return new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return NetworkUtils.isOnline();
            }

            @Override
            public void onInternetUnavailable() {
                //fixme no network available event will be fire
            }
        };
    }

    private static Interceptor getGenericResponseHandler() {
        return chain -> {
            Response response;
            try {
                Request request = chain.request();
                response = chain.proceed(request);

                if (response.code() == 500) {
                    //Todo Toast event will be hare
                } else if (response.code() == 401) {
                    // FIXME Logout Event
                }
                return response;
            } catch (SocketTimeoutException exception) {
                Logger.e(exception, "Socket time out");
                //TOdo Toast event week signal
            }
            return chain.proceed(chain.request());
        };
    }

    private static Interceptor getLogInterceptor() {
        HttpLoggingInterceptor logging;
        if (BuildConfig.DEBUG) {
            logging = new HttpLoggingInterceptor();
        } else {
            logging = new HttpLoggingInterceptor(getFileLogger());
        }
        return logging;
    }

    private static HttpLoggingInterceptor.Logger getFileLogger() {
        return message -> Logger.v(message);
    }

    private static Gson buildGson() {
        return gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();
    }

    private static Gson getGson() {
        if (gson == null) {
            gson = buildGson();
        }
        return gson;
    }
}
