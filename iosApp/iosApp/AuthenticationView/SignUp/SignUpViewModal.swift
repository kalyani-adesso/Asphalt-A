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
    var emailorPhoneNumber: String?
    func getEmailorPhoneNumber(emailorPhoneNumber: String, onSucess: @escaping () -> Void)  {
        Auth.auth().sendPasswordReset(withEmail: emailorPhoneNumber, completion: { error in
            if let error = error {
                print("Error sending password reset: \(error.localizedDescription)")
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
                } else {
                    // TODO: Show toast message
                    onSuccess()
                }
            }
            
        }
    }
}
