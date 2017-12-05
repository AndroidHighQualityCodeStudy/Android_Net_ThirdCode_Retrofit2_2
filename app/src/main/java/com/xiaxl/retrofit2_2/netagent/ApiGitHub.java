package com.xiaxl.retrofit2_2.netagent;

import com.xiaxl.retrofit2_2.netagent.model.Contributor;
import com.xiaxl.retrofit2_2.netagent.model.RetrofitBean;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface ApiGitHub {

    @GET("repos/{owner}/{repo}/contributors")
    Call<ResponseBody> getResponseBody(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> getContributorList(@Path("owner") String owner, @Path("repo") String repo);

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: RetrofitBean-Sample-App",
            "name:ljd"
    })
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> getContributorListHeader(@Path("owner") String owner, @Path("repo") String repo);

    @GET("search/repositories")
    Call<RetrofitBean> getRetrofitBean(@Query("q") String owner,
                                       @Query("since") String time,
                                       @Query("page") int page,
                                       @Query("per_page") int per_Page);

    @GET("search/repositories")
    Call<RetrofitBean> getRetrofitBean(@QueryMap Map<String, String> map);


}
