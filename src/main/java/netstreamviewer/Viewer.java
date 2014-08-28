package netstreamviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.netstream.NetStreamReceiver;
import org.graphstream.stream.thread.ThreadProxyPipe;

/**
 * Main viewer class.
 *
 * @author Hugo Hromic
 */
public class Viewer {
    /** Default graph window title */
    public static String GRAPH_TITLE = "Live NetStream Graph";

    /** Default stylesheet filename */
    public static String STYLE_FILE = "stylesheet.css";

    /** Default properties file path */
    public static Path PROPERTIES_PATH = Paths.get("viewer.properties");

    /** Default charset for the properties file */
    public static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Main function.
     *
     * @param args program arguments
     */
    public static void main(final String[] args) {
        // Set optimized renderer
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // Read settings properties
        final Properties properties = new Properties();
        try (final BufferedReader reader = Files.newBufferedReader(PROPERTIES_PATH, DEFAULT_CHARSET)) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println(String.format("error: can't read properties file: %s", e));
            System.exit(-1);
        }

        // Initialize the live graph display
        System.err.println("Initializing live graph display ...");
        final Graph graph = new MultiGraph(GRAPH_TITLE);
        graph.setAttribute("ui.title", GRAPH_TITLE);
        graph.setAttribute("ui.stylesheet", String.format("url('%s')", STYLE_FILE));
        applyProperties(graph, properties);
        graph.display();

        // Initialize the NetStream receiver
        System.err.println("Initializing NetStream receiver ...");
        try {
            if (!properties.containsKey("network.port")) {
                System.err.println("error: the network.port property was not found.");
                System.exit(-1);
            }
            final NetStreamReceiver receiver = new NetStreamReceiver("0.0.0.0", Integer.parseInt((String)properties.get("network.port")));
            final ThreadProxyPipe pipe = receiver.getDefaultStream();
            pipe.addSink(graph);

            // Endless run loop
            System.err.println("Running.");
            while (true) {
                pipe.pump();
                Thread.sleep(100L);
            }
        } catch (NumberFormatException | IOException | InterruptedException e) {
            System.err.println(String.format("error: while receiving NetStream: %s", e));
            System.exit(-1);
        }
    }

    /**
     * Apply graph and rendering properties.
     *
     * @param graph the {@code Graph} object to apply properties to
     * @param properties {@code Properties} object to get properties from
     */
    private static void applyProperties(final Graph graph, final Properties properties) {
        // UI Quality enable/disable
        if (properties.containsKey("ui.quality") && Boolean.parseBoolean((String)properties.get("ui.quality"))) {
            graph.setAttribute("ui.quality");
        }

        // UI Antialias enable/disable
        if (properties.containsKey("ui.antialias") && Boolean.parseBoolean((String)properties.get("ui.antialias"))) {
            graph.setAttribute("ui.antialias");
        }

        // Layout Force parameter
        if (properties.containsKey("layout.force")) {
            graph.setAttribute("layout.force", Double.parseDouble((String)properties.get("layout.force")));
        }

        // Layout Quality parameter
        if (properties.containsKey("layout.quality")) {
            graph.setAttribute("layout.quality", Integer.parseInt((String)properties.get("layout.quality")));
        }

        // Layout Stabilization Limit parameter
        if (properties.containsKey("layout.stabilization-limit")) {
            graph.setAttribute("layout.stabilization-limit", Double.parseDouble((String)properties.get("layout.stabilization-limit")));
        }
    }
}
