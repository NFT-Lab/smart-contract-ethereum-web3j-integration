package io.nfteam.nftlab.services.ipfs;

class PinataUploaded implements IPFSResponses.Uploaded {
    public String IpfsHash;
    public int PinSize;
    public String Timestamp;

    public String getHash() {
        return IpfsHash;
    }

    public int getSize() {
        return PinSize;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    @Override
    public String toString() {
        return IpfsHash + " - " + PinSize + " - " + Timestamp;
    }
}
