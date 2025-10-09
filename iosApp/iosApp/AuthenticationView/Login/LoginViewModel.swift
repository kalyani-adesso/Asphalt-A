//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/10/25.
//

import Foundation
import SwiftUI
import FirebaseAuth

class LoginViewModel: ObservableObject {
    @Published var errorMessage: String?
    @Published var showToast: Bool = false
    func didTapLogin(email: String, password: String, completion: @escaping ()->()) {
        Auth.auth().signIn(withEmail: email, password: password) {result,error in
            if self.isValidEmailAndPassword(email: email, password: password) {
                if let result = result {
                    print("User Signed In: \(result)")
                    if !result.user.uid.isEmpty {
                        completion()
                    }
                }
                if let error = error {
                    print("Error Signing In: \(error)")
                    self.errorMessage = error.localizedDescription
                    self.showToast = true
                }
            }
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
