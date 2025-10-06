//
//  SignUpViewModal.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/10/25.
//

import SwiftUI
import Combine
import Firebase
import FirebaseAuth
import FirebaseFirestore

class SignUpViewModal: ObservableObject {
    @Published var showToast: Bool = false
    var emailorPhoneNumber: String?
    @Published var errorMessage: String?
    func getEmailorPhoneNumber(emailorPhoneNumber: String, onSucess: @escaping () -> Void)  {
        Auth.auth().sendPasswordReset(withEmail: emailorPhoneNumber, completion: { error in
            if let error = error {
                print("Error sending password reset: \(error.localizedDescription)")
                self.showToast = true
                self.errorMessage = error.localizedDescription
            } else {
                print("Password reset email sent.")
                onSucess()
            }
        })
        
    }
    
    func didTapSignUp(email: String, username: String,password: String, onSuccess: @escaping () -> Void) {
        Auth.auth().createUser(withEmail: email, password: password) { authResult, error in
            if let error = error {
                print("Error creating user: \(error.localizedDescription)")
                self.showToast = true
                self.errorMessage = error.localizedDescription
            } else {
                print("User created successfully!")
            }
            guard let uid = authResult?.user.uid else { return }
            let values = [
                "email": email,
                "userName": username
            ]
            Database.database().reference().child("users").child(uid).updateChildValues(values as [AnyHashable : Any]){ err, ref in
                if let err = err {
                    print("Error adding document: \(err)")
                    self.showToast = true
                    self.errorMessage = err.localizedDescription
                } else {
                    // TODO: Show toast message
                    onSuccess()
                }
            }
            
        }
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

}
