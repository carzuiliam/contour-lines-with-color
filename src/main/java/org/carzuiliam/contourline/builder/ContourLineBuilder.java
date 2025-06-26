package org.carzuiliam.contourline.builder;

import org.carzuiliam.contourline.utils.ImageUtils;
import org.carzuiliam.contourline.utils.MarchingSquares;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContourLineBuilder {

    private float scaleStep = 0.5f;
    private float minValue = Float.MAX_VALUE;
    private float maxValue = Float.MIN_VALUE;
    private float[] levels;
    private float[][] grid;

    private int scale = 10;
    private int strokeWidth = 1;
    private int outputWidth = -1;
    private int outputHeight = -1;

    public ContourLineBuilder setScale(int _scale) {
        this.scale = _scale;
        return this;
    }

    public ContourLineBuilder setStrokeWidth(int _strokeWidth) {
        this.strokeWidth = _strokeWidth;
        return this;
    }

    public ContourLineBuilder setOutputSize(int _width, int _height) {
        this.outputWidth = _width;
        this.outputHeight = _height;
        return this;
    }

    public ContourLineBuilder loadCSV(String _resourceName) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(_resourceName);

        if (input == null) {
            throw new FileNotFoundException("File " + _resourceName + " not found.");
        }

        List<float[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            line = reader.readLine();

            if (line == null) {
                throw new IOException("The provided CSV file is empty.");
            }

            scaleStep = Float.parseFloat(line.trim());

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split(",");

                float[] row = new float[tokens.length];

                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Float.parseFloat(tokens[i].trim());

                    minValue = Math.min(minValue, row[i]);
                    maxValue = Math.max(maxValue, row[i]);
                }

                rows.add(row);
            }

            grid = rows.toArray(new float[0][]);

            float level = minValue + scaleStep;
            List<Float> levelList = new ArrayList<>();

            while (level < maxValue) {
                levelList.add(level);
                level += scaleStep;
            }

            levels = new float[levelList.size()];

            for (int i = 0; i < levelList.size(); i++) {
                levels[i] = levelList.get(i);
            }
        }

        return this;
    }

    private Color getColorForLevel(float level) {
        if (level <= 0f) {
            return new Color(173, 216, 230);
        }

        if (level < scaleStep) {
            return new Color(0, 255, 0);
        }

        float t = (level - scaleStep) / (maxValue - scaleStep);
        t = Math.max(0f, Math.min(1f, t));

        return new Color(255, (int) (255 * (1 - t)), 0);
    }

    public void generate(String outputFileName) throws IOException {
        float[][] sourceGrid = this.grid;

        int targetCols = (outputWidth > 0) ? outputWidth / scale : grid[0].length;
        int targetRows = (outputHeight > 0) ? outputHeight / scale : grid.length;

        if (targetCols != grid[0].length || targetRows != grid.length) {
            sourceGrid = interpolateGrid(grid, targetCols, targetRows);
        }

        int width = targetCols * scale;
        int height = targetRows * scale;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        for (int y = 0; y < targetRows; y++) {
            for (int x = 0; x < targetCols; x++) {
                Color c = getColorForLevel(sourceGrid[y][x]);
                g.setColor(c);
                g.fillRect(x * scale, y * scale, scale, scale);
            }
        }

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(strokeWidth));

        for (float level : levels) {
            List<Line2D.Float> lines = MarchingSquares.generate(sourceGrid, level, scale);

            for (Line2D.Float line : lines) {
                g.draw(line);
            }
        }

        g.dispose();
        byte[] output = ImageUtils.readImageToByteArray(image);

        ImageUtils.writeByteArrayToJPG(width, height, output, outputFileName);
    }

    private float[][] interpolateGrid(float[][] original, int newCols, int newRows) {
        float[][] result = new float[newRows][newCols];

        int rows = original.length;
        int cols = original[0].length;

        for (int y = 0; y < newRows; y++) {
            float gy = ((float) y / (newRows - 1)) * (rows - 1);

            int y0 = (int) Math.floor(gy);
            int y1 = Math.min(y0 + 1, rows - 1);

            float dy = gy - y0;

            for (int x = 0; x < newCols; x++) {
                float gx = ((float) x / (newCols - 1)) * (cols - 1);
                int x0 = (int) Math.floor(gx);
                int x1 = Math.min(x0 + 1, cols - 1);
                float dx = gx - x0;

                float top = (1 - dx) * original[y0][x0] + dx * original[y0][x1];
                float bottom = (1 - dx) * original[y1][x0] + dx * original[y1][x1];

                result[y][x] = (1 - dy) * top + dy * bottom;
            }
        }

        return result;
    }
}
