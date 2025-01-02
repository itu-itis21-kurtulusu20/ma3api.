package com.example.androidsignexample;

import android.view.View;
import android.widget.BaseAdapter;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tubitak.akis.cif.functions.ICommandTransmitter;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

import javax.smartcardio.CardTerminal;
import java.util.ArrayList;
import java.util.List;

public class CertListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private Pair<CardTerminal,ECertificate> selection;
    String ATR;

    private List<Pair<CardTerminal,ECertificate>> certificateList=new ArrayList<Pair<CardTerminal, ECertificate>>();

    public CertListAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return certificateList.size();
    }

    public Object getItem(int position) {
        return certificateList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void addItem(CardTerminal cardTerminal,ECertificate certificate){
        certificateList.add(new Pair<CardTerminal, ECertificate>(cardTerminal,certificate));
    }

    //get ATR Value
    public void getATRValue(ICommandTransmitter transmitter){
        try {
            ATR = StringUtil.toString(transmitter.atr().getBytes());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Pair<CardTerminal,ECertificate> entry = certificateList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cert_tree_row, null);
        }

        ECertificate eCertificate = entry.second;

        TextView tvId = convertView.findViewById(R.id.tvIdNumber);
        tvId.setText(eCertificate.getSubject().getSerialNumberAttribute());

        TextView tvNameAndSurname = convertView.findViewById(R.id.tvNameAndSurname);
        tvNameAndSurname.setText(eCertificate.getSubject().getCommonNameAttribute());

        TextView tvQualifiedStatus = convertView.findViewById(R.id.tvQulifiedStatus);
        if(eCertificate.isQualifiedCertificate()){
            tvQualifiedStatus.setText("Nitelikli Sertifika");
        }
        else
        {
            tvQualifiedStatus.setText("Nitelikli Olmayan Sertifika");
        }

        CheckBox selectedBox = convertView.findViewById(R.id.cb_selected_cert);
        selectedBox.setTag(entry);
        selectedBox.setOnClickListener(this);
        selectedBox.setChecked(false);
        if(selection == null)
        {
            selectedBox.setChecked(true);
            selection = entry;
        }
        else
        {
            if(selection == entry)
            {
                selectedBox.setChecked(true);
            }
        }
        return convertView;
    }

    public Pair<CardTerminal,ECertificate> getSelection(){
        return selection;
    }

    @Override
    public void onClick(View view) {
        Pair<CardTerminal,ECertificate> selectedItem = (Pair<CardTerminal, ECertificate>) view.getTag();
        selection  = selectedItem;
        notifyDataSetChanged();
    }

}
