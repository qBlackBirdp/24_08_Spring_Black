package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.vo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberRepository {

    @Select("""
            SELECT email
            FROM users
            WHERE email = #{email}
            """)
    Member findByEmail(String email);

    @Insert("""
            INSERT INTO users 
            (loginId, loginPw, u_name, nickname, email, regDate, updateDate) 
            VALUES 
            (#{loginId}, #{loginPw}, #{name}, #{nickname}, #{email}, #{regDate}, #{updateDate})
            """)
    void doJoin(Member newMember);
}
