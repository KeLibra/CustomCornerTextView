package test.kezy.com.customcornertextview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import test.kezy.com.customcornertextview.view.CoinPetView;

public class Main2Activity extends AppCompatActivity {

    private RelativeLayout rl_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        rl_layout = findViewById(R.id.rl_layout);


        CoinPetView view = new CoinPetView(this);

        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        view.setLayoutParams(lp);
        view.setCoinCount("1000");

        rl_layout.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "点击事件啊", Toast.LENGTH_SHORT).show();
            }
        });

//        ViewManager.getInstance().setContext(this).showFloatBall();

    }
}
