package com.lichtenw.android.exoplayer.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//
// Potential API to communicate with a video service...
//
public interface VideoServiceAPI {

    public String BASE_URL = "";

    @GET("videos/{category}")
    Call<VideosResponse> getVideos(@Path("category") String category,
                                   @Query("count") int count,
                                   @Query("offset") int offset,
                                   @Query("apiKey") String apiKey);
}
