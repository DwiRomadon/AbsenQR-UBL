package pojo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newversion.PilihMatakuliah;
import com.project.absenubl.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by KUNCORO on 09/08/2017.
 */

public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataMhsAbsen> item;

    public Adapter(Activity activity, List<DataMhsAbsen> item) {
        this.activity = activity;
        this.item = item;
    }


    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_rows, null);


        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView npm = (TextView) convertView.findViewById(R.id.npm);

        nama.setText(item.get(position).getNama());
        npm.setText(item.get(position).getNpm());

        return convertView;
    }
}
