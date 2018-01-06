package pojo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.absenubl.R;

import java.util.List;

/**
 * Created by Terminator on 04/12/2017.
 */

public class AdapterMatakuliah extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataMatakuliah> item;

    public AdapterMatakuliah(Activity activity, List<DataMatakuliah> item) {
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
            convertView = inflater.inflate(R.layout.list_matakuliah, null);


        TextView matakuliah = (TextView) convertView.findViewById(R.id.matakuliah);
        TextView nidn = (TextView) convertView.findViewById(R.id.nidn);
        TextView nomk = (TextView) convertView.findViewById(R.id.nomk);

        matakuliah.setText(item.get(position).getMatakuliah());
        nidn.setText(item.get(position).getNidn());
        nomk.setText(item.get(position).getNomk());

        return convertView;
    }
}
