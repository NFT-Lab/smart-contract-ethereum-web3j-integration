package io.nfteam.nftlab.services;

import io.hotmoka.beans.values.StorageReference;
import io.nfteam.nftlab.contracts.NFTLabStoreEthereum;
import io.nfteam.nftlab.contracts.NFTLabStoreHotmoka;
import io.nfteam.nftlab.services.ipfs.IPFSService;
import io.nfteam.nftlab.services.ipfs.pinataresponses.Uploaded;
import io.nfteam.nftlab.services.smartcontract.NFTID;
import io.nfteam.nftlab.services.smartcontract.UserTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NFTHotmokaContractServiceTest {
  @Mock
  private NFTLabStoreHotmoka contractService;
  @Mock
  private IPFSService ipfsService;
  @Mock
  private Uploaded uploaded;
  @Mock
  private ByteArrayResource file;

  @Mock
  private RemoteFunctionCall<BigInteger> bigIntegerRemoteFunctionCall;

  @Mock
  private RemoteFunctionCall<TransactionReceipt> transactionReceiptRemoteFunctionCall;

  @Mock
  private RemoteFunctionCall<List> listRemoteFunctionCall;

  @Mock
  private RemoteFunctionCall<NFTLabStoreEthereum.NFTLab> nftLabRemoteFunctionCall;

  @Test
  public void mint_ValidNFT_HashAndIdOfMintedNFT() throws Exception {
    UserTuple artist = new UserTuple("0x51b1D3246fc4D665A75F77599c82419ab11dEAc4", BigInteger.valueOf(1));
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    String timestamp = "2019";
    BigInteger tokenId = BigInteger.valueOf(1);

    when(uploaded.getHash()).thenReturn(hash);
    when(uploaded.getTimestamp()).thenReturn(timestamp);
    when(ipfsService.upload(file)).thenReturn(uploaded);

    when(contractService.mint(any(String.class), any(BigInteger.class), any(String.class), any(String.class)))
      .thenReturn(tokenId);

    NFTHotmokaContractService service = new NFTHotmokaContractService(contractService, ipfsService);

    NFTID mintedNFT = service.mint(artist, file);

    assertEquals(hash, mintedNFT.hash());
    assertEquals(tokenId, mintedNFT.tokenId());
  }

  @Test
  public void transfer_ValidTransfer_Nothing() throws Exception {
    BigInteger tokenId = BigInteger.valueOf(1);
    UserTuple seller = new UserTuple("0x51b1D3246fc4D665A75F77599c82419ab11dEAc4", BigInteger.valueOf(1));
    UserTuple buyer = new UserTuple("0x8f64E2Cb041D23fC961F300fdB092A59EF38dedc", BigInteger.valueOf(1));
    String price = "1 ETH";
    String timestamp = "2021";

    when(contractService.transfer(
      any(BigInteger.class),
      any(String.class),
      any(BigInteger.class),
      any(String.class),
      any(BigInteger.class),
      any(String.class),
      any(String.class))
    ).thenReturn(true);

    NFTHotmokaContractService service = new NFTHotmokaContractService(contractService, ipfsService);

    service.transfer(tokenId, seller, buyer, price, timestamp);
  }

  @Test
  public void getHistory() throws Exception {
    BigInteger tokenId = BigInteger.valueOf(1);
    List<NFTLabStoreHotmoka.NFTTransaction> expectedHistory = new ArrayList<>();

    when(contractService.getHistory(tokenId)).thenReturn(expectedHistory);

    NFTHotmokaContractService service = new NFTHotmokaContractService(contractService, ipfsService);

    List<NFTLabStoreHotmoka.NFTTransaction> actualHistory = service.getHistory(tokenId);

    assertTrue(
      expectedHistory.size() == actualHistory.size() &&
        expectedHistory.containsAll(actualHistory) &&
        expectedHistory.containsAll(actualHistory));
  }

  @Test
  public void getNFTById() throws Exception {
    BigInteger tokenId = BigInteger.valueOf(1);
    UserTuple artist = new UserTuple("4107e6d3eafbedcce7b0e3606adc59daa8cb690f0bbffe83ee9cd061aaf6c6c9#0", BigInteger.valueOf(1));
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    String timestamp = "2021";
    StorageReference expectedArtist = new StorageReference(artist.wallet());

    NFTHotmokaContractService service = new NFTHotmokaContractService(contractService, ipfsService);

    NFTLabStoreHotmoka.NFTLab expectedNFT = contractService.new NFTLab(
      expectedArtist, artist.id(), hash, timestamp
    );

    when(contractService.getNFTById(tokenId)).thenReturn(expectedNFT);

    NFTLabStoreHotmoka.NFTLab actualNFT = service.getNFTById(tokenId);

    assertEquals(expectedArtist, actualNFT.getArtist());
    assertEquals(artist.id(), actualNFT.getArtistId());
    assertEquals(hash, actualNFT.getHash());
    assertEquals(timestamp, actualNFT.getTimestamp());
  }

  @Test
  public void getNFTByHash() throws Exception {
    UserTuple artist = new UserTuple("4107e6d3eafbedcce7b0e3606adc59daa8cb690f0bbffe83ee9cd061aaf6c6c9#0", BigInteger.valueOf(1));
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    String timestamp = "2021";
    StorageReference expectedArtist = new StorageReference(artist.wallet());

    NFTLabStoreHotmoka.NFTLab expectedNFT = contractService.new NFTLab(
      expectedArtist, artist.id(), hash, timestamp
    );

    when(contractService.getNFTByHash(hash)).thenReturn(expectedNFT);

    NFTHotmokaContractService service = new NFTHotmokaContractService(contractService, ipfsService);
    NFTLabStoreHotmoka.NFTLab actualNFT = service.getNFTByHash(hash);

    assertEquals(expectedArtist, actualNFT.getArtist());
    assertEquals(artist.id(), actualNFT.getArtistId());
    assertEquals(hash, actualNFT.getHash());
    assertEquals(timestamp, actualNFT.getTimestamp());
  }
}
