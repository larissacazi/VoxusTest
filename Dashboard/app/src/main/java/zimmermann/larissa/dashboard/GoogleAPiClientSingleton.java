package zimmermann.larissa.dashboard;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by laris on 24/10/2017.
 */

public class GoogleAPiClientSingleton {
    private static final String TAG = "GoogleAPiClientSingleton";
    private static GoogleAPiClientSingleton instance = null;

    private static GoogleApiClient mGoogleApiClient = null;

    private GoogleAPiClientSingleton() {
    }

    public static GoogleAPiClientSingleton getInstance(GoogleApiClient aGoogleApiClient) {
        if(instance == null) {
            instance = new GoogleAPiClientSingleton();
            if(mGoogleApiClient == null) mGoogleApiClient = aGoogleApiClient;
        }
        return instance;
    }

    public GoogleApiClient getGoogleApiClient(){
        return mGoogleApiClient;
    }
}