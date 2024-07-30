package com.isstracker;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class CustomWaypointRenderer extends DefaultWaypointRenderer {
    private BufferedImage customIcon;

    public CustomWaypointRenderer(GeoPosition myGeoPosition) {
        super();
            try {  // Icon Created by Ben Didier from Noun Project //
                final URL imgURL = CustomWaypointRenderer.class.getResource("/icons/iss_icon.png");
                customIcon = ImageIO.read(imgURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    @Override
    public void paintWaypoint(final Graphics2D g, final JXMapViewer map, final Waypoint w) { // Add Error Handling
        if (customIcon == null)
            return;

        final Point2D point = map.getTileFactory().geoToPixel(w.getPosition(), map.getZoom());

        final int x = (int) point.getX() - customIcon.getWidth() / 2;
        final int y = (int) point.getY() - customIcon.getHeight();

        g.drawImage(customIcon, x, y, null);
    }
}
