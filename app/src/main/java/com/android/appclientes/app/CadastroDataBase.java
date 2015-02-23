package com.android.appclientes.app;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class CadastroDataBase extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "DBClientes.db"; // Nome do banco
 	public static final int DATABASE_VERSION = 2; 
 	final String TABLE_CLIENTE = "cliente";
 	final String TABLE_PRODUTO = "produto";
	

	public CadastroDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		System.out.println("Construtor do Data Base");
		// TODO Auto-generated constructor stub
	}


	@Override
	
	public void onCreate(SQLiteDatabase db) {
		// Cria as tabelas do banco de dados
		
	try{
		// TABELA CLIENTE
		String sql = "CREATE TABLE "+ TABLE_CLIENTE +" (" +
		"_ID integer PRIMARY KEY AUTOINCREMENT, "+
		"cpfcnpj Text,"+
		"nome TEXT, "+
		"fone TEXT, "+
		"celular TEXT, "+
		"email TEXT, "+
		"contato TEXT, "+
		"endereco TEXT, "+
		"cep TEXT, "+
		"cidade TEXT,"+
		"latitude TEXT,"+
		"longitude TEXT);";
		db.execSQL(sql);
		System.out.println("SQL Cliente: "+ sql);
		
		// TABELA PRODUTO
		
		sql = "CREATE TABLE "+ TABLE_PRODUTO +" (" +
		"_ID integer PRIMARY KEY AUTOINCREMENT, "+
		"nome TEXT);";
		
		System.out.println("SQL Produto: "+ sql);
		db.execSQL(sql);
	}catch(Exception e){
		System.out.println("Erro ao criar tabelas. "+e.getMessage());
		
	}
		
		
		
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	/*****************************************************
	 * 			   TABELA PRODUTO                       *
	 * 		                                            *
	 * ***************************************************
	 * */
	
	public void addProduto(String nome){
		ContentValues map = new ContentValues();
		map.put("nome", nome);
		try{
			getWritableDatabase().insert(TABLE_PRODUTO, null, map);
			
		}catch(SQLException e){
			Log.e("Erro ao gravar novo produto.", e.toString());
		}
	}

	public void updateProduto(int id , String nome){
		
		ContentValues map = new ContentValues();

		map.put("_ID", id);
		map.put("nome", nome);

		String[] whereArgs = new String[]{Long.toString(id)};
		
		
		try{
			getWritableDatabase().update(TABLE_PRODUTO, map, "_id=?", whereArgs);
			
		}catch(SQLException e){
			Log.e("Erro ao gravar produto: ", e.toString());
		}
	}
	
	public void deleteProdutos(int id){
		
		String[] whereArgs = new String[]{Long.toString(id)};
		
		
		try{
			getWritableDatabase().delete(TABLE_PRODUTO, "_id=?", whereArgs);
			
		}catch(SQLException e){
			Log.e("Erro ao excluir produto:", e.toString());
		}
		
	}
	
	/*Retorna lista dos produtos*/
	public Cursor getProdutos(){

		Cursor cursor = null;
		
		String sql = "select * from "+TABLE_PRODUTO;
		try{

			cursor = getReadableDatabase().rawQuery(sql, null);
			cursor.moveToFirst();
			
			return cursor;
		}catch(SQLException e){
			Log.e("Erro ao gravar obter clientes", e.toString());
			return cursor;
		}	
		
	}
	
	/*****************************************************
	 * 			   TABELA CLIENTE                       *
	 * 		                                            *
	 * ***************************************************
	 * */
	
	
	public void addCliente(String cpfcnp, String nome, String fone, String celular, 
			String email, String contato, String endereco, String cep, String cidade){
		ContentValues map = new ContentValues();
		map.put("cpfcnpj", cpfcnp);
		map.put("nome", nome);
		map.put("fone", fone);
		map.put("celular", celular);
		map.put("email", email);
		map.put("contato", contato);
		map.put("endereco", endereco);
		map.put("cep", cep);
		map.put("cidade", cidade);

		
		try{
			getWritableDatabase().insert(TABLE_CLIENTE, null, map);
			
		}catch(SQLException e){
			Log.e("Erro ao gravar novo cliente", e.toString());
		}
	}
	
	public void updateCliente(int id ,String cpfcnp, String nome, String fone, String celular, 
			String email, String contato, String endereco, String cep, String cidade){
		
		ContentValues map = new ContentValues();

		map.put("_ID", id);
		map.put("cpfcnpj", cpfcnp);
		map.put("nome", nome);
		map.put("fone", fone);
		map.put("celular", celular);
		map.put("email", email);
		map.put("contato", contato);
		map.put("endereco", endereco);
		map.put("cep", cep);
		map.put("cidade", cidade);
		
		

		String[] whereArgs = new String[]{Long.toString(id)};
		
		
		try{
			getWritableDatabase().update(TABLE_CLIENTE, map, "_id=?", whereArgs);
			
		}catch(SQLException e){
			Log.e("Erro ao gravar posi��o:", e.toString());
		}
		
	}
	
	// Atualiza o cliente gravando sua posi��o com base no GPS
	public void gravaLocalizacaoCliente(int id, String latitude, String longitude){
		ContentValues map = new ContentValues();

		map.put("_ID", id);
		map.put("latitude", latitude);
		map.put("longitude", longitude);

		String[] whereArgs = new String[]{Long.toString(id)};
		
		
		try{
			getWritableDatabase().update(TABLE_CLIENTE, map, "_id=?", whereArgs);
			
		}catch(SQLException e){
			Log.e("Erro ao gravar cliente:", e.toString());
		}
		
		
		
	}
	
	public void deleteCliente(int id){
		
		String[] whereArgs = new String[]{Long.toString(id)};
		
		
		try{
			getWritableDatabase().delete(TABLE_CLIENTE, "_id=?", whereArgs);
			
		}catch(SQLException e){
			Log.e("Erro ao excluir cliente:", e.toString());
		}
		
	}
		
	
	public Cursor getClientes(){

		Cursor cursor = null;
		
		String sql = "select * from "+TABLE_CLIENTE;
		try{

			cursor = getReadableDatabase().rawQuery(sql, null);
			cursor.moveToFirst();
			
			return cursor;
		}catch(SQLException e){
			Log.e("Erro ao gravar obter clientes", e.toString());
			return cursor;
		}	
		
	}


	public void getLocalClienteById(int id){
    // Recebe um Id e lista a latitude e longitude do cliente
		Cliente cli = new Cliente();
		Cursor c;
		String sql = "select * from "+TABLE_CLIENTE+ " where _id="+id ;
		try{

			c = getReadableDatabase().rawQuery(sql, null);
			c.moveToFirst();
			
			System.out.println("Latitude "+c.getString(10));
		    System.out.println("Latitude "+c.getString(11));
			
		    
		}catch(SQLException e){
			Log.e("Erro ao gravar obter clientes", e.toString());
		}	
		
	}
	
	
}
