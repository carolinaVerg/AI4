import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class main {
    public static Graph world;
    public static BN_Net bnNet;


    public static void main(String[] args) {
        File file = new File("C:\\univercity\\courses\\semester5\\intro to AI\\programming assignments\\AI4\\AI4\\tests\\test1.txt"); //graph description
        BufferedReader br = null;
        String st = "";
        initWorld(br, st, file);
        System.out.println("For an output of the Bayes network press 1\nFor inferance Q1-3 press 2\nFor inference Q4 3\nExit press 4");
        Scanner reader = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            switch (reader.nextLine()) {
                case "1":
                    printBnNet();
                    break;
                case "2":
                    HashMap<BN_Node, Boolean> evidence = queryEvidence();
                    BN_Node query = getQuery();
                    double[] distribution = bnNet.Enumeration_Ask(query, evidence);
                    System.out.println("The distribution is: \ntrue " + distribution[0] + "\nfalse " + distribution[1]);
                    break;
                case "3":
                    HashMap<BN_Node, Boolean> evidence2 = queryEvidence();
                    System.out.println("Enter edges id of path: <id id .... >");
                    String[] path = (reader.nextLine()).split(" ");
                    double[] distribution2 = calcPathDis(path,evidence2);
                    System.out.println("The distribution is: \ntrue " + distribution2[0] + "\nfalse " + distribution2[1]);
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    private static double[] calcPathDis(String[] path, HashMap<BN_Node, Boolean> evidence2) {
        double[] qX = {1,1};
        for(int i = 0; i<path.length;i++){
            BN_Node currB = bnNet.getBs().get(Integer.parseInt(path[i]));
            qX[0] = qX[0] * bnNet.Enumeration_Ask(currB,evidence2)[1];
            evidence2.put(currB,false);
        }
        qX[1] = 1 - qX[0];
        return qX;
    }


    public static void printBnNet() {
        printVerticesDis();
        printEdgeDis();
    }

    private static void printEdgeDis() {
        String[] trueOrFalse = {"", "not"};
        for (int i = 1; i <= bnNet.getBs().size(); i++) {
            System.out.format("EDGE %d:\n", i);
            BN_B bVertex = (BN_B) bnNet.getBs().get(i);
            for (int j = 0; j < 2; j++) {
                for (int k = 1; k < bVertex.cpt.length; k++) {
                    String probStr = generateProbString(k - 1, bVertex);
                    double prob = bVertex.getCpt()[k];
                    if (j != 0) {
                        prob = 1 - prob;
                    }
                    System.out.format("P(%s Evacuees| %s) = %fl\n", trueOrFalse[j], probStr, prob);
                }
            }
            System.out.println("-----------------------");
        }
    }

    private static void printVerticesDis() {
        String[] trueOrFalse = {"", "not"};
        for (int i = 1; i <= bnNet.getFls().size(); i++) {
            System.out.format("VERTEX %d:\n", i);
            BN_Fl flVertex = (BN_Fl) bnNet.getFls().get(i);
            BN_Ev evVertex = (BN_Ev) bnNet.getEvs().get(i);
            for (int j = 0; j < 2; j++) {
                double flDis = flVertex.getDistribution();
                if (j != 0) {
                    flDis = 1 - flDis;
                }
                System.out.format("P(%s Flooding) = %fl\n", trueOrFalse[j], flDis);
                for (int k = 1; k < evVertex.cpt.length; k++) {
                    String probStr = generateProbString(k - 1, evVertex);
                    double prob = evVertex.getCpt()[k];
                    if (j != 0) {
                        prob = 1 - prob;
                    }
                    System.out.format("P(%s Evacuees| %s) = %fl\n", trueOrFalse[j], probStr, prob);
                }
            }
            System.out.println("-----------------------");
        }
    }

    private static String generateProbString(int k, BN_Node vertex) {
        String[] binaryStr = String.format("%08d", Integer.parseInt(Integer.toBinaryString(k))).split("");
        String probExp = "";
        String b = "Blockage ";
        int pos = 8 - vertex.getParents().size();
        for (BN_Node pr : vertex.parents.values()) {
            if (binaryStr[pos].equals("0")) {
                probExp = probExp + "not ";
            }
            probExp = probExp + b + pr.id + " ,";
            pos++;
        }
        return probExp.substring(0, probExp.length() - 1);
    }

    private static BN_Node getQuery() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter query in the format: <variable type - id>");
        String[] nextEvi = reader.nextLine().split(" ");
        BN_Node bnToReturn = null;
        switch (nextEvi[0]) {
            case "fl":
                bnToReturn = bnNet.getFls().get(Integer.parseInt(nextEvi[1]));
                break;
            case "b":
                bnToReturn = bnNet.getBs().get(Integer.parseInt(nextEvi[1]));
                break;
            case "ev":
                bnToReturn = bnNet.getEvs().get(Integer.parseInt(nextEvi[1]));
                break;
            default:
                break;
        }
        return bnToReturn;
    }

    private static HashMap<BN_Node, Boolean> queryEvidence() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter number of evidence:");
        int numOfevi = Integer.parseInt(reader.nextLine());
        HashMap<BN_Node, Boolean> evidence = new HashMap<>();
        for (int i = 0; i < numOfevi; i++) {
            // [fl/b/ev --- id ---- true/false]
            System.out.println("Enter the fallowing format: <variable type , id , boolean value>");
            String[] nextEvi = reader.nextLine().split(" ");
            BN_Node eviToadd = null;
            switch (nextEvi[0]) {
                case "fl":
                    eviToadd = bnNet.getFls().get(Integer.parseInt(nextEvi[1]));
                    break;
                case "b":
                    eviToadd = bnNet.getBs().get(Integer.parseInt(nextEvi[1]));
                    break;
                case "ev":
                    eviToadd = bnNet.getEvs().get(Integer.parseInt(nextEvi[1]));
                    break;
                default:
                    break;
            }
            evidence.put(eviToadd, Boolean.parseBoolean(nextEvi[2]));
        }
        return evidence;
    }

    private static void initWorld(BufferedReader br, String st, File file) {
        try {
            br = new BufferedReader(new FileReader(file));
            try {
                st = br.readLine();
                int numOfNodes = Integer.parseInt(st.split(" ")[1]);
                world = new Graph(numOfNodes);
                bnNet = new BN_Net(numOfNodes);
                while ((st = br.readLine()) != null) {
                    String[] data = st.split(" ");
                    switch (data[0]) {
                        case "V":
                            updateVertex(data);
                            break;
                        case "E":
                            updateEdge(data);
                            break;
                        default:
                            break;
                    }
                }
                System.out.println(st);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bnNet.updateCPTInNet();

    }

    public static void updateVertex(String[] data) {
        int vid = Integer.parseInt(data[1]);
        double dis = Double.parseDouble(data[3]);
        ((BN_Fl) bnNet.getFls().get(vid)).setDistribution(dis);

    }

    public static void updateEdge(String[] data) {
        int edgeId = Integer.parseInt(data[1]);
        int idN1 = Integer.parseInt(data[3]);
        int idN2 = Integer.parseInt(data[4]);
        Vertex vfirst = world.getVertexById(idN1);
        Vertex vsecond = world.getVertexById(idN2);
        int weight = Integer.parseInt(data[6]);
        vfirst.addEdge(weight, vsecond);
        vsecond.addEdge(weight, vfirst);
        BN_B newB = new BN_B(edgeId, weight);
        bnNet.getBs().put(edgeId, newB);
        updateBnNet(idN1, idN2, edgeId);

    }

    public static void updateBnNet(int v1, int v2, int edge) {
        BN_Node newB = bnNet.getBs().get(edge);
        BN_Node fl1 = bnNet.getFls().get(v1);
        BN_Node fl2 = bnNet.getFls().get(v2);
        BN_Node ev1 = bnNet.getEvs().get(v1);
        BN_Node ev2 = bnNet.getEvs().get(v2);

        newB.getParents().put(v1, fl1);
        newB.getParents().put(v2, fl2);
        newB.getChildren().put(v1, ev1);
        newB.getChildren().put(v2, ev2);

        fl1.getChildren().put(edge, newB);
        fl2.getChildren().put(edge, newB);

        ev1.getParents().put(edge, newB);
        ev2.getParents().put(edge, newB);
    }


}

