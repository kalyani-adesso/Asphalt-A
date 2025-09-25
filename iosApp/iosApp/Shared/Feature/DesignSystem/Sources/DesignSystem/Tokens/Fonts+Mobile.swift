import SwiftUI
import UIKit

/// ## Fonts Mappings
///
/// Based on the text size, the mapping is applied to Apple text styles to ensure better scaling curves when increasing text size using accessibility dynamic text size.
///
/// ```
/// Apple TextStyle   | Font Size | MDE TextStyles
/// ----------------- | --------- | --------------
/// largeTitle        | 700       | PageTitle
/// extraLargeTitle   |           |
/// extraLargeTitle2  |           |
/// title1            | 600       | Headline Large
/// title2            | 500       | Headline Hero / Headline
/// title3            |           |
/// headline          |           |
/// subheadline       |           |
/// body              | 300       | Title / Label Large / Copy Large
/// callout           | 200       | Subtitle / Label / Copy
/// footnote          |           |
/// caption1          | 100       | Meta
/// caption2          |           |
/// ```
///
/// MDE TextStyle source: https://www.figma.com/design/NuT6RRWcu9XI8cDc7omqgJ/MDE-UI?node-id=17577-83070&t=k0jYKrU1SlePXkRR-0
/// Apple TextStyle source: https://developer.apple.com/design/human-interface-guidelines/ios/visual-design/typography/

public enum FontToken {
    case KlavikaBold
    case KlavikaLight
    case KlavikaMedium
    case KlavikaRegular
    public var attributes: FontAttributes {
        switch self {
        case .KlavikaBold:
            FontAttributes(
                family: .KlavikaBold,
                size: .size_600,
                lineHeight: .height_200,
                weight: .weight_700,
                style: .normal,
                textStyle: .title1
            )
            
        case .KlavikaLight:
            FontAttributes(
                family: .KlavikaBold,
                size: .size_600,
                lineHeight: .height_400,
                weight: .weight_500,
                style: .normal,
                textStyle: .title1
            )
            
        case .KlavikaMedium:
            FontAttributes(
                family: .KlavikaMedium,
                size: .size_500,
                lineHeight: .height_300,
                weight: .weight_500,
                style: .normal,
                textStyle: .title2
            )
            
        case .KlavikaRegular:
            FontAttributes(
                family: .KlavikaRegular,
                size: .size_500,
                lineHeight: .height_300,
                weight: .weight_700,
                style: .normal,
                textStyle: .title2
            )
        }
    }

    public var fontName: String {
        let fontWeightSuffix = switch attributes.weight {
        case .black: "Black"
        case .heavy: "Heavy"
        case .bold: "Bold"
        case .semibold: "Semibold"
        case .medium: "Medium"
        case .regular:
            switch attributes.style {
            case .normal: "Regular"
            case .italic: ""
            }
        case .light: "Light"
        case .thin: "Thin"
        case .ultraLight: "UltraLight"
        default: "Regular"
        }

        let fontStyleSuffix = switch attributes.style {
        case .normal: ""
        case .italic: "Italic"
        }

        let fontName = "\(attributes.fontFamily.baseFontName)-\(fontWeightSuffix)\(fontStyleSuffix)"

        guard FontLoader.fonts.contains(fontName) else {
            assertionFailure("Attempt to use font \(fontName) which is not available! Failing back to default font, but it might result in unexpected UI layout. Please contact design team to solve this issue. Available solutions: chose a different font, use one of the font tokens or add the missing font file to the DesignSystem module.")
            return "\(attributes.fontFamily.baseFontName)-Regular"
        }

        return fontName
    }
}

public struct FontAttributes {
    public enum FontFamily {
        case KlavikaBold
        case KlavikaLight
        case KlavikaMedium
        case KlavikaRegular
       
        var baseFontName: String {
            switch self {
            case .KlavikaBold: return "Klavika-Bold"
            case .KlavikaLight: return "Klavika-Light"
            case .KlavikaMedium: return "Klavika-Medium"
            case .KlavikaRegular: return "Klavika-Regular"
            }
        }
    }

    public let fontFamily: FontFamily
    public let size: CGFloat
    public let lineHeight: CGFloat
    public let weight: UIFont.Weight
    public let style: FontStyle
    public let textStyle: UIFont.TextStyle

    @available(iOS 14.0, *)
    public var swiftUITextStyle: Font.TextStyle {
        switch textStyle {
        case .largeTitle: return .largeTitle
        case .title1: return .title
        case .title2: return .title2
        case .title3: return .title3
        case .headline: return .headline
        case .subheadline: return .subheadline
        case .body: return .body
        case .callout: return .callout
        case .caption1: return .caption
        case .caption2: return .caption2
        case .footnote: return .footnote
        default: return .body
        }
    }

    init(size: CGFloat, lineHeight: CGFloat, weight: UIFont.Weight, style: FontStyle, textStyle: UIFont.TextStyle) {
        self.fontFamily = .KlavikaRegular
        self.size = size
        self.lineHeight = lineHeight
        self.weight = weight
        self.style = style
        self.textStyle = textStyle
    }

    init(
        family: FontFamily,
        size: Figma.FontTextSize,
        lineHeight: Figma.FontTextHeight,
        weight: Figma.FontTextWeight,
        style: FontStyle,
        textStyle: UIFont.TextStyle
    ) {
        self.fontFamily = family
        self.size = size.pixelValue
        self.lineHeight = lineHeight.pixelValue
        self.weight = weight.pixelValue
        self.style = style
        self.textStyle = textStyle
    }
}

public enum FontStyle {
    case normal
    case italic
}

enum FontLoader {
    nonisolated(unsafe) private static var isFontRegistered: Bool = false

    static let fonts = [
        "Klavika-Bold",
        "Klavika-Light",
        "Klavika-Medium",
        "Klavika-Regular"
    ]

    static func loadPackageFonts() {
        if isFontRegistered { return }

        for font in fonts {
            registerFont(bundle: .module, fontName: font, fontExtension: "otf")
        }
        isFontRegistered = true
    }

    private static func registerFont(bundle: Bundle, fontName: String, fontExtension: String) {
        guard let fontURL = bundle.url(forResource: fontName, withExtension: fontExtension) else {
            fatalError("Couldn't create font \(fontName) from data")
        }

        var error: Unmanaged<CFError>?

        CTFontManagerRegisterFontsForURL(fontURL as CFURL, .process, &error)
    }
}

public extension UIFont {
    static func font(token: FontToken) -> UIFont {
        FontLoader.loadPackageFonts()

        let baseFont = UIFont(name: token.fontName, size: token.attributes.size)!

        // Ensure the font scales properly
        let scaledFont = UIFontMetrics(forTextStyle: token.attributes.textStyle).scaledFont(for: baseFont)

        return scaledFont
    }
}

@available(iOS 13.0, *)
public extension Font {
    init(token: FontToken) {
        self.init(UIFont.font(token: token))
    }
}

public extension UIFont {

    static var KlavikaBold: UIFont {
        UIFont.font(token: .KlavikaBold)
    }

    static var KlavikaLight: UIFont {
        UIFont.font(token: .KlavikaLight)
    }


    static var KlavikaMedium: UIFont {
        UIFont.font(token: .KlavikaMedium)
    }

    static var KlavikaRegular: UIFont {
        UIFont.font(token: .KlavikaRegular)
    }
}

// @available(*, deprecated, message: "Use Font(token: .label) or .styleText(token: .label) instead")
@available(iOS 13.0, *)
public extension Font {
    static var KlavikaBold: UIFont {
        UIFont.font(token: .KlavikaBold)
    }

    static var KlavikaLight: UIFont {
        UIFont.font(token: .KlavikaLight)
    }


    static var KlavikaMedium: UIFont {
        UIFont.font(token: .KlavikaMedium)
    }

    static var KlavikaRegular: UIFont {
        UIFont.font(token: .KlavikaRegular)
    }
}
