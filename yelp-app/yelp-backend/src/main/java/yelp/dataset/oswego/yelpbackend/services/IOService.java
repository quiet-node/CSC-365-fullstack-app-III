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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import lombok.NoArgsConstructor;
import yelp.dataset.oswego.yelpbackend.data_structure.b_tree.BusinessBtree;
import yelp.dataset.oswego.yelpbackend.data_structure.weighted_graph.WeightedEdge;
import yelp.dataset.oswego.yelpbackend.models.graph_models.NearestNodeModel;

@NoArgsConstructor
public class IOService {
    private final String bTreeFilePath = System.getProperty("user.dir") + "/yelp-app/yelp-datastore-files/business-btree/btree.bin";
    private final String edgesFilePath = System.getProperty("user.dir") + "/yelp-app/yelp-datastore-files/business-graphs";

    /**
     * A function to write a whole Btree to disk
     * @param businessBtree
     * @throws IOException
     */
    protected void writeBtree(BusinessBtree businessBtree) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bTreeFilePath));
        oos.writeObject(businessBtree);
        System.out.println("Successfully write bTree to btree.bin!");
        oos.close();
    }

    /**
     * A function to read the B-tree from disk
     * @return BusinessBtree
     * @throws IOException
     */
    public BusinessBtree readBtree() throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bTreeFilePath));
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
     * A function to write each node with the edges between the closest four businesses to disk 
     * using File Channel and Buffer
     * @param businessBtree
     * @throws IOException
     */
    protected void writeNodesWithEdges(NearestNodeModel nearestFourNode) throws IOException {
        /*
         * This method will write 10,000 nodes to disk (disk usage = 40MBs in total). 
         * Storing each node is a file will help holding neccessary and needed node in memory rather than holding the whole 10000 nodes
         * Using byte buffer from java NIO helps speeing up the writing operation.
         */

        long targetNodeID = nearestFourNode.getRequestedNodeID();
        // Get the file
        RandomAccessFile raf = new RandomAccessFile(edgesFilePath +"/node-"+targetNodeID+".bin", "rw");
        raf.seek(0);
        // Register FileChannel to operate read and write
        FileChannel wChannel = raf.getChannel();

        // Allocate bytes to ByteBuffer to store message inside a buffer
        ByteBuffer wBuffer = ByteBuffer.allocate(4096); // 4KB

        // Write information to wBuffer
        List<WeightedEdge> edges = nearestFourNode.getEdges();
        for (WeightedEdge weightedEdge : edges) {
            String putString = weightedEdge.getSourceID() +","+ weightedEdge.getDestinationID() + "," +weightedEdge.getWeight()+"\n";
            wBuffer.put(putString.getBytes());
        }

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
    }

    /**
     * A function to read a graph node from disk
     * @throws IOException
     */
    protected NearestNodeModel readNodesWithEdges(long targetNodeID) throws IOException {
        List<WeightedEdge> edges = new ArrayList<>();
        Path path = FileSystems.getDefault().getPath(edgesFilePath, "node-"+targetNodeID+".bin");
        Files.lines(path).forEach(line -> {
            String[] lineArray = line.split(",");
            edges.add(new WeightedEdge(Long.parseLong(lineArray[0]), Long.parseLong(lineArray[1]), Double.parseDouble(lineArray[2])));
        } 
        );
        return new NearestNodeModel(targetNodeID, edges);
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
