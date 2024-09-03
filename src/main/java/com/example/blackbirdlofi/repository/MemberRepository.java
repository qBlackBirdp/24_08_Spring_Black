package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.vo.Member;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberRepository {

    @Select("""
            SELECT *
            FROM users
            WHERE email = #{email}
            """)
    Member findByEmail(String email);

    @Insert("""
            INSERT INTO users 
            (loginId, loginPw, u_name, nickname, email, regDate, updateDate) 
            VALUES 
            (#{loginId}, #{loginPw}, #{uName}, #{nickname}, #{email}, NOW(), NOW())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void doJoin(Member newMember);

    @Select("SELECT * FROM `users` WHERE id = #{id}")
    Member getMemberById(int id);
}
