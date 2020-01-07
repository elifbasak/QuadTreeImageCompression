

public class Main {

    public static final double SART_1 = 0.002 * 100; // % orani hesaplamak icin 100 ile carptim.
    public static final double SART_2 = 0.004 * 100;
    public static final double SART_3 = 0.01 * 100;
    public static final double SART_4 = 0.033 * 100;
    public static final double SART_5 = 0.077 * 100;
    public static final double SART_6 = 0.2 * 100;
    public static final double SART_7 = 0.5 * 100;
    public static final double SART_8 = 0.65 * 100;
    public static double[] istenenCompressionOrani = {SART_1, SART_2, SART_3, SART_4, SART_5, SART_6, SART_7, SART_8};
    public static final int TOPLAM_OUTPUT_SAYISI = 8;

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Eksik argumant!");
            return;
        }
        String inputFileName = "";
        String outputFileName = "";
        String command = "";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    inputFileName = args[i + 1];
                    break;
                case "-o":
                    outputFileName = args[i + 1];
                    break;
                case "-c":
                    command = args[i];
                    break;
                case "-e":
                    command = args[i];
                    break;
                default:
                    break;
            }
        }
//        System.out.println(inputFileName + "  " + outputFileName + " " + command);
        
        
//        QuadTree tree = new QuadTree(5,inputFileName , outputFileName+"TEST.ppm");

        if (command.equals("-c")) {
            commandCompress(inputFileName, outputFileName);
        } else if (command.equals("-e")) {
            System.out.println("Kenar Belirleme Basliyor.");
            commandEdgeDetection(inputFileName, outputFileName);
        } else {
            System.out.println("Komut Bulunamadi !!!!!");
        }
        
        System.out.println("Tamamlandi !!");

    }

    public static void commandCompress(String inputFileName, String outputFileName) {
        String[] outputFiles = new String[TOPLAM_OUTPUT_SAYISI];
        for (int i = 0; i < outputFiles.length; i++) {
            outputFiles[i] = outputFileName + "-" + (i + 1) + ".ppm";
//            System.out.println(outputFiles[i]);
        }

        double denenenTreshold = -20;
        for (int index = TOPLAM_OUTPUT_SAYISI - 1; index >= 0; index--) {
            Boolean isSuccess = false;
            for (; isSuccess == false;) {
                QuadTree tree = new QuadTree(denenenTreshold, istenenCompressionOrani[index], inputFileName, outputFiles[index]);
                System.out.println("Denenen Treshold degeri :     : " + denenenTreshold);
                System.out.println("İstenen Compression Orani     : " + istenenCompressionOrani[index]);
                System.out.println("Elde edilen Compression Orani : " + tree.compressionOran);
                System.out.println();
                if (tree.isSuccess) {
                    isSuccess = true;
                    System.out.println("---------BULUNDU-----------");
                    System.out.println("Bulunan Dosya : " + outputFiles[index]);
                    System.out.println("Bulunan Treshold degeri :     : " + denenenTreshold);
                    System.out.println("İstenen Compression Orani     : " + istenenCompressionOrani[index]);

                    System.out.println("Elde Edilen Compress Degeri : " + tree.compressionOran);
                    System.out.println("---------------------------");
                    break;

                } else {
                    denenenTreshold = tree.calculateNewTrashold(denenenTreshold, istenenCompressionOrani[index], tree.compressionOran);

                }
            }

        }

    }

    public static void commandEdgeDetection(String inputFileName, String outputFileName) {

//    outputFileName = outputFileName + ".ppm";
        QuadTree tree = new QuadTree(inputFileName, outputFileName + "-NEGATIVE.ppm", QuadTree.NEGATIVE);
        QuadTree tree2 = new QuadTree(inputFileName, outputFileName + "-GRAYSCALE.ppm", QuadTree.GRAY_SCALE);
        QuadTree tree3 = new QuadTree(inputFileName, outputFileName + "-EDGE.ppm", QuadTree.EDGE_DETECTION);

    }

}
