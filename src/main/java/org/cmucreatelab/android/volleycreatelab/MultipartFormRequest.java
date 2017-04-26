package org.cmucreatelab.android.volleycreatelab;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by mike on 4/26/17.
 *
 * Abstract class for sending HTTP requests using multipart/form-data.
 * For params, your "key" is the full header and your "value" is the content.
 */
public abstract class MultipartFormRequest<T> extends FormRequest<T> {

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE = "multipart/form-data";

    /** This gets placed before/after the boundary. */
    private static final String BOUNDARY_PREFIX_SUFFIX = "--";

    /** Specified length of randomly-generated boundaries. */
    private static final int BOUNDARY_LENGTH = 20;

    private String boundary;


    public MultipartFormRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        this.boundary = generateRandomBoundary();
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


    // Override (specific for multipart/form-data)


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        // Volley appears to use "Accept-Encoding: gzip" somewhere, but including this will override that preference.
        headers.put("Accept", "*/*");
        return headers;
    }


    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE+"; charset=" + getParamsEncoding()+"; boundary=" + boundary;
    }


    /**
     * Returns the raw body to be sent, using multipart/form-data
     *
     * @throws AuthFailureError in the event of auth failure
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    /**
     * Converts <code>params</code> into a multipart/form-data encoded string.
     */
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(boundary, paramsEncoding));
                encodedParams.append('\n');
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('\n');
                encodedParams.append('\n');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('\n');
                encodedParams.append(URLEncoder.encode(BOUNDARY_PREFIX_SUFFIX, paramsEncoding));
            }
            encodedParams.append(URLEncoder.encode(boundary, paramsEncoding));
            encodedParams.append(URLEncoder.encode(BOUNDARY_PREFIX_SUFFIX, paramsEncoding));
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


    // Helper methods


    private static String generateRandomBoundary() {
        String result = "";

        Random rand = new Random();
        for (int i=0;i<BOUNDARY_LENGTH;i++) {
            result = result.concat(Integer.toHexString(rand.nextInt(16)));
        }

        return result;
    }

}
