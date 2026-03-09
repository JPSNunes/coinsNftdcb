/**
 * BFT Map implementation (interactive client).
 *
 */
package dcb.dti;

import java.io.Console;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DTIClient {

    public static void main(String[] args) throws IOException {
        int clientId = (args.length > 0) ? Integer.parseInt(args[0]) : 1001;
        DTIStub dtiStub = new DTIStub(clientId);
        Console console = System.console();
        Scanner cmdScanner = new Scanner(console.readLine("\n  > "));

        System.out.println("\nCommands:\n");
        printCommands();

        while (true) {
            String cmd = "";

            if (cmdScanner.hasNext()) {
                cmd = cmdScanner.next();
            }

            switch (cmd) {
                case "MINT": {
                    long value = cmdScanner.nextLong();
                    if (!validateArgs(value, null, null, null)) {
                        break;
                    }
                    long newCoinId = dtiStub.mint(value);
                    System.out.println("New coin minted with ID: " + newCoinId);
                    break;
                }
                case "MY_COINS": {
                    List<Coin> coinList = dtiStub.my_coins();
                    if (coinList.isEmpty()) {
                        System.out.println("You do not have any coins.");
                    } else {
                        for (Coin c : dtiStub.my_coins()) {
                            System.out.println(c);
                        }
                    }
                    break;
                }
                case "SPEND": {
                    String longArray = cmdScanner.next();
                    int receiver = cmdScanner.nextInt();
                    long value = cmdScanner.nextLong();
                    if (!validateArgs(value, null, receiver, longArray)) {
                        break;
                    }
                    long[] coinIds = parseLongArray(longArray);
                    long code = dtiStub.spend(coinIds, receiver, value);
                    if (code == -1) {
                        System.out.println("Operation failed.");
                    } else if (code == 0) {
                        System.out.println("Operation successful. No value remaining to generate a new coin.");
                    } else {
                        System.out.println("Operation successful. Leftover value with coin " + code);
                    }
                }
                case "MY_NFTS": {
                    List<NFT> nfts = dtiStub.my_nfts();
                    if (nfts.isEmpty()) {
                        System.out.println("You do not have any NFTs.");
                    } else {
                        for (NFT n : nfts) {
                            System.out.println(n);
                        }
                    }
                    break;
                }
                case "MINT_NFT": {
                    String name = cmdScanner.next();
                    String uri = cmdScanner.next();
                    long value = cmdScanner.nextLong();
                    if (!validateArgs(value, null, null, null)) {
                        break;
                    }
                    long code = dtiStub.mint_nft(name, uri, value);
                    if (code == -1) {
                        System.out.println("Operation failed.");
                    } else {
                        System.out.println("NFT created with ID " + code);
                    }
                    break;
                }
                case "SET_NFT_PRICE": {
                    long nftId = cmdScanner.nextLong();
                    long value = cmdScanner.nextLong();
                    if (!validateArgs(nftId, value, null, null)) {
                        break;
                    }
                    long code = dtiStub.set_nft_price(nftId, value);
                    if (code == -1) {
                        System.out.println("Operation failed.");
                    } else {
                        System.out.println("NFT with ID " + code + "set to value " + value);
                    }
                    break;
                }
                case "SEARCH_NFT": {
                    String name = cmdScanner.next();
                    List<NFT> matches = dtiStub.search_nft(name);
                    if (matches.isEmpty()) {
                        System.out.println("No matches for the name given.");
                    } else {
                        for (NFT n : matches) {
                            System.out.println(n);
                        }
                    }
                    break;
                }
                case "BUY_NFT": {
                    long nft = cmdScanner.nextLong();
                    String longArray = cmdScanner.next();
                    if (!validateArgs(nft, null, null, longArray)) {
                        break;
                    }
                    long[] coinIds = parseLongArray(longArray);
                    long code = dtiStub.buy_nft(nft, coinIds);
                    if (code == -1) {
                        System.out.println("Operation failed.");
                    } else if (code == 0) {
                        System.out.println("Operation successful. No value remaining to generate a new coin.");
                    } else {
                        System.out.println("Operation successful. Leftover value with coin " + code);
                    }
                    break;
                }
                case "EXIT": {
                    System.out.println("\tEXIT: Bye bye!\n");
                    cmdScanner.close();
                    System.exit(0);
                }
                default:
                    printCommands();
                    break;
            }
        }
    }

    private static long[] parseLongArray(String s) {
        String[] split = s.split(",");
        long[] longArray = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            longArray[i] = Long.parseLong(split[i]);
        }
        return longArray;
    }

    private static boolean validateArgs(Long valueOne, Long valueTwo, Integer intValue, String longArray) {
        if (!validateFields(valueOne, valueTwo, intValue, longArray)) {
            printCommands();
            return false;
        }
        return true;
    }

    private static boolean validateFields(Long valueOne, Long valueTwo, Integer intValue, String longArray) {
        if (valueOne != null && valueOne < 0)
            return false;
        if (valueTwo != null && valueTwo < 0)
            return false;
        if (longArray != null && !longArray.matches("\\d+(,\\d+)*"))
            return false;
        if (intValue != null && intValue < 0)
            return false;

        return true;
    }

    private static void printCommands() {
        System.out.println(
            "Invalid command. Commands are as follows:\n" +
            "\tMY_COINS: Display your owned coins.\n" +
            "\tMINT <Value>: Mint new coins with the specified value.\n" +
            "\tSPEND <CoinIDs> <Receiver> <Value>: Transfer the specified value to the given recipient, from a list of coins.\n" +
            "\tMY_NFTS: Display your owned NFTs.\n" +
            "\tMINT_NFT: <Name> <URI> <Value>: Mint a given NFT.\n" +
            "\tSET_NFT_PRICE: <NFT> <Value>: Set a new price for a given NFT.\n" +
            "\tSEARCH_NFT: <Name>: Search for NFTs containing the keyword given.\n" +
            "\tBUY_NFT <NFTID> <CoinIDs>: Buy an NFT with a specified set of coins.\n" +
            "\tEXIT: Terminate this client.\n" +
            "Coin IDs should be entered in the following format:\n" +
            "<CoinID1>,<CoinID2>,<CoinID3>,<CoinID4>, (...)"
        );
    }

    
}
