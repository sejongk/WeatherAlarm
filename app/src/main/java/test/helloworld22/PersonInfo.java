package test.helloworld22;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonInfo extends AppCompatActivity {
    Intent intent;
    long id;
    String name;
    String email;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        intent = getIntent();
        id = intent.getIntExtra("id",0);
        Log.e("id",Long.toString(id));
        name = intent.getStringExtra("name");
        //email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        TextView nameT1 = (TextView) this.findViewById(R.id.getName1);
       // TextView nameT2 = (TextView) this.findViewById(R.id.getName2);
        //TextView emailT = (TextView) this.findViewById(R.id.getEmail);
        TextView phoneT = (TextView) this.findViewById(R.id.getPhone);

        nameT1.setText(name);
       // nameT2.setText(name);
       //emailT.setText(email);
        phoneT.setText(phone);


        Button sendCall = (Button) this.findViewById(R.id.sendCall);
        Button sendMsg = (Button) this.findViewById(R.id.sendMessage);

        sendCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                startActivity(intent);

            }
        } );
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_SENDTO );
                intent.putExtra( "sms_body", "" );
                intent.setData( Uri.parse( "smsto:"+phone  ) );
                startActivity( intent );
                }
        } );

    }

//Intent intent = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI);
//Bundle bundle = new Bundle();
//bundle.putString(Intents.Insert.PHONE, "01077777777");
//intent.putExtras(bundle);
//startActivity(intent);
//
//출처: http://darksilber.tistory.com/entry/연락처-추가하는-화면-띄우기 [안드로이드 / 자바]
}
