package com.android.appclientes.app;

import android.app.AlertDialog;
import android.content.Context;


public class Utilitarios {
	
	
	public static void showMessage(String Caption, String Title, Context context){
		// Exibe uma mensagem e informa��o. 
		AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
		dialogo.setTitle(Title);
		dialogo.setMessage(Caption);
		dialogo.setNeutralButton("OK", null);
		dialogo.setIcon(R.drawable.ic_launcher);
		dialogo.show();
	}

}
