package org.daniruiz.ufobusters;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class newSight extends Activity implements OnClickListener {
	
	private String DB_NAME = "ufobuster.db";
	private String TABLE = "sights";
	private String ADDR_DB = "address";
	private String COORD_X_DB = "coord_X";
	private String COORD_Y_DB = "coord_Y";
	private String INF_DB = "description";
	private String DAY_DB = "day";
	private String MONTH_DB = "month";
	private String YEAR_DB = "year";
	
	private String addr;
	private int x;
	private int y;
	private String inf;
	private int day;
	private int month;
	private int year;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsight);
		
		addr = this.getIntent().getExtras().getString("dir");
		x = this.getIntent().getExtras().getInt("X");
		y = this.getIntent().getExtras().getInt("Y");
		
		Calendar c = new GregorianCalendar();
		day = c.get(Calendar.DATE); 
		month = c.get(Calendar.MONTH)+1;
		year = c.get(Calendar.YEAR);
		
		TextView tvA = (TextView) findViewById(R.id.placeTextNew);
		tvA.setText(addr);
		
		TextView tvC = (TextView) findViewById(R.id.coordTextNew);
		tvC.setText(String.valueOf( (double) (((double) x)/1000000.) )+", "+String.valueOf( (double) (((double) y)/1000000.) ));
		
		TextView tvD = (TextView) findViewById(R.id.dateTextNew);
		tvD.setText(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
		
		inf = new String();
		
		Button b = (Button) findViewById(R.id.saveButton);
		b.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		if(v.getId()==R.id.saveButton) {
			EditText et = (EditText) findViewById(R.id.infEditText);
			inf = et.getText().toString();
			
			/*
			 * Hay que mirar si la dirección contiene el carácter ''', ya que
			 * da problemas con DB, para todas las instancias, se sustituyen por
			 * el carácter '´'
			 */
			char aux[] = new char[addr.length()];
			String prob = "'";
			Log.d("Caracter especial", prob);
			for(int i = 0; i<addr.length(); ++i) {
				if(addr.charAt(i)==prob.charAt(0)) {
					aux[i] = '´';
				} else {
					aux[i] = addr.charAt(i);
				}
			}
			addr = new String(String.copyValueOf(aux));
			/*
			 * Fin de la comprobación
			 */
			
			
			ContentValues cv = new ContentValues(); 
			
			cv.put(ADDR_DB, addr);
			cv.put(COORD_X_DB, x);
			cv.put(COORD_Y_DB, y);
			cv.put(INF_DB, inf);
			cv.put(DAY_DB, day);
			cv.put(MONTH_DB, month);
			cv.put(YEAR_DB, year);
			
			SQLiteDatabase myDB = null;
	        myDB = openOrCreateDatabase(DB_NAME, 1, null);
	        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" + ADDR_DB + " TEXT NOT NULL PRIMARY KEY, " + COORD_X_DB + " INTEGER NOT NULL, " + COORD_Y_DB + " INTEGER NOT NULL, " + INF_DB + " TEXT, " + DAY_DB + " INTEGER, " + MONTH_DB + " INTEGER, " + YEAR_DB + " INTEGER);");
	        
	        myDB.insertOrThrow(TABLE, null, cv);
	        if(myDB!=null) myDB.close();
	        
	        finish();
		}
		
	}

}
