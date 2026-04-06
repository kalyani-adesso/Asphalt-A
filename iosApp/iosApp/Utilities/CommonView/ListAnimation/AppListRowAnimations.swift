//
//  AppListRowAnimations.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 03/04/26.
//  Shared staggered “list row” motion for dashboard, profile garage, joins, queries, etc.
//

import SwiftUI

enum AppListRowAnimations {
    static let spring = Animation.spring(response: 0.44, dampingFraction: 0.84)
    static let staggerStep: Double = 0.048

    static var rowTransition: AnyTransition {
        .asymmetric(
            insertion: .move(edge: .bottom).combined(with: .opacity),
            removal: .opacity
        )
    }
}

/// Staggered fade + slide-up when `visible` becomes true (e.g. onAppear).
struct StaggeredListRowModifier: ViewModifier {
    let index: Int
    let isVisible: Bool

    func body(content: Content) -> some View {
        content
            .opacity(isVisible ? 1 : 0)
            .offset(y: isVisible ? 0 : 12)
            .animation(
                AppListRowAnimations.spring.delay(Double(index) * AppListRowAnimations.staggerStep),
                value: isVisible
            )
    }
}

extension View {
    /// Per-row stagger when the list becomes visible (first layout / screen open).
    func staggeredListRow(index: Int, visible: Bool) -> some View {
        modifier(StaggeredListRowModifier(index: index, isVisible: visible))
    }

    /// Standard row transition + spring when the collection’s ids change.
    func dashboardListRowTransition() -> some View {
        transition(AppListRowAnimations.rowTransition)
    }
}
