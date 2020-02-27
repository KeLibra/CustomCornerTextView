package test.kezy.com.customcornertextview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import test.kezy.com.customcornertextview.view.NumChangeTextView;

public class Main3Activity extends AppCompatActivity {

    private NumChangeTextView ntv_text;
    private EditText et_input;
    private Button btn_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ntv_text = findViewById(R.id.ntv_text);
        et_input = findViewById(R.id.et_input);
        btn_show = findViewById(R.id.btn_show);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    num = "0.00";
                }
                ntv_text.setNumText(num, 100, 50);
            }
        });
    }
}
