import Foundation

public enum CornerToken {
    case none

    /// Any card or box that contains other content.
    /// Examples: content cards, popups, modal views etc
    case background

    /// For images and visuals mixed between other types of elements, such as listings.
    /// Snackbar, info banners etc
    case content

    /// For photos of products
    case image

    /// Button, Input, Select, Notifications.
    case form

    /// Badge, Checkboxes, Chips
    case formSmall

    /// Fully rounded corners
    case full

    public var points: CGFloat {
        switch self {
        case .none: 0
        case .background: 16
        case .content: 12
        case .image: 8
        case .form: 8
        case .formSmall: 6
        case .full: 1000
        }
    }
}
