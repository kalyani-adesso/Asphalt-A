// The Swift Programming Language
// https://docs.swift.org/swift-book
import SwiftUI
import Shared
import DesignSystem

@available(iOS 15.0, *)
struct LoginView: View {
    @State private var showContent = false
    var body: some View {
        VStack {
            TextView(text:"Welcome", font: Font(token: .KlavikaBold),color: Color.black, insets:.bottom,length: .sm)
        }
    }
    
    @ViewBuilder
    func HeaderView() -> some View {
   
    }
    
    @ViewBuilder
    func LoginView()-> some View {
       
    }
    
    @ViewBuilder
    func FooterViewView()-> some View {
       
    }
}

@available(iOS 15.0, *)
struct TextView:View {
    let text:String
    let font:Font
    let color:Color
    let insets:Edge.Set
    let length:SpacingToken
        
    var body: some View {
        Text(text)
            .font(font)
            .foregroundStyle(color)
            .padding(insets,CGFloat(length.rawValue))
    }
}

@available(iOS 15.0, *)
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        LoginView()
    }
}
