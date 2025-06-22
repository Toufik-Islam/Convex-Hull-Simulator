# Convex Hull Algorithms Visualizer

An interactive educational tool for visualizing and understanding convex hull algorithms through step-by-step animations and real-time performance analysis.

## 📖 Overview

Convex hull algorithms are fundamental components in computational geometry with extensive applications across computer graphics, pattern recognition, image processing, and geographic information systems (GIS). This visualizer transforms abstract algorithmic concepts into tangible, interactive experiences by providing real-time visualization of algorithm execution, step-by-step animation, and performance comparison capabilities.

## ✨ Features

### Core Algorithms Implemented
- **Graham's Scan**: O(n log n) time complexity algorithm using polar angle sorting
- **Jarvis March (Gift Wrapping)**: O(nh) time complexity where h is the number of hull points
- **Monotone Chain**: O(n log n) algorithm processing points in lexicographic order
- **QuickHull**: Divide-and-conquer approach with average O(n log n) complexity
- **Chan's Algorithm**: Optimal O(n log h) algorithm combining multiple techniques

### User Interface Features
- **Interactive Point Input**: Click-to-add point functionality with mouse interaction
- **Random Point Generation**: Automated generation of random point sets for testing
- **Algorithm Selection**: Dropdown menu for choosing different convex hull algorithms
- **Animation Controls**: Play, pause, reset, and step-through animation capabilities
- **Speed Control**: Adjustable animation speed for detailed observation
- **Zoom and Pan**: Enhanced viewing capabilities for detailed analysis
- **Theme Support**: Multiple UI themes using FlatLaf library (Dark, Light, Dracula, etc.)

### Visualization Capabilities
- **Real-time Animation**: Step-by-step visualization of algorithm execution
- **Color-coded Elements**: Different colors for input points, hull points, and construction lines
- **Progress Indicators**: Visual feedback on algorithm completion status
- **Geometric Highlighting**: Emphasis on current algorithm operations and decisions
- **Anti-aliased Graphics**: High-quality rendering for smooth visual experience

### Analysis Features
- **Performance Metrics**: Real-time display of algorithm execution statistics
- **Comparison Counter**: Track and display the number of geometric comparisons
- **Time Complexity Analysis**: Theoretical and practical complexity demonstration
- **Algorithm Comparison**: Side-by-side performance analysis capabilities

## 🏗️ System Architecture

The project follows a modular, object-oriented architecture with clear separation of concerns:

```
Convex Hull Algorithms Visualizer/
├── algorithms/          # Algorithm implementations
│   ├── ConvexHullAlgorithm.java    # Abstract base class
│   ├── GrahamScan.java             # Graham's Scan implementation
│   ├── JarvisMarch.java            # Gift wrapping algorithm
│   ├── MonotoneChain.java          # Andrew's monotone chain
│   ├── QuickHull.java              # Divide-and-conquer QuickHull
│   └── ChanAlgorithm.java          # Optimal Chan's algorithm
├── setup/              # Core utilities and data structures
│   ├── Point.java                  # Fundamental point class
│   ├── PointStack.java            # Specialized stack for points
│   ├── HeapSort.java              # Efficient sorting implementation
│   ├── Line.java                  # Line segment operations
│   ├── Stack.java                 # Generic stack implementation
│   ├── Median.java                # Median finding utilities
│   └── PointCircular.java         # Circular point list
└── userinterface/      # GUI components and visualization
    ├── Visualizer.java            # Main application window
    └── AnimationArea.java         # Canvas for visualization
```

### Design Patterns
- **Strategy Pattern**: Algorithm selection through abstract base class
- **Observer Pattern**: Event-driven GUI updates and animation control
- **Template Method**: Standardized algorithm execution framework
- **Factory Pattern**: Dynamic algorithm instantiation

## 🚀 Technology Stack

- **Java SE**: Core programming language for cross-platform compatibility
- **Java Swing**: GUI framework for desktop application development
- **FlatLaf**: Modern look-and-feel library for enhanced UI aesthetics
- **Java AWT**: Low-level graphics and event handling
- **BufferedImage**: Double-buffered graphics for smooth animation

## 🎯 Educational Objectives

### Primary Goals
- **Algorithm Implementation**: Comprehensive implementation of multiple convex hull algorithms
- **Interactive Visualization**: Dynamic, user-friendly interface with animated graphics
- **Performance Analysis**: Real-time comparison and complexity analysis
- **Educational Enhancement**: Accessible learning platform for computational geometry

### Learning Outcomes
- Deep understanding of convex hull algorithm mechanics and complexity
- Practical experience in Java Swing GUI development
- Knowledge of computational geometry principles
- Experience in software architecture design and modular programming

## 🔧 Getting Started

### Prerequisites
- Java 8 or higher
- Java Development Kit (JDK) for compilation

### Running the Application
1. Navigate to the project directory
2. Compile the Java files or run the provided JAR file:
   ```bash
   java -jar Final.jar
   ```

### Usage
1. **Adding Points**: Click on the canvas to add points or use the random generation feature
2. **Algorithm Selection**: Choose an algorithm from the dropdown menu
3. **Animation**: Use the control buttons to start, pause, or reset the visualization
4. **Speed Control**: Adjust animation speed for detailed observation
5. **Analysis**: Monitor performance metrics in real-time

## 📈 Advantages

### Educational Benefits
- **Visual Learning Enhancement**: Transforms abstract concepts into intuitive visualizations
- **Step-by-Step Understanding**: Detailed observation of algorithmic decisions
- **Interactive Exploration**: Hands-on experimentation with different configurations
- **Comparative Analysis**: Direct algorithm comparison capabilities

### Technical Benefits
- **Cross-Platform Compatibility**: Runs on Windows, macOS, and Linux
- **Modular Architecture**: Easy maintenance and extension
- **Performance Optimization**: Efficient implementations with optimal complexity
- **Modern UI**: Contemporary design with customizable themes

## 🔮 Future Enhancements

### Planned Features
- **3D Convex Hull Algorithms**: Extension to three-dimensional space
- **Advanced Controls**: Backward stepping and bookmarking capabilities
- **Performance Profiling**: Detailed timing and memory analysis
- **Tutorial Integration**: Built-in guided tutorials for each algorithm
- **Web Application**: Browser-based version using modern web technologies

### Platform Extensions
- Mobile applications for iOS and Android
- Virtual Reality visualization for immersive learning
- Cloud integration for collaborative features
- RESTful API for educational platform integration

## 📝 Documentation

For detailed information about the project methodology, implementation challenges, and comprehensive analysis, refer to the complete lab report: `Lab_Report_Convex_Hull_Visualizer.tex`

## 👨‍💻 Author

**Muhommod Toufik Islam**  
Registration Number: 2021331538  
Software Development II Course  
2nd Year, 2nd Semester

## 📄 License

This project was developed as part of an academic assignment for educational purposes.

## 🤝 Contributing

This project serves as an educational tool and foundation for future enhancements in computational geometry visualization. The modular architecture supports easy addition of new algorithms and features.
