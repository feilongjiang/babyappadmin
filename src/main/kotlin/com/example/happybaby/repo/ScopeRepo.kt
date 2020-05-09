package com.example.happybaby.repo



import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import com.example.happybaby.entity.Scope;

@Repository("ScopeRepo")
interface ScopeRepo :JpaRepository<Scope,Long>{
   @Query(value = "SELECT * FROM Scope where id in (?1)", nativeQuery = true)
   open fun findAllByIds(@Param("ids") ids: String): List<Scope>?
   @Query(value = "SELECT * FROM Scope where ?1 = ?2", nativeQuery = true)
   open fun findEntityByEq(@Param("one") one: String, @Param("two") two: String):List<Scope>?


}
