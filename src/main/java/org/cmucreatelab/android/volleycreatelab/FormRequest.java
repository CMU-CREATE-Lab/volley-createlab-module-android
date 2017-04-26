package org.cmucreatelab.android.volleycreatelab;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 4/25/17.
 *
 * Abstract class for HTTP Forms. The default Content-Type format is application/x-www-form-urlencoded (for multipart/form-data see {@link MultipartFormRequest}).
 */
public abstract class FormRequest<T> extends Request<T> {

    private HashMap<String,String> params;


    public FormRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        this.params = new HashMap<>();
    }


    /**
     * Defaults to the method defined in Request, which sends in urlencoded format.
     *
     * @throws AuthFailureError in the event of auth failure
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        return super.getBody();
    }


    public Map<String, String> getParams() {
        return params;
    }


    /**
     * Subclasses must implement this to parse the raw network response
     * and return an appropriate response type. This method will be
     * called from a worker thread.  The response will not be delivered
     * if you return null.
     * @param response Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected abstract Response<T> parseNetworkResponse(NetworkResponse response);


    /**
     * Subclasses must implement this to perform delivery of the parsed
     * response to their listeners.  The given response is guaranteed to
     * be non-null; responses that fail to parse are not delivered.
     * @param response The parsed response returned by
     * {@link #parseNetworkResponse(NetworkResponse)}
     */
    @Override
    protected abstract void deliverResponse(T response);

}
