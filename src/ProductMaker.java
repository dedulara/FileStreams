import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ProductMaker extends JFrame
{
    JPanel wholePanel, buttonPanel, productPanel, productPanelOne, productPanelTwo, productPanelThree;

    JButton addButton, quitButton;

    JLabel nameL, descriptionL, idL, costL;

    JTextField nameTF, descriptionTF, idTF, costTF, recordCountTF;

    Font fontM = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    Product newProduct;

    ArrayList<Product> productList = new ArrayList<>();

    int recordNumber = 0;

    public ProductMaker()
    {
        wholePanel = new JPanel();
        createProductPanel();
        wholePanel.add(productPanel);
        createAddRecordButtonPanel();
        wholePanel.add(productPanelThree);
        add(wholePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(800,200);
    }

    public void createProductPanel()
    {
        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(2,1));
        createProductOne();
        productPanel.add(productPanelOne);
        createProductTwo();
        productPanel.add(productPanelTwo);
    }

    public void createProductOne()
    {
        productPanelOne = new JPanel();
        idL = new JLabel("Product ID: ", JLabel.RIGHT);
        idTF = new JTextField(7);
        nameL = new JLabel("   Product Name: ", JLabel.RIGHT);
        nameTF = new JTextField(36);
        costL = new JLabel("   Product Cost: $", JLabel.RIGHT);
        costTF = new JTextField(7);

        idL.setFont(fontM);
        idTF.setFont(fontM);
        nameL.setFont(fontM);
        nameTF.setFont(fontM);
        costL.setFont(fontM);
        costTF.setFont(fontM);

        productPanelOne.add(idL);
        productPanelOne.add(idTF);
        productPanelOne.add(nameL);
        productPanelOne.add(nameTF);
        productPanelOne.add(costL);
        productPanelOne.add(costTF);
    }

    public void createProductTwo()
    {
        productPanelTwo = new JPanel();
        descriptionL = new JLabel("Product Description: ", JLabel.RIGHT);
        descriptionTF = new JTextField(80);

        descriptionL.setFont(fontM);
        descriptionTF.setFont(fontM);

        productPanelTwo.add(descriptionL);
        productPanelTwo.add(descriptionTF);
    }

    public void createAddRecordButtonPanel()
    {
        productPanelThree = new JPanel();
        addButton = new JButton("Add Product");
        addButton.addActionListener((ActionEvent ae) ->
        {
            if(nameTF.getText().equals("") || idTF.getText().equals("") || descriptionTF.getText().equals("") || costTF.getText().equals("")){JOptionPane.showMessageDialog(null, "Must fill out all fields");}
            else if (idTF.getText().length() > 6) {JOptionPane.showMessageDialog(null, "Product ID cannot have a length more than 6");}
            else if (nameTF.getText().length() > 35) {JOptionPane.showMessageDialog(null, "Product name must be 35 characters or less");}
            else if (descriptionTF.getText().length() > 75) {JOptionPane.showMessageDialog(null, "Product description must be 75 characters or less");}
            else if (!isDouble(costTF.getText())){JOptionPane.showMessageDialog(null, "Product cost must be a number");}
            else
            {
                newProduct = new Product(idTF.getText(), nameTF.getText(), descriptionTF.getText(), Double.parseDouble(costTF.getText()));
                productList.add(newProduct);
                idTF.setText("");
                nameTF.setText("");
                descriptionTF.setText("");
                costTF.setText("");
                recordNumber++;
                recordCountTF.setText("Record: " + recordNumber);
                //System.out.println(productList);

                try (RandomAccessFile file = new RandomAccessFile("ProductData.bin", "rw"))
                {
                    for (Product writeProduct : productList)
                    {
                        file.writeBytes(String.format("%6s",writeProduct.getIdString()));
                        file.writeBytes(String.format("%35s",writeProduct.getNameString()));
                        file.writeBytes(String.format("%75s",writeProduct.getDescriptString()));
                        file.writeDouble(writeProduct.getCostDouble());
                    }
                }
                catch (IOException e) {e.printStackTrace();}
            }
        });
        productPanelThree.setLayout(new GridLayout(1,3));

        recordCountTF = new JTextField();
        recordCountTF.setFont(fontM);
        recordCountTF.setText("Record: " + recordNumber);
        recordCountTF.setEditable(false);
        productPanelThree.add(recordCountTF);

        productPanelThree.add(addButton);

        quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent ae) -> System.exit(0));
        productPanelThree.add(quitButton);

        quitButton.setFont(fontM);
        addButton.setFont(fontM);

    }

    public void createButtonPanel()
    {
        buttonPanel = new JPanel();
    }

    public boolean isDouble(String isThisADouble)
    {
        try{double checkDouble = Double.parseDouble(isThisADouble); return true;}
        catch(NumberFormatException nfe){return false;}
    }

    private static String readFixedLengthString(RandomAccessFile file, int length) throws IOException
    {
        byte[] bytes = new byte[length];
        file.read(bytes);
        return new String(bytes).trim();
    }
}
