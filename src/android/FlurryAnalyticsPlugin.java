package com.affinity.cordova.flurryanalytics;

import android.util.Log;
import com.flurry.android.Constants;
import com.flurry.android.FlurryAgent;
import com.flurry.android.FlurryEventRecordStatus;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class FlurryAnalyticsPlugin extends CordovaPlugin {

    private static final String TAG = "FlurryAnalyticsPlugin";
    private static final List<String> SUPPORTED_ACTIONS = Arrays.asList(
        "initialize",
        "logEvent",
        "endTimedEvent",
        "logPageView",
        "logError",
        "setLocation"
    );

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if(SUPPORTED_ACTIONS.contains(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        if ("initialize".equals(action)) {
                            init(args, callbackContext);
                        } else if ("logEvent".equals(action)) {
                            logEvent(args, callbackContext);
                        } else if ("endTimedEvent".equals(action)) {
                            endTimedEvent(args, callbackContext);
                        } else if ("logPageView".equals(action)) {
                            logPageView(args, callbackContext);
                        } else if ("logError".equals(action)) {
                            logError(args, callbackContext);
                        } else if ("setLocation".equals(action)) {
                            setLocation(args, callbackContext);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                        callbackContext.error("flurry json exception: " + e.getMessage());
                    }
                }
            });
            return true;
        } else {
            Log.d(TAG, "Invalid Action: " + action);
            callbackContext.error("Invalid Action: " + action);
            return false;
        }
    }

    private void setLocation(JSONArray args, CallbackContext callbackContext) throws JSONException {

        float latitude = (float) args.getDouble(0);
        float longitude = (float) args.getDouble(1);

        try {
            FlurryAgent.setLocation(latitude, longitude);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void logError(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            FlurryAgent.onError(args.getString(0), args.getString(1), (Exception) null);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void logPageView(JSONArray args, CallbackContext callbackContext) {
        try {
            FlurryAgent.onPageView();
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void endTimedEvent(JSONArray args, CallbackContext callbackContext) throws JSONException {
        String event = args.getString(0);
        if (args.isNull(1)) {
            FlurryAgent.endTimedEvent(event);
        } else {
            FlurryAgent.endTimedEvent(event, this.jsonObjectToMap(args.getJSONObject(1)));
        }
    }

    private void init(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {

            // deal with all the optional configuration data
            if (!args.isNull(1)) {

                JSONObject options = args.getJSONObject(1);
                if (!options.isNull("version")) {
                    FlurryAgent.setVersionName(options.getString("version"));
                }
                if (!options.isNull("continueSessionSeconds")) {
                    // TODO: validate the value is less than 5 and return error
                    FlurryAgent.setContinueSessionMillis(options.getInt("continueSessionSeconds") * 1000);
                }
                if (!options.isNull("userId")) {
                    FlurryAgent.setUserId(options.getString("userId"));
                }
                if (!options.isNull("gender")) {
                    char gender = options.getString("gender").toLowerCase().charAt(0);
                    if (gender == 'm') {
                        FlurryAgent.setGender(Constants.MALE);
                    } else if (gender == 'f') {
                        FlurryAgent.setGender(Constants.FEMALE);
                    } else {
                        // TODO: log and warning, leave gender as default
                    }
                }
                if (!options.isNull("age")) {
                    FlurryAgent.setAge(options.getInt("age"));
                }
                if (!options.isNull("logLevel")) {
                    String level = options.getString("logLevel");

                    if ("VERBOSE".equalsIgnoreCase(level)) {
                        FlurryAgent.setLogLevel(Log.VERBOSE);
                        FlurryAgent.setLogEnabled(true);
                    } else if ("DEBUG".equalsIgnoreCase(level)) {
                        FlurryAgent.setLogLevel(Log.DEBUG);
                        FlurryAgent.setLogEnabled(true);
                    } else if ("INFO".equalsIgnoreCase(level)) {
                        FlurryAgent.setLogLevel(Log.INFO);
                        FlurryAgent.setLogEnabled(true);
                    } else if ("WARN".equalsIgnoreCase(level)) {
                        FlurryAgent.setLogLevel(Log.WARN);
                        FlurryAgent.setLogEnabled(true);
                    } else if ("ERROR".equalsIgnoreCase(level)) {
                        FlurryAgent.setLogLevel(Log.ERROR);
                        FlurryAgent.setLogEnabled(true);
                    } else {
                        // TODO: log and return warning, leave log level at default
                    }
                }
                if (!options.isNull("enableEventLogging")) {

                    FlurryAgent.setLogEvents(options.getBoolean("enableEventLogging"));
                }
            }
        /*
        iOS only - noops for Android

        enableEventLogging
        reportSessionsOnClose
        reportSessionsOnPause
        enableSecureTransport
        enableBackgroundSessions
        enableCrashReporting
        */

            // app key is the only that is required.
            String appKey = args.getString(0);
            FlurryAgent.init(cordova.getActivity(), appKey);
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }

    }

    private void logEvent(JSONArray args, CallbackContext callbackContext) throws JSONException {

        FlurryEventRecordStatus result;
        String eventName = args.getString(0);
        boolean timed = args.getBoolean(1);

        if (args.isNull(2)) {
            result = FlurryAgent.logEvent(eventName, timed);
        } else {
            result = FlurryAgent.logEvent(eventName, this.jsonObjectToMap(args.getJSONObject(2)), timed);
        }

        if (result == FlurryEventRecordStatus.kFlurryEventRecorded) {
            callbackContext.success();
        } else {
            callbackContext.error(result.toString());
        }
    }

    private Map<String, String> jsonObjectToMap(JSONObject json) throws JSONException {
        if (json == null) {
            Log.d(TAG, "not json");
            return null;
        }
        @SuppressWarnings("unchecked")
        Iterator<String> nameItr = json.keys();
        Map<String, String> params = new HashMap<String, String>();
        while (nameItr.hasNext()) {
            String name = nameItr.next();
            params.put(name, json.getString(name));
        }
        return params;
    }
}
