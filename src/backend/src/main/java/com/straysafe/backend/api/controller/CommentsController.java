package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.request.CommentRequest;
import com.straysafe.backend.api.model.response.CommentResponse;
import com.straysafe.backend.service.CommentsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/public/comments/{reportId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getCommentsByReportId(@PathVariable String reportId) {
        return commentsService.getCommentsByReportId(reportId);
    }

    @PostMapping("/api/comments/post")
    @ResponseStatus(HttpStatus.CREATED)
    public void createReport(@Valid @RequestBody CommentRequest commentRequest) {
        commentsService.postComment(commentRequest);
    }

    @DeleteMapping("/api/comments/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@RequestParam String reportId, String commentId) {
        commentsService.deleteComment(reportId, commentId);
    }

}
