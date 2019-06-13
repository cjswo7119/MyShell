package user.inhatc.myshell;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

public class writeAnswer extends AppCompatActivity {

    int worryNo;
    String writerId;
    String writerNick;
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);

        if (getIntent() != null) {
            worryNo = getIntent().getIntExtra("WorryNo", -1);    // 작성할 답변의 고민번호
            writerId = getIntent().getStringExtra("ID");                    // 답변 작성자의 아이디
            writerNick = getIntent().getStringExtra("NICKNAME");            // 답변 작성자의 닉네임
        }

        Button btn_answerCancel = (Button)findViewById(R.id.btn_answerCancel);
        Button btn_answerWrite  = (Button)findViewById(R.id.btn_answerWrite);

        btn_answerCancel.setOnClickListener(listener_answerCancel);
        btn_answerWrite.setOnClickListener(listener_answerWrite);
    }

    View.OnClickListener listener_answerCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };

    View.OnClickListener listener_answerWrite = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myDB = writeAnswer.this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

            EditText edtAnswerContent = (EditText)findViewById(R.id.edtAnswer);
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));  // 현재 시간 가져오기
            int year = cal.get(cal.YEAR);           // 현재 년도
            int month = cal.get(cal.MONTH) + 1;     // 현재 월
            int day = cal.get(cal.DATE);            // 현재 일
            String nowDate = year + "-" + month + "-" + day + "";

            String strSQL = "Insert into Answer(Worryno, Content, Date, WriterId, WriterNick) values(" + worryNo + ", '" + edtAnswerContent.getText().toString() +
                    "', '" + nowDate + "', '" + writerId + "', '" + writerNick + "');";
            myDB.execSQL(strSQL);

            strSQL = "Update Worrymatch Set Iswrited = 'T' Where Worryno = " + worryNo + ";";
            myDB.execSQL(strSQL);

            // strSQL = "Update WorryBox Set ";

            setResult(RESULT_OK);
            finish();
        }
    };
}
