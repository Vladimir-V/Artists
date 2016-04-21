package artists.yandex.ru.artists;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created on Apr 21, 2016
 *
 * @author Vladimir Vedernikov
 */
public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();

        final TextView textView = (TextView) findViewById(R.id.description);

        textView.setText(intent.getStringExtra(MainActivity.DESCRIPTION_EXTRA));
    }
}
