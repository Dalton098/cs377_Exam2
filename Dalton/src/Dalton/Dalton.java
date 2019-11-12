package Dalton;

import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Dalton Rothenberger
 */
public class Dalton {

    private static Connection conn;
    private static Statement stmt;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        establishConnectionToDatabase();
        stmt = conn.createStatement();

        displayNumReferrals();

        completeReferralId(0, 9, 4);
        
        deleteReferralType(2);
        
        deleteReferralType(3);
        
        stmt.close();
        conn.close();
    }

    private static void displayNumReferrals() {

        try {
            establishConnectionToDatabase();
            stmt = conn.createStatement();
            String query = "SELECT rt.[ReferralTypeName], Count(rf.[RTID]) as 'Number Of Referrals' "
                    + "FROM [Dalton].[dbo].[ReferralType] rt "
                    + "LEFT JOIN [Dalton].[dbo].[Referrals] rf on rf.[RTID] = rt.[ReferralTypeID] "
                    + "GROUP BY rt.[ReferralTypeName]";

            ResultSet rs = executeQueryStatement(query);
            displayQueryResults(rs);

        } catch (Exception e) {
            System.out.println("ERROR: Could not query the database");
        }

    }

    private static void completeReferralId(int IsCurrentStatus, int ReferralStatusId, int ReferralId) {

        try {
            establishConnectionToDatabase();
            stmt = conn.createStatement();

//          Update the old entry to not be flagged as current status
            String query = "Update [Dalton].[dbo].[ReferralStatus] "
                    + "Set IsCurrentStatus = ? "
                    + "Where [Dalton].[dbo].[ReferralStatus].[ReferralStatusID] = ? ";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, IsCurrentStatus);
            pstmt.setInt(2, ReferralStatusId);

            int result = pstmt.executeUpdate();

//          Insert completed record
            String date = "2019-11-12 10:20:00";

            String sql = "INSERT INTO ReferralStatus Values(5, 1, ?, ?)";

            PreparedStatement pstmt2 = conn.prepareStatement(sql);

            pstmt2.setString(1, date);
            pstmt2.setInt(2, ReferralId);

            result += pstmt2.executeUpdate();

            System.out.println(result + " records affected");

        } catch (Exception e) {
            System.out.println("ERROR: Could not complete the given referral id");
        }

    }

    private static void deleteReferralType(int ReferralTypeId) {

        try {
            establishConnectionToDatabase();
            stmt = conn.createStatement();

//            Clearing the referral status table
            String query = "Delete from [Dalton].[dbo].[ReferralStatus] "
                    + "From [Dalton].[dbo].[ReferralStatus] rs "
                    + "Inner Join [Dalton].[dbo].[Referrals] rf on rs.RID = rf.ReferralID "
                    + "Where rf.RTID = ? ";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ReferralTypeId);
            int result = pstmt.executeUpdate();

//            Clearing the referrals table
            query = "Delete from [Dalton].[dbo].[Referrals] "
                    + "Where [Dalton].[dbo].[Referrals].[RTID] = ? ";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ReferralTypeId);
            result += pstmt.executeUpdate();

//            Clearing the workers table
            query = "Delete from [Dalton].[dbo].[Workers] "
                    + "Where [Dalton].[dbo].[Workers].[RTID] = ? ";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ReferralTypeId);
            result += pstmt.executeUpdate();
            
//            Clearing the referral type table
            query = "Delete from [Dalton].[dbo].[ReferralType] "
                    + "Where [Dalton].[dbo].[ReferralType].[ReferralTypeID] = ? ";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ReferralTypeId);
            result += pstmt.executeUpdate();

            System.out.println(result + " results affected");
            
        } catch (Exception e) {
            System.out.println("ERROR: Could not properly delete the given referral type id from the database");
        }

    }

    private static void establishConnectionToDatabase() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            Uncomment this to test (I am on linux so I have to use a different connection url
//            String connectionUrl = "jdbc:sqlserver://localhost;integratedSecurity=true;";
            String connectionUrl = "jdbc:sqlserver://localhost;databaseName=Dalton;integratedSecurity=false;user=sa;password=reallyStrongPwd123";
            conn = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static ResultSet executeQueryStatement(String s) throws SQLException {
        ResultSet rs;
        rs = stmt.executeQuery(s);
        return rs;
    }

    private static void displayQueryResults(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        int numOfCols = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i < numOfCols + 1; i++) {
                System.out.println((rsmd.getColumnLabel(i) + ": " + rs.getString(i)
                        + "   "));
            }
            System.out.println();
        }
    }
}
