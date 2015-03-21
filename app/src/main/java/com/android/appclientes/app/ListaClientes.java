package com.android.appclientes.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

@SuppressLint("NewApi")
public class ListaClientes extends Activity{
	public boolean Mapa=false ; // Variável para controlar qual menu aparecerá dependendo da ação executada na atividade ver onPrepareOptionsMenu
	public boolean Lista; //Variável para controlar se aparecem itens de menu ou nãoo. ver onPrepareOptionsMenu
	Intent i ; //Variável que manipula os intentos chamados na tela principal
	public CadastroDataBase dataBase = new CadastroDataBase(ListaClientes.this);
	public Cliente cliente = new Cliente();
	// Variáveis que recebem os edit text da tela de cadastro
	EditText edcnpjcpf, ednome, edfone, edcelular, edemail, edcontato, edendereco, edcep, edcidade;

// Utiliza como global para poder utilizar no mapa quando atualiza a localização
    PolylineOptions line = new PolylineOptions();
    ArrayList<LatLng> listaLatLng = new ArrayList<LatLng>();
    int indexLoc;
    boolean partida=true; //Inicia como true e quando começar a se deslocar passa para false para determinar o marcador inicial.



	void carregaListaPessoas(){
		Lista=true;

		invalidateOptionsMenu();
		setContentView(R.layout.listacadastros);
		final ListView listaClientes;
		Cursor c = dataBase.getClientes();
		final Cliente[] cliente = new Cliente[c.getCount()];
		
		for (int i=0; i<c.getCount();i++){
		   
			Cliente cli = new Cliente();
		    // Seta os calores na classe cliente
		    cli.setId(c.getInt(0));
		    cli.setCPFCNPJ(c.getString(1));
		    cli.setNome(c.getString(2));
		    cli.setFone(c.getString(3));
		    cli.setCelular(c.getString(4));
		    cli.setEmail(c.getString(5));
		    cli.setContato(c.getString(6));
		    cli.setEndereco(c.getString(7));
		    cli.setCEP(c.getString(8));
		    cli.setCidade(c.getString(9));
		    cli.setLatitude(c.getDouble(10));
		    cli.setLongitude(c.getDouble(11));
		    
		    
		    cliente[i] = cli; 
		    c.moveToNext();

		}
		
		

		ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(this, android.R.layout.simple_list_item_1, cliente);
		listaClientes = (ListView)findViewById(R.id.lsClientes);
		listaClientes.setAdapter(adapter);
		
		
		listaClientes.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			
			/*
			 *  Arg2 é a posição clicada
			 * 
			 * */	
			Lista=false;	
			invalidateOptionsMenu();
			Cliente clienteEditar = cliente[(int) listaClientes.getAdapter().getItemId(arg2)];

			//Seta na var Cliente (Objeto) do me, o objeto da lista conforme ID lista x Array
			carregaTelaCadastroParaEdicao(clienteEditar);
				
			}
		});
		
	} 
	
	// Carrega a tela de cadastro de clientes para ediçãoo, neste caso com os dados do cliente selecionado na lista e com opçoes de editar
	// e excluir o cliente 

	
	void carregaTelaCadastroParaEdicao(Cliente cli){
		
		// Recebe uma instancia do cliente clicado na lista de cliente e seta na tela de cadastro permitindo a edição dos dados
		cliente = cli;
		
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
		
		
		edcnpjcpf.setText(cli.getCPFCNPJ().toString());
		ednome.setText(cli.getNome().toString());
		edfone.setText(cli.getFone().toString());
		edcelular.setText(cli.getCelular().toString());
		edemail.setText(cli.getEmail().toString()); 
		edcontato.setText(cli.getContato().toString());
		edendereco.setText(cli.getEndereco().toString());
		edcep.setText(cli.getCEP().toString());
		edcidade.setText(cli.getCidade().toString());

        //Verifica se o cliente possui localização salva então mostra mapa abaixo dos campos do cadastro

        if((cliente.getLatitude()!=0)||(cliente.getLongitude()!=0)){
            visualizaLocalizacao();

        }

		

		
	}
	
	// Método que carrega a primeira tela.
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			carregaListaPessoas();
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
				Utilitarios.showMessage("Informe Nome e CPF/CNPJ para prosseguir.", "Cliente", ListaClientes.this);
			}else{
			
					try{
						
						// Salva os dados do cliente.
						dataBase.updateCliente(cliente.getId(),cliente.getCPFCNPJ().toString(),cliente.getNome().toString(),
								cliente.getFone().toString(), cliente.getCelular().toString(),
								cliente.getEmail().toString(), cliente.getContato().toString(),
								cliente.getEndereco().toString(), cliente.getCEP().toString(),cliente.getCidade().toString());
						Toast.makeText(ListaClientes.this, "Cadastro salvo.", Toast.LENGTH_SHORT).show();
						
						
					}catch(Exception e){
						Utilitarios.showMessage("Erro ao cadastrar.", "Erro", ListaClientes.this);
					}
			}
		}	
		
		void excluiCadastro(){
			try{
					cliente.setCPFCNPJ(edcnpjcpf.getText().toString());
					cliente.setNome(ednome.getText().toString());
					cliente.setFone(edfone.getText().toString());
					cliente.setCelular(edcelular.getText().toString());
					cliente.setEmail(edemail.getText().toString());
					cliente.setContato(edcontato.getText().toString());
					cliente.setEndereco(edendereco.getText().toString());
					cliente.setCEP(edcep.getText().toString());
					cliente.setCidade(edcidade.getText().toString());
					
					// Exclui cadastro
					dataBase.deleteCliente(cliente.getId());
					Toast.makeText(ListaClientes.this, "Cadastro exclu�do.", Toast.LENGTH_SHORT).show();
					carregaListaPessoas();
			
					
					
				}catch(Exception e){
					Utilitarios.showMessage("Erro ao excluir"+e.getMessage(), "Erro", ListaClientes.this);
			}
			
		}




		
		
		void visualizaLocalizacao() {

			/*
			    Após salva a localização do cliente no database.
			    Visualiza localização do cliente com base na altitule e longitude vinda do banco de dados do cliente cadastradas previamente.
			    Verifica também a localização atual para traçar uma rota.
                O layout cadastro tem um fragment dentro de um layout layoutMapa que é por padrão invisível
                Se torna visível quando alguma rotina chamma esta rotina de visualização pois só mostra o mapa para
                o cliente que tenha a localização já salva.

            */


			if ((cliente.getLatitude()!=0) ||(cliente.getLongitude()!=0)){
               /* Variáveis que manipulam o GPS



                permite o acesso a serviços de localização e status de GPS
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Location loc; 	// = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			    lm e loc abaixo utilizados para buscar dados da localização atual

                */
				
			System.out.println("Seta valor em lm.");
		    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


			if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);

							System.out.println("Dentro do IF");
							System.out.println("Seta valor em loc.");
                            //Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Location loc = new Location(lm.GPS_PROVIDER);

							System.out.println("Setou valor em loc.");

                            // Define o mapa
                            GoogleMap mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapCad)).getMap();

                            //Seta no mapa a localização atual
                            mMap.setMyLocationEnabled(true);

                            FrameLayout layout = (FrameLayout) findViewById(R.id.layoutMapa);
                            layout.setVisibility(View.VISIBLE);

                            //LatLng atualSystemLocation = atualizaLocation(loc);

							//LatLng atualSystemLocation = new LatLng(loc.getLatitude(), loc.getLongitude()); // localização atual (latitude e longitude)

                            /*
                            LatLng atualSystemLocation = atualizaLocation(loc);

							System.out.println("Setou valor atualSystemLocation."+"Latitude: "+loc.getLatitude()+" Longitude: "+loc.getLongitude());




                            Marker frameworkSystem = mMap.addMarker(new MarkerOptions()
							//.icon(BitmapDescriptorFactory.fromResource(R.drawable.home))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
							.position(atualSystemLocation)
							.title("Localização Atual")
							.snippet("Ponto de partida.")
							);
						
							System.out.println("Antes do move camera atualSystemLocation.");
							// Move a câmera para Framework System com zoom 15.
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atualSystemLocation , 15));
                            */
							
							System.out.println("Setou valor em clienteLocation.");
							// Define a localizaçãoo do cliente e seta dados e marcações do cliente no mapa
							LatLng clienteLocation = new LatLng(cliente.getLatitude(), cliente.getLongitude()); //Localização salva do cliente (latitude e longitude)

							Marker frameworkCliente = mMap.addMarker(new MarkerOptions()
							//.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
							.position(clienteLocation)
							.title(cliente.getNome())
							.flat(true)
							.snippet("Endereço: "+cliente.getEndereco())
								);
							
							
							System.out.println("Antes do move camera clienteLocation.");
							// Move a câmera para clienteLocation  com zoom 15.
							
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clienteLocation , 15));
				
						
							CameraPosition cameraPosition = CameraPosition.builder()
					                .target(clienteLocation)
					                .zoom(13)
					                .bearing(90)
					                .build();
							
							
							System.out.println("Animate camera");
							// Animate the change in camera view over 2 seconds
					        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000, null);
						
				           /* Traça pontos entre a posição atual e a posição do cliente

                            PolylineOptions line = new PolylineOptions();
                            line.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
                            line.add(new LatLng(cliente.getLatitude(),cliente.getLongitude()));
                            //line.color()
                            Polyline polyline = mMap.addPolyline(line);
                            polyline.setGeodesic(true);
                            */

                            //Traça rota entre o local atual e o destino


/*


                            String origem = "-25.443195, -49.280977";
                            String destino = "-25.442207, -49.278403";
                            System.out.println("Requisição Rotas: ");
                            String url = "http://maps.googleapis.com/maps/api/directions/json?origin="+origem+
                                    "&destinaton="+destino+"&sensor=true&mode=driving";

                            // Chama o método que traça a rota para o mapa




                            tracaRota(origem,destino);






                            //String url = "http://maps.google.com/maps?f=d&addr="+origem+"&daddr="+destino+"&hl=pt";
                            System.out.println("Rotas: "+ url);
                            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                            Uri uri = Uri.parse(url);

                            System.out.println("Rotas: "+ url);

*/




/*

                LocationListener locationListener = new LocationListener(){
                    /*
                    *  Listener utilizada abaixo para tratar os eventos de atualização do GPS
                    * */
/*

                    @Override
                    public void onLocationChanged(Location location) {
                        // Chama método que atualiza a localização

                        // lm e loc abaixo utilizados para buscar dados da localização atual
								/*
								lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
								loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
								atualSystemLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

								Marker locationSystem = mMap.addMarker(new MarkerOptions()
								.position(atualSystemLocation)
								.title("Localização Atual")
								//.snippet("Ponto de partida.")
								);
								*/

/*
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ListaClientes.this,"Gps Disabled",Toast.LENGTH_SHORT ).show();

                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ListaClientes.this,"Gps Enabled",Toast.LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                        // TODO Auto-generated method stub

                    }


                };

*/
			             /* Seta os parâmetros para caso ocorram updates na localização
			                1º - parâmetro é o provider, neste caso o GPS
			                2º - parâmetro é o tempo mínimo em milisegundos que é o intervalo que a aplicação deve atualizar a localização,
			                     se for zero atualiza sempre que a localização mudar
			                3º - parâmetro distância mínima em metros necessária que deve ser percorrido para a aplicação receber as atualizações
			                4º- parâmetro que representa a implementação da LocationListener, que trata os métodos para cada evento ocorrido com o GPS
                         */

                         lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);

                           // Seta valor de indexLoc,  que é a variável que controla os indices da lista de latLng
                            indexLoc=0;
                            System.out.println("indexLoc="+indexLoc);









            }else{
							Toast.makeText(ListaClientes.this, "Problemas na ativação do GPS. Verifique e tente novamente.", Toast.LENGTH_SHORT).show();
							Lista=false;
							//invalidateOptionsMenu();
                            // Volta para a lista de clientes e Abre as configurações para ativar o gps

                            i = new Intent(ListaClientes.this, ListaClientes.class);
                            Lista=true;
                            invalidateOptionsMenu();
                            startActivity(i);

                            Intent callGPSSettingIntent =  new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);



						}
			}else{
				
				Utilitarios.showMessage("Cliente ainda não tem posição no mapa salva. Salve local correto e então poderá visualizar no mapa.", "Mapa", ListaClientes.this);
			}
			
       
		}


    LocationListener locationListener = new LocationListener(){
                    /*
                    *  Listener utilizada abaixo para tratar os eventos de atualização do GPS
                    * */


        @Override
        public void onLocationChanged(Location location ) {
            // Chama método que atualiza a localização

            // lm e loc abaixo utilizados para buscar dados da localização atual


								LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
								Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                                // Define o mapa
                                GoogleMap mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapCad)).getMap();
                                //mMap.setMyLocationEnabled(false);

                                LatLng atualSystemLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

                                //atualizaLocation(loc);
                                System.out.println("Adiciona location na lista");

                                listaLatLng.add(atualSystemLocation);

                                // Traça pontos entre a posição atual e a posição do cliente



                               if (indexLoc>0) {

                                   System.out.println("Adiciona location na lista. Index: "+indexLoc);
                                   line.add((LatLng) listaLatLng.get(indexLoc-1));



                                   System.out.print("Adiciona location na lista. Index: " + indexLoc);
                                    line.add((LatLng) listaLatLng.get(indexLoc));
                                    partida=false;
                                }

                                indexLoc=indexLoc+1;

                                line.color(Color.BLUE);
                                Polyline polyline = mMap.addPolyline(line);
                                polyline.setGeodesic(true);


                                // Se a lista tiver somente o primeiro local vai setar o indicador do ponto de partida


                                if(partida) {

                                    Marker locationSystem = mMap.addMarker(new MarkerOptions()
                                                    .position(atualSystemLocation)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                                                    .title("Localização Atual")
                                            //.snippet("Ponto de partida.")
                                    );
                                }








        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(ListaClientes.this,"Gps Disabled",Toast.LENGTH_SHORT ).show();

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            Toast.makeText(ListaClientes.this,"Gps Enabled",Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            // TODO Auto-generated method stub

        }


    };





    /**
     *
     * Método que faz a requisição dos dados para a classe Http
     */
    public void tracaRota (final String origem, final String destino){






        //String  params  =  "origen|destino" ;
        //try  {
        //    params  =  URLEncoder. encode(params, "UTF-8");
        //}  catch  ( UnsupportedEncodingException e1 )  {
        //    // TODO Auto-gerado bloco catch
         //   e1 . printStackTrace ();
        //}



        final ProgressDialog dialogo = new ProgressDialog(this);




        // Instancia uma AsyncTask para executar a pesquisa na web em uma Thread fora da principal.
        AsyncTask<String, Void, JSONObject> task = new AsyncTask<String, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(String... strings) {

                try {


                    // URL com parâmetros e caracteres epeciais
                    final String params = "origin=" + origem +"&destinaton="+destino+"&sensor=true&mode=driving";
                    String url = URLEncoder.encode(params, "UTF-8");

                    // URL Principal
                    final String newUrl="http://maps.googleapis.com/maps/api/directions/json?"+params;
                    System.out.println("NewURL - " +newUrl);

                    Http http = new Http(newUrl);


                /*
                * Método que executa automaticamente a AssyncTask
                * Faz o processamento em Background
                * */
                //Chama o método que executa a pesquisa que retorna um Json

                Log.i("JSON LISTA", "antes de chamar http");
                JSONObject json = http.consultaRota();
                //Passa o json para o método onPostExecute
                return json;

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
            /*
            * Método chamado antes da execução da Thread
            * Utilizado também para apresentar uma mensagem de que o processamento está sendo executado
            * */
                super.onPreExecute();
                Log.i("Rotas ", "onPreExecute");
                dialogo.setMessage("Pesquisando. Aguarde...");
                dialogo.show();
            }

            @Override
            protected void onPostExecute(JSONObject json) {
              /*
              * Método executado a atualização da interface na Thread principal
              * Utilizado para atualizar as views da thread principal
              *
              * */
                super.onPostExecute(json);
                Log.i("Rotas " , "onPostExecute");
                /* Atualiza a view da tela principal*/



                if (json!= null) {
     //               try {

                        // Seta os dados na classe telefone
                        //fone.setNumero(telefone);
                        //fone.setOperadora(json.getString("operadora"));
                        //fone.setEstado(json.getString("estado"));
                        //fone.setPortabilidade(Boolean.parseBoolean(json.getString("portabilidade")));
                        System.out.print("Retorno JSon com as rotas: "+json);

     //               } catch (JSONException e) {
     //                   Log.e("Erro Json", "Erro no parsing JSON");
     //                   e.printStackTrace();
     //               }
                }


                // Finaliza a dialog que estava executando
                if (dialogo.isShowing()) {
                    dialogo.dismiss();
                }
            }
        };
        // Executa a task (AsyncTask) acima com todos os métodos
        task.execute();
    };



    void salvaLocalizacao(){
			// Com base nas coordenadas do GPS, seta no clinete a latitude e longitude atual. 
			// Salva no banco de dados do cliente 
			// Verifica o status do GPS
			/*
			if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				System.out.println("GPS Ativo");
			}else{System.out.println("GPS Inativo");
				}
			
				*/


            System.out.println("Entrou na rotina salva localização");

			LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			System.out.println(lm.isProviderEnabled(LocationManager.GPS_PROVIDER));

			if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){


                System.out.println("Entrou no if ");

			    Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			    LatLng atualSystemLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

                System.out.println("Antes de setar loc em cliente...");
			    cliente.setLatitude(loc.getLatitude());
			    cliente.setLongitude(loc.getLongitude());


			
                    try{

                        // Salva os dados do cliente.
                        dataBase.gravaLocalizacaoCliente(cliente.getId(), cliente.getLatitude().toString(), cliente.getLongitude().toString());
                        Toast.makeText(ListaClientes.this, "Localização salva.", Toast.LENGTH_SHORT).show();

                    }catch(Exception e){
                        Utilitarios.showMessage("Erro ao cadastrar!"+e.getMessage(), "Erro", ListaClientes.this);

                    }
                    }else{

                        Toast.makeText(ListaClientes.this, "Problemas na ativação do GPS. Verifique e tente novamente.", Toast.LENGTH_SHORT).show();

                        // Volta para a lista de clientes e Abre as configurações para ativar o gps

                        i = new Intent(ListaClientes.this, ListaClientes.class);
                        Lista=true;
                        invalidateOptionsMenu();
                        startActivity(i);

                        Intent callGPSSettingIntent =  new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);


            }

		}
	
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			   		getMenuInflater().inflate(R.menu.menu_cadastro_clientes_edicao, menu);
			   		return super.onCreateOptionsMenu(menu);
		
		}
		
		
		@Override
		public boolean onPrepareOptionsMenu(Menu menu) {
			/* Carrega este método toda vez que é selecionada a opção menu do aparelho e testa então quais itens devem aparecer
			 conforme as variáveis mapa e lista
			*/
			MenuItem itemSalvaLocal = menu.findItem(R.id.menuitem_mapa_clientes_salvalocal);
			MenuItem itemSalvaCliente = menu.findItem(R.id.menuitem_cadastro_clientes_edicao_salvar);
			MenuItem itemVoltar = menu.findItem(R.id.menuitem_cadastro_clientes_edicao_voltar_Lista);
			MenuItem itemExcluir = menu.findItem(R.id.menuitem_cadastro_clientes_edicao_exlcuir);
			//MenuItem itemMapa = menu.findItem(R.id.menuitem_cadastro_clientes_edicao_mapa);
			MenuItem itemNovo = menu.findItem(R.id.menuitem_cadastro_clientes_edicao_novocliente);
			MenuItem itemInicio = menu.findItem(R.id.menuitem_cadastro_clientes_edicao_voltar_principal);
			

			
			if (Mapa==true){
				
				itemSalvaLocal.setVisible(false);
		    	itemSalvaCliente.setVisible(false);
		    	itemVoltar.setVisible(true);
		    	itemExcluir.setVisible(false);
		    	//itemMapa.setVisible(false);
		    	itemNovo.setVisible(false);
		    	itemInicio.setVisible(true);
			}else{	
				 if(Lista==true){
					
					 
			    	itemSalvaLocal.setVisible(false);
			    	itemSalvaCliente.setVisible(false);
			    	itemVoltar.setVisible(false);
			    	itemExcluir.setVisible(false);
			    	//itemMapa.setVisible(false);
			    	itemNovo.setVisible(true);
			    	itemInicio.setVisible(true);
			    	
			    	
			    	
				}else{
					
					
					itemSalvaLocal.setVisible(true);
			    	itemSalvaCliente.setVisible(true);
			    	itemVoltar.setVisible(true);
			    	itemExcluir.setVisible(true);
			    	//itemMapa.setVisible(true);
			    	itemNovo.setVisible(false);
			    	itemInicio.setVisible(false);
			    	
					 
				}
			}
			return super.onPrepareOptionsMenu(menu);
			//return true;
			
		}
	

		@Override
		public void invalidateOptionsMenu() {
			// Invalida as opções de menu, sempre que chamado ele automaticamente invalida e o sistema chama o método onPrepareOptionsMenu
			super.invalidateOptionsMenu();
			
		}

		public boolean onOptionsItemSelected(MenuItem item){

			
			switch (item.getItemId()){
			
			case R.id.menuitem_cadastro_clientes_edicao_salvar:
				 if (cliente !=null){			
					 salvaCadastro();
					 return true;}else{
						 Utilitarios.showMessage("Selecione um cliente", "Cliente", ListaClientes.this);
						 return false;
				}
				
			case R.id.menuitem_cadastro_clientes_edicao_exlcuir: 
				if (cliente !=null){
					excluiCadastro();
					return true;}else{
						 Utilitarios.showMessage("Selecione um cliente", "Cliente", ListaClientes.this);
						 return false;	
					}
	
			case R.id.menuitem_cadastro_clientes_edicao_voltar_Lista: 
				// Executa algo
				i = new Intent(ListaClientes.this, ListaClientes.class);
				Lista=true;
				invalidateOptionsMenu();
	            startActivity(i);
				return true;
				
			case R.id.menuitem_cadastro_clientes_edicao_voltar_principal: 
				// Executa algo
				i = new Intent(ListaClientes.this, MenuPrincipal.class);
	            startActivity(i);
				return true;
					
			//case R.id.menuitem_cadastro_clientes_edicao_mapa:
			//	Mapa=true;
			//	invalidateOptionsMenu();
			//	visualizaLocalizacao();
			//		return true;
			case R.id.menuitem_mapa_clientes_salvalocal:
				    salvaLocalizacao();
				    return true;
				
				    
			case R.id.menuitem_cadastro_clientes_edicao_novocliente: 
				 	i = new Intent(ListaClientes.this, CadastroCliente.class);
				 	startActivity(i);
				 	return true;
				
			default:	
				return super.onOptionsItemSelected(item);
			}
		}
		
}	

	