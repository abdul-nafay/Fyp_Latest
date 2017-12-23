package com.sourcey.movnpack.Network;

import com.sourcey.movnpack.Helpers.API_TYPE;

import java.util.HashMap;

/**
 * Created by Abdul Nafay Waseem on 12/24/2017.
 */

public interface MessageAsyncInterface {
    void didCompleteWithTask(HashMap<String , String> data , API_TYPE apiType);
    void didFailToCompleteTask();
}
