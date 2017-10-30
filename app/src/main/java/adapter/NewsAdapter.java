package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.absenubl.R;

import java.util.List;

import pojo.DataMhsAbsen;

/**
 * Created by Terminator on 14/09/2017.
 */

public class NewsAdapter extends BaseAdapter{


    private Activity activity;
    private LayoutInflater inflater;
    private List<DataMhsAbsen> dataMhs;

    public NewsAdapter(Activity activity, List<DataMhsAbsen> dataMhs) {
        this.activity = activity;
        this.dataMhs = dataMhs;
    }

    @Override
    public int getCount() {
        return dataMhs.size();
    }

    @Override
    public Object getItem(int i) {
        return dataMhs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_mhs_view, null);

        TextView no = (TextView) convertView.findViewById(R.id.no);
        TextView npm = (TextView) convertView.findViewById(R.id.npm);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);

        DataMhsAbsen mhsData = dataMhs.get(position);

        no.setText(mhsData.getNo());
        npm.setText(mhsData.getNpm());
        nama.setText(mhsData.getNama());

        return convertView;
    }
}

