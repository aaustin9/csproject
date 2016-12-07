
package toromu.familymap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONObject;

import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;

import toromu.familymap.models.Model;

/**
 * Login Fragment
 * Created by Austin on 16-Nov-16.
 */
public class LoginFragment extends Fragment {
    HttpClient httpClient;
    LoginTask loginTask;
    EditText username;
    EditText password;
    EditText serverHost;
    EditText serverPort;
    Button button;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.login_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        serverHost = (EditText) view.findViewById(R.id.serverHost);
        serverPort = (EditText) view.findViewById(R.id.serverPort);
        button = (Button) view.findViewById(R.id.submitButton);
        httpClient = new HttpClient();
        loginTask = new LoginTask();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = username.getText().toString();
                String passwordInput = password.getText().toString();
                String serverHostInput = serverHost.getText().toString();
                String serverPortInput = serverPort.getText().toString();

                if (usernameInput.length() == 0 || passwordInput.length() == 0 || serverHostInput.length() == 0 || serverPortInput.length() == 0) {
                    Toast.makeText(getContext(), "Each field must contain at least 1 character.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        URL url = new URL("http://" + serverHostInput + ":" + serverPortInput + "/user/login");
                        loginTask = new LoginTask();
                        loginTask.setQuery("{username:\"" + usernameInput + "\",password:\"" + passwordInput + "\"}");
                        loginTask.execute(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Login was unsuccessful!  Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    public class LoginTask extends AsyncTask<URL, Integer, Long> {
        String authorization, output, personId, query, server;
        boolean isFinished = true;
        NameTask nameTask;

        protected Long doInBackground(URL... urls) {
            isFinished = false;

            HttpClient httpClient = new HttpClient();
            server = urls[0].getHost() + ":" + urls[0].getPort();

            long totalSize = 0;

            for (int i = 0; i < urls.length; i++) {

                String urlContent = httpClient.postUrl(urls[i], query, null, "POST");
                if (urlContent != null) {
                    totalSize += urlContent.length();
                    output = urlContent;
                }

                int progress = 0;
                if (i == urls.length - 1) {
                    progress = 100;
                    isFinished = true;
                } else {
                    float cur = i + 1;
                    float total = urls.length;
                    progress = (int) ((cur / total) * 100);
                }
                publishProgress(progress);
            }

            return totalSize;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        protected void onPostExecute(Long result) {
//            totalSizeTextView.setText("Total Size: " + result);
            if (output.contains("Authorization")) {
//                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    authorization = jsonObject.getString("Authorization");
                    personId = jsonObject.getString("personId");
                    URL url = new URL("http://" + server + "/person/" + personId);
                    nameTask = new NameTask();
                    nameTask.setAuthorization(authorization);
                    nameTask.setServer(server);
                    nameTask.execute(url);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "An error occurred while accessing the file.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Login was unsuccessful!  Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class NameTask extends AsyncTask<URL, Integer, Long> {
        String authorization, output, server;

        protected Long doInBackground(URL... urls) {

            HttpClient httpClient = new HttpClient();

            long totalSize = 0;

            for (int i = 0; i < urls.length; i++) {

                String urlContent = httpClient.postUrl(urls[i], null, authorization, "GET");
                if (urlContent != null) {
                    totalSize += urlContent.length();
                    output = urlContent;
                }

                int progress = 0;
                if (i == urls.length - 1) {
                    progress = 100;
                } else {
                    float cur = i + 1;
                    float total = urls.length;
                    progress = (int) ((cur / total) * 100);
                }
                publishProgress(progress);
            }

            return totalSize;
        }

        public void setAuthorization(String authorization) {
            this.authorization = authorization;
        }

        public void setServer(String server) {
            this.server = server;
        }

        //        protected void onProgressUpdate(Integer... progress) {
//            progressBar.setProgress(progress[0]);
//        }
//
        protected void onPostExecute(Long result) {
            try {
                JSONObject jsonObject = new JSONObject(output);
                HttpClient httpClient = new HttpClient();
                URL url1 = new URL("http://" + server + "/person/");
                URL url2 = new URL("http://" + server + "/event/");
                PersonTask personTask = new PersonTask(server, authorization);
                personTask.execute(url1);
                EventTask eventTask = new EventTask(server, authorization);
                eventTask.execute(url2);
                Toast.makeText(getContext(),
                        jsonObject.getString("firstName")+" "+jsonObject.getString("lastName"),
                        Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(), query1, Toast.LENGTH_LONG).show();
//                MapFragment mf = new MapFragment();

//                setContentView(R.layout.activity_maps);
                MainActivity main = (MainActivity) getActivity();
                main.authenticate();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to access person information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class PersonTask extends AsyncTask<URL, Integer, Long> {
        String authorization, output, server;

        public PersonTask(String server, String authorization) {
            this.authorization = authorization;
            this.server = server;
        }

        protected Long doInBackground(URL... urls) {

            HttpClient httpClient = new HttpClient();

            long totalSize = 0;

            for (int i = 0; i < urls.length; i++) {

                String urlContent = httpClient.postUrl(urls[i], null, authorization, "GET");
                if (urlContent != null) {
                    totalSize += urlContent.length();
                    output = urlContent;
                }

                int progress = 0;
                if (i == urls.length - 1) {
                    progress = 100;
                } else {
                    float cur = i + 1;
                    float total = urls.length;
                    progress = (int) ((cur / total) * 100);
                }
                publishProgress(progress);
            }
            return totalSize;
        }

        protected void onPostExecute(Long result) {
            try {
                Model.SINGLETON.loadPersons(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class EventTask extends AsyncTask<URL, Integer, Long> {
        String authorization, output, server;

        public EventTask(String server, String authorization) {
            this.authorization = authorization;
            this.server = server;
        }

        protected Long doInBackground(URL... urls) {

            HttpClient httpClient = new HttpClient();

            long totalSize = 0;

            for (int i = 0; i < urls.length; i++) {

                String urlContent = httpClient.postUrl(urls[i], null, authorization, "GET");
                if (urlContent != null) {
                    totalSize += urlContent.length();
                    output = urlContent;
                }

                int progress = 0;
                if (i == urls.length - 1) {
                    progress = 100;
                } else {
                    float cur = i + 1;
                    float total = urls.length;
                    progress = (int) ((cur / total) * 100);
                }
                publishProgress(progress);
            }
            return totalSize;
        }

        protected void onPostExecute(Long result) {
            try {
                Model.SINGLETON.loadEvents(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
