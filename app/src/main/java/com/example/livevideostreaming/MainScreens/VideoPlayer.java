package com.example.livevideostreaming.MainScreens;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.livevideostreaming.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource; // Updated source
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.MediaItem;

public class VideoPlayer extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.exoPlayer);

        // Hide action bar and set full screen mode
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Get the video URL from the Intent, handle null or empty URL
        String videoFileUrl = getIntent().getStringExtra("videoFileUrl");
        if (videoFileUrl == null || videoFileUrl.isEmpty()) {
            // Show error if URL is not available
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        setUpVideo(videoFileUrl);
    }

    private void setUpVideo(String videoFileUrl) {
        // Initialize the ExoPlayer using the Builder pattern
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(simpleExoPlayer);

        // Create a DataSourceFactory
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "YourAppName"));

        // Build a ProgressiveMediaSource to handle video streaming
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(videoFileUrl)));

        // Prepare and start the player
        simpleExoPlayer.setMediaSource(mediaSource);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the player if the activity goes into the background
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false); // Pause video
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Release the player when the activity is stopped
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure proper cleanup of resources
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
        }
    }
}
