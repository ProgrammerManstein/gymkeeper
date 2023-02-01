package com.example.spotsmusic.food;

import android.content.Context;
import android.widget.SimpleAdapter;

import com.example.spotsmusic.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NutrientAdapter {

	public SimpleAdapter getAdapter(Context context, String nutrient) {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		String[] s = nutrient.split(";");
		for (int i = 0; i < s.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			String[] s1 = s[i].split(":");
			if (s1.length == 1)
				return null;
			String name = s1[0];
			String content = s1[1];
			String comment = "";// Integer.parseInt(s1[1]);
			map.put("nutrient_name", name);
			map.put("nutrient_content", content);
			map.put("nutrient_comment", comment);
			listItem.add(map);
		}
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(context, listItem, R.layout.food_nutrient_list_item,
				new String[] { "nutrient_name", "nutrient_content", "nutrient_comment" },
				new int[] { R.id.nutrient_name, R.id.nutrient_content, R.id.nutrient_comment });

		return mSimpleAdapter;
	}
}
