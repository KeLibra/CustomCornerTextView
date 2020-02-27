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
    private Button btn_up, btn_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ntv_text = findViewById(R.id.ntv_text);
        et_input = findViewById(R.id.et_input);
        btn_up = findViewById(R.id.btn_up);

        btn_down = findViewById(R.id.btn_down);

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    num = "0.00";
                }
                ntv_text.setNumText(NumChangeTextView.REFRESH_TYPE.TYPE_UP, num, 20, 50);
            }
        });

        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_input.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    num = "0.00";
                }
                ntv_text.setNumText(NumChangeTextView.REFRESH_TYPE.TYPE_DOWN, num, 20, 50);
            }
        });
    }
}
