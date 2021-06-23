package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStoreHotmoka;
import io.nfteam.nftlab.services.ipfs.IPFSResponses;
import io.nfteam.nftlab.services.ipfs.IPFSService;
import io.nfteam.nftlab.services.smartcontract.NFTID;
import io.nfteam.nftlab.services.smartcontract.UserTuple;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;

public class NFTHotmokaContractService implements NFTContractService {
  private final IPFSService ipfsService;
  private final NFTLabStoreHotmoka contractService;

  public NFTHotmokaContractService(NFTLabStoreHotmoka contractService, IPFSService ipfsService) {
    this.contractService = contractService;
    this.ipfsService = ipfsService;
  }

  @Override
  public NFTID mint(UserTuple artist, ByteArrayResource file) throws Exception {
    IPFSResponses.UploadImage uploadedImage = ipfsService.uploadImage(file);

    String hash = uploadedImage.getHash();
    String timestamp = uploadedImage.getTimestamp();

    BigInteger tokenId = contractService.mint(
      artist.wallet(),
      artist.id(),
      hash,
      timestamp
    );

    return new NFTID(hash, tokenId);
  }

  @Override
  public void transfer(BigInteger tokenId, UserTuple seller, UserTuple buyer, String price, String timestamp) throws Exception {
    contractService.transfer(
      tokenId,
      seller.wallet(),
      seller.id(),
      buyer.wallet(),
      buyer.id(),
      price,
      timestamp
    );
  }

  @Override
  public List getHistory(BigInteger tokenId) throws Exception {
    return contractService.getHistory(tokenId);
  }

  @Override
  public NFTLabStoreHotmoka.NFTLab getNFTById(BigInteger tokenId) throws Exception {
    return contractService.getNFTById(tokenId);
  }

  @Override
  public NFTLabStoreHotmoka.NFTLab getNFTByHash(String hash) throws Exception {
    return contractService.getNFTByHash(hash);
  }

  public static NFTLabStoreHotmoka load(
    String url,
    String accountAddress,
    BigInteger defaultGasLimit,
    BigInteger defaultGasPrice,
    String classpath,
    String nftLabStoreAddress
  ) throws Exception {
    NFTLabStoreHotmoka contractService = NFTLabStoreHotmoka.load(
      url,
      accountAddress,
      defaultGasLimit,
      defaultGasPrice,
      classpath,
      nftLabStoreAddress
    );

    return contractService;
  }

  public static NFTLabStoreHotmoka deploy(
    String url,
    String accountAddress,
    BigInteger defaultGasLimit,
    BigInteger defaultGasPrice,
    String smartContractHotmokaPath,
    String erc721HotmokaPath,
    String name,
    String symbol
  ) throws Exception {
    NFTLabStoreHotmoka contractService = NFTLabStoreHotmoka.deploy(
      url,
      accountAddress,
      defaultGasLimit,
      defaultGasPrice,
      Paths.get(smartContractHotmokaPath),
      Paths.get(erc721HotmokaPath),
      name,
      symbol
    );

    return contractService;
  }
}
