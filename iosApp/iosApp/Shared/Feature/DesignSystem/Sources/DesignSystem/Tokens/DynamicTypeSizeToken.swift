import SwiftUI
import UIKit

@available(iOS 15.0, *)
public extension View {
    @ViewBuilder
    func limitDynamicTypeSizeRange(_ shouldApply: Bool) -> some View {
        if shouldApply {
            self.dynamicTypeSize(DynamicTypeSize.maxScaleRange)
        } else {
            self
        }
    }
}

@available(iOS 15.0, *)
public extension DynamicTypeSize {
    // This is the first iteration and will be finalized with the designers
    static var maxScaleRange: PartialRangeThrough<DynamicTypeSize> {
        let deviceIdentifier = Self.getDeviceIdentifier()

        switch deviceIdentifier {
        // iPhone 15 Series
        case "iPhone15,4": return ...DynamicTypeSize.xxLarge // "iPhone 15"
        case "iPhone15,5": return ...DynamicTypeSize.xxxLarge // "iPhone 15 Plus"
        case "iPhone16,1": return ...DynamicTypeSize.xxxLarge // "iPhone 15 Pro"
        case "iPhone16,2": return ...DynamicTypeSize.xxxLarge // "iPhone 15 Pro Max"
        // iPhone 14 Series
        case "iPhone14,7": return ...DynamicTypeSize.xxLarge // "iPhone 14"
        case "iPhone14,8": return ...DynamicTypeSize.xxxLarge // "iPhone 14 Plus"
        case "iPhone15,2": return ...DynamicTypeSize.xxxLarge // "iPhone 14 Pro"
        case "iPhone15,3": return ...DynamicTypeSize.xxxLarge // "iPhone 14 Pro Max"
        // iPhone 13 Series
        case "iPhone14,2": return ...DynamicTypeSize.xxxLarge // "iPhone 13 Pro"
        case "iPhone14,3": return ...DynamicTypeSize.xxxLarge // "iPhone 13 Pro Max"
        case "iPhone14,4": return ...DynamicTypeSize.xxLarge // "iPhone 13 Mini"
        case "iPhone14,5": return ...DynamicTypeSize.xxLarge // "iPhone 13"
        // iPhone 12 Series
        case "iPhone13,1": return ...DynamicTypeSize.xxLarge // "iPhone 12 Mini"
        case "iPhone13,2": return ...DynamicTypeSize.xxLarge // "iPhone 12"
        case "iPhone13,3": return ...DynamicTypeSize.xxxLarge // "iPhone 12 Pro"
        case "iPhone13,4": return ...DynamicTypeSize.xxxLarge // "iPhone 12 Pro Max"
        // iPhone 11 Series
        case "iPhone12,1": return ...DynamicTypeSize.large // "iPhone 11"
        case "iPhone12,3": return ...DynamicTypeSize.xxxLarge // "iPhone 11 Pro"
        case "iPhone12,5": return ...DynamicTypeSize.xxxLarge // "iPhone 11 Pro Max"
        // iPhone X Series
        case "iPhone10,3", "iPhone10,6": return ...DynamicTypeSize.xxLarge // "iPhone X"
        case "iPhone10,1", "iPhone10,4": return ...DynamicTypeSize.xxLarge // "iPhone 8"
        case "iPhone10,2", "iPhone10,5": return ...DynamicTypeSize.xxLarge // "iPhone 8 Plus"
        // Default case for iPads and other devices
        default: return ...DynamicTypeSize.xxLarge
        }
    }

    private static func getDeviceIdentifier() -> String {
        #if targetEnvironment(simulator)
        // Simulator-specific handling
        if let simulatorIdentifier = ProcessInfo().environment["SIMULATOR_MODEL_IDENTIFIER"] {
            return simulatorIdentifier
        }
        #endif

        // Physical device handling
        var systemInfo = utsname()
        uname(&systemInfo)
        let machineMirror = Mirror(reflecting: systemInfo.machine)
        let identifier = machineMirror.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8, value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }
        return identifier
    }
}
