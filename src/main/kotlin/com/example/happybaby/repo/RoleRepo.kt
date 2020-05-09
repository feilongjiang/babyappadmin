package com.example.happybaby.repo



import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import com.example.happybaby.entity.Role;

@Repository("RoleRepo")
interface RoleRepo :JpaRepository<Role,Long>{
   @Query(value = "SELECT * FROM Role where id in (?1)", nativeQuery = true)
   open fun findAllByIds(@Param("ids") ids: String): List<Role>?
   @Query(value = "SELECT * FROM Role where ?1 = ?2", nativeQuery = true)
   open fun findEntityByEq(@Param("one") one: String, @Param("two") two: String):List<Role>?


   fun findByName(name: String): Role?
   fun deleteByName(name: String): Boolean
}
