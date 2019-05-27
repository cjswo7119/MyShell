package user.inhatc.myshell;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                "Nickname text not null, Name text not null, Birth Integer not null, " +
                "Email text not null, Cntworry Integer not null, Lastlogin text not null);");
        /*
        * -User Table
        * Id : 아이디, 텍스트, 기본키
        * Password : 비밀번호, 텍스트
        * Nickname : 닉네임, 텍스트
        * Name : 이름, 텍스트
        * Birth : 생년월일, 숫자
        * Email : 이메일주소, 텍스트
        * Cntworry : 받은고민의수, 숫자
        * Lastlogin : 최근접속일, 텍스트
        * */

        myDB.execSQL("Create table if not exists Worry (Worryno Integer primary key autoincrement, Title text not null, " +
                    "Content text not null, Date text not null, Writer text not null, State text not null);");
        /*
         * -Worry Table
         * Worryno : 고민번호, 숫자, 기본키
         * Title : 고민제목, 텍스트
         * Content : 고민내용, 텍스트
         * Date : 고민작성일, 텍스트
         * Writer : 고민작성자, 텍스트
         * State : 답변유무, 텍스트, T:작성됨, F:작성되지않음
         * */

        myDB.execSQL("Create table if not exists Answer (Answerno Integer not null primary key, Worryno Integer not null, " +
                    "Content text not null, Date text not null, Writer text not null);");

        /*
         * -Answer Table
         * Answerno : 답변번호, 숫자, 기본키
         * Worryno : 고민번호, 숫자
         * Content : 답변내용, 텍스트
         * Date : 답변작성일, 텍스트
         * Writer : 답변작성자, 텍스트
         * */

        myDB.execSQL("Create table if not exists Worrymatch (Id text not null primary key, Worryno Integer not null);");
        /*
         * -WorryMatch Table
         * Id : 아이디
         * Worryno : 고민번호
         * */

        Button btn_join = (Button)findViewById(R.id.btn_join);
        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_join.setOnClickListener(join_listener);
        btn_login.setOnClickListener(login_listener);
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
            Cursor idRCD = myDB.query("User", null, "Id = '"+Id+"'", null, null, null, null, null);
            if (!idRCD.moveToFirst()) { // 아이디가 존재하지 않다면
                Toast.makeText(MainActivity.this, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            } else { // 아이디가 존재하는 경우
                EditText edt_password = (EditText)findViewById(R.id.edt_password);
                String Password = edt_password.getText().toString();
                if(idRCD.getString(1).equals(Password)) { // 패스워드 일치
                    Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(loginIntent);
                } else { // 패스워드 불일치
                    Toast.makeText(MainActivity.this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
}
