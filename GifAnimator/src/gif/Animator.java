package gif;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;

public class Animator {
    private GifFrameList list;
    private GifFramePanel panel;
    private static int size = 350;
    public String username;
    
    public Animator(GifFrame[] frames, String username) {
        this.username = username;
        final JFrame f = new JFrame("Gif Animator");
        f.setPreferredSize(new Dimension(700, 700));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new GifFramePanel(this);
        f.getContentPane().add(panel);

        JPanel listPanel = new JPanel(new BorderLayout());

        list = new GifFrameList(frames);
        JScrollPane listScrollPane = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        int inset = listScrollPane.getHorizontalScrollBar().getHeight();
        list.setBorder(BorderFactory.createEmptyBorder(0, 0, inset, 0));
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                GifFrame frame = (GifFrame) list.getSelectedValue();
                panel.setGifFrame(frame);
            }
        });
        list.setSelectedIndex(0);
        listPanel.add(listScrollPane);

        listPanel.add(new ButtonPanel(this), BorderLayout.SOUTH);

        f.getContentPane().add(listPanel, BorderLayout.SOUTH);

        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    
    public void addGifFrame(GifFrame frame) {
        list.addGifFrame(frame);
    }

    public void removeGifFrame(){
        list.removeGifFrame(panel.getCurrentFrame());
    }
    
    public void removeAllFrames(){
        list.removeAllFrames();
    }
    
    public boolean loop() {
        return panel.loop();
    }

    public List<GifFrame> getGifFrames() {
        return list.getGifFrames();
    }

    private static GifFrame createDemoGifFrame(int w, int h, String str, int delay) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(str, g);
        g.setColor(Color.BLACK);

        g.drawString(str, (int) ((w / 2) - (bounds.getWidth() / 2)), 
                          (int) ((h / 2) + (bounds.getHeight() / 2)));
        g.dispose();
        return new GifFrame(image, delay);
    }

    public static void main(String[] args) {
        final String name = args[0];
        try {
            final GifFrame[] frames = new GifFrame[] {
                createDemoGifFrame(size, size, "my", 500),
                createDemoGifFrame(size, size, "gif", 500),
                createDemoGifFrame(size, size, "to", 500),
                createDemoGifFrame(size, size, "you", 500),
                createDemoGifFrame(size, size, ";)", 1000)
            };
            Gif.write(Arrays.asList(frames), true, new File("demo.gif"));
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new Animator(frames, name);
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex, "Exception",
                        JOptionPane.ERROR_MESSAGE);
        }
    }
}
