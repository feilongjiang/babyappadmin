package com.example.happybaby.repo



import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import com.example.happybaby.entity.Catergory;

@Repository("CatergoryRepo")
interface CatergoryRepo :JpaRepository<Catergory,Long>{
   @Query(value = "SELECT * FROM Catergory where id in (?1)", nativeQuery = true)
   open fun findAllByIds(@Param("ids") ids: String): List<Catergory>?
   @Query(value = "SELECT * FROM Catergory where ?1 = ?2", nativeQuery = true)
   open fun findEntityByEq(@Param("one") one: String, @Param("two") two: String):List<Catergory>?


   fun findByName(name: String): Catergory?
   fun deleteByName(name: String): Boolean
}
