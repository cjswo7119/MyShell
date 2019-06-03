package user.inhatc.myshell;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

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
            EditText edtWorry = (EditText)findViewById(R.id.edtWorry);
            try {
                SQLiteDatabase myDB = writeWorry.this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

                String Content = edtWorry.getText().toString();

                Calendar cal = Calendar.getInstance();  //현재시간 가져오기
                int year = cal.get(cal.YEAR);           //현재시간의 년도 가져오기
                int month = cal.get(cal.MONTH) + 1;
                int day = cal.get(cal.DATE);
                String nowDate = year + "-" + month + "-" + day + "";

                String strSQL = "Insert into Worry(Content, Date, WriterNick, WriterId) values('" + Content + "', '" + nowDate + "', '" + getIntent().getStringExtra("NICKNAME") + "', '" + getIntent().getStringExtra("ID") + "');"; // 고민 테이블에 저장
                myDB.execSQL(strSQL);

                Cursor minCntWorryRCD = myDB.query("User", new String[] {"MIN(CntWorry)"}, null, null, null, null, null, null);
                minCntWorryRCD.moveToFirst();
                int minCntWorry = minCntWorryRCD.getInt(0);

                Cursor minCntWorryUserRCD = myDB.query("User", null, "CntWorry = " + minCntWorry, null, null, null, null, null); // 가장 적은 고민의 개수를 가진 유저들의 레코드

                Calendar[] dateList = new Calendar[minCntWorryUserRCD.getCount()];

                for (int i=0 ; i < minCntWorryUserRCD.getCount() ; i++) { // Calendar 배열 dateList에 가장 적은 고민의 개수를 가진 유저들의 최근 접속일 정보를 저장함.
                    if (i==0) {
                        if(!minCntWorryUserRCD.moveToFirst()) return;
                    } else {
                        if (!minCntWorryUserRCD.moveToNext()) return;
                    }
                    String[] ymd = minCntWorryUserRCD.getString(7).split("-");
                    dateList[i] = Calendar.getInstance();
                    dateList[i].set(Calendar.HOUR_OF_DAY, 0);
                    dateList[i].set(Calendar.MINUTE, 0);
                    dateList[i].set(Calendar.SECOND, 0);
                    dateList[i].set(Calendar.MILLISECOND, 0);

                    dateList[i].set(Integer.parseInt(ymd[0]), Integer.parseInt(ymd[1]), Integer.parseInt(ymd[2]));

                    Log.i("Error", "에르어:" + dateList[i].get(dateList[i].YEAR));
                    Log.i("Error", "에르어:" + dateList[i].get(dateList[i].MONTH));
                    Log.i("Error", "에르어:" + dateList[i].get(dateList[i].DATE));
                    Log.i("Error", "에르어:==================================");
                }

                long max = 0;
                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.HOUR_OF_DAY, 0);
                maxDate.set(Calendar.MINUTE, 0);
                maxDate.set(Calendar.SECOND, 0);
                maxDate.set(Calendar.MILLISECOND, 0);

                for (int i=0 ; i<dateList.length ; i++) {
                    if(dateList[i].getTimeInMillis() > max)
                        maxDate.setTimeInMillis(dateList[i].getTimeInMillis());
                }

                String strMaxDate = maxDate.get(maxDate.YEAR) + "-" + maxDate.get(maxDate.MONTH) + "-" + maxDate.get(maxDate.DATE);
                Log.i("Error", "에르어:가장 최근 접속일은? " + strMaxDate);

                Cursor maxLastLoginUserRCD = myDB.query("User", null, "Lastlogin = '" + strMaxDate + "'", null, null, null, null, null);
                maxLastLoginUserRCD.moveToFirst();
                String maxLastLoginUserID = maxLastLoginUserRCD.getString(0); // 가장 최근에 접속한 유저의 아이디

                int intMinCntWorry = 0;
                Cursor maxWorryNoRCD = myDB.query("Worry", new String[] {"MAX(Worryno)"}, null, null, null, null, null, null);
                maxWorryNoRCD.moveToFirst();

                strSQL = "insert into Worrymatch(Worryno, Id, Iswrited) values(" + maxWorryNoRCD.getString(0) + ", '" + maxLastLoginUserID + "', 'F');";
                Log.i("에르어", strSQL);
                myDB.execSQL(strSQL);
                //setResult(RESULT_OK);
                //finish();

            } catch (Exception e) {
                Log.i("에르어", e.getMessage());
            }
        }
    };
}
