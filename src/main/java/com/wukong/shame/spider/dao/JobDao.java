package com.wukong.shame.spider.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.wukong.shame.spider.entity.Job;

@Mapper
public interface JobDao {
	@Insert("insert into job(id,name,url,min_salary,max_salary,palce,experience,education) "
			+ "values (#{id},#{name},#{url},#{minSalary},#{maxSalary},#{palce},#{experience},#{education})")
	public void addJob(Job job);
	
	@Select("select url from job")
	public List<String> getUrlList();
}
