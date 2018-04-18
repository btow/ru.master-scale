import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    final static Dimensions[] DIMENSIONS = new Dimensions[]{
            new Dimensions(240, 353),
            new Dimensions(320, 470),
            new Dimensions(360, 480),
            new Dimensions(426, 567),
            new Dimensions(480, 640),
            new Dimensions(540, 720),
            new Dimensions(638,  851),
            new Dimensions(640, 940),
            new Dimensions(720, 960),
            new Dimensions(958, 1277),
            new Dimensions(960, 1280),
            new Dimensions(1080, 1440),
            new Dimensions(1440, 1920),
            new Dimensions(2160, 2880)
    };

    final static int
            NORMAL_WIDTH = 320,
            NORMAL_HEIGHT = 470,
            MODE_WIDTH = 0,
            MODE_HEIGHT = 1,
            MODE_FONT = 2;

    public static void main(String[] args) {

        String dirName;
        for (Dimensions dimensions :
                DIMENSIONS) {
            dirName = "values";
            switch (dimensions.getmWidth()) {
                case NORMAL_WIDTH:
                    break;
                default:
                    dirName += ("-sw" + dimensions.getmWidth() + "dp");
                    break;
            }
            try {
                File folder = new File(new File(".").getCanonicalPath() + File.separator + dirName);

                if (!folder.exists()) {
                    folder.mkdir();
                }
                String fileName = folder.getPath() + File.separator + "positiveScaleWidthsDim.xml";
                createDimensFile(dimensions.getmWidth(), fileName, MODE_WIDTH,
                        1, DIMENSIONS[DIMENSIONS.length - 1].getmWidth() * 2);

                fileName = folder.getPath() + File.separator + "negativeScaleWidthsDim.xml";
                createDimensFile(dimensions.getmWidth(), fileName, MODE_WIDTH,
                        DIMENSIONS[DIMENSIONS.length - 1].getmWidth() * (-1), 0);

                fileName = folder.getPath() + File.separator + "positiveScaleHeightsDim.xml";
                createDimensFile(dimensions.getmHeight(), fileName, MODE_HEIGHT,
                        1, DIMENSIONS[DIMENSIONS.length - 1].getmHeight() * 2);

                fileName = folder.getPath() + File.separator + "negativeScaleHeightsDim.xml";
                createDimensFile(dimensions.getmHeight(), fileName, MODE_HEIGHT,
                        DIMENSIONS[DIMENSIONS.length - 1].getmHeight() * (-1), 0);

                fileName = folder.getPath() + File.separator + "scaleFontsDim.xml";
                createDimensFile(dimensions.getmHeight(), fileName, MODE_FONT,
                        1, DIMENSIONS[DIMENSIONS.length - 1].getmHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param dimension  - <code>int</code> current width or height;
     * @param fileName   - <code>String</code> current file name;
     * @param mode       - <code>int</code> current mode of method: 0 - if creating of DIMENSIONS dimens,
     *                   1 - if creating of HEIGHTS, and 2 - if fonts size;
     * @param beginIndex - the beginning of the calculation of values from 1 for file with positive values
     *                   and from <code>-max</code> - for negative values;
     * @param endIndex   - end of the calculation of values to <code>max</code> for file with positive values
     *                   and to 0 - for negative values;
     * @throws IOException - in case exception.
     */
    private static void createDimensFile(int dimension, String fileName,
                                         int mode, int beginIndex, int endIndex) throws IOException {
        ArrayList<String> linesList = new ArrayList<>();
        linesList.add("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        linesList.add("    <resources>");
        String prefix, sign, dim;
        double dp;

        for (int i = beginIndex; i < endIndex; i++) {
            prefix = "        <dimen name=\"";
            sign = i > 0 ? "ps" : "ns";
            switch (mode) {
                case MODE_WIDTH:
                    dim = "w_";
                    switch (dimension) {
                        case NORMAL_WIDTH:
                            dp = i;
                            break;
                        default:
                            dp = i * dimension / NORMAL_HEIGHT;
                            break;
                    }
                    dim += (Math.abs(i) + "dp\">");
                    linesList.add(prefix + sign + dim + (Math.rint(100.0 * dp) / 100.0) + "dp</dimen>");
                    break;
                case MODE_HEIGHT:
                    dim = "h_";
                    switch (dimension) {
                        case NORMAL_HEIGHT:
                            dp = i;
                            break;
                        default:
                            dp = i * dimension / NORMAL_HEIGHT;
                            break;
                    }
                    dim += (Math.abs(i) + "dp\">");
                    linesList.add(prefix + sign + dim + (Math.rint(100.0 * dp) / 100.0) + "dp</dimen>");
                    break;
                case MODE_FONT:
                    dim = "f_";
                    switch (dimension) {
                        case NORMAL_HEIGHT:
                            dp = i;
                            break;
                        default:
                            dp = i * dimension / NORMAL_HEIGHT;
                            break;
                    }
                    dim += (i + "sp\">");
                    linesList.add(prefix + sign + dim + (Math.rint(100.0 * dp) / 100.0) + "sp</dimen>");
                    break;
                default:
                    break;
            }
        }
        linesList.add("    </resources>");
        Path file = Paths.get(fileName);
        Files.write(file, linesList, Charset.forName("UTF-8"));
    }
}
