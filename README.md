# Flurry Analytics Wrapper for Cordova

FlurryAnalytics Cordova wrapper that depends on floatinghotpot/cordova-plugin-flurry for the FlurryAnalytics SDK rather than including it.  Currently this means 5.3.0 support on Android and 5.4.0 support on iOS.

This plugin is borrowed from https://github.com/blakgeek/cordova-plugin-flurryanalytics and heavily inspired by https://github.com/jfpsf/flurry-phonegap-plugin.

## Installation

    cordova plugin add https://github.com/floatinghotpot/cordova-plugin-flurry
    cordova plugin add https://github.com/affinityis/cordova-flurry-analytics

## Usage

```javascript
// create a new instance
flurryAnalytics = new FlurryAnalytics();

// initialize it
flurryAnalytics.init('<your app key>', function() {
    console.log("Yippy I'm initialized and ish");
}, function(err) {
    console.error(['Awww man :(', err]);
});

// or initialize it with options (none are required)
var options = {
    version: 'my_custom_version',       // overrides the version of the app
    continueSessionSeconds: 3,          // how long can the app be paused before a new session is created, must be less than or equal to five for Android devices
    userId: 'blakgeek',
    gender: 'm',                        // valid values are "m", "M", "f" and "F"
    age: 38,
    logLevel: 'ERROR',                  // (VERBOSE, DEBUG, INFO, WARN, ERROR)
    enableLogging: true,                // defaults to false
    enableEventLogging: false,          // should every event show up the app's log, defaults to true
    enableCrashReporting: true,         // should app crashes be recorded in flurry, defaults to false, iOS only
    enableBackgroundSessions: true,     // should the session continue when the app is the background, defaults to false, iOS only
    reportSessionsOnClose: false,       // should data be pushed to flurry when the app closes, defaults to true, iOS only
    reportSessionsOnPause: false        // should data be pushed to flurry when the app is paused, defaults to true, iOS only
}
flurryAnalytics.init('<your app key>', options, function() {
    console.log("Look ma I'm initialized and customized");
}, function(err) {
    console.error(['Awww man :(', err]);
});

// log an event to flurry
flurryAnalytics.logEvent('dinner time', function() {
    console.log('Nice!');
}, function(err) {
    console.error(['Error', err]);
});

// log an event to flurry with custom parameters
var ovenParams = {
    temp: 350,
    mode: 'convection',
    rackPosition: 'center'
}
flurryAnalytics.logEvent('set oven', ovenParams, function() {
    console.log('Schweet!');
}, function(err) {
    console.error(['Error', err]);
});

// start a timed event
flurryAnalytics.startTimedEvent('bake chicken', function() {
    console.log('Hmmmm chicken');
}, function(err) {
    console.error(['Error', err]);
});

// start a timed event with custom parameters
var riceParams = {
    salt: '2tsp',
    pepper: 'dash',
    water: '2cups'
}
flurryAnalytics.startTimedEvent('prep rice', riceParams, function() {
    console.log('Rice is prep started');
}, function(err) {
    console.error(['Error', err]);
});

// complete a timed event
flurryAnalytics.endTimedEvent('bake chicken', function() {
    console.log('Winner winner chicken dinner');
}, function(err) {
    console.error(['Error', err]);
});

// complete a timed event and change the value of parameters
var newRiceParams = {
    butter: '2pads'
}
flurryAnalytics.endTimedEvent('prep rice', newRiceParams, function() {
    console.log('Winner winner chicken dinner');
}, function(err) {
    console.error(['Error', err]);
});

// log an error
flurryAnalytics.logError('NO_EtOH', "We're out of wine and beer", function() {
    console.log('The authorities have been alerted');
}, function(err) {
    console.error(['Error', err]);
});

// log a page view
flurryAnalytics.logPageView(function() {
    console.log('I see you playa');
}, function(err) {
    console.error(['Error', err]);
});

// set the location for the event (this is will only be used for very course grained statistics like city
var location = {
    latitude: 17.2500,
    longitude: -62.6667,
    verticalAccuracy: -1, // optional iOS only
    horizontalAccuracy: 1440 // optional iOS only
}
flurryAnalytics.setLocation(location, function() {
    console.log('Party over here');
}, function(err) {
    console.error(['Error', err]);
});

```
