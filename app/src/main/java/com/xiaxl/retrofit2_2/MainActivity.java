package com.xiaxl.retrofit2_2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.xiaxl.retrofit2_2.netagent.ApiGitHub;
import com.xiaxl.retrofit2_2.netagent.model.Contributor;
import com.xiaxl.retrofit2_2.netagent.model.RetrofitBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "xiaxl: MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.get01_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContributorList01();
            }
        });
        findViewById(R.id.get02_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContributorList02();
            }
        });
        findViewById(R.id.get03_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContributorList03();
            }
        });
        findViewById(R.id.get04_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> queryMap = new HashMap<>();
                queryMap.put("q", "retrofit");
                queryMap.put("since", "2016-03-29");
                queryMap.put("page", "1");
                queryMap.put("per_page", "3");
                getRetrofitBean(queryMap);
            }
        });

    }


    /**
     * get 不带参数，不带header，不包含Gson解析
     */

    private void getContributorList01() {
        Log.e(TAG, "---getContributorList01---");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();
        ApiGitHub apiGitHub = retrofit.create(ApiGitHub.class);

        Call<ResponseBody> call = apiGitHub.getResponseBody("square", "retrofit");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e(TAG, "response: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * get 不带参数，不带header，包含Gson解析
     */
    private void getContributorList02() {
        Log.e(TAG, "---getContributorList02---");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiGitHub repo = retrofit.create(ApiGitHub.class);

        Call<List<Contributor>> call = repo.getContributorList("square", "retrofit");
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                List<Contributor> contributorList = response.body();
                Log.e(TAG, "contributorList: " + contributorList);
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });
    }


    /**
     * get 不带参数，带header，包含Gson解析
     */
    private void getContributorList03() {
        Log.e(TAG, "---getContributorList03---");
        //
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();
        ApiGitHub repo = retrofit.create(ApiGitHub.class);
        //
        Call<List<Contributor>> call = repo.getContributorListHeader("square", "retrofit");
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                List<Contributor> contributorList = response.body();
                Log.e(TAG, "contributorList: " + contributorList);
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });
    }


    /**
     * get 带参数，不带header，包含Gson解析
     * <p>
     * Map<String, String> queryMap = new HashMap<>();
     * queryMap.put("q", "retrofit");
     * queryMap.put("since", "2016-03-29");
     * queryMap.put("page", "1");
     * queryMap.put("per_page", "3");
     *
     * @param queryMap
     */
    private void getRetrofitBean(Map<String, String> queryMap) {
        Log.e(TAG, "---getRetrofitBean---");
        //
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();
        ApiGitHub repo = retrofit.create(ApiGitHub.class);
        //
        Call<RetrofitBean> call;
        if (queryMap == null || queryMap.size() == 0) {
            call = repo.getRetrofitBean("retrofit", "2016-03-29", 1, 3);
        } else {
            call = repo.getRetrofitBean(queryMap);
        }
        call.enqueue(new Callback<RetrofitBean>() {
            @Override
            public void onResponse(Call<RetrofitBean> call, Response<RetrofitBean> response) {
                RetrofitBean retrofit = response.body();
                Log.e(TAG, "retrofit: " + retrofit);

            }

            @Override
            public void onFailure(Call<RetrofitBean> call, Throwable t) {

            }
        });
    }


}
