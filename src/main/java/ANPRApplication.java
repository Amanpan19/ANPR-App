import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.tess4j.*;


public class ANPRApplication extends JFrame {
    private final JLabel imageLabel;
    private final JTextArea resultTextArea;

    public ANPRApplication() {
        setTitle("Automatic Number Plate Recognition");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.SOUTH);

        JButton openButton = new JButton("Open Image");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openImage();
            }
        });

        add(openButton, BorderLayout.NORTH);// North represent "Top of the frame"
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            displayImage(selectedFile);
            performOCR(selectedFile);
        }
    }

    private BufferedImage processImage(File fileImage){
        try{
            BufferedImage originalImage = ImageIO.read(fileImage);
            // Reading the original image.
            int targetWidth = 600;
            int targetHeight = 400;

            BufferedImage resizeImg = new BufferedImage(targetWidth,targetHeight,originalImage.getType());
            Graphics2D graphics = resizeImg.createGraphics();
            // Graphics 2D allow to perform various operations on image
            graphics.drawImage(originalImage,0,0,targetWidth,targetHeight,null);
            graphics.dispose();

            return resizeImg;

        }catch (Exception e){
           e.printStackTrace();
           return null;
        }
    }


    private void displayImage(File imageFile) {
        try {

            BufferedImage image = processImage(imageFile);
            ImageIcon imageIcon = new ImageIcon(image);
            Image scaledImage = imageIcon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(imageIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void performOCR(File imageFile) {
        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setLanguage("eng");
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata"); // Set the path to the tessdata directory

            String result = tesseract.doOCR(imageFile);
            resultTextArea.setText("OCR Result:\n" + result);
        } catch (TesseractException e) {
            e.printStackTrace();
            resultTextArea.setText("Error performing OCR.");
        }
    }

    public static void main(String[] args) {

                new ANPRApplication().setVisible(true);

    }
}
