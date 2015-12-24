package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

public class Klijent {
	public static void main(String[] args) {

		Socket kontrolniSoket;
		try {
			kontrolniSoket = new Socket("localhost", 1908);
			BufferedReader odServera = new BufferedReader(new InputStreamReader(kontrolniSoket.getInputStream()));
			PrintStream odKlijenta = new PrintStream(kontrolniSoket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			while(true){ //petlja za operacije i brojeve
				String odgovor = "";
				do {
					//System.out.println("Unesite neku od ponudjenih operacija:");
					System.out.println("Odaberite računsku operaciju: " + "\n"
							+ "sabiranje -> index 1 " + "\n"
							+ "oduzimanje -> index 2 " + "\n"
							+ "množenje -> index 3 " + "\n"
							+ "deljenje -> index 4");
					odgovor = in.readLine();
				} while (!odgovor.matches("[1-4]"));

				odKlijenta.println(odgovor);

				String potvrdaServera = odServera.readLine();
				System.out.println("Imate potvrdu: " + potvrdaServera);

				String unos1 = "0";
				String unos2 = "0";

				while (true) {
					try {
						System.out.println("Unesite prvu vrednost za računanje ");
						unos1 = in.readLine();
						Double.parseDouble(unos1);
						break;
					} catch (Exception e) {
						System.out.println("Niste uneli broj! Pokusajte opet!");
					} 
				}
				LinkedList<Double> brojevi = new LinkedList<>();
				do{
					try {
						
							System.out.println("Unesite sledecu vrednost za računanje, unesite stop za prekid unosa i rezultat");
							unos2 = in.readLine();
							if(unos2.equals("stop")){
								break;
							}
							double a = Double.parseDouble(unos2);
							if(a==0 && odgovor.equals("4")){
								System.out.println("Ne mozete deliti sa nulom");
								continue;
							}
							brojevi.add(a);
						}
					 catch (Exception e) {
						System.out.println("Niste uneli broj! Pokusajte opet!");
					} 
				} while (true);



				//				System.out.println("Unesi port");
				//				String portUnos = in.readLine();
				//				int port = Integer.parseInt(portUnos);

				//				int port = 18413;


				Socket podaciSoket = new Socket("localhost", 18413);
				BufferedReader dataOdServera = new BufferedReader(new InputStreamReader(podaciSoket.getInputStream()));
				PrintStream dataOdKlijenta = new PrintStream(podaciSoket.getOutputStream());

				String poruka = unos1 + "##";
				for (int i = 0; i < brojevi.size(); i++) {
					poruka += brojevi.get(i) + "##";
					
				}
				dataOdKlijenta.println(poruka);


				String rezultat = dataOdServera.readLine();
				System.out.println("Rezultat je: " + rezultat);

				podaciSoket.close();




				System.out.println("Za prekid računanja unesite exit, ili enter za novo racunanje");
				String exit = in.readLine();


				if(exit.equals("exit")){
					odKlijenta.println("KRAJ");
					break;
				}

			}
			kontrolniSoket.close();


		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

