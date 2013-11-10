package com.url.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple Web crawler with methods for traversing urls within a url until it reaches a given limit.
 * @author phanisaripalli
 *
 */
public class URLCrawler {
	
	private final static int MAX_URL_COUNT = 1000;
	private static final boolean UNIQUE_URLS = false;
	private static List<String> urls = new ArrayList<String>();
	private static URLConnection connection = null;
	private static List<String> urlsToTraverse = new ArrayList<String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Initial URL
		String urlString = "http://www.bth.se/";

		getURLs(urlString);
		List<String> initialList = new ArrayList<String>();
		initialList.add(urlString);
		try {
			traverseURLs(initialList);
		} catch (Exception e) {
			System.out.println("End of reading");
		}
		System.out.println("total urls " + urls.size());
		
	}
	
	/**
	 * A recursive method to traverse a list Urls(Strings)
	 * @param listOfUrls 
	 * @throws ConcurrentModificationException
	 */
	private static void traverseURLs(List<String> listOfUrls) throws ConcurrentModificationException{
		while (urls.size() < MAX_URL_COUNT) {
			for(String url : listOfUrls) {
				List<String> tmp = getURLs(url); 
				if (tmp.size() + urls.size() > MAX_URL_COUNT) {
					urls.addAll(tmp.subList(0, MAX_URL_COUNT - urls.size()));
					
				} else {
					urls.addAll(tmp);
					urlsToTraverse.addAll(tmp);
					urlsToTraverse.remove(url);
					System.out.println("generated -> " + urls.size());
					traverseURLs(urlsToTraverse);
				}
			}	
		}
			
	}

	/**
	 * Parses a URL and returns a list of urls.
	 * @param urlString An initial Url to parse for the contained links. 
	 * @return returns list of urls found in a given url.
	 */
	private static List<String> getURLs(String urlString) {
		
		List<String> links = new ArrayList<String>();
		if (isValidURL(urlString)) {
			try {
				String p = "href=\"(.*?)\"";
				Pattern pattern = Pattern.compile(p);

				BufferedReader br = new BufferedReader(
				        new InputStreamReader(connection.getInputStream()));
				String line;
				String content = "";
				while ((line = br.readLine()) != null) {
					content += line + "\n";				
				}
				if (content.indexOf("<body") != -1 && content.indexOf("</body>") != -1) {
					content = content.substring(content.indexOf("<body"), content.indexOf("</body>"));
					
					Matcher  matcher = pattern.matcher(content);
					
					while (matcher.find()) {
						String htmlTag = content.substring(matcher.start(), matcher.end());
						String htmlLink = htmlTag.substring(6, htmlTag.length() - 1);
						if (htmlLink.startsWith("http") && !htmlLink.equals(urlString) ) {
							//&& !urls.contains(htmlLink) 
							if (UNIQUE_URLS) {
								if (!urls.contains(htmlLink)) {
									links.add(htmlLink);
								}
							} else {
								links.add(htmlLink);
							}
						}
					}
				}
//				processedUrls = parseHTML(urlString);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			System.out.println("invalid " + urlString);
		}
		return links; 
	}

	
	/**
	 * Performs if a URL is valid.
	 * @param urlString Url.
	 * @return <b>True</b> if the url is valid. 
	 */
	private static boolean isValidURL(String urlString) {
		
		URL u = null;
		int responseCode = -1;

	    try {  
	        u = new URL(urlString);  
	        if (u.openConnection() instanceof HttpURLConnection) {
	        	HttpURLConnection connect = (HttpURLConnection) u.openConnection();
	        	responseCode = connect.getResponseCode();
	        } else if (u.openConnection() instanceof HttpsURLConnection) {
	        	HttpURLConnection connect = (HttpsURLConnection) u.openConnection();
	        	responseCode = connect.getResponseCode();
	        } 
	        if ((responseCode != HttpURLConnection.HTTP_OK)) {
	        	return false;
	        } else {
	        	connection = u.openConnection();
	        	if ((connection == null) 
	        			|| (connection.getContentLength() == -1)) {
	        		return false;
	        	}
	        	
	        }
	    } catch (MalformedURLException e) {  
	        return false;  
	    } catch (UnknownHostException e) {
	    	return false;
		} catch (IOException e) {
			e.printStackTrace();
		} 
	    

	    return true; 
		
	}
	
}
