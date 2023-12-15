package fr.ensma.a3.ia.be.blockchain.database.api.transactions.readJsonrpcMessage;

import java.util.List;

public class MyJsonrpcTxResult {
    public int code;
    public String data;
    public String log;
    public String info;
    public String gas_wanted;
    public String gas_used;
    public List<MyJsonrpcEvents> events;

    public String codespace;
}
