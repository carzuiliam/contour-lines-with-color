# Contour Line Visualization in Java

This project implements **Contour Line Visualization** using the **Marching Squares** algorithm in Java. It reads a 2D grid of floating-point values from a CSV file and renders **contour lines (isolines)** over a colorized background that represents scalar values. It also supports resolution-independent rendering with bilinear interpolation.

## Introduction

Contour lines, also known as isolines, are used to represent regions of equal value within a 2D scalar field — commonly used in elevation maps, fluid dynamics, and medical imaging.

This implementation uses the **Marching Squares algorithm** to trace contours and visualize them with customizable resolution, color gradients, and image size. The project is self-contained and uses only standard Java libraries.

The system supports:

- **CSV-based Input:** A CSV file where the first line defines the contour step (scale), and the remaining lines contain float values.
- **Color Mapping:**
  - Values ≤ 0: light blue (water).
  - Values > 0 and < scale: green.
  - Values ≥ scale: gradient from yellow to red (based on relative intensity).
- **Contour Generation:** Curves are drawn at each scalar level based on the contour step.
- **Bilinear Interpolation:** Grid is scaled to match the output resolution with smooth value transitions.
- **Custom Image Size and Styling:** Define output image dimensions, stroke width, and scale factor per cell.

## Usage Instructions

### Requirements

- **Java 8** or newer;
- No external dependencies (pure Java).

### Example Usage

```java
public class Main {
    public static void main(String[] args) throws IOException {
        new ContourLineBuilder()
            .setOutputSize(600, 600)
            .setScale(10)
            .setStrokeWidth(1)
            .loadCSV("dataset/sample.csv")
            .generate("contour_output.jpg");
    }
}
```

### CSV Format

The first line defines the **scale step** (used for both coloring and contour thresholds). The following lines define a rectangular grid of decimal values:

```
0.5
1.0,1.2,1.5,1.3
1.5,1.8,2.0,1.7
...
```

### Output

- **Color background** is computed from scalar values.
- **Contour lines** are overlaid in black for visibility.
- **Output image** is saved to `target/output/`.

## Project Structure

- `ContourLineBuilder.java` — Main class to load CSV, compute interpolation and draw contours.
- `MarchingSquares.java` — Contains the implementation of the Marching Squares algorithm.
- `ImageUtils.java` — Utility methods for image byte conversion and JPG output.

## License

The available source codes here are under the Apache License, version 2.0 (see the attached `LICENSE` file for more details). Any questions can be submitted to my email: carloswilldecarvalho@outlook.com.

## References

[1] W. E. Lorensen and H. E. Cline, *"Marching Cubes: A High Resolution 3D Surface Construction Algorithm"*, Proceedings of SIGGRAPH '87.

[2] P. Bourke, *"Contour Plotting Using the Marching Squares Algorithm"*, 1994. Available at: http://paulbourke.net/geometry/polygonise/.

[3] M. De Smith, M. F. Goodchild, and P. A. Longley, *"Geospatial Analysis: A Comprehensive Guide"*, 6th edition, 2020. Available at: https://www.spatialanalysisonline.com
