package io.nfteam.nftlab.services.smartcontract;

import java.math.BigInteger;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import io.nfteam.nftlab.contracts.NFTLabStore;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

public class NFTLabETHContractService
{
    private final Web3j web3j;
    private final Credentials credentials;

    private final NFTLabStore contract;

    private final String contractAddress;

    private final static BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final static BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    private final ContractGasProvider contractGasProvider = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);

    public NFTLabETHContractService() {
        Dotenv dotenv = Dotenv.load();
        final HttpService service = new HttpService(dotenv.get("ETHEREUM_URL"));
        this.web3j = Web3j.build(service);

        this.contractAddress = dotenv.get("ETHEREUM_CONTRACT_ADDRESS");

        String privateKey = dotenv.get("ETHEREUM_PRIVATE_KEY");
        this.credentials = Credentials.create(privateKey);

        this.contract = loadContract();
    }

    public void mint(NFTLabStore.NFTLab nft) throws Exception {
        this.contract.mint(nft).send();
    }

    public List getHistory(BigInteger tokenId) throws Exception {
      return this.contract.getHistory(tokenId).send();
    }

    public BigInteger getTokenId(String hash) throws Exception {
      return this.contract.getTokenId(hash).send();
    }

    public void transfer(NFTLabStore.NFTTransaction transaction) throws Exception {
      this.contract.transfer(transaction).send();
    }

    public NFTLabStore.NFTLab getNFTById(BigInteger id) throws Exception {
      return this.contract.getNFTById(id).send();
    }

    public NFTLabStore.NFTLab getNFTByHash(String hash) throws Exception {
      return this.contract.getNFTByHash(hash).send();
    }

    public static String deploy(String name, String symbol) throws Exception {
        Dotenv dotenv = Dotenv.load();
        final HttpService service = new HttpService(dotenv.get("ETHEREUM_URL"));
        final Web3j web3j = Web3j.build(service);
        final String privateKey = dotenv.get("ETHEREUM_PRIVATE_KEY");
        final Credentials credentials = Credentials.create(privateKey);
        final ContractGasProvider contractGasProvider = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);
        
        return NFTLabStore.deploy(
            web3j,
            credentials,
            contractGasProvider,
            name,
            symbol
        ).send().getContractAddress();
    }

    private NFTLabStore loadContract(){

      return NFTLabStore.load(
            contractAddress,
            web3j,
            credentials,
            contractGasProvider
        );
    }
}
