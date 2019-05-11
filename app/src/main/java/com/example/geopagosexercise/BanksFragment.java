package com.example.geopagosexercise;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.geopagosexercise.adapters.SpinBanksAdapter;
import com.example.geopagosexercise.bo.CardIssuer;
import com.example.geopagosexercise.bo.Installments;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BanksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BanksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BanksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner sBanks;
    private CardView btnInstallments;
    private SpinBanksAdapter adapter;
    private CardIssuer cardIssuer;

    private MainActivity mainActivity;

    public BanksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BanksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BanksFragment newInstance(String param1, String param2) {
        BanksFragment fragment = new BanksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_banks, container, false);


        /* Obtengo el activity padre */
        mainActivity = (MainActivity) getActivity();


        /* Hago las referencias a los objetos en pantalla */
        sBanks = (Spinner) fragment.findViewById(R.id.sBanks);
        btnInstallments= (CardView) fragment.findViewById(R.id.btnInstallments);


        /* Obtengo los datos a mostrar en el Spinner */
        List<CardIssuer> cardIssuerList = CardIssuer.listAll(CardIssuer.class);


        /* Seteo la vista que va a tener el adapter */
        adapter = new SpinBanksAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                cardIssuerList);


        /* Seteo al spinner el adapter */
        sBanks.setAdapter(adapter);


        /* Evento al seleccionar un item */
        sBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                /* Me quedo con el CardIssuer de la posición obtenida del Spiner */
                cardIssuer = adapter.getItem(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        /* Evento click del boton Continuar */
        clickContinuar();

        return fragment;
    }

    private void clickContinuar() {
        /* Evento click boton "Continuar" */
        btnInstallments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Guardo los datos de la entidad bancaria o distribuidora de la tarjeta */
                mainActivity.setCardIssuer(cardIssuer.getCiId(),cardIssuer.getCiName());


                /* Muestro el ProgressDialog */
                mainActivity.showProgressDialog();


                /* Me conecto a la API de MercadoPago para obtener las cuotas*/
                connectionAPIInstallments();
            }


            private void connectionAPIInstallments() {
                /* Inicializo variables y obtengo la KEY de MercadoPago */
                OkHttpClient client = new OkHttpClient();
                String publicKeyMP = getString(R.string.public_key_mercado_pago);
                String url ="https://api.mercadopago.com/v1/payment_methods/installments?public_key=" + publicKeyMP + "&payment_method_id=" + mainActivity.payment.getPaymentMethodId() + "&amount=" +mainActivity.payment.getOriginalAmount() + "&issuer.id=" + cardIssuer.getCiId();

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

                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!result.equals("")) {
                                        try {

                                            /* Obtengo la respuesta de la API */
                                            JSONArray array = new JSONArray(result);


                                            /* Limpio la tabla de Installments */
                                            Installments.deleteAll(Installments.class);


                                            /* Recorro el JsonArray para obtener los datos necesarios */
                                            for (int i = 0; i < array.length(); i++) {

                                                //Obtengo el elemento y lo guardo en Installments
                                                JsonObject row = new JsonParser().parse(array.getJSONObject(i).toString()).getAsJsonObject();


                                                /* Obtengo el segundo Array para obtener datos mas especificos sobre las cuotas */
                                                String jsonPayerCost = row.get("payer_costs").toString();
                                                JSONArray array2 = new JSONArray(jsonPayerCost);


                                                /* Recorro el segundo JsonArray para obtener los datos necesarios */
                                                for (int j = 0; j < array2.length(); j++) {
                                                    //Obtengo el elemento y lo guardo en PaymentMethod
                                                    JsonObject row2 = new JsonParser().parse(array2.getJSONObject(j).toString()).getAsJsonObject();

                                                    Installments installments = new Installments();
                                                    installments.setiCount(row2.get("installments").getAsInt());
                                                    installments.setiRate(row2.get("installment_rate").getAsDouble());
                                                    installments.setiAmount(row2.get("installment_amount").getAsDouble());
                                                    installments.setiAmountMsg(row2.get("recommended_message").getAsString());
                                                    installments.setiTotalAmount(row2.get("total_amount").getAsDouble());


                                                    /* Obtengo el tercer Array para obtener los datos sobre los intereses de las cuotas */
                                                    String jsonLabel = row2.get("labels").toString();
                                                    JSONArray array3 = new JSONArray(jsonLabel);


                                                    /* Recorro el tercer JsonArray para obtener los datos sobre los intereses */
                                                    for (int k = 0; k < array3.length(); k++) {

                                                        //Obtengo el elemento y lo guardo en PaymentMethod
                                                        installments.setiRateMsg(array3.get(k).toString());

                                                    }
                                                    installments.save();
                                                }
                                            }

                                            /* Si existe cuotas para el metodo de pago seleccionado y el distribuidor pasa a la pantalla de InstallmentsFragment */
                                            mainActivity.changeFragment(getString(R.string.f_installments));

                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                            /* Si no existe cuotas para el metodo de pago seleccionado y el distribuidor pasa a la pantalla de SuccesViewFragment */
                                            mainActivity.changeFragment(getString(R.string.f_payment));
                                        }
                                    }

                                    /* Oculto el ProgressDialog */
                                    mainActivity.hideProgressDialog();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
