package com.example.diviction.module.DAST.repository

import com.example.diviction.module.DAST.entity.Dast
import com.example.diviction.module.account.entity.Member
import org.springframework.data.jpa.repository.JpaRepository


interface DastRepository : JpaRepository<Dast,Long>{
    override fun getById(id: Long): Dast

    fun getAllByDastMember(member: Member) : List<Dast>
}
