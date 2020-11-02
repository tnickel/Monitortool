package statistic;

import java.util.Scanner;

//Bibliothek zur statistischen Datenverarbeitung 
public class Statistics {
	
	// Berechnet das Maximum der Arrayeintraege
	public static double max(double[] a) {
		double max = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}

	// Berechnet den Mittelwert der Arrayeintraege
	public static double mean(double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i];
		}
		return sum/a.length;
	}

	// Berechnet die korrigierte Stichprobenvarianz
	public static double var(double[] a) {
		double m = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + (a[i] - m) * (a[i] - m);
		}
		return sum / (a.length - 1);
	}

	// Berechnet die Standardabweichung
	public static double stddv(double[] a) {
		return Math.sqrt(var(a));
	}

	// Ermittelt die Position des kleinsten Wertes 
	// aus dem Indexbereich [low,high]
	public static int minPos(double[] a, int low, int high) {
		double min = a[low];
		int minPos = low;
		for (int i = low; i < high; i++){
			if (min > a[i]) {
				min = a[i];
				minPos = i;
			}
		}
		return minPos;
	}

	// Vertauscht den Wert von Eintrag an Position i 
	// mit dem Eintrag an Position j
	public static void swap(double[] a, int i, int j) {
		double t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	// Sortiert die Werte im Array aufsteigend
	public static void sort(double[] a) {
		for (int i = 0; i < a.length; i++) {
			int m = minPos(a, i, a.length);
			swap(a,m,i);
		}
	}

	// Berechnet den Median der Arrayeintraege
	public static double median(double[] a){
		sort(a);
		if (a.length % 2 == 0) {
			return (a[a.length/2 - 1] + a[a.length/2]) / 2.0;
		} else {
			return (a[a.length/2]);
		}
	}

	

	// Gibt zunaechst die Arraylaenge und dann die 
	// einzelnen Arrayeintrage auf der Konsole aus
	public static void printArray(double[] a) {
		System.out.println(a.length);
		for(int i = 0; i < a.length; i++) {
			System.out.println(" " + a[i]);
		}
		System.out.println("");
	}

}