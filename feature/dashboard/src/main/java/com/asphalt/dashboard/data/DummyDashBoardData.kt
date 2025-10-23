package com.asphalt.dashboard.data


val dummyRideDataList = listOf(
    RidePerMonthDto(RideDateDto(1, 2024), PerMonthRideStatisticsDto(12, 20, 8500)),
    RidePerMonthDto(RideDateDto(2, 2024), PerMonthRideStatisticsDto(8, 15, 6200)),
    RidePerMonthDto(RideDateDto(3, 2024), PerMonthRideStatisticsDto(15, 22, 9400)),
    RidePerMonthDto(RideDateDto(4, 2024), PerMonthRideStatisticsDto(10, 18, 7100)),
    RidePerMonthDto(RideDateDto(5, 2024), PerMonthRideStatisticsDto(18, 26, 12000)),
    RidePerMonthDto(RideDateDto(6, 2024), PerMonthRideStatisticsDto(9, 14, 5400)),
    RidePerMonthDto(RideDateDto(7, 2024), PerMonthRideStatisticsDto(20, 28, 13000)),
    RidePerMonthDto(RideDateDto(8, 2024), PerMonthRideStatisticsDto(16, 24, 10200)),
    RidePerMonthDto(RideDateDto(9, 2024), PerMonthRideStatisticsDto(7, 11, 4300)),
    RidePerMonthDto(RideDateDto(10, 2024), PerMonthRideStatisticsDto(14, 21, 9000)),
    RidePerMonthDto(RideDateDto(11, 2024), PerMonthRideStatisticsDto(5, 9, 3700)),
    RidePerMonthDto(RideDateDto(12, 2024), PerMonthRideStatisticsDto(11, 17, 8200)),

    RidePerMonthDto(RideDateDto(1, 2025), PerMonthRideStatisticsDto(25, 40, 10000)),
    RidePerMonthDto(RideDateDto(2, 2025), PerMonthRideStatisticsDto(3, 5, 2000)),
    RidePerMonthDto(RideDateDto(3, 2025), PerMonthRideStatisticsDto(10, 15, 7900)),
    RidePerMonthDto(RideDateDto(4, 2025), PerMonthRideStatisticsDto(1, 2, 500)),
    RidePerMonthDto(RideDateDto(5, 2025), PerMonthRideStatisticsDto(8, 12, 680)),
    RidePerMonthDto(RideDateDto(6, 2025), PerMonthRideStatisticsDto(4, 8, 800)),
    RidePerMonthDto(RideDateDto(7, 2025), PerMonthRideStatisticsDto(2, 8, 160)),
    RidePerMonthDto(RideDateDto(8, 2025), PerMonthRideStatisticsDto(6, 4, 200)),
    RidePerMonthDto(RideDateDto(9, 2025), PerMonthRideStatisticsDto(2, 1, 20)),
    RidePerMonthDto(RideDateDto(10, 2025), PerMonthRideStatisticsDto(1, 1, 180)),
)
val dummyRideInvites = listOf(
    // 1. Alice's Invite
    DashboardRideInvite(
        inviterName = "Johnson Benoit",
        inviterProfilePicUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=600&h=600&fit=crop&crop=face",
        startPoint = "Kanyakumari",
        destination = "Athirapally Waterfalls",
        dateTime = "11/10/2025 18:30",
        inviteesProfilePicUrls = listOf(
            "https://images.unsplash.com/photo-1507003211169-0a816d557620?w=600&h=600&fit=crop&crop=face",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=600&h=600&fit=crop&crop=face",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=600&h=600&fit=crop&crop=face",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=600&h=600&fit=crop&crop=face",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=600&h=600&fit=crop&crop=face",
        )
    ),

    // 2. Bob's Invite
    DashboardRideInvite(
        inviterName = "Shiva Karthikeyan",
        inviterProfilePicUrl = "https://images.unsplash.com/photo-1507003211169-0a816d557620?w=600&h=600&fit=crop&crop=face",
        startPoint = "Trivandrum",
        destination = "Kozhikode",
        // Tomorrow, 07:45 -> dd/MM/yyyy HH:mm
        dateTime = "12/10/2025 07:45",
        inviteesProfilePicUrls = listOf(
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=600&h=600&fit=crop&crop=face"
        )
    ),

    // 3. Cathy's Invite
    DashboardRideInvite(
        inviterName = "Christy",
        inviterProfilePicUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=600&h=600&fit=crop&crop=face",
        startPoint = "Kochi",
        destination = "Bangalore",
        // Next Friday, 17:00 (assuming next Friday is 17/10/2025) -> dd/MM/yyyy HH:mm
        dateTime = "17/10/2025 17:00",
        inviteesProfilePicUrls = listOf(
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&h=600&fit=crop&crop=face",
            "https://images.unsplash.com/photo-1531427186808-54b15099ac5e?w=600&h=600&fit=crop&crop=face",
            "https://images.unsplash.com/photo-1494790108377-be9c29b29329?w=600&h=600&fit=crop&crop=face"
        )
    ),

    // 4. David's Invite
    DashboardRideInvite(
        inviterName = "David",
        inviterProfilePicUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&h=600&fit=crop&crop=face",
        startPoint = "Delhi",
        destination = "Kodaikanal",
        // In 2 days, 13:00 (i.e., 13/10/2025) -> dd/MM/yyyy HH:mm
        dateTime = "13/10/2025 13:00",
        inviteesProfilePicUrls = emptyList()
    )
)

val adventureJourneyDummyDataList = listOf(
    AdventureTimeFrameChoiceDto(1, AdventureJourneyDto(12, 20, 20, 5)),
    AdventureTimeFrameChoiceDto(2, AdventureJourneyDto(28, 50, 17, 12)),
    AdventureTimeFrameChoiceDto(3, AdventureJourneyDto(65, 40, 15, 120)),
    AdventureTimeFrameChoiceDto(4, AdventureJourneyDto(44, 12, 4, 20)),
    AdventureTimeFrameChoiceDto(5, AdventureJourneyDto(78, 21, 14, 24))

)

val placesVisitedDummyData = listOf(
    PlacesVisitedGraphDto(1, 2024, 12),
    PlacesVisitedGraphDto(2, 2024, 1),
    PlacesVisitedGraphDto(3, 2024, 19),
    PlacesVisitedGraphDto(4, 2024, 33),
    PlacesVisitedGraphDto(5, 2024, 27),
    PlacesVisitedGraphDto(6, 2024, 41),
    PlacesVisitedGraphDto(7, 2024, 0),
    PlacesVisitedGraphDto(8, 2024, 29),
    PlacesVisitedGraphDto(9, 2024, 37),
    PlacesVisitedGraphDto(10, 2024, 22),
    PlacesVisitedGraphDto(11, 2024, 31),
    PlacesVisitedGraphDto(12, 2024, 0),
    PlacesVisitedGraphDto(1, 2025, 26),
    PlacesVisitedGraphDto(2, 2025, 5),
    PlacesVisitedGraphDto(3, 2025, 15),
    PlacesVisitedGraphDto(4, 2025, 20),
    PlacesVisitedGraphDto(5, 2025, 23),
    PlacesVisitedGraphDto(6, 2025, 35),
    PlacesVisitedGraphDto(7, 2025, 0),
    PlacesVisitedGraphDto(8, 2025, 28),
    PlacesVisitedGraphDto(9, 2025, 31),
    PlacesVisitedGraphDto(10, 2025, 19)
)
