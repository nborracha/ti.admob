/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiAdmobInterstitial.h"

@implementation TiAdmobInterstitial

-(void)prepareInterstitial:(id)args {
    ENSURE_UI_THREAD(prepareInterstitial, nil);
    ENSURE_UI_THREAD(show, nil);
    NSLog(@"[Ti.Admob] Prepare was called on admob");
    self.interstitial = [[GADInterstitial alloc] init];
    self.interstitial.adUnitID = self.adUnitID;
    self.interstitial.delegate = self;
    
    GADRequest *request = [GADRequest request];
    // Requests test ads on simulators.
    request.testDevices = @[ GAD_SIMULATOR_ID ];
    
    // Go through the configurable properties, populating our request with their values (if they have been provided).
    request.keywords = [self.proxy valueForKey:@"keywords"];
    request.birthday = [self.proxy valueForKey:@"dateOfBirth"];
    
    NSDictionary* location = [self.proxy valueForKey:@"location"];
    if (location != nil) {
        [request setLocationWithLatitude:[[location valueForKey:@"latitude"] floatValue]
                               longitude:[[location valueForKey:@"longitude"] floatValue]
                                accuracy:[[location valueForKey:@"accuracy"] floatValue]];
    }
    
    NSString* gender = [self.proxy valueForKey:@"gender"];
    if ([gender isEqualToString:@"male"]) {
        request.gender = kGADGenderMale;
    } else if ([gender isEqualToString:@"female"]) {
        request.gender = kGADGenderFemale;
    } else {
        request.gender = kGADGenderUnknown;
    }
    
    [self.interstitial loadRequest:request];
}

-(void)show:(id)args {
    NSLog(@"[Ti.Admob] Show was called on interstitial wrapper");
    if ([self.interstitial isReady]) {
        [self.interstitial presentFromRootViewController:[[TiApp app] controller]];
    }
}

#pragma mark -
#pragma mark Ad Delegate

/// Called when an interstitial ad request succeeded.
- (void)interstitialDidReceiveAd:(GADInterstitial *)ad {
    NSLog(@"[Ti.Admob] interstitialDidReceiveAd");
    [self.proxy fireEvent:@"load"];
    
}

/// Called when an interstitial ad request failed.
- (void)interstitial:(GADInterstitial *)ad didFailToReceiveAdWithError:(GADRequestError *)error {
    NSLog(@"[Ti.Admob] interstitialDidFailToReceiveAdWithError: %@", [error localizedDescription]);
    NSDictionary *event = [NSDictionary dictionaryWithObjectsAndKeys:[error localizedDescription],@"error",nil];
    [self.proxy fireEvent:@"failure" withObject:event];
}

/// Called just before presenting an interstitial.
- (void)interstitialWillPresentScreen:(GADInterstitial *)ad {
    NSLog(@"[Ti.Admob] interstitialWillPresentScreen");
    [self.proxy fireEvent:@"open"];
}

/// Called before the interstitial is to be animated off the screen.
- (void)interstitialWillDismissScreen:(GADInterstitial *)ad {
    NSLog(@"[Ti.Admob] interstitialWillDismissScreen");
    [self.proxy fireEvent:@"willDismissScreen"];
}

/// Called just after dismissing an interstitial and it has animated off the screen.
- (void)interstitialDidDismissScreen:(GADInterstitial *)ad {
    NSLog(@"[Ti.Admob] interstitialDidDismissScreen");
    [self.proxy fireEvent:@"close"];
}

/// Called just before the application will background or terminate because the user clicked on an
/// ad that will launch another application (such as the App Store).
- (void)interstitialWillLeaveApplication:(GADInterstitial *)ad {
    NSLog(@"[Ti.Admob] interstitialWillLeaveApplication");
    [self.proxy fireEvent:@"leftApplication"];
}


@end