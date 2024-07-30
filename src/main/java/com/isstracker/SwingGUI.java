package com.isstracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.HashSet;

import javax.swing.*;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;

public class SwingGUI {
    private static JLabel messageLabel;
    private static JLabel longitudeLabel;
    private static JLabel latitudeLabel;
    private static JLabel timestampLabel;

    public static void main() throws Exception {
        ResponseParser.ISSDataTemplate issObject = ResponseParser.populateDataObject();
        JXMapViewer mapViewer = new JXMapViewer();

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        mapViewer.setTileFactory(tileFactory);
        mapViewer.setZoom(16);

        // Zoom Buttons
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");

        zoomIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mapViewer.setZoom(mapViewer.getZoom() - 1);
            }
        });

        zoomOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mapViewer.setZoom(mapViewer.getZoom() + 1);
            }
        });

        // Zoom Button Styles
        zoomIn.setFocusPainted(false);
        zoomOut.setFocusPainted(false);

        zoomIn.setContentAreaFilled(false);
        zoomOut.setContentAreaFilled(false);

        tileFactory.setThreadPoolSize(8);

        // Initial ISS Waypoint
        final GeoPosition initialISSPosition = new GeoPosition(issObject.latitude, issObject.longitude);
        Waypoint initialWaypoints = new Waypoint() {
            @Override
            public GeoPosition getPosition() {
                return initialISSPosition;
            }
        };

        // Initialize Custom Renderer
        CustomWaypointRenderer customRenderer = new CustomWaypointRenderer(initialISSPosition);

        // Create and Set Up the Waypoint Painter
        @SuppressWarnings({ "rawtypes", "unchecked" })
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter();
        waypointPainter.setRenderer(customRenderer);

        // Set of Points to be Displayed
        Set<Waypoint> waypoints = new HashSet<>();
        waypoints.add(initialWaypoints);

        // Set the Waypoints on the Waypoint Painter
        waypointPainter.setWaypoints(waypoints);

        // Add Waypoint Painter to the Map Viewer
        mapViewer.setOverlayPainter(waypointPainter);
        mapViewer.setCenterPosition(initialISSPosition);

        messageLabel = new JLabel("      Message: " + issObject.message);
        longitudeLabel = new JLabel("      Longitude: " + issObject.longitude);
        latitudeLabel = new JLabel("       Latitude: " + issObject.latitude);
        timestampLabel = new JLabel("      Timestamp: " + issObject.timestamp);
        
        JPanel statusBar = new JPanel(new FlowLayout());
        statusBar.add(zoomIn);
        statusBar.add(zoomOut);
        statusBar.add(messageLabel);
        statusBar.add(longitudeLabel);
        statusBar.add(latitudeLabel);
        statusBar.add(timestampLabel);
        
        JFrame frame = new JFrame("ISS Tracker");
        frame.getContentPane().add(mapViewer, BorderLayout.CENTER);
        frame.getContentPane().add(statusBar, BorderLayout.NORTH);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        

        Timer updateISS = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    waypoints.removeAll(waypoints);    
                    ResponseParser.ISSDataTemplate updatedISSObject = ResponseParser.populateDataObject();

                    messageLabel.setText("      Message: " + updatedISSObject.message);
                    longitudeLabel.setText("      Longitude: " + updatedISSObject.longitude);
                    latitudeLabel.setText("       Latitude: " + updatedISSObject.latitude);
                    timestampLabel.setText("      Timestamp: " + updatedISSObject.timestamp);
                    
                    GeoPosition updatedISSPosition = new GeoPosition(updatedISSObject.latitude, updatedISSObject.longitude);
                    Waypoint updatedWaypoints = new Waypoint() {
                        @Override
                        public GeoPosition getPosition() {
                            return updatedISSPosition;
                        }
                    };
                    waypoints.add(updatedWaypoints);
                    waypointPainter.setWaypoints(waypoints);
                    mapViewer.setOverlayPainter(waypointPainter);
                    mapViewer.setCenterPosition(updatedISSPosition);

                    mapViewer.repaint();
                    mapViewer.revalidate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        updateISS.start();
    }
}
