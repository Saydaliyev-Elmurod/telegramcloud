package com.example.tgcloud.controller;

import com.example.tgcloud.MyTelegramBot;
import com.example.tgcloud.enums.FileType;
import com.example.tgcloud.model.DocumentEntity;
import com.example.tgcloud.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedAudio;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedDocument;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedPhoto;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.cached.InlineQueryResultCachedVideo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
public class InlineQueryController {
    private final DocumentService documentService;
    private final MyTelegramBot myTelegramBot;
    public void start(Update update) {
        InlineQuery inlineQuery = update.getInlineQuery();
        Long userId = inlineQuery.getFrom().getId();
        if(!inlineQuery.getQuery().isEmpty()){
            sendInlineQueryToUser(userId,inlineQuery.getId(),inlineQuery.getQuery());
        }
    }

    public void sendInlineQueryToUser(Long userId, String queryId, String query) {
        // Process the inline query and generate results
        List<InlineQueryResult> results = new ArrayList<>();
        List<DocumentEntity> documentEntityList = documentService.getDocQuery(query, userId);
        for (DocumentEntity document : documentEntityList) {
            if (document.getType().equals(FileType.VIDEO)) {
                InlineQueryResultCachedVideo videoResult = new InlineQueryResultCachedVideo();
                videoResult.setId(document.getId() + "");
                videoResult.setTitle(document.getName());
                videoResult.setVideoFileId(document.getFileId()); // Set the file ID of the video
                results.add(videoResult);
            } else if (document.getType().equals(FileType.DOCUMENT)) {
                InlineQueryResultCachedDocument cachedDocument = new InlineQueryResultCachedDocument();
                cachedDocument.setId(document.getId() + "");
                cachedDocument.setDocumentFileId(document.getFileId());
                cachedDocument.setTitle(document.getName());
                results.add(cachedDocument);
            } else if (document.getType().equals(FileType.AUDIO)) {
                InlineQueryResultCachedAudio cachedDocument = new InlineQueryResultCachedAudio();
                cachedDocument.setId(document.getId() + "");
                cachedDocument.setAudioFileId(document.getFileId());
                cachedDocument.setCaption(document.getName());
                results.add(cachedDocument);
            } else if (document.getType().equals(FileType.PHOTO)) {
                InlineQueryResultCachedPhoto cachedDocument = new InlineQueryResultCachedPhoto();
                cachedDocument.setId(document.getId() + "");
                cachedDocument.setPhotoFileId(document.getFileId());
                cachedDocument.setTitle(document.getName());
                results.add(cachedDocument);
            }
        }
        AnswerInlineQuery sendInlineQuery = new AnswerInlineQuery();
        sendInlineQuery.setInlineQueryId(queryId);
        sendInlineQuery.setResults(Collections.unmodifiableList(results));
        // Send the inline query to the user
        myTelegramBot.send(sendInlineQuery);
    }
}
