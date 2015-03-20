package com.android.appclientes.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class AppClientes extends Activity {
    private static final String CAT = "AppClientes";
    public CadastroDataBase dataBase = new CadastroDataBase(AppClientes.this);//Instancia a classe do banco de dados
    public User user = new User();
    Boolean loginOk = false; //Controla se login foi efetuado. Se true, então acessa a tela principal do sistema e também habilita dos menus
    // se false permanece na tela de login.
    Intent i ; //Variável que manipula os intentos chamados na tela principal
    private static final String NOME = "pref"; //nome da estrutura de preferencias do aplicativo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        Button btAcessar = (Button) findViewById(R.id.login_btAcessar);
        final EditText txtlogin = (EditText) findViewById(R.id.login_txtLogin);


        /* Campos utilizados para senha e confirmação da senha
           Faz as configurações para ficar com máscara de senha
        */
        final EditText txtSenha = new EditText(this);
        txtSenha.setTextColor(Color.BLACK);

        txtSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
        txtSenha.setWidth(500);



        final TextView tSenha = new TextView(this);
        tSenha.setText("Senha:");
        tSenha.setTextColor(Color.BLACK);

        /* Campos utilizados para senha e confirmação da confirmação da senha
           Faz as configurações para ficar com máscara de senha
        */
        final EditText txtConfirmaSenha = new EditText(this);
        txtConfirmaSenha.setTextColor(Color.BLACK);

        txtConfirmaSenha.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtConfirmaSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtConfirmaSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
        txtConfirmaSenha.setWidth(500);



        final TextView tConfirmaSenha = new TextView(this);
        tConfirmaSenha.setTextColor(Color.BLACK);
        txtConfirmaSenha.setHint("Confirme a senha");
        tConfirmaSenha.setText("Confirmação:");


        //Cria uma checkbox para salvar o email em preferencias da aplicação

        final CheckBox checkEmail = new CheckBox(this);
        checkEmail.setText("Lembre-me");
        checkEmail.setTextColor(Color.BLACK);


        //Recupera o valor do flag salvo em preferencias
        SharedPreferences pref = getSharedPreferences(NOME, 0);
        // O segundo argumento é o valor default caso não encontrar, mas pref.getBoolean vai trazer o valor gravado
        boolean marcado = pref.getBoolean("status",false);
        String email = pref.getString("login",null);
        checkEmail.setChecked(marcado);
        if (marcado)
            txtlogin.setText(email);
        else
            txtlogin.setText(null);



        TableLayout layoutSenha = (TableLayout) findViewById(R.id.layoutSenha) ; // Instancia do layout criado na tela para os campos senha e confirmação de senha utilizados e tratados em tempo de execução

        // Cria as linhas do Table Layout
        //Linha 1
        TableRow linha1 = new TableRow(this);

        //Linha 2
        TableRow linha2 = new TableRow(this);

        //Linha 3

        TableRow linha3 = new TableRow(this);
        // Busca os usuários do sistema. Caso não tenham usuários solicita o cadastramente, senão habilita para login

        Cursor c = dataBase.getUsers();

        if (c.getCount()>0){



            linha1.addView(tSenha);
            linha1.addView(txtSenha);

            linha3.addView(checkEmail);

            layoutSenha.addView(linha1);
            layoutSenha.addView(linha3);

            btAcessar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    //Ao acessar o usuário, salva as preferências - email
                    //Salva o flag nas preferencias
                    SharedPreferences pref = getSharedPreferences(NOME, MODE_PRIVATE);
                    //Abre as preferencias para edição
                    SharedPreferences.Editor editor = pref.edit();

                    boolean isChecked = checkEmail.isChecked();
                    String email = txtlogin.getText().toString();

                    //Atualiza o valor

                    if(isChecked==true){
                        editor.putBoolean("status",isChecked);
                        editor.putString("login",email);
                    }else{
                        editor.putBoolean("status",isChecked);
                        editor.putString("email",null);

                    }
                    //Faz commit dos dados
                    editor.commit();

                    String login = txtlogin.getText().toString();
                    String senha = txtSenha.getText().toString();

                    //user.setLogin(login);
                    //user.setSenha(senha);

                    System.out.println("Login : " + login.toString());
                    System.out.println("Senha: " + senha.toString());


                    if (login.equals("")||senha.equals("")){
                        Utilitarios.showMessage("Login e senha obrigatórios.", "Login", AppClientes.this);

                    }else {

                        //Busca o usuário pelo login
                        Cursor u = dataBase.getUser(login.toString());
                        //Se achar um usuário então acessa o sistema
                        if (u.getCount() == 1) {
                            user.setLogin(u.getString(0));
                            System.out.println("Encontrou o usuário pelo login digitado: " + user.getLogin());
                            System.out.println("Login buscado: " + user.getLogin());
                            user.setSenha(u.getString(1));
                            System.out.println("Senha: " + user.getSenha());

                            /*  Testa pelos dados retornados com base no login.
                                Se a senha estiver correta acessa, senão nega o acesso.
                            */

                            if(senha.equals(user.getSenha())){
                                i = new Intent(AppClientes.this, MenuPrincipal.class);
                                startActivity(i);
                            }else{
                                Utilitarios.showMessage("Senha inválida. Tente novamente","Login",AppClientes.this);
                            }
                        }else{
                            Utilitarios.showMessage("Usuário não encontrado. Tente novamente","Login",AppClientes.this);
                        }

                    }
                }
            });



        }else{

            // Se não exitir nenhum usuário então vai fazer a inclusão

            //Exibe mensagem de que não existem usuários e abre os campos para cadastramento






            Utilitarios.showMessage("Não existem usuários cadastrados. Informe os dados e confirme para cadastrar","Login",AppClientes.this);



            txtlogin.setHint("Crie novo usuário");
            txtSenha.setHint("Informe a senha");
            btAcessar.setText("Cadastrar");



            linha1.addView(tSenha);
            linha1.addView(txtSenha);
            linha2.addView(tConfirmaSenha);
            linha2.addView(txtConfirmaSenha);


            layoutSenha.addView(linha1);
            layoutSenha.addView(linha2);

            btAcessar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {




                    String login = txtlogin.getText().toString();
                    String senha = txtSenha.getText().toString();

                    user.setLogin(login);
                    user.setSenha(senha);
                    user.setConfirmaSenha(txtConfirmaSenha.getText().toString());

                    if (login.equals("")||senha.equals("")){
                        Utilitarios.showMessage("Login e senha obrigatórios.", "Login", AppClientes.this);

                    }else {

                        //System.out.println("Tela login: login = " + txtlogin.getText().toString() + " senha = " + txtsenha.getText().toString());

                        if (user.getSenha().toString().equals(user.getConfirmaSenha().toString())) {
                            //Cadastra o usuário
                            dataBase.addUser(login,senha);
                            Toast.makeText(AppClientes.this, "Login criado. Acesso aceito.", Toast.LENGTH_SHORT).show();
                            i = new Intent(AppClientes.this, MenuPrincipal.class);
                            startActivity(i);

                        }else{
                            Utilitarios.showMessage("Senha e confirmação devem ser iguais.", "Login", AppClientes.this);
                        }
                    }
                }
            });
        }

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
  }
