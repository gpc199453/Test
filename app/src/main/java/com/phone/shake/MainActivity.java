package com.phone.shake;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;

    //震动器️的类
    Vibrator vibrator;
    //传感器的监听器
    SensorEventListener  eventListener;

    //播放音频
    SoundPool soundPool;

    int soundId=0;
    ImageView img_up;

    ImageView img_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //img_up = (ImageView) findViewById(R.id.img_up);
        //img_down = (ImageView) findViewById(R.id.img_down);



        //创建soundPool对象
        /**
         * 参数1:通过的流的最大数量一般1-10
         * 参数2:流的类型AudioManager.STREAM_MUSIC
         * 参数3:质量转化率，无效，默认0
         */
        soundPool =new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        //获取音频文件的id
        soundId = soundPool.load(this,R.raw.shake,0);



        //获取震动器的对象
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //获取传感器的管理类
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //获取手机种所有的传感器
//        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
//        for (int i = 0; i <sensorList.size(); i++) {
//            //获取具体的一个传感器的对象
//            Sensor sensor = sensorList.get(i);
//            Log.e("======","===名称==="+sensor.getName());
//            Log.e("======","===供应商==="+sensor.getVendor());
//            Log.e("======","===功率==="+sensor.getPower());
//            Log.e("======","===精确度==="+sensor.getResolution());
//        }

        //查找加速度传感器的对象
        Sensor sensor =  sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        eventListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent event) {
                //传感器发生变化的时候调用
                //从event得到传感器变化的值
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                Log.e("=====","===x===="+x);
                Log.e("=====","===y===="+y);
                Log.e("=====","===z===="+z);

                if ((Math.abs(x)>19||Math.abs(y)>19||Math.abs(z)>19)){
                    Log.e("=====","===表示手机摇动====");
                    //播放震动
                    vibrator.vibrate(2000);
                    /**
                     * 参数1:音频文件的id，通过load()获取
                     * 参数2:左音量0-1
                     * 参数3:右音量0-1
                     * 参数4:流的优先级最低为0
                     * 参数5:循环模式，0不循环，－1无限循环，大于0表示循环次数
                     * 参数6:播放的速率，正常是1，范围是0.5-2
                     */
                    soundPool.play(soundId,0,1,0,-1,1);

                    //startAnimation();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //传感器精度发生变化的时候调用

            }
        };
        //注册加速度传感器
        /**
         * 参数1:传感器的监听器
         * 参数2:需要注册的传感器的对象
         * 参数3:传感器的速率
         */
        sensorManager.registerListener(eventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);

    }


    public void startAnimation(){
        //img_up:先上后下,正值向下，参照自身
        TranslateAnimation a1= new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,-0.5f
        );
        a1.setDuration(500);
        TranslateAnimation a2= new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        a2.setDuration(500);
        a2.setStartOffset(1000);

        AnimationSet set =new AnimationSet(true);
        set.addAnimation(a1);
        set.addAnimation(a2);
        img_up.startAnimation(set);


        //img_down:先下后上,正值向下
        TranslateAnimation a11= new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        a11.setDuration(500);
        TranslateAnimation a22= new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,-0.5f
        );
        a22.setDuration(500);
        a22.setStartOffset(1000);

        AnimationSet set1 =new AnimationSet(true);
        set1.addAnimation(a11);
        set1.addAnimation(a22);
        img_down.startAnimation(set1);


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除加速度传感器
        sensorManager.unregisterListener(eventListener);
    }
}
