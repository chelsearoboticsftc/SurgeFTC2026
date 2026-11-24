import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Turns Red coordinates into Blue ones, so a Red auto can become a Blue BaseAuto subclass
 * and the Red version is just isRed() = true.
 *
 * Red and Blue are the same field turned 180° around the center (70.75, 70.75):
 *     blue x = 141.5 - red x,  blue y = 141.5 - red y,  blue heading = red heading + 180
 * The same math also goes Blue -> Red.
 *
 * This is the same result as PoseFactory.degrees().mirrorAroundPoint(70.75, 70.75), which
 * BaseAuto.pose() uses for Red (checked against Pedro 3.0.2). The tool does the math itself so
 * it runs with plain java and no Pedro jar. If BaseAuto ever changes how Red works, change
 * FIELD here too.
 *
 * Easiest on Windows, from the repo folder in Android Studio's terminal:
 *       .\tools\red-to-blue 9,63,0 21,63,0
 *       .\tools\red-to-blue TeamCode\src\main\java\org\firstinspires\ftc\teamcode\Autos\legacy\ShortRed.java
 * (red-to-blue.bat finds Android Studio's Java for you.)
 *
 * Run it from the repo folder (Java 11 or newer runs a single .java file directly):
 *
 *   A whole Red auto: every  of(x, y, h)  /  pose(x, y, h)  /  new Pose(x, y, h)  is converted
 *       java tools/RedToBlue.java TeamCode/src/main/java/org/firstinspires/ftc/teamcode/Autos/legacy/RedFar.java
 *
 *   Just numbers: x,y,heading (no spaces, or put each one in quotes)
 *       java tools/RedToBlue.java 9,63,0 21,63,0 16,96,0
 *
 *   Nothing: paste lines, then press Ctrl+D (Mac/Linux) or Ctrl+Z then Enter (Windows)
 *       java tools/RedToBlue.java
 *
 * In Android Studio's terminal on Windows (PowerShell), java usually isn't on the PATH, so
 * use the Java that comes with Android Studio. The & at the start is needed:
 *       & "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" tools\RedToBlue.java 9,63,0
 */
public class RedToBlue {
    static final double FIELD = 141.5;

    static final String NUM = "(-?\\d+(?:\\.\\d+)?)";
    // of(...), pose(...), Pose(...): 2 or 3 plain numbers. Skips anything with math in it.
    static final Pattern CALL = Pattern.compile(
            "\\b(?:of|pose|Pose)\\(\\s*" + NUM + "\\s*,\\s*" + NUM + "\\s*(?:,\\s*" + NUM + "\\s*)?\\)");
    // a bare "x, y, heading" line or argument
    static final Pattern BARE = Pattern.compile(
            "^\\s*" + NUM + "\\s*,\\s*" + NUM + "\\s*(?:,\\s*" + NUM + "\\s*)?$");

    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>();
        if (args.length == 0) {
            System.err.println("Paste Red lines, then Ctrl+D (Mac/Linux) or Ctrl+Z, Enter (Windows):");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (String line; (line = in.readLine()) != null; ) lines.add(line);
        } else {
            for (String arg : args) {
                Path p = Paths.get(arg);
                if (Files.isRegularFile(p)) {
                    System.out.println("// from " + p.getFileName());
                    lines.addAll(Files.readAllLines(p));
                } else {
                    lines.add(arg);
                }
            }
        }

        int found = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Matcher bare = BARE.matcher(line);
            if (bare.matches()) {
                if (found == 0) checkStartIsRed(bare);
                System.out.println(convert(bare) + "   // red " + line.trim());
                found++;
                continue;
            }
            Matcher call = CALL.matcher(line);
            while (call.find()) {
                if (found == 0) checkStartIsRed(call);
                System.out.println(convert(call) + "   // red line " + (i + 1) + ": " + line.trim());
                found++;
            }
        }
        if (found == 0) {
            System.err.println("No coordinates found. Expected x,y,heading or of(x, y, heading).");
        }
    }

    /**
     * Robots start on their own alliance's half, and Red is the left half (x < 70.75).
     * If the first point is on the right half, these are probably Blue coordinates already
     * (for example, a Red auto that was copied from a Blue one and never changed).
     */
    static void checkStartIsRed(Matcher first) {
        double x = Double.parseDouble(first.group(1));
        if (x > FIELD / 2) {
            System.err.println("WARNING: the first point has x = " + fmt(x) + ", on the BLUE half of the field."
                    + " Red robots start at x < " + fmt(FIELD / 2) + ", so these may already be Blue"
                    + " coordinates. Check before using the output.");
        }
    }

    /** Groups 1-3 are x, y and (maybe) heading, in degrees. */
    static String convert(Matcher m) {
        double x = FIELD - Double.parseDouble(m.group(1));
        double y = FIELD - Double.parseDouble(m.group(2));
        if (m.group(3) == null) {
            return "pose(" + fmt(x) + ", " + fmt(y) + ", ?)";   // no heading given
        }
        double h = (Double.parseDouble(m.group(3)) + 180) % 360;
        if (h < 0) h += 360;
        return "pose(" + fmt(x) + ", " + fmt(y) + ", " + fmt(h) + ")";
    }

    /** 132.5 stays 132.5, 78.0 prints as 78, and floating-point noise is rounded away. */
    static String fmt(double v) {
        double r = Math.round(v * 1000) / 1000.0;
        return r == Math.rint(r) ? String.valueOf((long) r) : String.valueOf(r);
    }
}
