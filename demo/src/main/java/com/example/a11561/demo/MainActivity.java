package com.example.a11561.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bmcopy;
    private Canvas mcanvas;
    private int startx;
    private int starty;
    private Paint paint;
    private Button red,bule,shua,save;
    private Bitmap bitmap;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView=findViewById(R.id.image);
        red=findViewById(R.id.bt_red);
        bule=findViewById(R.id.bt_bule);
        shua=findViewById(R.id.bt_shua);
        save=findViewById(R.id.bt_save);


        //加载原图
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        //创建白纸，宽高
        bmcopy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        //创建画板，参数就是白纸对象
        mcanvas = new Canvas(bmcopy);
        //创建画笔
        paint = new Paint();
        //在纸上作画
        mcanvas.drawBitmap(bitmap,new Matrix(), paint);

        //手势识别器和画笔画板相结合
        //给控件设置手势识别器，可以得到用户在这个控件上滑动的坐标
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startx = (int)event.getX();
                        starty = (int)event.getY();
                        System.out.print("startx"+ startx +"****************"+"starty"+ starty);
                        Log.i("aaa","startx"+ startx +"starty"+ starty);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endx= (int)event.getX();
                        int endy = (int)event.getY();
                        System.out.print("startx"+endx+"****************"+"starty"+endy);
                        //在背景图画线，画一条线，起点(x,y),终点(x,y)
                        mcanvas.drawLine(startx, starty,endx,endy, paint);
                        //最后把终点变成起点
                        startx=endx;
                        starty=endy;
                        imageView.setImageBitmap(bmcopy);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.RED);

            }
        });
        bule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(Color.BLUE);

            }
        });
        shua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setStrokeWidth(5);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据当前时间当做文件夹
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());//获取当前时间
                //
                String str= format.format(date) + "paint.png";
                File file = new File("sdcard/" + str);
                FileOutputStream stream=null;
                try {
                    stream=new FileOutputStream(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bmcopy.compress(Bitmap.CompressFormat.PNG,100,stream);
                ////发送Sd卡的就绪广播,要不然在手机图库中不存在
                Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                Toast.makeText(MainActivity.this,"已经保存图片",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
