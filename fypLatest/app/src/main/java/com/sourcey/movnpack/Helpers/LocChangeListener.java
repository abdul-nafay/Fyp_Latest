package com.sourcey.movnpack.Helpers;

import android.location.Location;

/**
 * Created by abdul on 12/10/17.
 */

public interface LocChangeListener {

    void locationDidChanged(Location loc);
    void failedToGetLocationWithError(String error);

}
