import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Database {
    private static Database INSTANCE;
    private Connection connection;
    private Database(){

    }

    public static Database getINSTANCE(){
        if (INSTANCE == null){
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    public void startConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:.\\contact.db");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception exception) {
            exception.getMessage();
        }
    }

    public void addNewContact(int id, String firstName, String lastName, int age, String phone) throws SQLException{
        try
        {
            this.startConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO contacts(id, firstName, lastName, age, phone)" +
                    "VALUES(" + id + ", '" + firstName + "', '" + lastName + "', " + age + ", '" + phone + "')");

            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }
    }

    public void updateContact(int id, String firstName, String lastName, int age, String phone) throws SQLException{
        try
        {
            this.startConnection();
            Statement statement = connection.createStatement();
            statement.execute("UPDATE contacts SET firstName='"+firstName+"', lastName='"+lastName+"', age="+age+", phone='"+phone+"'" +
                    "WHERE id = "+id);

            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }
    }

    public HashMap<String, String> getContactInfoByName(String firstName) throws SQLException{
        HashMap<String, String> contactInfoMap = new HashMap();

        try
        {
            this.startConnection();
            Statement statement = connection.createStatement();

            statement.execute("SELECT * FROM contacts WHERE firstName = '"+firstName+"'");
            ResultSet result = statement.getResultSet();

            contactInfoMap.put("Id", String.valueOf(result.getInt("id")));
            contactInfoMap.put("firstName", result.getString("firstName"));
            contactInfoMap.put("lastName", result.getString("lastName"));
            contactInfoMap.put("age", String.valueOf(result.getString("age")));
            contactInfoMap.put("phone", result.getString("phone"));

            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }

        return contactInfoMap;
    }

    public HashMap<String, String> getContactInfoById(int id) throws SQLException{
        HashMap<String, String> contactInfoMap = new HashMap();

        try
        {
            this.startConnection();
            Statement statement = connection.createStatement();

            statement.execute("SELECT * FROM contacts WHERE id = "+id);
            ResultSet result = statement.getResultSet();

            contactInfoMap.put("Id", String.valueOf(result.getInt("id")));
            contactInfoMap.put("firstName", result.getString("firstName"));
            contactInfoMap.put("lastName", result.getString("lastName"));
            contactInfoMap.put("age", String.valueOf(result.getString("age")));
            contactInfoMap.put("phone", result.getString("phone"));

            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }

        return contactInfoMap;
    }

    public ArrayList<Integer> getAllContactId() throws SQLException{
        ArrayList<Integer> idArrayList = new ArrayList<>();

        try
        {
            this.startConnection();
            Statement statement = connection.createStatement();

            statement.execute("SELECT id FROM contacts");
            ResultSet result = statement.getResultSet();

            while(result.next()){
                idArrayList.add(result.getInt("id"));
            }

            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }

        return idArrayList;
    }

    public int getNextAvailableId() throws SQLException{
        int nextId;
        Stack<Integer> idStack = new Stack<>();


        try
        {
            this.startConnection();
            Statement statement = connection.createStatement();

            statement.execute("SELECT id FROM contacts");
            ResultSet result = statement.getResultSet();

            while(result.next()){
                idStack.push(result.getInt("id"));
            }

            statement.close();
            connection.close();
        }
        catch (SQLException e)
        {
            throw new SQLException();
        }

        nextId = idStack.pop() + 1;

        return nextId;
    }
}
