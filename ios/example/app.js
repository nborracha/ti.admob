var Admob = require('ti.admob');
var win = Ti.UI.createWindow({
    backgroundColor: 'white'
});

/*
 We'll make two ads. This first one doesn't care about where the user is located.
 */
var ad;
win.add(ad = Admob.createView({
    top: 0, left: 0,
    width: 320, height: 50,
    adUnitId: '<<YOUR AD UNIT ID HERE>>', // You can get your own at http: //www.admob.com/
    adBackgroundColor: 'black',
    // You can get your device's id for testDevices by looking in the console log after the app launched
    testDevices: [Admob.SIMULATOR_ID],
    dateOfBirth: new Date(1985, 10, 1, 12, 1, 1),
    gender: 'male',
    keywords: ''
}));
ad.addEventListener('didReceiveAd', function() {
    alert('Did receive ad!');
});
ad.addEventListener('didFailToReceiveAd', function() {
    alert('Failed to receive ad!');
});
ad.addEventListener('willPresentScreen', function() {
    alert('Presenting screen!');
});
ad.addEventListener('willDismissScreen', function() {
    alert('Dismissing screen!');
});
ad.addEventListener('didDismissScreen', function() {
    alert('Dismissed screen!');
});
ad.addEventListener('willLeaveApplication', function() {
    alert('Leaving the app!');
});

/*
 And we'll try to get the user's location for this second ad!
 */
Ti.Geolocation.accuracy = Ti.Geolocation.ACCURACY_BEST;
Ti.Geolocation.distanceFilter = 0;
Ti.Geolocation.purpose = 'To show you local ads, of course!';
Ti.Geolocation.getCurrentPosition(function reportPosition(e) {
    if (!e.success || e.error) {
        // aw, shucks...
    }
    else {
        win.add(Admob.createView({
            top: 100, left: 0,
            width: 320, height: 50,
            adUnitId: '<<YOUR AD UNIT ID HERE>>', // You can get your own at http: //www.admob.com/
            adBackgroundColor: 'black',
            // You can get your device's id for testDevices by looking in the console log after the app launched
            testDevices: [Admob.SIMULATOR_ID],
            dateOfBirth: new Date(1985, 10, 1, 12, 1, 1),
            gender: 'female',
            keywords: '',
            location: e.coords
        }));
    }
});

win.add(Ti.UI.createLabel({
    text: 'Loading the ads now! ' +
        'Note that there may be a several minute delay ' +
        'if you have not viewed an ad in over 24 hours.',
    bottom: 40,
    height: Ti.UI.SIZE || 'auto', width: Ti.UI.SIZE || 'auto'
}));
win.open();


// create Admob Interstitial Ad (full screen ad)
var adMobInterstitialAd = Admob.createInterstitialAd({
	keywords: 'flowers, nature', //Android & iOS
	gender: 'female', //Android & iOS
	location: {latitude: "13", longitude: "1", accuracy: "12"}, //Android & iOS
	
	//This is for the Android module
	publisherId: '<<YOUR PUBLISHER ID HERE>>', /* Android ID */
	//testDevices: false, // default is false
	
	//This is for the iOS module
    adUnitId: '<<YOUR PUBLISHER ID HERE>>', /* iOS ID */
  	//testDevices: [this.Admob.SIMULATOR_ID]
});

// Interstitial Ad Events

adMobInterstitialAd.addEventListener('load', function() {
	// Show the Interstitial Ad when done loading
	adMobInterstitialAd.show();
});

adMobInterstitialAd.addEventListener('failure', function() {});
adMobInterstitialAd.addEventListener('close', function() {});
adMobInterstitialAd.addEventListener('open', function() {});
adMobInterstitialAd.addEventListener('leftApplication', function() {});
adMobInterstitialAd.addEventListener('willDismissScreen', function() {}); // iOS Only


//Load the Interstitial Ad
adMobInterstitialAd.load();