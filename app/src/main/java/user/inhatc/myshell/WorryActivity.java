package user.inhatc.myshell;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WorryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worry);

        if (getIntent() != null) {
            int WorryNo = getIntent().getIntExtra("WorryNo", 0);
            String WorryContent = getIntent().getStringExtra("WorryContent");
            String WorryWriterNick = getIntent().getStringExtra("WorryWriterNick");
            String WorryDate = getIntent().getStringExtra("WorryDate");

            TextView worryContent = (TextView)findViewById(R.id.worryContent);
            String content = WorryDate + "에 작성한 고민...\n\n";
            content += WorryContent + "\n\n";
            content += "간절한 답장을 바라는 " + WorryWriterNick + "가...";
            worryContent.append(content);
        }

        Button answerCancel = (Button)findViewById(R.id.btn_answerCancel);

        answerCancel.setOnClickListener(listener_answerCancel);
    }

    View.OnClickListener listener_answerCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };
}
