//
//  SignUpViewModal.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/10/25.
//

import SwiftUI
import Combine
import shared

class SignUpViewModal: ObservableObject {
    @Published var showToast: Bool = false
    var emailorPhoneNumber: String?
    @Published var errorMessage: String?
    func getEmailorPhoneNumber(emailorPhoneNumber: String,password: String, confirmPassword: String, onSucess: @escaping () -> Void)  {
        
        if isValidEmailAndPassword(email: emailorPhoneNumber, password: password, confirmPassword: confirmPassword) {
            //        Auth.auth().sendPasswordReset(withEmail: emailorPhoneNumber, completion: { error in
            //            if let error = error {
            //                print("Error sending password reset: \(error.localizedDescription)")
            //                self.showToast = true
            //                self.errorMessage = error.localizedDescription
            //            } else {
            //                print("Password reset email sent.")
            //                onSucess()
            //            }
            //        })
        }
    }
    
    func didTapSignUp(email: String, username: String, password: String,confirmPassword:String, onSuccess: @escaping () -> Void) {
        let user = User(email: email, password: password, name: username, confirmPassword: confirmPassword)
        AuthenticatorImpl().signUp(user: user, completionHandler: { success, failure in
            if let _ = failure {
                self.showToast = true
            } else {
                onSuccess()
            }
        })
    }
    
    func checkEmailDomainExists(_ email: String) async -> Bool {
        guard let domain = email.split(separator: "@").last else {
            await MainActor.run {
                self.showToast = true
            }
            return false
        }
        
        guard let url = URL(string: "https://dns.google/resolve?name=\(domain)&type=MX") else {
            await MainActor.run {
                self.showToast = true
            }
            return false
        }
        
        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            guard let json = try JSONSerialization.jsonObject(with: data) as? [String: Any],
                  let answer = json["Answer"] as? [[String: Any]],
                  !answer.isEmpty else {
                await MainActor.run {
                    self.showToast = true
                }
                return false
            }
            return true
        } catch {
            await MainActor.run {
                self.showToast = true
            }
            return false
        }
    }
    
    func isValidPassword(_ password: String) -> Bool {
        return !password.isEmpty
    }
    
    func isValidEmailAndPassword(email: String, password: String, confirmPassword: String) -> Bool {
        if email.isEmpty || !email.isValidEmail {
            errorMessage = AppStrings.ValidationMessage.validateEmail.rawValue
            self.showToast = true
            return false
        } else if password.isEmpty {
            self.showToast = true
            errorMessage = AppStrings.ValidationMessage.validatePassword.rawValue
            return false
        } else if password != confirmPassword {
            errorMessage = AppStrings.ValidationMessage.validateConfirmPassword.rawValue
            self.showToast = true
            return false
        } else {
            self.showToast = false
            return email.isValidEmail && isValidPassword(password)
        }
    }
}
