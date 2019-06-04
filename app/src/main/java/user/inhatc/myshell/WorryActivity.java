package user.inhatc.myshell;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WorryActivity extends AppCompatActivity {

    int WorryNo;
    String WorryContent;
    String WorryWriterNick;
    String WorryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worry);

        if (getIntent() != null) {
            WorryNo = getIntent().getIntExtra("WorryNo", 0);
            WorryContent = getIntent().getStringExtra("WorryContent");
            WorryWriterNick = getIntent().getStringExtra("WorryWriterNick");
            WorryDate = getIntent().getStringExtra("WorryDate");

            TextView worryContent = (TextView)findViewById(R.id.worryContent);
            String content = WorryDate + "에 작성한 고민...\n\n";
            content += WorryContent + "\n\n";
            content += "간절한 답장을 바라는 " + WorryWriterNick + "가...";
            worryContent.append(content);
        }

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
            Intent writeAnswerIntent = new Intent(WorryActivity.this, writeAnswer.class);
            writeAnswerIntent.putExtra("WorryNo", WorryNo);                                       // 작성할 고민의 고민번호 인텐트에 저장
            writeAnswerIntent.putExtra("ID", getIntent().getStringExtra("ID"));             // 답변 작성자의 ID 인텐트에 저장
            writeAnswerIntent.putExtra("NICKNAME", getIntent().getStringExtra("NICKNAME")); // 답변 작성자의 닉네임 인텐트에 저장
            startActivityForResult(writeAnswerIntent, 2000); // 답변 작성 : 2000
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) { // 답변 작성하고 나온 결과
            if (requestCode == 2000) {
                Intent numberIntent = getIntent();
                numberIntent.putExtra("Number", getIntent().getIntExtra("Number", -1));
                setResult(RESULT_OK, numberIntent);
                finish();
            }
        }
    }
}
