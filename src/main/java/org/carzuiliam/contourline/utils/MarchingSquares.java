package org.carzuiliam.contourline.utils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class MarchingSquares {

    public static List<Line2D.Float> generate(float[][] _grid, float _level, float _cellSize) {
        int rows = _grid.length;
        int cols = _grid[0].length;

        List<Line2D.Float> lines = new ArrayList<>();

        for (int y = 0; y < rows - 1; y++) {
            for (int x = 0; x < cols - 1; x++) {
                float tl = _grid[y][x];
                float tr = _grid[y][x + 1];
                float bl = _grid[y + 1][x];
                float br = _grid[y + 1][x + 1];

                int index = 0;

                if (tl > _level) {
                    index |= 1;
                }

                if (tr > _level) {
                    index |= 2;
                }

                if (br > _level) {
                    index |= 4;
                }

                if (bl > _level) {
                    index |= 8;
                }

                Point.Float[] edgePoints = new Point.Float[4];
                edgePoints[0] = interpolate(x, y, x + 1, y, tl, tr, _level, _cellSize);
                edgePoints[1] = interpolate(x + 1, y, x + 1, y + 1, tr, br, _level, _cellSize);
                edgePoints[2] = interpolate(x, y + 1, x + 1, y + 1, bl, br, _level, _cellSize);
                edgePoints[3] = interpolate(x, y, x, y + 1, tl, bl, _level, _cellSize);

                switch (index) {
                    case 1, 14 ->
                        lines.add(new Line2D.Float(edgePoints[3], edgePoints[0]));
                    case 2, 13 ->
                        lines.add(new Line2D.Float(edgePoints[0], edgePoints[1]));
                    case 3, 12 ->
                        lines.add(new Line2D.Float(edgePoints[3], edgePoints[1]));
                    case 4, 11 ->
                        lines.add(new Line2D.Float(edgePoints[1], edgePoints[2]));
                    case 5 -> {
                        lines.add(new Line2D.Float(edgePoints[0], edgePoints[1]));
                        lines.add(new Line2D.Float(edgePoints[3], edgePoints[2]));
                    }
                    case 6, 9 ->
                        lines.add(new Line2D.Float(edgePoints[0], edgePoints[2]));
                    case 7, 8 ->
                        lines.add(new Line2D.Float(edgePoints[3], edgePoints[2]));
                    case 10 -> {
                        lines.add(new Line2D.Float(edgePoints[0], edgePoints[3]));
                        lines.add(new Line2D.Float(edgePoints[1], edgePoints[2]));
                    }
                }
            }
        }

        return lines;
    }

    private static Point.Float interpolate(int x1, int y1, int x2, int y2,
                                           float v1, float v2, float level, float cellSize) {
        float t = (level - v1) / (v2 - v1 + 1e-6f);
        float px = (x1 + t * (x2 - x1)) * cellSize;
        float py = (y1 + t * (y2 - y1)) * cellSize;

        return new Point.Float(px, py);
    }
} 