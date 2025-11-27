//
//  ReusableHeader.swift
//  iosApp
//
//  Created by Lavanya Selvan on 27/11/25.
//

import SwiftUI

struct ReusableHeader<Leading: View, Trailing: View>: View {
    let leading: Leading
    let trailing: Trailing
    
    init(
        @ViewBuilder leading: () -> Leading,
        @ViewBuilder trailing: () -> Trailing
    ) {
        self.leading = leading()
        self.trailing = trailing()
    }
    
    var body: some View {
        HStack {
            leading
            Spacer()
            trailing
        }
        .padding(.horizontal)
        .padding(.vertical, 12)
    }
}

