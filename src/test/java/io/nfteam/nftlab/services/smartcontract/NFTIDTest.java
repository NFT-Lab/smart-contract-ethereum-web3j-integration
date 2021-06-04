package io.nfteam.nftlab.services.smartcontract;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class NFTIDTest {

  @Test
  public void defaultConstructor_ValidParameters_ObjectCreatedRight() {
    final String expectedHash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    final BigInteger expectedTokenId = BigInteger.valueOf(1);

    NFTID nftid = new NFTID(expectedHash, expectedTokenId);

    assertEquals(expectedHash, nftid.hash());
    assertEquals(expectedTokenId, nftid.tokenId());
  }

  @Test
  public void defaultConstructor_NullHash_ThrowException() {
    final String expectedHash = null;
    final BigInteger expectedTokenId = BigInteger.valueOf(1);

    assertThrows(NullPointerException.class, () -> new NFTID(expectedHash, expectedTokenId));
  }

  @Test
  public void defaultConstructor_NullTokenId_ThrowException() {
    final String expectedHash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    final BigInteger expectedTokenId = null;

    assertThrows(NullPointerException.class, () -> new NFTID(expectedHash, expectedTokenId));
  }
}
