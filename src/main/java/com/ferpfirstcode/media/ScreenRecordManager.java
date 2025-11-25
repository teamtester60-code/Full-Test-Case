package com.ferpfirstcode.media;

import com.automation.remarks.video.RecorderFactory;
import com.automation.remarks.video.recorder.IVideoRecorder;
import com.automation.remarks.video.recorder.VideoRecorder;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.apache.commons.codec.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.Encoder;
import java.io.File;
import java.io.IOException;

public class ScreenRecordManager {
    public final static String RECORDINGS_PATH = "test-output/recordings/";
    private static final ThreadLocal<IVideoRecorder> recorder = new ThreadLocal<>();

    /**
     * Starts screen recording.
     */
    public static void startRecording() {
        if (PropertyReader.getProperty("recordTests").equalsIgnoreCase("true")) {
            try {
                // Ensure the recordings directory exists
                File recordingsDir = new File(RECORDINGS_PATH);
                if (!recordingsDir.exists()) {
                    recordingsDir.mkdirs();
                }
                // Configure the recorder to use the custom directory and file name
                if (PropertyReader.getProperty("executionType").equalsIgnoreCase("local")) {
                    recorder.set(RecorderFactory.getRecorder(VideoRecorder.conf().recorderType()));
                    // Start recording
                    recorder.get().start();
                    LogsManager.info("Recording Started");

                }


            } catch (Exception e) {
                LogsManager.error("Failed to start recording: " + e.getMessage());
            }

        }
    }

    /**
     * Stops screen recording and returns the video as an InputStream.
     */
    public static void stopRecording(String testMethodName) {
        try {
            if (recorder.get() != null) {
                // Stop the recorder and get the video file
                String videoFilePath = String.valueOf(recorder.get().stopAndSave(testMethodName));
                File videoFile = new File(videoFilePath);

                // Log the file path for debugging
                LogsManager.info("Video file saved at: " + videoFile.getAbsolutePath());


                // Convert the video to .mp4 format
                File mp4File = encodeRecording(videoFile);
                LogsManager.info("Recording Stopped and Converted to MP4: " + mp4File.getName());
            }
        } catch (Exception e) {
            LogsManager.error("Failed to stop recording: " + e.getMessage());
        } finally {
            recorder.remove();
        }
    }

    /**
     * Converts a video file to .mp4 format.
     *
     * @param sourceFile The input video file.
     * @return The converted .mp4 file.
     */
    private static File encodeRecording(File sourceFile) {
        File targetFile = new File(sourceFile.getParent(), sourceFile.getName().replace(".avi", ".mp4"));

        try {
            // Initialize audio attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac"); // AAC audio codec

            // Initialize video attributes
            VideoAttributes video = new VideoAttributes();
            video.setCodec("libx264"); // H.264 video codec

            // Set encoding attributes
            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setOutputFormat("mp4"); // Output format
            encodingAttributes.setAudioAttributes(audio);
            encodingAttributes.setVideoAttributes(video);

            // Encode the video
            Encoder encoder = new Encoder();
            try {
                encoder.encode(new MultimediaObject(sourceFile), targetFile, encodingAttributes);

                // Verify the output file was created
                if (targetFile.exists() && targetFile.length() > 0) {
                    // Delete the original file only if conversion was successful
                    if (sourceFile.delete()) {
                        LogsManager.info("Successfully deleted original AVI file: " + sourceFile.getName());
                    } else {
                        LogsManager.warn("Could not delete original AVI file: " + sourceFile.getAbsolutePath());
                    }
                } else {
                    throw new IOException("Failed to create output file or file is empty");
                }

            } catch (Exception e) {
                // Log specific error details
                LogsManager.error("Video conversion failed: " + e.getMessage());
                // Delete the target file if it was partially created
                if (targetFile.exists() && !targetFile.delete()) {
                    LogsManager.warn("Failed to delete partially converted file: " + targetFile.getAbsolutePath());
                }
                // Re-throw as a runtime exception if needed
                throw new RuntimeException("Video conversion failed", e);
            }

        } catch (Exception e) {
            LogsManager.error("Error during video encoding process: " + e.getMessage(), e.getMessage());
            // Return the source file if conversion fails
            return sourceFile;
        }

        return targetFile;
    }
}

//asfadfgag
