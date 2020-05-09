package com.example.happybaby.repo



import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import com.example.happybaby.entity.Annex;

@Repository("AnnexRepo")
interface AnnexRepo :JpaRepository<Annex,Long>{
   @Query(value = "SELECT * FROM Annex where id in (?1)", nativeQuery = true)
   open fun findAllByIds(@Param("ids") ids: String): List<Annex>?
   @Query(value = "SELECT * FROM Annex where ?1 = ?2", nativeQuery = true)
   open fun findEntityByEq(@Param("one") one: String, @Param("two") two: String):List<Annex>?


   fun findByName(name: String): Annex?
   fun deleteByName(name: String): Boolean
}
