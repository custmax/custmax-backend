package com.custmax.officialsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class  UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }
}