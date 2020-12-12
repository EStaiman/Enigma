public class Rotor {
	
	int [] rotor;
	int notch;
	int ring;
	int offset;
	
	public Rotor(int[] array,int n) {
		rotor = array;
		notch = n;
	}
	
	public Rotor(Rotor rot) {
		this.rotor = rot.rotor;
		this.notch = rot.notch;
	}
}
