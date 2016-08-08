/* 
 * This is an example of how to record video in a simple way on your Android device. 
 */

package org.turntotech.videocapture;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

    Uri fileUri; // file url to store image/video

    VideoView videoPreview;
    Button btnRecordVideo;
    Button btnViewVideoInfo;
    String outputFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TurnToTech", "Project Name - VideoCapture");


        outputFilePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/myvideo.mp4";
        fileUri = Uri.fromFile(new File(outputFilePath));


        videoPreview = (VideoView) findViewById(R.id.videoPreview);
        btnRecordVideo = (Button) findViewById(R.id.btn_Record_Video);
        btnViewVideoInfo = (Button) findViewById(R.id.btn_Video_Info);
        btnViewVideoInfo.setEnabled(false);

        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                recordVideo();
            }
        });

        btnViewVideoInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getVideoInfo();
            }
        });


        videoPreview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                videoPreview.start();
                return true;
            }

        });
    }


    // Recording video
    private void recordVideo() {

        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera

            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            // set video quality
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

            // set the image file name
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the video capture Intent
            startActivityForResult(intent, 1);

        } else {
            // no camera on this device
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
        }

    }

    // Receiving activity result method will be called after closing the camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the result is available
        if (resultCode == RESULT_OK) {
            // video successfully recorded
            // preview the recorded video
            videoPreview.setVideoPath(fileUri.getPath());
            // start playing
            videoPreview.start();
            btnViewVideoInfo.setEnabled(true);
        }
    }

    private void getVideoInfo() {

        MediaMetadataRetriever m = new MediaMetadataRetriever();
        m.setDataSource(outputFilePath);

        String extractedHeight = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String extractedWidth = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String extractedDate = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        String extractedBitRate = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

        long durationMs = Long.parseLong(m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        long duration = durationMs / 1000;
        long hr = duration / 3600;
        long min = (duration - hr * 3600) / 60;
        long sec = duration - (hr * 3600 + min * 60);
        String durationValue;
        if (hr == 0) {
            durationValue = "Min: " + min + ", Sec: " + sec;
        } else {
            durationValue = "Hr: " + hr + ", Min: " + min + ", Sec: " + sec;
        }


        Toast.makeText(MainActivity.this,
                "Video Created: " + extractedDate, Toast.LENGTH_LONG).show();

        Toast.makeText(MainActivity.this,
                "Duration:\n" + durationValue, Toast.LENGTH_LONG).show();

        Toast.makeText(MainActivity.this,
                "Video Bit Rate: " + extractedBitRate + " Bits/Sec", Toast.LENGTH_LONG).show();

        Toast.makeText(MainActivity.this,
                "Video Height: " + extractedHeight + "\n" +
                        "Video Width: " + extractedWidth, Toast.LENGTH_LONG).show();

    }


}