package com.unosquare.myfirtsapplication;

/**
 * Created by admin on 11/10/2014.
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MyActivity extends Activity implements BlankFragment.OnFragmentInteractionListener{

    TextView textTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        intiUI();
    }

    private void intiUI() {
        textTop = (TextView)findViewById(R.id.text_top);
        Button btnAdd = (Button) findViewById(R.id.button_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager manager = getFragmentManager();
//                manager.beginTransaction().replace(R.id.container, BlankFragment.newInstance("Ricardo")).commit();

                Intent intent = new Intent(MyActivity.this, ListGridActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onFragmentInteraction(String texto) {
        textTop.setText(texto);
    }
}
