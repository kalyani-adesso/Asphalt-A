package com.asphalt.dashboard.sealedclasses

import com.asphalt.commonui.R
import com.asphalt.dashboard.constants.AdventureJourneyConstants

sealed class AdventureJourneyTimeFrameChoices(val choiceRes: Int, val choiceId: Int) {
    object LastFourMonths : AdventureJourneyTimeFrameChoices(
        R.string.last_four_months,
        AdventureJourneyConstants.CHOICE_LAST_FOUR_MONTHS
    )

    object LastSixMonths : AdventureJourneyTimeFrameChoices(
        R.string.last_six_months,
        AdventureJourneyConstants.CHOICE_LAST_SIX_MONTHS
    )

    object ThisMonth : AdventureJourneyTimeFrameChoices(
        R.string.this_month,
        AdventureJourneyConstants.CHOICE_THIS_MONTH
    )

    object LastYear : AdventureJourneyTimeFrameChoices(
        R.string.last_year,
        AdventureJourneyConstants.CHOICE_LAST_YEAR
    )

    object LastMonth : AdventureJourneyTimeFrameChoices(
        R.string.last_month,
        AdventureJourneyConstants.CHOICE_LAST_MONTH
    )

    companion object {
        fun getAllChoices(): List<AdventureJourneyTimeFrameChoices> {
            return listOf(
                LastMonth, ThisMonth, LastYear, LastFourMonths, LastSixMonths
            ).sortedBy {
                it.choiceId
            }
        }
    }

}