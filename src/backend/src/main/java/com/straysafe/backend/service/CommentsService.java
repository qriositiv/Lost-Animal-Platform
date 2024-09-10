package com.straysafe.backend.service;

import com.fasterxml.uuid.Generators;
import com.straysafe.backend.api.model.exception.ReportCreationException;
import com.straysafe.backend.api.model.request.CommentRequest;
import com.straysafe.backend.api.model.response.CommentResponse;
import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.domain.CommentDAORequest;
import com.straysafe.backend.domain.CommentDAOResponse;
import com.straysafe.backend.repository.CommentsRepository;
import com.straysafe.backend.util.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {
    private final CommentsRepository commentsRepository;

    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public List<CommentResponse> getCommentsByReportId(String reportId) {
        List<CommentDAOResponse> commentDAOResponses = commentsRepository.getCommentsByReportId(reportId);

        return commentDAOResponses.stream()
                .map(commentDAOResponse -> new CommentResponse(
                        commentDAOResponse.commentId(),
                        commentDAOResponse.reportId(),
                        commentDAOResponse.username(),
                        commentDAOResponse.role(),
                        commentDAOResponse.comment(),
                        commentDAOResponse.createdAt()
                )).toList();
    }

    public void postComment(CommentRequest commentRequest) {
        try {
            UserCredentialResponse userData = (UserCredentialResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String commentUUID = Generators.timeBasedGenerator().generate().toString();

            CommentDAORequest commentDAORequest = convertCommentRequestIntoCommentDAORequest(
                    userData.getId(),
                    commentRequest.reportId(),
                    commentUUID,
                    commentRequest.comment()
            );
            System.out.println(commentDAORequest);


            commentsRepository.postCommentUnderReport(commentDAORequest);
        } catch (Exception e) {
            throw new ReportCreationException("Failed to post a comment " + e.getMessage());
        }


    }

    public void deleteComment(String reportId, String commentId) {
        UserCredentialResponse apiIssuerData = (UserCredentialResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String reportOwnerId = null;
        try{
            reportOwnerId = commentsRepository.getOwnerByReportId(reportId, commentId);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

        System.out.println("hello");
        if (!isOwner(reportOwnerId, apiIssuerData)) {
            throw new IllegalArgumentException("You are not allowed to delete this comment");
        } else {
            commentsRepository.deleteCommentByReportId(reportId, commentId);
        }

    }

    private CommentDAORequest convertCommentRequestIntoCommentDAORequest(
            String userId, @NotNull(message = "Report id is null") String reportId,
            String commentId, @NotBlank(message = "Comment is required") String comment) {
        return new CommentDAORequest(
                userId,
                reportId,
                commentId,
                comment
        );
    }

    private boolean isOwner(String reportOwnerId, UserCredentialResponse apiIssuerData) {
        if (reportOwnerId.equals(apiIssuerData.getId()) ||
                apiIssuerData.getRole().equals(Role.MODERATOR) ||
                apiIssuerData.getRole().equals(Role.SUPERUSER)) {
            return true;
        }
        return false;
    }
}
