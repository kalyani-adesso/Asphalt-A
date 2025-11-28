//
//  File.swift
//  DesignSystem
//
//  Created by Lavanya Selvan on 25/09/25.
//

import Foundation

public struct AppStrings{
    
    public enum WelcomeStrings {
        
        public static let getStarted = "GET STARTED"
        
        public static let welcomeTitle = "Welcome to the Joy\nof Riding"
        public static let welcomeSubtitle = "Connect with fellow motorcycle enthusiasts and discover amazing rides in your area."
        
        public static let communityTitle = "Community\nFeatures"
        public static let communitySubtitle = "Share knowledge, ask questions, and connect with mechanics and experienced riders."
        
        public static let rideTitle = "Ride Together\nSafely"
        public static let rideSubtitle = "Create group rides, track locations in real-time, and ensure everyone stays connected."
    }
    
    enum SignUpLabel: String {
 
        case continueButton = "CONTINUE"
        case welcome = "Create Your Account"
        case welcomeSubtitle = "Create your account to get started"
        case clubName = "adesso Rider’s Club"
        case emailOrPhone = "Email or Phone Number"
        case password = "Password"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    enum VerificationLabel: String {
        case verifyButton = "VERIFY ACCOUNT"
        case confirmation = "Confirm Your Email"
        case confirmationText = "We’ve sent 5 digits verification code to %@"
        case verifyTitle = "Enter Verification Code"
        case verificationCode = "Code"
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum CreateAccountLabel: String {
        case createTitle = "CREATE ACCOUNT"
        case userName = "User Name"
        case email = "Email Id"
        case password = "Password"
        case confirmPassword = "Confirm Password"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    enum SignUpPlaceholder: String {
 
        case email = "Enter your email"
        case userName = "Enter User Name"
        case otp = "Enter your OTP"
        case firstName = "First Name"
        case lastName = "Last Name"
        case password = "Enter your password"
        case confirmPassword = "Confirm Password"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInLabel: String {
        case signInTitle = "SIGN IN"
        case welcome = "Welcome"
        case welcomeSubtitle = "Let’s login for explore continues"
        case clubName = "adesso Rider’s Club"
        case emailOrPhone = "Email or Phone Number"
        case password = "Password"
        case confirmPassword = "Confirm Password"
        case updatePassword = "Update Password"
        case createNewPassword = "Create New Password"
        case enterYourNewPassword = "Enter your new password below."
        case backToSignIn = "Back to Sign In"
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInPlaceholder: String {
        case email = "Enter your email"
        case password = "Enter your password"
        case confirmPassword = "Confirm your Password"
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInToggle: String {
        case keepMeSignedIn = "Keep me signed in"
        case rememberMe = "Remember me"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInAction: String {
        case forgotPassword = "Forgot password"
        case signInButton = "SIGN IN"
        case signUpPrompt = "Don’t have an account?"
        case signUpAction = "Sign Up here"
        case connectWith = "You can connect with"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SignInSucessView: String {
        case loginSuccessTitle = "Yey! Login Successfull"
        case loginSuccessSubtitle = "You will be moved to home screen right now. Enjoy the features!"
       case exploreButton = "Lets Explore"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }


    enum AppStorageKey: String {
        case hasSeenOnboarding
    }

    enum ValidationMessage: String {
        case validatePassword = "Please enter a password"
        case validateEmail = "Please enter a valid email"
        case validateConfirmPassword = "Passwords do not match"
    }


    enum userdefaultKeys: String {
        case rememberMeData = "com.adesso.rider.club.rememberMeData"
        case hasSeenOnboarding = "com.adesso.rider.club.hasSeenOnboarding"
        case hasShownLoginSuccess = "com.adesso.rider.club.hasShownLoginSuccess"
        case userId = "com.adesso.rider.club.userId"
        case userName = "com.adesso.rider.club.userName"
        case rideId = "com.adesso.rider.club.rideId"
        case rideJoinedId = "com.adesso.rider.club.rideJoinedId"
    }
    
    enum ForgotPassword: String {
        case title = "Forgot Password?"
        case subtitle = "We’ll send you reset instructions"
        case forgotAction = "SEND RESET LINK"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum ResetSnackbarLabel: String {
        case title = "Reset link sent!"
        case subtitle = "Check your mail for password reset link"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum CreateAccountSnackbarLabel: String {
        case title = "Account Created Successfully!"
       
    }
    enum HomeLabel: String {
        case createRide = "CREATE A RIDE"
        case joinRide = "JOIN A RIDE"
        case rideDetails = "Total Rides"
        case location = "Locations"
        case totalKms = "Total Kms"
        case upcomingRides = "Upcoming Rides"
        case journey = "Adventure Journey"
        case chart = "Places Visited In Last 6 Months"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    enum JourneyChart: String {
        case rides = "Total Rides"
        case distance = "Distance Covered"
        case places = "Places Explored"
        case groups = "Ride Groups"
        case invites = "Ride Invites"
        case title = "Adventure Journey"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum Placesvisited: String {
        case title = "Places Visited In Last 7 Months"
    }

    enum HomeSnackbarLabel: String {
        case title = "Welcome!"
        case subtitle = "You have successfully logged into adesso Riders Club."
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum HomeButton: String {
        case accept = "ACCEPT"
        case decline = "DECLINE"

        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    enum CreateRideButton: String {
        
        case previous = "PREVIOUS"
        case next = "NEXT"
        case nextStep = "NEXT STEP"
        case create = "CREATE RIDE"
        case done = "DONE"
        
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }
    
    enum CreateRide {
        static let rideType = "Ride Type"
        static let rideTitle = "Ride Title"
        static let rideTitleLabel =  "Enter ride name..."
        static let description = "Description"
        static let descriptionLabel = "Describe the vibe..."
        static let startDate = "Start Date"
        static let startTime = "Time"
        static let endDate = "End Date"
        static let endTime = "Time"
        static let createRide = "Create a Ride"
        static let inviteContacts = "Invite Contacts"
        static let search = "Search by name ,number or bike type..."
        static let reviewSubTitle = "Review Your Ride"
        static let reviewDate = "Date and Time"
        static let reviewRoute = "Route"
        static let reviewParticipants =  "Participants"
        static let shareTitle =  "Share Link"
        static let shareSubTitle =  "Share Via"
        
    }

    enum Notification : String {
        case rideReminder = "Ride Reminder"
        case notifications = "Notifications"
        case newRiderJoined = "New Rider Joined"
        var localized: String { NSLocalizedString(self.rawValue, comment: "") }
    }

    enum SelectRide {
        static let selectingRide = "Selecting Your Ride"
        static let chooseType = "Choose your perfect motorcycle type"
        static let sportBikeDesc = "High-performance racing bikes"
        static let make = "Make"
        static let selectMake = "Select motorcycle"
        static let model = "Model"
        static let selectModel = "Select model"
        static let addVehicle = "ADD VEHICLE"
        
    }

    enum Profile {
        static let addBike = "+ ADD BIKE"
        static let yourVehicles = "Your Vehicles"
        static let description = "Motorcycles your own or ride"
        static let noVehicles = "No Vehicles added yet"
        static let addPrompt = "Add your motorcycle to get started!"
        static let totalStats = "Total Statistics"
        static let distanceCovered = "Distance Covered"
        static let totalRides = "Total Rides"
        static let placesExplored = "Places Explored"
        static let elevationGain = "Elevation Gain"
        static let achievements = "Achievements"
        static let badgesDescription = "Badges earned through community participation"
        static let noBadges = "No badges earned yet"
        static let earnBadgeTip = "Complete quizzes and participate in the community to earn your first badge!"
        static let startEarnBadge = "Start earning badges"
        static let yourgarage = "Your Garage"
        static let profileTitle = "Profile"
    }

    enum VehicleType: String, CaseIterable, Identifiable {
        case sportBike = "Sport Bike"
        case nakedBike = "Naked Bike"
        case touringBike = "Touring Bike"
        case adventureBike = "Adventure Bike"
        case cruiser = "Cruiser"
        case scooter = "Scooter"
        case electric = "Electric"
        
        var id: String { rawValue }
        
        var subtitle: String? {
            switch self {
            case .sportBike:
                return "High-performance racing bikes"
            default:
                return nil
            }
        }
        
        var constantValue: Int32 {
            switch self {
            case .sportBike: return 1
            case .nakedBike: return 2
            case .adventureBike: return 3
            case .cruiser: return 4
            case .touringBike: return 5
            case .scooter: return 6
            case .electric: return 7
            }
        }
        
        init?(constantValue: Int32) {
            switch constantValue {
            case 1: self = .sportBike
            case 2: self = .nakedBike
            case 3: self = .adventureBike
            case 4: self = .cruiser
            case 5: self = .touringBike
            case 6: self = .scooter
            case 7: self = .electric
            default: return nil
            }
        }
    }
    
  

    enum EditProfile {
        static let editProfile = "Edit Profile"
        static let updateProfileInfo = "Update your profile information & preferences"
        static let enterPhoneNumber = "Enter Phone number"
        static let enterNumber = "Enter number"
        static let emergencyContact = "Emergeny contact number"
        static let drivingLicense = "Driving license number"
        static let markAsMechanic = "Mark as mechanic"
        static let responseHighlight = "Your response will be highlighted"
        static let cancel = "Cancel"
        static let saveChanges = "Save changes"
    }

    enum NavigationSlider {
        // Main Section
        static let main = "Main"
        static let home = "Home"
        static let homeSubtitle = "Your riding dashboard"
        static let profile = "Profile"
        static let profileSubtitle = "Manage your info"
        static let rideHistory = "Your ride history"
        static let planAdventure = "Plan new adventure"
        static let referFriend = "Refer a friend"
        static let logout = "Logout"

        // Community Section
        static let community = "Community"
        static let connectedRide = "Connected Ride"
        static let joinGroupRides = "Join group rides"
        static let askAnswer = "Ask & answer"
        static let queries = "Queries"

        // Learning Section
        static let learning = "Learning"
        static let knowledgeCircle = "Knowledge Circle"
        static let learnRoadSigns = "Learn road signs"
        static let testSkills = "Test your skills"
        static let preRideCheck = "Pre Ride Check"

        // More Section
        static let more = "More"
        static let marketplace = "Marketplace"
        static let buySellGears = "Buy and sell gears"
        static let settings = "Settings"
        static let appPreferences = "App preferences"
    }
    
    enum UpcomingRide {
        static let yourRide = "Your Rides"
        static let share = "Share"
        static let viewPhotos = "View Photos"
        static let accept = "Accept"
    }
    
    enum JoinRide {
        static let searcRide = "Search rides by location, theme..."
        static let joinRide = "Join Ride"
        static let callRider = "Call Rider"
        static let joinaRideTitle = "Join a Ride"
        static let reJoinRideTitle = "ReJoin Ride"
        static let rideActive = "Ride Started! Navigation active."
        static let noActiveRidesFound = "No active rides found"
        static let confirmEndCurrentRide = "Do you want to end your current ride and join this one?"
        static let rideAlreadyActive = "Ride already active"
        static let loading = "Loading..."
        static let yes = "Yes"
        static let no = "No"
    }
    
    enum ConnectedRide {
        static let rideCompleted = "Ride successfully completed!"
        static let startRideTitle = "Starting Connected Ride"
        static let startRideSubtitle = "Initializing navigation and group cordination"
        static let completeRideTitle = "Completing Ride"
        static let completeRideSubtitle = "Saving your ride data and generating summary"
        static let connectedRide = "Connected Ride"
        static let rideSucess = "Ride completed sucessfully"
        static let rideDifficultyLevel = "Ride Difficulty Level"
        static let rateRide = "Rate This Ride"
        static let rideJoined = "Successfully joined the ride!"
        static let rideStarted = "Ride Started! Navigation active."
        static let connectedRideTitle = "Connected Ride"
        static let rideNameWeekendCoast = "Weekend Coast Ride"
        static let rideInProgressTitle = "Ride in Progress"
        static let groupNavigationActiveSubtitle = "Group navigation active"
        static let groupStatusTitle = "Group Status"
        static let emergencyActionsTitle = "Emergency Actions"
        static let offlineAlertTitle = "Abhishek has been stopped for 5 minutes."
        static let offlineAlertSubtitle = "Check if assistance is needed."
        static let endRideButton = "END RIDE"
        static let emergencySOSButton = "Emergency SOS"
        static let shareLocationButton = "Share Location"
        static let startTrackingButton = "Start Tracking"
        static let stopTrackingButton = "Stop Tracking"
        static let rideCompletedTitle = "Ride successfully completed"
        static let rideCompletionProgressTitle = "Completing ride"
        static let rideCompletionProgressSubtitle = "Saving your ride data and generating summary"
        static let standard = "Standard"
        static let rideMessage = "Completing ride"
        static let skip = "skip"
        static let submitRating = "Submit Rating"
    }
    
    enum Rating {
        static let rateTitle = "Rate Your Ride"
        static let rateSubTitle = "How was your experience on this ride?"
        static let rateFeedback = "Share your feedback (optional)"
        static let rateFeedbackLabel = "Tell us about your ride experience..."
    }
    
    enum Query {
        static let answerTitle = "Answer to Query"
        static let feedbackLabel = "Share your knowledge and help the community..."
        static let cancel = "Cancel"
        static let postAnswer = "Post Answer"
        static let selectMake = "Select motorcycle"
        static let model = "Model"
        static let selectModel = "Select model"
        static let searchLabel = "Search questions ..."
        static let emptyQuery = "No queries found"
        static let emptySubQuery = "Try changing filters or ask a new question."
        static let qsButton = "ASK QUESTION"
        static let qsTitle = "Question Title"
        static let qsTitleLabel = "Brief Description of the question.."
        static let qsCategory = "Category"
        static let qsDescription = "Description"
        static let qsDescriptionLabel = "Provide more details about your question..."
        static let qsCancel =  "CANCEL"
    }
    
    enum RidePopup {
        static let photo = "Upload Photos from Gallery"
        static let share = "Share your ride memories with the group"
        static let photoTitle = "Ride Photos"
        static let noPhoto = "No photos selected"
        static let cancel =  "CANCEL"
        static let addMore =  "ADD MORE"
    }
}


