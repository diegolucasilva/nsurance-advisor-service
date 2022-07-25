package com.dls.insuranceadvisorservice.adapter.controller

import com.dls.insuranceadvisorservice.adapter.dto.RiskProfileResponse
import com.dls.insuranceadvisorservice.adapter.dto.UserPersonalInformationRequest
import com.dls.insuranceadvisorservice.adapter.dto.toDomain
import com.dls.insuranceadvisorservice.adapter.dto.toResponse
import com.dls.insuranceadvisorservice.adapter.port.RiskScoreProcessorUseCase
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@Validated
@RestController
class InsuranceAdvisorController(val riskScoreProcessorUseCase: RiskScoreProcessorUseCase) {

    @PostMapping("/insurance/advisor")
    @ResponseStatus(HttpStatus.CREATED)
    fun createRiskProfile(@Valid @RequestBody userPersonalInformationRequest: UserPersonalInformationRequest): RiskProfileResponse? {
         val customerProfile = userPersonalInformationRequest.toDomain()
         return riskScoreProcessorUseCase.execute(customerProfile).toResponse()
    }

}