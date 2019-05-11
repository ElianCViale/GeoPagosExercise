package com.example.geopagosexercise;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geopagosexercise.bo.Payment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SuccessViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SuccessViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuccessViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String CREDIT_CARD = "credit_card";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView tvPaymentMethodNameVal,tvCardIssuerVal,tvInstallmentsRateVal,tvTotalAmountVal,tvInstallmentsCountVal,tvFinalAMountVal;
    private CardView btnAmount,itemInstallmentsCount,itemCardIssuer;
    private ImageView ivEditTotalAmount,ivEditInstallments,ivEditCardIssuer,ivEditPaymentMethod;

    private MainActivity mainActivity;

    public SuccessViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuccessViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuccessViewFragment newInstance(String param1, String param2) {
        SuccessViewFragment fragment = new SuccessViewFragment();
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

        View fragment = inflater.inflate(R.layout.fragment_success_view, container, false);


        /* Obtengo el activity padre */
        mainActivity = (MainActivity) getActivity();


        /* Hago las referencias a los objetos en pantalla */
        tvPaymentMethodNameVal = (TextView) fragment.findViewById(R.id.tvPaymentMethodNameVal);
        tvCardIssuerVal = (TextView) fragment.findViewById(R.id.tvCardIssuerVal);
        tvInstallmentsCountVal = (TextView) fragment.findViewById(R.id.tvInstallmentsCountVal);
        tvInstallmentsRateVal = (TextView) fragment.findViewById(R.id.tvInstallmentsRateVal);
        tvTotalAmountVal = (TextView) fragment.findViewById(R.id.tvTotalAmountVal);
        tvFinalAMountVal = (TextView) fragment.findViewById(R.id.tvFinalAMountVal);

        ivEditTotalAmount = (ImageView) fragment.findViewById(R.id.ivEditTotalAmount);
        ivEditInstallments = (ImageView) fragment.findViewById(R.id.ivEditInstallments);
        ivEditCardIssuer = (ImageView) fragment.findViewById(R.id.ivEditCardIssuer);
        ivEditPaymentMethod = (ImageView) fragment.findViewById(R.id.ivEditPaymentMethod);

        itemCardIssuer = (CardView) fragment.findViewById(R.id.itemCardIssuer);
        itemInstallmentsCount = (CardView) fragment.findViewById(R.id.itemInstallmentsCount);
        btnAmount = (CardView) fragment.findViewById(R.id.btnAmount);


        /* Obtengo el Payment cargado del MainActivity */
        Payment payment = mainActivity.payment;


        /** INICIO: Seteo los valores de Payment que voy a mostrar en pantalla */
        tvPaymentMethodNameVal.setText(payment.getPaymentMethodName());

        /* Verifico el tipo de pago para mostar distribuidora de tarjeta */
        if(payment.getPaymentType().equals(CREDIT_CARD)) {
            itemCardIssuer.setVisibility(View.VISIBLE);
            tvCardIssuerVal.setText(payment.getCardIssuerName());
        }else{
            itemCardIssuer.setVisibility(View.GONE);
        }

        /* Verifico que tenga cuotas para mostrar en pantalla */
        if(payment.getInstallmentsCount() > 0) {
            itemInstallmentsCount.setVisibility(View.VISIBLE);
            tvInstallmentsCountVal.setText(String.valueOf(payment.getInstallmentsCount()) + " cuotas de " + payment.getCurrencySymbol() + " " + payment.getInstallmentsAmount());
        }else{
            itemInstallmentsCount.setVisibility(View.GONE);
        }

        tvInstallmentsRateVal.setText(String.valueOf(payment.getInstallmentsRate()) + "%");
        tvTotalAmountVal.setText(String.valueOf(payment.getCurrencySymbol()) + " " + payment.getOriginalAmount());
        tvFinalAMountVal.setText(String.valueOf(payment.getCurrencySymbol()) + " " + payment.getFinalAmount());
        /** FIN: Seteo los valores de Payment que voy a mostrar en pantalla */


        /** INICIO: Evento Click sobre las Imagenes para Editar */
        /* Modificar Monto */
        editAmount(ivEditTotalAmount, R.string.f_amount);

        /* Modificar Cuotas */
        editAmount(ivEditInstallments, R.string.f_installments);

        /* Modificar Distribuidor de Tarjeta */
        editAmount(ivEditCardIssuer, R.string.f_card_issuer);

        /* Modificar Metodo de pago */
        editAmount(ivEditPaymentMethod, R.string.f_payment_method);
        /** FIN: Evento Click sobre las Imagenes para Editar */


        /* Evento click boton "Confirmar" */
        btnAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Vuelvo al fragment de Amount para empezar de vuelta el ciclo */
                mainActivity.changeFragment(getString(R.string.f_amount));
            }
        });

        return fragment;
    }

    private void editAmount(ImageView ivEdit, int i) {
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.changeFragment(getString(i));
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
