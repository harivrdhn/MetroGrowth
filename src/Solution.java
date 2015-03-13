import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.nio.charset.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.*;
import com.google.common.collect.MinMaxPriorityQueue.Builder;

class byValue implements Comparator<Map.Entry<String,Double>> {
    public int compare(Map.Entry<String,Double> e1, Map.Entry<String,Double> e2) {
        if (e1.getValue() < e2.getValue()){
            return 1;
        } else if (e1.getValue() == e2.getValue()) {
            return 0;
        } else {
            return -1;
        }
    }
}

public class Solution {

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws IOException {

		File csv = new File("75f647c2ac77-Metropolitan_Populations_2010-2012_.csv");
        CSVParser parser = CSVParser.parse(csv,Charset.defaultCharset(), CSVFormat.RFC4180);
         
        Builder b = MinMaxPriorityQueue.orderedBy(new byValue());
        MinMaxPriorityQueue<Map.Entry<String,Double>> maxqueue = b.maximumSize(5).create();
        MinMaxPriorityQueue<Map.Entry<String,Double>> squeue = b.maximumSize(5).create();
        MinMaxPriorityQueue<Map.Entry<String,Double>> minqueue = b.maximumSize(5).create();
        
      //  while(!scanner.next().equals("2012 Population"));
        
        int sten = 0, stwelve = 0;
        String state = "";
        for (CSVRecord record : parser){
        	if(record.get(0).equals("Geography"))
        		continue;
        	String name = record.get(0);
        	int ten = Integer.parseInt(record.get(1));
        	int twelve = Integer.parseInt(record.get(3));
        	
        	String s[] = name.replace("\"", "").split(",");
        	//if new state
        	if(state.equalsIgnoreCase(s[1])){
            	sten += ten;
            	stwelve += twelve;
        	} else {
        		if(state.equals("")){
        			state = s[1];
        		} else{
        			double spercentage  = (double)(stwelve-sten)/sten *100;
        			squeue.add(Maps.immutableEntry(state, spercentage));
        			sten = ten;
        			stwelve = twelve;
        			state = s[1];
        		}
        	}
        	
        	
        	//skip city if population less than 50000
        	if(ten >= 50000 && ten<twelve){
        		double percentage = (double)(twelve - ten)/ten *100;
        		maxqueue.add(Maps.immutableEntry(name, percentage));
        	}
        	if(twelve >= 50000 && twelve<ten){
        		double percentage = (double)(ten - twelve)/ten *100;
        		minqueue.add(Maps.immutableEntry(name, percentage));
        	}
        } 

        
        System.out.println("The cities with highest growth rates are:");
        while(maxqueue.peek() != null){
        		System.out.printf("%-40s\tPercentage: %.2f%% \n" ,maxqueue.peek().getKey(), maxqueue.poll().getValue());
        }
        
        System.out.println("\nThe cities with highest shrinking rates are:");
        while(minqueue.peek() != null){
        		System.out.printf("%-40s\tPercentage: %.2f%% \n",minqueue.peek().getKey(), minqueue.poll().getValue()) ;
        }
        
        System.out.println("\nThe states with highest growth rates are:");
        while(squeue.peek() != null){
        		System.out.printf("%-40s\tPercentage: %.2f%% \n" ,squeue.peek().getKey(), squeue.poll().getValue());
        }
	}


}
