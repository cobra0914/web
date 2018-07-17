package org.cobra.web.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cobra.web.bean.User;


public interface UserMapper {

    @Select(value = "select * from users where username = #{username}")
    User loadUserByUsername(@Param("username") String username);

    @Insert(value = "insert into users (username, password, enabled, create_date) value(#{username},#{password},#{enabled},#{createDate})")
    void saveUser(User user);
}
