package com.mybatis.sample.blog.dao;

import java.util.List;

import com.mybatis.sample.blog.entity.Author;

public interface AuthorMapper {
	
	int insertAuthor(Author author);
	
	List<Author> selectAuthorByUsernameOrEmail(String username, String email);
}
