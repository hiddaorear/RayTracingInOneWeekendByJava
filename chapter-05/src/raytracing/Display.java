package raytracing;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Display {
    public int width;
    public int height;

    private Vec3 lower_left = new Vec3(-2.0f, -1.0f, -1.0f);
    private Vec3 horizontal = new Vec3(4.0f, 0.0f, 0.0f);
    private Vec3 vertical = new Vec3(0.0f, 2.0f, 0.0f);
    private Vec3 origin = new Vec3(0.0f, 0.0f, 0.0f);

    Hitable world;

    static String init(String title) {
        SimpleDateFormat df = new SimpleDateFormat("HH_mm_ss");
        String outputPath = "./";
        String pictureName = outputPath + title + df.format(new Date()) + ".ppm";
        return pictureName;
    }

    public Display() {
        this(200, 100, "ray_tracer");
    }

    public Display(int width, int height, String title) {
        this.width = width;
        this.height = height;

        String pictureName = Display.init(title);
        System.out.println("pictureName: " + pictureName);

        List<Hitable> objList = new ArrayList<Hitable>();
        objList.add(new Sphere(new Vec3(0.0f, 0.0f, -1.0f), 0.5f));
        objList.add(new Sphere(new Vec3(0.3f, 0.0f, -1.0f), 0.3f));
        objList.add(new Sphere(new Vec3(0.0f, -100.5f, -1.0f), 100f));
        world = new HitableList(objList);

        try {
            FileWriter fw = new FileWriter(pictureName);
            fw.write("P3\n" + width + " " + height + "\n255\n");

            for(int j = height - 1; j >= 0; j--) {
                for(int i = 0; i < width; i++) {
                    float u = (float)i/(float)width;
                    float v = (float)j/(float)height;
                    // 一条光线
                    Ray r = new Ray(origin, lower_left.Add(horizontal.Scale(u)).Add(vertical.Scale(v)));
                    Vec3 col = color(r);

                    int ir = (int)(255.59f*col.x());
                    int ig = (int)(255.59f*col.y());
                    int ib = (int)(255.59f*col.z());
                    fw.write(ir + " " + ig + " " + ib + "\n");
                }
            }

            fw.close();

        } catch (Exception e) {
            System.out.println("display 异常");
        }
    }

    public Vec3 color(Ray r) {
        HitRecord rec = new HitRecord();
        if (world.hit(r, 0.0f, Float.MAX_VALUE, rec)) {
            // 有撞击点，按照法向量代表的颜色绘制
            return new Vec3(rec.normal.x() + 1, rec.normal.y() + 1, rec.normal.z() + 1).Scale(0.5f);
        } else {
            Vec3 unit_dir = r.direction().normalize(); // 单位方向向量
            float t = 0.5f * (unit_dir.y() + 1.0f); // 原本范围为[-1, 1]，调整为[0, 1]
            // 返回背景；沿y轴线性插值，返回的颜色介于白色和蓝色之间
            return new Vec3(1.0f, 1.0f , 1.0f).Scale(1.0f - t).Add(new Vec3(0.5f, 0.7f, 1.0f).Scale(t));
        }
    }
}
