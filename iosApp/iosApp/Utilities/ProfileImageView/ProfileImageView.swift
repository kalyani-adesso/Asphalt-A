//
//  ProfileImageView.swift
//  iosApp
//
//  Displays a profile image from asset name, base64 string, or URL (API profilePicUrl).
//

import SwiftUI
import UIKit

// MARK: - Base64 decoding (handles raw base64 or data:image/...;base64,...)
enum ProfileImageDecoder {
    static func image(fromBase64OrDataURL string: String) -> UIImage? {
        let base64: String
        if string.hasPrefix("data:"), let range = string.range(of: "base64,") {
            base64 = String(string[range.upperBound...])
        } else {
            base64 = string
        }
        guard !base64.isEmpty, let data = Data(base64Encoded: base64) else { return nil }
        return UIImage(data: data)
    }
}

// MARK: - Profile image view (asset name, URL, or base64)
struct ProfileImageView: View {
    /// Profile image source: bundle asset name, http(s) URL, or base64 (optional data URL prefix).
    let profileImageName: String?
    var placeholder: Image = AppIcon.Profile.profile
    var size: CGSize = CGSize(width: 37, height: 37)

    private var isEmpty: Bool {
        guard let s = profileImageName, !s.trimmingCharacters(in: .whitespaces).isEmpty else { return true }
        return false
    }

    private var isURL: Bool {
        guard let s = profileImageName else { return false }
        return s.hasPrefix("http://") || s.hasPrefix("https://")
    }

    var body: some View {
        Group {
            if isEmpty {
                placeholder
                    .resizable()
            } else if isURL, let url = URL(string: profileImageName!) {
                AsyncImage(url: url) { phase in
                    switch phase {
                    case .success(let image):
                        image.resizable().scaledToFill()
                    case .failure, .empty:
                        placeholder.resizable()
                    @unknown default:
                        placeholder.resizable()
                    }
                }
                .frame(width: size.width, height: size.height)
                .clipped()
            } else if let uiImage = ProfileImageDecoder.image(fromBase64OrDataURL: profileImageName!) {
                Image(uiImage: uiImage)
                    .resizable()
                    .scaledToFill()
            } else {
                // Treat as bundle asset name
                Image(profileImageName!)
                    .resizable()
                    .scaledToFill()
            }
        }
        .frame(width: size.width, height: size.height)
        .clipShape(Circle())
    }
}
