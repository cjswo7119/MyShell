package user.inhatc.myshell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class writeWorry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_worry);

        Button btn_worryCancel = (Button)findViewById(R.id.btn_worryCancel);
        Button btn_worryWrite  = (Button)findViewById(R.id.btn_worryWrite);

        btn_worryCancel.setOnClickListener(listener_worryCancel);
        btn_worryWrite.setOnClickListener(listener_worryWrite);
    }

    View.OnClickListener listener_worryCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    View.OnClickListener listener_worryWrite = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
