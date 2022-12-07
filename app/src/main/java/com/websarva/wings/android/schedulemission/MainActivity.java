package com.websarva.wings.android.schedulemission;




import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import static com.websarva.wings.android.schedulemission.DBContract.DBEntry;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private SampDatabaseHelper helper = null;
    MainListAdapter sc_adapter;

    private TextView text_date;
    private TextView text_date2;
    private TextView text_date3;
    private TextView text_date4;




    // アクティビティ初期化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //定義
        text_date  = findViewById(R.id.title);
            text_date2 = findViewById(R.id.text);
            text_date3 = findViewById(R.id.date);
            text_date4 = findViewById(R.id.time);


    }

    // アクティビティ再開
    @Override
    protected void onResume() {
        super.onResume();
        // データを一覧表示
        onShow();
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new SampDatabaseHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {DBEntry._ID, DBEntry.COLUMN_NAME_TITLE, DBEntry.COLUMN_NAME_CONTENTS
                ,DBEntry.COLUMN_NAME_DATE,DBEntry.COLUMN_NAME_TIME };

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            // 検索結果から取得する項目を定義
            String[] from = {DBEntry.COLUMN_NAME_TITLE};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title};

            // ListViewの検索結果を関連付け
            sc_adapter = new MainListAdapter(
                    this, R.layout.row_main, cursor, from, to,0);

            // ListViewオブジェクトを取得
            ListView list = findViewById(R.id.mainList);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたとき
            list.setOnItemClickListener((av, view, position, id) -> {
                //list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                //　クリックされた行のデータを取得
                Cursor cursor1 = (Cursor)av.getItemAtPosition(position);

                // テキスト登録画面 Activity へのインテントを作成
                Intent intent2  = new Intent(MainActivity.this, TextActivity.class);

                //データを取得
                intent2.putExtra(DBEntry._ID, cursor1.getInt(0));
                    intent2.putExtra(DBEntry.COLUMN_NAME_TITLE, cursor1.getString(1));
                    intent2.putExtra(DBEntry.COLUMN_NAME_CONTENTS, cursor1.getString(2));
                    intent2.putExtra(DBEntry.COLUMN_NAME_DATE, cursor1.getString(3));
                    intent2.putExtra(DBEntry.COLUMN_NAME_TIME, cursor1.getString(4));


                //データを指定の型に入れる
                id = intent2.getIntExtra(DBEntry._ID,0);
                    String title = intent2.getStringExtra(DBEntry.COLUMN_NAME_TITLE);
                    String contents = intent2.getStringExtra(DBEntry.COLUMN_NAME_CONTENTS);
                    String date = intent2.getStringExtra(DBEntry.COLUMN_NAME_DATE);
                    String time = intent2.getStringExtra(DBEntry.COLUMN_NAME_TIME);


                //表示
                text_date.setText(title);
                    text_date2.setText(contents);
                    text_date3.setText( date + " " + time );


                //時間 抽出
                String ans1 = date.substring(0, 4 );//年
                int year = Integer.parseInt(ans1);

                    String ans2 = date.substring( 5);//月
                    ans2 = ans2.substring(0, 2);//月
                    int month = Integer.parseInt(ans2);

                    String ans3 = date.substring( 8);//日
                    int date1 = Integer.parseInt(ans3);

                    //String t1 = time.substring(0, 2 );//時
                    //int  time1= Integer.parseInt(t1);

                    //String t2 = time.substring( 3 );//分
                    //int  time2= Integer.parseInt(t2);



                //予定時間_定義
                Calendar calendar1 = Calendar.getInstance();
                month = month -1;
                calendar1.set(year, month, date1);




                long timeMillis1 = calendar1.getTimeInMillis();
                    // 現在時刻のミリ秒
                    long currentTimeMillis = System.currentTimeMillis();
                    // 差分のミリ秒
                    long diff = timeMillis1 - currentTimeMillis;


                // ミリ秒から秒へ変換
                diff = diff / 1000;
                    // minutes
                    diff = diff / 60;
                    // hour
                    diff = diff / 60;
                    // day
                    diff = diff / 24;


                //データ表示
                String str = "あと" + diff + " 日";
                text_date4.setText( str );


            });
        }
    }




    // 削除ボタン　タップ時に呼び出される
    public void btnDel_onClick(View view){

        // リスト内の位置を取得
        int pos = (Integer)view.getTag();

        // _idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        // データ削除
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(DBEntry.TABLE_NAME, DBEntry._ID+" = ?", new String[] {String.valueOf(id)});
        }

        // データ一覧表示
        onShow();
    }

    // 「+」フローティング操作ボタン　タップ時に呼び出される
    public void fab_reg_onClick(View view) {

        // 登録画面 作成
        Intent intent  = new Intent(MainActivity.this, com.websarva.wings.android.schedulemission.TextActivity.class);

        // 起動
        startActivity(intent);
    }
}