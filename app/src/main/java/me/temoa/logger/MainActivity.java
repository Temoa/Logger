package me.temoa.logger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.temoa.library.Logger;

public class MainActivity extends AppCompatActivity {

    private static final String sJson = "{\n" +
            "  \"error\": false, \n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"_id\": \"5a389723421aa90fe72536c5\", \n" +
            "      \"createdAt\": \"2017-12-19T12:35:47.103Z\", \n" +
            "      \"desc\": \"Cipher.so: A simple way to encrypt your secure datas into a native .so library.\", \n" +
            "      \"publishedAt\": \"2018-01-16T08:40:08.101Z\", \n" +
            "      \"source\": \"web\", \n" +
            "      \"type\": \"Android\", \n" +
            "      \"url\": \"https://github.com/MEiDIK/Cipher.so\", \n" +
            "      \"used\": true, \n" +
            "      \"who\": \"drakeet\"\n" +
            "    }, \n" +
            "    {\n" +
            "      \"_id\": \"5a41af49421aa90fef2035c5\", \n" +
            "      \"createdAt\": \"2017-12-26T10:09:13.935Z\", \n" +
            "      \"desc\": \"Android rtmp rtsp \\u63a8\\u6d41\\u5ba2\\u6237\\u7aef\", \n" +
            "      \"publishedAt\": \"2018-01-16T08:40:08.101Z\", \n" +
            "      \"source\": \"web\", \n" +
            "      \"type\": \"Android\", \n" +
            "      \"url\": \"https://github.com/pedroSG94/rtmp-rtsp-stream-client-java\", \n" +
            "      \"used\": true, \n" +
            "      \"who\": \"\\u848b\\u670b\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logger(View view) {
        Logger.d("hello");
        Logger.e("hello");
        Logger.w("hello");
        Logger.v("hello");
        Logger.i("hello");
        Logger.wtf("hello");

        Logger.d("Hello %s", "Logger");

        Logger.tag("Logger").d("hello");

        Logger.lineNumber().d("hello");

        Logger.tag("Logger").lineNumber().d("hello");

        Logger.json(sJson);
    }
}
