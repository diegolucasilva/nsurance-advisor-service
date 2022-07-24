package com.dls.insuranceadvisorservice.domain.insurancelines;

import com.dls.insuranceadvisorservice.domain.RiskProfileLineInsurance
import com.dls.insuranceadvisorservice.domain.UserProfile
import org.junit.jupiter.api.Test
import java.time.LocalDate


class AutoInsuranceRiskScoreTest {

    private val autoInsuranceRiskScore= AutoInsuranceRiskScore()

    @Test
    fun `Given a user that doesn't have a vehicle, the auto insurance risk calculator should return the score status INELIGIBLE`() {
        //GIVEN
        val userProfile = UserProfile(
            age=30,
            dependents=1,
            income=10,
            maritalStatus = UserProfile.MaritalStatus.married,
            house = null,
            questionScore = 2,
            vehicle = null
        )
        //WHEN
        val riskProfileLineInsurance = autoInsuranceRiskScore.execute(userProfile)
        //THEN
        assert(riskProfileLineInsurance.finalScoreStatus == RiskProfileLineInsurance.FinalScoreStatus.INELIGIBLE)
    }

    @Test
    //If the user is under 30 years old, deduct 2 risk points from all lines of insurance.
    //If the user's vehicle was produced in the last 5 years, add 1 risk point to that vehicle’s score.
    fun `Given a user that is under 30 and has a vehicle produced in the last 5 years, the auto insurance risk calculator should return the score status REGULAR and less 1 score points`() {
        //GIVEN
        val userProfile = UserProfile(
            age=25,
            dependents=1,
            income=10,
            maritalStatus = UserProfile.MaritalStatus.married,
            house = null,
            questionScore = 3,
            vehicle = UserProfile.Vehicle(LocalDate.now().year)
        )
        //WHEN
        val riskProfileLineInsurance = autoInsuranceRiskScore.execute(userProfile)
        //THEN
        assert(riskProfileLineInsurance.score ==userProfile.questionScore-1)
        assert(riskProfileLineInsurance.finalScoreStatus == RiskProfileLineInsurance.FinalScoreStatus.REGULAR)
    }

    @Test
    //If the user is under 30 years old, deduct 2 risk points from all lines of insurance.
    //If the user's vehicle was produced in the last 5 years, add 1 risk point to that vehicle’s score.
    fun `Given a user that is under 30 and has a vehicle wasn't produced in the last 5 years, the auto insurance risk calculator should return the score status REGULAR and less 2 score points`() {
        //GIVEN
        val userProfile = UserProfile(
            age=25,
            dependents=1,
            income=10,
            maritalStatus = UserProfile.MaritalStatus.married,
            house = null,
            questionScore = 3,
            vehicle = UserProfile.Vehicle(LocalDate.now().year-10)
        )
        //WHEN
        val riskProfileLineInsurance = autoInsuranceRiskScore.execute(userProfile)
        //THEN
        assert(riskProfileLineInsurance.score ==userProfile.questionScore-2)
        assert(riskProfileLineInsurance.finalScoreStatus == RiskProfileLineInsurance.FinalScoreStatus.REGULAR)
    }

    @Test
    //If the user is between 30 and 40 years old, deduct 1.
    //If the user's vehicle was produced in the last 5 years, add 1 risk point to that vehicle’s score.
    fun `Given a user that is between 30 and 40 years old and has a vehicle wasn't produced in the last 5 years, the auto insurance risk calculator should return the score status RESPONSIBLE  and less 1 score point`() {
        //GIVEN
        val userProfile = UserProfile(
            age=35,
            dependents=1,
            income=10,
            maritalStatus = UserProfile.MaritalStatus.married,
            house = null,
            questionScore = 3,
            vehicle = UserProfile.Vehicle(LocalDate.now().year-6)
        )
        //WHEN
        val riskProfileLineInsurance = autoInsuranceRiskScore.execute(userProfile)
        //THEN
        assert(riskProfileLineInsurance.score ==userProfile.questionScore-1)
        assert(riskProfileLineInsurance.finalScoreStatus == RiskProfileLineInsurance.FinalScoreStatus.REGULAR)
    }

    @Test
    //User is between 30 and 40 years old, deduct 1.
    //User's vehicle was produced in the last 5 years, add 1 risk point to that vehicle’s score.
    //User's income is above $200k, deduct 1 risk point from all lines of insurance.
    fun `Given a user that is between 30 and 40 years old, that has a vehicle produced in the last 5 years and income above $200k, the auto insurance risk calculator should return the score status RESPONSIBLE and less 1 score point`() {
        //GIVEN
        val userProfile = UserProfile(
            age=35,
            dependents=1,
            income=200001,
            maritalStatus = UserProfile.MaritalStatus.married,
            house = null,
            questionScore = 3,
            vehicle = UserProfile.Vehicle(LocalDate.now().year-4)
        )
        //WHEN
        val riskProfileLineInsurance = autoInsuranceRiskScore.execute(userProfile)
        //THEN
        assert(riskProfileLineInsurance.score ==userProfile.questionScore-1)
        assert(riskProfileLineInsurance.finalScoreStatus == RiskProfileLineInsurance.FinalScoreStatus.REGULAR)
    }
}