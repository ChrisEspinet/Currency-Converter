import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConverter {

	public static void main(String[] args) throws MalformedURLException, IOException, JSONException {

		System.out.println("Currency Converter 1.0 \nThis program will allow you to convert between US Dollars, Euros, Yen, Pounds, Australian Dollars, Candadian Dollars, Francs, New Zealand Dollars, Hong Kong Dollars, and Rupees.\n ");	
		Boolean operating = true;
		
		do{
			HashMap<Integer, String> currencyCode = new HashMap<Integer, String>();
			
			currencyCode.put(1, "USD");
			currencyCode.put(2, "EUR");
			currencyCode.put(3, "JPY");
			currencyCode.put(4, "GBP");
			currencyCode.put(5, "AUD");
			currencyCode.put(6, "CAD");
			currencyCode.put(7, "CHF");
			currencyCode.put(8, "NZD");
			currencyCode.put(9, "HKD");
			currencyCode.put(10, "INR");
			
			
			String convertFrom, convertTo;
			double amount;
			int from, to;
			
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("Which currency are you converting from?");
			System.out.println("1:USD \t 2:EUR \t 3:JPY \t 4:GBP \t 5:AUD \t 6:CAD \t 7:CHF \t 8:NZD \t 9:HKD \t 10:INR");
			from = sc.nextInt();
			while(from < 1 || from > 10) {
				System.out.println("Please select a currency between 1 and 10");
				System.out.println("1:USD \t 2:EUR \t 3:JPY \t 4:GBP \t 5:AUD \t 6:CAD \t 7:CHF \t 8:NZD \t 9:HKD \t 10:INR");
				from = sc.nextInt();
			}
			convertFrom = currencyCode.get(from);
			
			System.out.println("Which currency are you converting to?");
			System.out.println("1:USD \t 2:EUR \t 3:JPY \t 4:GBP \t 5:AUD \t 6:CAD \t 7:CHF \t 8:NZD \t 9:HKD \t 10:INR");
			to = sc.nextInt();
			while(to < 1 || to > 10) {
				System.out.println("Please select a currency between 1 and 10");
				System.out.println("1:USD \t 2:EUR \t 3:JPY \t 4:GBP \t 5:AUD \t 6:CAD \t 7:CHF \t 8:NZD \t 9:HKD \t 10:INR");
				to = sc.nextInt();
			}
			convertTo = currencyCode.get(to);
			
			System.out.println("Enter the amount you wish to convert.");
			amount = sc.nextFloat();
			
			sendHttpGETRequest(convertFrom, convertTo, amount);	
			
			System.out.println("Press 1 to make another conversion or press any other number to exit the converter");
			if(sc.nextInt() != 1) {
				operating = false;
			}
		} 
		while(operating);
		
		System.out.println("Thank you for using Currency Converter 1.0");
		
	}
	
	private static void sendHttpGETRequest(String convertFrom, String convertTo, double amount) throws MalformedURLException, IOException, JSONException {
		
		DecimalFormat df = new DecimalFormat("00.00");
		
		String getURL = "https://api.exchangeratesapi.io/latest?base=" + convertTo + "&symbols=" + convertFrom;
		
		URL url = new URL(getURL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		
		int responseCode = httpURLConnection.getResponseCode();
		
		if(responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			
			JSONObject obj = new JSONObject(response.toString());
			Double exchangeRate = obj.getJSONObject("rates").getDouble(convertFrom);
			
				/* Keep for debugging, will show if proper rates are appearing
				 * System.out.println(exchangeRate);
				 * System.out.println(obj.getJSONObject("rates"));
				 * System.out.println();
				 */
			
			System.out.println(df.format(amount) + " " + convertFrom + " = " + df.format(amount/exchangeRate) + " " + convertTo);
		}
		else 
			System.out.println("Get request failed. URL may be invalid or API may be down");	
	}

}
