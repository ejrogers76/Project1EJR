package project1EJR;

import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Database {

    String sessionID;
    
    public String getResults(String sessionID) throws ServletException, IOException {
        this.sessionID = sessionID;
        String table = "";
        String query;
        
        Connection conn = getConnection();
        
        query = "SELECT * FROM registrations r WHERE sessionID = ?;";
        
        try { 
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, sessionID);
            boolean hasResults = statement.execute();
            
            if(hasResults) {
                ResultSet resultSet = statement.getResultSet();
                table += "<table>";
                while(resultSet.next()){
                    table += "<tr> <td>";
                    table += resultSet.getString("id");
                    table += "</td> <td>";
                    table += resultSet.getString("firstname");
                    table += "</td> <td>";
                    table += resultSet.getString("lastname");
                    table += "</td> <td>";
                    table += resultSet.getString("displayname");
                    table += "</td> <td>";
                    table += sessionID + "</td> </tr>";
                }
                
                table += "</table>";
            }
        }
        
        catch(Exception e){System.err.println(e);}
        return table;
    }
    
    public Connection getConnection() { 
        Connection conn = null;
        
        try {
            
            Context envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)initContext.lookup("jdbc/db_pool");
            conn = ds.getConnection();
            
        }   
        
        catch (Exception e) { e.printStackTrace(); }
        
        return conn;

    }
    
    public String register(String first, String last, String display, String session) {
        
        String query;
        String id = "";
        String registerCode;
        ResultSet keys;
        JSONObject results = new JSONObject();
        
        Connection conn = getConnection();
        query = ("INSERT INTO registrations (firstname, lastname, displayname, sessionid) values (?, ?, ?, ?);");
        
        try {
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, first);
            statement.setString(2, last);
            statement.setString(3, display);
            statement.setString(4, session);
            
            int test = statement.executeUpdate();
            if(test == 1){
                keys = statement.getGeneratedKeys();
                if(keys.next()){
                    id = keys.getString(1);
                }
            }
            
            registerCode = String.format("R" + "%0" + (6 - id.length()) + "d%s", 0, id);
            results.put("display", display);
            results.put("registerCode", registerCode);
        }
        
        catch(Exception e){}
        
        return (results.toJSONString());
        
    }

}
