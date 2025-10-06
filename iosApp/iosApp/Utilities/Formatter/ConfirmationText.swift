//
//  ConfirmationText.swift
//  iosApp
//
//  Created by Lavanya Selvan on 06/10/25.
//

import Foundation
import SwiftUI

class ConfirmationText: ObservableObject{
    
    @Published var email = ""
    
     var formattedConfirmationText: Text {
        let parts = AppStrings.VerificationLabel.confirmationText.rawValue
            .components(separatedBy: "%@")
        let prefix = parts.first ?? ""
        let suffix = parts.count > 1 ? parts.last! : ""
        
        return Text(prefix)
            .foregroundColor(AppColor.stoneGray)
        + Text(email)
            .foregroundColor(AppColor.celticBlue)
            .fontWeight(.semibold)
        + Text(suffix)
            .foregroundColor(AppColor.stoneGray)
    }
}
