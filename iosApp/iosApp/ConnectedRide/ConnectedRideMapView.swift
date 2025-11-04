//
//  ConnectedRideMapView.swift
//  iosApp
//
//  Created by Adarsha Hebbar on 21/10/25.
//

import SwiftUI
import MapKit

struct ConnectedRideMapView: View {
    @StateObject private var viewModel = ConnectedRideViewModel()
    @State private var rideComplted: Bool = false
    @State private var startTrack: Bool = false
    @State private var showToast: Bool = true
    @State var showJoinRideView:Bool = false
    
    let rideStops: [RideStop] = [
           RideStop(name: "Kochi Center", coordinate: CLLocationCoordinate2D(latitude: 9.9312, longitude: 76.2673)),
           RideStop(name: "Vyttila", coordinate: CLLocationCoordinate2D(latitude: 9.9622, longitude: 76.3185)),
           RideStop(name: "Edappally", coordinate: CLLocationCoordinate2D(latitude: 10.0280, longitude: 76.3080)),
           RideStop(name: "Kakkanad", coordinate: CLLocationCoordinate2D(latitude: 10.0158, longitude: 76.3419)),
           RideStop(name: "Aluva", coordinate: CLLocationCoordinate2D(latitude: 10.1081, longitude: 76.3516))
       ]
    
    var body: some View {
        List {
            Section {
                VStack {
                    ZStack(alignment: .topLeading) {
                        PolylineMapView(coordinates: rideStops.map { $0.coordinate })
                                        .frame(height: 534)
                                        .cornerRadius(12)
                                        .ignoresSafeArea(edges: .top)
                        VStack {
                            ZStack {
                                HStack {
                                    mapActionButton()
                                }
                                if showToast {
                                    showToast(title: AppStrings.ConnectedRide.rideStarted)
                                }
                            }
                            Spacer()
                            HStack {
                                distanceAndETA()
                                Spacer()
                                floatingButton()
                            }
                        }
                    }
                    .frame( height: 534)
                    Spacer()
                }
            }
            .listRowSeparator(.hidden)
            Section {
                ConnectedRideOfflineView(title: "Abhishek has been stopped for 5 minutes.", subtitle: "Check if assistance is needed.", image: AppIcon.ConnectedRide.warning)
            }
            .listRowSeparator(.hidden)
            Section {
                VStack(spacing: 18) {
                    ConnectedRideHeaderView(title: AppStrings.ConnectedRide.rideInProgressTitle, subtitle:AppStrings.ConnectedRide.groupNavigationActiveSubtitle, image: AppIcon.Profile.profile)
                    ForEach(viewModel.activeRider, id: \.id) { rider in
                        ActiveRiderView( title: rider.name, speed: "\(rider.speed) km", startTrack: $startTrack)
                    }
                    Button(action: {
                        self.rideComplted = true
                    }, label: {
                        Text(AppStrings.ConnectedRide.endRideButton)
                            .frame(maxWidth: .infinity,minHeight: 60)
                            .font(KlavikaFont.bold.font(size: 18))
                            .foregroundColor(AppColor.white)
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(AppColor.red)
                            )
                    })
                    .padding([.leading,.trailing,.bottom],16)
                    .buttonStyle(.plain)
                }
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.listGray)
                )
            }
            .listRowSeparator(.hidden)
            .listRowBackground(Color.clear)
            
            Section {
                VStack(spacing: 18) {
                    ConnectedRideHeaderView(title: "\(AppStrings.ConnectedRide.groupStatusTitle) (\(viewModel.groupRiders.count))", subtitle: "", image: AppIcon.ConnectedRide.groupStatus)
                    ForEach(viewModel.groupRiders, id: \.id) { rider in
                        GroupRiderView(title: rider.name, status: rider.status.rawValue, speed: "\(rider.speed) km", subTitle: rider.timeSinceUpdate)
                    }
                }
                .padding(.bottom,16)
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.listGray)
                )
            }
            .listRowSeparator(.hidden)
            .listRowBackground(Color.clear)
            
            Section {
                VStack(spacing: 18) {
                    ConnectedRideHeaderView(title: AppStrings.ConnectedRide.emergencyActionsTitle, subtitle: "", image: AppIcon.ConnectedRide.emergency)
                    HStack(spacing: 16) {
                        Button(action: {
                            
                        }) {
                            HStack(alignment: .center,spacing: 5) {
                                AppIcon.ConnectedRide.sos
                                Text(AppStrings.ConnectedRide.emergencySOSButton)
                                    .font(KlavikaFont.bold.font(size: 16))
                                    .foregroundStyle(AppColor.black)
                            }
                            .padding()
                            .frame( height: 50)
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(AppColor.white)
                            )
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(AppColor.darkGray, lineWidth: 2)
                            )
                        }
                        Button(action: {
                            
                        }) {
                            HStack(alignment: .center,spacing: 5) {
                                AppIcon.ConnectedRide.shareLocation
                                Text(AppStrings.ConnectedRide.shareLocationButton)
                                    .font(KlavikaFont.bold.font(size: 16))
                                    .foregroundStyle(AppColor.black)
                            }
                            .padding()
                            .frame( height: 50)
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(AppColor.white)
                            )
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(AppColor.darkGray, lineWidth: 2)
                            )
                        }
                    }
                    .padding(.bottom)
                }
                .background(
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AppColor.listGray)
                )
            }
            .listRowSeparator(.hidden)
            .listRowBackground(Color.clear)
        }.listStyle(.plain)
            .listRowSeparator(.hidden)
            .navigationBarBackButtonHidden()
            .navigationDestination(isPresented: $rideComplted, destination: {
                ConnectedRideView(notificationTitle: "Ride sucessfully completed", title: "Completing ride", subTitle: "Saving your ride data and generating summary", model: JoinRideModel(
                    title: "Weekend Coast Ride",
                    organizer: "Sooraj",
                    description: "Join us for a beautiful sunrise ride along the coastal highway",
                    route: "Kochi - Kanyakumari",
                    distance: "280km",
                    date: "Sun, Oct 21",
                    ridersCount: "3",
                    maxRiders: "8",
                    riderImage: "rider_avatar"
                ))
            })
            .onAppear() {
                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                    showToast = false
                }
            }
            .toolbar {
                ToolbarItemGroup(placement: .navigationBarTrailing) {
                    HStack() {
                        Button {
                            print("Menu tapped")
                        } label: {
                            HStack(spacing: 10) {
                                Rectangle()
                                    .fill(AppColor.spanishGreen)
                                    .frame(width: 9, height: 9)
                                
                                Text("LIVE")
                                    .font(KlavikaFont.regular.font(size: 12))
                                    .foregroundStyle(.spanishGreen)
                            }
                            .frame(width: 69, height: 30)
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(AppColor.white)
                            )
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(AppColor.spanishGreen, lineWidth: 1)
                            )
                        }
                        Button {
                            print("Menu tapped")
                        } label: {
                            HStack {
                                AppIcon.ConnectedRide.rideDuration
                                    .resizable()
                                    .frame(width:12,height: 12)
                                Text("00:00:49")
                                    .font(KlavikaFont.regular.font(size: 12))
                                    .foregroundStyle(AppColor.celticBlue)
                            }
                            .frame(width: 83, height: 30)
                            .background(
                                RoundedRectangle(cornerRadius: 10)
                                    .fill(AppColor.white)
                            )
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(AppColor.celticBlue, lineWidth: 1)
                            )
                        }
                    }
                }
                
                ToolbarItemGroup(placement: .topBarLeading) {
                    HStack {
                        Button(action: {
                            showJoinRideView = true
                        }, label:{
                            AppIcon.CreateRide.backButton
                        })
                        VStack(alignment: .leading, spacing: 5) {
                            Text(AppStrings.ConnectedRide.connectedRide)
                                .font(KlavikaFont.bold.font(size: 16))
                                .foregroundColor(AppColor.black)
                            
                            Text(AppStrings.ConnectedRide.rideNameWeekendCoast)
                                .font(KlavikaFont.regular.font(size: 12))
                                .foregroundColor(AppColor.black)
                            
                        }
                    }
                }
            }
            .navigationDestination(isPresented: $showJoinRideView, destination: {
                JoinRideView()
            })
    }
    
    @ViewBuilder func mapActionButton() -> some View {
        Button(action: {
            
        }) {
            AppIcon.ConnectedRide.zoom
                .resizable()
                .frame(width: 50, height: 50)
                .padding([.top, .leading],15)
        }
        Spacer()
        Button(action: {
            
        }) {
            HStack(alignment: .center) {
                Text(AppStrings.ConnectedRide.standard)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundStyle(AppColor.stoneGray)
            }
            .frame(width: 69, height: 24)
            .background(
                RoundedRectangle(cornerRadius: 10)
                    .fill(AppColor.white)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(AppColor.darkGray, lineWidth: 2)
            )
        }
        .buttonStyle(.plain)
        .padding([.top,.trailing])
    }
    
    @ViewBuilder func showToast(title:String) -> some View {
        HStack(spacing: 10) {
            AppIcon.ConnectedRide.checkmark
                .padding(.leading,20)
            Text(title)
                .font(KlavikaFont.bold.font(size: 14))
                .foregroundStyle(.spanishGreen)
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .frame(height: 60)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.lightGreen)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.aeroGreen, lineWidth: 2)
        )
        .padding([.leading, .trailing,.top],16)
        .animation(.easeInOut(duration: 0.3), value: false)
    }
    
    @ViewBuilder func distanceAndETA() -> some View {
        VStack(alignment: .center) {
            Text("25")
                .font(KlavikaFont.bold.font(size: 20))
                .foregroundStyle(AppColor.black)
            Text("kph")
                .font(KlavikaFont.medium.font(size: 16))
                .foregroundStyle(AppColor.stoneGray)
        }
        .frame(width: 58,height: 50)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
        )
        .padding()
    }
    
    @ViewBuilder func floatingButton() -> some View {
        HStack(spacing: 10) {
            ButtonView(title: "", icon: AppIcon.ConnectedRide.refresh ,onTap: {
                
            })
            ButtonView(title: "", icon: AppIcon.ConnectedRide.nearMe,onTap: {
                
            })
        }
        .frame(width: 198)
        .padding(.trailing)
    }
}

struct ConnectedRideHeaderView: View {
    let title: String
    let subtitle: String
    let image: Image
    
    var body: some View {
        HStack(spacing: 11) {
            image
                .resizable()
                .scaledToFill()
                .frame(width: 30, height: 30)
                .modifier(CustomCornerRadius(text: subtitle))
            
            VStack(alignment: .leading, spacing: 5) {
                Text(title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.black)
                if !subtitle.isEmpty {
                    Text(subtitle)
                        .font(KlavikaFont.regular.font(size: 12))
                        .foregroundColor(AppColor.black)
                }
            }
            Spacer()
        }
        .padding([.top,.leading],16)
    }
}

struct ConnectedRideOfflineView: View {
    let title: String
    let subtitle: String
    let image: Image
    
    var body: some View {
        HStack(spacing: 11) {
            image
                .resizable()
                .scaledToFill()
                .frame(width: 32, height: 32)
            
            VStack(alignment: .leading, spacing: 5) {
                Text(title)
                    .font(KlavikaFont.bold.font(size: 16))
                    .foregroundColor(AppColor.yellow)
                Text(subtitle)
                    .font(KlavikaFont.regular.font(size: 12))
                    .foregroundColor(AppColor.stoneGray)
            }
            Spacer()
        }
        .padding([.top,.leading,.bottom],16)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.yellow.opacity(0.1))
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.backgroundLight, lineWidth: 2)
        )
    }
}

struct ActiveRiderView: View {
    let title: String
    let speed: String
    @Binding var startTrack:Bool
    var body: some View {
        HStack {
            HStack(spacing: 16) {
                AppIcon.Profile.profile
                    .resizable()
                    .clipShape(Circle())
                    .frame(width: 37, height: 37)
                    .overlay(Circle().stroke(AppColor.green, lineWidth: 1.5))
                    .padding(.leading, 18)
                
                VStack(alignment: .leading, spacing: 5) {
                    Text(title)
                        .font(KlavikaFont.bold.font(size: 16))
                        .foregroundColor(AppColor.black)
                    HStack(spacing: 3) {
                        AppIcon.ConnectedRide.speed
                        Text(speed)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(.stoneGray)
                    }
                }
            }
            Spacer()
            Button(action: {
                startTrack.toggle()
            }) {
                Text(!startTrack ? AppStrings.ConnectedRide.stopTrackingButton.uppercased() : AppStrings.ConnectedRide.startTrackingButton.uppercased())
                    .font(KlavikaFont.bold.font(size: 12))
                    .foregroundColor(AppColor.white)
                    .frame(width: 120, height: 30)
                    .background(
                        RoundedRectangle(cornerRadius: 10)
                            .fill(startTrack ? AppColor.green : AppColor.red)
                    )
            }
            .padding(.trailing, 16)
        }
        .frame(maxWidth: .infinity, minHeight: 60)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .padding([.leading,.trailing],16)
    }
}

struct GroupRiderView: View {
    let title: String
    let status:String
    let speed: String
    let subTitle:String
    
    var body: some View {
        HStack {
            HStack(spacing: 16) {
                AppIcon.Profile.profile
                    .resizable()
                    .clipShape(Circle())
                    .frame(width: 37, height: 37)
                    .overlay(Circle().stroke(AppColor.green, lineWidth: 1.5))
                    .padding(.leading, 18)
                
                VStack(alignment: .leading, spacing: 5) {
                    HStack(spacing: 6) {
                        Text(title)
                            .font(KlavikaFont.bold.font(size: 16))
                            .foregroundColor(AppColor.black)
                        Text(status)
                            .padding(6)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(statusTextColor)
                            .background(statusBgColor)
                            .frame(height: 16)
                            .cornerRadius(10)
                    }
                    HStack(spacing: 3) {
                        AppIcon.ConnectedRide.speed
                        Text(speed)
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(.stoneGray)
                        Text("• \(subTitle)")
                            .font(KlavikaFont.regular.font(size: 12))
                            .foregroundColor(.stoneGray)
                    }
                }
                Spacer()
                HStack {
                    Button(action: {
                        
                    }, label: {
                        AppIcon.ConnectedRide.call
                    })
                    .buttonStyle(.plain)
                    Button(action: {
                        
                    }, label: {
                        AppIcon.Home.message
                    })
                    .buttonStyle(.plain)
                }
            }
            Spacer()
        }
        .frame(maxWidth: .infinity, minHeight: 60)
        .background(
            RoundedRectangle(cornerRadius: 10)
                .fill(AppColor.white)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(AppColor.darkGray, lineWidth: 2)
        )
        .padding([.leading,.trailing],16)
    }
    
    var statusBgColor:Color {
        if status == RiderStatus.connected.rawValue {
            return AppColor.lightGreen
        } else if status == RiderStatus.delayed.rawValue {
            return AppColor.pastelYellow
        } else if status == RiderStatus.stopped.rawValue {
            return AppColor.blushPink
        } else {
            return AppColor.pink
        }
    }
    
    var statusTextColor:Color {
        if status == RiderStatus.connected.rawValue {
            return AppColor.darkGreen
        } else if status == RiderStatus.delayed.rawValue {
            return AppColor.deepOrange
        } else if status == RiderStatus.stopped.rawValue {
            return AppColor.crimsonRed
        } else {
            return AppColor.pink
        }
    }
}

struct CustomCornerRadius: ViewModifier {
    let text:String
    func body(content: Content) -> some View {
        if text.isEmpty {
            content
        } else {
            content
                .clipShape(Circle())
                .overlay(Circle().stroke(AppColor.aeroGreen, lineWidth: 1.5))
        }
    }
}

#Preview {
    NavigationStack {
        ConnectedRideMapView()
    }
}
