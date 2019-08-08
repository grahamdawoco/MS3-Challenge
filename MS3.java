import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javax.swing.*;
public class MS3 {
    private static final char SEPARATOR = ',';
    private static final char QUOTE = '"';
    private static final int COLUMN_SIZE = 10;
    private static final int BATCH_LIMIT = 100;
    private static final String LOG_FILE = "log.txt";
    private static final String SQL = "INSERT INTO employeeData(A,B,C,D,E,F,G,H,I,j) VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String SQL_CREATE = "CREATE TABLE employeeData"
            + "("
            + " A string,"
            + " B string,"
            + " C string,"
            + " D string,"
            + " E string,"
            + " F string,"
            + " G string,"
            + " H string,"
            + " I string,"
            + " J string"
            + ");";

    public static void main(String args[])
    {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String outFileName = "badCSV-" + dtf.format(now) + ".csv";

        //Creating the Frame
        JFrame frame = new JFrame("CSV Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);

        //Creating the panels and adding components
        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel();// the panel is not visible in output
        JLabel topLabel = new JLabel("Enter file name");
        JTextField fileNameTextField = new JTextField(15);
        JButton button = new JButton("Run Tool");

        // Components Added using Flow Layout
        topPanel.add(topLabel);
        topPanel.add(fileNameTextField);
        centerPanel.add(button);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, topPanel);
        frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
        frame.setVisible(true);

        button.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {

                //initialize columnNumber at 1 because csv does not start at 0

                int columnNumber = 1, goodResults = 0, badResults = 0, batchCounter = 0, total = 0;
                boolean quoteInColumn = false, firstQuote= true, rejected = false;

                String fileName = fileNameTextField.getText();
                String csvLine = "";
                String inputFilePath = new File(fileName).getAbsolutePath();
                File workingFile = new File(inputFilePath);
                StringBuilder data = new StringBuilder();
                String[] column = new String[COLUMN_SIZE];

                try (Scanner scanner = new Scanner(workingFile, "UTF-8"); //try with resources to manage closing connections
                     Connection conn = SQLconnecter.connect();) {

                    Statement create = conn.createStatement();
                    create.execute(SQL_CREATE);
                    PreparedStatement pstmt = conn.prepareStatement(SQL);

                    while (scanner.hasNextLine()) { // loop through file by line
                        total++;
                        csvLine = scanner.nextLine();
                        for (int i =0; i < csvLine.length(); i++) { //loop through line by character
                            char c = csvLine.charAt(i);
                            data.append(c);

                            if(columnNumber > COLUMN_SIZE) {
                                rejected = true;
                                break; //break out of loop this is rejected
                            }

                            if (c == SEPARATOR && !quoteInColumn) {
                                if(data.toString().contentEquals(",")){
                                    rejected = true;
                                    break; //break out of loop this is rejected || blank column ||
                                }
                                //I remove 1 from columnNumber to match array index
                                //and remove 1 from data to remove the comma
                                column[columnNumber - 1] = data.substring(0, data.length()-1);
                                data = new StringBuilder();
                                columnNumber++;
                            }

                            if (i == csvLine.length()) {
                                column[columnNumber - 1] = data.substring(0, data.length()-1);
                            }

                            if (c == QUOTE) {
                                if(firstQuote) {
                                    data = new StringBuilder();
                                    firstQuote = false;
                                }
                                else {
                                    //removes the final quote
                                    data.deleteCharAt(data.length()-1);
                                }

                                quoteInColumn = !quoteInColumn; //flip the flag boolean
                            }
                        }//end for

                        if(columnNumber < COLUMN_SIZE) {
                            rejected = true;
                        }

                        if(rejected) {
                            writeLineToFile(outFileName, csvLine);
                            badResults++;
                        }
                        else {
                            //setting the prepared statement
                            pstmt.setString(1, column[0]);
                            pstmt.setString(2, column[1]);
                            pstmt.setString(3, column[2]);
                            pstmt.setString(4, column[3]);
                            pstmt.setString(5, column[4]);
                            pstmt.setString(6, column[5]);
                            pstmt.setString(7, column[6]);
                            pstmt.setString(8, column[7]);
                            pstmt.setString(9, column[8]);
                            pstmt.setString(10, column[9]);
                            pstmt.addBatch();

                            //processing batch
                            if(batchCounter > BATCH_LIMIT) {
                                pstmt.executeBatch();
                                batchCounter = 0;
                            }

                            goodResults++;
                            batchCounter++;
                        }

                        //reset variables
                        columnNumber = 1;
                        data = new StringBuilder();
                        rejected = false;

                    }//end while
                    pstmt.executeBatch();// process final batch

                    String t =  dtf.format(now) + " Records recieved: " + total +
                            " Records sucessful: " + goodResults + " Records Failed: " + badResults;
                    writeLineToFile(LOG_FILE, t);

                    System.out.println("finished");

                }
                catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });// end actionListener
    }//end main

    //this helper method writes a line to a file
    private static void writeLineToFile(String fileName, String line) {

        File file = new File(fileName);
        try (FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
             BufferedWriter bw = new BufferedWriter(fw); ){
            bw.append(line + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end writeLineToFile
}//end class

