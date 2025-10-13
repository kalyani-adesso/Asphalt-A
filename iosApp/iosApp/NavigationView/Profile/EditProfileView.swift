//
//  EditProfileView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 09/10/25.
//

import SwiftUI

struct EditProfileView: View {
    @State var userName: String = ""
    @State var email: String = ""
    @State var model: String = ""
    @State private var currentPage = 0
    private let totalPages = 8
    var body: some View {
        VStack {
            ScrollView {
                VStack {
                    HStack {
                        ProfileTitleView(title:AppStrings.EditProfile.editProfile, subtitle: AppStrings.EditProfile.updateProfileInfo, icon: AppIcon.Profile.ride)
                            .padding(.horizontal,15)
                            .padding(.top,20)
                        Button(action: {}) {
                            Image(systemName: "xmark")
                                .resizable()
                                .frame(width: 14,height: 14)
                                .foregroundStyle(AppColor.richBlack)
                                .padding(.trailing,15)
                        }
                    }
                    ZStack(alignment: .bottomTrailing) {
                        AppImage.Welcome.bg2
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 92, height: 73)
                            .clipShape(Circle())
                            .overlay(Circle().stroke(Color.blue.opacity(0.2), lineWidth: 5))
                            .padding([.top, .bottom], 20)
                        Button(action: {}) {
                            AppIcon.Profile.camera
                                .resizable()
                                .frame(width: 38,height: 38)
                                .foregroundStyle(AppColor.richBlack)
                                .padding([.leading, .bottom],10)
                        }
                    }
                    
                    Group {
                        EditProfileFieldView(label: AppStrings.CreateAccountLabel.userName.localized, placeholder: AppStrings.SignUpPlaceholder.userName.localized, inputText: $userName, keyboardType: .default)
                        EditProfileFieldView(label: AppStrings.CreateAccountLabel.email.localized, placeholder: AppStrings.CreateAccountLabel.email.localized, inputText: $email, keyboardType: .emailAddress)
                        EditProfileFieldView(label: AppStrings.EditProfile.enterPhoneNumber, placeholder: AppStrings.EditProfile.enterNumber, inputText: $email, keyboardType: .numberPad)
                        EditProfileFieldView(label: AppStrings.EditProfile.emergencyContact, placeholder: AppStrings.EditProfile.enterNumber, inputText: $email, keyboardType: .numberPad)
                        EditProfileFieldView(label: AppStrings.EditProfile.drivingLicense, placeholder: AppStrings.EditProfile.enterNumber, inputText: $email, keyboardType: .default)
                        MechanicView()
                        HStack(spacing: 19) {
                            Button(action: {
                                
                            }) {
                                Text(AppStrings.EditProfile.cancel.uppercased())
                                    .frame(height: 60)
                                    .frame(maxWidth: .infinity)
                                    .background(AppColor.white)
                                    .foregroundStyle(AppColor.red)
                                    .font(KlavikaFont.bold.font(size: 16))
                                    .cornerRadius(15)
                                    .overlay(
                                        RoundedRectangle(cornerRadius: 15)
                                            .stroke(AppColor.red, lineWidth: 1)
                                    )
                            }
                            .padding(.bottom, 21)
                            ButtonView(title: AppStrings.EditProfile.saveChanges.uppercased(), onTap: {
                                
                            })
                        }
                    }
                    .padding()
                }
                .background(AppColor.listGray)
                .cornerRadius(10)
                .padding(EdgeInsets(top: 50, leading: 15, bottom: 150, trailing: 15))
            }
        }
        .frame(maxWidth: .infinity,maxHeight: .infinity)
        .background(AppColor.darkgray)
        .padding(.top,30)
    }
}

struct EditProfileFieldView: View {
    let label: String
    let placeholder: String
    @Binding var inputText: String
    var keyboardType: UIKeyboardType
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(label)
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.black)
            HStack {
                TextField(placeholder, text: $inputText)
                    .onChange(of: inputText) { value in
                        
                    }
                    .font(KlavikaFont.regular.font(size: 16))
                    .autocapitalization(.none)
                    .foregroundStyle(AppColor.richBlack)
                    .keyboardType(.emailAddress)
            }
            .padding()
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
            )
            .cornerRadius(10)
        }
    }
}

struct MechanicView: View {
    @State var isOn: Bool = false
    var body: some View {
        HStack(spacing: 11) {
            AppIcon.Profile.mechanic
                .resizable()
                .frame(width: 30, height: 30)
                .padding(.leading, 8)
            VStack(alignment: .leading, spacing: 4) {
                Text(AppStrings.EditProfile.markAsMechanic)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.purple)
                Text(AppStrings.EditProfile.responseHighlight)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
            }
            Spacer()
            Toggle("", isOn: $isOn)
                .frame(width: 44, height: 24)
                .padding(.trailing, 18)
        }
        .frame(height: 80)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .stroke(AppColor.whiteGray, lineWidth: 1)
        )
        .contentShape(Rectangle())
    }
}

#Preview {
    EditProfileView()
}
