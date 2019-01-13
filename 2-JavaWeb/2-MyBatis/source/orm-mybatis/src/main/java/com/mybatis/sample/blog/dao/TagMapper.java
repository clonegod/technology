package com.mybatis.sample.blog.dao;

import com.mybatis.sample.blog.entity.Tag;

public interface TagMapper {
	int insertTag(Tag tag);
	
	Tag selectAnyTag();
}
