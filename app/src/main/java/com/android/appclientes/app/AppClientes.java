package com.android.appclientes.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AppClientes extends Activity {
    private static final String CAT = "AppClientes";
    Boolean loginOk = false; //Controla se login foi efetuado. Se true, então acessa a tela principal do sistema e também habilita dos menus
    // se false permanece na tela de login.
    Intent i ; //Variável que manipula os intentos chamados na tela principal


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button btAcessar = (Button) findViewById(R.id.login_btAcessar);
        final EditText txtlogin = (EditText) findViewById(R.id.login_txtLogin);
        final EditText txtsenha = (EditText) findViewById(R.id.login_txtsenha);

        btAcessar.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                String login = txtlogin.getText().toString();
                String senha = txtsenha.getText().toString();


                i = new Intent(AppClientes.this, MenuPrincipal.class);
                startActivity(i);
             /* Implementar a parte do acesso ao server



                if (login.equals("")||senha.equals("")){
                    Utilitarios.showMessage("Login e senha obrigatórios.", "Login", AppClientes.this);
                }else{

                    System.out.println("Tela login: login = " + txtlogin.getText().toString()+ " senha = "+txtsenha.getText().toString());

                    try {
                        loginOk = new WebServiceLogin().execute(login, senha).get();
                        if (loginOk){
                            i = new Intent(AppClientes.this, MenuPrincipal.class);
                            startActivity(i);

                        }else{
                            Utilitarios.showMessage("Problemas ao efetuar login. Verifique usuário ou senha", "Web Service", AppClientes.this);
                        }

                    } catch (InterruptedException e) {
                        Log.i(CAT,"Falha na chamada. InterruptedException. "+e.getMessage());
                        Utilitarios.showMessage("Problemas ao fazer a conexão.", "Web Service", AppClientes.this);

                    } catch (ExecutionException e) {
                        Log.i(CAT,"Falha na chamada. ExecutionException. "+e.getMessage());
                        Utilitarios.showMessage("Problemas ao fazer a conexão. Verifique conexão de rede. ", "Web Service", AppClientes.this);

                    }catch (Exception e){
                        Log.i(CAT,"Falha na chamada. Excessão. "+e.getMessage());
                        Utilitarios.showMessage("Problemas ao fazer a conexão.", "Web Service", AppClientes.this);
                    }


                }
 */
            }

        });

            }
  }
