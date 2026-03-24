//
//  ConnectedRideSessionController.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 24/03/26.
//

import Foundation

@MainActor
final class ConnectedRideSessionController: ObservableObject {
    private var uiTimer: Timer?
    private var heartbeatTimer: Timer?

    func startUiTimer(onTick: @escaping () -> Void) {
        uiTimer?.invalidate()
        uiTimer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
            onTick()
        }
    }

    func startHeartbeat(
        interval: TimeInterval = 10,
        onTick: @escaping () -> Void
    ) {
        heartbeatTimer?.invalidate()
        heartbeatTimer = Timer.scheduledTimer(withTimeInterval: interval, repeats: true) { _ in
            onTick()
        }
    }

    func tryJoinWhenLocationReady(
        maxRetries: Int = 4,
        retryDelay: TimeInterval = 1.5,
        locationProvider: @escaping () -> (lat: Double, long: Double, isValid: Bool),
        joinAction: @escaping (_ lat: Double, _ long: Double) -> Void,
        onRetriesExhaustedWithoutValidLocation: (() -> Void)? = nil
    ) {
        attemptJoin(
            retryCount: 0,
            maxRetries: maxRetries,
            retryDelay: retryDelay,
            locationProvider: locationProvider,
            joinAction: joinAction,
            onRetriesExhaustedWithoutValidLocation: onRetriesExhaustedWithoutValidLocation
        )
    }

    private func attemptJoin(
        retryCount: Int,
        maxRetries: Int,
        retryDelay: TimeInterval,
        locationProvider: @escaping () -> (lat: Double, long: Double, isValid: Bool),
        joinAction: @escaping (_ lat: Double, _ long: Double) -> Void,
        onRetriesExhaustedWithoutValidLocation: (() -> Void)?
    ) {
        let location = locationProvider()
        if location.isValid {
            joinAction(location.lat, location.long)
            return
        }
        if retryCount >= maxRetries {
            onRetriesExhaustedWithoutValidLocation?()
            return
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + retryDelay) { [weak self] in
            self?.attemptJoin(
                retryCount: retryCount + 1,
                maxRetries: maxRetries,
                retryDelay: retryDelay,
                locationProvider: locationProvider,
                joinAction: joinAction,
                onRetriesExhaustedWithoutValidLocation: onRetriesExhaustedWithoutValidLocation
            )
        }
    }

    func stopUiTimer() {
        uiTimer?.invalidate()
        uiTimer = nil
    }

    func stopSessionTimer() {
        heartbeatTimer?.invalidate()
        heartbeatTimer = nil
    }

    func stopAllTimers() {
        stopUiTimer()
        stopSessionTimer()
    }
}
