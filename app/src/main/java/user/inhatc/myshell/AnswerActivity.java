package user.inhatc.myshell;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AnswerActivity extends AppCompatActivity {

    int AnswerNo;
    int WorryNo;
    String AnswerContent;
    String AnswerDate;
    String AnswerWriterId;
    String AnswerWriterNick;
    String WriterId;
    String WriterNick;

    int Number;
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        if (getIntent() != null) {
            myDB = AnswerActivity.this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

            AnswerNo = getIntent().getIntExtra("AnswerNo", 0);
            WorryNo = getIntent().getIntExtra("WorryNo", 0);
            AnswerContent = getIntent().getStringExtra("AnswerContent");
            AnswerDate = getIntent().getStringExtra("AnswerDate");
            AnswerWriterId = getIntent().getStringExtra("AnswerWriterId");
            AnswerWriterNick = getIntent().getStringExtra("AnswerWriterNick");
            WriterId = getIntent().getStringExtra("ID");
            WriterNick = getIntent().getStringExtra("NICK");
            Number = getIntent().getIntExtra("Number", -1);

            TextView answerContent = (TextView)findViewById(R.id.answerContent);
            String content = AnswerDate + "에 작성한 답변...\n\n";
            content += AnswerContent + "\n\n";
            content += "친애하는 " + AnswerWriterNick + "이(가)...";
            answerContent.append(content);
        }

        Button btn_answerReadCancel    = (Button)findViewById(R.id.btn_answerReadCancel);
        Button btn_answerReadComplete  = (Button)findViewById(R.id.btn_answerReadComplete);
        btn_answerReadCancel.setOnClickListener(listener_answerReadCancel);
        btn_answerReadComplete.setOnClickListener(listener_answerReadComplete);
    }

    View.OnClickListener listener_answerReadCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    View.OnClickListener listener_answerReadComplete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AnswerActivity.this);
            builder.setTitle("답변을 확인합니다.");
            builder.setMessage("화면에서 물병이 사라집니다.");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                myDB.execSQL("Delete From Worry Where Worryno = " + WorryNo + ";");
                                myDB.execSQL("Delete From Answer Where Answerno = " + AnswerNo + ";");
                                myDB.execSQL("Delete From Worrymatch Where Worryno = " + WorryNo + ";");
                                Intent answerReadIntent = new Intent();
                                answerReadIntent.putExtra("Number", Number);
                                setResult(RESULT_OK, answerReadIntent);
                                finish();
                            } catch (Exception e) {
                                Log.i("MagicShell", e.getMessage());
                            }
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
    };
}
