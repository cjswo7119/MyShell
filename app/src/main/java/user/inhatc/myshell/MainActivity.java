package user.inhatc.myshell;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{
    // 테이블 생성
    SQLiteDatabase myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        myDB = this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

        myDB.execSQL("Create table if not exists User (Id text not null primary key, Password text not null, " +
                "Name text not null, Nickname text not null, Birth Integer not null, " +
                "Email text not null, Cntworry Integer not null, Lastlogin text not null);");
        /*
        * -User Table
        * Id : 아이디, 텍스트, 기본키
        * Password : 비밀번호, 텍스트
        * Name : 이름, 텍스트
        * Nickname : 별명, 텍스트
        * Birth : 생년월일, 숫자
        * Email : 이메일주소, 텍스트
        * Cntworry : 받은고민의수, 숫자
        * Lastlogin : 최근접속일, 텍스트
        * */

        myDB.execSQL("Create table if not exists Worry (Worryno Integer primary key autoincrement," +
                    "Content text not null, Date text not null, WriterId text not null, WriterNick text not null);");
        /*
         * -Worry Table
         * Worryno : 고민번호, 숫자, 기본키
         * Content : 고민내용, 텍스트
         * Date : 고민작성일, 텍스트
         * WriterId : 고민작성자, 아이디, 텍스트
         * WriterNick : 고민작성자, 텍스트
         * */

        myDB.execSQL("Create table if not exists Answer (Answerno Integer primary key autoincrement, Worryno Integer not null, " +
                    "Content text not null, Date text not null, WriterId text not null, WriterNick text not null);");

        /*
         * -Answer Table
         * Answerno : 답변번호, 숫자, 기본키
         * Worryno : 고민번호, 숫자
         * Content : 답변내용, 텍스트
         * Date : 답변작성일, 텍스트
         * WriterId : 답변작성자, 텍스트
         * WriterNick : 답변작성자, 텍스트
         * */

        myDB.execSQL("Create table if not exists Worrymatch (Matchno Integer not null primary key, Worryno Integer not null, Id text not null, Iswrited text not null);");
        /*
         * -WorryMatch Table
         * Matchno : 매치번호
         * Worryno : 고민번호
         * Id : 아이디
         * Iswrited : 작성유무 (T:답변작성완료, F:답변미작성)
         * */

        myDB.execSQL("Create table if not exists WorryBox (boxNo Integer primary key autoincrement, " +
                "worryNo Integer not null, worryWriterId Text not null, worryWriterNick Text not null, worryContent Text not null, worryDate Text not null," +
                "answerNo Integer, answerWriterId Text, answerWriterNick Text, answerContent Text, answerDate Text);");

        /*
        * -WorryBox Table
        * boxNo : 상자 번호
        * worryNo : 고민 번호, worryWriterId : 고민 작성자 ID, worryWriterNick : 고민 작성자 별명
        * worryContent : 고민 내용, worryDate : 고민 작성 날짜
        * answerNo : 답변 번호, answerWriterId : 답변 작성자 ID, answerWriterNick : 답변작 성자 별명
        * answerContent : 답변 내용, answerDate : 답변 작성 날짜
        * */

        Button btn_join = (Button)findViewById(R.id.btn_join);
        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_join.setOnClickListener(join_listener);
        btn_login.setOnClickListener(login_listener);

        if(getIntent() != null) { // 받은 인텐트가 있으면
            boolean isLogout = getIntent().getBooleanExtra("Logout", false);
            if (isLogout)
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener join_listener = new View.OnClickListener() {   // 회원가입 버튼 클릭
        @Override
        public void onClick(View v) {
            Intent joinIntent = new Intent(MainActivity.this, JoinActivity.class);
            startActivity(joinIntent);
        }
    };

    View.OnClickListener login_listener = new View.OnClickListener() {  // 로그인 버튼 클릭
        @Override
        public void onClick(View v) {
            EditText edt_id = (EditText)findViewById(R.id.edt_id);
            String Id = edt_id.getText().toString();
            Cursor idRCD = myDB.query("User", null, "Id='"+Id+"'", null, null, null, null, null);
            if (!idRCD.moveToFirst()) { // 아이디가 존재하지 않다면
                Toast.makeText(MainActivity.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            } else { // 아이디가 존재하는 경우
                EditText edt_password = (EditText) findViewById(R.id.edt_password);
                String Password = edt_password.getText().toString();
                String Nickname = idRCD.getString(3);

                if (idRCD.getString(1).equals(Password)) { // 패스워드 일치
                    Calendar cal = Calendar.getInstance();  // 현재시간 가져오기
                    cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
                    int year = cal.get(cal.YEAR);           // 현재 년도
                    int month = cal.get(cal.MONTH) + 1;     // 현재 월
                    int day = cal.get(cal.DATE);            // 현재 일
                    String strDate = year + "-" + month + "-" + day + "";
                    myDB.execSQL("Update User Set Lastlogin = '" + strDate + "' where id = '" + Id + "'"); // 접속한 유저의 최근 접속일 갱신

                    Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                    loginIntent.putExtra("ID", Id); // 아이디 전달
                    loginIntent.putExtra("NICKNAME", Nickname);
                    startActivityForResult(loginIntent, 1);
                    finish();

                    idRCD.close();
                    if (myDB != null) myDB.close();
                } else { // 패스워드 불일치
                    Toast.makeText(MainActivity.this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Log.i(this.getClass().getName(), "메시지");
            Toast.makeText(MainActivity.this, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
