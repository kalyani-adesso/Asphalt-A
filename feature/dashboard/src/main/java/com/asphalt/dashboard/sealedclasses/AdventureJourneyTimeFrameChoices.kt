package com.asphalt.dashboard.sealedclasses

import com.asphalt.commonui.R
import com.asphalt.dashboard.constants.AdventureJourneyConstants

sealed class AdventureJourneyTimeFrameChoices(val choiceRes: Int, val choiceId: Int) {
    object LastFourMonths : AdventureJourneyTimeFrameChoices(
        R.string.last_four_months,
        AdventureJourneyConstants.CHOICE_LAST_FOUR_MONTHS
    )

    object LastWeek : AdventureJourneyTimeFrameChoices(
        R.string.last_week,
        AdventureJourneyConstants.CHOICE_LAST_WEEK
    )

    object LastYear : AdventureJourneyTimeFrameChoices(
        R.string.last_year,
        AdventureJourneyConstants.CHOICE_LAST_YEAR
    )

}