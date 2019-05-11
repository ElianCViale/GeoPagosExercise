package com.example.geopagosexercise;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.geopagosexercise.bo.Currency;
import com.example.geopagosexercise.bo.Payment;
import com.example.geopagosexercise.bo.PaymentMethod;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AmountFragment.OnFragmentInteractionListener, PaymentMethodsFragment.OnFragmentInteractionListener, BanksFragment.OnFragmentInteractionListener
                                                    , InstallmentsFragment.OnFragmentInteractionListener, SuccessViewFragment.OnFragmentInteractionListener {

    // Constantes
    public static final String AMOUNT = "Amount";
    public static final String PAYMENTMETHOD = "PaymentMethod";
    public static final String CARDISSUER = "CardIssuer";
    public static final String INSTALLMENTS = "Installments";
    public static final String PAYMENT = "Payment";

    public Payment payment;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Inicializo variables */
        payment = new Payment();

        /* Me conecto a la API de MercadoPago para obtener los metodos de pago*/
        connectionAPIPaymentMethods();

        /* Cargo las monedas */
        setCurrency();

        /* Inicializo el Fragment en cual comenzar */
        changeFragment(AMOUNT);
    }

    private void setCurrency() {
        Currency.deleteAll(Currency.class);

        /* Cargo las monedas */
        Currency currency = new Currency();
        currency.setcSymbol("USD $");
        currency.setcCountry("Estados Unidos");

        currency.save();

        currency = new Currency();
        currency.setcSymbol("ARG $");
        currency.setcCountry("Argentina");

        currency.save();
    }

    private void connectionAPIPaymentMethods() {
        /* Me conecto a la API de MercadoPago */

        /* Muestro el ProgressDialog */
        showProgressDialog();

        /* Inicializo variables y obtengo la KEY de MercadoPago */
        OkHttpClient client = new OkHttpClient();
        String publicKeyMP = getString(R.string.public_key_mercado_pago);
        String url ="https://api.mercadopago.com/v1/payment_methods?public_key=" + publicKeyMP;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    /* Obtengo la respuesta de la API */
                    final String result = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!result.equals("")) {
                                try {
                                    /* Paso a un JsonArray el resultado obtenido */
                                    JSONArray array = new JSONArray(result);

                                    /* Limpio la tabla de PaymentMethods  */
                                    PaymentMethod.deleteAll(PaymentMethod.class);

                                    /* Recorro el JsonArray para obtener los datos necesarios */
                                    for (int i = 0; i < array.length(); i++) {
                                        //Obtengo el elemento y lo guardo en PaymentMethod
                                        JsonObject row = new JsonParser().parse(array.getJSONObject(i).toString()).getAsJsonObject();

                                        PaymentMethod paymentMethod = new PaymentMethod();
                                        paymentMethod.setPmId(row.get("id").getAsString());
                                        paymentMethod.setPmName(row.get("name").getAsString());
                                        paymentMethod.setPmType(row.get("payment_type_id").getAsString());
                                        paymentMethod.save();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            /* Oculto el ProgressDialog */
                            hideProgressDialog();
                        }
                    });
                }
            }
        });
    }

    /* Metodo para ser consumido por los diferentes fragment y cambiar a otro */
    public void changeFragment(String Fragment) {
        switch (Fragment) {
            case AMOUNT:
                payment = new Payment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AmountFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case PAYMENTMETHOD:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new PaymentMethodsFragment())
                        .addToBackStack(null)
                        .commit();
                break;

            case CARDISSUER:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new BanksFragment())
                        .addToBackStack(null)
                        .commit();
                break;

            case INSTALLMENTS:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new InstallmentsFragment())
                        .addToBackStack(null)
                        .commit();
                break;

            case PAYMENT:

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SuccessViewFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    /** INICIO: Todos los SETs para ir cargando el atributo Payment */
    public void setPaymentMethod(String paymentMethodId,String paymentMethodName,String paymentType) {
        payment.setPaymentMethodId(paymentMethodId);
        payment.setPaymentMethodName(paymentMethodName);
        payment.setPaymentType(paymentType);
    }

    public void setCardIssuer(int cardIssuerId,String cardIssuerName) {
        payment.setCardIssuerId(cardIssuerId);
        payment.setCardIssuerName(cardIssuerName);
    }

    public void setInstallments(double installmentsAmount,String installmentsAmountMsg,int installmentsCount,double installmentsRate,double finalAmount) {
        payment.setInstallmentsAmount(installmentsAmount);
        payment.setInstallmentsAmountMsg(installmentsAmountMsg);
        payment.setInstallmentsCount(installmentsCount);
        payment.setInstallmentsRate(installmentsRate);
        payment.setFinalAmount(finalAmount);
    }

    public void setAmount(double originalAmount,Double finalAmount) {
        payment.setOriginalAmount(originalAmount);
        payment.setFinalAmount(finalAmount);
    }

    public void setCurrency(String currencySymbol,String currencyCountry) {
        payment.setCurrencySymbol(currencySymbol);
        payment.setCurrencyCountry(currencyCountry);
    }
    /** FIN: Todos los SETs para ir cargando el atributo Payment */

    /* Metodo para mostrar un ProgressDialog */
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /* Metodo para ocultar el ProgressDialog */
    public void hideProgressDialog() {
        progressDialog.dismiss();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
