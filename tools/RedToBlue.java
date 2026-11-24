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
 * x and y are rounded to the nearest 1/4 inch (headings are not rounded).
 * The same math also goes Blue -> Red.
 *
 * This is the same result as PoseFactory.degrees().mirrorAroundPoint(70.75, 70.75), which
 * BaseAuto.pose() uses for Red (checked against Pedro 3.0.2). The tool does the math itself so
 * it runs with plain java and no Pedro jar. If BaseAuto ever changes how Red works, change
 * FIELD here too.
 *
 * Easiest on Windows, from the repo folder in Android Studio's terminal:
 *       .\tools\red-to-blue 9,63,0 21,63,0
 *       .\tools\red-to-blue RedFar
 * (red-to-blue.bat finds Android Studio's Java for you.)
 *
 * Run it from the repo folder (Java 11 or newer runs a single .java file directly):
 *
 *   A whole Red auto: every  of(x, y, h)  /  pose(x, y, h)  /  new Pose(x, y, h)  is converted.
 *   Give just the class name and it finds the file anywhere under TeamCode (Autos, TeleOps, ...).
 *   If two files have that name (an old copy in legacy/, say), add the folder: legacy/ShortRed
 *       java tools/RedToBlue.java RedFar
 *       java tools/RedToBlue.java legacy/ShortRed
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

    // A class name, file name or end of a path: RedFar, RedFar.java, legacy/ShortBlue, Autos\RedFar.java
    static final Pattern NAME = Pattern.compile("[A-Za-z_$][\\w$]*(?:[/\\\\][\\w$]+)*(?:\\.java)?");

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Paste Red lines, then Ctrl+D (Mac/Linux) or Ctrl+Z, Enter (Windows):");
            List<String> pasted = new ArrayList<>();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (String line; (line = in.readLine()) != null; ) pasted.add(line);
            convertLines(pasted, true);
            return;
        }

        List<String> numbers = new ArrayList<>();   // bare x,y,h arguments, converted together
        for (String arg : args) {
            if (BARE.matcher(arg).matches()) {
                numbers.add(arg);
                continue;
            }
            Path file = findFile(arg);
            if (file == null) continue;              // findFile already said what went wrong
            convertLines(numbers, false);            // keep the output in the order it was asked for
            numbers.clear();
            System.out.println("// from " + shortName(file));
            convertLines(Files.readAllLines(file), true);
        }
        convertLines(numbers, false);
    }

    /**
     * Turns an argument into a file. A real path is used as is. Otherwise a name like
     * "RedFar", "RedFar.java" or "legacy/ShortBlue" is looked up under TeamCode/src/main/java
     * (Autos, TeleOps, and so on). Returns null, after printing why, if there isn't exactly one match.
     */
    static Path findFile(String arg) throws IOException {
        try {
            Path direct = Paths.get(arg);
            if (Files.isRegularFile(direct)) return direct;
        } catch (java.nio.file.InvalidPathException e) {
            // not a usable path on this computer; try it as a name below
        }
        if (!NAME.matcher(arg).matches()) {
            System.err.println("Skipping \"" + arg + "\": not x,y,heading, a file, or a class name.");
            return null;
        }

        Path root = codeRoot();
        // "legacy\ShortBlue.java" -> "legacy/ShortBlue.java", so it matches on any computer
        String wanted = arg.replace('\\', '/');
        if (!wanted.endsWith(".java")) wanted += ".java";
        final String suffix = "/" + wanted;

        List<Path> exact = new ArrayList<>();
        List<Path> ignoringCase = new ArrayList<>();
        try (java.util.stream.Stream<Path> files = Files.walk(root)) {
            for (Path p : (Iterable<Path>) files::iterator) {
                String path = "/" + p.toString().replace('\\', '/');
                if (path.contains("/build/") || !Files.isRegularFile(p)) continue;
                if (path.endsWith(suffix)) exact.add(p);
                else if (path.toLowerCase().endsWith(suffix.toLowerCase())) ignoringCase.add(p);
            }
        }
        List<Path> matches = exact.isEmpty() ? ignoringCase : exact;

        if (matches.size() == 1) return matches.get(0);
        if (matches.isEmpty()) {
            System.err.println("Couldn't find " + wanted + " under " + root + ".");
        } else {
            System.err.println(wanted + " matches more than one file. Add its folder, e.g. legacy/"
                    + matches.get(0).getFileName().toString().replace(".java", "") + ":");
            for (Path p : matches) System.err.println("    " + shortName(p));
        }
        return null;
    }

    /**
     * Where to look for class names: TeamCode/src/main/java in this folder or the closest folder
     * above it that has one, so it works from the repo folder or from deeper inside it.
     */
    static Path codeRoot() {
        for (Path dir = Paths.get("").toAbsolutePath(); dir != null; dir = dir.getParent()) {
            Path code = dir.resolve(Paths.get("TeamCode", "src", "main", "java"));
            if (Files.isDirectory(code)) return code;
        }
        return Paths.get("").toAbsolutePath();   // not in the repo: search here instead
    }

    /** The file's path inside TeamCode/src/main/java, e.g. org/.../Autos/legacy/RedFar.java -> Autos/legacy/RedFar.java. */
    static String shortName(Path file) {
        String path = file.toAbsolutePath().normalize().toString().replace('\\', '/');
        int teamcode = path.indexOf("/teamcode/");
        return teamcode >= 0 ? path.substring(teamcode + "/teamcode/".length()) : file.toString();
    }

    /** Converts every coordinate in these lines. Line numbers and the Blue-half check are per file. */
    static void convertLines(List<String> lines, boolean showLineNumbers) {
        if (lines.isEmpty()) return;
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
                String where = showLineNumbers ? "red line " + (i + 1) + ": " : "red ";
                System.out.println(convert(call) + "   // " + where + line.trim());
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

    /** Groups 1-3 are x, y and (maybe) heading, in degrees. x and y are rounded to the nearest 1/4 in. */
    static String convert(Matcher m) {
        double x = toQuarterInch(FIELD - Double.parseDouble(m.group(1)));
        double y = toQuarterInch(FIELD - Double.parseDouble(m.group(2)));
        if (m.group(3) == null) {
            return "pose(" + fmt(x) + ", " + fmt(y) + ", ?)";   // no heading given
        }
        double h = (Double.parseDouble(m.group(3)) + 180) % 360;
        if (h < 0) h += 360;
        return "pose(" + fmt(x) + ", " + fmt(y) + ", " + fmt(h) + ")";
    }

    /**
     * Rounds to the nearest quarter inch: 58.6096 -> 58.5, 8.5897 -> 8.5, 12.13 -> 12.25.
     * Our field measurements are only good to about an inch, so more decimals don't help.
     */
    static double toQuarterInch(double inches) {
        return Math.round(inches * 4) / 4.0;
    }

    /** 132.5 stays 132.5, 78.0 prints as 78, and floating-point noise is rounded away. */
    static String fmt(double v) {
        double r = Math.round(v * 1000) / 1000.0;
        return r == Math.rint(r) ? String.valueOf((long) r) : String.valueOf(r);
    }
}
