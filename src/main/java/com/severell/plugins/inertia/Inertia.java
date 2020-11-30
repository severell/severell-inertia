package com.severell.plugins.inertia;

import com.severell.core.exceptions.ViewException;
import com.severell.core.http.NeedsRequest;
import com.severell.core.http.Response;

import java.io.IOException;
import java.util.HashMap;

public class Inertia extends NeedsRequest {

    private String rootView = "base.mustache";
    private static HashMap<String, Object> sharedProps = new HashMap<>();
    private static String version;

    public void setRootView(String view) {
        this.rootView = view;
    }

    public static void share(String key, Object value) {
        if(sharedProps == null) {
            sharedProps = new HashMap<>();
        }

        sharedProps.put(key, value);
    }

    public Object getShared() {
        return sharedProps;
    }

    public Object getShared(String key) {
        if(this.sharedProps != null) {
            return this.sharedProps.get(key);
        }

        return null;
    }

    public static String version() {
        return version;
    }

    public static void setVersion(String v) {
        version = v;
    }

    public void render(String component, HashMap<String,Object> props, Response response) throws IOException, ViewException {
        HashMap<String, Object> propsToPass = new HashMap<>(sharedProps);
        if(props != null) {
            propsToPass.putAll(props);
        }

        InertiaResponse resp  = new InertiaResponse(
            response,
            component,
            propsToPass,
            this.rootView,
            this.version()
        );

        resp.toResponse(this.request);
    }
}
