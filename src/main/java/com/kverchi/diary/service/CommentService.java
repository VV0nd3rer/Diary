package com.kverchi.diary.service;

import com.kverchi.diary.domain.Comment;
import com.kverchi.diary.domain.ServiceResponse;

public interface CommentService {
	ServiceResponse addComment(Comment comment);

}
