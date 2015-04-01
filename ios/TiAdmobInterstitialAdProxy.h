/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiProxy.h"
//#import "TiAdmobInterstitial.h"
#import "GADInterstitial.h"
#import "GADInterstitialDelegate.h"
#import "TiAdmobInterstitial.h"

@interface TiAdmobInterstitialAdProxy : TiProxy<GADInterstitialDelegate> {
    TiAdmobInterstitial *interstitial;
}

-(void)load:(id)args;
-(void)show:(id)args;

@property(readonly,nonatomic) NSString *isReady;

@end