import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws IOException {

        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);

        File imageFile = fc.getSelectedFile();
        File compressedImageFile = new File(imageFile.getName().substring(0, imageFile.getName().length() - 4) +"_compressed.jpg");

        //Set compression ratio from 0 to 1
        float quality = 0.5f;

        BufferedImage image = ImageIO.read(imageFile);
        OutputStream outputFileStream = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers =  ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = (ImageWriter) writers.next();

        ImageOutputStream imageStream = ImageIO.createImageOutputStream(outputFileStream);
        writer.setOutput(imageStream);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);
        writer.write(null, new IIOImage(image, null, null), param);

        BufferedImage compressedImage = ImageIO.read(compressedImageFile);
        outputFileStream.close();
        imageStream.close();
        writer.dispose();

        Main imageDisplay = new Main(image, image.getHeight(), image.getWidth(), compressedImage);

        System.out.println("Compression Ratio: " +(imageFile.length()/compressedImageFile.length()));
    }

    public Main(BufferedImage image, int height, int width, BufferedImage compressedImage) throws IOException
    {
        ImageIcon icon = new ImageIcon(image);
        ImageIcon icon2 = new ImageIcon(compressedImage);
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(1,2));
        frame.setSize(width*2,height);
        JLabel lbl = new JLabel("Original");
        JLabel lbl2 = new JLabel("Compressed");
        lbl.setIcon(icon);
        lbl2.setIcon(icon2);
        frame.add(lbl);
        frame.add(lbl2);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}