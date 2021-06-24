# Smart contract java integration
[![Coverage Status](https://coveralls.io/repos/github/NFT-Lab/smart-contract-integration/badge.svg?branch=main)](https://coveralls.io/github/NFT-Lab/smart-contract-integration?branch=main)
[![Build Web3J integration](https://github.com/NFT-Lab/smart-contract-ethereum-web3j-integration/actions/workflows/build.yml/badge.svg)](https://github.com/NFT-Lab/smart-contract-ethereum-web3j-integration/actions/workflows/build.yml)

La seguente repository contiene la libreria che permette la comunicazione da java con lo smart contract NFTLabStore per Hotmoka ed Ethereum per la gestione della compra vendita di NFT nella piattaforma NFTLab.

## Strumenti utilizzati

* **[Maven](https://maven.apache.org/).**
* **[JUnit5](https://junit.org/junit5/):** libreria per la scrittura di test nel linguaggio java.
* **[Mockito](https://site.mockito.org/):** libreria per la scrittura di mock utilizzabili ed integrabili con **JUnit5**.
* **Solc**: compilatore per solidity.
* **Web3J**: libreria per l'interazione con la blockchain.

## Prerequisiti
1. Creare un token personale che permetta la lettura di pacchetti da github ([la guida qui](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token)).

2. Creare il file **settings.xml** dentro la cartella **~/.m2** con il seguente contenuto:
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                         http://maven.apache.org/xsd/settings-1.0.0.xsd">

     <activeProfiles>
       <activeProfile>github</activeProfile>
     </activeProfiles>

     <profiles>
       <profile>
         <id>github</id>
         <repositories>
           <repository>
             <id>central</id>
             <url>https://repo1.maven.org/maven2</url>
           </repository>
           <repository>
             <id>github</id>
             <url>https://maven.pkg.github.com/NFT-Lab/*</url>
             <snapshots>
               <enabled>true</enabled>
             </snapshots>
           </repository>
         </repositories>
       </profile>
     </profiles>

     <servers>
       <server>
         <id>github</id>
         <username>$GITHUB_USERNAME</username>
         <password>$ACCESS_TOKEN</password>
       </server>
     </servers>
</settings>
```

In questo modo si permetterà a maven di accedere alla repository maven di github.

## Organizzazione della repository

```bash
├── LICENSE
├── pom.xml
├── README.md
├── smart-contract-ethereum
├── smart-contract-integration.iml
├── sol-to-java.sh
└── src
    ├── main
    │   └── java
    │       └── io
    │           └── nfteam
    │               └── nftlab
    │                   ├── contracts
    │                   │   ├── hotmoka
    │                   │   │   ├── HotmokaIntegration.java
    │                   │   │   └── NFTLabStoreHotmokaMethods.java
    │                   │   ├── NFTLabStoreEthereum.java
    │                   │   └── NFTLabStoreHotmoka.java
    │                   └── services
    │                       ├── ipfs
    │                       │   ├── PinataUploaded.java
    │                       │   ├── IPFSPinataService.java
    │                       │   ├── IPFSUploaded.java
    │                       │   └── IPFSService.java
    │                       ├── NFTContractService.java
    │                       ├── NFTDummyContractService.java
    │                       ├── NFTETHContractService.java
    │                       ├── NFTHotmokaContractService.java
    │                       └── smartcontract
    │                           ├── INFTLab.java
    │                           ├── INFTTransaction.java
    │                           ├── NFTID.java
    │                           └── UserTuple.java
    └── test
        └── java
            └── io
                └── nfteam
                    └── nftlab
                        └── services
                            ├── ipfs
                            │   ├── IPFSPinataServiceTest.java
                            │   └── PinataUploadedTest.java
                            ├── NFTDummyContractServiceTest.java
                            ├── NFTETHContractServiceTest.java
                            ├── NFTHotmokaContractServiceTest.java
                            └── smartcontract
                                ├── NFTIDTest.java
                                └── UserTupleTest.java

```



### Package io.nfteam.nftlab

#### Contracts

##### NFTLabStoreEthereum

Classe che gestisce l'interazione con la blockchain Ethereum. Viene autogenerata dallo strumento **Web3J**, il quale dovrà essere installato nel proprio pc ([la guida qui](http://docs.web3j.io/latest/quickstart/)), a partire dall'ABI dello smart contract, il quale sarà ottenuto attraverso il compilatore solc ([disponibile qui](https://github.com/ethereum/solidity/releases)). Per comodità è stato creato lo script **sol-to-java.sh** che, dato il path allo smart contract, genera automaticamente la classe **NFTLabStoreEthereum** all'interno del package io.nfteam.nftlab.contracts.

Esempio di esecuzione:

```bash
./sol-to-java.sh ./smart-contract-ethereum/contracts/NFTLabStore.sol
```

##### NFTLabStoreHotmoka

![NFTLabStoreHotmoka class diagram](./docs/images/classdiagram_contracts_hotmoka.svg)

Classe che gestisce l'interazione con la blockchain Hotmoka. Al contrario della classe precedente, questa è stata scritta a mano data la mancanza di uno strumento come Web3J per il linguaggio Takamaka.

#### Services


#### Test

Tutti i test sono stati implementati attraverso le librerie **Junit5** e **Mockito**.

## Continuous Integration

### Build

Viene eseguita dalla action presente nel file **build.yml** ad ogni push nel **main** o nel **develop** e ha il compito di compilare e eseguire i test.

### Publish to github packages

Viene eseguita dalla action presente nel file **publish-to-github-packages.yml** ad ogni creazione di una nuova release e ha il compito di pubblicare l'artefatto maven nella repository maven dell'organizzazione NFT-Lab ([https://github.com/orgs/NFT-Lab/packages](https://github.com/orgs/NFT-Lab/packages)).
