import java.util.HashMap;

public class BN_Net {
	HashMap<Integer,BN_Fl> fls;
	HashMap<Integer,BN_B> bs;
	HashMap<Integer,BN_Ev> evs;
	public BN_Net(int numOfFls) {
		this.fls= new HashMap<>();
		this.bs=new HashMap<>();
		this.evs=new HashMap<>();
		for(int i=1; i<= numOfFls; i++) {
			fls.put(i, new BN_Fl(i, 0.001));
			evs.put(i, new BN_Ev(i));
		}
	}
	public HashMap<Integer, BN_Fl> getFls() {
		return fls;
	}
	public void setFls(HashMap<Integer, BN_Fl> fls) {
		this.fls = fls;
	}
	public HashMap<Integer, BN_B> getBs() {
		return bs;
	}
	public void setBs(HashMap<Integer, BN_B> bs) {
		this.bs = bs;
	}
	public HashMap<Integer, BN_Ev> getEvs() {
		return evs;
	}
	public void setEvs(HashMap<Integer, BN_Ev> evs) {
		this.evs = evs;
	}


	public void updateCPTInNet() {
		for (BN_Fl fl: fls.values()){
			fl.updateCPT();
		}
		for (BN_B b: bs.values()){
			b.updateCPT();
		}
		for (BN_Ev ev: evs.values()){
			ev.updateCPT();
		}
	}
}
