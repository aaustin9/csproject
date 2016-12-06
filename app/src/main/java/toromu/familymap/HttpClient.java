package toromu.familymap;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * HTTP Client
 * Contains functions used to make HTTP requests for the FamilyMap program.
 * Created by Austin on 16-Nov-16.
 */
public class HttpClient {
    public String postUrl(URL url, String query, String authorization, String requestMethod) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);

            if(authorization != null) connection.addRequestProperty("Authorization", authorization);

            connection.setDoOutput(true);

//            connection.connect();

            if(query != null) {
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }

//            byte[] outputInBytes = str.getBytes("UTF-8");
//            OutputStream os = connection.getOutputStream();
//            os.write( outputInBytes );
//            os.close();


            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                return responseBodyData;
            }
        }
        catch (Exception e) {
            Log.e("HttpClient", e.getMessage(), e);
        }

        return null;
    }
}
