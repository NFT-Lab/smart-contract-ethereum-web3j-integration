package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStore;
import io.nfteam.nftlab.services.ipfs.IPFSResponses;
import io.nfteam.nftlab.services.ipfs.IPFSService;
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
    IPFSResponses.UploadImage uploadedImage = ipfsService.uploadImage(file);
    String hash = uploadedImage.getHash();

    return new NFTID(hash, BigInteger.valueOf(-1));
  }

  @Override
  public void transfer(BigInteger tokenId, UserTuple seller, UserTuple buyer, String price, String timestamp)
    throws Exception
  { }

  @Override
  public List getHistory(BigInteger tokenId) throws Exception {
    return null;
  }

  @Override
  public NFTLabStore.NFTLab getNFTById(BigInteger tokenId) throws Exception {
    return null;
  }

  @Override
  public NFTLabStore.NFTLab getNFTByHash(String hash) throws Exception {
    return null;
  }
}