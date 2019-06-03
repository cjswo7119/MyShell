package user.inhatc.myshell;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/////
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "새로고침 하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent callIntent = getIntent();
        String strID = callIntent.getStringExtra("ID"); // 전달받은 ID값
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.textView);
        navUsername.setText(strID + " 님"); // 환영합니다 ID 님

        // 고민 출력
        SQLiteDatabase myDB = this.openOrCreateDatabase("MagicShell", MODE_PRIVATE, null);

        Cursor worryNoRCD = myDB.query("Worrymatch", new String[] {"Worryno"}, "Id='"+strID+"'", null, null, null, null, null);
        // select Worryno from Worrymatch where Id = '아이디'

        Shell[] worries = new Shell[10]; // 이미지 객체 배열 생성
        int j=0;
        for (int i=0 ; i<worries.length ; i++) {
            if (i==0) {
                if (!worryNoRCD.moveToFirst()) break;
            } else {
                if (!worryNoRCD.moveToNext()) break;
            }
            int worryNo = worryNoRCD.getInt(0);
            worries[i] = new Shell(this);
            worries[i].setImageResource(R.drawable.shell);

            //Cursor worryRCD = myDB.query("Worry", null, "Worryno = '" + Integer.toString(worryNo) + "'", null, null, null, null, null);
            Cursor worryRCD = myDB.query("Worry", null, "Worryno='"+worryNo+"'", null, null, null, null, null);
            if (j == 0) worryRCD.moveToFirst();
            else worryRCD.moveToNext();
            j++;

            worries[i].setWorryNo(Integer.parseInt(worryRCD.getString(0)));
            worries[i].setContent(worryRCD.getString(2));
            worries[i].setDate(worryRCD.getString(3));
            worries[i].setWriter(worryRCD.getString(4));

            worryRCD.close();
            Random random = new Random(); // 랜덤 객체 선언

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;  // 안드로이드 화면의 가로 길이
            int height = size.y; // 안드로이드 화면의 세로 길이
            worries[i].setX(random.nextInt(width - worries[i].getWidth()) - worries[i].getWidth()/2);                                   // X축 범위 : 0   ~ 화면 크기
            worries[i].setY(random.nextInt(height - height/11 + 1 - worries[i].getHeight()) + height/11 - worries[i].getHeight()/2);    // Y축 범위 : 280 ~ 화면 크기
            worries[i].setLayoutParams(new DrawerLayout.LayoutParams(width/6, height/6));
            // addContentView(worries[i], new DrawerLayout.LayoutParams(width/6, height/6));
            CoordinatorLayout cLayout = (CoordinatorLayout)findViewById(R.id.coordinator);

            cLayout.addView(worries[i]);
            worries[i].setOnClickListener(ShellListener);
        }
    }

    View.OnClickListener ShellListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v = (Shell)v;
            Intent writeWorry = new Intent(HomeActivity.this, WorryActivity.class);
            writeWorry.putExtra("WorryNo", ((Shell) v).getWorryNo());
            writeWorry.putExtra("WorryContent", ((Shell) v).getContent());
            writeWorry.putExtra("WorryWriter", ((Shell) v).getWriter());
            writeWorry.putExtra("WorryDate", ((Shell) v).getDate());
            startActivityForResult(writeWorry, 1000); // 고민작성 : 1000
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_write) {
            Intent writeIntent = new Intent(HomeActivity.this, writeWorry.class);
            writeIntent.putExtra("ID", getIntent().getStringExtra("ID"));
            writeIntent.putExtra("NICKNAME", getIntent().getStringExtra("NICKNAME"));
            startActivityForResult(writeIntent, 1000); // 고민작성 : 1000
        } else if (id == R.id.nav_myWorries) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_logout) {
            Intent logoutIntent = new Intent(HomeActivity.this, MainActivity.class);
            logoutIntent.putExtra("Logout", true);
            startActivity(logoutIntent);
        } else if (id == R.id.nav_closeAccount) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) { // 고민작성하고 나온 결과
                Toast.makeText(HomeActivity.this, "고민이 누군가에게 전송되었습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(HomeActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT);
        }

    }
}