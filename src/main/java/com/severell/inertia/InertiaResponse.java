package com.severell.inertia;

import com.severell.core.exceptions.ViewException;
import com.severell.core.http.Request;
import com.severell.core.http.Response;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class InertiaResponse  {

    private String component;
    private HashMap<String, Object> props;
    private String version;
    private Response response;
    private String rootView;

    public InertiaResponse(Response response, String component, HashMap<String, Object> propsToPass, String rootView, String version) {
        this.component = component;
        this.props = propsToPass;
        this.version = version;
        this.response = response;
        this.rootView = rootView;
    }

    public void toResponse(Request request) throws IOException, ViewException {
        String[] only = null;
        if(request.getHeader("X-Inertia-Partial-Data") != null) {
            only = request.getHeader("X-Inertia-Partial-Data").split(",");
        }

        HashMap<String, Object> props;
        if(only != null  && component.equals(request.getHeader("X-Inertia-Partial-Component"))) {
            props = new HashMap<>();
            for(String key : only) {
                props.put(key, this.props.get(key));
            }
        } else {
            props = this.props;
        }

        HashMap<String, Object> page = new HashMap<>();
        page.put("component", this.component);
        page.put("props", props);
        page.put("url", request.getRequestURI());
        page.put("version", this.version);
        JSONObject obj = new JSONObject();
        obj.putAll(page);

        if(request.getHeader("X-Inertia") != null) {
            try {


                response.setContentType("application/json");
                response.setHeader("Vary", "Accept");
                response.setHeader("X-Inertia", "true");
                response.getWriter().write(obj.toJSONString());
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(obj.toJSONString());
        props.put("page", obj.toJSONString());
        response.render(this.rootView, props);
    }
}
