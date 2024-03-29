package com.example.diviction.module.account.service

import com.example.diviction.infra.gcp.GCP_URLs
import com.example.diviction.infra.gcp.GcpStorageService
import com.example.diviction.module.account.dto.MatchResponseDto
import com.example.diviction.module.account.dto.ResponseCounselorDto
import com.example.diviction.module.account.dto.ResponseMemberDto
import com.example.diviction.module.account.entity.Counselor
import com.example.diviction.module.account.entity.Member
import com.example.diviction.module.account.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val gcpStorageService: GcpStorageService
    ){
    fun getMemberById(id : Long) : ResponseMemberDto{
        val member = memberRepository.getById(id)

        return member.toResponseDto()
    }

    fun getMemberByEamil(email : String) : ResponseMemberDto
    {
        val cur = memberRepository.findByEmail(email)

        if(cur.isPresent)
        {
            val member = cur.get()

            return member.toResponseDto()
        }
        else{
            throw RuntimeException("해당 이메일의 사용자는 존재하지 않습니다.")
        }
    }

    fun getMatchById(id : Long) : MatchResponseDto?
    {
        var cur = memberRepository.findById(id)

        if(cur.isPresent)
        {
            var member = cur.get()
            var match =  member.matching

            if(match!=null)
            {
                return MatchResponseDto(matchId = match.id, counselor = match.counselor.toResponseDto(), member = match.patient.toResponseDto())
            }
        }

        return null
    }

    fun deleteMember(id : Long)
    {
        memberRepository.deleteById(id)
    }

    fun Member.toResponseDto() : ResponseMemberDto = ResponseMemberDto(id!!,email, password, name, birth, address, gender, profile_img_url)
    fun Counselor.toResponseDto() : ResponseCounselorDto = ResponseCounselorDto(id!!,email, password, name, birth, address, gender, profile_img_url, confirm)

    fun getAllMember() : List<ResponseMemberDto>
    {
        val members = memberRepository.findAll()
        val list : MutableList<ResponseMemberDto> = mutableListOf()

        members.forEach {
            list.add(it.toResponseDto())
        }

        return list
    }

    fun updateMemberImg(
        memberId : Long,
        multipartFile: MultipartFile?
    )
    {
        val member = memberRepository.getById(memberId)
        if (multipartFile == null) {
            member.profile_img_url = GCP_URLs.PATIENT_BASIC_IMG_URL
        } else {
            member.profile_img_url = gcpStorageService.uploadFileToGCS(multipartFile)
        }
    }
}
