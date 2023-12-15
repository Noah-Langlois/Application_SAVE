package fr.ensma.a3.ia.be.blockchain.database.api.transactions.transactionsTypes;

import fr.ensma.a3.ia.be.blockchain.database.api.transactions.MyTransaction;

public class MyMessageTransaction extends MyTransaction {

    private String idConv;
    private String idMsg;
    private String contenu;

    public MyMessageTransaction(String type, int n_date, String client, String msgId, String cont, String conv) {
        super(type, n_date, client);
        if (conv == null || msgId == null || cont == null) {
            throw new IllegalArgumentException();
        }

        idConv = conv;
        idMsg = msgId;
        contenu = cont;
    }

    public String getConvId() {
        return idConv;
    }

    public String getMsgId() {
        return idMsg;
    }

    public String getContenu() {
        return contenu;
    }

    @Override
    public String toStore() {
        return "\"{" + super.toString() +
                ",\\\"IdConv\\\":\\\"" + idConv +
                "\\\",\\\"IdMsg\\\":\\\"" + idMsg +
                "\\\",\\\"Cont\\\":\\\"" + contenu +
                "\\\"}\"";
    }

}
