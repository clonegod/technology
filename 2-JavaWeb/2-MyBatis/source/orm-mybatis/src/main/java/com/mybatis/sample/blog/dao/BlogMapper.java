package com.mybatis.sample.blog.dao;

import com.mybatis.sample.blog.entity.Blog;

public interface BlogMapper {
	int insertBlog(Blog blog);
	
	Blog selectBlogDetails(int id);
}
