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

import com.example.geopagosexercise.adapters.SpinPaymentMethodsAdapter;
import com.example.geopagosexercise.bo.CardIssuer;
import com.example.geopagosexercise.bo.PaymentMethod;
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
 * {@link PaymentMethodsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentMethodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentMethodsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner sPaymentMethods;
    private CardView btnBanks;
    private SpinPaymentMethodsAdapter adapter;
    private PaymentMethod paymentMethod;

    private MainActivity mainActivity;

    boolean cardIssuerExist;

    public PaymentMethodsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentMethodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentMethodsFragment newInstance(String param1, String param2) {
        PaymentMethodsFragment fragment = new PaymentMethodsFragment();
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
        View fragment = inflater.inflate(R.layout.fragment_payment_methods, container, false);


        /* Obtengo el activity padre */
        mainActivity = (MainActivity) getActivity();


        /* Hago las referencias a los objetos en pantalla */
        btnBanks = (CardView) fragment.findViewById(R.id.btnBanks);
        sPaymentMethods= (Spinner) fragment.findViewById(R.id.sPaymentMethods);


        /* Obtengo los datos a mostrar en el Spinner */
        List<PaymentMethod> paymentMethodList = PaymentMethod.listAll(PaymentMethod.class);


        /* Seteo la vista que va a tener el adapter */
        adapter = new SpinPaymentMethodsAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                paymentMethodList);


        /* Seteo al spinner el adapter */
        sPaymentMethods.setAdapter(adapter);


        /* Evento al seleccionar un item */
        sPaymentMethods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                /* Me quedo con el PaymentMethod de la posición obtenida del Spiner */
                paymentMethod = adapter.getItem(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {

            }
        });


        /* Evento click boton "Continuar" */
        clickContinuar();

        return fragment;
    }

    private void clickContinuar() {
        /* Evento click boton "Continuar" */
        btnBanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Muestro el ProgressDialog */
                mainActivity.showProgressDialog();

                /* Guardo los datos del metodo de pago seleccionado */
                mainActivity.setPaymentMethod(paymentMethod.getPmId(),paymentMethod.getPmName(),paymentMethod.getPmType());

                /* Me conecto a la API de MercadoPago para obtener las distribuidoras de tarjetas*/
                connectionAPICardIssuers();
            }

            private void connectionAPICardIssuers() {
                /* Me conecto a la API de MercadoPago */

                /* Inicializo variables y obtengo la KEY de MercadoPago */
                OkHttpClient client = new OkHttpClient();
                String publicKeyMP = getContext().getString(R.string.public_key_mercado_pago);
                String url ="https://api.mercadopago.com/v1/payment_methods/card_issuers?public_key=" + publicKeyMP + "&payment_method_id=" + paymentMethod.getPmId() ;
                cardIssuerExist = false;

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



                                            /* Limpio la tabla de CardIssuer */
                                            CardIssuer.deleteAll(CardIssuer.class);


                                            /* Recorro el JsonArray para obtener los datos necesarios */
                                            for (int i = 0; i < array.length(); i++) {


                                                //Obtengo el elemento y lo guardo en CardIssuer
                                                JsonObject row = new JsonParser().parse(array.getJSONObject(i).toString()).getAsJsonObject();

                                                CardIssuer cardIssuer = new CardIssuer();
                                                cardIssuer.setCiId(row.get("id").getAsInt());
                                                cardIssuer.setCiName(row.get("name").getAsString());
                                                cardIssuer.save();

                                                cardIssuerExist = true;
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    /* Oculto el ProgressDialog */
                                    mainActivity.hideProgressDialog();

                                    /* Si existe distribuidor pasa a la pantalla de BanksFragment */
                                    if(cardIssuerExist) {

                                        mainActivity.changeFragment(getString(R.string.f_card_issuer));

                                    }else{

                                        /* Si no existe distribuidor pasa a la pantalla de PaymentFragment */
                                        mainActivity.changeFragment(getString(R.string.f_payment));

                                    }
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
