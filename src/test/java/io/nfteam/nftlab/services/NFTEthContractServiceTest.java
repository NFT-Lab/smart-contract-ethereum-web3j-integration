package io.nfteam.nftlab.services;

import io.nfteam.nftlab.contracts.NFTLabStore;
import io.nfteam.nftlab.services.ipfs.IPFSResponses;
import io.nfteam.nftlab.services.ipfs.IPFSService;
import io.nfteam.nftlab.services.smartcontract.NFTID;
import io.nfteam.nftlab.services.smartcontract.UserTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NFTEthContractServiceTest {
  @Mock
  private NFTLabStore contractService;
  @Mock
  private IPFSService ipfsService;
  @Mock
  private IPFSResponses.UploadImage uploadImage;
  @Mock
  private FileSystemResource file;

  @Mock
  private RemoteFunctionCall<BigInteger> bigIntegerRemoteFunctionCall;

  @Mock
  private RemoteFunctionCall<TransactionReceipt> transactionReceiptRemoteFunctionCall;

  @Mock
  private RemoteFunctionCall<List> listRemoteFunctionCall;

  @Mock
  private RemoteFunctionCall<NFTLabStore.NFTLab> nftLabRemoteFunctionCall;

  @Test
  public void mint_ValidNFT_HashAndIdOfMintedNFT() throws Exception {
    UserTuple artist = new UserTuple("0x51b1D3246fc4D665A75F77599c82419ab11dEAc4", BigInteger.valueOf(1));
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    BigInteger tokenId = BigInteger.valueOf(1);

    when(uploadImage.getHash()).thenReturn(hash);
    when(ipfsService.uploadImage(file)).thenReturn(uploadImage);

    doReturn(when(bigIntegerRemoteFunctionCall.send()).thenReturn(tokenId).getMock()).
      when(contractService)
      .getTokenId(hash);

    when(contractService.mint(any(NFTLabStore.NFTLab.class))).thenReturn(transactionReceiptRemoteFunctionCall);

    NFTEthContractService service = new NFTEthContractService(contractService, ipfsService);

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

    when(contractService.transfer(any(NFTLabStore.NFTTransaction.class))).thenReturn(transactionReceiptRemoteFunctionCall);

    NFTEthContractService service = new NFTEthContractService(contractService, ipfsService);

    service.transfer(tokenId, seller, buyer, price, timestamp);
  }

  @Test
  public void getHistory() throws Exception {
    BigInteger tokenId = BigInteger.valueOf(1);
    List<NFTLabStore.NFTTransaction> expectedHistory = new ArrayList<>();

    doReturn(when(listRemoteFunctionCall.send()).thenReturn(expectedHistory).getMock()).
      when(contractService)
      .getHistory(tokenId);

    NFTEthContractService service = new NFTEthContractService(contractService, ipfsService);

    List<NFTLabStore.NFTTransaction> actualHistory = service.getHistory(tokenId);

    assertTrue(expectedHistory.size() == actualHistory.size() && expectedHistory.containsAll(actualHistory) && expectedHistory.containsAll(actualHistory));
  }

  @Test
  public void getNFTById() throws Exception {
    BigInteger tokenId = BigInteger.valueOf(1);
    UserTuple artist = new UserTuple("0x51b1D3246fc4D665A75F77599c82419ab11dEAc4", BigInteger.valueOf(1));
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    String timestamp = "2021";

    NFTLabStore.NFTLab expectedNFT = new NFTLabStore.NFTLab(artist.wallet(), artist.id(), hash, timestamp);

    doReturn(when(nftLabRemoteFunctionCall.send()).thenReturn(expectedNFT).getMock()).
      when(contractService)
      .getNFTById(tokenId);

    NFTEthContractService service = new NFTEthContractService(contractService, ipfsService);
    NFTLabStore.NFTLab actualNFT = service.getNFTById(tokenId);

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

    NFTLabStore.NFTLab expectedNFT = new NFTLabStore.NFTLab(artist.wallet(), artist.id(), hash, timestamp);

    doReturn(when(nftLabRemoteFunctionCall.send()).thenReturn(expectedNFT).getMock()).
      when(contractService)
      .getNFTByHash(hash);

    NFTEthContractService service = new NFTEthContractService(contractService, ipfsService);
    NFTLabStore.NFTLab actualNFT = service.getNFTByHash(hash);

    assertEquals(artist.wallet(), actualNFT.artist);
    assertEquals(artist.id(), actualNFT.artistId);
    assertEquals(hash, actualNFT.hash);
    assertEquals(timestamp, actualNFT.timestamp);
  }
}
