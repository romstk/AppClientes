package com.android.appclientes.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroCliente extends Activity{
	Intent i ; //Variável que manipula os intentos chamados na tela principal
	
	private final String TAG = this.getClass().getSimpleName();
	public  CadastroDataBase dataBase = new CadastroDataBase(CadastroCliente.this);
	public Cliente cliente = new Cliente();
	// Variáveis que recebem os edit text da tela de cadastro
	EditText edcnpjcpf, ednome, edfone, edcelular, edemail, edcontato, edendereco, edcep, edcidade; 

	@Override
	protected void onStart(){
		super.onStart();
		Log.i(TAG, "onStart");
		
	} 	
	
	void salvaCadastro(){
		
		cliente.setCPFCNPJ(edcnpjcpf.getText().toString());
		cliente.setNome(ednome.getText().toString());
		cliente.setFone(edfone.getText().toString());
		cliente.setCelular(edcelular.getText().toString());
		cliente.setEmail(edemail.getText().toString());
		cliente.setContato(edcontato.getText().toString());
		cliente.setEndereco(edendereco.getText().toString());
		cliente.setCEP(edcep.getText().toString());
		cliente.setCidade(edcidade.getText().toString());
		
		
		if (cliente.getNome().equals("")||(cliente.getCPFCNPJ().equals(""))){
			Utilitarios.showMessage("Informe Nome e CPF/CNPJ para prosseguir.", "Cliente", CadastroCliente.this);
		}else{
				try{
					
		
					dataBase.addCliente(cliente.getCPFCNPJ().toString(),cliente.getNome().toString(),
							cliente.getFone().toString(), cliente.getCelular().toString(),
							cliente.getEmail().toString(), cliente.getContato().toString(),
							cliente.getEndereco().toString(), cliente.getCEP().toString(),cliente.getCidade().toString());
							
					
					Toast.makeText(CadastroCliente.this, "Cadastro efetuado.", Toast.LENGTH_SHORT).show();
					i = new Intent(CadastroCliente.this, ListaClientes.class);
		            startActivity(i);
					
				}catch(Exception e){
					Utilitarios.showMessage("Erro ao cadastrar"+e.getMessage(), "Erro", CadastroCliente.this);
					
				}
		}		
		
	}
	
	
	// Carrega a tela de cadastro de clientes
	void carregaTelaCadastro(){
			
			setContentView(R.layout.cadastro);
			
			edcnpjcpf = (EditText) findViewById(R.id.cadastro_edCNPJCPF);
			ednome = (EditText) findViewById(R.id.cadastro_edNome);
			edfone = (EditText) findViewById(R.id.cadastro_edFone);
			edcelular = (EditText) findViewById(R.id.cadastro_edCelular);
			edemail = (EditText) findViewById(R.id.cadastro_edEmail);
			edcontato = (EditText) findViewById(R.id.cadastro_edContato);
			edendereco = (EditText) findViewById(R.id.cadastro_edEndereco);
			edcep = (EditText) findViewById(R.id.cadastro_edCep);
			edcidade = (EditText) findViewById(R.id.cadastro_edCidade);
			
			
		}
		
	
	//M�todo que carrega o sistema, primeira tela.	
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				carregaTelaCadastro();
			}
			
			
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				
				getMenuInflater().inflate(R.menu.menu_cadastro_clientes, menu);
				
				return super.onCreateOptionsMenu(menu);
			}
			
			public boolean onOptionsItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.menuitem_cadastro_clientes_salvar:
                        salvaCadastro();
                        return true;

                    case R.id.menuitem_cadastro_clientes_voltar:
                        // Executa algo
                        i = new Intent(CadastroCliente.this, AppClientes.class);
                        startActivity(i);
                        return true;

                    default:

                        return super.onOptionsItemSelected(item);

                }

            }
}
