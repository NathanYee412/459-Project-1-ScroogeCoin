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
	 * Nathan
	 * (2) the signatures on each input of tx are valid, 
	 * Carlos
	 * (3) no UTXO is claimed multiple times by tx, 
	 * Brandon
	 * (4) all of tx’s output values are non-negative, and
	 * Danny
	 * (5) the sum of tx’s input values is greater than or equal to the sum of   
	        its output values;
	   and false otherwise.
	 */

	public boolean isValidTx(Transaction tx) {
		//things we need to implement
		// 1. variables for a new unique utxo
		// 2. previous transaction output sum
		// 3. current transaction output sum
		// 4. for loop to check the 5 conditions above 
		
		// for (int i = 0; i < tx.numInputs(); i++) 
		//check if all outputs are claimed by tx are in teh current UTXO pool
		if(!poolCopy.contains(utxo)){
			return false;
		}
	}

	/* Handles each epoch by receiving an unordered array of proposed 
	 * transactions, checking each transaction for correctness, 
	 * returning a mutually valid array of accepted transactions, 
	 * and updating the current UTXO pool as appropriate.
	 */
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		// IMPLEMENT THIS
		return null;
	}

} 
