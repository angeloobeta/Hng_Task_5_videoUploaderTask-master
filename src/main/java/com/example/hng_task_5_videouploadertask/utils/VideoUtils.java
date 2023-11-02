package com.example.hng_task_5_videouploadertask.utils;

import com.example.hng_task_5_videouploadertask.data.dto.response.VideoResponseDto;
import com.example.hng_task_5_videouploadertask.data.entities.Video;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VideoUtils {
    public VideoResponseDto mapVideoToDto(Video video){
        return VideoResponseDto.builder()
                .timeStamp(video.getTimestamp())
                .fileUrl(video.getFileUrl())
                .fileId(String.valueOf(video.getId()))
                .fileName(video.getFilename())
                .fileSize(video.getFileSize())
                .transcription(video.getTranscriptionText())
                .build();
    }



        public static String saveVideoToDisk(InputStream fileInputStream, String filename) throws IOException {
            // Define the absolute path to the root directory of the server
            String videosFolder = "UploadedVideosDirectory"; // Replace with the actual path

            // Get the absolute path to the videos folder
            Path videosFolderPath = Paths.get(videosFolder).toAbsolutePath();

            if (!Files.exists(videosFolderPath)) {
                try {
                    Files.createDirectories(videosFolderPath);
                } catch (IOException e) {
                    // Handle the exception if directory creation fails
                    e.printStackTrace();
                    System.out.println("Failed to create the directory: "+ videosFolder);
                    throw e;
                }
            }

            // Generate a unique identifier for the video file
            String uniqueIdentifier = UUID.randomUUID().toString();
            String videoPath = Paths.get(videosFolderPath.toString(), LocalDateTime.now() + uniqueIdentifier + "_" + filename).toString();

            // Save the uploaded video file to the "UploadedVideoDirectory" folder.
            try (OutputStream videoOutputStream = new FileOutputStream(videoPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    videoOutputStream.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Video uploaded successfully");
//            System.out.println(beanConfig.apiKey);
            return videoPath;
        }


        public static String transcribeVideo(String videoPath) {
        // pass the api key transcription
//            OpenAiService service = new OpenAiService(beanConfig.apiKey);
            OpenAiService service = new OpenAiService("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");
            CreateTranscriptionRequest request = CreateTranscriptionRequest.builder()
                    .model("whisper-1")
                    .build();
            File file = new File(videoPath);
            //            String transcriptionSrtFile = service.createTranscription(request,file).getText();
            // store transcription file
//            String transcriptFilename = Paths.get(videoPath).getFileName().toString().replaceFirst("[.][^.]+$", "") + ".srt";
//            String transcriptPath = Paths.get("UploadedVideosDirectory", transcriptFilename).toString();
//
//            try (Writer transcriptWriter = new FileWriter(transcriptPath)) {
//                transcriptWriter.write(transcriptionSrtFile);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            return service.createTranscription(request,file).getText();
        }

    }




