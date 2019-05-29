package com.devtech.ivn.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.widget.Toast;

import com.devtech.ivn.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class Util {

    private static Context ctx;
    private WifiManager wifi;
    private ProgressDialog progress;

    public Util(Context context) {
        ctx = context;
        wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
    }

    public Util() {

    }



    public String key() {
        Random random = new Random();
        int x = random.nextInt(999);

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        return dateFormat.format(date) + "-" + String.valueOf(x);
    }

    public int dia() {
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }

    public String data() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String anoMes() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String mes() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String mesPorExtenso() {
        DateFormat dateFormat = new SimpleDateFormat("M");
        Date date = new Date();
        String mes = dateFormat.format(date);
        String retorno = "";
        if (mes.equals("1")) {
            retorno = "Agenda de Janeiro";
        } else if (mes.equals("2")) {
            retorno = "Agenda de Fevereiro";
        } else if (mes.equals("3")) {
            retorno = "Agenda de Março";
        } else if (mes.equals("4")) {
            retorno = "Agenda de Abril";
        } else if (mes.equals("5")) {
            retorno = "Agenda de Maio";
        } else if (mes.equals("6")) {
            retorno = "Agenda de Junho";
        } else if (mes.equals("7")) {
            retorno = "Agenda de Julho";
        } else if (mes.equals("8")) {
            retorno = "Agenda de Agosto";
        } else if (mes.equals("9")) {
            retorno = "Agenda de Setembro";
        } else if (mes.equals("10")) {
            retorno = "Agenda de Outubro";
        } else if (mes.equals("11")) {
            retorno = "Agenda de Novembro";
        } else if (mes.equals("12")) {
            retorno = "Agenda de Dezembro";
        }
        return retorno;
    }

    public String mesPorExtenso1(String mes) {
        String retorno = "";
        if (mes.equals("01")) {
            retorno = "Janeiro";
        } else if (mes.equals("02")) {
            retorno = "Fevereiro";
        } else if (mes.equals("03")) {
            retorno = "Março";
        } else if (mes.equals("04")) {
            retorno = "Abril";
        } else if (mes.equals("05")) {
            retorno = "Maio";
        } else if (mes.equals("06")) {
            retorno = "Junho";
        } else if (mes.equals("07")) {
            retorno = "Julho";
        } else if (mes.equals("08")) {
            retorno = "Agosto";
        } else if (mes.equals("09")) {
            retorno = "Setembro";
        } else if (mes.equals("10")) {
            retorno = "Outubro";
        } else if (mes.equals("11")) {
            retorno = "Novembro";
        } else if (mes.equals("12")) {
            retorno = "Dezembro";
        }
        return retorno;
    }

    public void Compartilhar(Context context, int position) {

        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.setType("image/jpg");
            share.setType("image/png");
            share.setType("text/plain");

            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            //share.putExtra(Intent.EXTRA_TEXT, "teste");

            final int[] photos = {
                    R.drawable.dardos1,
                    R.drawable.dardos2,
                    R.drawable.dardos3,
                    R.drawable.dardos4,
                    R.drawable.dardos5,
                    R.drawable.dardos6,
                    R.drawable.dardos7,
            };

            Uri imageUri = null;
            try {
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), photos[position]), null, null));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            share.putExtra(Intent.EXTRA_STREAM, imageUri);
            context.startActivity(Intent.createChooser(share, "Selecione"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "teste", Toast.LENGTH_LONG).show();
        }
    }

    private void sharedImage(String nomeArquivo, String texto, Context context) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.setType("image/jpg");
        share.setType("image/png");
        share.setType("text/plain");

        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, texto);

        Uri imageUri = Uri.parse("sdcard/dardos/" + nomeArquivo);
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        context.startActivity(Intent.createChooser(share, "Selecione"));
    }

    public void download(final String url, final String nomeArquivo, final String texto, final Context context) {
        progress = ProgressDialog.show(context, "", "Baixando ...", true, false);
        Toast.makeText(context, "Fazendo download da imagem aguarde.", Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                try {

                    File folder = new File("sdcard/dardos");
                    if (!folder.exists()) {
                        folder.mkdir();
                    }

                    String FileName = url.substring(url.lastIndexOf('/') + 1, url.length());
                    File DestFile = new File(folder, FileName);
                    Copiando(file, DestFile, nomeArquivo, texto, context);
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Copiando(File src, File dst, String nomeArquivo, String texto, Context context) {
        Toast.makeText(context, "Carregando...", Toast.LENGTH_SHORT).show();
        try {

            FileChannel inChannel = new FileInputStream(src).getChannel();
            FileChannel outChannel = new FileOutputStream(dst).getChannel();
            String arquivo = String.valueOf(dst);

            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
                outChannel.force(true);
            } finally {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sharedImage(nomeArquivo, texto, context);
        }
    }
}
