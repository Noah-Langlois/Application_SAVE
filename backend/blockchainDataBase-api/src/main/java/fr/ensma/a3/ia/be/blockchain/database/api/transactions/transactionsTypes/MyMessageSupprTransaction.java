package fr.ensma.a3.ia.be.blockchain.database.api.transactions.transactionsTypes;

import fr.ensma.a3.ia.be.blockchain.database.api.transactions.MyTransaction;

public class MyMessageSupprTransaction extends MyTransaction{
    
    private String idConv;
    private String idMsg;

    public MyMessageSupprTransaction(String type, int n_date, String client, String conv, String msg) {
        super(type, n_date, client);
        if(conv == null || msg == null){
            throw new IllegalArgumentException();
        }
        idConv = conv;
        idMsg = msg;
    }

    public String getConvId(){
        return idConv;
    }

    public String getMsgId(){
        return idMsg;
    }

    @Override
    public String toStore() {
        return "{ " + super.toString() + ", \"IdConv\" :\" " + idConv + "\", \"IdMsg\" : \"" + idMsg + "\"}";
    }

    
}
