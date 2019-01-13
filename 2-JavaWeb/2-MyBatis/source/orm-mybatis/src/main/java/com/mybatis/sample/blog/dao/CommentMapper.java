package com.mybatis.sample.blog.dao;

import com.mybatis.sample.blog.entity.Comment;

public interface CommentMapper {
	int insertComment(Comment comment);
}
