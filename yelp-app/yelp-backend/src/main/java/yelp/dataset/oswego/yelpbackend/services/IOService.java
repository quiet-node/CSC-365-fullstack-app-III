package yelp.dataset.oswego.yelpbackend.services;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.NoArgsConstructor;
import yelp.dataset.oswego.yelpbackend.dataStructure.btree.BusinessBtree;

@NoArgsConstructor
public class IOService {
    private final String bTree = System.getProperty("user.dir") + "/yelp-app/yelp-dataset/btree.bin";
    private final String bTreeRoot = System.getProperty("user.dir") + "/yelp-app/yelp-dataset/btree-node.bin";

    /**
     * A function to write a whole Btree to disk
     * @param businessBtree
     * @throws IOException
     */
    protected void writeBtree(BusinessBtree businessBtree) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bTree));
        oos.writeObject(businessBtree);
        System.out.println("Successfully write bTree to btree.bin!");
        oos.close();
    }

    /**
     * A function to read the B-tree from disk
     * @return BusinessBtree
     * @throws IOException
     */
    protected BusinessBtree readBtree() throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bTree));
        try {
            BusinessBtree bTree = (BusinessBtree) ois.readObject();
            ois.close();
            return bTree;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ois.close();
            return null;
        }
    }

    /**
     * A function to write a btree node to disk using File Channel and Buffer
     * @param businessBtree
     * @throws IOException
     */
    protected void writeNode(BusinessBtree businessBtree) throws IOException {
        // Get the file
        RandomAccessFile raf = new RandomAccessFile(bTreeRoot, "rw");
        raf.seek(0);
        // Register FileChannel to operate read and write
        FileChannel wChannel = raf.getChannel();

        // Allocate bytes to ByteBuffer to store message inside a buffer
        ByteBuffer wBuffer = ByteBuffer.allocate(4096); // 4KB

        // Write information to wBuffer
        wBuffer.put("writing to disk...".getBytes());

        // ByteBuffer::flip() is used to flip BB from "reading from I/O"(put) to "writing to I/O"(get) after a sequence of put
        wBuffer.flip();

        // Acctually write to file from buffer
        wChannel.write(wBuffer);

        // forcely flushes all unwritten data from channel to disk
        wChannel.force(true);

        // clean up
        wBuffer.clear();
        wChannel.close();
        raf.close();
        
        System.out.println("Successfully wrote node to file!");
    }

    /**
     * A function to read a btree node from disk
     * @throws IOException
     */
    protected void readNode() throws IOException {
        // Get the file
        RandomAccessFile raf = new RandomAccessFile(bTreeRoot, "rw");

        // Register FileChannel to operate read and write
        FileChannel rChannel = raf.getChannel();

        // Allocaate rBufferSize 
        int rBufferSize = 4096; // 4KB
        if (rBufferSize > rChannel.size()) {
            rBufferSize = (int) rChannel.size();
        }
        
        // Register ByteBuffer to store message inside a buffer
        ByteBuffer rBuffer = ByteBuffer.allocate(rBufferSize);

        // Read the file to buffer
        rChannel.read(rBuffer);
        
        // Flip the rBufferer
        rBuffer.flip();

        // clean up
        rBuffer.clear();
        rChannel.close();
        raf.close();
        
        System.out.println("Reading from wBuffer: " +new String(rBuffer.array()));
    }

    /**
     * A function to empty a file
     * @param PATH
     * @throws IOException
     */
    public void emptyFile(String PATH) throws IOException{
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(PATH));
        writer.write("");
        writer.flush();
    }

}
