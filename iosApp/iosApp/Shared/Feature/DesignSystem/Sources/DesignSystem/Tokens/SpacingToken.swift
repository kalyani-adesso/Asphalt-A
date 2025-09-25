import Foundation
import UIKit

public enum SpacingToken: Int, CaseIterable, Identifiable {
    /// 0 points
    case none = 0
    /// 2 points
    case xxs = 2
    /// 4 points
    case xs = 4
    /// 8 points
    case s = 8
    /// 12 points
    case sm = 12
    /// 16 points
    case m = 16
    /// 24 points
    case l = 24
    /// 32 points
    case xl = 32
    /// 48 points
    case xxl = 48
    /// 64 points
    case xxxl = 64

    /// spacings that grow with the font, switchable by debug flag. This switching will stay in place till we figured out if it should
    /// return scaled or not. Or remove this var entirely to force devs to explicitly decide whether a space is scaled or not.
    public var points: CGFloat {
        scaledPoints
    }

    /// you can use this for spacings that do not scale with the font.
    public var fixedPoints: CGFloat {
        CGFloat(rawValue)
    }

    /// you can use this for spacings that scale with the font.
    public var scaledPoints: CGFloat {
        if UserDefaults.standard.bool(forKey: "tweaks_dynamic_spacing_enabled") {
            return UIFontMetrics.default.scaledValue(for: CGFloat(rawValue))
        } else {
            return fixedPoints
        }
    }

    public var id: Int {
        rawValue
    }
}
