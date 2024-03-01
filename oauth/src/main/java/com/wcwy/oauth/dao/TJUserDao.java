package com.wcwy.oauth.dao;


import com.wawy.company.api.TCompanyLoginApi;
import com.wcwy.company.entity.TCompany;
import com.wcwy.company.entity.TJobhunter;
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
public class TJUserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private TCompanyLoginApi tCompanyLoginApi;
    //根据账号查询用户信息
    public UserDto getUserByUsername(String username,String QRCode,String ip){
        String sql = "SELECT\n" +
                "\tcompany.t_jobhunter.user_id, \n" +
                "\tcompany.t_jobhunter.login_name, \n" +
                "\tcompany.t_jobhunter.user_name, \n" +
                "\tcompany.t_jobhunter.`status`, \n" +
                "\tcompany.t_jobhunter.`password`\n" +
                "FROM\n" +
                "\tcompany.t_jobhunter\n" +
                "WHERE\n" +
                "\tcompany.t_jobhunter.login_name = ?";
        //连接数据库查询用户
        List<TJobhunter> query = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper<>(TJobhunter.class));
        if(query.size()>0){
            TJobhunter tJobhunter = query.get(0);
            if(tJobhunter.getStatus().equals("1")){
                throw new InvalidClientException("用户已经被禁用");
            }
            UserDto userDto=new UserDto();
            userDto.setId(tJobhunter.getUserId());
            userDto.setUsername(tJobhunter.getUserName());
            userDto.setFullname(tJobhunter.getUserName());
            userDto.setMobile(tJobhunter.getLoginName());
            userDto.setPassword(tJobhunter.getPassword());
            return userDto;
        }else if(query.size()==0){
            TJobhunter tJobhunter = tCompanyLoginApi.insertCodeJobHunter(username,QRCode,ip);
            UserDto userDto=new UserDto();
            userDto.setId(tJobhunter.getUserId());
            userDto.setUsername(tJobhunter.getUserName());
            userDto.setFullname(tJobhunter.getUserName());
            userDto.setMobile(tJobhunter.getLoginName());
            userDto.setPassword(tJobhunter.getPassword());
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
                "\tcompany.t_role_permission.role_id IN (SELECT\n" +
                "\tcompany.t_jobhunter_role.role_id\n" +
                "FROM\n" +
                "\tcompany.t_jobhunter_role\n" +
                "WHERE\n" +
                "\tcompany.t_jobhunter_role.user_id = ?)\n" +
                "\t)";

        List<PermissionDto> list = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(PermissionDto.class));
        List<String> permissions = new ArrayList<>();
        list.forEach(c -> permissions.add(c.getCode()));
        return permissions;
    }
}
