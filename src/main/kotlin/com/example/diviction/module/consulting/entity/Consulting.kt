package com.example.diviction.module.consulting.entity

import com.example.diviction.module.account.entity.Counselor
import com.example.diviction.module.account.entity.Member
import com.fasterxml.jackson.annotation.JsonFormat
import lombok.Getter
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Entity
@Getter
@Table(name ="consulting")
class Consulting(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val consultPatient : Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_id",nullable = false)
    val consultCounselor : Counselor,

    @field: NotBlank
    var content : String,

    @field: JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var date : LocalDate
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null

}
