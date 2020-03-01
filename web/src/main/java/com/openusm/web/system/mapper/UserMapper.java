package com.openusm.web.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openusm.web.system.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

//    @Results(id="stuMap",value = {
//            @Result(property = "id",column = "Id"),
//            @Result(property = "name",column = "SName"),
//            @Result(property = "age",column = "Age"),
//            @Result(property = "classId",column = "ClassID"),
//            @Result(property = "studentClass",column = "ClassID",one = @One(select = "com.lyb.springmybatisdemo.mapper.StudentClassMapper.selectById"))
//    })
    @Select({"SELECT * FROM sys_user WHERE USERNAME=#{username}"})
    User getUserByUsername(@Param("username") String username);
}
