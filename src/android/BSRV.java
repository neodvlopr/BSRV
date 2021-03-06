package you.got.it.plugin;

import android.app.Activity;
import android.os.IBinder;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class BSRV extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("echo")) {
            String message = args.getString(0);
            this.echo(message, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        Activity context = cordova.getActivity();
        if (message != null && message.length() > 0) {

            Intent myServiceIntent = new Intent(context, performSync.class);
            //fireEvent(Event.ACTIVATE, null);
            context.startService(myServiceIntent);
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}