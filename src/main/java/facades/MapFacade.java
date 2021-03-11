/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Tweny
 */
public class MapFacade {

    public String fetchData(String string) throws WebApplicationException {
        try{
        URL url = new URL("http://restcountries.eu/rest/v1/alpha?codes=" + string);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = null;
        if (scan.hasNext()) {
            jsonStr = scan.nextLine();
        }
        scan.close();
        System.out.println(jsonStr);
        return jsonStr;
         } catch (MalformedURLException ex){
             throw new WebApplicationException("This is a MalformedURLException", 404);
         } catch (IOException ex){
             throw new WebApplicationException("This is a MalformedURLException", 404);
         }

    }

    public static void main(String[] args) throws WebApplicationException {
        MapFacade mapFacade = new MapFacade();
        System.out.println(mapFacade.fetchData("de"));
    }

}
