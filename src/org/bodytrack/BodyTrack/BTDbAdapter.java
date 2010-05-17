package org.bodytrack.BodyTrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class BTDbAdapter {
	private static String DB_NAME = "BodytrackDB";
	private static int DB_VERSION = 1;
	
	
	//Location table creation SQL
    private static final String LOCATION_TABLE_CREATE =
        "create table location (_id integer primary key autoincrement, "
                + "latitude real not null, longitude real not null, time integer not null,"
                + "accuracy real, altitude real, bearing real, provider text,"
                + "speed real);";
	//fields of location table
	private static final String LOCATION_TABLE = "location";
	private static final String LOC_KEY_LATITUDE = "latitude";
	private static final String LOC_KEY_LONGITUDE = "longitude";
	private static final String LOC_KEY_TIME = "time";
	private static final String LOC_KEY_ACCURACY = "accuracy";
	private static final String LOC_KEY_ALTITUDE = "altitude";
	private static final String LOC_KEY_BEARING = "bearing";
	private static final String LOC_KEY_PROVIDER = "provider";
	private static final String LOC_KEY_SPEED = "speed";


    //Barcode table creation SQL
    private static final String BARCODE_TABLE_CREATE =
        "create table barcode (_id integer primary key autoincrement, "
                + "time integer not null, barcode integer not null);";
    //fields of barcode table
	private static final String BARCODE_TABLE = "barcode";
	private static final String	BC_KEY_TIME = "time";
	private static final String BC_KEY_BARCODE = "barcode";
	
    //Photo table creation SQL
    private static final String PIX_TABLE_CREATE =
        "create table pix (_id integer primary key autoincrement, "
                + "time integer not null, pic blob not null);";
    //fields of photo table
	private static final String PIX_TABLE = "pix";
	private static final String	PIX_KEY_TIME = "time";
	private static final String PIX_KEY_PIC = "pic";

    
    private DatabaseHelper mDbHelper;
    private Context mCtx;
    private SQLiteDatabase mDb;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, BTDbAdapter.DB_NAME, null, BTDbAdapter.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//create the 3 database tables
			db.execSQL(LOCATION_TABLE_CREATE);
			db.execSQL(BARCODE_TABLE_CREATE);
			db.execSQL(PIX_TABLE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}	
	}
	
	public BTDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public BTDbAdapter open() throws SQLException{
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public long writeLocation(Location loc)
	{
		ContentValues locToPut = new ContentValues();
		locToPut.put(LOC_KEY_LATITUDE, loc.getLatitude());
		locToPut.put(LOC_KEY_LONGITUDE, loc.getLongitude());
		locToPut.put(LOC_KEY_TIME, loc.getTime());
		locToPut.put(LOC_KEY_ACCURACY, loc.getAccuracy());
		locToPut.put(LOC_KEY_ALTITUDE, loc.getAltitude());
		locToPut.put(LOC_KEY_BEARING, loc.getBearing());
		locToPut.put(LOC_KEY_PROVIDER, loc.getProvider());
		locToPut.put(LOC_KEY_SPEED, loc.getSpeed());
		
		return mDb.insert(LOCATION_TABLE, null, locToPut);
	}
	
    public Cursor fetchAllLocations() {
        return mDb.query(LOCATION_TABLE, new String[] {LOC_KEY_LATITUDE, 
        		LOC_KEY_LONGITUDE, LOC_KEY_TIME, LOC_KEY_ACCURACY, LOC_KEY_ALTITUDE,
        		LOC_KEY_BEARING, LOC_KEY_PROVIDER, LOC_KEY_SPEED},
                null, null, null, null, LOC_KEY_TIME);
    }
    
	public long writeBarcode(long barcode)
	{
		ContentValues codeToPut = new ContentValues();
		codeToPut.put(BC_KEY_BARCODE, barcode);
		codeToPut.put(BC_KEY_TIME, System.currentTimeMillis());

		return mDb.insert(BARCODE_TABLE, null, codeToPut);
	}
	
	public long writePicture(byte [] picture) {
		ContentValues picToPut = new ContentValues();
		
		
		picToPut.put(PIX_KEY_PIC, picture);
		picToPut.put(PIX_KEY_TIME, System.currentTimeMillis());

		return mDb.insert(PIX_TABLE, null, picToPut);
	}
	
	
}