import Foundation
import SwiftUI

public enum ShadowToken: String, CaseIterable {
    case none

    /// Very light shadow that almost mimics a border line
    case level1

    /// Default shadow for elevated elements: header, overlay, snackbar etc
    case level2

    /// Default shadow for elevated elements appearing from bottom [not used in iOS]
    case level2Rotate

    /// [not used in iOS]
    case level3
}
