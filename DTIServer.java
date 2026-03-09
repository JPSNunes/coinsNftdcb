/**
 * BFT Map implementation (server side).
 *
 */
package dcb.dti;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;

import java.io.*;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;

public class DTIServer extends DefaultSingleRecoverable {
    private TreeMap<Long, Coin> storedCoins;
    private TreeMap<Long, NFT> storedNFTs;
    //IDs. sempre que se criar uma nova vamos ter de fazer ++ em cada.
    private long nextCoinId = 1;
    private long nextNftId = 1;

    //The constructor passes the id of the server to the super class
    public DTIServer(int id) {

        storedCoins = new TreeMap<>();
        storedNFTs = new TreeMap<>();

        //turn-on BFT-SMaRt'replica
        new ServiceReplica(id, this, this);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java DTIServer <server id>");
            System.exit(-1);
        }
        new DTIServer(Integer.parseInt(args[0]));
    }

    @Override
    public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {
        //all operations must be defined here to be invoked by BFT-SMaRt
        try {
            GenericMessage request = GenericMessage.fromBytes(command);
            GenericMessage.Type cmd = request.getType();
            GenericMessage response = new GenericMessage(cmd);

            int senderId = msgCtx.getSender();

            System.out.println("Ordered execution of a "+cmd+" request from "+senderId);

            switch (cmd) {
                case MINT:
                    //cliente 4
                    if(senderId == 4){
                        Coin nCoin = new Coin(nextCoinId,senderId,request.getValue());
                        storedCoins.put(nextCoinId, nCoin);
                        response.setTokenId(nextCoinId);
                        nextCoinId++;
                    }
                    else{
                        response.setTokenId(-1);
                    }
                    break;

                case SPEND:
                    long[] coinIds = request.getCoinIds();
                    long total = 0;
                    boolean valid = true;
                    
                    //validar as coins
                    for (long id : coinIds) {
                        Coin c = storedCoins.get(id);
                        if (c == null || c.getOwner() != senderId) {
                            valid = false;
                            break;
                        }
                        total += c.getValue();
                    }
                    //se as coins estiverem invalidas return -1
                    if (!valid || total < request.getValue()) {
                        response.setTokenId(-1);
                        break;
                    }
                    //remover as coins usadas
                    for (long id : coinIds) storedCoins.remove(id);

                    storedCoins.put(nextCoinId, new Coin(nextCoinId, request.getReceiver(), request.getValue()));
                    nextCoinId++;

                    long change = total - request.getValue();
                    if (change > 0) {
                        storedCoins.put(nextCoinId, new Coin(nextCoinId, senderId, change));
                        response.setTokenId(nextCoinId);
                        nextCoinId++;
                    } else {
                        response.setTokenId(0);
                    }
                    break;

                case MINT_NFT:
                    //ver se ja existe alguma nft com esse nome
                    boolean nameExists = false;
                    for (NFT nft : storedNFTs.values()) {
                        if (nft.getName().equalsIgnoreCase(request.getName())) {
                            nameExists = true;
                            break;
                        }
                    }

                    if (nameExists) {
                        response.setTokenId(-1);
                        break;
                    }
                    //criar nft
                    NFT newNFT = new NFT(nextNftId, senderId, request.getName(), request.getUri(), request.getValue());
                    storedNFTs.put(nextNftId, newNFT);
                    response.setTokenId(nextNftId);
                    nextNftId++;
                    break;

                case SET_NFT_PRICE:
                    NFT nftToPrice = storedNFTs.get(request.getNftId());

                    if (nftToPrice == null || nftToPrice.getOwner() != senderId) {
                        response.setTokenId(-1);
                        break;
                    }

                    nftToPrice.setValue(request.getValue());
                    response.setTokenId(nftToPrice.getId());
                    break;

                case BUY_NFT:
                    NFT nftToBuy = storedNFTs.get(request.getNftId());
                    //verificar se existe a nft
                    if (nftToBuy == null) {
                        response.setTokenId(-1);
                        break;
                    }
                    //verificar se existem as coins e se sao do sender, mais ver se é suficiente para comprar
                    long[] buyCoins = request.getCoinIds();
                    long buyTotal = 0;
                    boolean buyValid = true;

                    for (long id : buyCoins) {
                        Coin c = storedCoins.get(id);
                        if (c == null || c.getOwner() != senderId) {
                            buyValid = false;
                            break;
                        }
                        buyTotal += c.getValue();
                    }

                    if (!buyValid || buyTotal < nftToBuy.getValue()) {
                        response.setTokenId(-1);
                        break;
                    }

                    int sellerId = nftToBuy.getOwner();
                    //remover as coins usadas
                    for (long id : buyCoins){
                        storedCoins.remove(id);
                    } 

                    storedCoins.put(nextCoinId, new Coin(nextCoinId, sellerId, nftToBuy.getValue()));
                    nextCoinId++;

                    long buyChange = buyTotal - nftToBuy.getValue();
                    if (buyChange > 0) {
                        storedCoins.put(nextCoinId, new Coin(nextCoinId, senderId, buyChange));
                        response.setTokenId(nextCoinId);
                        nextCoinId++;
                    } else {
                        response.setTokenId(0);
                    }
                    //transferir nft para o comrpador
                    nftToBuy.setOwner(senderId);
                    break;
                
                default:
                    break;
            }

            return GenericMessage.toBytes(response);
        }catch (IOException | ClassNotFoundException ex) {
            System.err.println("Failed to process ordered request "+ex);
            return new byte[0];
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        //read-only operations can be defined here to be invoked by BFT-SMaRt
        try {
            GenericMessage request = GenericMessage.fromBytes(command);
            GenericMessage.Type cmd = request.getType();
            GenericMessage response = new GenericMessage(cmd);

            int senderId = msgCtx.getSender();

            System.out.println("Unordered execution of a "+cmd+" request from "+senderId);

            switch (cmd) {
                case MY_COINS:
                    //filtrar as coins que pertencem ao sender 
                    List<Coin> myCoins = new ArrayList<>();
                    for (Coin c : storedCoins.values()) {
                        if (c.getOwner() == senderId) {
                            myCoins.add(c);
                        }
                    }
                    response.setCoins(myCoins);
                    break;

                case MY_NFTS:
                    //filtras as nfts que pertencem ao sender
                    List<NFT> myNfts = new ArrayList<>();
                    for (NFT nft : storedNFTs.values()) {
                        if (nft.getOwner() == senderId) {
                            myNfts.add(nft);
                        }
                    }
                    response.setNfts(myNfts);
                    break;

                case SEARCH_NFT:
                    //devolver a lista de nfts que contenham o texto no seu nome
                    List<NFT> foundNfts = new ArrayList<>();
                    String searchText = request.getName().toLowerCase();
                    for (NFT nft : storedNFTs.values()) {
                        if (nft.getName().toLowerCase().contains(searchText)) {
                            foundNfts.add(nft);
                        }
                    }
                    response.setNfts(foundNfts);
                    break;

                default:
                    break;
            }

            return GenericMessage.toBytes(response);
        }catch (IOException | ClassNotFoundException ex) {
            System.err.println("Failed to process unordered request "+ex);
            return new byte[0];
        }
    }

    @Override
    public byte[] getSnapshot() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(storedCoins);
            out.writeObject(storedNFTs);
            out.writeLong(nextCoinId);
            out.writeLong(nextNftId);
            out.flush();
            bos.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(); //debug instruction
            return new byte[0];
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void installSnapshot(byte[] state) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(state);
             ObjectInput in = new ObjectInputStream(bis)) {
            storedCoins = (TreeMap<Long, Coin>) in.readObject();
            storedNFTs = (TreeMap<Long, NFT>) in.readObject();
            nextCoinId = in.readLong();
            nextNftId = in.readLong();
        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace(); //debug instruction
        }
    }

}
