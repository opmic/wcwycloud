package com.wcwy.oauth.dao;


import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TRecommend;
import com.wcwy.oauth.model.PermissionDto;
import com.wcwy.oauth.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 **/
@Repository
public class RMUserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //根据账号查询用户信息
    public UserDto getUserByUsername(String username){
        String sql = "SELECT\n" +
                "\tcompany.t_recommend.id, \n" +
                "\tcompany.t_recommend.username, \n" +
                "\tcompany.t_recommend.login_name, \n" +
                "\tcompany.t_recommend.`password`, \n" +
                "\tcompany.t_recommend.`status`\n" +
                "FROM\n" +
                "\tcompany.t_recommend\n" +
                "WHERE\n" +
                "\tcompany.t_recommend.login_name =?";
        //连接数据库查询用户
        List<TRecommend> query = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(TRecommend.class));
        if(query.size()>0){
            TRecommend recommend = query.get(0);
            if(recommend.getStatus().equals("1")){
                throw new InvalidClientException("用户已经被禁用");
            }
            UserDto userDto=new UserDto();
            userDto.setId(recommend.getId());
            userDto.setUsername(recommend.getCompanyName());
            userDto.setFullname(recommend.getCompanyName());
            userDto.setMobile(recommend.getLoginName());
            userDto.setPassword(recommend.getPassword());
            return userDto;
        }
       return null;
    }

    //根据用户id查询用户权限
    public List<String> findPermissionsByUserId(String userId){
        String sql = "SELECT\n" +
                "\tcompany.t_permission.*\n" +
                "FROM\n" +
                "\tcompany.t_permission\n" +
                "WHERE\n" +
                "\tcompany.t_permission.id IN (\n" +
                "\tSELECT\n" +
                "\tcompany.t_role_permission.permission_id\n" +
                "FROM\n" +
                "\tcompany.t_role_permission\n" +
                "WHERE\n" +
                "\tcompany.t_role_permission.role_id IN (\n" +
                "\tSELECT\n" +
                "\tcompany.t_recommend_role.role_id\n" +
                "FROM\n" +
                "\tcompany.t_recommend_role\n" +
                "WHERE\n" +
                "\tcompany.t_recommend_role.recommend_id =?))";

        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));
        List<String> permissions = new ArrayList<>();
        list.forEach(c -> permissions.add(c.getCode()));
        return permissions;
    }
}
