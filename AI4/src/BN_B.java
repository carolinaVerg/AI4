

public class BN_B extends BN_Node{

	public BN_B(int id) {
		super(id);
	}

	@Override
	public void updateCPT(){
		int numOfParents = this.parents.size();
		this.cpt = new double[numOfParents+1];
		for(int i=2; i< this.cpt.length; i++){

		}
	}
}
