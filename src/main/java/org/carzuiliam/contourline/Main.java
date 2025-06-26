package org.carzuiliam.contourline;

import org.carzuiliam.contourline.builder.ContourLineBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ContourLineBuilder builder = new ContourLineBuilder()
                .setOutputSize(600, 600)
                .setScale(10)
                .setStrokeWidth(1)
                .loadCSV("dataset/sample_2.csv");

        builder.generate("contour_lines.jpg");
    }
}