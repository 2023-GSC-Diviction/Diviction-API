package com.example.diviction.module.account.service

import com.example.diviction.module.account.dto.MatchDto
import com.example.diviction.module.account.dto.MatchResponseDto
import com.example.diviction.module.account.entity.Matching
import com.example.diviction.module.account.entity.Member
import com.example.diviction.module.account.repository.CounselorRepository
import com.example.diviction.module.account.repository.MatchRepository
import com.example.diviction.module.account.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchService(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val counselorRepository: CounselorRepository,
    @Autowired private val matchRepository: MatchRepository
) {

    fun patientDuplication(id : Long) : Boolean
    {
        return matchRepository.existsByPatient(memberRepository.getById(id))
    }


    @Transactional
    fun saveMatch(matchDto : MatchDto) : MatchResponseDto
    {
        var curPatient = memberRepository.findById(matchDto.patientId)
        var curCounselor = counselorRepository.findById(matchDto.counselorId)

        if(curPatient.isPresent&&curCounselor.isPresent)
        {
            var patient = curPatient.get()
            var counselor = curCounselor.get()

            if(patientDuplication(patient.id!!))
            {
                throw RuntimeException()
            }
            else{
                var match : Matching = Matching(patient = patient,counselor = counselor)
                patient.matching = match
                counselor.matching_list.add(match)
                val cur = matchRepository.save(match)

                return MatchResponseDto(cur.id,cur.counselor.id,cur.counselor.email,cur.patient.id,cur.counselor.email)
            }
        }

        else{
            throw RuntimeException()
        }
    }

    fun getMatchByMatchingId(id :Long) : MatchResponseDto
    {
        var cur = matchRepository.findById(id)

        if(cur.isPresent)
        {
            var match = cur.get()

            return MatchResponseDto(matchId = match.id, counselorId = match.counselor.id ,counselorEmail = match.counselor.email,patientId = match.patient.id ,patientEmail = match.patient.email)
        }

        else{
            throw RuntimeException()
        }
    }

    fun getAllMatch() : List<MatchResponseDto>
    {
        val match = matchRepository.findAll()

        val list : MutableList<MatchResponseDto> = mutableListOf()

        match.forEach {
            list.add(MatchResponseDto(it.id,it.counselor.id,it.counselor.email,it.patient.id,it.patient.email))
        }

        return list
    }

    fun deleteMatch(id : Long)
    {
        matchRepository.deleteById(id)
    }
}
