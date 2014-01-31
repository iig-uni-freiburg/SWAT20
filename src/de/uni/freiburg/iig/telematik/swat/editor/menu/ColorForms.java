package de.uni.freiburg.iig.telematik.swat.editor.menu;

import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import java.util.*;
import javax.imageio.ImageIO;
public class ColorForms extends JFrame
{
    LeftPanel leftPanel;
    TopPanel topPanel;
    CanvasPanel canvasPanel;
    JSplitPane wholePanel, bottomPanel;
    
    public static void main (String args[])
    {
        ColorForms cf = new ColorForms ();
    }
    
    
    public ColorForms ()
    {
        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        this.setSize (400, 400);
        
        leftPanel = new LeftPanel ();
        canvasPanel = new CanvasPanel ();
        topPanel = new TopPanel (canvasPanel);
        
        bottomPanel = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, leftPanel, canvasPanel);
        bottomPanel.setDividerLocation (50);
        wholePanel = new JSplitPane (JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        wholePanel.setDividerLocation (70);
        this.getContentPane ().add (wholePanel);
        this.setVisible (true);
        canvasPanel.repaint ();
    }
}
class LeftPanel extends JPanel
{
    public LeftPanel ()
    {        
    }
}
class TopPanel extends JPanel implements ActionListener
{
    JButton createNewImageButton;
    JButton saveImageToFileButton;
    JButton retrieveImageFromFileButton;
    CanvasPanel cp;
    
    public TopPanel (CanvasPanel canPan)
    {
        cp = canPan;
        createNewImageButton = new JButton ("Create New Image");
        saveImageToFileButton = new JButton ("Save Image To File");
        retrieveImageFromFileButton = new JButton ("Retrieve Image From File");
        this.add (createNewImageButton);
        this.add (saveImageToFileButton);
        this.add (retrieveImageFromFileButton);
        createNewImageButton.addActionListener(this);
        saveImageToFileButton.addActionListener(this);
        retrieveImageFromFileButton.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource () == createNewImageButton)
            CreateNewImage ();
        else if (e.getSource () == saveImageToFileButton)
            SaveImageToFile();
        else if (e.getSource () == retrieveImageFromFileButton)
            RetrieveImageFromFile ();
        
        cp.repaint ();
    }
    
    public void CreateNewImage ()
    {
        cp.bi = new BufferedImage (200, 200, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = cp.bi.createGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color color1 = new Color(255, 0, 0, 100);
        g2.setColor (color1);
        g2.fillOval (30, 30, 80, 80);
        g2.dispose ();
    }
    
    
    public void SaveImageToFile ()
    {
        try
        {
             File saveFile = new File("SemiTransparentCircle.png");
             System.out.println ("Got this far");
             if (saveFile == null)
                 System.out.println ("saveFile is null");
             if (cp == null)
                 System.out.println ("cp is null");                 
             if (cp.bi == null)
                 System.out.println ("cp.bi is null");
             ImageIO.write(cp.bi, "png", saveFile);           
        }
        catch (IllegalArgumentException ex)
        {
            System.out.print ("Caught an illegal argument exception - ");
            System.out.println (ex.toString());            
        }
        catch (IOException ex)
        {
            System.out.print ("Caught an IO exception - ");
            System.out.println (ex.toString());            
        }
        catch (NullPointerException ex)
        {
            System.out.print ("Caught a null pointer exception - ");
            System.out.println (ex.toString());            
        }
        catch (Exception ex)
        {
            System.out.print ("Caught an exception - ");
            System.out.println (ex.toString());
        }
    }
    
    
    public void RetrieveImageFromFile ()
    {
        try 
        {
            File biFile = new File("SemiTransparentCircle.png");
            cp.bi = ImageIO.read(biFile);
        } 
        catch (IOException ex) 
        {
            System.out.println ("Problem opening file");
        }        
    }
}
class CanvasPanel extends JPanel
{
    BufferedImage bi;
    
    
    public CanvasPanel ()
    {
    }
    public void paintComponent (Graphics g)
    {
        super.paintComponent (g);        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setBackground(Color.GREEN);
        g2.setColor(Color.WHITE);
        g2.fillOval (50, 50, 75, 75);
        g2.drawImage (bi, 50, 50, null);
    }
}