package com.asphalt.queries.data

data class Query(
    val title: String,
    val description: String,
    val categoryId: Int,
    val isAnswered: Boolean,
    val postedOn: String,
    val postedByName: String,
    val postedByUrl: String,
    val likeCount: Int,
    val answerCount: Int,
    val answers: List<Answer>
)

var bikeQueries: List<Query> = listOf(
    // ---------------------- 1. Maintenance ----------------------
    Query(
        title = "Best Chain Lube for Dusty Conditions?",
        description = "I ride mostly dry, dusty gravel. My current wet lube attracts too much grit. Should I switch to a dry wax-based lube, and if so, any brand recommendations?",
        categoryId = 1, // Maintenance
        isAnswered = true,
        postedOn = "2025-10-15T10:30:00Z", // Server Timestamp
        postedByName = "Alex Rodriguez",
        postedByUrl = "https://picsum.photos/seed/AR/100/100",
        likeCount = 55,
        answerCount = 2,
        answers = listOf(
            Answer(
                answeredByName = "Maria Chen",
                answeredByUrl = "https://picsum.photos/seed/MC/100/100",
                answeredOn = "2025-10-15T11:15:00Z", // Server Timestamp
                likeCount = 18,
                dislikeCount = 0,
                answer = "Wax is the way to go for dust. Look at **Squirt** or **Smoove**. It takes a few applications to fully replace the wet lube, but your drivetrain will stay much cleaner.",
                isMechanic = true
            )
        )
    ),
    Query(
        title = "SRAM vs. Shimano Brake Bleeding Kits?",
        description = "Need to bleed my Shimano MT200 brakes for the first time. Can I use a universal bleed kit, or is it better to buy the official Shimano kit? Are the procedures vastly different?",
        categoryId = 1, // Maintenance
        isAnswered = false,
        postedOn = "2025-10-12T14:45:00Z", // Server Timestamp
        postedByName = "Joseph Miller",
        postedByUrl = "https://picsum.photos/seed/JM/100/100",
        likeCount = 31,
        answerCount = 1,
        answers = listOf(
            Answer(
                answeredByName = "Timothy Lee",
                answeredByUrl = "https://picsum.photos/seed/TL/100/100",
                answeredOn = "2025-10-13T09:05:00Z", // Server Timestamp
                likeCount = 10,
                dislikeCount = 0,
                answer = "Use the Shimano funnel system! It's worth the investment for a beginner. Shimano uses mineral oil, while SRAM uses DOT fluid, so NEVER mix the fluids or tools."
            )
        )
    ),
    Query(
        title = "How Often Should I Replace Brake Pads?",
        description = "I ride road and put in about 100 miles a week. How do I know when my caliper brake pads are worn out, and is there a brand with better longevity?",
        categoryId = 1, // Maintenance
        isAnswered = true,
        postedOn = "2025-09-01T17:00:00Z", // Server Timestamp
        postedByName = "Clive Davies",
        postedByUrl = "https://picsum.photos/seed/CD/100/100",
        likeCount = 68,
        answerCount = 2,
        answers = listOf(
            Answer(
                answeredByName = "Sarah Smith",
                answeredByUrl = "https://picsum.photos/seed/SS/100/100",
                answeredOn = "2025-09-02T08:30:00Z", // Server Timestamp
                likeCount = 20,
                dislikeCount = 0,
                answer = "Look for the wear line indicator groove on the pad. If the groove is almost gone, replace them immediately. Longevity depends heavily on riding in wet/gritty conditions.",
                isMechanic = true

            )
        )
    ),

    // ---------------------- 2. Routes ----------------------
    Query(
        title = "Paved Multi-Day Cycling Routes near Denver?",
        description = "Looking for a 3-4 day paved cycling route, starting and ending near Denver. Prefer low-traffic roads or dedicated bike paths. Any recommendations for scenic loops?",
        categoryId = 2, // Routes
        isAnswered = false,
        postedOn = "2025-10-10T09:10:00Z", // Server Timestamp
        postedByName = "David Johnson",
        postedByUrl = "https://picsum.photos/seed/DJ/100/100",
        likeCount = 92,
        answerCount = 0,
        answers = emptyList()
    ),
    Query(
        title = "Are there any hidden singletrack trails around Lake Tahoe?",
        description = "I've ridden Flume Trail and Tahoe Rim Trail. Looking for something more technical and less crowded on the Nevada side for advanced mountain biking. Private trails or lesser-known loops welcome!",
        categoryId = 2, // Routes
        isAnswered = true,
        postedOn = "2025-10-08T11:20:00Z", // Server Timestamp
        postedByName = "Adam Peterson",
        postedByUrl = "https://picsum.photos/seed/AP/100/100",
        likeCount = 112,
        answerCount = 3,
        answers = listOf(
            Answer(
                answeredByName = "Frank Wilson",
                answeredByUrl = "https://picsum.photos/seed/FW/100/100",
                answeredOn = "2025-10-09T18:00:00Z", // Server Timestamp
                likeCount = 35,
                dislikeCount = 1,
                answer = "Try the trails around Kingsbury Grade (above Stateline). They connect into some steep, rocky lines that are technically challenging. Check TrailForks for 'Mott Canyon'."
            )
        )
    ),
    Query(
        title = "Tips for First Time Bike Packing in the Pacific Northwest?",
        description = "Planning a 5-day bike-packing trip along the Oregon Coast. What specific concerns (weather, water access, bears?) should I prepare for compared to interior US trips?",
        categoryId = 2, // Routes
        isAnswered = true,
        postedOn = "2025-08-20T20:15:00Z", // Server Timestamp
        postedByName = "Erica Nelson",
        postedByUrl = "https://picsum.photos/seed/EN/100/100",
        likeCount = 75,
        answerCount = 2,
        answers = listOf(
            Answer(
                answeredByName = "Leonard Green",
                answeredByUrl = "https://picsum.photos/seed/LG/100/100",
                answeredOn = "2025-08-21T07:45:00Z", // Server Timestamp
                likeCount = 28,
                dislikeCount = 0,
                answer = "Fog and mist are constant—bring waterproof bags, not just water-resistant ones. Bears are rare right on the coast, but strong winds are guaranteed. Plan your daily mileage conservatively."
            )
        )
    ),

    // ---------------------- 3. Gear ----------------------
    Query(
        title = "Helmet Ventilation vs. Aerodynamics for Road Racing?",
        description = "I'm buying a new road helmet. Should I prioritize a lightweight helmet with maximum vents, or a more aero helmet for local crits? How much difference does aero really make at 20mph?",
        categoryId = 3, // Gear
        isAnswered = true,
        postedOn = "2025-10-05T13:35:00Z", // Server Timestamp
        postedByName = "Frederick Allen",
        postedByUrl = "https://picsum.photos/seed/FA/100/100",
        likeCount = 15,
        answerCount = 3,
        answers = listOf(
            Answer(
                answeredByName = "Daniel Velez",
                answeredByUrl = "https://picsum.photos/seed/DV/100/100",
                answeredOn = "2025-10-06T12:00:00Z", // Server Timestamp
                likeCount = 10,
                dislikeCount = 1,
                answer = "If you're racing crits, go aero. The time savings are measurable, and the races are short enough that marginal ventilation loss isn't a huge factor."
            )
        )
    ),
    Query(
        title = "Recommendations for Budget-Friendly Power Meter?",
        description = "I want to start training with power but don't want to spend \$1000+. Are there any reliable crank-based or pedal-based power meters under \$500 that you recommend for road cycling?",
        categoryId = 3, // Gear
        isAnswered = true,
        postedOn = "2025-10-01T16:25:00Z", // Server Timestamp
        postedByName = "Oliver King",
        postedByUrl = "https://picsum.photos/seed/OK/100/100",
        likeCount = 78,
        answerCount = 4,
        answers = listOf(
            Answer(
                answeredByName = "Christopher Hayes",
                answeredByUrl = "https://picsum.photos/seed/CH/100/100",
                answeredOn = "2025-10-02T10:10:00Z", // Server Timestamp
                likeCount = 22,
                dislikeCount = 0,
                answer = "The 4iiii Precision (left-side crank) is excellent value and reliable. You can often find them on sale under \$350, and they are very easy to install."
            )
        )
    ),

    // ---------------------- 4. Safety ----------------------
    Query(
        title = "Visibility Tips for Night Commuting in Rain",
        description = "I commute late evening and live in a high-rain area. My current rear light isn't cutting it in downpours. What are the best, brightest, and most reliable safety lights for poor weather?",
        categoryId = 4, // Safety
        isAnswered = true,
        postedOn = "2025-09-28T19:50:00Z", // Server Timestamp
        postedByName = "Raymond Soto",
        postedByUrl = "https://picsum.photos/seed/RS/100/100",
        likeCount = 55,
        answerCount = 1,
        answers = listOf(
            Answer(
                answeredByName = "Nicholas Wu",
                answeredByUrl = "https://picsum.photos/seed/NW/100/100",
                answeredOn = "2025-09-29T06:00:00Z", // Server Timestamp
                likeCount = 18,
                dislikeCount = 0,
                answer = "Invest in a light with high lumens (100+) and a flash pattern specifically designed for daytime/rain visibility (e.g., Bontrager Flare RT). Also, consider a reflective vest or jacket to increase your profile."
            )
        )
    ),
    Query(
        title = "Easiest Way to Teach Kids Basic Hand Signals?",
        description = "My 7-year-old is starting to ride on the street with me. What's the best way to get them to reliably use turn signals without getting overwhelmed by traffic?",
        categoryId = 4, // Safety
        isAnswered = true,
        postedOn = "2025-09-25T15:15:00Z", // Server Timestamp
        postedByName = "Michael Thompson",
        postedByUrl = "https://picsum.photos/seed/MT/100/100",
        likeCount = 28,
        answerCount = 2,
        answers = listOf(
            Answer(
                answeredByName = "Elizabeth Young",
                answeredByUrl = "https://picsum.photos/seed/EY/100/100",
                answeredOn = "2025-09-26T14:30:00Z", // Server Timestamp
                likeCount = 15,
                dislikeCount = 0,
                answer = "Make it a game in a quiet area first! Use simple, distinct commands like 'Left Arm Out' and 'Right Arm Out' rather than just 'turn right.' Repetition is key."
            )
        )
    ),

    // ---------------------- 5. Other ----------------------
    Query(
        title = "Can I use Mountain Bike Shoes for Indoor Cycling?",
        description = "I bought a Peloton and have MTB shoes (SPD cleats). Can I use these on the Peloton, or do I need to buy dedicated road shoes (Delta cleats)?",
        categoryId = 5, // Other
        isAnswered = true,
        postedOn = "2025-09-20T12:05:00Z", // Server Timestamp
        postedByName = "Susan Garcia",
        postedByUrl = "https://picsum.photos/seed/SG/100/100",
        likeCount = 30,
        answerCount = 2,
        answers = listOf(
            Answer(
                answeredByName = "Jacob Harris",
                answeredByUrl = "https://picsum.photos/seed/JH/100/100",
                answeredOn = "2025-09-20T12:40:00Z", // Server Timestamp
                likeCount = 8,
                dislikeCount = 0,
                answer = "It depends on the pedal. If the Peloton has dual-sided pedals (SPD and Delta), yes. If it only has Delta, you'll need to swap the pedals to a dual-sided style to use your SPD (MTB) shoes."
            )
        )
    ),
    Query(
        title = "Best Way to Store Bikes in a Small Apartment?",
        description = "I have three bikes (road, gravel, and folding) and a tiny apartment. Any recommendations for wall mounts or floor stands that are minimalist and space-saving?",
        categoryId = 5, // Other
        isAnswered = false,
        postedOn = "2025-09-15T18:00:00Z", // Server Timestamp
        postedByName = "Wayne Cooper",
        postedByUrl = "https://picsum.photos/seed/WC/100/100",
        likeCount = 40,
        answerCount = 1,
        answers = listOf(
            Answer(
                answeredByName = "Laura White",
                answeredByUrl = "https://picsum.photos/seed/LW/100/100",
                answeredOn = "2025-09-16T11:00:00Z", // Server Timestamp
                likeCount = 12,
                dislikeCount = 0,
                answer = "Look into tension-pole racks (floor-to-ceiling). They hold two bikes vertically without drilling into the wall, which is perfect for rentals. Keep the folding bike in the closet."
            ),
            Answer(
                answeredByName = "Jacob Harris",
                answeredByUrl = "https://picsum.photos/seed/JH/100/100",
                answeredOn = "2025-09-20T12:40:00Z", // Server Timestamp
                likeCount = 8,
                dislikeCount = 0,
                answer = "It depends on the pedal. If the Peloton has dual-sided pedals (SPD and Delta), yes. If it only has Delta, you'll need to swap the pedals to a dual-sided style to use your SPD (MTB) shoes."
            )
        )
    )
)

