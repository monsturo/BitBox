package com.redbear.chat;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Frederik on 21-05-2015.
 */
public class HighscoreAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public HighscoreAdapter (Context context, LayoutInflater inflater){
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }


    @Override
    public int getCount() { return mJsonArray.length(); }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Check if view already exists
        if(convertView == null) {

            // Inflate custom row layout from xml
            convertView = mInflater.inflate(R.layout.score_rows, null);

            // Create new "holder" with subviews
            holder = new ViewHolder();

            holder.highscoreView = (TextView) convertView.findViewById(R.id.highscore);
            holder.usernameView = (TextView) convertView.findViewById(R.id.username);
            holder.idView = (TextView) convertView.findViewById(R.id.id);

            convertView.setTag(holder);
        } else {

            // Skip all the expensive inflation
            // and get existing holder
            holder = (ViewHolder) convertView.getTag();
        }

        // Get current element in JSON form
        JSONObject jsonObject = (JSONObject) getItem(position);

        String user = "";
        String score = "";
        String id = "";

        if(jsonObject.has("brugernavn")) { user = jsonObject.optString("brugernavn"); }
        if(jsonObject.has("highscore")) { score = jsonObject.optString("highscore"); }
        if(jsonObject.has("id")) { id = jsonObject.optString("id"); }

        holder.highscoreView.setText(score);
        holder.usernameView.setText(user);
        holder.idView.setText(id);

        Log.d("BITBOX: ", "Adapter created");
        return convertView;
    }

    private static class ViewHolder {
        public TextView idView;
        public TextView highscoreView;
        public TextView usernameView;
    }

    public void updateData(JSONArray jsonArray) {
        // Update adapters dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
}