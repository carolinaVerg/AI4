import java.util.HashMap;
import java.util.LinkedList;

public class BN_Net {
	HashMap<Integer,BN_Node> fls;
	HashMap<Integer,BN_Node> bs;
	HashMap<Integer,BN_Node> evs;
	public BN_Net(int numOfFls) {
		this.fls= new HashMap<>();
		this.bs=new HashMap<>();
		this.evs=new HashMap<>();
		for(int i=1; i<= numOfFls; i++) {
			fls.put(i, new BN_Fl(i, 0.001));
			evs.put(i, new BN_Ev(i));
		}
	}
	


	public void updateCPTInNet() {
		for (BN_Node fl: fls.values()){
			fl.updateCPT();
		}
		for (BN_Node b: bs.values()){
			b.updateCPT();
		}
		for (BN_Node ev: evs.values()){
			ev.updateCPT();
		}
	}
	public double [] Enumeration_Ask(BN_Node x, HashMap<BN_Node, Boolean> e){
		double Qx []= new double [2]; // distrebution over x.
		if(e.containsKey(x)){
			if(e.get(x)){
				Qx[0] = 1;
				Qx[1] = 0;
			}
			else{
				Qx[0] = 0;
				Qx[1] = 1;
			}
		}
		else{
			HashMap<BN_Node, Boolean> eT= (HashMap<BN_Node, Boolean>) e.clone();
			eT.put(x, true);
			HashMap<BN_Node, Boolean> eF=(HashMap<BN_Node, Boolean>) e.clone();
			eF.put(x, false);
			LinkedList<BN_Node>vars= new LinkedList<>();  //build vars
			for(BN_Node node: this.fls.values())
				vars.add(node);
			for(BN_Node node: this.bs.values())
				vars.add(node);
			for(BN_Node node: this.evs.values())
				vars.add(node);
			LinkedList<BN_Node>vars2= (LinkedList<BN_Node>) vars.clone();
			Qx[0]= Enumerate_AllVars(vars , eT);  // true
			Qx[1]= Enumerate_AllVars(vars2 , eF);  // false
		}
		return normlize(Qx);
	
	}
	public double Enumerate_AllVars(LinkedList<BN_Node> vars ,HashMap<BN_Node, Boolean> e) {
		double b;
		double a;
		if(vars.isEmpty())
			return 1;
		BN_Node Y =vars.getFirst();
		vars.removeFirst();
		if(e.containsKey(Y)) {
			b = (calc_y_given_p(Y, e.get(Y), e)) ;
			return b * Enumerate_AllVars(vars, e);
		}
		else {
			HashMap<BN_Node, Boolean> eYt=(HashMap<BN_Node, Boolean>) e.clone();
			HashMap<BN_Node, Boolean> eYf=(HashMap<BN_Node, Boolean>) e.clone();
			eYt.put(Y, true);
			eYf.put(Y, false);
			b = (calc_y_given_p (Y, true, (HashMap<BN_Node, Boolean>)e.clone()));
			a = (calc_y_given_p (Y, false, (HashMap<BN_Node, Boolean>)e.clone()));
			return (b * Enumerate_AllVars((LinkedList<BN_Node> )vars.clone(), eYt)) +
					(a * Enumerate_AllVars((LinkedList<BN_Node> )vars.clone(), eYf));
		}
	}
	public double calc_y_given_p (BN_Node y, boolean value, HashMap<BN_Node, Boolean> e) {
		int entryNum=1;
		int counter=y.getParents().size()-1;
		for(BN_Node p: y.getParents().values()) {
			if(e.get(p))
				entryNum=entryNum+(int)Math.pow(2,counter);
			counter--;
				
		}

		if(value){
			return y.cpt[entryNum];
		}
		else{
			return 1- y.cpt[entryNum];
		}

	}
	public double [] normlize(double Qx []) {
		double alpha =1/(Qx[0]+Qx[1]);
		Qx[0]=alpha* Qx[0];
		Qx[1]=alpha* Qx[1];
		return Qx;
	}



	public HashMap<Integer, BN_Node> getFls() {
		return fls;
	}



	public void setFls(HashMap<Integer, BN_Node> fls) {
		this.fls = fls;
	}



	public HashMap<Integer, BN_Node> getBs() {
		return bs;
	}



	public void setBs(HashMap<Integer, BN_Node> bs) {
		this.bs = bs;
	}



	public HashMap<Integer, BN_Node> getEvs() {
		return evs;
	}



	public void setEvs(HashMap<Integer, BN_Node> evs) {
		this.evs = evs;
	}
}
