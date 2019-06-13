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
import java.util.TimeZone;

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

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));  // 현재 시간 가져오기
                int year = cal.get(cal.YEAR);           // 현재 년도
                int month = cal.get(cal.MONTH) + 1;     // 현재 월
                int day = cal.get(cal.DATE);            // 현재 일
                String nowDate = year + "-" + month + "-" + day + "";

                String strSQL = "Insert into Worry(Content, Date, WriterNick, WriterId) values('" + Content + "', '" + nowDate + "', '" +
                        getIntent().getStringExtra("NICKNAME") + "', '" + getIntent().getStringExtra("ID") + "');"; // 고민 테이블에 값을 넣는 SQL문
                myDB.execSQL(strSQL);

                Cursor minCntWorryRCD = myDB.query("User", new String[] {"MIN(CntWorry)"}, null, null, null, null, null, null);
                // select min(cntworry) from user;
                minCntWorryRCD.moveToFirst();
                int minCntWorry = minCntWorryRCD.getInt(0); // 가장 적게 받은 고민의 수
                Log.i("MagicShell", "가장 적게 받은 고민의 수 : " + minCntWorry);

                String[] args = {Integer.toString(minCntWorry), getIntent().getStringExtra("ID")}; // args[0]=가장 적은 고민의 수, args[1]=유저의 ID
                Cursor minCntWorryUserRCD = myDB.query("User", null, "CntWorry = ? and id <> ?", args, null, null, null, null);
                //Cursor minCntWorryUserRCD = myDB.query("User", null, "CntWorry = " + minCntWorry, null, null, null, null, null); // 가장 적은 고민의 개수를 가진 유저들의 레코드
                // select * from user where cntworry = minCntWorry; 가장 적은 고민을 받은 유저들을 불러옴

                Calendar[] dateList = new Calendar[minCntWorryUserRCD.getCount()]; // 가장 적은 고민을 받은 유저들의 날짜를 저장하기 위한 Calendar 객체 배열 선언.
                Log.i("MagicShell", " " + minCntWorryUserRCD.getCount());
                for (int i=0 ; i < minCntWorryUserRCD.getCount() ; i++) { // Calendar 배열 dateList에 가장 적은 고민을 받은 유저들의 최근 접속일(Lastlogin) 저장
                    if (i==0) minCntWorryUserRCD.moveToFirst();
                    else      minCntWorryUserRCD.moveToNext();

                    if (minCntWorryUserRCD == null) return;
                    Log.i("MagicShell", minCntWorryUserRCD.getString(7));
                    String[] ymd = minCntWorryUserRCD.getString(7).split("-"); // index 7 : lastlogin, 최근 접속일을 - 을 구분자로 쪼개어서 ymd 문자열 배열에 저장.
                    Log.i("MagicShell", ymd[0] + " " + ymd[1] + " " + ymd[2]);
                    dateList[i] = Calendar.getInstance();
                    //dateList[i].set(Calendar.MILLISECOND, 0);
                    // 현재 날짜를 받고, 자잘한 시간들을 0으로 초기화함.

                    dateList[i].set(Integer.parseInt(ymd[0]), (Integer.parseInt(ymd[1])-1), (Integer.parseInt(ymd[2])), 0, 0, 0);
                    // i 번째 유저의 최근 접속일의 년, 월, 일을 세팅.

                    Log.i("MagicShell", "" + dateList[i].get(dateList[i].YEAR));
                    Log.i("MagicShell", "" + (dateList[i].get(dateList[i].MONTH)+1));
                    Log.i("MagicShell", "" + dateList[i].get(dateList[i].DATE));
                    Log.i("MagicShell", "==================================");
                }
                // 각 유저들의 최근 접속일 Calendar 배열에 담기 완료.

                /*
                * 아래 라인부터 수행할 작업
                * 가장 적은 고민을 받은 유저들의 최근 접속일을
                * millisecond 단위로 환산하여 누가 가장 최근에 접속했는지를 판별함.
                * (millisecond 단위로 환산한 값이 가장 큰 유저가 가장 최근에 접속한 것임)
                * */

                long max = 0;

                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.HOUR_OF_DAY, 0);
                maxDate.set(Calendar.MINUTE, 0);
                maxDate.set(Calendar.SECOND, 0);
                maxDate.set(Calendar.MILLISECOND, 0);
                // 가장 최근의 날짜를 기억하기 위한 Calendar 변수.

                for (int i=0 ; i<dateList.length ; i++) {
                    if(dateList[i].getTimeInMillis() > max) { // 유저의 최근 접속일을 milliSeconds로 환산한 값이 max보다 크다면? = 가장 최근의 날짜
                        maxDate.setTimeInMillis(dateList[i].getTimeInMillis());
                        max = dateList[i].getTimeInMillis();
                        // maxDate = 가장 최근의 접속일을 날짜 형태로 담는 변수, max = 비교를 위해 millisecond 단위를 담을 수 있는 long 타입의 변수
                    }
                }
                // 가장 최근의 접속일을 구해서 maxDate에 날짜 형태로 담았다.

                String strMaxDate = maxDate.get(maxDate.YEAR) + "-" + (maxDate.get(maxDate.MONTH)+1) + "-" + maxDate.get(maxDate.DATE); // 가장 최근 접속일을 DB에 넣기 좋게 String 타입으로 변환.
                Log.i("MagicShell", "가장 적게 받은 유저들 중 가장 최근 접속일 : " + strMaxDate);

                args[0] = strMaxDate; args[1] = Integer.toString(minCntWorry); // Cursor의 조건문에 넣기 위한 String 배열.
                Cursor maxLastLoginUserRCD = myDB.query("User", null, "Lastlogin = ? and Cntworry = ?", args, null, null, null, null); // select * from user where lastlogin = '날짜' and cntworry = '숫자';
                // select * from user where Lastlogin = '가장 최근 접속일' and Cntworry = '가장 적게 받은 고민의 수';
                // 가장 고민을 적게 받았고, 최근 접속일이 가장 최신인 유저를 불러온다.

                maxLastLoginUserRCD.moveToFirst();
                String maxLastLoginUserID = maxLastLoginUserRCD.getString(0); // 가장 고민을 적게 받았고, 최근 접속일이 가장 최신인 유저의 아이디 저장.

                Cursor maxWorryNoRCD = myDB.query("Worry", new String[] {"MAX(Worryno)"}, null, null, null, null, null, null);
                // select max(Worryno) from Worry; 고민 번호 중 가장 큰 값을 불러온다.
                maxWorryNoRCD.moveToFirst();
                String maxNo = maxWorryNoRCD.getString(0);

                strSQL = "insert into Worrymatch(Worryno, Id, Iswrited) values(" + maxNo + ", '" + maxLastLoginUserID + "', 'F');";
                // 내부 DB(SQLite)를 사용시에는 문제가 되지 않을 부분이지만, 외부 DB로 구현 시 문제가 생길 가능성이 가장 다분한 코드.
                // 코드의 수행 순서상 Worry 테이블에 고민을 넣자마자 알고리즘에 따른 유저에게 고민을 전송하기 때문에 (Worrymatch 테이블에 레코드 삽입)
                // Worryno가 1씩 증가하는 Worry 테이블의 특성상 방금 작성한 고민번호와 매칭됨.
                // 하지만 외부 DB로 구현 시에는 여러 명이 동시에 작성하면서 오류가 발생할 수 있음.
                myDB.execSQL(strSQL);

                strSQL = "insert into WorryBox(worryNo, worryWriterId, worryWriterNick, worryContent, worryDate)" +
                        "values(" + maxNo + ", '" + getIntent().getStringExtra("ID") + "', '" + getIntent().getStringExtra("NICKNAME") +
                        "', '" + Content + "', '" + nowDate + "');";
                Log.i("MagicShell", strSQL);
                myDB.execSQL(strSQL);

                strSQL = "update User set Cntworry = Cntworry+1 where id = '" + maxLastLoginUserID + "'";
                // update User set Cntworry = Cntworry+1 where id = '가장 적게 고민을 받고 가장 최근에 접속한 유저의 아이디';
                // 고민을 받은 유저의 받은 고민의 수를 1 증가시켜주는 코드

                myDB.execSQL(strSQL);
                minCntWorryRCD.close();
                minCntWorryUserRCD.close();
                maxLastLoginUserRCD.close();
                maxWorryNoRCD.close();

                setResult(RESULT_OK);
                finish();

            } catch (Exception e) {
                Log.i("MagicShell", e.getMessage());
            }
        }
    };
}
