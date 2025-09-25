import Foundation
import class UIKit.UIFont

enum Figma {
    enum FontTextSize {
        case size_900
        case size_800
        case size_700
        case size_600
        case size_500
        case size_400
        case size_300
        case size_200
        case size_100

        // Text Size as pixel float value
        var pixelValue: CGFloat {
            switch self {
            case .size_900: return 48
            case .size_800: return 40
            case .size_700: return 32
            case .size_600: return 22
            case .size_500: return 20
            case .size_400: return 18
            case .size_300: return 16
            case .size_200: return 14
            case .size_100: return 12
            }
        }
    }

    enum FontTextHeight {
        case height_900
        case height_800
        case height_700
        case height_600
        case height_500
        case height_400
        case height_300
        case height_200
        case height_100
        case height_50

        // Text Height as pixel float value
        var pixelValue: CGFloat {
            switch self {
            case .height_900: return 72
            case .height_800: return 64
            case .height_700: return 56
            case .height_600: return 48
            case .height_500: return 40
            case .height_400: return 32
            case .height_300: return 28
            case .height_200: return 24
            case .height_100: return 20
            case .height_50: return 16
            }
        }
    }

    enum FontTextWeight {
        case weight_900
        case weight_800
        case weight_700
        case weight_600
        case weight_500
        case weight_400
        case weight_300
        case weight_200
        case weight_100

        // Text Height as pixel float value
        var pixelValue: UIFont.Weight {
            switch self {
            case .weight_900: return .black
            case .weight_800: return .heavy
            case .weight_700: return .bold
            case .weight_600: return .semibold
            case .weight_500: return .medium
            case .weight_400: return .regular
            case .weight_300: return .light
            case .weight_200: return .ultraLight
            case .weight_100: return .thin
            }
        }
    }
}
