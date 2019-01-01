package test.helloworld22;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private MainViewAdapter adapter;
     ArrayList<ContactItem> list = new ArrayList<>();

     @Override
     public void onResume(){
         super.onResume();
         list = getContactList();
         adapter.notifyDataSetChanged();
     }
    @Override
    public void onStart(){
        super.onStart();
        list = getContactList();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        list = getContactList();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        Button plusPerson = (Button)rootView.findViewById(R.id.plusPerson);
        // list = WordItemData.createContactsList(50);
        recyclerView.setHasFixedSize(true);
        adapter = new MainViewAdapter(getActivity(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        Log.e("Frag", "MainFragment");

        //연락처 추가
        plusPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(),PlusPerson.class);
            //    Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
            }
        } );
        return rootView;
    }

    public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.Holder> {
        private Context context;
        private List<ContactItem> list = new ArrayList<>();

        public MainViewAdapter(Context context, List<ContactItem> list) {
            this.context = context;
            this.list = list;
        }

        // ViewHolder 생성
        // row layout을 화면에 뿌려주고 holder에 연결
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_voca_row, parent, false);
            list = getContactList();
            Holder holder = new Holder(view);
            return holder;
        }

        /*
         * Todo 만들어진 ViewHolder에 data 삽입 ListView의 getView와 동일
         *
         * */
        @Override
        public void onBindViewHolder(Holder holder, int position) {
            // 각 위치에 문자열 세팅
            final int itemposition = position;
            holder.nameText.setText(list.get(itemposition).user_Name);
            holder.nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = list.get(itemposition).id;
                    String name = list.get(itemposition).user_Name;
                    String phone = list.get(itemposition).user_phNumber;
                    String email = list.get(itemposition).user_Email;
                    Intent intent = new Intent(getActivity(),PersonInfo2.class);
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    intent.putExtra("phone",phone);
                    intent.putExtra("email",email);
                    startActivity(intent);

                }
            });
            Log.e("StudyApp", "onBindViewHolder" + itemposition);
        }

        // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
        @Override
        public int getItemCount() {
            return list.size(); // RecyclerView의 size return
        }

        // ViewHolder는 하나의 View를 보존하는 역할을 한다
        public class Holder extends RecyclerView.ViewHolder{
            public TextView noText;
            public TextView nameText;
            public TextView phoneText;
            public Holder(View view){
                super(view);
                nameText = (TextView) view.findViewById(R.id.nameText);

            }
        }


    }

    public ArrayList<ContactItem> getContactList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        ArrayList<ContactItem> contactItems;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ", " + ContactsContract.CommonDataKinds.Email.DATA+" COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(uri,projection,null,selectionArgs,sortOrder);
        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        if(cursor.moveToFirst()){
            do{
             //   long photo_id = cursor.getLong(2);
                long person_id = cursor.getLong(4);
                ContactItem contactItem = new ContactItem();
                contactItem.setUser_phNumber(cursor.getString(0));
                contactItem.setUser_Name(cursor.getString(1));
                contactItem.setUser_Email(cursor.getString(2));
         //       contactItem.setPhoto_id(photo_id);
                contactItem.setPerson_id(person_id);
                hashlist.add(contactItem);
            }while (cursor.moveToNext());
        }
        contactItems = new ArrayList<>(hashlist);
        for(int i = 0;i<contactItems.size();i++){
            contactItems.get(i).setId(i);
        }
        return contactItems;
    }

}
