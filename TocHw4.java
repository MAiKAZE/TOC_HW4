import java.io.*;
import java.util.*;
import java.net.*;
import org.json.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.Charset;

public class TocHW4
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
		int maxcounter = 0, index = 0, tempprice = 0;
		boolean appear = false, yearcheck = false;
		JSONArray json = RealPriceSearch.readJsonFromUrl(args[0]);
	    Pattern avenue = Pattern.compile(".*大道");
	    Pattern road = Pattern.compile(".*路");
	    Pattern street = Pattern.compile(".*街");
	    Pattern lane = Pattern.compile(".*巷");
	    List<String> roadnamelist = new ArrayList<String>();
	    List<Integer> counter = new ArrayList<Integer>();
	    List<List<Integer>> yearlist = new ArrayList<List<Integer>>();
	    List<Integer> maxprice = new ArrayList<Integer>();
	    List<Integer> minprice = new ArrayList<Integer>();
	    List<Integer> indexlist = new ArrayList<Integer>();   
	    
	    for(int i = 0; i < json.length(); i++)
	    {
	    	JSONObject temp = json.getJSONObject(i);
	    	String jsondata = temp.toString();
	    	JSONObject data = new JSONObject(jsondata);
	    	
	    	String roadstring = (String) data.get("土地區段位置或建物區門牌");
	    	int tradeyear = data.getInt("交易年月");
	    	int tradeprice = data.getInt("總價元");
	    	Matcher avenuematcher = avenue.matcher(roadstring);
	    	Matcher roadmatcher = road.matcher(roadstring);
	    	Matcher streetmatcher = street.matcher(roadstring);
	    	Matcher lanematcher = lane.matcher(roadstring);
	    	if(avenuematcher.find())
	    	{
	    		if(roadnamelist.isEmpty())
	    		{
	    			List<Integer> tradeyearlist1 = new ArrayList<Integer>();
		    		roadnamelist.add(avenuematcher.group());
		    		counter.add(1);
		    		tradeyearlist1.add(tradeyear);
		    		yearlist.add(tradeyearlist1);
		    		maxprice.add(tradeprice);
		    		minprice.add(0);
		    		continue;
	    		}
	    		else
	    		{
		    		for(int j = 0; j < roadnamelist.size(); j++)
		    		{
		    			if(roadnamelist.get(j).equals(avenuematcher.group()))
		    			{		    				
		    				appear = true;
		    				index = j;
		    				break;
		    			}
		    		}
		    		if(!appear)
		    		{
		    			List<Integer> tradeyearlist2 = new ArrayList<Integer>();
			    		roadnamelist.add(avenuematcher.group());
			    		counter.add(1);
			    		tradeyearlist2.add(tradeyear);
			    		yearlist.add(tradeyearlist2);
			    		maxprice.add(tradeprice);
			    		minprice.add(0);
			    		continue;
		    		}
		    		else
		    		{
		    			for(int k = 0; k < yearlist.get(index).size(); k++)
		    			{
		    				if(yearlist.get(index).get(k).equals(tradeyear))
		    				{
		    					yearcheck = true;
		    					break;
		    				}
		    			}
		    			if(!yearcheck)
		    			{
		    				List<Integer> tradeyearlist3 = new ArrayList<Integer>();
		    				for(int l = 0; l < yearlist.get(index).size(); l++)
		    					tradeyearlist3.add(yearlist.get(index).get(l));
	    					int tempcounter = counter.get(index);
	    					tempcounter = tempcounter + 1;
	    	    			counter.set(index, tempcounter);
	    	    			tradeyearlist3.add(tradeyear);
	    		    		yearlist.set(index, tradeyearlist3);
		    			}
		    		}
	    		}
		    	int tempmax = (Integer) maxprice.get(index);
		    	int tempmin = (Integer) minprice.get(index);
		    	if(tradeprice - tempmax > 0)
		    	{
		    		tempprice = tempmax;
		    		maxprice.set(index, tradeprice);	    				
		    		minprice.set(index, tempprice);
		    	}
		    	else if(tradeprice - tempmin > 0)
		    		minprice.set(index, tradeprice);
		    	else ;
		    	appear = false;
		    	yearcheck = false;
		    	tempprice = 0;
	    		continue;
	    	}
	    	if(roadmatcher.find())
	    	{
	    		if(roadnamelist.isEmpty())
	    		{
	    			List<Integer> tradeyearlist1 = new ArrayList<Integer>();
		    		roadnamelist.add(roadmatcher.group());
		    		counter.add(1);
		    		tradeyearlist1.add(tradeyear);
		    		yearlist.add(tradeyearlist1);
		    		maxprice.add(tradeprice);
		    		minprice.add(0);
		    		continue;
	    		}
	    		else
	    		{
		    		for(int j = 0; j < roadnamelist.size(); j++)
		    		{
		    			if(roadnamelist.get(j).equals(roadmatcher.group()))
		    			{		    				
		    				appear = true;
		    				index = j;
		    				break;
		    			}
		    		}
		    		if(!appear)
		    		{
		    			List<Integer> tradeyearlist2 = new ArrayList<Integer>();
			    		roadnamelist.add(roadmatcher.group());
			    		counter.add(1);
			    		tradeyearlist2.add(tradeyear);
			    		yearlist.add(tradeyearlist2);
			    		maxprice.add(tradeprice);
			    		minprice.add(0);
			    		continue;
		    		}
		    		else
		    		{
		    			for(int k = 0; k < yearlist.get(index).size(); k++)
		    			{
		    				if(yearlist.get(index).get(k).equals(tradeyear))
		    				{
		    					yearcheck = true;
		    					break;
		    				}
		    			}
		    			if(!yearcheck)
		    			{
		    				List<Integer> tradeyearlist3 = new ArrayList<Integer>();
		    				for(int l = 0; l < yearlist.get(index).size(); l++)
		    					tradeyearlist3.add(yearlist.get(index).get(l));
	    					int tempcounter = counter.get(index);
	    					tempcounter = tempcounter + 1;
	    	    			counter.set(index, tempcounter);
	    	    			tradeyearlist3.add(tradeyear);
	    		    		yearlist.set(index, tradeyearlist3);
		    			}
		    		}
	    		}
		    	int tempmax = (Integer) maxprice.get(index);
		    	int tempmin = (Integer) minprice.get(index);
		    	if(tradeprice - tempmax > 0)
		    	{
		    		tempprice = tempmax;
		    		maxprice.set(index, tradeprice);	    				
		    		minprice.set(index, tempprice);
		    	}
		    	else if(tradeprice - tempmin > 0)
		    		minprice.set(index, tradeprice);
		    	else ;
		    	appear = false;
		    	yearcheck = false;
		    	tempprice = 0;
	    		continue;
	    	}
	    	if(streetmatcher.find())
	    	{
	    		if(roadnamelist.isEmpty())
	    		{
	    			List<Integer> tradeyearlist1 = new ArrayList<Integer>();
		    		roadnamelist.add(streetmatcher.group());
		    		counter.add(1);
		    		tradeyearlist1.add(tradeyear);
		    		yearlist.add(tradeyearlist1);
		    		maxprice.add(tradeprice);
		    		minprice.add(0);
		    		continue;
	    		}
	    		else
	    		{
		    		for(int j = 0; j < roadnamelist.size(); j++)
		    		{
		    			if(roadnamelist.get(j).equals(streetmatcher.group()))
		    			{		    				
		    				appear = true;
		    				index = j;
		    				break;
		    			}
		    		}
		    		if(!appear)
		    		{
		    			List<Integer> tradeyearlist2 = new ArrayList<Integer>();
			    		roadnamelist.add(streetmatcher.group());
			    		counter.add(1);
			    		tradeyearlist2.add(tradeyear);
			    		yearlist.add(tradeyearlist2);
			    		maxprice.add(tradeprice);
			    		minprice.add(0);
			    		continue;
		    		}
		    		else
		    		{
		    			for(int k = 0; k < yearlist.get(index).size(); k++)
		    			{
		    				if(yearlist.get(index).get(k).equals(tradeyear))
		    				{
		    					yearcheck = true;
		    					break;
		    				}
		    			}
		    			if(!yearcheck)
		    			{
		    				List<Integer> tradeyearlist3 = new ArrayList<Integer>();
		    				for(int l = 0; l < yearlist.get(index).size(); l++)
		    					tradeyearlist3.add(yearlist.get(index).get(l));
	    					int tempcounter = counter.get(index);
	    					tempcounter = tempcounter + 1;
	    	    			counter.set(index, tempcounter);
	    	    			tradeyearlist3.add(tradeyear);
	    		    		yearlist.set(index, tradeyearlist3);
		    			}
		    		}
	    		}
		    	int tempmax = (Integer) maxprice.get(index);
		    	int tempmin = (Integer) minprice.get(index);
		    	if(tradeprice - tempmax > 0)
		    	{
		    		tempprice = tempmax;
		    		maxprice.set(index, tradeprice);	    				
		    		minprice.set(index, tempprice);
		    	}
		    	else if(tradeprice - tempmin > 0)
		    		minprice.set(index, tradeprice);
		    	else ;
		    	appear = false;
		    	yearcheck = false;
		    	tempprice = 0;
	    		continue;
	    	}
	    	if(lanematcher.find())
	    	{
	    		if(roadnamelist.isEmpty())
	    		{
	    			List<Integer> tradeyearlist1 = new ArrayList<Integer>();
		    		roadnamelist.add(lanematcher.group());
		    		counter.add(1);
		    		tradeyearlist1.add(tradeyear);
		    		yearlist.add(tradeyearlist1);
		    		maxprice.add(tradeprice);
		    		minprice.add(0);
		    		continue;
	    		}
	    		else
	    		{
		    		for(int j = 0; j < roadnamelist.size(); j++)
		    		{
		    			if(roadnamelist.get(j).equals(lanematcher.group()))
		    			{		    				
		    				appear = true;
		    				index = j;
		    				break;
		    			}
		    		}
		    		if(!appear)
		    		{
		    			List<Integer> tradeyearlist2 = new ArrayList<Integer>();
			    		roadnamelist.add(lanematcher.group());
			    		counter.add(1);
			    		tradeyearlist2.add(tradeyear);
			    		yearlist.add(tradeyearlist2);
			    		maxprice.add(tradeprice);
			    		minprice.add(0);
			    		continue;
		    		}
		    		else
		    		{
		    			for(int k = 0; k < yearlist.get(index).size(); k++)
		    			{
		    				if(yearlist.get(index).get(k).equals(tradeyear))
		    				{
		    					yearcheck = true;
		    					break;
		    				}
		    			}
		    			if(!yearcheck)
		    			{
		    				List<Integer> tradeyearlist3 = new ArrayList<Integer>();
		    				for(int l = 0; l < yearlist.get(index).size(); l++)
		    					tradeyearlist3.add(yearlist.get(index).get(l));
	    					int tempcounter = counter.get(index);
	    					tempcounter = tempcounter + 1;
	    	    			counter.set(index, tempcounter);
	    	    			tradeyearlist3.add(tradeyear);
	    		    		yearlist.set(index, tradeyearlist3);
		    			}
		    		}
	    		}
		    	int tempmax = (Integer) maxprice.get(index);
		    	int tempmin = (Integer) minprice.get(index);
		    	if(tradeprice - tempmax > 0)
		    	{
		    		tempprice = tempmax;
		    		maxprice.set(index, tradeprice);	    				
		    		minprice.set(index, tempprice);
		    	}
		    	else if(tradeprice - tempmin > 0)
		    		minprice.set(index, tradeprice);
		    	else ;
		    	appear = false;
		    	yearcheck = false;
		    	tempprice = 0;
	    		continue;
	    	}
	    }
	    for(int a = 0; a < counter.size(); a++)
	    {
	    	if(counter.get(a) > maxcounter)
	    	{
	    		maxcounter = counter.get(a);
	    	}
	    }
	    for(int b = 0; b < counter.size(); b++)
	    {
	    	if(counter.get(b) == maxcounter)
	    		indexlist.add(b);
	    }
	    for(int i = 0; i < indexlist.size(); i++)
	    {
	    	int tempnum = indexlist.get(i);
	    	System.out.print(roadnamelist.get(tempnum)+", ");
	    	System.out.print("最高成交價:"+maxprice.get(tempnum)+", ");
	    	System.out.println("最低成交價:"+minprice.get(tempnum));
	    }
	}
}
