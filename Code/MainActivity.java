package com.example.mygps;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
//import android.widget.TextView;


public class MainActivity extends Activity {

	
	double old_clat,old_clon,clat,clon;
	int prev_time,time_diff,curr_time;
	private String message,IMEI_no;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Loadcord();
	}

	private void Loadcord() {
		// TODO Auto-generated method stub
		//final TextView tv_message = (TextView) findViewById(R.id.message);
		prev_time = (int) new Date().getTime();
		final LocationManager locationmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI_no = tm.getDeviceId();
		old_clat = locationmanager.getLastKnownLocation("gps").getLatitude();
		old_clon = locationmanager.getLastKnownLocation("gps").getLongitude();
		time_diff = 0;
		
		LocationListener listener = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
				clat =  locationmanager.getLastKnownLocation("gps").getLatitude();
				clon =  locationmanager.getLastKnownLocation("gps").getLongitude();
				curr_time = (int) new Date().getTime();
				
				if (old_clat == clat && old_clon == clon)
				 {

					 old_clat = clat;
					 old_clon = clon;
				 }
				 else
				 {
					 
					 	 time_diff = curr_time - prev_time;
					 	 time_diff = time_diff/(60*1000);
					 	try {
					 		 message = "<" + IMEI_no + "," + Double.toString(old_clat) + "," + Double.toString(old_clon) + "," + Integer.toString(time_diff) + ">";
					 	     Socket client = new Socket("10.0.2.2", 4444);  //connect to server
					 	     PrintWriter printwriter = new PrintWriter(client.getOutputStream(),true);
					 	     printwriter.write(message);  //write the message to output stream
					 	 
					 	     printwriter.flush();
					 	     printwriter.close();
					 	     client.close();   //closing the connection
					 	 
					 	    } catch (UnknownHostException e) {
					 	     e.printStackTrace();
					 	   } catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
					 	 
						 old_clat = clat;
						 old_clon = clon;
						 
						 prev_time = curr_time;
						 //tv_message.setText(message);
				 }
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			
			
			
		};
		 locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,(2*60*1000),0,listener);
		
		
		
	}

	
}
