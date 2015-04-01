/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2015 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiAdmobInterstitialAdProxy.h"

#import "TiUtils.h"
#import "TiApp.h"
#import "TiUtils.h"

@implementation TiAdmobInterstitialAdProxy

-(void)load:(id)args
{
    ENSURE_UI_THREAD(load, nil);
    ENSURE_UI_THREAD(show, nil);
    NSLog(@"[Ti.Admob] viewDidLoad was called on TiAdmobInterstitialAd with key %@", [self valueForKey:@"adUnitId"]);
    interstitial = [[TiAdmobInterstitial alloc] init];
    interstitial.adUnitID = [self valueForKey:@"adUnitId"];
    interstitial.proxy = self;
    [interstitial prepareInterstitial:nil];
}

-(void)show:(id)args
{
    NSLog(@"[Ti.Admob] Request to show ad was received");
    [interstitial show:nil];
}

// Check if ad is loaded and ready to be shown
-(NSNumber*)isReady:(id)args
{
    if ([interstitial.interstitial isReady]) {
        return NUMBOOL(YES);
    }
    return NUMBOOL(NO);
}

-(NSNumber*)isReady
{
    if ([interstitial.interstitial isReady]) {
        return NUMBOOL(YES);
    }
    return NUMBOOL(NO);
}

@end
