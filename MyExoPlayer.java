package com.lichtenw.android.exoplayer;

public class MyExoPlayer {

    /*
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    private Context mContext;
    private String mURL, mSubtitleURL;
    private Uri mUri, mSubtitleUri;
    protected String userAgent;
    private DataSource.Factory dataSourceFactory;
    private FrameworkMediaDrm mediaDrm;

    public MyExoPlayer(Context context, PlayerView playerView, String URL, String subtitleURL) {
        this.mContext = context;
        this.mPlayerView = playerView;
        this.mURL = URL;
        this.mSubtitleURL = subtitleURL;
        this.mUri = Uri.parse(mURL);
        this.mSubtitleUri = Uri.parse(mSubtitleURL);
    }

    public void initExoPlayer() {
        userAgent = Util.getUserAgent(mContext, mContext.getString(R.string.app_name));
        dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext);
        mPlayer.setPlayWhenReady(true);

        MediaSource videoSource = buildMediaSource(mUri, null);
        MediaSource subtitleSource = buildSubtitleSource(mSubtitleUri);
        MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);

        mPlayerView.setBackgroundColor(mContext.getResources().getColor(R.color.black));
        mPlayerView.setControllerShowTimeoutMs(3000);
        mPlayerView.setPlayer(mPlayer);
        mPlayer.prepare(mergedSource);
    }

    private DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(
            UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
            throws UnsupportedDrmException {
        HttpDataSource.Factory licenseDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
        HttpMediaDrmCallback drmCallback =
                new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        releaseMediaDrm();
        mediaDrm = FrameworkMediaDrm.newInstance(uuid);
        return new DefaultDrmSessionManager<>(uuid, mediaDrm, drmCallback, null, multiSession);
    }

    private void releaseMediaDrm() {
        if (mediaDrm != null) {
            mediaDrm.release();
            mediaDrm = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private MediaSource buildSubtitleSource(Uri uri) {
        // Build the subtitle MediaSource.
        Format subtitleFormat = Format.createTextSampleFormat(
                null, // An identifier for the track. May be null.
                MimeTypes.APPLICATION_SUBRIP, // The mime type. Must be set correctly.
                Format.NO_VALUE, // Selection flags for the track.
                null); // The subtitle language. May be null.

        return new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(uri, subtitleFormat, C.TIME_UNSET);
    }

    public void stop() {
        if (mPlayer != null)
            mPlayer.stop();
    }

    public void addListener(Player.EventListener eventListener) {
        mPlayer.addListener(eventListener);
    }

    public void removeListener(Player.EventListener eventListener) {
        mPlayer.removeListener(eventListener);
    }

    public void onRestart(long position, boolean isPlaying) {
        initExoPlayer();
        setSeekPosition(position);
        startAutoPlay(isPlaying);
    }

    public void destroy() {
        if (mPlayer != null)
            mPlayer.release();
    }

    public long getSeekPosition() {
        return mPlayer.getCurrentPosition();
    }

    public void setSeekPosition(long position) {
        mPlayer.seekTo(position);
    }

    public boolean getIsPlaying() {
        return mPlayer.getPlayWhenReady();
    }

    public void startAutoPlay(boolean isPlaying) {
        mPlayer.setPlayWhenReady(isPlaying);
    }
    */
}

