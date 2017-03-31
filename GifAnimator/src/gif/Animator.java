package gif;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

public class Animator {
    private GifFrameList list;
    private GifFramePanel panel;
    private static int size = 350;
    public String username;
    
    public Animator(GifFrame[] frames, String[] args) {
        if(args[1] != ""){
            String[] newArgs = new String[]{args[0], ""};
            loadNewAnimator(loadGif(args[1]), newArgs);
        }else{
            this.username = args[0];
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
    }
    
    public void addGifFrame(GifFrame frame) {
        list.addGifFrame(frame);
    }

    public void removeGifFrame(){
        list.removeGifFrame(panel.getCurrentFrame());
    }
    
    public void removeAllFrames(){
        if(list != null){
            list.removeAllFrames();
        }
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

    public void loadNewAnimator(final GifFrame[] frames, final String[] args){
            try{
                //Gif.write(Arrays.asList(frames), true, new File(args[1]));
                EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new Animator(frames, args);
                }
            });
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public GifFrame[] loadGif(String gifPath){
        File file = new File(gifPath);
        GifFrame[] gframes = new GifFrame[1];
        
        try {
            if (file.getName().endsWith("gif")) {
                removeAllFrames();
                List<GifFrame> frames = Gif.read(file);
                
                gframes = new GifFrame[frames.size()];
                for(int i = 0; i < gframes.length; i++){
                    gframes[i] = frames.get(i);
                }
                list = new GifFrameList(gframes);
                for (GifFrame frame : frames) {
                    addGifFrame(frame);
                }
                
                Gif.write(Arrays.asList(gframes), true, file);
                return gframes;
            } else {
                GifFrame gf = createDemoGifFrame(size, size, "my", 500);
                addGifFrame(gf);
                gframes[0] = gf;
                return gframes;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(list, ex, "Exception",
                    JOptionPane.ERROR_MESSAGE);
        }
        return gframes;
    }
    
    public static void main(final String[] args){        
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
                    new Animator(frames, args);
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex, "Exception",
                        JOptionPane.ERROR_MESSAGE);
        }
    }
}
