package userinterface;

import com.formdev.flatlaf.intellijthemes.*;

/* Use 
    FlatLightLaf 
    FlatDarkLaf
    FlatIntelliJLaf 
    FlatDarculaLaf
    FlatArcIJTheme
    FlatArcOrangeIJTheme
    FlatSolarizedLightIJTheme
    FlatGitHubLightIJTheme
    FlatHiberbeeLightIJTheme

    FlatArcDarkIJTheme (popular dark theme)
    FlatArcDarkOrangeIJTheme
    FlatOneDarkIJTheme (inspired by One Dark theme)
    FlatMonokaiProIJTheme (classic Monokai Pro theme)
    FlatDraculaIJTheme (IntelliJ's Dracula theme) ***
    FlatSolarizedDarkIJTheme
*/


import javax.swing.*;
import algorithms.ChanAlgorithm;
import algorithms.ConvexHullAlgorithm;
import algorithms.GrahamScan;
import algorithms.JarvisMarch;
import algorithms.MonotoneChain;
import algorithms.QuickHull;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;

import setup.Point;

public class Visualizer extends JFrame {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatArcIJTheme());
            new Visualizer();
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
    }

    JButton visualize, random, reset, zoomIn, zoomOut, resetZoom, saveButton, loadButton, compare;
    JComboBox<String> options;
    JSlider speed;
    AnimationArea animationArea;
    JScrollPane scrollPane;
    HashSet<Point> points;
    Timer timer;
    JLabel comparisonLabel;
    JList<String> algorithmList;
    DefaultListModel<String> listModel;
    JLabel algorithmNameLabel;
    private double zoomLevel = 1.0; // 1.0 represents default zoom


    

    public Visualizer() {
        // Main Panel
        JPanel thePanel = new JPanel(new BorderLayout());
        
        // Replace the fixed size with full screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);
        // Or alternatively, use setExtendedState for maximized window:
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setTitle("Convex Hull Algorithms Visualizer");

        // Update the controls panel preferred size to match screen height
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setPreferredSize(new Dimension(300, screenSize.height));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 100, 10));

        // Controls Panel (Left Side)
        // JPanel visualizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // visualizePanel.add(visualize);
        // visualizePanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Ensure alignment within BoxLayout


        // Create Buttons and Controls
        visualize = new JButton("         Visualize         ");
        visualize.setFont(new Font("TimesRoman", Font.BOLD, 25));
        // visualize.setBorder(50);
        random = new JButton("    Random Points    ");
        random.setFont(new Font("TimesRoman", Font.BOLD, 25));
        reset = new JButton("            Reset            ");
        reset.setFont(new Font("TimesRoman", Font.BOLD, 25));
        zoomIn = new JButton("          Zoom In         ");
        zoomIn.setFont(new Font("TimesRoman", Font.BOLD, 25));
        zoomOut = new JButton("         Zoom Out       ");
        zoomOut.setFont(new Font("TimesRoman", Font.BOLD, 25));
        resetZoom = new JButton("       Reset Zoom      ");
        resetZoom.setFont(new Font("TimesRoman", Font.BOLD, 25));
        saveButton = new JButton("   Save Points (.txt)  ");
        saveButton.setFont(new Font("TimesRoman", Font.BOLD, 25));
        loadButton = new JButton("  Load Points (.txt)  ");
        loadButton.setFont(new Font("TimesRoman", Font.BOLD, 25));
        compare = new JButton("         Compare         ");
        compare.setFont(new Font("TimesRoman", Font.BOLD, 25));

        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        speed = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer

        // Add components to Controls Panel
        controlsPanel.add(optionsPanel());
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(speedSlider());        
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        // Add algorithm name label after comparison label
        algorithmNameLabel = new JLabel("");
        algorithmNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
        algorithmNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(algorithmNameLabel);
        
        controlsPanel.add(visualize);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(random);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(reset);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(zoomIn);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(zoomOut);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(resetZoom);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(saveButton);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(loadButton);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        controlsPanel.add(compare);
        controlsPanel.add(Box.createVerticalStrut(20)); // Spacer
        
        // Add comparison count label
        comparisonLabel = new JLabel("Comparisons: 0");
        comparisonLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
        comparisonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(comparisonLabel);

        // Initialize algorithm selection list
        listModel = new DefaultListModel<>();
        String[] choices = {"Chan's Algorithm", "Monotone Chain", "Quick Hull", "Graham Scan", "Jarvis March"};
        for (String choice : choices) {
            listModel.addElement(choice);
        }
        algorithmList = new JList<>(listModel);
        algorithmList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        algorithmList.setFont(new Font("TimesRoman", Font.BOLD, 16));
        algorithmList.setVisibleRowCount(4); // Show 4 items at a time
        
        // Create a scroll pane for the algorithm list
        JScrollPane algorithmScrollPane = new JScrollPane(algorithmList);
        algorithmScrollPane.setPreferredSize(new Dimension(280, 100));
        algorithmScrollPane.setMaximumSize(new Dimension(280, 100));
        algorithmScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        algorithmScrollPane.setVisible(false); // Initially hidden
        
        // Add the scroll pane to the controls panel
        controlsPanel.add(algorithmScrollPane);

        // Add Action Listeners
        ListenForButton lForButton = new ListenForButton();
        visualize.addActionListener(lForButton);
        random.addActionListener(lForButton);
        reset.addActionListener(lForButton);
        zoomIn.addActionListener(lForButton);
        zoomOut.addActionListener(lForButton);
        resetZoom.addActionListener(lForButton);
        saveButton.addActionListener(e -> savePointsToFile());
        loadButton.addActionListener(e -> loadPointsFromFile());
        compare.addActionListener(e -> {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            
            JLabel titleLabel = new JLabel("Select Algorithms to Compare:");
            titleLabel.setFont(new Font("TimesRoman", Font.BOLD, 16));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(10));
            
            String[] algorithms = {"Chan's Algorithm", "Monotone Chain", "Quick Hull", "Graham Scan", "Jarvis March"};
            Map<String, JCheckBox> checkBoxes = new HashMap<>();
            
            for (String alg : algorithms) {
                JCheckBox checkBox = new JCheckBox(alg);
                checkBox.setFont(new Font("TimesRoman", Font.BOLD, 14));
                checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
                checkBoxes.put(alg, checkBox);
                panel.add(checkBox);
                panel.add(Box.createVerticalStrut(5));
            }
            
            // Add select all/none buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JButton selectAll = new JButton("Select All");
            selectAll.addActionListener(evt -> checkBoxes.values().forEach(cb -> cb.setSelected(true)));
            
            JButton selectNone = new JButton("Select None");
            selectNone.addActionListener(evt -> checkBoxes.values().forEach(cb -> cb.setSelected(false)));
            
            buttonPanel.add(selectAll);
            buttonPanel.add(selectNone);
            panel.add(buttonPanel);
            
            int result = JOptionPane.showConfirmDialog(this, panel,
                "Compare Algorithms", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
            if (result == JOptionPane.OK_OPTION) {
                List<String> selectedAlgorithms = checkBoxes.entrySet().stream()
                    .filter(entry -> entry.getValue().isSelected())
                    .map(Map.Entry::getKey)
                    .collect(java.util.stream.Collectors.toList());
                    
                if (!selectedAlgorithms.isEmpty()) {
                    compareAlgorithms(selectedAlgorithms);
                } else {
                    errorMessage("Please select at least one algorithm");
                }
            }
        });

        // Animation Area (Right Side)
        points = new HashSet<>();
        animationArea = new AnimationArea(points);
        scrollPane = new JScrollPane(animationArea);

        // Create Algorithm Details Panel (Right Side)
        JPanel algorithmDetailsPanel = new JPanel();
        algorithmDetailsPanel.setLayout(new BoxLayout(algorithmDetailsPanel, BoxLayout.Y_AXIS));
        algorithmDetailsPanel.setPreferredSize(new Dimension(400, screenSize.height));
        algorithmDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add title for algorithm details
        JLabel detailsTitle = new JLabel("Algorithm Details");
        detailsTitle.setFont(new Font("TimesRoman", Font.BOLD, 25));
        detailsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        algorithmDetailsPanel.add(detailsTitle);
        algorithmDetailsPanel.add(Box.createVerticalStrut(20));

        // Add text area for pseudocode
        JTextArea pseudocodeArea = new JTextArea();
        pseudocodeArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        pseudocodeArea.setEditable(false);
        pseudocodeArea.setLineWrap(true);
        pseudocodeArea.setWrapStyleWord(true);
        pseudocodeArea.setText(getDefaultAlgorithmDetails());

        JScrollPane pseudocodeScrollPane = new JScrollPane(pseudocodeArea);
        pseudocodeScrollPane.setPreferredSize(new Dimension(380, screenSize.height - 100));
        pseudocodeScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        algorithmDetailsPanel.add(pseudocodeScrollPane);

        // Add to Main Panel
        thePanel.add(controlsPanel, BorderLayout.WEST);
        thePanel.add(scrollPane, BorderLayout.CENTER);
        thePanel.add(algorithmDetailsPanel, BorderLayout.EAST);

        // Add Main Panel to Frame
        this.add(thePanel);
        this.setVisible(true);

        // Add ComboBox listener to update algorithm details
        options.addActionListener(e -> {
            String selectedAlgorithm = (String) options.getSelectedItem();
            pseudocodeArea.setText(getAlgorithmDetails(selectedAlgorithm));
        });
    }

    private String getDefaultAlgorithmDetails() {
        return getAlgorithmDetails("Chan's Algorithm");
    }

    private String getAlgorithmDetails(String algorithm) {
        switch (algorithm) {
            case "Chan's Algorithm":
                return "Chan's Algorithm\n\n" +
                       "Time Complexity:\n" +
                       "- Best Case: O(n log h)\n" +
                       "- Average Case: O(n log h)\n" +
                       "- Worst Case: O(n log h)\n" +
                       "where n is the number of points and h is the number of points on the hull\n\n" +
                       "Implementation Steps:\n" +
                       "1. Split the points into n/m sections\n" +
                       "2. For each section, use Graham scan to find its convex hull\n" +
                       "3. Find the right tangent point from a known point (extreme point) to the sections\n" +
                       "4. Choose the tangent that would form the greatest angle\n" +
                       "5. If the number of points found is greater than m, start over with m^2\n" +
                       "   (merge the convex hull when separating into sections instead of recalculating them)\n\n" +
                       "Note: The algorithm combines Graham Scan for local hulls and Jarvis March for finding tangents";

            case "Monotone Chain":
                return "Monotone Chain Algorithm\n\n" +
                       "Time Complexity:\n" +
                       "- Best Case: O(n log n)\n" +
                       "- Average Case: O(n log n)\n" +
                       "- Worst Case: O(n log n)\n" +
                       "where n is the number of points\n\n" +
                       "Implementation Steps:\n" +
                       "1. Sort the list of points by X order (if tie, sort by Y)\n" +
                       "2. Add the first point to the solution\n" +
                       "3. Check if the second point and third point makes a left turn\n" +
                       "4. While it doesn't make a left turn, pop out the second point\n" +
                       "5. Loop through the array using steps 3 and 4 to build the lower hull\n" +
                       "6. Loop through the array in reverse order using steps 3 and 4 to build upper hull\n\n";

            case "Quick Hull":
                return "Quick Hull Algorithm\n\n" +
                       "Time Complexity:\n" +
                       "- Best Case: O(n log n)\n" +
                       "- Average Case: O(n log n)\n" +
                       "- Worst Case: O(n²)\n" +
                       "where n is the number of points\n\n" +
                       "Implementation Steps:\n" +
                       "1. Find the min and max x points\n" +
                       "2. Connect them together and find the furthest point from the line\n" +
                       "3. Connect the furthest point to the extreme points\n" +
                       "4. Find the points that are 'facing' toward the borders and furthest away from the newly formed line\n" +
                       "5. Recursively repeat steps 3 and 4\n\n";

            case "Graham Scan":
                return "Graham Scan Algorithm\n\n" +
                       "Time Complexity:\n" +
                       "- Best Case: O(n log n)\n" +
                       "- Average Case: O(n log n)\n" +
                       "- Worst Case: O(n log n)\n" +
                       "where n is the number of points\n\n" +
                       "Implementation Steps:\n" +
                       "1. Find the lowest point\n" +
                       "2. Sort the other points based on the angle it makes with the lowest point\n" +
                       "3. Push the first and second point into the stack\n" +
                       "4. Check if the third point makes a counterclockwise turn from the second point\n" +
                       "5. While it does not make a counterclockwise turn, pop out the second point\n" +
                       "6. Push the third point into the stack\n" +
                       "7. Repeat steps 4-6 to get a convex hull\n\n";

            case "Jarvis March":
                return "Jarvis March Algorithm\n\n" +
                       "Time Complexity:\n" +
                       "- Best Case: O(n)\n" +
                       "- Average Case: O(nh)\n" +
                       "- Worst Case: O(n²)\n" +
                       "where n is the number of points and h is the number of points on the hull\n\n" +
                       "Implementation Steps:\n" +
                       "1. Find the leftmost point first\n" +
                       "2. Loop through the points given to find the point that is leftmost to first\n" +
                       "3. Set the newly found point to first\n" +
                       "4. Repeat steps 2 and 3 until the newly found point is the leftmost point\n\n" +
                       "Note: Also known as the Gift Wrapping algorithm";

            default:
                return getDefaultAlgorithmDetails();
        }
    }

    private JPanel optionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT); // Ensure alignment within BoxLayout

        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(getMaximumSize());
        panel.add(new JLabel("Algorithm:"), BorderLayout.NORTH);
        String[] choices = {"Chan's Algorithm", "Monotone Chain", "Quick Hull", "Graham Scan", "Jarvis March"};
        options = new JComboBox<>(choices);
        options.setFont(new Font("TimesRoman", Font.BOLD, 20));
        panel.add(options, BorderLayout.CENTER);

        return panel;
    }

    private JSlider speedSlider() {
        speed = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
        speed.setMajorTickSpacing(50);
        speed.setPaintTicks(true);
        speed.setSnapToTicks(true);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Slow"));
        labelTable.put(100, new JLabel("Normal"));
        labelTable.put(200, new JLabel("Fast"));
        speed.setLabelTable(labelTable);
        speed.setPaintLabels(true);

        return speed;
    }

    private void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

    private class ListenForButton implements ActionListener {
        int speedVal;

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == reset) {
                // Stop any running algorithm animation
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                // Reset points and display
                points = new HashSet<>();
                animationArea.reset(points);
                animationArea.resetZoom();
                scrollPane.revalidate();
                zoomLevel = 1.0;

                // Enable all controls
                visualize.setEnabled(true);
                options.setEnabled(true);
                random.setEnabled(true);
                speed.setEnabled(true);
                zoomIn.setEnabled(true);
                zoomOut.setEnabled(true);
                resetZoom.setEnabled(true);
                saveButton.setEnabled(true);
                loadButton.setEnabled(true);
                compare.setEnabled(true);
                
                // Reset labels
                comparisonLabel.setText("Comparisons: 0");
                algorithmNameLabel.setText("");
            } else if (e.getSource() == random) {
                String input = JOptionPane.showInputDialog(Visualizer.this, "Enter number of points (5-100):", "50");
                int numPoints = 50; // Default value
                
                if (input != null) {
                    try {
                        numPoints = Integer.parseInt(input);
                        if (numPoints < 5 || numPoints > 100) {
                            errorMessage("Please enter a number between 5 and 100");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        errorMessage("Please enter a valid number");
                        return;
                    }
                }
                
                points = new HashSet<>();
                if (zoomLevel == 1.0) {
                    animationArea.random(points, numPoints);
                } else {
                    // Store current zoom level
                    double currentZoom = zoomLevel;
                    // Reset zoom before generating random points
                    animationArea.resetZoom();
                    zoomLevel = 1.0;
                    animationArea.random(points, numPoints);
                    
                    // Create a timer for smoother zoom restoration
                    Timer zoomTimer = new Timer(100, new ActionListener() { // Increased delay to 100ms
                        double targetZoom = currentZoom;
                        double currentZ = 1.0;
                        double zoomStep = 1.4; // Larger zoom step to reduce number of operations
                        
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            if (Math.abs(currentZ - targetZoom) < 0.1) { // Increased tolerance
                                ((Timer)evt.getSource()).stop();
                                // Final adjustment to get exact zoom
                                if (currentZ != targetZoom) {
                                    animationArea.zoom(targetZoom/currentZ);
                                    zoomLevel = targetZoom;
                                }
                                scrollPane.revalidate();
                                return;
                            }
                            
                            if (currentZ < targetZoom) {
                                double step = Math.min(zoomStep, targetZoom/currentZ);
                                animationArea.zoom(step);
                                zoomLevel *= step;
                                currentZ *= step;
                            } else if (currentZ > targetZoom) {
                                double step = Math.max(1/zoomStep, targetZoom/currentZ);
                                animationArea.zoom(step);
                                zoomLevel *= step;
                                currentZ *= step;
                            }
                            scrollPane.revalidate();
                        }
                    });
                    zoomTimer.start();
                }
            } else if (e.getSource() == zoomIn) {
                animationArea.zoom(1.2); // Increase zoom by 20%
                zoomLevel *= 1.2;
                scrollPane.revalidate();
            } else if (e.getSource() == zoomOut) {
                animationArea.zoom(0.8); // Decrease zoom by 20%
                zoomLevel *= 0.8;
                scrollPane.revalidate();
            } else if (e.getSource() == resetZoom) {
                animationArea.resetZoom();
                zoomLevel = 1.0;
                scrollPane.revalidate();
            } else if (e.getSource() == visualize) {
                animationArea.resetZoom();
                animationArea.reset(points);
                scrollPane.revalidate();
                speedVal = 200 - speed.getValue();
                if (points.size() < 3) {
                    errorMessage("At least 3 points is needed");
                    return;
                }

                ConvexHullAlgorithm algorithm;
                String userChoice = (String) options.getSelectedItem();
                algorithmNameLabel.setText("Running: " + userChoice);

                // convert from hashset to array
                Point[] pts = new Point[points.size()];
                pts = points.toArray(pts);
                // points = null; // get rid of the hashset points since we already have the array
                animationArea.startAnimation(); // get rid of the hashset and disable clicking

                visualize.setEnabled(false); // disable the button once the animation starts
                options.setEnabled(false);
                random.setEnabled(false);
                // reset.setEnabled(false);
                speed.setEnabled(false);
                zoomIn.setEnabled(false);
                zoomOut.setEnabled(false);
                resetZoom.setEnabled(false);
                saveButton.setEnabled(false);
                loadButton.setEnabled(false);
                compare.setEnabled(false);
                

                switch (Objects.requireNonNull(userChoice)) {
                    case "Chan's Algorithm":
                        algorithm = new ChanAlgorithm(pts);
                        break;
                    case "Monotone Chain":
                        algorithm = new MonotoneChain(pts);
                        break;
                    case "Quick Hull":
                        algorithm = new QuickHull(pts);
                        break;
                    case "Graham Scan":
                        algorithm = new GrahamScan(pts);
                        break;
                    case "Jarvis March":
                        algorithm = new JarvisMarch(pts);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + Objects.requireNonNull(userChoice));
                }
                timer = new Timer((int) (algorithm.getTime()*(speedVal/100.0)), actionEvent -> runAnimation(algorithm));
                timer.start();
             }
        }
    }
    private void runAnimation(ConvexHullAlgorithm algorithm)
    {
        assert algorithm != null : "Algorithm shouldn't be null";
        animationArea.clear();
        algorithm.draw(animationArea);
        animationArea.display();
        if (algorithm.isComplete())
        {
            timer.stop();
            reset.setEnabled(true);
            visualize.setVisible(true);
            visualize.setEnabled(true);
            options.setEnabled(true);
            random.setEnabled(true);
            speed.setEnabled(true);
            zoomIn.setEnabled(true);
            zoomOut.setEnabled(true);
            resetZoom.setEnabled(true);
            saveButton.setEnabled(true);
            loadButton.setEnabled(true);
            compare.setEnabled(true);
            algorithmNameLabel.setText(""); // Clear the algorithm name when done
            
            // Update comparison count display
            int comparisons = algorithm.getTotalComparisons();
            comparisonLabel.setText("Comparisons: " + comparisons);
            
            // Show popup with comparison count
            String algorithmName = (String) options.getSelectedItem();
            JOptionPane.showMessageDialog(this,
                algorithmName + " completed!\nTotal comparisons: " + comparisons,
                "Algorithm Complete",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void savePointsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }
            
            try (PrintWriter writer = new PrintWriter(file)) {
                for (Point p : points) {  // Use the points HashSet directly
                    writer.println(p.getX() + "," + p.getY());
                }
                JOptionPane.showMessageDialog(this, "Points saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
    }
    
    private void loadPointsFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                points = new HashSet<>();  // Create new HashSet
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] coords = line.split(",");
                    if (coords.length == 2) {
                        try {
                            int x = Integer.parseInt(coords[0].trim());
                            int y = Integer.parseInt(coords[1].trim());
                            Point newPoint = new Point(x, y);
                            points.add(newPoint);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }
                
                // Update the animation area with loaded points
                animationArea.reset(points);
                for (Point p : points) {
                    animationArea.drawPoint(p);
                }
                animationArea.display();
                
                JOptionPane.showMessageDialog(this, "Points loaded successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage());
            }
        }
    }

    private void compareAlgorithms(List<String> algorithms) {
        if (points.size() < 3) {
            errorMessage("At least 3 points is needed");
            return;
        }

        // Disable controls during comparison
        setControlsEnabled(false);

        Point[] pts = new Point[points.size()];
        pts = points.toArray(pts);
        // points = null; // get rid of the hashset points since we already have the array
        
        // Create a copy of points for each algorithm
        // Point[] pts = points.toArray(new Point[0]);
        Map<String, Integer> comparisonResults = new HashMap<>();
        
        // Queue to store algorithms to run sequentially
        Queue<String> algorithmQueue = new LinkedList<>(algorithms);
        
        // Run first algorithm
        runNextComparison(algorithmQueue, pts.clone(), comparisonResults);
    }

    private void runNextComparison(Queue<String> remainingAlgorithms, Point[] pts, Map<String, Integer> results) {
        if (remainingAlgorithms.isEmpty()) {
            showComparisonResults(results);
            setControlsEnabled(true);
            return;
        }

        String currentAlgorithm = remainingAlgorithms.poll();
        algorithmNameLabel.setText("Running: " + currentAlgorithm);
        // Update dropdown to show current algorithm
        for (int i = 0; i < options.getItemCount(); i++) {
            if (options.getItemAt(i).equals(currentAlgorithm)) {
                options.setSelectedIndex(i);
                break;
            }
        }
        ConvexHullAlgorithm algorithm = createAlgorithm(currentAlgorithm, pts.clone());
        
        animationArea.startAnimation();
        int speedVal = 200 - speed.getValue(); // Convert slider value same as visualization
        timer = new Timer((int) (algorithm.getTime()*(speedVal/100.0)), 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    animationArea.clear();
                    algorithm.draw(animationArea);
                    animationArea.display();
                    
                    if (algorithm.isComplete()) {
                        timer.stop();
                        results.put(currentAlgorithm, algorithm.getTotalComparisons());
                        int comparisons = algorithm.getTotalComparisons();
                        comparisonLabel.setText("Comparisons: " + comparisons);
                        Timer delayTimer = new Timer(1000, e2 -> 
                            runNextComparison(remainingAlgorithms, pts, results));
                        delayTimer.setRepeats(false);
                        delayTimer.start();
                    }
                }
            });
        timer.start();
    }

    private ConvexHullAlgorithm createAlgorithm(String algorithmName, Point[] pts) {
        switch (algorithmName) {
            case "Chan's Algorithm": return new ChanAlgorithm(pts);
            case "Monotone Chain": return new MonotoneChain(pts);
            case "Quick Hull": return new QuickHull(pts);
            case "Graham Scan": return new GrahamScan(pts);
            case "Jarvis March": return new JarvisMarch(pts);
            default: throw new IllegalStateException("Unknown algorithm: " + algorithmName);
        }
    }

    private void showComparisonResults(Map<String, Integer> results) {
        StringBuilder message = new StringBuilder("Comparison Results:\n\n");
        String bestAlgorithm = null;
        int minComparisons = Integer.MAX_VALUE;
        
        algorithmNameLabel.setText(""); // Clear the algorithm name when comparison is done
        
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            message.append(entry.getKey()).append(": ")
                   .append(entry.getValue()).append(" comparisons\n");
            
            if (entry.getValue() < minComparisons) {
                minComparisons = entry.getValue();
                bestAlgorithm = entry.getKey();
            }
        }
        
        message.append("\nBest performing algorithm: ")
               .append(bestAlgorithm)
               .append(" (").append(minComparisons).append(" comparisons)");
        
        JOptionPane.showMessageDialog(this, message.toString(), 
            "Algorithm Comparison Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setControlsEnabled(boolean enabled) {
        visualize.setEnabled(enabled);
        options.setEnabled(enabled);
        random.setEnabled(enabled);
        // reset.setEnabled(enabled);
        speed.setEnabled(enabled);
        zoomIn.setEnabled(enabled);
        zoomOut.setEnabled(enabled);
        resetZoom.setEnabled(enabled);
        compare.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        loadButton.setEnabled(enabled);
    }
}
