//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/10/25.
//

import Foundation
import SwiftUI
import shared

class LoginViewModel: ObservableObject {
    @Published var errorMessage: String?
    @Published var showToast: Bool = false
    func didTapLogin(email: String, password: String, completion: @escaping ()->(), errorCompletion: @escaping ()->()) {
        if isValidEmailAndPassword(email:email, password:password) {
            AuthenticatorImpl().signIn(email: email, password: password, completionHandler: { result,error in
                if let result = result {
                    if let _ = result.uid {
                        completion()
                    } else {
                        self.errorMessage = result.errorMessage ?? ""
                        self.showToast = true
                        errorCompletion()
                    }
                } else {
                    self.errorMessage = result?.errorMessage ?? ""
                    self.showToast = true
                    errorCompletion()
                }
            })
        } else {
            errorCompletion()
        }
    }
    
    func isValidPassword(_ password: String) -> Bool {
        return !password.isEmpty
    }
    
    func isValidEmailAndPassword(email: String, password: String) -> Bool {
        if email.isEmpty || !email.isValidEmail {
            errorMessage = AppStrings.ValidationMessage.validateEmail.rawValue
            self.showToast = true
            return false
        } else if password.isEmpty {
            self.showToast = true
            errorMessage = AppStrings.ValidationMessage.validatePassword.rawValue
            return false
        } else {
            self.showToast = false
            return email.isValidEmail && isValidPassword(password)
        }
    }
}
