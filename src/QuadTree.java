


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuadTree {

    public static final String NEGATIVE = "negative";
    public static final String GRAY_SCALE = "grayScale";
    public static final String EDGE_DETECTION = "edgeDetection";
    public static final String NO_FILTER = "noFilter";

    public static Node[][] array;
    public static Color[][] colors;
    public static int boyut;
    public Node root;
    public double threshold;
    public double compressionOran;
    public double istenenCompressionOrani;
    public int leafNodeSayisi = 0;
    public String inputFileName;
    public String outputFileName;
    public boolean isSuccess = false;

    public QuadTree(double treshold, double istenenCompressionOrani, String inputFileName, String outputFileName) {

        this.istenenCompressionOrani = istenenCompressionOrani;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.threshold = treshold;
        readFromFile();
        root = compress(colors, 0, 0, boyut, boyut);
        leafNodeSayisiHesapla(root);
        compressionOran = ((double) this.leafNodeSayisi / (this.boyut * this.boyut)) * 100;
        double altSinir = istenenCompressionOrani * 80 / 100;
        double ustSinir = istenenCompressionOrani * 120 / 100;
        if (compressionOran >= altSinir && compressionOran <= ustSinir) {
            writeFileFromTree();
            isSuccess = true;
        }

    }

    public QuadTree(String inputFileName, String outputFileName, String filter) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.threshold = 0;
        readFromFile();
        root = buildTree(colors, 0, 0, boyut, boyut);
        if (filter.equals(NO_FILTER)) {
            // do nothing; 
        } else if (filter.equals(NEGATIVE)) {
            root = applyNegativeFilter(root);
        } else if (filter.equals(GRAY_SCALE)) {
            root = applyGrayScale(root);
        } else if (filter.equals(EDGE_DETECTION)){  
            threshold = 15;
            root= compress(colors, 0, 0, boyut, boyut);
            root = edgeDetection(root);
        }
        
        
        leafNodeSayisiHesapla(root);
        compressionOran = ((double) this.leafNodeSayisi / (this.boyut * this.boyut)) * 100;
        writeFileFromTree();
    }
    
       public QuadTree(double treshold , String inputFileName, String outputFileName) {

        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.threshold = treshold;
        readFromFile();
        root = compress(colors, 0, 0, boyut, boyut);
        leafNodeSayisiHesapla(root);
        compressionOran = ((double) this.leafNodeSayisi / (this.boyut * this.boyut)) * 100;
            writeFileFromTree();

    }
    
    

    private Node compress(Color image[][], int i, int j, int h, int w) {

        Node node = new Node(i, j, h, w);

        Color c;

        if (h == 1 && w == 1) {
            node.color = colors[i][j];

            return node;
        }

        if ((c = kontrolTrash(node)) != null) {
            node.color = c;
            return node;
        }

        node.setChildren(Node.NORTH_WEST, compress(image, i, j, w / 2, h / 2));
        node.setChildren(Node.NORTH_EAST, compress(image, i, j + w / 2, w / 2, h / 2));
        node.setChildren(Node.SOUTH_EAST, compress(image, i + h / 2, j + w / 2, w / 2, h / 2));
        node.setChildren(Node.SOUTH_WEST, compress(image, i + h / 2, j, w / 2, h / 2));

        node.color = node.getAvarageChildren();

        return node;

    }

    private Node buildTree(Color image[][], int i, int j, int h, int w) {

        Node node = new Node(i, j, h, w);

        if (h == 1 && w == 1) {
            node.color = colors[i][j];
            return node;
        }

        node.setChildren(Node.NORTH_WEST, buildTree(image, i, j, w / 2, h / 2));
        node.setChildren(Node.NORTH_EAST, buildTree(image, i, j + w / 2, w / 2, h / 2));
        node.setChildren(Node.SOUTH_EAST, buildTree(image, i + h / 2, j + w / 2, w / 2, h / 2));
        node.setChildren(Node.SOUTH_WEST, buildTree(image, i + h / 2, j, w / 2, h / 2));

        node.color = node.getAvarageChildren();

        return node;

    }

    public Node applyNegativeFilter(Node root) {
        if (root.isLeaf()) {
            int r_ = root.color.r;
            int g_ = root.color.g;
            int b_ = root.color.b;

            r_ = 255 - r_;
            g_ = 255 - g_;
            b_ = 255 - b_;

            root.color = new Color(r_, g_, b_);

            return root;
        }
        applyNegativeFilter(root.children.get(Node.NORTH_WEST));
        applyNegativeFilter(root.children.get(Node.NORTH_EAST));
        applyNegativeFilter(root.children.get(Node.SOUTH_EAST));
        applyNegativeFilter(root.children.get(Node.SOUTH_WEST));

        Color childrenAvarage = root.getAvarageChildren();
        int r_ = childrenAvarage.r;
        int g_ = childrenAvarage.g;
        int b_ = childrenAvarage.b;

        r_ = 255 - r_;
        g_ = 255 - g_;
        b_ = 255 - b_;

        root.color = new Color(r_, g_, b_);

        return root;
    }

    public Node applyGrayScale(Node root) {
        if (root.isLeaf()) {
            double r_ = root.color.r * 0.3;
            double g_ = root.color.g * 0.59;
            double b_ = root.color.b * 0.11;
            int c = (int) (r_ + g_ + b_);

            root.color = new Color(c, c, c);

            return root;
        }
        applyGrayScale(root.children.get(Node.NORTH_WEST));
        applyGrayScale(root.children.get(Node.NORTH_EAST));
        applyGrayScale(root.children.get(Node.SOUTH_EAST));
        applyGrayScale(root.children.get(Node.SOUTH_WEST));

        Color childrenAvarage = root.getAvarageChildren();
        double r_ = childrenAvarage.r * 0.3;
        double g_ = childrenAvarage.g * 0.59;
        double b_ = childrenAvarage.b * 0.11;
        int c = (int) (r_ + g_ + b_);

        root.color = new Color(c, c, c);

        return root;
    }

    public void readFromFile() {

        Scanner nesne = null;
        try {
            nesne = new Scanner(new File(inputFileName));
        } catch (FileNotFoundException e) {
            System.out.println("hata");
        }
        nesne.nextLine();
        String temp = nesne.nextLine();
//       
        int heightI = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
        int widthI = Integer.parseInt(temp.substring(temp.indexOf(" ")+1));
        if(heightI == widthI) boyut = heightI;
        else{
            System.out.println("Resim kare değil !!");
            System.exit(1);
        }
//        
        array = new Node[boyut][boyut];
        colors = new Color[boyut][boyut];
        nesne.nextLine();
        for (int height = 0; height < boyut; height++) {
            for (int width = 0; width < boyut; width++) {
                Node newest = new Node();
                Color color = new Color();
                color.setR(nesne.nextInt());
                color.setG(nesne.nextInt());
                color.setB(nesne.nextInt());
                colors[height][width] = color;
                newest.color = color;
                newest.height = 1;
                newest.width = 1;
                newest.x = height;
                newest.y = width;
                array[height][width] = newest;

            }
        }

    }

    public void writeFile(Color[][] array) {
        try {
            PrintWriter writer = new PrintWriter(outputFileName);

            writer.println("P3");
            writer.println(boyut + " " + boyut);
            writer.println("255");

            for (int k = 0; k < boyut; k++) {
                for (int t = 0; t < boyut; t++) {
                    writer.print(array[k][t].toString() + " ");

                }
                writer.println();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Color[][] listToFile(List<Node> list) {
        Color[][] outPutArray = new Color[boyut][boyut];
        for (Node node : list) {
            int startX = node.x;
            int startY = node.y;
            int uzunluk = node.height;
            int genislik = node.width;
            for (int i = startX; i < startX + uzunluk; i++) {
                for (int j = startY; j < startY + genislik; j++) {
                    outPutArray[i][j] = node.color;
                }

            }

        }
        writeFile(outPutArray);
        return outPutArray;
    }

    public static List<Node> arrayToList() {
        List<Node> liste = new ArrayList();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                liste.add(array[i][j]);
            }
        }

        return liste;

    }

    public void writeFileFromTree() {
        List<Node> list = new ArrayList();

        auxPreOrder(list, root);

        listToFile(list);
    }

    public void auxPreOrder(List<Node> list, Node p) {
        if (p.isLeaf()) {
            list.add(p);

        }
        if (!p.isLeaf()) {
            auxPreOrder(list, p.children.get(Node.NORTH_WEST));
            auxPreOrder(list, p.children.get(Node.NORTH_EAST));
            auxPreOrder(list, p.children.get(Node.SOUTH_WEST));
            auxPreOrder(list, p.children.get(Node.SOUTH_EAST));
        }

    }

    public void leafNodeSayisiHesapla(Node p) {
        if (p.isLeaf()) {

            leafNodeSayisi++;
        }
        if (!p.isLeaf()) {
            leafNodeSayisiHesapla(p.children.get(Node.NORTH_WEST));
            leafNodeSayisiHesapla(p.children.get(Node.NORTH_EAST));
            leafNodeSayisiHesapla(p.children.get(Node.SOUTH_WEST));
            leafNodeSayisiHesapla(p.children.get(Node.SOUTH_EAST));
        }

    }

    public Color kontrolTrash(Node node) {
        double r = 0;
        double g = 0;
        double b = 0;
        int nodeSayisi = 0;
        double hata = 0;
        for (int k = node.x; k < node.x + node.height; k++) {
            for (int l = node.y; l < node.y + node.width; l++) {
                r += colors[k][l].r;
                g += colors[k][l].g;
                b += colors[k][l].b;
                nodeSayisi++;
            }
        }
        r = r / nodeSayisi;
        g = g / nodeSayisi;
        b = b / nodeSayisi;
        for (int k = node.x; k < node.x + node.height; k++) {
            for (int l = node.y; l < node.y + node.width; l++) {
                hata += Math.pow((colors[k][l].r - r), 2) + Math.pow((colors[k][l].g - g), 2)
                        + Math.pow((colors[k][l].b - b), 2);

            }
        }
        hata = hata / nodeSayisi;
        if (hata > (threshold) * (threshold)) {
            return null;
        }

        return new Color((int) r, (int) g, (int) b);
    }
    
        public Node edgeDetection(Node p) {
            if(p.width * p.height == 1 ){
                p.color = new Color(255,255,255);
            }
            else p.color = new Color(0,0,0);
            
            
        if (p.isLeaf()) {

            return p;
        }
        if (!p.isLeaf()) {
            edgeDetection(p.children.get(Node.NORTH_WEST));
            edgeDetection(p.children.get(Node.NORTH_EAST));
            edgeDetection(p.children.get(Node.SOUTH_WEST));
            edgeDetection(p.children.get(Node.SOUTH_EAST));
        }

        return p;
    }
    
    
    

    public double calculateNewTrashold(double denenenTreshold, double istenenCompressionOrani, double compressionOran) {
        if (denenenTreshold < 0 && istenenCompressionOrani > compressionOran) {
            System.out.println("Treshold degeri <0  += 3");
            denenenTreshold += 3;
        } else if (istenenCompressionOrani - compressionOran > 20) {
            System.out.println("Treshold degeri -=5.1");

            denenenTreshold -= 5.1;
        } else if (istenenCompressionOrani - compressionOran > 0) {
            System.out.println("Treshold degeri -=4");

            denenenTreshold -= 4;
        } else if (istenenCompressionOrani < compressionOran * 5) {
            System.out.println("Treshold degeri +=3");

            denenenTreshold += 3;
        } else if (istenenCompressionOrani < compressionOran * 2) {
            System.out.println("Treshold degeri += 0.2");
            denenenTreshold += 1;
        } else if (istenenCompressionOrani < compressionOran) {
            System.out.println("Treshold degeri += 0.2");
            denenenTreshold += 0.15;
        } else if (istenenCompressionOrani > compressionOran) {
            System.out.println("Treshold degeri -=0.2");
            denenenTreshold -= 0.25;
        } else {
            denenenTreshold += 0.2;

        }

        return denenenTreshold;
    }

}
