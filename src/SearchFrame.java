import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class SearchFrame extends JFrame
{
    JPanel wholePanel, buttonPanel, searchPanel, taPanel;

    JTextField searchTF;

    JTextArea searchTA;

    JButton searchButton, quitButton;

    Font fontM = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    public SearchFrame()
    {
        wholePanel = new JPanel();
        wholePanel.setLayout(new BorderLayout());
        createSearchPanel();
        wholePanel.add(searchPanel, BorderLayout.NORTH);
        createSearchTwoPanel();
        wholePanel.add(taPanel, BorderLayout.CENTER);
        createButtonPanel();
        wholePanel.add(buttonPanel, BorderLayout.SOUTH);
        add(wholePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(1000,300);
    }

    public void createSearchPanel()
    {
        searchPanel = new JPanel();
        searchTF = new JTextField(36);
        searchTF.setFont(fontM);
        searchPanel.add(searchTF);

        searchButton = new JButton("Search For Product");
        searchButton.addActionListener((ActionEvent ae) ->
        {
            searchTA.setText("");
            Map<String, Integer> productLine = new TreeMap<>();
            try (RandomAccessFile file = new RandomAccessFile("ProductData.bin", "r"))
            {
                int lengthInt = (int) file.length() / (6+35+75+8);
                for (int i = 0; i < lengthInt; i++)
                {
                    file.seek(6+(124*i));
                    String searchName = readFixedLengthString(file, 35).trim();
                    productLine.put(searchName, i);
                }
            }
            catch (IOException e) {e.printStackTrace();}

            String searchProduct = searchTF.getText();
            int notFoundInt = 0;
            for(String keyString : productLine.keySet())
            {
                boolean findProduct = keyString.contains(searchProduct);
                if(findProduct)
                {
                    try (RandomAccessFile file = new RandomAccessFile("ProductData.bin", "r"))
                    {
                        file.seek(124 * productLine.get(keyString));
                        String idString = readFixedLengthString(file, 6);
                        String nameString = readFixedLengthString(file, 35);
                        String descriptionString = readFixedLengthString(file, 75);
                        double costDouble = file.readDouble();

                        String formatID = "";
                        for(int i = idString.length(); i < 8; i++){formatID += " ";};
                        idString += formatID;

                        String formatName = "";
                        for(int i = nameString.length(); i < 35; i++){formatName += " ";};
                        nameString += formatName;

                        String formatDescription = "";
                        for(int i = descriptionString.length(); i < 75; i++){formatDescription += " ";};
                        descriptionString += formatDescription;

                        String formatCost = "";
                        String costString = String.valueOf(costDouble);
                        for(int i = costString.length(); i < 8; i++){formatCost += " ";};

                        searchTA.append(idString + nameString + descriptionString + "$" + formatCost + costDouble + "\n");
                    }
                    catch (IOException e) {e.printStackTrace();}
                }
                else{notFoundInt++;}
            }
            if(notFoundInt == 5){searchTA.setText("Product not found");}
        });
        searchPanel.add(searchButton);
        searchButton.setFont(fontM);
    }

    public void createSearchTwoPanel()
    {
        taPanel = new JPanel();

        searchTA = new JTextArea(11,135);
        searchTA.setFont(fontM);
        searchPanel.add(searchTA);
        searchTA.setEditable(false);
    }

    public void createButtonPanel()
    {
        buttonPanel = new JPanel();
        quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent ae) -> System.exit(0));
        buttonPanel.add(quitButton);
        quitButton.setFont(fontM);
    }

    private static String readFixedLengthString(RandomAccessFile file, int length) throws IOException
    {
        byte[] bytes = new byte[length];
        file.read(bytes);
        return new String(bytes).trim();
    }
}
