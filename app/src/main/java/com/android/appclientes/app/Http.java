package com.android.appclientes.app;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Implementação dos métodos que fazem as operações na Web, GET e POST
 *
 */
public class Http {
    private final String CATEGORIA="HTTP";// Para uso de log para ver onde está imprimindo as mensagens
    final String url;
    //final String url = "http://private-61fc-rodrigoknascimento.apiary-mock.com/consulta/";

    //Método que faz o GET na utl conforme o número

    // Construtor da classe que recebe a url a ser pesquisada
    public Http(String url) {
        this.url = url;

    }

    public JSONObject consultaRota(){

        try {


            Log.i(CATEGORIA, "Entrou na solicitação http");
            //Cria a URL
            URL u = new URL(url);

            Log.i(CATEGORIA,"Criou a URL "+url);


            Log.i(CATEGORIA,"Cria httpclient");
            HttpClient httpClient = new DefaultHttpClient();

            Log.i(CATEGORIA,"Cria httpget ");
            HttpGet httpGet = new HttpGet(url);

            Log.i(CATEGORIA,"Executa response");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpResponse.setHeader("Accept-Language","application/json");
            httpResponse.setHeader("User-Agent","Mozilla/5.0  (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko)  Chrome/17.0.963.56 Safari/535.11");
            httpResponse.setHeader("Content-type", "application/json");


            HttpEntity entity = httpResponse.getEntity();

            InputStream in = entity.getContent();

            //String result = readString(in);
            String result = EntityUtils.toString(entity);
            JSONObject json = new JSONObject(result);
            return json;
/*
            //Cria a URL
            URL u = new URL(url);
            Log.i(CATEGORIA,url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            //Configura a requisição para GET

            Log.i(CATEGORIA, "stream reader");
            InputStreamReader in  =  new  InputStreamReader (conn.getInputStream());
            Log.i(CATEGORIA, "setou stream reader ");

            Log.i(CATEGORIA, "seta o valor em string");
            String result = in.toString();

            //conn.setRequestMethod("GET");

            //conn.setRequestProperty("Accept-Language","application/json");
            //conn.setRequestProperty("User-Agent","Mozilla/5.0  (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko)  Chrome/17.0.963.56 Safari/535.11");
            //conn.setRequestProperty("Content-type", "application/json");

            //conn.setDoInput(true);
            //conn.setDoOutput(false);

            //conn.connect();
            Log.i(CATEGORIA, "conn.getInputStream");
            //InputStream in = conn.getInputStream();
            Log.i(CATEGORIA, String.valueOf(conn.getErrorStream()));
            Log.i(CATEGORIA, "read string");
            //String result = readString(in);
            Log.i(CATEGORIA, result);

           conn.disconnect();
            JSONObject json = new JSONObject(result);

            return json;
*/


        }catch (MalformedURLException e){
            Log.e(CATEGORIA, e.getMessage(),e);
        }catch (IOException e){
            Log.e(CATEGORIA, e.getMessage(),e);
        } catch (JSONException e) {
          e.printStackTrace();
            Log.e(CATEGORIA, e.getMessage(),e);
        }

        return null;
    }






    //Faz a leitura do texto da InputStream retornada transformando em String
    private String readString(InputStream in)throws IOException{
        byte[] bytes = readBytes(in);
        String texto = new String(bytes);
        Log.i(CATEGORIA, "Http.readString: "+texto);
        return texto;

    }

    //Faz a leitura do array de bytes da InputStream retornada
    private byte[] readBytes(InputStream in) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer))>0){
                bos.write(buffer,0,len);
            }
            byte[] bytes = bos.toByteArray();
            return bytes;
        }finally {
            bos.close();
            in.close();
        }

    }

}
