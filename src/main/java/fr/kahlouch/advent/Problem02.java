package fr.kahlouch.advent;

public class Problem02 extends Problem {

    @Override
    public void setupData() {

    }

    @Override
    public Object rule1() {
        Point3D pos = new Point3D(0, 0, 0);
        for (var str : this.input) {
            pos = pos.add(Point3D.fromStr(str));
        }
        return pos.x * pos.z;
    }

    @Override
    public Object rule2() {
        Point3D pos = new Point3D(0, 0, 0);
        int aim = 0;
        for (var str : this.input) {
            var command = Point3D.fromStr(str);
            aim += command.z;
            var val = command.x;
            pos = pos.add(new Point3D(val, 0, val * aim));
        }
        return pos.x * pos.z;
    }

    record Point3D(int x, int y, int z) {
        static Point3D fromStr(String str) {
            String command = str.split(" ")[0];
            var val = Integer.parseInt(str.split(" ")[1]);
            return switch (command) {
                case "forward" -> new Point3D(val, 0, 0);
                case "down" -> new Point3D(0, 0, val);
                case "up" -> new Point3D(0, 0, -val);
                default -> {
                    System.err.println(command);
                    yield null;
                }
            };
        }

        Point3D add(Point3D point3D) {
            return new Point3D(this.x + point3D.x, this.y + point3D.y, this.z + point3D.z);
        }
    }
}
