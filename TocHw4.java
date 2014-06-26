import java.io.*;
import java.net.*;

import org.json.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.Charset;

public class TocHW3
{
	public static class RealPriceSearch
	{
		private static String ReadFile(Reader filename) throws IOException
		{
			int temp;
		    StringBuilder buffer = new StringBuilder();
		    while ((temp = filename.read()) != -1)
		      buffer.append((char) temp);
		    return buffer.toString();
		}

		  public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException
		  {
			  InputStream is = new URL(url).openStream();
			  try 
			  {
				  BufferedReader rd = new BufferedReader(new InputStreamReader(is,  Charset.forName("UTF-8")));
				  String jsonText = ReadFile(rd);
				  JSONArray json = new JSONArray(jsonText);
				  return json;
			  } 
			  finally
			  {
				  is.close();
			  }
		  }
	}
	public static void main(String[] args) throws JSONException, IOException
	{
		int value = 0, counter = 0;
		JSONArray json = RealPriceSearch.readJsonFromUrl(args[0]);
	    Pattern county = Pattern.compile(args[1]);
	    Pattern roadname = Pattern.compile(".*"+args[2]+".*");
	    
	    for(int i = 0; i < json.length(); i++)
	    {
	    	JSONObject temp = json.getJSONObject(i);
	    	String jsondata = temp.toString();
	    	JSONObject data = new JSONObject(jsondata);
	    	String countystring = (String) data.get("鄉鎮市區");
	    	Matcher countymatcher = county.matcher(countystring);
	    	String roadstring = (String) data.get("土地區段位置或建物區門牌");
	    	Matcher roadmatcher = roadname.matcher(roadstring);
	    	int tradeyear = data.getInt("交易年月");
	    	
	    	if(countymatcher.find())
	    	{
	    		if(roadmatcher.find())
	    		{
	    			int year = Integer.parseInt(args[3]);
	    			year = year * 100;
	    			if(tradeyear - year > 0)
	    			{
	    				int tradevalue = data.getInt("總價元");
	    				value = value + tradevalue;
	    				counter++;
	    			}
	    		}
	    	}
	    }
	    System.out.println(value/counter);
	}
}
