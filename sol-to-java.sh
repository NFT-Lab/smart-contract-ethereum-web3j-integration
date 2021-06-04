#!/bin/bash

contract=$1
fileName=`basename $contract`
name="${fileName%.*}"

solc @openzeppelin/="$(pwd)/smart-contract-ethereum/node_modules/@openzeppelin/" $contract --bin --abi --optimize -o ./out/ --overwrite

web3j generate solidity -b out/$name.bin -a out/$name.abi -o ./src/main/java -p io.nfteam.nftlab.contracts
