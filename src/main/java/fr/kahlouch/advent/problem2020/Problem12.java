package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.awt.geom.Point2D;

public class Problem12 extends Problem<Double> {
    @Override
    public Double rule1() {
        Point2D pos = new Point2D.Double();
        double angleInDegree = 0;

        for (String line : input) {
            char command = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));
            switch (command) {
                case 'N' -> pos.setLocation(pos.getX(), pos.getY() + value);
                case 'S' -> pos.setLocation(pos.getX(), pos.getY() - value);
                case 'E' -> pos.setLocation(pos.getX() + value, pos.getY());
                case 'W' -> pos.setLocation(pos.getX() - value, pos.getY());
                case 'L' -> angleInDegree = (angleInDegree + value) % 360;
                case 'R' -> angleInDegree = (angleInDegree - value + 360) % 360;
                case 'F' -> {
                    double angleInRad = Math.toRadians(angleInDegree);
                    double deltaX = Math.round(Math.cos(angleInRad) * value);
                    double deltaY = Math.round(Math.sin(angleInRad) * value);
                    pos.setLocation(pos.getX() + deltaX, pos.getY() + deltaY);
                }
            }
        }

        return Math.abs(pos.getX()) + Math.abs(pos.getY());
    }

    @Override
    public Double rule2() {
        Point2D pos = new Point2D.Double();
        Point2D wp = new Point2D.Double(10, 1);
        double angleInDegree = 0;

        for (String line : input) {
            char command = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));
            switch (command) {
                case 'N' -> wp.setLocation(wp.getX(), wp.getY() + value);
                case 'S' -> wp.setLocation(wp.getX(), wp.getY() - value);
                case 'E' -> wp.setLocation(wp.getX() + value, wp.getY());
                case 'W' -> wp.setLocation(wp.getX() - value, wp.getY());
                case 'L', 'R' -> {
                    double currentDistance = wp.distance(new Point2D.Double());
                    double currentAngleInRad = Math.acos(wp.getX() / currentDistance) * (wp.getY() < 0 ? -1 : 1);
                    double currentAngleInDegree = (Math.toDegrees(currentAngleInRad) + 360) %360;
                    double newAngleInDegree = currentAngleInDegree + (command == 'L' ? 1 : -1) * value;
                    double newAngleInRad = Math.toRadians(newAngleInDegree);
                    double newX = Math.round(Math.cos(newAngleInRad) * currentDistance);
                    double newY = Math.round(Math.sin(newAngleInRad) * currentDistance);
                    wp.setLocation(newX, newY);
                }
                case 'F' -> {
                    pos.setLocation(pos.getX() + wp.getX() * value, pos.getY() + wp.getY() * value);
                }
            }
        }

        return Math.abs(pos.getX()) + Math.abs(pos.getY());
    }
}
