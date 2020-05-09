package com.example.happybaby.repo



import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import com.example.happybaby.entity.User;

@Repository("UserRepo")
interface UserRepo :JpaRepository<User,Long>{
   @Query(value = "SELECT * FROM User where id in (?1)", nativeQuery = true)
   open fun findAllByIds(@Param("ids") ids: String): List<User>?
   @Query(value = "SELECT * FROM User where ?1 = ?2", nativeQuery = true)
   open fun findEntityByEq(@Param("one") one: String, @Param("two") two: String):List<User>?


   fun findByName(name: String): User?
   fun deleteByName(name: String): Boolean
}
