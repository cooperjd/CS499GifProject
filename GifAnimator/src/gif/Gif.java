package gif;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class Gif {
    private static int size = 350;
    
    private Gif() {}

    public static void write(List<GifFrame> frames, boolean loopContinuously, File file)
            throws IIOInvalidTreeException, IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
        ImageOutputStream out = ImageIO.createImageOutputStream(file);
        writer.setOutput(out);
        writer.prepareWriteSequence(writer.getDefaultStreamMetadata(
                writer.getDefaultWriteParam()));

        for (GifFrame frame : frames) {
            BufferedImage image = frame.getImage();
            long delay = frame.getDelay() / 10;

            IIOMetadata metadata = writer.getDefaultImageMetadata(
                    new ImageTypeSpecifier(image), writer.getDefaultWriteParam());

            String fmt = metadata.getNativeMetadataFormatName();

            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(fmt);

            IIOMetadataNode gce = getNode(root, "GraphicControlExtension");

            gce.setAttribute("disposalMethod", "none");
            gce.setAttribute("userInputFlag", "FALSE");
            gce.setAttribute("transparentColorFlag", "FALSE");

            gce.setAttribute("delayTime", Long.toString(delay));

            gce.setAttribute("transparentColorIndex", "0");

            IIOMetadataNode appexts = getNode(root, "ApplicationExtensions");
            IIOMetadataNode appext = new IIOMetadataNode("ApplicationExtension");
            appext.setAttribute("applicationID", "NETSCAPE");
            appext.setAttribute("authenticationCode", "2.0");

            appext.setUserObject(new byte[] {
                0x1,
                (byte) (loopContinuously ? 0x0 : 0x1),
                0x0 
            });

            appexts.appendChild(appext);

            metadata.setFromTree(fmt, root);

            writer.writeToSequence(new IIOImage(image, null, metadata),
                    writer.getDefaultWriteParam());
        }
        
        writer.endWriteSequence();

        writer.reset();
        out.close();
        
        ////Save gif to server here
        //file = new File();
        //writeToServer(frames, loopContinuously, );
    }

    public static void writeToServer(List<GifFrame> frames, boolean loopContinuously, File file)
            throws IIOInvalidTreeException, IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
        ImageOutputStream out = ImageIO.createImageOutputStream(file);
        writer.setOutput(out);
        writer.prepareWriteSequence(writer.getDefaultStreamMetadata(
                writer.getDefaultWriteParam()));

        for (GifFrame frame : frames) {
            BufferedImage image = frame.getImage();
            long delay = frame.getDelay() / 10;

            IIOMetadata metadata = writer.getDefaultImageMetadata(
                    new ImageTypeSpecifier(image), writer.getDefaultWriteParam());

            String fmt = metadata.getNativeMetadataFormatName();

            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(fmt);

            IIOMetadataNode gce = getNode(root, "GraphicControlExtension");

            gce.setAttribute("disposalMethod", "none");
            gce.setAttribute("userInputFlag", "FALSE");
            gce.setAttribute("transparentColorFlag", "FALSE");

            gce.setAttribute("delayTime", Long.toString(delay));

            gce.setAttribute("transparentColorIndex", "0");

            IIOMetadataNode appexts = getNode(root, "ApplicationExtensions");
            IIOMetadataNode appext = new IIOMetadataNode("ApplicationExtension");
            appext.setAttribute("applicationID", "NETSCAPE");
            appext.setAttribute("authenticationCode", "2.0");

            appext.setUserObject(new byte[] {
                0x1,
                (byte) (loopContinuously ? 0x0 : 0x1),
                0x0 
            });

            appexts.appendChild(appext);

            metadata.setFromTree(fmt, root);

            writer.writeToSequence(new IIOImage(image, null, metadata),
                    writer.getDefaultWriteParam());
        }
        
        writer.endWriteSequence();

        writer.reset();
        out.close();
    }
    
    public static List<GifFrame> read(File file) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(ImageIO.createImageInputStream(file));
        
        int numImages = reader.getNumImages(true);
        
        List<GifFrame> frames = new ArrayList<GifFrame>();
        
        for (int i = 0; i < numImages; i++) {
            BufferedImage image = reader.read(i);
            
            IIOMetadata metadata = reader.getImageMetadata(i);
            String fmt = metadata.getNativeMetadataFormatName();
            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(fmt);
            
            IIOMetadataNode gce = getNode(root, "GraphicControlExtension");
            String delayTime = gce.getAttribute("delayTime");
            long delay = 500;
            if (!delayTime.isEmpty()) {
                delay = Long.parseLong(delayTime);
            }
            
            frames.add(new GifFrame(image, delay * 10));
        }
        
        return frames;
    }
    
    public static List<GifFrame> readSingleImage(File file, String format) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByFormatName(format).next();
        reader.setInput(ImageIO.createImageInputStream(file));
        
        int numImages = reader.getNumImages(true);
        
        List<GifFrame> frames = new ArrayList<GifFrame>();
        
        for (int i = 0; i < numImages; i++) {
            BufferedImage image = reader.read(i);
            
            IIOMetadata metadata = reader.getImageMetadata(i);
            String fmt = metadata.getNativeMetadataFormatName();
            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(fmt);
            
            IIOMetadataNode gce = getNode(root, "GraphicControlExtension");
            String delayTime = gce.getAttribute("delayTime");
            long delay = 500;
            if (!delayTime.isEmpty()) {
                delay = Long.parseLong(delayTime);
            }
            
            BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, size, size, null);
            g2d.dispose();
            frames.add(new GifFrame(resizedImage, delay * 2));
        }
        
        return frames;
    }

    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                return ((IIOMetadataNode) rootNode.item(i));
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return (node);
    }
}
