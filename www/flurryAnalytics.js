
function FlurryAnalytics() {

	var featureName = featureName;

	/*
	 the only function that is actually required to start tracking sessions

	 options:

	 version
	 continueSessionSeconds       (must be less than or equal to five for Android)
	 userId
	 gender
	 age
	 logLevel                    (VERBOSE, DEBUG, INFO, WARN, ERROR)
	 enableLogging               (defaults to false)
	 enableEventLogging          (defaults to true)
	 enableCrashReporting        (defaults to false, iOS only)
	 enableBackgroundSessions    (defaults to false, iOS only)
	 reportSessionsOnClose       (defaults to true, iOS only)
	 reportSessionsOnPause       (defaults to true, iOS only)
	 */
	this.init = function(appKey /* [options], successCallback, failureCallback */) {

		var successCallback,
			failureCallback,
			options;

		if(arguments.length === 4) {
			options = arguments[1];
			successCallback = arguments[2];
			failureCallback = arguments[3];
		} else if(arguments.length === 3) {
			successCallback = arguments[1];
			failureCallback = arguments[2];
		} else if(arguments.length === 2) {
			options = arguments[1];
		}

		return cordova.exec(successCallback, failureCallback, featureName, 'initialize', [appKey, options]);
	};

	// the params parameter is optional
	this.logEvent = function(event /* [params], successCallback, failureCallback */) {

		var successCallback,
			failureCallback,
			params;

		if(arguments.length === 4) {
			params = arguments[1];
			successCallback = arguments[2];
			failureCallback = arguments[3];
		} else if(arguments.length === 3) {
			successCallback = arguments[1];
			failureCallback = arguments[2];
		} else if(arguments.length === 2) {
			params = arguments[1];
		}

		return cordova.exec(successCallback, failureCallback, featureName, 'logEvent', [
			event,
			false,
			params
		]);
	};

	// the params parameter is optional
	this.startTimedEvent = function(event /* [params], successCallback, failureCallback */) {

		var successCallback,
			failureCallback,
			params;

		if(arguments.length === 4) {
			params = arguments[1];
			successCallback = arguments[2];
			failureCallback = arguments[3];
		} else if(arguments.length === 3) {
			successCallback = arguments[1];
			failureCallback = arguments[2];
		} else if(arguments.length === 2) {
			params = arguments[1];
		}

		return cordova.exec(successCallback, failureCallback, featureName, 'logEvent', [
			event,
			true,
			params
		]);
	};

	// the params parameter is optional
	this.endTimedEvent = function(event /* [params], successCallback, failureCallback */) {

		var successCallback,
			failureCallback,
			params;

		if(arguments.length === 4) {
			params = arguments[1];
			successCallback = arguments[2];
			failureCallback = arguments[3];
		} else if(arguments.length === 3) {
			successCallback = arguments[1];
			failureCallback = arguments[2];
		} else if(arguments.length === 2) {
			params = arguments[1];
		}
		return cordova.exec(successCallback, failureCallback, featureName, 'endTimedEvent', [
			event,
			params
		]);
	};

	this.logPageView = function(successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, featureName, 'logPageView', []);
	};

	this.logError = function(code, message, successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, featureName, 'logError', [code, message]);
	};

	this.setLocation = function(location, message, successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, featureName, 'setLocation', [
			location.latitude,
			location.longitude,
			location.verticalAccuracy,
			location.horizontalAccuracy
		]);
	};

	// only needed for older versions of Android
	this.startSession = function(successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, featureName, 'startSession', []);
	};

	// only needed for older versions of Android
	this.endSession = function(successCallback, failureCallback) {
		return cordova.exec(successCallback, failureCallback, featureName, 'endSession', []);
	};

}

if(typeof module !== undefined && module.exports) {
	module.exports = FlurryAnalytics;
}

