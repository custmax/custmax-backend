package com.custmax.officialsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.custmax.officialsite.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}