package org.daniruiz.ufobusters;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class viewSight extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewsight);
		
		String addr = this.getIntent().getExtras().getString("dir");
		int x = this.getIntent().getExtras().getInt("X");
		int y = this.getIntent().getExtras().getInt("Y");
		String inf = this.getIntent().getExtras().getString("inf");
		int day = this.getIntent().getExtras().getInt("day");
		int month = this.getIntent().getExtras().getInt("month");
		int year = this.getIntent().getExtras().getInt("year");
		
		TextView tvA = (TextView) findViewById(R.id.placeText);
		tvA.setText(addr);
		
		TextView tvI = (TextView) findViewById(R.id.infText);
		if(inf.isEmpty()) {
			tvI.setText("---No se introdujo ninguna descripción---");
		} else {
			tvI.setText(inf);
		}
		
		TextView tvC = (TextView) findViewById(R.id.coordText);
		tvC.setText(String.valueOf( (double) (((double) x)/1000000.) )+", "+String.valueOf( (double) (((double) y)/1000000.) ));
		
		TextView tvD = (TextView) findViewById(R.id.dateText);
		tvD.setText(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
	}
}
