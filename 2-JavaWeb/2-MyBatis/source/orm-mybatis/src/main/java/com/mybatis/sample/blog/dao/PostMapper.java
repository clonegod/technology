package com.mybatis.sample.blog.dao;

import com.mybatis.sample.blog.entity.Post;

public interface PostMapper {
	int insertPost(Post post);
	
	Post selectPostById(int postId);
}
