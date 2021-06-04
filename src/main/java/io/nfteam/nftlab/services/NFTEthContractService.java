package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStore;
import io.nfteam.nftlab.services.ipfs.IPFSResponses;
import io.nfteam.nftlab.services.ipfs.IPFSService;
import io.nfteam.nftlab.services.smartcontract.*;
import org.springframework.core.io.FileSystemResource;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.List;

//@Component("NFTEthContractService")
public class NFTEthContractService implements NFTContractService {
  private final IPFSService ipfsService;
  private final NFTLabStore nftLabStore;

  public NFTEthContractService(NFTLabStore contractService, IPFSService ipfsService)
  {
    this.nftLabStore = contractService;
    this.ipfsService = ipfsService;
  }

  public NFTID mint(UserTuple artist, FileSystemResource file) throws Exception
  {
    IPFSResponses.UploadImage uploadedImage = ipfsService.uploadImage(file);
    String hash = uploadedImage.getHash();
    String timestamp = uploadedImage.getTimestamp();

    NFTLabStore.NFTLab newNft = new NFTLabStore.NFTLab(artist.wallet(), artist.id(), hash, timestamp);

    nftLabStore.mint(newNft).send();
    BigInteger tokenId = nftLabStore.getTokenId(newNft.hash).send();

    return new NFTID(hash, tokenId);
  }

  public void transfer(
          BigInteger tokenId,
          UserTuple seller,
          UserTuple buyer,
          String price,
          String timestamp) throws Exception
  {
    NFTLabStore.NFTTransaction transaction = new NFTLabStore.NFTTransaction(tokenId, seller.wallet(), seller.id(), buyer.wallet(), buyer.id(), price, timestamp);

    nftLabStore.transfer(transaction).send();
  }

  public List<NFTLabStore.NFTTransaction> getHistory(BigInteger tokenId) throws Exception {
    return nftLabStore.getHistory(tokenId).send();
  }

  public NFTLabStore.NFTLab getNFTById(BigInteger tokenId) throws Exception {
    return this.nftLabStore.getNFTById(tokenId).send();
  }

  public NFTLabStore.NFTLab getNFTByHash(String hash) throws Exception {
    return this.nftLabStore.getNFTByHash(hash).send();
  }

  public static NFTLabStore deploy(
    Web3j web3j,
    Credentials credentials,
    ContractGasProvider contractGasProvider,
    String name,
    String symbol) throws Exception {
    return NFTLabStore.deploy(
      web3j,
      credentials,
      contractGasProvider,
      name,
      symbol
    ).send();
  }

  public static NFTLabStore loadContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider){
    return NFTLabStore.load(
      contractAddress,
      web3j,
      credentials,
      contractGasProvider
    );
  }
}
