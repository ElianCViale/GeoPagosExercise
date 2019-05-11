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

import com.example.geopagosexercise.adapters.SpinInstallmentsAdapter;
import com.example.geopagosexercise.bo.Installments;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InstallmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InstallmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstallmentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner sInstallments;
    private CardView btnPayment;
    private SpinInstallmentsAdapter adapter;
    private Installments installments;
    private TextView tvRate;

    private MainActivity mainActivity;

    public InstallmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstallmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InstallmentsFragment newInstance(String param1, String param2) {
        InstallmentsFragment fragment = new InstallmentsFragment();
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
        View fragment = inflater.inflate(R.layout.fragment_installments, container, false);


        /* Obtengo el activity padre */
        mainActivity = (MainActivity) getActivity();


        /* Hago las referencias a los objetos en pantalla */
        sInstallments = (Spinner) fragment.findViewById(R.id.sInstallments);
        btnPayment= (CardView) fragment.findViewById(R.id.btnPayment);
        tvRate = (TextView) fragment.findViewById(R.id.tvRate);


        /* Obtengo los datos a mostrar en el Spinner */
        List<Installments> installmentsList = Installments.listAll(Installments.class);


        /* Seteo la vista que va a tener el adapter */
        adapter = new SpinInstallmentsAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                installmentsList,mainActivity.payment.getCurrencySymbol());


        /* Seteo al spinner el adapter */
        sInstallments.setAdapter(adapter);


        /* Evento al seleccionar un item */
        sInstallments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                /* Me quedo con el Installments de la posición obtenida del Spiner */
                installments = adapter.getItem(position);

                /* Muestro en pantalla el porcentaje de interes */
                tvRate.setText(installments.getiRateMsg());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


        /* Evento click boton "Continuar" */
        clickContinuar();

        return fragment;
    }

    private void clickContinuar() {
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Guardo los datos de las cuotas seleccionadas */
                mainActivity.setInstallments(installments.getiAmount(),installments.getiAmountMsg(),installments.getiCount(),installments.getiRate(),installments.getiTotalAmount());

                /* Cambio al fragment para ver todos los datos selecionados */
                mainActivity.changeFragment(getString(R.string.f_payment));
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
