package org.daniruiz.ufobusters;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;


public class OverlayMap extends Overlay {
	
	private Context context = null;
	
	public OverlayMap(Context context) {
        this.context = context;
    }
	
    @Override
    public boolean onTap(final GeoPoint point, MapView mapView)
    {
        final Context contexto = mapView.getContext();
        Geocoder gc = new Geocoder(contexto);
        ArrayList<Address> addresses = new ArrayList<Address>();
        try {
			addresses = (ArrayList<Address>) gc.getFromLocation(point.getLatitudeE6()/1E6, (double) point.getLongitudeE6()/1E6, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
        String address = new String();
        if(addresses.size()>0) {
        	for(int i=0; i<addresses.get(0).getMaxAddressLineIndex(); ++i) {
        		if(i==0) address = addresses.get(0).getAddressLine(i);
        		else address = address + " " + addresses.get(0).getAddressLine(i); 
        	}
        	final String addr = address;
            
            new AlertDialog.Builder(contexto).setTitle("¿Guardar avistamiento en...\n "+address+"?").setItems(R.array.opcionesSINO, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialogInterface, int i) {
    				if(i==0) {
    					Intent intent = new Intent((UFObusters) contexto, newSight.class);
    					intent.putExtra("dir", addr);
    					intent.putExtra("X", point.getLatitudeE6());
    					intent.putExtra("Y", point.getLongitudeE6());
    					((UFObusters) contexto).startActivity(intent);
    				}
    			}
    		}).show();
        } else {
        	Toast t = Toast.makeText(contexto, "Lugar no válido", Toast.LENGTH_SHORT);
        	t.show();
        }
        return true;
    }
    
    private String DB_NAME = "ufobuster.db";
	private String TABLE = "sights";
	private String ADDR_DB = "address";
	private String COORD_X_DB = "coord_X";
	private String COORD_Y_DB = "coord_Y";
	private String INF_DB = "description";
	private String DAY_DB = "day";
	private String MONTH_DB = "month";
	private String YEAR_DB = "year";
    
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    	super.draw(canvas, mapView, shadow);
    	Projection projection = mapView.getProjection();
    	
    	mapView.postInvalidate();

    	SQLiteDatabase myDB = null;
        
        myDB = ((UFObusters) context).openOrCreateDatabase(DB_NAME, 1, null);
        ArrayList<Pair<Integer, Integer>> sights = new ArrayList<Pair<Integer, Integer>>();
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" + ADDR_DB + " TEXT NOT NULL PRIMARY KEY, " + COORD_X_DB + " INTEGER NOT NULL, " + COORD_Y_DB + " INTEGER NOT NULL, " + INF_DB + " TEXT, " + DAY_DB + " INTEGER, " + MONTH_DB + " INTEGER, " + YEAR_DB + " INTEGER);");
        
        String[] FROM = { COORD_X_DB, COORD_Y_DB };
        
        Cursor c = myDB.query(TABLE, FROM, null, null, null, null, null);
        
        //Obtenemos solo la dirección y la fecha
        ((UFObusters) context).startManagingCursor(c);
        while(c.moveToNext()) {
        	Pair<Integer, Integer> aux = new Pair<Integer, Integer>(c.getInt(0), c.getInt(1));
        	sights.add(aux);
        }
        
        c.close();
        
        if(myDB!=null) {
        	myDB.close();
        }
    	
    	if(shadow==false) {
    		for(int i=0; i<sights.size(); ++i) {
    			GeoPoint geoPoint = new GeoPoint(sights.get(i).first, sights.get(i).second);
    			
    			Point centro = new Point();
    			projection.toPixels(geoPoint, centro);
    			
    			Paint p = new Paint();
    			p.setColor(Color.BLUE);
    			
    			Bitmap bm = BitmapFactory.decodeResource(
    			        mapView.getResources(),
    			        R.drawable.iconoalien);
    			 
    			canvas.drawBitmap(bm, centro.x - bm.getWidth(),
    			        centro.y - bm.getHeight(), p);
    		}
    	}
    }

}