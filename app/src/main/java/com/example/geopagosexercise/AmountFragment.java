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
import android.widget.TextView;
import android.widget.Toast;

import com.example.geopagosexercise.adapters.SpinCurrencyAdapter;
import com.example.geopagosexercise.bo.Currency;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AmountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AmountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView etAmount;
    private CardView btnPM;
    private Spinner sCurrency;
    private SpinCurrencyAdapter adapter;

    private Currency currency;

    private MainActivity mainActivity;

    public AmountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AmountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AmountFragment newInstance(String param1, String param2) {
        AmountFragment fragment = new AmountFragment();
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
        View fragment = inflater.inflate(R.layout.fragment_amount, container, false);

        /* Obtengo el activity padre */
        mainActivity = (MainActivity) getActivity();


        /* Hago las referencias a los objetos en pantalla */
        btnPM = (CardView) fragment.findViewById(R.id.btnPM);
        etAmount= (TextView) fragment.findViewById(R.id.etAmount);
        sCurrency = (Spinner) fragment.findViewById(R.id.sCurrency);


        /* Obtengo los datos a mostrar en el Spinner */
        List<Currency> currencyList = Currency.listAll(Currency.class);


        /* Seteo la vista que va a tener el adapter */
        adapter = new SpinCurrencyAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                currencyList);


        /* Seteo al spinner el adapter */
        sCurrency.setAdapter(adapter);


        /* Evento al seleccionar un item */
        sCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                /* Me quedo con el Currency de la posición obtenida del Spiner */
                currency = adapter.getItem(position);
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
        btnPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sAmount = etAmount.getText().toString();
                double amount = Double.valueOf(sAmount);


                /* Valido que se completo un valor en el monto */
                if(!sAmount.equals("") && amount != 0) {

                    /* Guardo los datos del monto seleccionadas */
                    mainActivity.setAmount(amount,amount);
                    mainActivity.setCurrency(currency.getcSymbol(),currency.getcCountry());
                    mainActivity.changeFragment(getString(R.string.f_payment_method));

                }else{
                    Toast.makeText(getContext(),"Debe ingresar un monto a abonar para continuar",Toast.LENGTH_LONG).show();
                }
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
