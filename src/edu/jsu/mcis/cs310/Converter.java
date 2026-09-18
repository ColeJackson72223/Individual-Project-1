package edu.jsu.mcis.cs310;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
        
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> csv = reader.readAll();
            
            String[] headings = csv.get(0);
            
            JsonArray prodNums = new JsonArray();
            JsonArray colHeadings = new JsonArray();
            JsonArray data = new JsonArray();
            
            for (String heading : headings){
                colHeadings.add(heading);
            }
            
            for (int i = 1; i < csv.size(); i++){
                String[] row = csv.get(i);
                
                prodNums.add(row[0]);
                
                JsonArray dataLine = new JsonArray(); 
                
                dataLine.add(row[1]);
                dataLine.add(Integer.parseInt(row[2]));
                dataLine.add(Integer.parseInt(row[3]));
                dataLine.add(row[4]);
                dataLine.add(row[5]);
                dataLine.add(row[6]);
                
                data.add(dataLine);
                
            }
            
            JsonObject json = new JsonObject();
            
            json.put("ProdNums", prodNums);
            json.put("ColHeadings", colHeadings);
            json.put("Data", data);
            
            result = Jsoner.serialize(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            JsonObject json = Jsoner.deserialize(jsonString, new JsonObject());
            
            JsonArray prodNums = (JsonArray)json.get("ProdNums");
            JsonArray colHeadings = (JsonArray) json.get("ColHeadings");
            JsonArray data = (JsonArray) json.get("Data");
            
            StringWriter copy = new StringWriter();
            CSVWriter writer = new CSVWriter(copy);
            
            
            //for headings or Col
            String[] headings = new String[colHeadings.size()];
            for (int i = 0; i < colHeadings.size(); i++){
                headings[i] = colHeadings.getString(i);
            }
            writer.writeNext(headings); // row?
           
            
            // For data
            for (int i = 0; i < data.size(); i++){
                JsonArray dataLine = (JsonArray) data.get(i);
                
                String[] row = new String[7];
                
                row[0] = prodNums.getString(i);
                
                row[1] = dataLine.getString(0);
                
                row[2] = dataLine.get(1).toString();
                // Doesn't work fix this? // find out how to get the 0 infront to match json
                row[3] = String.format("%02d", dataLine.getInteger(2));
                // Finish the rows
                row[4] = dataLine.getString(3);
                row[5] = dataLine.getString(4);
                row[6] = dataLine.getString(5);
              
                writer.writeNext(row);
                
            }     
            
            result = copy.toString();
            copy.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
