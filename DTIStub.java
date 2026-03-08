/**
 * BFT Map implementation (client side).
 *
 */
package dcb.dti;

import java.io.IOException;
import java.util.List;

import bftsmart.tom.ServiceProxy;

public class DTIStub {
    private final ServiceProxy serviceProxy;

    public DTIStub(int id) {
        serviceProxy = new ServiceProxy(id);
    }

    public long mint(long value) {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.MINT);
            request.setValue(value);

            //invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send MINT request");
            return -1;
        }

        if (rep.length == 0) {
            return -1;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getTokenId();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of MINT request "+ex);
            return -1;
        }
    }

    public List<Coin> my_coins() {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.MY_COINS);
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send MY_COINS request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getCoins();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of MY_COINS request "+ex);
            return null;
        }
    }

    public long spend(long[] coins, int receiver, long value) {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.SPEND);
            request.setCoinIds(coins);
            request.setReceiver(receiver);
            request.setValue(value);
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send SPEND request");
            return -1;
        }

        if (rep.length == 0) {
            return -1;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getTokenId();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of SPEND request "+ex);
            return -1;
        }
    }

    public List<NFT> my_nfts() {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.MY_NFTS);
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send MY_NFTS request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getNfts();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of MY_NFTS request "+ex);
            return null;
        }
    }

    public long mint_nft(String name, String URI, long value) {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.MINT_NFT);
            request.setName(name);
            request.setUri(URI);
            request.setValue(value);
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send MINT_NFT request");
            return -1;
        }

        if (rep.length == 0) {
            return -1;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getTokenId();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of MINT_NFT request "+ex);
            return -1;
        }
    }

    public long set_nft_price(long nft, long value) {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.SET_NFT_PRICE);
            request.setNftId(nft);
            request.setValue(value);
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send SET_NFT_PRICE request");
            return -1;
        }

        if (rep.length == 0) {
            return -1;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getTokenId();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of SET_NFT_PRICE request "+ex);
            return -1;
        }
    }

    public List<NFT> search_nft(String text) {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.SEARCH_NFT);
            request.setName(text);
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send SEARCH_NFT request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getNfts();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of SEARCH_NFT request "+ex);
            return null;
        }
    }

    public long buy_nft(long nft, long[] coins) {
        byte[] rep;
        try {
            GenericMessage request = new GenericMessage(GenericMessage.Type.BUY_NFT);
            request.setNftId(nft);
            request.setCoinIds(coins);
            rep = serviceProxy.invokeOrdered(GenericMessage.toBytes(request));
        } catch (IOException e) {
            System.err.println("Failed to send BUY_NFT request");
            return -1;
        }

        if (rep.length == 0) {
            return -1;
        }
        try {
            GenericMessage response = GenericMessage.fromBytes(rep);
            return response.getTokenId();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println("Failed to deserialized response of BUY_NFT request "+ex);
            return -1;
        }
    }
}
