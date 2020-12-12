import java.util.Scanner;
import java.io.*;

public class Enigma {

	int num; //Used to track current letter
	
	//Rotor connections and reflectors taken from actual Enigma machines
	final int r1[] = {4,10,12,5,11,6,3,16,21,25,13,19,14,22,24,7,23,20,18,15,0,8,1,17,2,9};
	final int r2[] = {0,9,3,10,18,8,17,20,23,1,11,7,22,19,12,2,16,6,25,13,15,24,5,21,14,4};
	final int r3[] = {1,3,5,7,9,11,2,15,17,19,23,21,25,13,24,4,8,22,6,0,10,12,20,18,16,14};
	final int r4[] = {4,18,14,21,15,25,9,0,24,16,20,8,17,7,23,11,13,5,19,6,10,3,2,12,22,1};
	final int r5[] = {21,25,1,17,6,8,19,24,20,15,18,3,13,7,11,23,0,22,12,9,16,14,5,4,2,10};
	int reflectora[] = {4,9,12,25,0,11,24,23,21,1,22,5,2,17,16,20,14,13,19,18,15,8,10,7,6,3};
	int reflectorb[] = {24,17,20,7,16,18,11,3,15,23,13,6,14,10,12,8,4,1,5,25,2,22,21,9,0,19};
	int reflectorc[] = {5,21,15,9,8,0,14,24,4,3,17,25,23,22,6,2,19,10,20,16,18,1,13,12,7,11};
	
	//Creates rotors with their connections and notch points
	final Rotor ROTOR_1 = new Rotor(r1,16);
	final Rotor ROTOR_2 = new Rotor(r2,4);
	final Rotor ROTOR_3 = new Rotor(r3,21);
	final Rotor ROTOR_4 = new Rotor(r4,9);
	final Rotor ROTOR_5 = new Rotor(r5,25);
	final Rotor Rotors[] = {ROTOR_1,ROTOR_2,ROTOR_3,ROTOR_4,ROTOR_5};
	
	//Actual rotors being used
	Rotor rotor1;
	Rotor rotor2;
	Rotor rotor3;
	
	String reflectLetter; //Store reflectLetter

	//Store switchboard
	int switchBoard[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	
	PrintStream out; //File printstream
	
	public Enigma() throws IOException{
		Scanner input = new Scanner(System.in);
		out = new PrintStream(new FileOutputStream("enigma.txt"));
		String line = " ";
		char letter; //Current letter encoding
    
		//Store switchboard values
		int board1;
		int board2;
		int plugs = 0;
		
		//Getting user input
		System.out.println("What rotor do you want to use for rotor 1 (1-5)");
		rotor1 = new Rotor(Rotors[input.nextInt() - 1]);
		
		do {
			System.out.println("What rotor do you want to use for rotor 2 (1-5)");
			rotor2 = new Rotor(Rotors[input.nextInt() - 1]);
		} while (rotor1.notch == rotor2.notch);
		
		do {
			System.out.println("What rotor do you want to use for rotor 3 (1-5)");
			rotor3 = new Rotor(Rotors[input.nextInt() - 1]);
		} while (rotor3.notch == rotor2.notch || rotor3.notch == rotor1.notch);
		
		input.nextLine();
		
		System.out.println("What reflector would you like to use (A, B, or C)");
		reflectLetter = input.nextLine();
		
		System.out.println("Ring settings are between 0 and 25");
		System.out.println("What is the ring setting for rotor 1 (Left)");
		rotor1.offset = ((int) input.nextLine().charAt(0));
		System.out.println("What is the ring setting for rotor 2 (Middle)");
		rotor2.offset = ((int) input.nextLine().charAt(0));
		System.out.println("What is the ring setting for rotor 3 (Right)");
		rotor3.offset = ((int) input.nextLine().charAt(0));
		
		System.out.println("Pick a starting letter");
		System.out.println("What would you like the first rotor to start on");
		rotor1.ring = ((int) input.nextLine().charAt(0)) - 65 ;
		System.out.println("What would you like the second rotor to start on");
		rotor2.ring = ((int) input.nextLine().charAt(0)) - 65 ;
		System.out.println("What would you like the third rotor to start on");
		rotor3.ring = ((int) input.nextLine().charAt(0)) - 65 ;

		//Hardcoded values for testing purposes, skips user input
		/*
		rotor1 = new Rotor(Rotors[0]);
		rotor2 = new Rotor(Rotors[2]);
		rotor3 = new Rotor(Rotors[1]);
		reflectLetter = "B";
		rotor1.offset = 0;
		rotor2.offset = 0;
		rotor3.offset = 0;
		rotor1.ring = 0;
		rotor2.ring = 0;
		rotor3.ring = 0;
		*/

		//Switchboard
		System.out.println("Set up switchboard by typing pairs of capital letters");
		System.out.println("You can have up to ten pairs");
		System.out.println("Type \"STOP\" to stop");
		while (!line.equals("STOP")) {
			line = input.next();
			board1 = (int) line.charAt(0) - 65;
			board2 = (int) line.charAt(1) - 65;

			//Checking if letters are in use already before plugging
			if (switchBoard[board1] == board1 && switchBoard[board2] == board2) {
				switchBoard[board1] = board2;
				switchBoard[board2] = board1;
				plugs++;
			} else {
				System.out.println("One or more plugs already in use");
			}	

			//Exits switchboard setup if 10 plugs are in board
			if (plugs == 10) {
				System.out.println("Ten plugs are in the board");
				break;
			}
		}
		line = " ";

		//Encoding loop
		System.out.println("Input capital letters (Type \"STOP\" to stop) (Hit ENTER for space)");
		while (!line.equals("STOP")) {
			line = input.next();
			if (!line.equals("STOP")) {
				for (int i = 0; i < line.length(); i++) {
					letter = line.charAt(i);
					num = (int) letter;
					num -= 65;
					notch();  //Moves the rotors
					encode(); //Encodes the letter
				}
				System.setOut(out); //Write to text file
				out.print(" ");
				System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
			}
		}
		input.close();
	}
	
	//Handles the encoding going into the reflector
	public void ringIn(Rotor rot) {
		num += rot.ring;
		num -= rot.offset;
		num = (num + 26) % 26;  //Need to add 26 first to handle negatives
		num = rot.rotor[num];
		num -= rot.ring;
		num += rot.offset;
		num = (num + 26) % 26;
	}
	
	//Handles the encoding going out from the reflector
	public void ringOut(Rotor rot) {
		num += rot.ring;
		num -= rot.offset;
		num = (num + 26) % 26;
		
		//Handles going 'backwards', so sets num to the index
		for (int i = 0; i < 26; i++) {
			if (num == rot.rotor[i]) {
				num = i;
				break;
			}
		}
		num -= rot.ring;
		num += rot.offset;
		num = (num + 26) % 26;
	}

	//Moves the rings
	public void notch() {
    //Moves the leftmost ring if middle ring is at notch point
		if (rotor2.ring == rotor2.notch) {
			rotor1.ring++;
			rotor1.ring %= 26;
			rotor2.ring++;
			rotor2.ring %= 26;
		}
		//Moves the middle ring if left ring is at notch point
		if (rotor3.ring == rotor3.notch) {
			rotor2.ring++;
			rotor2.ring %= 26;
		}
		rotor3.ring++;
		rotor3.ring %= 26;
	}
	
	//Handles entire encoding process
	public void encode() {
		num = switchBoard[num];
		ringIn(rotor3);
		ringIn(rotor2);
		ringIn(rotor1);
		
		switch (reflectLetter) {
			case "A":
				num = reflectora[num];
				break;
			case "B":
				num = reflectorb[num];
				break;
			case "C":
				num = reflectorc[num];
				break;
		}
		
		ringOut(rotor1);
		ringOut(rotor2);
		ringOut(rotor3);
		num = switchBoard[num];
		
		num += 65;
		String finalLetter = Character.toString((char) num);
		System.setOut(out);
		out.print(finalLetter);
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	public static void main(String[] args) throws IOException{
		Enigma machine = new Enigma();
	}
}
