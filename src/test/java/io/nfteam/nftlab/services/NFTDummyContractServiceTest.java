package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStoreEthereum;
import io.nfteam.nftlab.services.ipfs.IPFSService;
import io.nfteam.nftlab.services.ipfs.pinataresponses.Uploaded;
import io.nfteam.nftlab.services.smartcontract.NFTID;
import io.nfteam.nftlab.services.smartcontract.UserTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NFTDummyContractServiceTest {
  @Mock
  private IPFSService ipfsService;
  @Mock
  private Uploaded uploaded;
  @Mock
  private ByteArrayResource file;

  @Test
  public void mint_ValidNFT_HashAndNegativeIdOfMintedNFT() throws Exception {
    UserTuple artist = new UserTuple("0x51b1D3246fc4D665A75F77599c82419ab11dEAc4", BigInteger.valueOf(1));
    String expectedHash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    BigInteger expectedTokenId = BigInteger.valueOf(-1);

    when(uploaded.getHash()).thenReturn(expectedHash);
    when(ipfsService.upload(file)).thenReturn(uploaded);

    NFTContractService service = new NFTDummyContractService(ipfsService);

    NFTID mintedNFT = service.mint(artist, file);

    assertEquals(expectedHash, mintedNFT.hash());
    assertEquals(expectedTokenId, mintedNFT.tokenId());
  }

  @Test
  public void transfer() throws Exception {
    NFTContractService service = new NFTDummyContractService(ipfsService);

    service.transfer(null, null, null, null, null);

    assertTrue(true);
  }

  @Test
  public void getHistory() throws Exception {
    NFTContractService service = new NFTDummyContractService(ipfsService);

    List actualHistory = service.getHistory(null);

    assertNull(actualHistory);
  }

  @Test
  public void getNFTById() throws Exception {
    NFTContractService service = new NFTDummyContractService(ipfsService);

    NFTLabStoreEthereum.NFTLab actualNFT = (NFTLabStoreEthereum.NFTLab) service.getNFTById(null);

    assertNull(actualNFT);
  }

  @Test
  public void getNFTByHash() throws Exception {
    NFTContractService service = new NFTDummyContractService(ipfsService);

    NFTLabStoreEthereum.NFTLab actualNFT = (NFTLabStoreEthereum.NFTLab) service.getNFTByHash(null);

    assertNull(actualNFT);
  }
}
