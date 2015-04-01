/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "GADInterstitial.h"
#import "GADInterstitialDelegate.h"
#include "TiApp.h"

@interface TiAdmobInterstitial : UIViewController<GADInterstitialDelegate>

-(void)show:(id)args;
-(void)prepareInterstitial:(id)args;

@property(nonatomic, strong) TiProxy *proxy;
@property(nonatomic, strong) GADInterstitial *interstitial;
@property(nonatomic, strong) NSString *adUnitID;

@end
