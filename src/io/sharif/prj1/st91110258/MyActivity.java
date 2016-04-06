package io.sharif.prj1.st91110258;

import android.app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UpdateAppearance;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyActivity extends Activity implements View.OnTouchListener, View.OnClickListener{
    /**
     * Called when the activity is first created.
     */
    ImageView imageView;
    ImageView gopher;
    ImageButton Up ;
    ImageButton Down ;
    ImageButton Left ;
    ImageButton Right;
    ImageButton Option;
    float gopherX;
    float gopherY;
    Display display;
    Point size = new Point();
    int width;
    int height;
    View ContextMenu;
    RelativeLayout map;
    SharedPreferences prefs;
    public static final String MY_PREFS_NAME = "gopher_Coordination";
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences(
                MY_PREFS_NAME, Context.MODE_PRIVATE);
        setContentView(R.layout.main);
        map = (RelativeLayout) findViewById(R.id.map);
        Up = (ImageButton) findViewById(R.id.imageButton2);
        Down = (ImageButton) findViewById(R.id.imageButton);
        Left = (ImageButton) findViewById(R.id.imageButton3);
        Right = (ImageButton) findViewById(R.id.imageButton4);
        Option = (ImageButton) findViewById(R.id.option);
        registerForContextMenu(Option);
        Right.setOnTouchListener(this);
        Left.setOnTouchListener(this);
        Down.setOnTouchListener(this);
        Up.setOnTouchListener(this);
        Option.setOnClickListener(this);
        display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        width = size.x;
        height = map.getHeight();
        gopher = (ImageView) findViewById(R.id.gopher);
        gopherX = gopher.getX();
        gopherY = gopher.getY();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               loadGame();
            }
        }, 200);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
        ContextMenu = v;
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.newGame:
                newGame();
                return true;
            case R.id.saveGame:
                saveGame();
                return true;

            case R.id.loadGame:
                loadGame();
                return true;
            default:
            return super.onContextItemSelected(item);
        }
    }

    public void newGame(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        gopher.setX(width/2-gopher.getWidth()/2);
        gopher.setY(height/2+gopher.getHeight());

        SpannableString span = new SpannableString(getResources().getString(R.string.New_Game_Started).toString());
        span.setSpan(new RainbowSpan(this), 0, getResources().getString(R.string.New_Game_Started).length(), 0);
        Toast.makeText(this, span, Toast.LENGTH_LONG).show();

    }

    public void saveGame(){

        prefs.edit().putFloat("X",gopher.getX()).apply();
        prefs.edit().putFloat("Y",gopher.getY()).apply();

        Toast.makeText(this, getResources().getString(R.string.SavedGame),
                Toast.LENGTH_LONG).show();

    }

    public void loadGame(){
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            float centerX = (width/2-gopher.getWidth()/2);
            float centerY = (height/2+gopher.getHeight());
            float restoredFloat = prefs.getFloat("X",centerX);
        if (restoredFloat != 0) {
            gopherX = prefs.getFloat("X", centerX);
            gopherY = prefs.getFloat("Y", centerY);
            gopher.setX(gopherX);
            gopher.setY(gopherY);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        imageView = (ImageView) findViewById(R.id.animatedImage);
        final Animation animationRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imageView.startAnimation(animationRotate);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.about_us:
                //TODO show a dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.about_us_message)
                        .setTitle(R.string.about_us_title);
                builder.show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch(v.getId())
        {
            //left
            case R.id.imageButton3:
            {
                if(gopher.getX() >= 3 )
                    gopher.setX(gopher.getX() - 3);
                break;
            }

            //right
            case R.id.imageButton4:
            {
                if((gopher.getX() +(float) (gopher.getWidth())) <= (float) (width))
                gopher.setX(gopher.getX() + 3);
                break;
            }

            //up
            case R.id.imageButton2:
            {
                if(gopher.getY() >= 3)
                    gopher.setY(gopher.getY() - 3);
                break;
            }

            //down
            case R.id.imageButton:
            {
                    if( (gopher.getY() + (float) gopher.getHeight() ) <= map.getHeight())
                        gopher.setY(gopher.getY() + 3);
                break;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.option:
            {
                openContextMenu(v);
                break;
            }
        }
    }
    class RainbowSpan extends CharacterStyle implements UpdateAppearance {
        private final int[] colors;

        public RainbowSpan(Context context) {

            colors = context.getResources().getIntArray(R.array.colors);
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            paint.setStyle(Paint.Style.FILL);
            Shader shader = new LinearGradient(0, 0, 0, paint.getTextSize() * colors.length, colors, null,
                    Shader.TileMode.MIRROR);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            shader.setLocalMatrix(matrix);
            paint.setShader(shader);
        }
    }
}
