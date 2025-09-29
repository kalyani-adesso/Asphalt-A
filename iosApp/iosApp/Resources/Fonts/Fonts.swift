//
//  Fonts.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 29/09/25.
//

import Foundation
import SwiftUI

enum KlavikaFont: String {
    case bold = "Klavika-Bold"
    case light = "Klavika-Light"
    case medium = "Klavika-Medium"
    case regular = "Klavika-Regular"

    func font(size: CGFloat) -> Font {
        Font.custom(self.rawValue, size: size)
    }
}
