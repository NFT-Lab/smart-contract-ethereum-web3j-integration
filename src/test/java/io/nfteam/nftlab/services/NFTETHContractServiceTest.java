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
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NFTETHContractServiceTest {
  @Mock
  private NFTLabStoreEthereum contractService;
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
    BigInteger tokenId = BigInteger.valueOf(1);

    when(uploaded.getHash()).thenReturn(hash);
    when(ipfsService.upload(file)).thenReturn(uploaded);

    doReturn(when(bigIntegerRemoteFunctionCall.send()).thenReturn(tokenId).getMock())
      .when(contractService)
      .getTokenId(hash);

    when(contractService.mint(any(NFTLabStoreEthereum.NFTLab.class))).thenReturn(transactionReceiptRemoteFunctionCall);

    NFTETHContractService service = new NFTETHContractService(contractService, ipfsService);

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

    when(contractService.transfer(any(NFTLabStoreEthereum.NFTTransaction.class))).thenReturn(transactionReceiptRemoteFunctionCall);

    NFTETHContractService service = new NFTETHContractService(contractService, ipfsService);

    service.transfer(tokenId, seller, buyer, price, timestamp);
  }

  @Test
  public void getHistory() throws Exception {
    BigInteger tokenId = BigInteger.valueOf(1);
    List<NFTLabStoreEthereum.NFTTransaction> expectedHistory = new ArrayList<>();

    doReturn(when(listRemoteFunctionCall.send()).thenReturn(expectedHistory).getMock()).
      when(contractService)
      .getHistory(tokenId);

    NFTETHContractService service = new NFTETHContractService(contractService, ipfsService);

    List<NFTLabStoreEthereum.NFTTransaction> actualHistory = service.getHistory(tokenId);

    assertTrue(expectedHistory.size() == actualHistory.size() && expectedHistory.containsAll(actualHistory) && expectedHistory.containsAll(actualHistory));
  }

  @Test
  public void getNFTById() throws Exception {
    BigInteger tokenId = BigInteger.valueOf(1);
    UserTuple artist = new UserTuple("0x51b1D3246fc4D665A75F77599c82419ab11dEAc4", BigInteger.valueOf(1));
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    String timestamp = "2021";

    NFTLabStoreEthereum.NFTLab expectedNFT = new NFTLabStoreEthereum.NFTLab(artist.wallet(), artist.id(), hash, timestamp);

    doReturn(when(nftLabRemoteFunctionCall.send()).thenReturn(expectedNFT).getMock()).
      when(contractService)
      .getNFTById(tokenId);

    NFTETHContractService service = new NFTETHContractService(contractService, ipfsService);
    NFTLabStoreEthereum.NFTLab actualNFT = service.getNFTById(tokenId);

    assertEquals(artist.wallet(), actualNFT.artist);
    assertEquals(artist.id(), actualNFT.artistId);
    assertEquals(hash, actualNFT.hash);
    assertEquals(timestamp, actualNFT.timestamp);
  }

  @Test
  public void getNFTByHash() throws Exception {
    UserTuple artist = new UserTuple("0x51b1D3246fc4D665A75F77599c82419ab11dEAc4", BigInteger.valueOf(1));
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    String timestamp = "2021";

    NFTLabStoreEthereum.NFTLab expectedNFT = new NFTLabStoreEthereum.NFTLab(artist.wallet(), artist.id(), hash, timestamp);

    doReturn(when(nftLabRemoteFunctionCall.send()).thenReturn(expectedNFT).getMock()).
      when(contractService)
      .getNFTByHash(hash);

    NFTETHContractService service = new NFTETHContractService(contractService, ipfsService);
    NFTLabStoreEthereum.NFTLab actualNFT = service.getNFTByHash(hash);

    assertEquals(artist.wallet(), actualNFT.artist);
    assertEquals(artist.id(), actualNFT.artistId);
    assertEquals(hash, actualNFT.hash);
    assertEquals(timestamp, actualNFT.timestamp);
  }
}
