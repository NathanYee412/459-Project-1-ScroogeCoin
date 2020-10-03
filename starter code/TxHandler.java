import java.util.*;
import java.util.Vector;

public class TxHandler {

	/* Creates a public ledger whose current UTXOPool (collection of unspent 
	 * transaction outputs) is utxoPool. This should make a defensive copy of 
	 * utxoPool by using the UTXOPool(UTXOPool uPool) constructor.
	 */

	private UTXOPool poolCopy;
	
	public TxHandler(UTXOPool utxoPool) {
		// IMPLEMENT THIS
		this.poolCopy = new UTXOPool(utxoPool);
	}

	/* Returns true if 
	 * (1) all outputs claimed by tx are in the current UTXO pool, 
	 * (2) the signatures on each input of tx are valid, 
	 * (3) no UTXO is claimed multiple times by tx, 
	 * (4) all of tx’s output values are non-negative, and
	 * (5) the sum of tx’s input values is greater than or equal to the sum of   
	        its output values;
	   and false otherwise.
	 */

	public boolean isValidTx(Transaction tx) {
		//create varaibles to compare if tx's input values are 
		//greater than or equal to the sum of its output values 
		double inputValueSum = 0.0;
		double outputValueSum = 0.0;
		
		Vector<UTXO> previousUTXO = new Vector<UTXO>();
		
		boolean result = true;
		
		//check inputs
		for(int i = 0; i < tx.numInputs(); i++){

			
			//create utxo object to hold current tx values 
			Transaction.Input input = tx.getInput(i);
			UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);

			//RSA Key inputs for verifySignature function 
			byte[] message = tx.getRawDataToSign(i);
			byte[] signature = input.signature;
			
			//(1) all outputs claimed by tx are in the current UTXO pool
			if(!this.poolCopy.contains(utxo)){
				return false;
			}

			//adding previous transaction output to our inputValueSum
			inputValueSum += poolCopy.getTxOutput(utxo).value;

			//poolCopy.getTxOutput(utxo).address try to short hand using RSA class 
			//(2) the signatures on each input of tx are valid
			if(!poolCopy.getTxOutput(utxo).address.verifySignature(message, signature)){
				return false;
			}
			
			//(3) no UTXO is claimed multiple times by tx
			if(previousUTXO.contains(utxo)){
				return false;
			}
			//add utxo to vector for comparison 
			previousUTXO.add(utxo);

		}
		//check outputs
		for(int i = 0; i < tx.numOutputs(); i++){
			//store transaction output value 
			Transaction.Output output = tx.getOutput(i);
			
			//(4) all of tx’s output values are non-negative
			if(output.value < 0.0){
				return false;
			}
			outputValueSum += output.value;
		}

		//(5) the sum of tx’s input values is greater than or equal to the sum of   
		//its output values;
		if(outputValueSum > inputValueSum){
			result = false;
		}

		return result;
	}

	/* Handles each epoch by receiving an unordered array of proposed 
	 * transactions, checking each transaction for correctness, 
	 * returning a mutually valid array of accepted transactions, 
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		//create vector to store valid transactions 
		Vector<Transaction> valid = new Vector<Transaction>();

		//loop through possible transactions and call updatePool
		for(int i = 0; i < possibleTxs.length; i++){
			Transaction handler = possibleTxs[i];

			//check if transaction is valid 
			if(isValidTx(handler)){
				
				//add valid transactions to vector
				valid.add(handler);

				updatePool(handler);
			}
		}

		//create new array of size 
		Transaction[] validArray = new Transaction[valid.size()];
		return valid.toArray(validArray);
	}


	//updates the transaction pool
	public void updatePool(Transaction handler) {
		
		for(int j = 0; j < handler.numInputs(); j++){
			Transaction.Input in = handler.getInput(j);
			UTXO removeIn = new UTXO(in.prevTxHash, in.outputIndex);
			poolCopy.removeUTXO(removeIn);
		}

		for(int k = 0; k < handler.numOutputs(); k++){
			Transaction.Output out = handler.getOutput(k);
			UTXO addOut = new UTXO(handler.getHash(), k);
			poolCopy.addUTXO(addOut, out);
		}
	} 

} 
