package com.lichtenw.android.exoplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.lichtenw.android.exoplayer.model.Video;



public class VideoContainer extends FrameLayout implements VideoRendererEventListener,
        ExoPlayer.EventListener, View.OnClickListener {

    private static String TAG = VideoContainer.class.getSimpleName();

    public interface Callback {
        void onNext();
    }

    private Callback callback;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    //private ImageView imageView;
    private ProgressBar progressBar;
    private Video video;
    //private long currentPosition;


    public VideoContainer(Context context) {
        super(context);
        init();
    }


    public VideoContainer(Context context, Video video) {
        super(context);
        setVideo(video);
        init();
    }


    private void init() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.video_container, null);
        addView(view, -1, -2);

        progressBar = view.findViewById(R.id.progress_bar);
        //imageView = findViewById(R.id.image_view);

        playerView = view.findViewById(R.id.player_view);
        playerView.setUseController(false);
        playerView.setControllerShowTimeoutMs(3000);

        setOnClickListener(this);
    }


    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public void setVideo(Video video) {
        this.video = video;
    }


    private MediaSource getVideoSource() {

        Uri uri = Uri.parse(video.sources[0]);

        if (video.sources[0].startsWith("asset")) {
            DataSpec dataSpec = new DataSpec(uri);
            final AssetDataSource assetDataSource = new AssetDataSource(getContext());
            try {
                assetDataSource.open(dataSpec);
            } catch(AssetDataSource.AssetDataSourceException e) {
                Log.e(TAG, "Failed to open asset", e);
            }
            DataSource.Factory dataSourceFactory = () -> assetDataSource;
            return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        } else {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            String userAgent = Util.getUserAgent(getContext(), "ExoPlayerOath");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent, bandwidthMeter);
            if (video.sources[0].endsWith("m3u8")) {
                //Live Stream...
                //Uri uri = Uri.parse("http://live.field59.com/wwsb/ngrp:wwsb1_all/playlist.m3u8"); //ABC NEWS
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            } else {
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            }
        }
    }


    public void onStart() {
        Log.d(TAG, "onStart " + video.sources[0]);
        TrackSelection.Factory selectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(selectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        player.setPlayWhenReady(true);
        playerView.setPlayer(player);
        player.prepare(getVideoSource());
        player.addListener(this);
        player.setVideoDebugListener(this);
    }


    public void onStop() {
        Log.d(TAG, "onStop");
        if (player != null) {
            player.stop();
        }
        onRelease();
    }


    public void onRelease() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


    @Override
    public void onClick(View v) {
        if (playerView.isControllerVisible()) {
            playerView.setUseController(false);
            playerView.hideController();
        } else {
            playerView.setUseController(true);
            playerView.showController();
        }
    }


    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(TAG, "onVideoSizeChanged [" + " width: " + width + " height: " + height + "]");
        progressBar.setVisibility(View.GONE);
        //imageView.setVisibility(View.GONE);
        /*
        View view = ((FrameLayout) playerView.findViewById(R.id.exo_content_frame)).getChildAt(0);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        view.setLayoutParams(lp);
        */
    }


    @Override
    public void onRenderedFirstFrame(Surface surface) {
    }


    @Override
    public void onVideoDisabled(DecoderCounters counters) {
    }



    // EventListener..............................................

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d(TAG, "onTracksChanged");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.e(TAG, "onPlayerStateChanged..." + playbackState);
        if (callback != null && playbackState == Player.STATE_ENDED) {
            callback.onNext();
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e(TAG, "onPlayerError");
        player.stop();
        new AlertDialog.Builder(getContext())
                .setTitle("Player Error")
                .setMessage("\n" + error.getMessage() + "\n")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
    }



























    /*
        //MediaSource subtitleSource = getSubtitleSource(mSubtitleUri);
        //MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);
    private MediaSource getSubtitleSource() {
        Uri uri = Uri.parse(subtitle);
        // Build the subtitle MediaSource.
        Format subtitleFormat = Format.createTextSampleFormat(
                null, // An identifier for the track. May be null.
                MimeTypes.APPLICATION_SUBRIP, // The mime type. Must be set correctly.
                Format.NO_VALUE, // Selection flags for the track.
                null); // The subtitle language. May be null.

        return new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(uri, subtitleFormat, C.TIME_UNSET);
    }
    */


    /*
    public void loadThumb() {
        if (video.thumb == null) {
            return;
        }
        if (currentPosition > 0) {
            return;
        }
        String src = video.sources[0].substring(0, video.sources[0].lastIndexOf("/")+1);
        src += video.thumb;
        Log.d(TAG, "loadThumb " + src);
        Glide.with(getContext())
                .load(src)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Failed to load image");
                        imageView.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }
    */
}
