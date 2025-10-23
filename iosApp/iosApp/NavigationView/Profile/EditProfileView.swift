//
//  EditProfileView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 09/10/25.
//

import SwiftUI
import PhotosUI

struct EditProfileView: View {
    @State var userName: String = ""
    @State var email: String = ""
    @State var phoneNumber: String = ""
    @State var emargeContact: String = ""
    @State var drivingLicenseNumber: String = ""
    @State var enableMechanic: Bool = false
    @StateObject var profileViewModel = ProfileViewModel()
    @State private var currentPage = 0
    @State private var showActionSheet: Bool = false
    @Binding var isPresented: Bool
    @State private var sourceType: UIImagePickerController.SourceType = .photoLibrary
    @State private var selectedImage: UIImage?
    @State private var isShowingImagePicker = false
    private let totalPages = 8
    var body: some View {
        VStack {
            ScrollView {
                VStack {
                    HStack {
                        ProfileTitleView(title:AppStrings.EditProfile.editProfile, subtitle: AppStrings.EditProfile.updateProfileInfo, icon: AppIcon.Profile.ride)
                            .padding(.horizontal,15)
                            .padding(.top,20)
                        Button(action: {
                            isPresented = false
                        }) {
                            Image(systemName: "xmark")
                                .resizable()
                                .frame(width: 14,height: 14)
                                .foregroundStyle(AppColor.richBlack)
                                .padding(.trailing,15)
                        }
                    }
                    ZStack(alignment: .bottomTrailing) {
                        Image(uiImage: ((selectedImage ?? UIImage(named:"icon-profile"))!))
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 92, height: 73)
                            .clipShape(Circle())
                            .overlay(Circle().stroke(Color.blue.opacity(0.2), lineWidth: 5))
                            .padding([.top, .bottom], 20)
                        Button(action: {
                            showActionSheet = true
                        }) {
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
                        EditProfileFieldView(label: AppStrings.EditProfile.enterPhoneNumber, placeholder: AppStrings.EditProfile.enterNumber, inputText: $phoneNumber, keyboardType: .numberPad)
                        EditProfileFieldView(label: AppStrings.EditProfile.emergencyContact, placeholder: AppStrings.EditProfile.enterNumber, inputText: $emargeContact, keyboardType: .numberPad)
                        EditProfileFieldView(label: AppStrings.EditProfile.drivingLicense, placeholder: AppStrings.EditProfile.enterNumber, inputText: $drivingLicenseNumber, keyboardType: .default)
                        MechanicView(isOn: $enableMechanic)
                        HStack(spacing: 19) {
                            ButtonView( title: "CANCEL",
                                        background: LinearGradient(
                                                    gradient: Gradient(colors: [.white, .white]),
                                                    startPoint: .leading,
                                                    endPoint: .trailing),
                                        foregroundColor: AppColor.darkRed,
                                        showShadow: false ,
                                        borderColor: AppColor.darkRed,
                                        onTap: {
                                isPresented = false
                            })
                            .padding(.bottom, 21)
                            ButtonView(title: AppStrings.EditProfile.saveChanges.uppercased(), onTap: {
                                profileViewModel.updateProfile(fullName: userName, email: email, phoneNumber: phoneNumber, emargencyContact: emargeContact, DL:drivingLicenseNumber , machanic:enableMechanic)
                                isPresented = false
                            }).disabled(profileViewModel.validateProfile(fullName: userName, email: email, phoneNumber: phoneNumber, emargencyContact: emargeContact, DL: drivingLicenseNumber))
                                .padding(.bottom, 21)
                        }
                    }
                    .padding()
                }
                .background(AppColor.listGray)
                .cornerRadius(10)
                .padding(EdgeInsets(top: 15, leading: 15, bottom: 150, trailing: 15))
            }
        }
        .frame(maxWidth: .infinity,maxHeight: .infinity)
        .background(AppColor.darkgray)
        .padding(.top,30)
        // TODO: Actual names are not finalized once get the actuall text to display then i will localize this.
        .confirmationDialog("Select Photo Source", isPresented: $showActionSheet) {
            Button("Take Photo") {
                sourceType = .camera
                isShowingImagePicker = true
            }
            Button("Choose from Gallery") {
                sourceType = .photoLibrary
                isShowingImagePicker = true
            }
            Button(AppStrings.EditProfile.cancel, role: .cancel) {}
        }
        .sheet(isPresented: $isShowingImagePicker) {
            ImagePicker(sourceType: sourceType, selectedImage: $selectedImage)
        }
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
    @Binding var isOn: Bool
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
