package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStoreHotmoka;
import io.nfteam.nftlab.services.ipfs.IPFSService;
import io.nfteam.nftlab.services.ipfs.pinataresponses.Uploaded;
import io.nfteam.nftlab.services.smartcontract.INFTTransaction;
import io.nfteam.nftlab.services.smartcontract.NFTID;
import io.nfteam.nftlab.services.smartcontract.UserTuple;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigInteger;
import java.util.List;

public class NFTDummyContractService implements NFTContractService {
  private final IPFSService ipfsService;

  public NFTDummyContractService(IPFSService ipfsService)
  {
    this.ipfsService = ipfsService;
  }

  @Override
  public NFTID mint(UserTuple artist, ByteArrayResource file) throws Exception {
    Uploaded uploadedImage = ipfsService.upload(file);
    String hash = uploadedImage.getHash();

    return new NFTID(hash, BigInteger.valueOf(-1));
  }

  @Override
  public void transfer(BigInteger tokenId, UserTuple seller, UserTuple buyer, String price, String timestamp)
    throws Exception
  { }

  @Override
  public List<INFTTransaction> getHistory(BigInteger tokenId) throws Exception {
    return null;
  }

  @Override
  public NFTLabStoreHotmoka.NFTLab getNFTById(BigInteger tokenId) throws Exception {
    return null;
  }

  @Override
  public NFTLabStoreHotmoka.NFTLab getNFTByHash(String hash) throws Exception {
    return null;
  }
}
