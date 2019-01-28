package com.lichtenw.android.exoplayer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lichtenw.android.exoplayer.api.VideoServiceAPI;
import com.lichtenw.android.exoplayer.api.VideosResponse;
import com.lichtenw.android.exoplayer.model.Video;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * This demo app was created using ExoPlayer Version 2.9.4.
 */
public class MainActivity extends AppCompatActivity implements VerticalPager.OnScrollListener, VideoContainer.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();

    private VerticalPager mPager;
    private int currentPage;
    private int prevPage = -1;
    private VideoContainer currView;
    private VideoContainer prevView;
    //private int offset = 0;
    //private int count = 15;
    private int total_count;
    //private String category = "sports";
    //private VideoServiceAPI videoAPI;
    //private Call<VideosResponse> videoCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager = findViewById(R.id.vert_pager);
        mPager.addOnScrollListener(this);
        //initVideoAPI();
        //callVideosApi(category);
        initMockData();
    }


    @Override
    public void onStop() {
        super.onStop();
        //if (videoCall != null) {
            //videoCall.cancel();
        //}
        if (currView != null) {
            currView.onStop();
        }
    }


    private void addVideos(List<Video> list) {
        for (Video video : list) {
            VideoContainer container = new VideoContainer(getBaseContext());
            container.setVideo(video);
            container.addCallback(this);
            mPager.addView(container);
        }
    }


    // **************************** VIDEO CONTAINER CALLBACK ******************************
    @Override
    public void onNext() {
        if (currentPage+1 < mPager.getChildCount()) {
            mPager.snapToPage(currentPage+1, 1000);
        }
    }



    // **************************** VIEW PAGER ******************************

    @Override
    public void onScroll(int scrollX) {
    }


    @Override
    public void onViewScrollFinished(int currentPage) {
        Log.d(TAG, "onViewScrollFinished " + currentPage);
        this.currentPage = currentPage;
        View view = mPager.getChildAt(currentPage);
        if (currentPage != prevPage && view != null) {
            prevPage = currentPage;
            if (prevView != null) {
                prevView.onStop();
            }
            currView = (VideoContainer) view;
            currView.onStart();
            prevView = currView;
        }
    }


    // **************************** VIDEO API ******************************

                    /*
    private void initVideoAPI() {

        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(24, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VideoServiceAPI.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        videoAPI = retrofit.create(VideoServiceAPI.class);
    }


    private void callVideosApi(String cat) {
        Log.d(TAG, "Execute query for category: " + cat + ", offset: " + offset);
        videoCall = videoAPI.getVideos();
        videoCall.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                VideosResponse videosResponse = response.body();
                for (String url : videosResponse.videos) {
                    Log.d(TAG, "Video: " + url);
                    VideoContainer container = new VideoContainer(getBaseContext());
                    Video video = new Video();
                    video.sources = new String[]{url};
                    container.setVideo(video);
                    container.addCallback(MainActivity.this);
                    mPager.addView(container);
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("API Error")
                        .setMessage("\n" + t.getMessage() + "\n")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }
                    */


    // **************************** MOCK ******************************


    private void initMockData() {
        //addVideos(getLocalVideos());
        addVideos(getVideoList());
        total_count = mPager.getChildCount();
        findViewById(R.id.root_progress_bar).setVisibility(View.GONE);
    }


    //https://gist.github.com/jsturgis/3b19447b304616f18657
    private List<Video> getVideoList() {
        try {
            InputStream is = getResources().openRawResource(R.raw.videos);
            byte[] b = new byte[is.available()];
            is.read(b);
            String json = new String(b);
            is.close();
            Type type = TypeToken.getParameterized(ArrayList.class, Video.class).getType();
            Gson GSON = new GsonBuilder().create();
            return GSON.fromJson(json, type);
        } catch (Exception ex) {
            Log.e(TAG, "Failed to Load Videos", ex);
            return null;
        }
    }


    private List<Video> getLocalVideos() {
        List<Video> list = new ArrayList<>();
        for (String sample : new String[] {"Video_640x360", "small", "Video_640x360", "small"}) {
            Video video = new Video();
            video.sources = new String[]{"asset:///" + sample + ".mp4"};
            list.add(video);
        }
        return list;
    }
}
