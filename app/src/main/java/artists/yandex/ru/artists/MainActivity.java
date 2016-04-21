package artists.yandex.ru.artists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vladimir Vedernikov
 */
public class MainActivity extends AppCompatActivity {

    // For some reason encoding is not specified in http headers
    // and the default encoding is used which is
    // org.apache.http.protocol.HTTP.DEFAULT_CONTENT_CHARSET = "ISO-8859-1"
    // but in reality it's Unicode
    final static String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

    public static final String DESCRIPTION_EXTRA = "DESCRIPTION_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Map<Integer, JSONObject> objectMap = new HashMap<>();

        final Map<Integer, Integer> positionMap = new HashMap<>();

        final TextView textView = (TextView) findViewById(R.id.textView);
        final ListView listView = (ListView) findViewById(R.id.list);

        final List<String> arrayList = new ArrayList<>();

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                arrayList );

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://download.cdn.yandex.net/mobilization-2016/artists.json";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(new String(response.getBytes(DEFAULT_CONTENT_CHARSET)));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                final JSONObject jsonObject = ((JSONObject) jsonArray.get(i));
                                objectMap.put(jsonObject.getInt("id"), jsonObject);
                                positionMap.put(i, jsonObject.getInt("id"));
                                arrayList.add(jsonObject.getString("name"));

                            }
                            listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            String error = "Error parsing JSON" + response;
                            textView.setText(error);
                        } catch (UnsupportedEncodingException ex) {
                            String error = "Bad encoding" + DEFAULT_CONTENT_CHARSET;
                            textView.setText(error);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Set Click Listener to open activity with description
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                try {
                    Integer objectId = positionMap.get(position);
                    Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
                    intent.putExtra(DESCRIPTION_EXTRA, objectMap.get(objectId).getString("description"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
