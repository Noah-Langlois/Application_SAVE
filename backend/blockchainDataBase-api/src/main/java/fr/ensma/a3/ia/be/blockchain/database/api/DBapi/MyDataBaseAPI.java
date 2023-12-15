package fr.ensma.a3.ia.be.blockchain.database.api.DBapi;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.ensma.a3.ia.be.blockchain.database.api.transactions.ITransaction;
import fr.ensma.a3.ia.be.blockchain.database.api.transactions.transactionsTypes.MyConvTransaction;
import fr.ensma.a3.ia.be.blockchain.database.api.transactions.transactionsTypes.MyMessageTransaction;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

public class MyDataBaseAPI {

    Client client = ClientBuilder.newClient();
    ObjectMapper mapper = new ObjectMapper();

    // TODO to Implement

    public void writeNewTransaction(ITransaction transaction) {

        privateWriteNewTransaction(transaction, 5);

    }

    private void privateWriteNewTransaction(ITransaction transaction, int nbInterationsRestante) {
        if (nbInterationsRestante == 0) {
            return;
        }

        try {
            Response dep = client.target(getBaseURI())
                    .path("broadcast_tx_commit")
                    .queryParam("tx", URLEncoder.encode(transaction.toStore(), "UTF-8"))
                    .request(MediaType.TEXT_PLAIN)
                    .get();
            if (dep.getStatus() != 200) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            privateWriteNewTransaction(transaction, nbInterationsRestante - 1);
        }

    }

    // public List<MyMessageTransaction> getXDernierMessage(String id_client, String
    // id_conv, int x){}

    // public String getHashPassword (String id_client){}

    // public int getNbTrollAlerts(String id_client){}
    
    // // que pour le lanceurs d'alerte
    // public List<MyConvTransaction> getConvTransactionsLA(String id_client){}

    // // que pour le client ref
    // public List<MyConvTransaction> getConvTransactionsRef(){}


    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/").port(26657).build();
    }

}
