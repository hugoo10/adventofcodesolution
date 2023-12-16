package fr.kahlouch.coding._common.geometry;

public record Point2D(double x, double y) {
    public Point2D east(double amount) {
        return new Point2D(x + amount, y);
    }

    public Point2D west(double amount) {
        return new Point2D(x - amount, y);
    }

    public Point2D north(double amount) {
        return new Point2D(x, y + amount);
    }

    public Point2D south(double amount) {
        return new Point2D(x, y - amount);
    }

    public Point2D applyDirection(double amount, Direction direction) {
        return switch (direction) {
            case EAST -> east(amount);
            case WEST -> west(amount);
            case NORTH -> north(amount);
            case SOUTH -> south(amount);
        };
    }

    public Point2D applyDirection(Direction direction) {
        return applyDirection(1, direction);
    }
}
