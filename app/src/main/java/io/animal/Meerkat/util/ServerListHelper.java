package io.animal.Meerkat.util;

import android.content.Context;
import android.content.ContextWrapper;

import java.util.HashMap;
import java.util.Map;

import io.animal.Meerkat.R;

public class ServerListHelper extends ContextWrapper {

    private Map<String, String> servers;

    public ServerListHelper(Context c) {
        super(c);
    }

    public Map getServers() {
        if (servers == null) {
            servers = parseStringArray(getServerResource());
        }
        return servers;
    }


    private Map<String, String> parseStringArray(int stringArrayResourceId) {
        String[] stringArray = getResources().getStringArray(stringArrayResourceId);
        HashMap<String, String> t = new HashMap<>();

        for (String entry : stringArray) {
            String[] splitResult = entry.split("\\|", 2);
            t.put(splitResult[0], splitResult[1]);
        }
        return t;
    }

    private int getServerResource() {
        return R.array.server_list;
    }
}
