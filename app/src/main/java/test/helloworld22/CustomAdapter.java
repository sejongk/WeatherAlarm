package test.helloworld22;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{
    private ArrayList<Person> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView no;
        protected TextView name;
        protected TextView phone;

        public CustomViewHolder(View view) {
            super(view);
            this.no = (TextView) view.findViewById(R.id.showNo);
            this.name = (TextView) view.findViewById(R.id.showName);
            this.phone = (TextView) view.findViewById(R.id.showPhone);
        }
    }
        public CustomAdapter(ArrayList<Person> list) {
            this.mList = list;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position){
        viewholder.no.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25) ;
        viewholder.phone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.no.setGravity(Gravity.CENTER);
        viewholder.name.setGravity(Gravity.CENTER);
        viewholder.phone.setGravity(Gravity.CENTER);

        viewholder.no.setText(mList.get(position).getNo());
        viewholder.name.setText(mList.get(position).getName());
        viewholder.phone.setText(mList.get(position).getPhone());

        }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
