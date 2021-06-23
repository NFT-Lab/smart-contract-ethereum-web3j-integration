package io.nfteam.nftlab.services.ipfs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinataUploadedTest {
  @Test
  public void defaultConstructor() {
    int expectedPinSize = 0;

    PinataUploaded uploadImage = new PinataUploaded();

    assertNull(uploadImage.getHash());
    assertEquals(expectedPinSize, uploadImage.getSize());
    assertNull(uploadImage.getTimestamp());
  }

  @Test
  public void getHash() {
    String expectedHash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";

    PinataUploaded uploadImage = new PinataUploaded();

    uploadImage.IpfsHash = expectedHash;

    assertEquals(expectedHash, uploadImage.getHash());
  }

  @Test
  public void getSize() {
    int expectedPinSize = 12;

    PinataUploaded uploadImage = new PinataUploaded();

    uploadImage.PinSize = expectedPinSize;

    assertEquals(expectedPinSize, uploadImage.getSize());
  }

  @Test
  public void getTimestamp() {
    String expectedTimestamp = "2021";

    PinataUploaded uploadImage = new PinataUploaded();

    uploadImage.Timestamp = expectedTimestamp;

    assertEquals(expectedTimestamp, uploadImage.getTimestamp());
  }

  @Test
  public void toString_Object_StringOfObject() {
    String hash = "QmeK3GCfbMzRp3FW3tWZCg5WVZKM52XZrk6WCTLXWwALbq";
    int pinSize = 12;
    String timestamp = "2021";

    String expectedToString =  hash + " - " + pinSize + " - " + timestamp;

    PinataUploaded uploadImage = new PinataUploaded();

    uploadImage.IpfsHash = hash;
    uploadImage.PinSize = pinSize;
    uploadImage.Timestamp = timestamp;

    assertEquals(expectedToString, uploadImage.toString());
  }
}
